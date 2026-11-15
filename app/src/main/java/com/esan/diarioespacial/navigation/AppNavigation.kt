package com.esan.diarioespacial.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.esan.diarioespacial.ui.screens.*
import com.esan.diarioespacial.ui.viewmodel.ApodViewModel
import com.esan.diarioespacial.ui.viewmodel.AsteroidViewModel

sealed class BottomNavItem(val route: String, val title: String, val icon: @Composable () -> Unit) {
    object Images : BottomNavItem("images_tab", "Imágenes", { Icon(Icons.Default.Image, contentDescription = null) })
    object Asteroids : BottomNavItem("asteroids_tab", "Asteroides", { Icon(Icons.Default.Public, contentDescription = null) })
}

@Composable
fun AppNavigation(
    apodViewModel: ApodViewModel = viewModel(),
    asteroidViewModel: AsteroidViewModel = viewModel()
) {
    val navController = rememberNavController()
    val apodUiState by apodViewModel.uiState.collectAsState()
    val asteroidUiState by asteroidViewModel.uiState.collectAsState()

    val items = listOf(
        BottomNavItem.Images,
        BottomNavItem.Asteroids
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        BottomNavItem.Images.route,
        BottomNavItem.Asteroids.route,
        "apod_list"
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    items.forEach { item ->
                        val isSelected = currentRoute == item.route || (item == BottomNavItem.Images && currentRoute == "apod_list")
                        NavigationBarItem(
                            icon = item.icon,
                            label = { Text(item.title) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Images.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- ALUMNO 1: APOD (IMÁGENES) ---
            composable(BottomNavItem.Images.route) {
                ApodListScreen(
                    uiState = apodUiState,
                    onToggleFavorite = { apodViewModel.toggleFavorite(it) },
                    onApodClick = { apod ->
                        navController.navigate("apod_detail/${apod.date}")
                    },
                    onOpenFavorites = {
                        navController.navigate("apod_favorites")
                    },
                    onRefresh = { apodViewModel.loadLast7DaysApod() }
                )
            }

            composable("apod_favorites") {
                ApodFavoritesScreen(
                    favoriteApods = apodUiState.favoriteApods,
                    onBack = { navController.popBackStack() },
                    onToggleFavorite = { apodViewModel.toggleFavorite(it) },
                    onApodClick = { apod ->
                        navController.navigate("apod_detail/${apod.date}")
                    }
                )
            }

            composable(
                route = "apod_detail/{date}",
                arguments = listOf(navArgument("date") { type = NavType.StringType })
            ) { backStackEntry ->
                val date = backStackEntry.arguments?.getString("date")
                val apod = apodUiState.apods.find { it.date == date }
                    ?: apodUiState.favoriteApods.find { it.date == date }

                ApodDetailScreen(
                    apod = apod,
                    onBack = { navController.popBackStack() },
                    onToggleFavorite = { apodViewModel.toggleFavorite(it) }
                )
            }

            // --- ALUMNO 2: NEO WS (ASTEROIDES) ---
            composable(BottomNavItem.Asteroids.route) {
                AsteroidListScreen(
                    uiState = asteroidUiState,
                    onToggleFavorite = { asteroidViewModel.toggleFavorite(it) },
                    onFilterChange = { asteroidViewModel.setOnlyHazardousFilter(it) },
                    onAsteroidClick = { asteroid ->
                        navController.navigate("asteroid_detail/${asteroid.id}")
                    },
                    onOpenFavorites = {
                        navController.navigate("asteroid_favorites")
                    },
                    onRefresh = { asteroidViewModel.loadLast7DaysAsteroids() }
                )
            }

            composable("asteroid_favorites") {
                AsteroidFavoritesScreen(
                    favoriteAsteroids = asteroidUiState.favoriteAsteroids,
                    onBack = { navController.popBackStack() },
                    onToggleFavorite = { asteroidViewModel.toggleFavorite(it) },
                    onAsteroidClick = { asteroid ->
                        navController.navigate("asteroid_detail/${asteroid.id}")
                    }
                )
            }

            composable(
                route = "asteroid_detail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                val asteroid = asteroidUiState.asteroids.find { it.id == id }
                    ?: asteroidUiState.favoriteAsteroids.find { it.id == id }

                AsteroidDetailScreen(
                    asteroid = asteroid,
                    onBack = { navController.popBackStack() },
                    onToggleFavorite = { asteroidViewModel.toggleFavorite(it) }
                )
            }
        }
    }
}
