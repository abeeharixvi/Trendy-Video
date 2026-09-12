package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.FavoriteVideoEntity
import com.example.data.database.HistoryVideoEntity
import com.example.data.model.VideoItem
import com.example.data.model.VideoSortOrder
import com.example.data.preferences.TrendyPreferences
import com.example.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainViewModel(
    private val repository: VideoRepository,
    private val preferences: TrendyPreferences
) : ViewModel() {

    private val _sortOrder = MutableStateFlow(VideoSortOrder.NEWEST)
    val sortOrder: StateFlow<VideoSortOrder> = _sortOrder.asStateFlow()

    private val _targetFeedIndex = MutableStateFlow<Int?>(null)
    val targetFeedIndex: StateFlow<Int?> = _targetFeedIndex.asStateFlow()

    private val _hasStoragePermission = MutableStateFlow(false)
    val hasStoragePermission: StateFlow<Boolean> = _hasStoragePermission.asStateFlow()

    val isScanning: StateFlow<Boolean> = repository.isScanning

    val autoplay: StateFlow<Boolean> = preferences.autoplay
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val loopVideos: StateFlow<Boolean> = preferences.loopVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val startMuted: StateFlow<Boolean> = preferences.startMuted
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val rememberPosition: StateFlow<Boolean> = preferences.rememberPosition
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val shuffleEnabled: StateFlow<Boolean> = preferences.shuffleEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val themeMode: StateFlow<String> = preferences.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "dark")

    val favorites: StateFlow<List<FavoriteVideoEntity>> = repository.favoriteEntities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val history: StateFlow<List<HistoryVideoEntity>> = repository.historyEntities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Sorted/filtered videos for the library list
     */
    val libraryVideos: StateFlow<List<VideoItem>> = combine(
        repository.videosWithMetadata,
        _sortOrder
    ) { videos, sort ->
        repository.sortVideos(videos, sort)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Active videos list for the feed (shuffled if shuffle is enabled, otherwise newest first)
     */
    val feedVideos: StateFlow<List<VideoItem>> = combine(
        repository.videosWithMetadata,
        shuffleEnabled
    ) { videos, isShuffled ->
        if (isShuffled && videos.isNotEmpty()) {
            // Deterministic shuffle by seed or random permutation
            videos.shuffled(Random(42))
        } else {
            videos
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updatePermissionStatus(hasPermission: Boolean) {
        _hasStoragePermission.value = hasPermission
        if (hasPermission) {
            refreshVideos()
        }
    }

    fun refreshVideos() {
        viewModelScope.launch {
            repository.refreshVideos()
        }
    }

    fun toggleFavorite(video: VideoItem) {
        viewModelScope.launch {
            repository.toggleFavorite(video)
        }
    }

    fun recordHistory(video: VideoItem, positionMs: Long) {
        viewModelScope.launch {
            repository.recordHistory(video, positionMs)
        }
    }

    fun setSortOrder(order: VideoSortOrder) {
        _sortOrder.value = order
    }

    fun jumpToFeedIndex(index: Int) {
        _targetFeedIndex.value = index
    }

    fun clearTargetFeedIndex() {
        _targetFeedIndex.value = null
    }

    fun setAutoplay(enabled: Boolean) {
        viewModelScope.launch { preferences.setAutoplay(enabled) }
    }

    fun setLoopVideos(enabled: Boolean) {
        viewModelScope.launch { preferences.setLoopVideos(enabled) }
    }

    fun setStartMuted(muted: Boolean) {
        viewModelScope.launch { preferences.setStartMuted(muted) }
    }

    fun setRememberPosition(enabled: Boolean) {
        viewModelScope.launch { preferences.setRememberPosition(enabled) }
    }

    fun setShuffleEnabled(enabled: Boolean) {
        viewModelScope.launch { preferences.setShuffleEnabled(enabled) }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch { preferences.setThemeMode(mode) }
    }

    fun clearAllHistory() {
        viewModelScope.launch { repository.clearAllHistory() }
    }

    fun clearAllFavorites() {
        viewModelScope.launch { repository.clearAllFavorites() }
    }
}
