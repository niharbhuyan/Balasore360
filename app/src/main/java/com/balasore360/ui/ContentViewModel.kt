package com.balasore360.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balasore360.data.Event
import com.balasore360.data.News
import com.balasore360.data.SupabaseEventRepository
import com.balasore360.data.SupabaseNewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ContentUiState<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

class NewsViewModel(
    private val repository: SupabaseNewsRepository = SupabaseNewsRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContentUiState<News>())
    val uiState: StateFlow<ContentUiState<News>> = _uiState.asStateFlow()

    init { load() }

    fun refresh() = load(isRefresh = true)

    private fun load(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = !isRefresh && _uiState.value.items.isEmpty(),
                isRefreshing = isRefresh,
                errorMessage = null
            )
            repository.getPublishedNews()
                .onSuccess { items -> _uiState.value = ContentUiState(items, false, false, null) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = error.message ?: "Unable to load news."
                    )
                }
        }
    }
}

class EventViewModel(
    private val repository: SupabaseEventRepository = SupabaseEventRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ContentUiState<Event>())
    val uiState: StateFlow<ContentUiState<Event>> = _uiState.asStateFlow()

    init { load() }

    fun refresh() = load(isRefresh = true)

    private fun load(isRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = !isRefresh && _uiState.value.items.isEmpty(),
                isRefreshing = isRefresh,
                errorMessage = null
            )
            repository.getPublishedEvents()
                .onSuccess { items -> _uiState.value = ContentUiState(items, false, false, null) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        errorMessage = error.message ?: "Unable to load events."
                    )
                }
        }
    }
}
