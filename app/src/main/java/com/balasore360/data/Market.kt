package com.balasore360.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarketDto(
    val id: String,
    val item: String,
    val unit: String? = null,
    val price: Double? = null,
    val trend: String? = null,
    val source: String? = null,
    @SerialName("observed_at") val observedAt: String? = null,
    val provider: String? = null
)

data class MarketItem(
    val id: String,
    val item: String,
    val unit: String?,
    val price: Double?,
    val trend: String?,
    val source: String?,
    val observedAt: String?,
    val provider: String?
)

fun MarketDto.toDomain() = MarketItem(id, item, unit, price, trend, source, observedAt, provider)
