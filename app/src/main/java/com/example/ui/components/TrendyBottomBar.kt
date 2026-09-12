package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.TrendyDestination
import com.example.ui.theme.TrendyDeepBlack
import com.example.ui.theme.TrendyNeonMagenta
import com.example.ui.theme.TrendySurfaceDark
import com.example.ui.theme.TrendyTextSecondary

@Composable
fun TrendyBottomBar(
    currentRoute: String,
    onNavigateToDestination: (TrendyDestination) -> Unit,
    isFeedScreen: Boolean = false,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isFeedScreen) {
        Color.Black.copy(alpha = 0.65f)
    } else {
        TrendySurfaceDark
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isFeedScreen) {
                    Modifier.background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                } else {
                    Modifier
                }
            )
            .navigationBarsPadding()
    ) {
        NavigationBar(
            containerColor = containerColor,
            contentColor = Color.White,
            tonalElevation = if (isFeedScreen) 0.dp else 8.dp,
            modifier = Modifier
                .height(64.dp)
                .testTag("trendy_bottom_navigation")
        ) {
            TrendyDestination.bottomNavItems.forEach { destination ->
                val isSelected = currentRoute == destination.route

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigateToDestination(destination) },
                    icon = {
                        Icon(
                            imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                            contentDescription = destination.title
                        )
                    },
                    label = {
                        Text(
                            text = destination.title,
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = TrendyNeonMagenta,
                        selectedTextColor = TrendyNeonMagenta,
                        indicatorColor = TrendyNeonMagenta.copy(alpha = 0.15f),
                        unselectedIconColor = TrendyTextSecondary,
                        unselectedTextColor = TrendyTextSecondary
                    ),
                    modifier = Modifier.testTag("nav_item_${destination.route}")
                )
            }
        }
    }
}
