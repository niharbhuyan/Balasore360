package com.example.data.firebase

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.data.local.UserEntity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Manages Firebase Authentication for Balasore 360, including:
 * - Google Sign-In via Credential Manager & GoogleIdTokenCredential
 * - Email & Password authentication
 * - Password reset
 * - Current user synchronization
 */
class FirebaseAuthManager(private val context: Context) {

    private val auth: FirebaseAuth? by lazy {
        try {
            if (com.example.data.fcm.FcmManager.isFirebaseConfigured(context)) {
                FirebaseAuth.getInstance()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w("FirebaseAuthManager", "Firebase Auth not available: ${e.message}")
            null
        }
    }

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    init {
        try {
            auth?.addAuthStateListener { firebaseAuth ->
                _currentUser.value = firebaseAuth.currentUser
                Log.d("FirebaseAuthManager", "Auth state changed: user=${firebaseAuth.currentUser?.email}")
            }
            _currentUser.value = auth?.currentUser
        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Failed to register auth listener: ${e.message}")
        }
    }

    val isAvailable: Boolean
        get() = auth != null

    val isUserSignedIn: Boolean
        get() = auth?.currentUser != null

    /**
     * Map FirebaseUser to UserEntity
     */
    fun toUserEntity(fbUser: FirebaseUser, fallbackLocality: String = "Balasore"): UserEntity {
        return UserEntity(
            id = fbUser.uid,
            email = fbUser.email ?: "${fbUser.uid}@google.com",
            fullName = fbUser.displayName ?: fbUser.email?.substringBefore("@") ?: "Balasore Resident",
            passwordHash = "FIREBASE_OAUTH_TOKEN",
            phoneNumber = fbUser.phoneNumber ?: "",
            locality = fallbackLocality,
            bio = "Verified Balasore 360 Community Member",
            avatarUri = fbUser.photoUrl?.toString(),
            securityQuestion = "Google Account Authentication",
            securityAnswer = "FIREBASE_AUTH_MANAGED",
            createdAt = System.currentTimeMillis()
        )
    }

    /**
     * Sign in using Google Identity via Android Credential Manager and Firebase Auth.
     */
    suspend fun signInWithGoogle(
        context: Context,
        serverClientId: String? = null
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = auth
                ?: return@withContext Result.failure(Exception("Firebase is not initialized. Please ensure google-services.json is configured."))

            val credentialManager = CredentialManager.create(context)

            // Server Client ID: can be Web Client ID from Firebase Console or fallback placeholder
            val clientId = serverClientId
                ?.takeIf { it.isNotBlank() }
                ?: "613265325843-placeholder.apps.googleusercontent.com"

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(clientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = try {
                credentialManager.getCredential(context, request)
            } catch (e: GetCredentialCancellationException) {
                return@withContext Result.failure(Exception("Google Sign-In was cancelled by user."))
            } catch (e: GetCredentialException) {
                // If Play Services or server client ID isn't linked to real Google Cloud project yet,
                // give a friendly message with guidance:
                Log.w("FirebaseAuthManager", "CredentialManager getCredential warning: ${e.message}")
                return@withContext Result.failure(Exception("Google Sign-In unavailable: ${e.message}. You can also use Email sign-in or Quick Demo."))
            }

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                // Sign in with Firebase Auth credential
                val authCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()
                val user = authResult.user
                    ?: return@withContext Result.failure(Exception("Firebase returned null user after Google Sign-In"))

                val mappedUser = toUserEntity(user)
                _currentUser.value = user
                Result.success(mappedUser)
            } else {
                Result.failure(Exception("Unexpected credential type: ${credential.type}"))
            }
        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Google Sign-In error: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Sign in with Email and Password using Firebase Auth.
     */
    suspend fun signInWithEmail(email: String, pass: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = auth
                ?: return@withContext Result.failure(Exception("Firebase Auth not initialized."))

            val authResult = firebaseAuth.signInWithEmailAndPassword(email.trim(), pass).await()
            val user = authResult.user
                ?: return@withContext Result.failure(Exception("Authentication succeeded but no user returned."))

            _currentUser.value = user
            Result.success(toUserEntity(user))
        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Email sign in failed: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Create account with Email, Password and full name using Firebase Auth.
     */
    suspend fun signUpWithEmail(
        name: String,
        email: String,
        pass: String,
        phone: String = "",
        locality: String = "Balasore"
    ): Result<UserEntity> = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = auth
                ?: return@withContext Result.failure(Exception("Firebase Auth not initialized."))

            val authResult = firebaseAuth.createUserWithEmailAndPassword(email.trim(), pass).await()
            val user = authResult.user
                ?: return@withContext Result.failure(Exception("Account created but no user profile returned."))

            // Update display name
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
            user.updateProfile(profileUpdates).await()

            _currentUser.value = user
            val userEntity = toUserEntity(user, locality).copy(
                fullName = name,
                phoneNumber = phone,
                locality = locality
            )
            Result.success(userEntity)
        } catch (e: Exception) {
            Log.e("FirebaseAuthManager", "Email sign up failed: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Send password reset email via Firebase Auth.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val firebaseAuth = auth
                ?: return@withContext Result.failure(Exception("Firebase Auth not initialized."))

            firebaseAuth.sendPasswordResetEmail(email.trim()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sign out from Firebase Auth and clear credentials.
     */
    suspend fun signOut(context: Context? = null): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth?.signOut()
            _currentUser.value = null
            if (context != null) {
                try {
                    val credentialManager = CredentialManager.create(context)
                    credentialManager.clearCredentialState(ClearCredentialStateRequest())
                } catch (e: Exception) {
                    Log.w("FirebaseAuthManager", "Clear credential state warning: ${e.message}")
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
