package com.example.data.model

import java.util.UUID

enum class FeedbackType {
    SUGGESTION,
    REPORT,
    COMPLAINT,
    FEATURE_REQUEST;

    fun getDisplayName(language: AppLanguage = AppLanguage.ENGLISH): String {
        return when (this) {
            SUGGESTION -> if (language == AppLanguage.ODIA) "ପ୍ରସ୍ତାବ / ପରାମର୍ଶ" else "Suggestion"
            REPORT -> if (language == AppLanguage.ODIA) "ସମସ୍ୟା ରିପୋର୍ଟ" else "Report Issue"
            COMPLAINT -> if (language == AppLanguage.ODIA) "ଅଭିଯୋଗ" else "Complaint"
            FEATURE_REQUEST -> if (language == AppLanguage.ODIA) "ନୂତନ ସୁବିଧା ଅନୁରୋଧ" else "Feature Request"
        }
    }
}

/**
 * Data model representing a user report or suggestion submitted
 * and persisted to the Firestore collection 'feedback'.
 */
data class FeedbackReport(
    val id: String = UUID.randomUUID().toString(),
    val type: FeedbackType = FeedbackType.SUGGESTION,
    val category: String = "General",
    val title: String = "",
    val description: String = "",
    val userName: String = "",
    val contactInfo: String = "",
    val locality: String = "Balasore",
    val rating: Int = 5,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "submitted"
) {
    /**
     * Converts to a map ready for Firestore collection 'feedback'.
     */
    fun toFirestoreMap(): Map<String, Any> {
        return hashMapOf(
            "id" to id,
            "type" to type.name,
            "category" to category,
            "title" to title,
            "description" to description,
            "userName" to userName,
            "contactInfo" to contactInfo,
            "locality" to locality,
            "rating" to rating,
            "timestamp" to timestamp,
            "status" to status,
            "devicePlatform" to "Android"
        )
    }

    companion object {
        fun fromFirestoreMap(map: Map<String, Any?>): FeedbackReport {
            val typeStr = map["type"] as? String ?: FeedbackType.SUGGESTION.name
            val type = try {
                FeedbackType.valueOf(typeStr)
            } catch (_: Exception) {
                FeedbackType.SUGGESTION
            }

            return FeedbackReport(
                id = map["id"] as? String ?: UUID.randomUUID().toString(),
                type = type,
                category = map["category"] as? String ?: "General",
                title = map["title"] as? String ?: "",
                description = map["description"] as? String ?: "",
                userName = map["userName"] as? String ?: "",
                contactInfo = map["contactInfo"] as? String ?: "",
                locality = map["locality"] as? String ?: "Balasore",
                rating = (map["rating"] as? Number)?.toInt() ?: 5,
                timestamp = (map["timestamp"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                status = map["status"] as? String ?: "submitted"
            )
        }
    }
}
