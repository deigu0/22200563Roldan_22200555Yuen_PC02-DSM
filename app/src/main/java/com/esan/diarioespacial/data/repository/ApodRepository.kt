package com.esan.diarioespacial.data.repository

import com.esan.diarioespacial.BuildConfig
import com.esan.diarioespacial.data.api.ApodApiService
import com.esan.diarioespacial.data.local.ApodDao
import com.esan.diarioespacial.data.local.ApodEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApodRepository(private val apodDao: ApodDao) {

    private val apiService: ApodApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("https://science.nasa.gov/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApodApiService::class.java)
    }

    val allApods: Flow<List<ApodEntity>> = apodDao.getAllApods()
    val favoriteApods: Flow<List<ApodEntity>> = apodDao.getFavoriteApods()

    suspend fun fetchApods(startDate: String, endDate: String): Result<Unit> {
        return try {
            val apiKey = BuildConfig.NASA_API_KEY
            val dtoList = apiService.getApodList(apiKey, startDate, endDate)

            val entities = dtoList.mapNotNull { dto ->
                val date = dto.date ?: return@mapNotNull null
                val existingEntity = apodDao.getApodByDate(date)
                val isFavorite = existingEntity?.isFavorite ?: false

                ApodEntity(
                    date = date,
                    title = dto.title ?: "Sin título",
                    explanation = dto.explanation ?: "Sin explicación disponible",
                    mediaType = dto.mediaType ?: "image",
                    hdurl = dto.hdurl,
                    credit = dto.credit,
                    isFavorite = isFavorite
                )
            }

            if (entities.isNotEmpty()) {
                apodDao.insertApods(entities)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(date: String, currentIsFavorite: Boolean) {
        apodDao.updateFavorite(date, !currentIsFavorite)
    }
}
