package com.example.player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.data.model.VideoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
class TrendyPlayerManager(
    private val context: Context,
    private val scope: CoroutineScope
) {

    companion object {
        private const val TAG = "TrendyPlayerManager"
    }

    private var exoPlayer: ExoPlayer? = null
    private var progressPollJob: Job? = null

    private val _playbackState = MutableStateFlow(TrendyPlaybackState())
    val playbackState: StateFlow<TrendyPlaybackState> = _playbackState.asStateFlow()

    private var currentVideo: VideoItem? = null
    private var isMutedPreference: Boolean = true
    private var loopPreference: Boolean = true
    private var autoplayPreference: Boolean = true
    private var rememberPositionPreference: Boolean = true

    init {
        initPlayer()
    }

    fun getPlayer(): ExoPlayer {
        if (exoPlayer == null) {
            initPlayer()
        }
        return exoPlayer!!
    }

    private fun initPlayer() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
            .build()

        exoPlayer = ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
            .apply {
                repeatMode = if (loopPreference) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                volume = if (isMutedPreference) 0f else 1f

                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _playbackState.value = _playbackState.value.copy(isPlaying = isPlaying)
                        if (isPlaying) {
                            startProgressPolling()
                        } else {
                            stopProgressPolling()
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_BUFFERING -> {
                                _playbackState.value = _playbackState.value.copy(
                                    isBuffering = true,
                                    errorMessage = null
                                )
                            }
                            Player.STATE_READY -> {
                                _playbackState.value = _playbackState.value.copy(
                                    isBuffering = false,
                                    errorMessage = null,
                                    durationMs = exoPlayer?.duration?.coerceAtLeast(0L) ?: 0L
                                )
                            }
                            Player.STATE_ENDED -> {
                                _playbackState.value = _playbackState.value.copy(
                                    isBuffering = false,
                                    isPlaying = false
                                )
                            }
                            Player.STATE_IDLE -> {
                                _playbackState.value = _playbackState.value.copy(
                                    isBuffering = false
                                )
                            }
                        }
                    }

                    override fun onPlayerError(error: PlaybackException) {
                        Log.e(TAG, "ExoPlayer playback error: ${error.errorCodeName}", error)
                        _playbackState.value = _playbackState.value.copy(
                            isBuffering = false,
                            isPlaying = false,
                            errorMessage = "Unable to play this video."
                        )
                    }
                })
            }
    }

    fun updatePreferences(
        autoplay: Boolean,
        loop: Boolean,
        startMuted: Boolean,
        rememberPosition: Boolean
    ) {
        this.autoplayPreference = autoplay
        this.loopPreference = loop
        this.isMutedPreference = startMuted
        this.rememberPositionPreference = rememberPosition

        exoPlayer?.let { player ->
            player.repeatMode = if (loop) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            player.volume = if (isMutedPreference) 0f else 1f
            _playbackState.value = _playbackState.value.copy(isMuted = isMutedPreference)
        }
    }

    fun playVideo(video: VideoItem, onSavePosition: ((VideoItem, Long) -> Unit)? = null) {
        // Save position of previous video before switching
        currentVideo?.let { prevVideo ->
            val prevPos = exoPlayer?.currentPosition ?: 0L
            if (prevPos > 0L) {
                onSavePosition?.invoke(prevVideo, prevPos)
            }
        }

        currentVideo = video
        _playbackState.value = _playbackState.value.copy(
            errorMessage = null,
            isBuffering = true,
            currentPositionMs = 0L,
            durationMs = video.durationMs
        )

        val player = getPlayer()
        player.stop()
        player.clearMediaItems()

        val mediaItem = MediaItem.fromUri(video.uri)
        player.setMediaItem(mediaItem)
        player.repeatMode = if (loopPreference) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        player.volume = if (isMutedPreference) 0f else 1f

        if (rememberPositionPreference && video.lastPlayedPositionMs > 0L && video.lastPlayedPositionMs < video.durationMs) {
            player.seekTo(video.lastPlayedPositionMs)
        } else {
            player.seekTo(0L)
        }

        player.prepare()
        player.playWhenReady = autoplayPreference
    }

    fun togglePlayPause() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    fun toggleMute(): Boolean {
        exoPlayer?.let { player ->
            val willBeMuted = player.volume > 0f
            player.volume = if (willBeMuted) 0f else 1f
            isMutedPreference = willBeMuted
            _playbackState.value = _playbackState.value.copy(isMuted = willBeMuted)
            return willBeMuted
        }
        return isMutedPreference
    }

    fun setMuted(muted: Boolean) {
        isMutedPreference = muted
        exoPlayer?.volume = if (muted) 0f else 1f
        _playbackState.value = _playbackState.value.copy(isMuted = muted)
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _playbackState.value = _playbackState.value.copy(currentPositionMs = positionMs)
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun resume() {
        if (autoplayPreference) {
            exoPlayer?.play()
        }
    }

    fun getCurrentPosition(): Long = exoPlayer?.currentPosition ?: 0L

    fun getCurrentVideo(): VideoItem? = currentVideo

    private fun startProgressPolling() {
        stopProgressPolling()
        progressPollJob = scope.launch(Dispatchers.Main) {
            while (isActive) {
                exoPlayer?.let { player ->
                    val pos = player.currentPosition.coerceAtLeast(0L)
                    val dur = player.duration.coerceAtLeast(0L)
                    _playbackState.value = _playbackState.value.copy(
                        currentPositionMs = pos,
                        durationMs = if (dur > 0) dur else _playbackState.value.durationMs
                    )
                }
                delay(200)
            }
        }
    }

    private fun stopProgressPolling() {
        progressPollJob?.cancel()
        progressPollJob = null
    }

    fun release() {
        stopProgressPolling()
        exoPlayer?.release()
        exoPlayer = null
    }
}
