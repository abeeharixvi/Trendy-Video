package com.example.data.repository

import android.net.Uri
import com.example.data.database.FavoriteVideoEntity
import com.example.data.database.HistoryVideoEntity
import com.example.data.database.TrendyDao
import com.example.data.media.MediaStoreScanner
import com.example.data.model.VideoItem
import com.example.data.model.VideoSortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class VideoRepository(
    private val mediaStoreScanner: MediaStoreScanner,
    private val trendyDao: TrendyDao
) {

    private val _rawVideos = MutableStateFlow<List<VideoItem>>(emptyList())
    val rawVideos: StateFlow<List<VideoItem>> = _rawVideos.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    val favoriteEntities: Flow<List<FavoriteVideoEntity>> = trendyDao.getAllFavorites()
    val historyEntities: Flow<List<HistoryVideoEntity>> = trendyDao.getAllHistory()

    /**
     * Combined Flow of videos with current favorite status and playback position updated.
     */
    val videosWithMetadata: Flow<List<VideoItem>> = combine(
        _rawVideos,
        trendyDao.getAllFavorites(),
        trendyDao.getAllHistory()
    ) { videos, favorites, history ->
        val favoriteIds = favorites.map { it.videoId }.toSet()
        val historyMap = history.associate { it.videoId to it.playbackPositionMs }

        videos.map { video ->
            video.copy(
                isFavorite = favoriteIds.contains(video.id),
                lastPlayedPositionMs = historyMap[video.id] ?: 0L
            )
        }
    }

    suspend fun refreshVideos() = withContext(Dispatchers.IO) {
        _isScanning.value = true
        try {
            val scanned = mediaStoreScanner.scanLocalVideos()
            _rawVideos.value = scanned

            // Prune any favorites or history for files deleted from device
            val validIds = scanned.map { it.id }
            if (validIds.isNotEmpty()) {
                trendyDao.pruneMissingFavorites(validIds)
                trendyDao.pruneMissingHistory(validIds)
            }
        } finally {
            _isScanning.value = false
        }
    }

    suspend fun toggleFavorite(video: VideoItem) = withContext(Dispatchers.IO) {
        if (video.isFavorite) {
            trendyDao.deleteFavorite(video.id)
        } else {
            trendyDao.insertFavorite(
                FavoriteVideoEntity(
                    videoId = video.id,
                    uriString = video.uri.toString(),
                    displayName = video.displayName,
                    durationMs = video.durationMs,
                    sizeBytes = video.sizeBytes,
                    addedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun recordHistory(video: VideoItem, positionMs: Long) = withContext(Dispatchers.IO) {
        trendyDao.upsertHistory(
            HistoryVideoEntity(
                videoId = video.id,
                uriString = video.uri.toString(),
                displayName = video.displayName,
                lastPlayedTimestamp = System.currentTimeMillis(),
                playbackPositionMs = positionMs,
                durationMs = video.durationMs
            )
        )
    }

    suspend fun getSavedPosition(videoId: Long): Long = withContext(Dispatchers.IO) {
        trendyDao.getPlaybackPosition(videoId) ?: 0L
    }

    suspend fun clearAllHistory() = withContext(Dispatchers.IO) {
        trendyDao.clearAllHistory()
    }

    suspend fun clearAllFavorites() = withContext(Dispatchers.IO) {
        trendyDao.clearAllFavorites()
    }

    fun sortVideos(videos: List<VideoItem>, sortOrder: VideoSortOrder): List<VideoItem> {
        return when (sortOrder) {
            VideoSortOrder.NEWEST -> videos.sortedByDescending { it.dateAddedSeconds }
            VideoSortOrder.OLDEST -> videos.sortedBy { it.dateAddedSeconds }
            VideoSortOrder.NAME_ASC -> videos.sortedBy { it.displayName.lowercase() }
            VideoSortOrder.DURATION_DESC -> videos.sortedByDescending { it.durationMs }
            VideoSortOrder.DURATION_ASC -> videos.sortedBy { it.durationMs }
        }
    }
}
