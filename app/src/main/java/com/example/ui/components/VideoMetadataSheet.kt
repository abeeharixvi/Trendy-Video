package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.VideoItem
import com.example.ui.theme.TrendyNeonCyan
import com.example.ui.theme.TrendySurfaceCard
import com.example.ui.theme.TrendySurfaceDark
import com.example.ui.theme.TrendyTextPrimary
import com.example.ui.theme.TrendyTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoMetadataSheet(
    video: VideoItem,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TrendySurfaceDark,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier.testTag("video_metadata_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = TrendyNeonCyan,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Video Details",
                    style = MaterialTheme.typography.titleLarge,
                    color = TrendyTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = TrendySurfaceCard)
            Spacer(modifier = Modifier.height(16.dp))

            MetadataRow(icon = Icons.Default.Description, label = "Filename", value = video.displayName)
            MetadataRow(icon = Icons.Default.Schedule, label = "Duration", value = video.formattedDuration)
            MetadataRow(icon = Icons.Default.Storage, label = "File Size", value = video.formattedSize)
            MetadataRow(icon = Icons.Default.AspectRatio, label = "Resolution", value = "${video.width} × ${video.height} (${if (video.isVertical) "Vertical" else "Landscape"})")
            MetadataRow(icon = Icons.Default.Folder, label = "MIME Type", value = video.mimeType)

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Local Path: ${video.uri}",
                style = MaterialTheme.typography.bodySmall,
                color = TrendyTextSecondary
            )
        }
    }
}

@Composable
private fun MetadataRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TrendyTextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TrendyTextSecondary
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TrendyTextPrimary
        )
    }
}
