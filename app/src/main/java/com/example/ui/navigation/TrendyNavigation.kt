package com.example.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.player.TrendyPlayerManager
import com.example.ui.about.AboutScreen
import com.example.ui.components.TrendyBottomBar
import com.example.ui.favorites.FavoritesScreen
import com.example.ui.feed.FeedScreen
import com.example.ui.library.LibraryScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.viewmodel.MainViewModel

@Composable
fun TrendyAppRoot(
    viewModel: MainViewModel,
    playerManager: TrendyPlayerManager,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: TrendyDestination.Feed.route
    val isFeedScreen = currentRoute == TrendyDestination.Feed.route

    Scaffold(
        bottomBar = {
            TrendyBottomBar(
                currentRoute = currentRoute,
                onNavigateToDestination = { destination ->
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                isFeedScreen = isFeedScreen
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (isFeedScreen) 0.dp else innerPadding.calculateBottomPadding())
        ) {
            NavHost(
                navController = navController,
                startDestination = TrendyDestination.Feed.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(TrendyDestination.Feed.route) {
                    FeedScreen(
                        viewModel = viewModel,
                        playerManager = playerManager,
                        onNavigateToLibrary = {
                            navController.navigate(TrendyDestination.Library.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(TrendyDestination.Library.route) {
                    LibraryScreen(
                        viewModel = viewModel,
                        onVideoSelected = {
                            navController.navigate(TrendyDestination.Feed.route) {
                                popUpTo(TrendyDestination.Feed.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(TrendyDestination.Favorites.route) {
                    FavoritesScreen(
                        viewModel = viewModel,
                        onVideoSelected = {
                            navController.navigate(TrendyDestination.Feed.route) {
                                popUpTo(TrendyDestination.Feed.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        onNavigateToFeed = {
                            navController.navigate(TrendyDestination.Feed.route) {
                                popUpTo(TrendyDestination.Feed.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(TrendyDestination.Settings.route) {
                    SettingsScreen(
                        viewModel = viewModel,
                        onNavigateToAbout = {
                            navController.navigate(TrendyDestination.About.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable(TrendyDestination.About.route) {
                    AboutScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
