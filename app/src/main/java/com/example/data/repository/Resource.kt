package com.example.data.repository

/**
 * A sealed class representing unified UI data consumption states.
 * Wraps local Room database cache and remote network responses into a single stream.
 */
sealed class Resource<out T> {

    /**
     * Data is currently being loaded or refreshed.
     * Optionally holds existing cached data from Room for immediate offline UI rendering.
     */
    data class Loading<out T>(
        val cachedData: T? = null
    ) : Resource<T>()

    /**
     * Data successfully retrieved and cached.
     * [isOfflineCached] indicates whether the data is served from local Room storage.
     */
    data class Success<out T>(
        val data: T,
        val isOfflineCached: Boolean = false,
        val message: String? = null
    ) : Resource<T>()

    /**
     * An error occurred during network fetch or database transaction.
     * [cachedData] provides local Room fallback so the UI never crashes or displays a blank screen.
     */
    data class Error<out T>(
        val message: String,
        val cachedData: T? = null,
        val throwable: Throwable? = null
    ) : Resource<T>()

    val dataOrNull: T?
        get() = when (this) {
            is Success -> data
            is Loading -> cachedData
            is Error -> cachedData
        }

    val isSuccess: Boolean get() = this is Success
    val isLoading: Boolean get() = this is Loading
    val isError: Boolean get() = this is Error
}
