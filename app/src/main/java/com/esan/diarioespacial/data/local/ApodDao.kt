package com.esan.diarioespacial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ApodDao {
    @Query("SELECT * FROM apod_images ORDER BY date DESC")
    fun getAllApods(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod_images WHERE isFavorite = 1 ORDER BY date DESC")
    fun getFavoriteApods(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod_images WHERE date = :date LIMIT 1")
    suspend fun getApodByDate(date: String): ApodEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApods(apods: List<ApodEntity>)

    @Query("UPDATE apod_images SET isFavorite = :isFavorite WHERE date = :date")
    suspend fun updateFavorite(date: String, isFavorite: Boolean)
}
