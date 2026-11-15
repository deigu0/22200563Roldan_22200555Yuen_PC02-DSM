package com.esan.diarioespacial.data.repository

import com.esan.diarioespacial.BuildConfig
import com.esan.diarioespacial.data.api.NeoWsApiService
import com.esan.diarioespacial.data.local.AsteroidDao
import com.esan.diarioespacial.data.local.AsteroidEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AsteroidRepository(private val asteroidDao: AsteroidDao) {

    private val apiService: NeoWsApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NeoWsApiService::class.java)
    }

    val allAsteroids: Flow<List<AsteroidEntity>> = asteroidDao.getAllAsteroids()
    val favoriteAsteroids: Flow<List<AsteroidEntity>> = asteroidDao.getFavoriteAsteroids()

    suspend fun fetchLast7DaysAsteroids(startDate: String, endDate: String): Result<Unit> {
        return try {
            val apiKey = BuildConfig.NASA_API_KEY
            val response = apiService.getAsteroidFeed(apiKey, startDate, endDate)

            val nearEarthObjects = response.nearEarthObjects ?: emptyMap()
            val entities = mutableListOf<AsteroidEntity>()

            for ((dateKey, dtoList) in nearEarthObjects) {
                for (dto in dtoList) {
                    val id = dto.id ?: continue
                    val existing = asteroidDao.getAsteroidById(id)
                    val isFavorite = existing?.isFavorite ?: false

                    val approachData = dto.closeApproachData?.firstOrNull()
                    val approachDate = approachData?.closeApproachDate ?: dateKey
                    val velocityKmh = approachData?.relativeVelocity?.kilometersPerHour?.toDoubleOrNull() ?: 0.0
                    val missDistanceKm = approachData?.missDistance?.kilometers?.toDoubleOrNull() ?: 0.0

                    val minDiameter = dto.estimatedDiameter?.meters?.minMeters ?: 0.0
                    val maxDiameter = dto.estimatedDiameter?.meters?.maxMeters ?: 0.0

                    entities.add(
                        AsteroidEntity(
                            id = id,
                            name = dto.name ?: "Sin nombre",
                            approachDate = approachDate,
                            isPotentiallyHazardous = dto.isPotentiallyHazardous ?: false,
                            estimatedDiameterMinMeters = minDiameter,
                            estimatedDiameterMaxMeters = maxDiameter,
                            velocityKmh = velocityKmh,
                            missDistanceKm = missDistanceKm,
                            isFavorite = isFavorite
                        )
                    )
                }
            }

            if (entities.isNotEmpty()) {
                asteroidDao.insertAsteroids(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(id: String, currentIsFavorite: Boolean) {
        asteroidDao.updateFavorite(id, !currentIsFavorite)
    }
}
