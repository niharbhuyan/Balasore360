package com.example.data.repository

import com.example.data.local.CivicReportDao
import com.example.data.local.CivicReportEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

/**
 * Repository managing offline-first Citizen Civic Issue Reports in Room Database.
 * Powers the 'My Reports' tracking section with automated municipal status updates.
 */
class CivicReportRepository(
    private val civicReportDao: CivicReportDao
) {

    val allReports: Flow<List<CivicReportEntity>> = civicReportDao.getAllReportsFlow()

    fun getReportsByStatus(status: String): Flow<List<CivicReportEntity>> =
        civicReportDao.getReportsByStatusFlow(status)

    /**
     * Seeds initial representative citizen reports if the offline Room database is empty.
     */
    suspend fun ensureDefaultReportsSeeded() {
        val count = civicReportDao.getReportsCount()
        if (count == 0) {
            val now = System.currentTimeMillis()
            val initialReports = listOf(
                CivicReportEntity(
                    id = "BLS-CIVIC-2026-4821",
                    title = "Deep pothole cluster near Phandi Chhak on OT Road",
                    category = "Road Pothole",
                    wardLocation = "Ward 14 - OT Road / Phandi Chhak",
                    description = "Vehicles and two-wheelers risk skidding during rain. Urgent bitumen filling needed.",
                    status = "IN_PROGRESS",
                    urgency = "HIGH",
                    assignedDepartment = "PWD & Balasore Municipality Road Div",
                    reportedTimestamp = now - (3 * 3600 * 1000L),
                    lastUpdatedTimestamp = now - (30 * 60 * 1000L),
                    hasPhotoAttached = true,
                    resolutionNotes = "Road contractor assigned. Bitumen hot-mix batch dispatched to Phandi Chhak."
                ),
                CivicReportEntity(
                    id = "BLS-CIVIC-2026-3914",
                    title = "Solid waste accumulation near Nuabazar Sabzi Mandi",
                    category = "Garbage Heap / Waste",
                    wardLocation = "Ward 4 - Sahadevkhunta",
                    description = "Vegetable waste spillover blocking pedestrian passage. Municipal dumper required.",
                    status = "PENDING",
                    urgency = "NORMAL",
                    assignedDepartment = "BMC Sanitation Wing",
                    reportedTimestamp = now - (1 * 3600 * 1000L),
                    lastUpdatedTimestamp = now - (1 * 3600 * 1000L),
                    hasPhotoAttached = true,
                    resolutionNotes = "Report logged in municipal grievance queue. Scheduled for evening sanitation beat."
                ),
                CivicReportEntity(
                    id = "BLS-CIVIC-2026-2105",
                    title = "Solar streetlights not turning on along FM University Road",
                    category = "Faulty Streetlight",
                    wardLocation = "Ward 1 - Azimabad",
                    description = "Dark stretch at night causing safety concern for evening student commuters.",
                    status = "RESOLVED",
                    urgency = "NORMAL",
                    assignedDepartment = "TPNODL & Balasore Municipal Electrical Div",
                    reportedTimestamp = now - (24 * 3600 * 1000L),
                    lastUpdatedTimestamp = now - (2 * 3600 * 1000L),
                    hasPhotoAttached = false,
                    resolutionNotes = "Faulty solar charge controller replaced by TPNODL lineman. Streetlights tested and fully operational."
                )
            )
            civicReportDao.insertReports(initialReports)
        }
    }

    /**
     * Submits and saves a new civic issue report into the local Room database.
     */
    suspend fun submitReport(
        title: String,
        category: String,
        wardLocation: String,
        description: String,
        urgency: String = "NORMAL",
        hasPhotoAttached: Boolean = false,
        photoUri: String? = null,
        latitude: Double? = 21.4934,
        longitude: Double? = 86.9135,
        geoAddress: String? = null
    ): CivicReportEntity {
        val trackingNumber = 1000 + Random.nextInt(9000)
        val reportId = "BLS-CIVIC-2026-$trackingNumber"
        val department = when (category) {
            "Road Pothole" -> "PWD & Balasore Municipality Road Div"
            "Faulty Streetlight" -> "TPNODL & Municipal Electrical Wing"
            "Drain Clog / Waterlogging" -> "Drainage Desiltation Taskforce"
            "Garbage Heap / Waste" -> "BMC Solid Waste Sanitation Wing"
            "Drinking Water Leak" -> "WATCO Public Health Engineering"
            else -> "Balasore Municipality Grievance Cell"
        }
        val entity = CivicReportEntity(
            id = reportId,
            title = title.ifBlank { "$category near $wardLocation" },
            category = category,
            wardLocation = wardLocation,
            description = description.ifBlank { "Citizen issue reported via Balasore 360 app." },
            status = "PENDING",
            urgency = urgency,
            assignedDepartment = department,
            reportedTimestamp = System.currentTimeMillis(),
            lastUpdatedTimestamp = System.currentTimeMillis(),
            hasPhotoAttached = hasPhotoAttached,
            resolutionNotes = "Report registered offline in Room DB. Auto-synced with Ward Grievance Desk.",
            photoUri = photoUri,
            latitude = latitude,
            longitude = longitude,
            geoAddress = geoAddress ?: "$wardLocation, Balasore"
        )
        civicReportDao.insertOrUpdateReport(entity)
        return entity
    }

    /**
     * Deletes a report from local Room Database.
     */
    suspend fun deleteReport(reportId: String) {
        civicReportDao.deleteReportById(reportId)
    }

    /**
     * Progresses a single report's status to the next stage (PENDING -> IN_PROGRESS -> RESOLVED).
     */
    suspend fun progressReportStatus(reportId: String): CivicReportEntity? {
        val existing = civicReportDao.getReportById(reportId) ?: return null
        val now = System.currentTimeMillis()
        val (nextStatus, notes) = when (existing.status) {
            "PENDING" -> Pair(
                "IN_PROGRESS",
                "Field inspection completed by ${existing.assignedDepartment}. Repair crew dispatched to ${existing.wardLocation}."
            )
            "IN_PROGRESS" -> Pair(
                "RESOLVED",
                "Work completed and verified by Ward Junior Engineer. Issue closed successfully in municipal registry."
            )
            else -> Pair(
                "RESOLVED",
                "Resolution inspected and confirmed by citizen sentinel."
            )
        }
        civicReportDao.updateReportStatus(reportId, nextStatus, now, notes)
        return existing.copy(status = nextStatus, lastUpdatedTimestamp = now, resolutionNotes = notes)
    }

    /**
     * Automatically called by Balasore 360's background engine to simulate live municipal
     * status progression for submitted citizen issues over time.
     */
    suspend fun autoProgressReports(): Int {
        val reports = civicReportDao.getAllReportsSync()
        var updatedCount = 0
        val now = System.currentTimeMillis()

        for (report in reports) {
            if (report.status == "PENDING") {
                val elapsedMinutes = (now - report.reportedTimestamp) / (60 * 1000L)
                if (elapsedMinutes >= 1) { // Progress after 1 minute of logging
                    civicReportDao.updateReportStatus(
                        id = report.id,
                        newStatus = "IN_PROGRESS",
                        updatedAt = now,
                        notes = "Inspection assigned to ${report.assignedDepartment}. Field technician en route to ${report.wardLocation}."
                    )
                    updatedCount++
                }
            } else if (report.status == "IN_PROGRESS") {
                val elapsedSinceUpdate = (now - report.lastUpdatedTimestamp) / (60 * 1000L)
                if (elapsedSinceUpdate >= 3) { // Complete after 3 minutes in progress
                    civicReportDao.updateReportStatus(
                        id = report.id,
                        newStatus = "RESOLVED",
                        updatedAt = now,
                        notes = "Repair work completed by municipal squad. Quality check passed by Balasore Ward Supervisor."
                    )
                    updatedCount++
                }
            }
        }
        return updatedCount
    }
}
