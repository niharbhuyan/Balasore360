package com.balasore360.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balasore360.data.SearchCategory
import com.balasore360.data.SearchRepository
import com.balasore360.data.SearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val category: SearchCategory = SearchCategory.ALL,
    val results: List<SearchResult> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)

class SearchViewModel(
    private val repository: SearchRepository = SearchRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query, errorMessage = null)
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(results = emptyList(), isLoading = false)
            return
        }
        searchJob = viewModelScope.launch {
            delay(300)
            executeSearch(isRefresh = false)
        }
    }

    fun selectCategory(category: SearchCategory) {
        _uiState.value = _uiState.value.copy(category = category, errorMessage = null)
        if (_uiState.value.query.isNotBlank()) {
            searchJob?.cancel()
            searchJob = viewModelScope.launch { executeSearch(isRefresh = false) }
        }
    }

    fun retry() {
        if (_uiState.value.query.isBlank()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch { executeSearch(isRefresh = false) }
    }

    fun refresh() {
        if (_uiState.value.query.isBlank()) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch { executeSearch(isRefresh = true) }
    }

    fun clear() = onQueryChange("")

    private suspend fun executeSearch(isRefresh: Boolean) {
        val current = _uiState.value
        _uiState.value = current.copy(
            isLoading = !isRefresh,
            isRefreshing = isRefresh,
            errorMessage = null
        )
        repository.search(current.query, current.category)
            .onSuccess { results ->
                _uiState.value = _uiState.value.copy(
                    results = results,
                    isLoading = false,
                    isRefreshing = false,
                    errorMessage = null
                )
            }
            .onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    errorMessage = error.message ?: "Unable to search right now."
                )
            }
    }
}
