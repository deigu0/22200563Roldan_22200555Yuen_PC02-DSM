package com.esan.diarioespacial.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.esan.diarioespacial.data.local.AsteroidEntity
import com.esan.diarioespacial.ui.viewmodel.AsteroidUiState
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidListScreen(
    uiState: AsteroidUiState,
    onToggleFavorite: (AsteroidEntity) -> Unit,
    onFilterChange: (Boolean) -> Unit,
    onAsteroidClick: (AsteroidEntity) -> Unit,
    onOpenFavorites: () -> Unit,
    onRefresh: () -> Unit
) {
    val displayedAsteroids = if (uiState.onlyHazardousFilter) {
        uiState.asteroids.filter { it.isPotentiallyHazardous }
    } else {
        uiState.asteroids
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asteroides (NeoWs)", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                    IconButton(onClick = onOpenFavorites) {
                        BadgeBox(count = uiState.favoriteAsteroids.size) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Mis Asteroides Favoritos",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filter section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Solo potencialmente peligrosos",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Switch(
                        checked = uiState.onlyHazardousFilter,
                        onCheckedChange = onFilterChange
                    )
                }
            }

            // Error banner if any
            if (uiState.errorMessage != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Content
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading && uiState.asteroids.isEmpty()) {
                    CircularProgressIndicator()
                } else if (displayedAsteroids.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text(
                            text = if (uiState.onlyHazardousFilter) {
                                "No hay asteroides potencialmente peligrosos en los últimos 7 días."
                            } else {
                                "No se encontraron asteroides almacenados."
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRefresh) {
                            Text("Reintentar")
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(displayedAsteroids, key = { it.id }) { asteroid ->
                            AsteroidItemCard(
                                asteroid = asteroid,
                                onToggleFavorite = { onToggleFavorite(asteroid) },
                                onClick = { onAsteroidClick(asteroid) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AsteroidItemCard(
    asteroid: AsteroidEntity,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = asteroid.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (asteroid.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (asteroid.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (asteroid.isPotentiallyHazardous) {
                Surface(
                    color = Color(0xFFD32F2F),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "⚠️ POTENCIALMENTE PELIGROSO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "📅 Fecha aproximación: ${asteroid.approachDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "📏 Diámetro estimado máx: ${formatDecimal(asteroid.estimatedDiameterMaxMeters)} m",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "🚀 Velocidad: ${formatDecimal(asteroid.velocityKmh)} km/h",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "🌌 Distancia de paso: ${formatDecimal(asteroid.missDistanceKm)} km",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun BadgeBox(
    count: Int,
    content: @Composable () -> Unit
) {
    Box {
        content()
        if (count > 0) {
            Badge(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(text = count.toString())
            }
        }
    }
}

fun formatDecimal(value: Double): String {
    val formatter = DecimalFormat("#,##0.00")
    return formatter.format(value)
}
