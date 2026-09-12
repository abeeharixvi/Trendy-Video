package com.example.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.ui.about.DeveloperInformationSection
import com.example.ui.theme.TrendyDeepBlack
import com.example.ui.theme.TrendyLikeRed
import com.example.ui.theme.TrendyNeonCyan
import com.example.ui.theme.TrendyNeonMagenta
import com.example.ui.theme.TrendySurfaceCard
import com.example.ui.theme.TrendySurfaceDark
import com.example.ui.theme.TrendyTextPrimary
import com.example.ui.theme.TrendyTextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onNavigateToAbout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val autoplay by viewModel.autoplay.collectAsStateWithLifecycle()
    val loop by viewModel.loopVideos.collectAsStateWithLifecycle()
    val startMuted by viewModel.startMuted.collectAsStateWithLifecycle()
    val rememberPos by viewModel.rememberPosition.collectAsStateWithLifecycle()
    val shuffle by viewModel.shuffleEnabled.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showClearFavoritesDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendyDeepBlack)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 88.dp)
            .testTag("settings_screen")
    ) {
        // Header
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = TrendyTextPrimary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        // Section: Playback
        SettingsSectionHeader(title = "Playback")
        Card(
            colors = CardDefaults.cardColors(containerColor = TrendySurfaceDark),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                SettingsSwitchRow(
                    icon = Icons.Default.PlayCircle,
                    title = "Autoplay",
                    subtitle = "Automatically start video on swipe",
                    checked = autoplay,
                    onCheckedChange = { viewModel.setAutoplay(it) },
                    testTag = "switch_autoplay"
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchRow(
                    icon = Icons.Default.Loop,
                    title = "Loop Videos",
                    subtitle = "Repeat active video indefinitely",
                    checked = loop,
                    onCheckedChange = { viewModel.setLoopVideos(it) },
                    testTag = "switch_loop"
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchRow(
                    icon = Icons.Default.VolumeMute,
                    title = "Start Muted",
                    subtitle = "Play videos muted until sound is unmuted",
                    checked = startMuted,
                    onCheckedChange = { viewModel.setStartMuted(it) },
                    testTag = "switch_muted"
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchRow(
                    icon = Icons.Default.History,
                    title = "Remember Playback Position",
                    subtitle = "Resume video where you last left off",
                    checked = rememberPos,
                    onCheckedChange = { viewModel.setRememberPosition(it) },
                    testTag = "switch_remember_pos"
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsSwitchRow(
                    icon = Icons.Default.Shuffle,
                    title = "Shuffle Feed",
                    subtitle = "Play local videos in randomized order",
                    checked = shuffle,
                    onCheckedChange = { viewModel.setShuffleEnabled(it) },
                    testTag = "switch_shuffle"
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Appearance
        SettingsSectionHeader(title = "Appearance")
        Card(
            colors = CardDefaults.cardColors(containerColor = TrendySurfaceDark),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                ThemeRadioOption(
                    title = "Dark theme (Recommended)",
                    selected = themeMode == "dark",
                    onSelect = { viewModel.setThemeMode("dark") }
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                ThemeRadioOption(
                    title = "Light theme",
                    selected = themeMode == "light",
                    onSelect = { viewModel.setThemeMode("light") }
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                ThemeRadioOption(
                    title = "System default",
                    selected = themeMode == "system",
                    onSelect = { viewModel.setThemeMode("system") }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Library & Storage
        SettingsSectionHeader(title = "Library & Data")
        Card(
            colors = CardDefaults.cardColors(containerColor = TrendySurfaceDark),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                SettingsActionRow(
                    icon = Icons.Default.Refresh,
                    title = "Rescan Videos",
                    subtitle = "Scan MediaStore for new, renamed, or removed files",
                    onClick = { viewModel.refreshVideos() },
                    tint = TrendyNeonCyan,
                    testTag = "settings_rescan"
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsActionRow(
                    icon = Icons.Default.DeleteSweep,
                    title = "Clear Watch History",
                    subtitle = "Reset all saved video playback positions",
                    onClick = { showClearHistoryDialog = true },
                    tint = TrendyTextSecondary,
                    testTag = "settings_clear_history"
                )
                HorizontalDivider(color = TrendySurfaceCard, modifier = Modifier.padding(horizontal = 16.dp))
                SettingsActionRow(
                    icon = Icons.Default.DeleteSweep,
                    title = "Clear Favorites",
                    subtitle = "Remove all videos from your favorites list",
                    onClick = { showClearFavoritesDialog = true },
                    tint = TrendyLikeRed,
                    testTag = "settings_clear_favorites"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: About
        SettingsSectionHeader(title = "About Trendy")
        Card(
            colors = CardDefaults.cardColors(containerColor = TrendySurfaceDark),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(TrendyNeonMagenta, TrendyNeonCyan)
                            )
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_trendy_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "TRENDY",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    color = TrendyTextPrimary
                )

                Text(
                    text = "Version 1.0.0",
                    fontSize = 12.sp,
                    color = TrendyTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Private local video player.",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TrendyNeonCyan
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Your videos stay on your device. Trendy does not upload your videos.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TrendyTextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(TrendySurfaceCard)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = TrendyNeonCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "100% Offline • Zero Trackers • Zero Telemetry",
                        fontSize = 11.sp,
                        color = TrendyTextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action to view dedicated About page
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TrendySurfaceCard,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onNavigateToAbout)
                        .testTag("settings_open_about")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = TrendyNeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "View Full About Page",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = TrendyNeonCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Developer Information section at bottom of About
                DeveloperInformationSection(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Confirmation dialog for clear history
    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            containerColor = TrendySurfaceDark,
            title = { Text("Clear Watch History?", color = TrendyTextPrimary) },
            text = { Text("This will reset all remembered playback positions. Your original videos will not be affected.", color = TrendyTextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllHistory()
                    showClearHistoryDialog = false
                }) {
                    Text("Clear", color = TrendyLikeRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text("Cancel", color = TrendyTextSecondary)
                }
            }
        )
    }

    // Confirmation dialog for clear favorites
    if (showClearFavoritesDialog) {
        AlertDialog(
            onDismissRequest = { showClearFavoritesDialog = false },
            containerColor = TrendySurfaceDark,
            title = { Text("Clear Favorites?", color = TrendyTextPrimary) },
            text = { Text("This will remove all videos from your favorites list. Your original files will stay safely on your device.", color = TrendyTextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllFavorites()
                    showClearFavoritesDialog = false
                }) {
                    Text("Clear All", color = TrendyLikeRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearFavoritesDialog = false }) {
                    Text("Cancel", color = TrendyTextSecondary)
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = TrendyNeonMagenta,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TrendyNeonCyan,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = TrendyTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TrendyTextSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = TrendyNeonMagenta,
                uncheckedThumbColor = TrendyTextSecondary,
                uncheckedTrackColor = TrendySurfaceCard
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}

@Composable
private fun SettingsActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    tint: Color,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = TrendyTextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TrendyTextSecondary
            )
        }
    }
}

@Composable
private fun ThemeRadioOption(
    title: String,
    selected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = TrendyNeonMagenta,
                unselectedColor = TrendyTextSecondary
            )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) TrendyTextPrimary else TrendyTextSecondary,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
