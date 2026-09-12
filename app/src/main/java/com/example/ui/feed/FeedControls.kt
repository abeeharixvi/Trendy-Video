package com.example.ui.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoItem
import com.example.player.TrendyPlaybackState
import com.example.ui.theme.TrendyLikeRed
import com.example.ui.theme.TrendyNeonCyan
import com.example.ui.theme.TrendyNeonMagenta
import com.example.ui.theme.TrendySurfaceCard
import com.example.ui.theme.TrendyTextPrimary
import com.example.ui.theme.TrendyTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FeedControls(
    video: VideoItem,
    playbackState: TrendyPlaybackState,
    isControlsVisible: Boolean,
    isFullscreen: Boolean,
    isShuffleActive: Boolean,
    onTogglePlayPause: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onShowMoreInfo: () -> Unit,
    onSeekTo: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPlayPauseFeedback by remember { mutableStateOf(false) }
    var isScrubbing by remember { mutableStateOf(false) }
    var scrubPositionMs by remember { mutableStateOf(0L) }

    val heartScale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Center play/pause indicator feedback
        AnimatedVisibility(
            visible = !playbackState.isPlaying && isControlsVisible,
            enter = scaleIn(tween(150)) + fadeIn(),
            exit = scaleOut(tween(150)) + fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.55f),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Paused",
                        tint = Color.White,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
        }

        // Overlay Controls (Visible or Auto-fading)
        AnimatedVisibility(
            visible = isControlsVisible,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(300)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Top Header Gradient & Badges
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent)
                            )
                        )
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TRENDY",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = TrendyTextPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "• OFFLINE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TrendyNeonCyan
                            )
                        }

                        if (isShuffleActive) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TrendyNeonMagenta.copy(alpha = 0.2f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shuffle,
                                    contentDescription = "Shuffle Active",
                                    tint = TrendyNeonMagenta,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SHUFFLE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TrendyNeonMagenta
                                )
                            }
                        }
                    }
                }

                // Right-side Action Column
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 90.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Like / Favorite Button
                    ActionButton(
                        icon = if (video.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        label = if (video.isFavorite) "Liked" else "Like",
                        tint = if (video.isFavorite) TrendyLikeRed else Color.White,
                        onClick = {
                            scope.launch {
                                heartScale.animateTo(
                                    1.4f,
                                    animationSpec = tween(120, easing = FastOutSlowInEasing)
                                )
                                heartScale.animateTo(
                                    1.0f,
                                    animationSpec = tween(120, easing = FastOutSlowInEasing)
                                )
                            }
                            onToggleFavorite()
                        },
                        iconScale = heartScale.value,
                        testTag = "feed_like_button"
                    )

                    // Mute / Unmute Button
                    ActionButton(
                        icon = if (playbackState.isMuted) Icons.Filled.VolumeMute else Icons.Filled.VolumeUp,
                        label = if (playbackState.isMuted) "Muted" else "Sound",
                        tint = if (playbackState.isMuted) TrendyTextSecondary else TrendyNeonCyan,
                        onClick = onToggleMute,
                        testTag = "feed_mute_button"
                    )

                    // Fullscreen / Immersive Toggle
                    ActionButton(
                        icon = if (isFullscreen) Icons.Filled.FullscreenExit else Icons.Filled.Fullscreen,
                        label = if (isFullscreen) "Exit" else "Expand",
                        tint = Color.White,
                        onClick = onToggleFullscreen,
                        testTag = "feed_fullscreen_button"
                    )

                    // More Info Button
                    ActionButton(
                        icon = Icons.Filled.MoreVert,
                        label = "Info",
                        tint = Color.White,
                        onClick = onShowMoreInfo,
                        testTag = "feed_more_info_button"
                    )
                }

                // Bottom Video Information & Scrubber
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                        .padding(start = 16.dp, end = 80.dp, bottom = 68.dp)
                ) {
                    // Filename
                    Text(
                        text = video.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Metadata badge row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = video.formattedDuration,
                            style = MaterialTheme.typography.bodySmall,
                            color = TrendyTextSecondary
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = TrendyTextSecondary
                        )
                        Text(
                            text = video.formattedSize,
                            style = MaterialTheme.typography.bodySmall,
                            color = TrendyTextSecondary
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = TrendyTextSecondary
                        )
                        Text(
                            text = video.resolutionLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = TrendyNeonCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Scrubber / Progress slider
                    val currentPos = if (isScrubbing) scrubPositionMs else playbackState.currentPositionMs
                    val duration = playbackState.durationMs.coerceAtLeast(1L)
                    val progressRatio = (currentPos.toFloat() / duration).coerceIn(0f, 1f)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Slider(
                            value = progressRatio,
                            onValueChange = { newRatio ->
                                isScrubbing = true
                                scrubPositionMs = (newRatio * duration).toLong()
                            },
                            onValueChangeFinished = {
                                onSeekTo(scrubPositionMs)
                                isScrubbing = false
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = TrendyNeonMagenta,
                                activeTrackColor = TrendyNeonMagenta,
                                inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(20.dp)
                                .testTag("video_progress_slider")
                        )
                    }
                }
            }
        }

        // Minimalist non-intrusive progress line when controls are faded out
        if (!isControlsVisible) {
            LinearProgressIndicator(
                progress = { playbackState.progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .align(Alignment.BottomCenter),
                color = TrendyNeonMagenta.copy(alpha = 0.8f),
                trackColor = Color.Transparent
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit,
    iconScale: Float = 1.0f,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.45f),
            modifier = Modifier
                .size(46.dp)
                .testTag(testTag)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier
                        .size(26.dp)
                        .scale(iconScale)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
