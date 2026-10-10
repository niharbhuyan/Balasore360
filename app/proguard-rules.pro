# Proguard / R8 Optimization and Obfuscation Rules for Balasore 360

-ignorewarnings
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, SourceFile, LineNumberTable

# Preserve native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Preserve Enum values() and valueOf()
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Preserve Parcelable Creator
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# --- Jetpack Compose ---
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}

# --- Moshi & JSON Serialization ---
-dontwarn com.squareup.moshi.**
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
}

# Keep All Application Data Models & DTOs
-keep class com.example.data.remote.** { *; }
-keep class com.example.data.model.** { *; }
-keep class com.example.data.local.** { *; }
-keep class com.example.data.repository.** { *; }
-keep class com.example.data.service.** { *; }
-keep class com.example.data.notification.** { *; }
-keep class com.example.ui.model.** { *; }
-keep class com.example.ui.viewmodel.** { *; }

# --- Room Database ---
-dontwarn androidx.room.paging.**
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public abstract *;
}

# --- Retrofit & OkHttp ---
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.**

# --- Kotlin Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**

# --- Google Play Services (Ads / Maps / App Update / Credentials) ---
-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.ads.** { *; }
-keep interface com.google.android.gms.ads.** { *; }
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }
-keep class com.google.android.play.core.** { *; }
-keep class androidx.credentials.** { *; }
-keep class com.google.android.libraries.identity.googleid.** { *; }

# --- Firebase (Auth, Firestore, Messaging) ---
-dontwarn com.google.firebase.**
-keep class com.google.firebase.** { *; }

# --- Coil Image Loading ---
-dontwarn coil.**
-keep class coil.** { *; }
