package com.balasore360.data

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

sealed interface SearchResult {
    val id: String
    val title: String
    val category: String?
    val description: String?

    data class BusinessResult(val value: Business) : SearchResult {
        override val id: String get() = value.id
        override val title: String get() = value.name
        override val category: String? get() = value.category
        override val description: String? get() = value.description
    }

    data class NewsResult(val value: News) : SearchResult {
        override val id: String get() = value.id
        override val title: String get() = value.title
        override val category: String? get() = value.category
        override val description: String? get() = value.summary
    }

    data class EventResult(val value: Event) : SearchResult {
        override val id: String get() = value.id
        override val title: String get() = value.title
        override val category: String? get() = value.category
        override val description: String? get() = value.description
    }

    data class JobResult(val value: Job) : SearchResult {
        override val id: String get() = value.id
        override val title: String get() = value.title
        override val category: String? get() = value.employment
        override val description: String? get() = value.description
    }
}

enum class SearchCategory(val label: String) {
    ALL("All"),
    BUSINESS("Businesses"),
    NEWS("News"),
    EVENT("Events"),
    JOB("Jobs")
}

class SearchRepository(
    private val businessRepository: BusinessRepository = SupabaseBusinessRepository(),
    private val newsRepository: NewsRepository = SupabaseNewsRepository(),
    private val eventRepository: EventRepository = SupabaseEventRepository(),
    private val jobRepository: JobRepository = SupabaseJobRepository()
) {
    suspend fun search(query: String, category: SearchCategory): Result<List<SearchResult>> = runCatching {
        if (query.isBlank()) return@runCatching emptyList()

        coroutineScope {
            val businesses = async {
                if (category == SearchCategory.ALL || category == SearchCategory.BUSINESS) {
                    businessRepository.getPublishedBusinesses().getOrThrow().map(SearchResult::BusinessResult)
                } else emptyList()
            }
            val news = async {
                if (category == SearchCategory.ALL || category == SearchCategory.NEWS) {
                    newsRepository.getPublishedNews().getOrThrow().map(SearchResult::NewsResult)
                } else emptyList()
            }
            val events = async {
                if (category == SearchCategory.ALL || category == SearchCategory.EVENT) {
                    eventRepository.getPublishedEvents().getOrThrow().map(SearchResult::EventResult)
                } else emptyList()
            }
            val jobs = async {
                if (category == SearchCategory.ALL || category == SearchCategory.JOB) {
                    jobRepository.getPublishedJobs().getOrThrow().map(SearchResult::JobResult)
                } else emptyList()
            }

            (awaitAll(businesses, news, events, jobs).flatten())
                .filter { result ->
                    val needle = query.trim().lowercase()
                    listOfNotNull(
                        result.title,
                        result.category,
                        result.description,
                        when (result) {
                            is SearchResult.BusinessResult -> result.value.address
                            is SearchResult.NewsResult -> null
                            is SearchResult.EventResult -> result.value.area
                            is SearchResult.JobResult -> "${result.value.company} ${result.value.area}"
                        }
                    ).any { it.contains(needle, ignoreCase = true) }
                }
                .sortedBy { it.title.lowercase() }
        }
    }
}
