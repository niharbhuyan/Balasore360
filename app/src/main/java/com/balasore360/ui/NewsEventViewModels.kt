package com.balasore360.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balasore360.data.Event
import com.balasore360.data.EventRepository
import com.balasore360.data.News
import com.balasore360.data.NewsRepository
import com.balasore360.data.SupabaseEventRepository
import com.balasore360.data.SupabaseNewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NewsUiState(
    val isLoading: Boolean = false,
    val news: List<News> = emptyList(),
    val errorMessage: String? = null
) {
    val isRefreshing: Boolean get() = isLoading && news.isNotEmpty()
    val items: List<News> get() = news
}

class NewsViewModel(
    private val repository: NewsRepository = SupabaseNewsRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.getPublishedNews()
                .onSuccess { items -> _uiState.value = NewsUiState(news = items) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Unable to load news"
                    )
                }
        }
    }
}

data class EventUiState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val errorMessage: String? = null
) {
    val isRefreshing: Boolean get() = isLoading && events.isNotEmpty()
    val items: List<Event> get() = events
}

class EventViewModel(
    private val repository: EventRepository = SupabaseEventRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.getPublishedEvents()
                .onSuccess { items -> _uiState.value = EventUiState(events = items) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Unable to load events"
                    )
                }
        }
    }
}
