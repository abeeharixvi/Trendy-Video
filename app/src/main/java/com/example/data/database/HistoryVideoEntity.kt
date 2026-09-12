package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryVideoEntity(
    @PrimaryKey val videoId: Long,
    val uriString: String,
    val displayName: String,
    val lastPlayedTimestamp: Long = System.currentTimeMillis(),
    val playbackPositionMs: Long = 0L,
    val durationMs: Long = 0L
)
