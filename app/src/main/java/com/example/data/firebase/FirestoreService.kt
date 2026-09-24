package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.example.data.local.HotspotEntity
import com.example.data.local.NewsArticleEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.data.model.FeedbackReport
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Cloud Firestore persistence service for Balasore 360.
 * Persists user bookmarks, saved articles, preferences, and community reviews.
 */
class FirestoreService(private val context: Context) {

    private val firestore: FirebaseFirestore? by lazy {
        try {
            if (com.example.data.fcm.FcmManager.isFirebaseConfigured(context) || FirebaseApp.getApps(context).isNotEmpty()) {
                FirebaseFirestore.getInstance()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w("FirestoreService", "Firebase not initialized: ${e.message}")
            null
        }
    }

    // Local in-memory cache for seamless offline-first experience
    private val localFeedbackCache = mutableListOf<FeedbackReport>()

    val isAvailable: Boolean
        get() = firestore != null

    /**
     * Save or sync a bookmarked hotspot to Firestore for the authenticated user.
     */
    suspend fun saveBookmark(userId: String, hotspot: HotspotEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext Result.failure(Exception("Firestore not initialized"))
            val docRef = db.collection("users")
                .document(userId)
                .collection("bookmarks")
                .document(hotspot.id)

            val data = hashMapOf(
                "id" to hotspot.id,
                "name" to hotspot.name,
                "odiaName" to hotspot.odiaName,
                "category" to hotspot.category,
                "shortDescription" to hotspot.shortDescription,
                "fullDescription" to hotspot.fullDescription,
                "highlights" to hotspot.highlights,
                "distanceKmFromBls" to hotspot.distanceKmFromBls,
                "bestTimeToVisit" to hotspot.bestTimeToVisit,
                "timings" to hotspot.timings,
                "entryFee" to hotspot.entryFee,
                "specialty" to hotspot.specialty,
                "localTip" to hotspot.localTip,
                "latitude" to hotspot.latitude,
                "longitude" to hotspot.longitude,
                "updatedAt" to System.currentTimeMillis()
            )

            docRef.set(data, SetOptions.merge()).await()
            Log.d("FirestoreService", "Bookmark saved to Firestore for user: $userId, spot: ${hotspot.id}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error saving bookmark to Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Remove a bookmarked hotspot from Firestore.
     */
    suspend fun removeBookmark(userId: String, hotspotId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext Result.failure(Exception("Firestore not initialized"))
            db.collection("users")
                .document(userId)
                .collection("bookmarks")
                .document(hotspotId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error removing bookmark from Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Fetch user's bookmarked hotspot IDs from Firestore.
     */
    suspend fun fetchBookmarkedIds(userId: String): Result<List<String>> = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext Result.failure(Exception("Firestore not initialized"))
            val snapshot = db.collection("users")
                .document(userId)
                .collection("bookmarks")
                .get()
                .await()

            val ids = snapshot.documents.map { it.id }
            Result.success(ids)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error fetching bookmarks from Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Persist user preferences to Firestore.
     */
    suspend fun saveUserPreferences(
        userId: String,
        language: String,
        weatherAlerts: Boolean,
        breakingNews: Boolean,
        locality: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext Result.failure(Exception("Firestore not initialized"))
            val data = hashMapOf(
                "userId" to userId,
                "language" to language,
                "weatherAlertsEnabled" to weatherAlerts,
                "breakingNewsEnabled" to breakingNews,
                "locality" to locality,
                "lastSyncedAt" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(userId)
                .collection("profile")
                .document("preferences")
                .set(data, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error saving preferences to Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Save user profile details to Firestore.
     */
    suspend fun saveUserProfile(user: UserEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext Result.failure(Exception("Firestore not initialized"))
            val data = hashMapOf(
                "uid" to user.id,
                "fullName" to user.fullName,
                "email" to user.email,
                "phoneNumber" to user.phoneNumber,
                "locality" to user.locality,
                "bio" to user.bio,
                "avatarUri" to (user.avatarUri ?: ""),
                "updatedAt" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(user.id)
                .set(data, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error saving user profile: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Persist community reviews to Firestore so residents and tourists can share feedback.
     */
    suspend fun submitReview(review: ReviewEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext Result.failure(Exception("Firestore not initialized"))
            val data = hashMapOf(
                "id" to review.id,
                "targetType" to review.targetType,
                "targetId" to review.targetId,
                "targetTitle" to review.targetTitle,
                "userId" to review.userId,
                "userName" to review.userName,
                "userLocality" to review.userLocality,
                "userAvatarUri" to (review.userAvatarUri ?: ""),
                "rating" to review.rating,
                "comment" to review.comment,
                "timestamp" to review.timestamp
            )
            db.collection("reviews")
                .document(review.id.toString())
                .set(data, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error submitting review to Firestore: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Save search history or query grounding analytics.
     */
    suspend fun logGroundedQuery(userId: String, query: String, groundingType: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val db = firestore ?: return@withContext Result.failure(Exception("Firestore not initialized"))
            val data = hashMapOf(
                "userId" to userId,
                "query" to query,
                "groundingType" to groundingType,
                "timestamp" to System.currentTimeMillis()
            )
            db.collection("users")
                .document(userId)
                .collection("grounded_queries")
                .add(data)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Submit user report or suggestion directly to the Firestore collection 'feedback'.
     */
    suspend fun submitFeedback(report: FeedbackReport): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Always preserve in local cache first
            synchronized(localFeedbackCache) {
                localFeedbackCache.removeAll { it.id == report.id }
                localFeedbackCache.add(0, report)
            }

            val db = firestore
            if (db != null) {
                val data = report.toFirestoreMap()
                db.collection("feedback")
                    .document(report.id)
                    .set(data, SetOptions.merge())
                    .await()
                Log.d("FirestoreService", "Feedback successfully submitted to collection 'feedback': ${report.id}")
            } else {
                Log.i("FirestoreService", "Firestore offline/mock. Feedback preserved locally: ${report.id}")
            }
            Result.success(report.id)
        } catch (e: Exception) {
            Log.e("FirestoreService", "Error saving feedback to 'feedback' collection: ${e.message}")
            // Still succeed since it is cached locally
            Result.success(report.id)
        }
    }

    /**
     * Fetch latest feedback reports and suggestions from Firestore collection 'feedback'.
     */
    suspend fun fetchFeedbackReports(limitCount: Long = 25): Result<List<FeedbackReport>> = withContext(Dispatchers.IO) {
        try {
            val db = firestore
            if (db != null) {
                val snapshot = db.collection("feedback")
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(limitCount)
                    .get()
                    .await()

                val firestoreReports = snapshot.documents.map { doc ->
                    FeedbackReport.fromFirestoreMap(doc.data ?: emptyMap())
                }
                Result.success(firestoreReports)
            } else {
                synchronized(localFeedbackCache) {
                    Result.success(localFeedbackCache.toList())
                }
            }
        } catch (e: Exception) {
            Log.w("FirestoreService", "Falling back to local cached feedback: ${e.message}")
            synchronized(localFeedbackCache) {
                Result.success(localFeedbackCache.toList())
            }
        }
    }
}
