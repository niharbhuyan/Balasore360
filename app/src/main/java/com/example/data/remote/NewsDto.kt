package com.example.data.remote

import com.example.data.local.NewsArticleEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalasoreNewsResponse(
    @field:Json(name = "status") val status: String? = null,
    @field:Json(name = "totalResults") val totalResults: Int? = null,
    @field:Json(name = "source") val source: String? = null,
    @field:Json(name = "articles") val articles: List<NewsArticleDto>? = null
)

@JsonClass(generateAdapter = true)
data class NewsArticleDto(
    @field:Json(name = "id") val id: Long? = null,
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "summary") val summary: String? = null,
    @field:Json(name = "content") val content: String? = null,
    @field:Json(name = "category") val category: String? = null,
    @field:Json(name = "source") val source: String? = null,
    @field:Json(name = "publishedAt") val publishedAt: String? = null,
    @field:Json(name = "timestamp") val timestamp: Long? = null,
    @field:Json(name = "isBreaking") val isBreaking: Boolean? = null,
    @field:Json(name = "imageUrl") val imageUrl: String? = null,
    @field:Json(name = "location") val location: String? = null
) {
    fun toEntity(isBookmarked: Boolean = false): NewsArticleEntity {
        return NewsArticleEntity(
            id = id ?: 0,
            title = title ?: "Balasore District Update",
            summary = summary ?: "Recent updates from Balasore district administration.",
            content = content ?: summary ?: "Detailed coverage from Balasore regional reporting desks.",
            category = category ?: "Local",
            source = source ?: "Balasore Wire Service",
            publishedAt = publishedAt ?: "Recently published",
            timestamp = timestamp ?: System.currentTimeMillis(),
            isBreaking = isBreaking ?: false,
            imageUrl = imageUrl,
            isBookmarked = isBookmarked
        )
    }
}
