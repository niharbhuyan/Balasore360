package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Database entity representing a citizen's reported civic issue in Balasore district.
 * Persisted locally for 100% offline access, status progression, and automated municipal updates.
 */
@Entity(tableName = "civic_reports")
data class CivicReportEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val category: String,
    val wardLocation: String,
    val description: String,
    val status: String = "PENDING", // "PENDING", "IN_PROGRESS", "RESOLVED"
    val urgency: String = "NORMAL", // "NORMAL", "HIGH", "CRITICAL"
    val assignedDepartment: String = "Balasore Municipality Grievance Cell",
    val reportedTimestamp: Long = System.currentTimeMillis(),
    val lastUpdatedTimestamp: Long = System.currentTimeMillis(),
    val hasPhotoAttached: Boolean = false,
    val resolutionNotes: String = "Report logged in local municipal queue. Awaiting field inspection.",
    val photoUri: String? = null,
    val latitude: Double? = 21.4934,
    val longitude: Double? = 86.9135,
    val geoAddress: String? = "Balasore, Odisha"
)
