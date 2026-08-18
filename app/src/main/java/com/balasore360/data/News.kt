package com.balasore360.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsDto(
    val id: String,
    val title: String,
    val category: String? = null,
    val summary: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("published_at") val publishedAt: String? = null
)

data class News(
    val id: String,
    val title: String,
    val category: String?,
    val summary: String?,
    val imageUrl: String?,
    val publishedAt: String?
)

fun NewsDto.toDomain() = News(id, title, category, summary, imageUrl, publishedAt)
