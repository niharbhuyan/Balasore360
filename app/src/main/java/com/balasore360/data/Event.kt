package com.balasore360.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: String,
    val title: String,
    val date: String? = null,
    val area: String? = null,
    val category: String? = null,
    val description: String? = null,
    val published: Boolean = false,
    @SerialName("created_at") val createdAt: String? = null,
    val provider: String? = null,
    @SerialName("external_id") val externalId: String? = null
)

data class Event(
    val id: String,
    val title: String,
    val date: String?,
    val area: String?,
    val category: String?,
    val description: String?,
    val provider: String?
)

fun EventDto.toDomain() = Event(id, title, date, area, category, description, provider)
