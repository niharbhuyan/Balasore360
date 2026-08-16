package com.balasore360.data

data class Business(
    val id: String,
    val name: String,
    val category: String?,
    val description: String?,
    val address: String?,
    val area: String?,
    val imageUrl: String?,
    val rating: Double?,
    val reviewCount: Int?,
    val verified: Boolean,
    val featured: Boolean
)
