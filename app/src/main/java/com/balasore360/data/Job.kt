package com.balasore360.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobDto(
    val id: String,
    val title: String,
    val company: String? = null,
    val area: String? = null,
    val employment: String? = null,
    val description: String? = null,
    @SerialName("apply_url") val applyUrl: String? = null,
    val published: Boolean = false,
    val provider: String? = null
)

data class Job(
    val id: String,
    val title: String,
    val company: String?,
    val area: String?,
    val employment: String?,
    val description: String?,
    val applyUrl: String?,
    val provider: String?
)

fun JobDto.toDomain() = Job(id, title, company, area, employment, description, applyUrl, provider)
