package com.esan.diarioespacial.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.esan.diarioespacial.data.local.ApodEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodFavoritesScreen(
    favoriteApods: List<ApodEntity>,
    onBack: () -> Unit,
    onToggleFavorite: (ApodEntity) -> Unit,
    onApodClick: (ApodEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Imágenes Favoritas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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
            if (favoriteApods.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No tienes imágenes agregadas a favoritos.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoriteApods, key = { it.date }) { apod ->
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
