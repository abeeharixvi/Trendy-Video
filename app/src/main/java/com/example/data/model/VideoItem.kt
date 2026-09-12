package com.example.data.model

import android.net.Uri
import java.util.Locale

/**
 * Represents a local video file discovered on the device.
 */
data class VideoItem(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val durationMs: Long,
    val sizeBytes: Long,
    val dateAddedSeconds: Long,
    val mimeType: String,
    val width: Int,
    val height: Int,
    val isFavorite: Boolean = false,
    val lastPlayedPositionMs: Long = 0L
) {
    val isVertical: Boolean
        get() = height > width

    val formattedDuration: String
        get() {
            val totalSeconds = (durationMs / 1000).coerceAtLeast(0)
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            val hours = minutes / 60
            return if (hours > 0) {
                String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes % 60, seconds)
            } else {
                String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
            }
        }

    val formattedSize: String
        get() {
            val mb = sizeBytes / (1024.0 * 1024.0)
            return if (mb >= 1024.0) {
                String.format(Locale.getDefault(), "%.1f GB", mb / 1024.0)
            } else {
                String.format(Locale.getDefault(), "%.1f MB", mb)
            }
        }

    val resolutionLabel: String
        get() = if (width > 0 && height > 0) "${width}x${height}" else "HD"
}
