package com.esan.diarioespacial.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.esan.diarioespacial.data.local.ApodEntity
import com.esan.diarioespacial.data.local.AppDatabase
import com.esan.diarioespacial.data.repository.ApodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ApodUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val apods: List<ApodEntity> = emptyList(),
    val favoriteApods: List<ApodEntity> = emptyList()
)

class ApodViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ApodRepository

    private val _uiState = MutableStateFlow(ApodUiState(isLoading = true))
    val uiState: StateFlow<ApodUiState> = _uiState.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ApodRepository(database.apodDao())

        viewModelScope.launch {
            combine(repository.allApods, repository.favoriteApods) { apods, favorites ->
                Pair(apods, favorites)
            }.collect { (apods, favorites) ->
                _uiState.value = _uiState.value.copy(
                    apods = apods,
                    favoriteApods = favorites
                )
            }
        }

        loadLast7DaysApod()
    }

    fun loadLast7DaysApod() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val today = LocalDate.now()
            val sixDaysAgo = today.minusDays(6)

            val startDate = sixDaysAgo.format(formatter)
            val endDate = today.format(formatter)

            val result = repository.fetchApods(startDate, endDate)
            result.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = error.localizedMessage ?: "Error al cargar datos de la API NASA"
                )
            }.onSuccess {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun toggleFavorite(apod: ApodEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(apod.date, apod.isFavorite)
        }
    }
}
