package com.esan.diarioespacial.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.esan.diarioespacial.data.local.ApodEntity
import com.esan.diarioespacial.ui.viewmodel.ApodUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodListScreen(
    uiState: ApodUiState,
    onToggleFavorite: (ApodEntity) -> Unit,
    onApodClick: (ApodEntity) -> Unit,
    onOpenFavorites: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Imágenes APOD (NASA)", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(
                        onClick = onOpenFavorites,
                        modifier = Modifier.padding(end = 8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Mis favoritas")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading && uiState.apods.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Cargando imágenes de la NASA...")
                    }
                }

                uiState.apods.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "No hay imágenes astronómicas para mostrar.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRefresh) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (uiState.isLoading) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        if (uiState.errorMessage != null) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Error al actualizar: ${uiState.errorMessage}",
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.padding(8.dp),
                                    fontSize = 12.sp
                                )
                            }
                        }
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.apods, key = { it.date }) { apod ->
                                ApodItemCard(
                                    apod = apod,
                                    onToggleFavorite = { onToggleFavorite(apod) },
                                    onClick = { onApodClick(apod) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApodItemCard(
    apod: ApodEntity,
    onToggleFavorite: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            if (apod.mediaType.equals("video", ignoreCase = true)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Contenido en video",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            } else {
                if (!apod.hdurl.isNullOrBlank()) {
                    AsyncImage(
                        model = apod.hdurl,
                        contentDescription = apod.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Imagen no disponible", color = Color.White)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = apod.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Fecha: ${apod.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (apod.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorito",
                        tint = if (apod.isFavorite) Color(0xFFFFC107) else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
