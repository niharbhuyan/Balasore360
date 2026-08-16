package com.balasore360.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BusinessDto(
    val id: String,
    val name: String,
    val category: String? = null,
    val description: String? = null,
    val address: String? = null,
    val area: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val rating: Double? = null,
    @SerialName("review_count") val reviewCount: Int? = null,
    val verified: Boolean = false,
    val featured: Boolean = false
) {
    fun toDomain() = Business(
        id = id,
        name = name,
        category = category,
        description = description,
        address = address,
        area = area,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        verified = verified,
        featured = featured
    )
}
