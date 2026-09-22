package com.example.data.model

data class HeritageAudioStory(
    val id: String,
    val titleEn: String,
    val titleOd: String,
    val monumentEn: String,
    val monumentOd: String,
    val era: String,
    val distanceKm: Double,
    val narrationScriptEn: String,
    val narrationScriptOd: String,
    val lyriaMusicPrompt: String,
    val suggestedAudioTrackName: String,
    val durationSeconds: Int,
    val historicalSignificance: String
)

data class BloodDonorContact(
    val id: String,
    val fullName: String,
    val bloodGroup: String,
    val blockTown: String,
    val contactPhone: String,
    val lastDonationDate: String,
    val isVerified: Boolean = true,
    val availabilityStatus: String = "Available for Emergency"
)

data class HospitalBedPulse(
    val hospitalId: String,
    val name: String,
    val odiaName: String,
    val location: String,
    val totalIcuBeds: Int,
    val availableIcuBeds: Int,
    val totalOxygenBeds: Int,
    val availableOxygenBeds: Int,
    val dialysisSlotsTotal: Int,
    val availableDialysisSlots: Int,
    val emergencyContact: String,
    val lastUpdatedMinutesAgo: Int
)

data class RareBloodAlert(
    val id: String,
    val patientNameOrBed: String,
    val bloodGroup: String,
    val unitsNeeded: Int,
    val hospital: String,
    val contactPerson: String,
    val contactPhone: String,
    val postedTimeAgo: String,
    val isUrgent: Boolean = true
)
