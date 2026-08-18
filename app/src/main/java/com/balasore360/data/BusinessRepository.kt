package com.balasore360.data

interface BusinessRepository {
    suspend fun getPublishedBusinesses(): Result<List<Business>>
}
