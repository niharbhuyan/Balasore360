package com.balasore360.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.balasore360.data.Business
import com.balasore360.data.BusinessRepository
import com.balasore360.data.SupabaseBusinessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BusinessUiState(
    val isLoading: Boolean = false,
    val businesses: List<Business> = emptyList(),
    val errorMessage: String? = null
)

class BusinessViewModel(
    private val repository: BusinessRepository = SupabaseBusinessRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(BusinessUiState())
    val uiState: StateFlow<BusinessUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            repository.getPublishedBusinesses()
                .onSuccess { businesses ->
                    _uiState.value = BusinessUiState(businesses = businesses)
                }
                .onFailure { error ->
                    _uiState.value = BusinessUiState(errorMessage = error.message ?: "Unable to load businesses")
                }
        }
    }
}
