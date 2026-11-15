package com.esan.diarioespacial.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.esan.diarioespacial.data.local.ApodEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApodDetailScreen(
    apod: ApodEntity?,
    onBack: () -> Unit,
    onToggleFavorite: (ApodEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Imagen", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    if (apod != null) {
                        IconButton(onClick = { onToggleFavorite(apod) }) {
                            Icon(
                                imageVector = if (apod.isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = "Favorito",
                                tint = if (apod.isFavorite) Color(0xFFFFC107) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (apod == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Imagen no encontrada")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                if (apod.mediaType.equals("video", ignoreCase = true)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Contenido en video",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No se incluye vista previa de video.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
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
                                .heightIn(min = 250.dp, max = 400.dp)
                                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Imagen no disponible", color = Color.White)
                        }
                    }
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = apod.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Fecha: ${apod.date}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (!apod.credit.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Crédito: ${apod.credit}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Explicación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = apod.explanation,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}
