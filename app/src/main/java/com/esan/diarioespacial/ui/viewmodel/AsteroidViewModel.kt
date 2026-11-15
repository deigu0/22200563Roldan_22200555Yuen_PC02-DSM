package com.esan.diarioespacial.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.esan.diarioespacial.data.local.AppDatabase
import com.esan.diarioespacial.data.local.AsteroidEntity
import com.esan.diarioespacial.data.repository.AsteroidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class AsteroidUiState(
    val asteroids: List<AsteroidEntity> = emptyList(),
    val favoriteAsteroids: List<AsteroidEntity> = emptyList(),
    val onlyHazardousFilter: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AsteroidViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AsteroidRepository

    private val _uiState = MutableStateFlow(AsteroidUiState())
    val uiState: StateFlow<AsteroidUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AsteroidRepository(database.asteroidDao())

        viewModelScope.launch {
            repository.allAsteroids.collect { list ->
                _uiState.update { it.copy(asteroids = list) }
            }
        }

        viewModelScope.launch {
            repository.favoriteAsteroids.collect { favorites ->
                _uiState.update { it.copy(favoriteAsteroids = favorites) }
            }
        }

        loadLast7DaysAsteroids()
    }

    fun loadLast7DaysAsteroids() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val calendar = Calendar.getInstance()
            val endDate = sdf.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            val startDate = sdf.format(calendar.time)

            val result = repository.fetchLast7DaysAsteroids(startDate, endDate)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
            }.onFailure { exception ->
                val message = if (exception is HttpException && exception.code() == 429) {
                    "⚠️ Límite de consultas excedido (HTTP 429). Si estás usando DEMO_KEY, registra una clave gratuita en api.nasa.gov y agrégala en local.properties (NASA_API_KEY=tu_clave)."
                } else {
                    exception.localizedMessage ?: "Error al cargar los asteroides de la API NASA"
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        }
    }

    fun setOnlyHazardousFilter(enabled: Boolean) {
        _uiState.update { it.copy(onlyHazardousFilter = enabled) }
    }

    fun toggleFavorite(asteroid: AsteroidEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(asteroid.id, asteroid.isFavorite)
        }
    }
}
