package com.example.ui.feed

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.data.model.VideoItem
import com.example.player.TrendyPlayerManager
import com.example.player.VideoPlayerView
import com.example.ui.components.VideoMetadataSheet
import com.example.ui.theme.TrendyDeepBlack
import com.example.ui.theme.TrendyLikeRed
import com.example.ui.theme.TrendyNeonCyan
import com.example.ui.theme.TrendyNeonMagenta
import com.example.ui.theme.TrendySurfaceCard
import com.example.ui.theme.TrendySurfaceDark
import com.example.ui.theme.TrendyTextPrimary
import com.example.ui.theme.TrendyTextSecondary
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, UnstableApi::class)
@Composable
fun FeedScreen(
    viewModel: MainViewModel,
    playerManager: TrendyPlayerManager,
    onNavigateToLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val videos by viewModel.feedVideos.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val playbackState by playerManager.playbackState.collectAsStateWithLifecycle()
    val targetIndex by viewModel.targetFeedIndex.collectAsStateWithLifecycle()
    val shuffleActive by viewModel.shuffleEnabled.collectAsStateWithLifecycle()
    val autoplay by viewModel.autoplay.collectAsStateWithLifecycle()
    val loop by viewModel.loopVideos.collectAsStateWithLifecycle()
    val startMuted by viewModel.startMuted.collectAsStateWithLifecycle()
    val rememberPos by viewModel.rememberPosition.collectAsStateWithLifecycle()

    var isControlsVisible by remember { mutableStateOf(true) }
    var isFullscreen by remember { mutableStateOf(false) }
    var selectedVideoForInfo by remember { mutableStateOf<VideoItem?>(null) }
    var showDoubleTapHeart by remember { mutableStateOf(false) }

    // Keep player updated with settings
    LaunchedEffect(autoplay, loop, startMuted, rememberPos) {
        playerManager.updatePreferences(autoplay, loop, startMuted, rememberPos)
    }

    if (videos.isEmpty()) {
        EmptyFeedView(
            isScanning = isScanning,
            onScanVideos = { viewModel.refreshVideos() },
            modifier = modifier
        )
        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { videos.size }
    )

    // Handle targeted navigation from Library or Favorites
    LaunchedEffect(targetIndex) {
        targetIndex?.let { index ->
            if (index in videos.indices) {
                pagerState.scrollToPage(index)
            }
            viewModel.clearTargetFeedIndex()
        }
    }

    // React to page changes
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
            if (pageIndex in videos.indices) {
                val activeVideo = videos[pageIndex]
                playerManager.playVideo(activeVideo) { video, pos ->
                    viewModel.recordHistory(video, pos)
                }
            }
        }
    }

    // Auto-fade controls after 3.5 seconds of inactivity
    LaunchedEffect(isControlsVisible) {
        if (isControlsVisible) {
            delay(3500)
            isControlsVisible = false
        }
    }

    // Save playback position on dispose (leaving screen)
    DisposableEffect(Unit) {
        onDispose {
            playerManager.getCurrentVideo()?.let { currentVid ->
                val pos = playerManager.getCurrentPosition()
                if (pos > 0L) {
                    viewModel.recordHistory(currentVid, pos)
                }
            }
            playerManager.pause()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("feed_screen")
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { index -> videos.getOrNull(index)?.id ?: index.toLong() }
        ) { page ->
            val video = videos[page]
            val isCurrentPage = pagerState.currentPage == page

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                isControlsVisible = !isControlsVisible
                                playerManager.togglePlayPause()
                            },
                            onDoubleTap = {
                                if (!video.isFavorite) {
                                    viewModel.toggleFavorite(video)
                                }
                                scope.launch {
                                    showDoubleTapHeart = true
                                    delay(800)
                                    showDoubleTapHeart = false
                                }
                            }
                        )
                    }
            ) {
                if (isCurrentPage) {
                    // Video Player
                    if (playbackState.errorMessage == null) {
                        VideoPlayerView(
                            player = playerManager.getPlayer(),
                            modifier = Modifier.fillMaxSize(),
                            resizeMode = if (isFullscreen) {
                                AspectRatioFrameLayout.RESIZE_MODE_FIT
                            } else {
                                AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                            }
                        )
                    } else {
                        // Friendly Decode Error Card
                        VideoDecodeErrorView(
                            errorMessage = playbackState.errorMessage ?: "Unable to play this video.",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Buffering spinner
                    if (playbackState.isBuffering && playbackState.errorMessage == null) {
                        CircularProgressIndicator(
                            color = TrendyNeonMagenta,
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }

                    // Feed Controls Overlay
                    FeedControls(
                        video = video,
                        playbackState = playbackState,
                        isControlsVisible = isControlsVisible,
                        isFullscreen = isFullscreen,
                        isShuffleActive = shuffleActive,
                        onTogglePlayPause = { playerManager.togglePlayPause() },
                        onToggleFavorite = { viewModel.toggleFavorite(video) },
                        onToggleMute = { playerManager.toggleMute() },
                        onToggleFullscreen = { isFullscreen = !isFullscreen },
                        onShowMoreInfo = { selectedVideoForInfo = video },
                        onSeekTo = { pos -> playerManager.seekTo(pos) }
                    )
                } else {
                    // Off-screen black placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    )
                }
            }
        }

        // Double-Tap Heart Burst Animation
        AnimatedVisibility(
            visible = showDoubleTapHeart,
            enter = scaleIn(tween(180)) + fadeIn(),
            exit = scaleOut(tween(300)) + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Liked",
                tint = TrendyLikeRed,
                modifier = Modifier
                    .size(100.dp)
                    .scale(1.2f)
            )
        }

        // Video details bottom sheet
        selectedVideoForInfo?.let { video ->
            VideoMetadataSheet(
                video = video,
                onDismiss = { selectedVideoForInfo = null }
            )
        }
    }
}

@Composable
private fun EmptyFeedView(
    isScanning: Boolean,
    onScanVideos: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TrendyDeepBlack)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = TrendySurfaceCard,
                modifier = Modifier.size(90.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = null,
                        tint = TrendyNeonMagenta,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "TRENDY",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = TrendyTextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "No videos found on your device.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = TrendyTextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add videos or capture recordings on your phone to watch them in your private vertical feed.",
                style = MaterialTheme.typography.bodyMedium,
                color = TrendyTextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = onScanVideos,
                colors = ButtonDefaults.buttonColors(containerColor = TrendyNeonMagenta),
                shape = RoundedCornerShape(14.dp),
                enabled = !isScanning,
                modifier = Modifier
                    .height(48.dp)
                    .testTag("scan_videos_button")
            ) {
                if (isScanning) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scanning Device...")
                } else {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scan Videos")
                }
            }
        }
    }
}

@Composable
private fun VideoDecodeErrorView(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.Black)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(TrendySurfaceDark.copy(alpha = 0.85f), RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = null,
                tint = TrendyTextSecondary,
                modifier = Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TrendyTextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Swipe up or down to continue watching other videos.",
                style = MaterialTheme.typography.bodyMedium,
                color = TrendyTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
