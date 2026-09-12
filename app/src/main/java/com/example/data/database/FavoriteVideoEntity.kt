package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteVideoEntity(
    @PrimaryKey val videoId: Long,
    val uriString: String,
    val displayName: String,
    val durationMs: Long,
    val sizeBytes: Long,
    val addedAt: Long = System.currentTimeMillis()
)
