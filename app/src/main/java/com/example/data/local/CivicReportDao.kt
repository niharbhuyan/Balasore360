package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for local citizen civic issue reports.
 * Supports offline storage, querying by resolution status, and automated status updates.
 */
@Dao
interface CivicReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateReport(report: CivicReportEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<CivicReportEntity>)

    @Query("SELECT * FROM civic_reports ORDER BY reportedTimestamp DESC")
    fun getAllReportsFlow(): Flow<List<CivicReportEntity>>

    @Query("SELECT * FROM civic_reports ORDER BY reportedTimestamp DESC")
    suspend fun getAllReportsSync(): List<CivicReportEntity>

    @Query("SELECT * FROM civic_reports WHERE status = :status ORDER BY reportedTimestamp DESC")
    fun getReportsByStatusFlow(status: String): Flow<List<CivicReportEntity>>

    @Query("SELECT * FROM civic_reports WHERE id = :id LIMIT 1")
    suspend fun getReportById(id: String): CivicReportEntity?

    @Query("UPDATE civic_reports SET status = :newStatus, lastUpdatedTimestamp = :updatedAt, resolutionNotes = :notes WHERE id = :id")
    suspend fun updateReportStatus(id: String, newStatus: String, updatedAt: Long, notes: String)

    @Query("DELETE FROM civic_reports WHERE id = :id")
    suspend fun deleteReportById(id: String)

    @Query("DELETE FROM civic_reports")
    suspend fun clearAllReports()

    @Query("SELECT COUNT(*) FROM civic_reports")
    suspend fun getReportsCount(): Int
}
