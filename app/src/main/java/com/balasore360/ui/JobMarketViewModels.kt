package com.balasore360.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balasore360.data.Job
import com.balasore360.data.JobRepository
import com.balasore360.data.MarketItem
import com.balasore360.data.MarketRepository
import com.balasore360.data.SupabaseJobRepository
import com.balasore360.data.SupabaseMarketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class JobUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val jobs: List<Job> = emptyList(),
    val errorMessage: String? = null
)

class JobViewModel(private val repository: JobRepository = SupabaseJobRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(JobUiState())
    val uiState: StateFlow<JobUiState> = _uiState.asStateFlow()

    init { load(initial = true) }

    fun refresh() = load(initial = false)

    private fun load(initial: Boolean) {
        viewModelScope.launch {
            val current = _uiState.value
            _uiState.value = current.copy(
                isLoading = initial && current.jobs.isEmpty(),
                isRefreshing = !initial,
                errorMessage = null
            )
            repository.getPublishedJobs()
                .onSuccess { items -> _uiState.value = JobUiState(jobs = items) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = error.message ?: "Unable to load jobs"
                    )
                }
        }
    }
}

data class MarketUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val items: List<MarketItem> = emptyList(),
    val errorMessage: String? = null
)

class MarketViewModel(private val repository: MarketRepository = SupabaseMarketRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    init { load(initial = true) }

    fun refresh() = load(initial = false)

    private fun load(initial: Boolean) {
        viewModelScope.launch {
            val current = _uiState.value
            _uiState.value = current.copy(
                isLoading = initial && current.items.isEmpty(),
                isRefreshing = !initial,
                errorMessage = null
            )
            repository.getMarketItems()
                .onSuccess { items -> _uiState.value = MarketUiState(items = items) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = error.message ?: "Unable to load market prices"
                    )
                }
        }
    }
}
