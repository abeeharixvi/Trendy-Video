package com.example.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.TrendyDeepBlack
import com.example.ui.theme.TrendyNeonCyan
import com.example.ui.theme.TrendyNeonMagenta
import com.example.ui.theme.TrendySurfaceCard
import com.example.ui.theme.TrendySurfaceDark
import com.example.ui.theme.TrendyTextPrimary
import com.example.ui.theme.TrendyTextSecondary

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TrendyDeepBlack)
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 96.dp)
            .testTag("about_screen")
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.testTag("about_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate Back",
                    tint = TrendyTextPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "About",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TrendyTextPrimary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hero Brand Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(TrendyNeonMagenta, TrendyNeonCyan)
                        )
                    )
                    .padding(3.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_trendy_logo),
                    contentDescription = "Trendy Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "TRENDY",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = TrendyTextPrimary
            )

            Text(
                text = "Version 1.0.0",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TrendyTextSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Your Videos. Your Phone. Your Feed.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TrendyNeonCyan,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // App Overview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = TrendySurfaceDark),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "About the Application",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TrendyTextPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Trendy is an offline-first vertical video player designed in the fast-paced style of modern short-form feeds, strictly showcasing videos already saved on your Android device.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TrendyTextSecondary,
                    lineHeight = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Features Card
        Card(
            colors = CardDefaults.cardColors(containerColor = TrendySurfaceDark),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Core Features",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TrendyTextPrimary
                )

                Spacer(modifier = Modifier.height(14.dp))

                AboutFeatureRow(
                    icon = Icons.Default.OfflineBolt,
                    title = "100% Offline & Private",
                    description = "Videos remain exclusively on your phone. No network calls or cloud uploads."
                )

                Spacer(modifier = Modifier.height(12.dp))

                AboutFeatureRow(
                    icon = Icons.Default.PlayCircle,
                    title = "High-Performance Feed",
                    description = "Smooth vertical snap swiping powered by Jetpack Media3 ExoPlayer."
                )

                Spacer(modifier = Modifier.height(12.dp))

                AboutFeatureRow(
                    icon = Icons.Default.Storage,
                    title = "Local Persistence",
                    description = "Android Room database stores your liked favorites and playback history locally."
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Privacy Commitment Badge
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = TrendySurfaceDark,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(TrendyNeonCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = TrendyNeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Privacy Guarantee",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TrendyTextPrimary
                    )
                    Text(
                        text = "Zero Trackers • Zero Telemetry • Zero Ads",
                        style = MaterialTheme.typography.bodySmall,
                        color = TrendyTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Developer Information Section at bottom
        DeveloperInformationSection(
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AboutFeatureRow(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TrendyNeonCyan,
            modifier = Modifier
                .size(22.dp)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TrendyTextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = TrendyTextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}
