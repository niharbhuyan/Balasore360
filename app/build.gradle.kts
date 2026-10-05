import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.niharsales.balasore360"
        minSdk = 24
        targetSdk = 36
        versionCode = 12
        versionName = "1.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val envFile = rootProject.file(".env")
        val envProperties = Properties()
        if (envFile.exists()) {
            FileInputStream(envFile).use { stream ->
                envProperties.load(stream)
            }
        }

        val rawGeminiKey = System.getenv("GEMINI_API_KEY")
            ?: envProperties.getProperty("GEMINI_API_KEY")
            ?: "MY_GEMINI_API_KEY"
        val geminiKey = rawGeminiKey.trim()
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiKey\"")

        val rawWeatherKey = System.getenv("OPENWEATHERMAP_API_KEY")
            ?: envProperties.getProperty("OPENWEATHERMAP_API_KEY")
            ?: ""
        val openWeatherKey = rawWeatherKey.trim()
        buildConfigField("String", "OPENWEATHERMAP_API_KEY", "\"$openWeatherKey\"")

        val rawMapsKey = System.getenv("MAPS_API_KEY")
            ?: System.getenv("GOOGLE_MAPS_KEY")
            ?: System.getenv("GOOGLE_MAPS_API_KEY")
            ?: envProperties.getProperty("MAPS_API_KEY")
            ?: envProperties.getProperty("GOOGLE_MAPS_KEY")
            ?: envProperties.getProperty("GOOGLE_MAPS_API_KEY")
            ?: ""
        val trimmedMapsKey = rawMapsKey.trim()
        // Sanitize out placeholder or fake keys to prevent Google Maps authorization failure
        val mapsApiKey = if (trimmedMapsKey.equals("AIzaSyBalasore360MapsKey", ignoreCase = true) ||
            trimmedMapsKey.equals("AIzaSyBiQAArK-Hc0lYU35S41zBvPQnZT1S0d0k", ignoreCase = true) ||
            trimmedMapsKey.contains("YOUR_KEY", ignoreCase = true) ||
            trimmedMapsKey.contains("PLACEHOLDER", ignoreCase = true)) {
            ""
        } else {
            trimmedMapsKey
        }
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file("${rootDir}/my-upload-key.jks")
            storePassword = "android"
            keyAlias = "upload"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.play.services.ads)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
    implementation(libs.coil.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.play.services.maps)
    implementation(libs.kotlinx.coroutines.play.services)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
