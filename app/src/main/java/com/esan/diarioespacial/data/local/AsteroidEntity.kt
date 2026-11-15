package com.esan.diarioespacial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids")
data class AsteroidEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val approachDate: String,
    val isPotentiallyHazardous: Boolean,
    val estimatedDiameterMinMeters: Double,
    val estimatedDiameterMaxMeters: Double,
    val velocityKmh: Double,
    val missDistanceKm: Double,
    val isFavorite: Boolean = false
)
