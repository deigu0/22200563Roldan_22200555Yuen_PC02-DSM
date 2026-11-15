package com.esan.diarioespacial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroids ORDER BY approachDate DESC, name ASC")
    fun getAllAsteroids(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE isFavorite = 1 ORDER BY approachDate DESC, name ASC")
    fun getFavoriteAsteroids(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids WHERE id = :id LIMIT 1")
    suspend fun getAsteroidById(id: String): AsteroidEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroids(asteroids: List<AsteroidEntity>)

    @Query("UPDATE asteroids SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: String, isFavorite: Boolean)
}
