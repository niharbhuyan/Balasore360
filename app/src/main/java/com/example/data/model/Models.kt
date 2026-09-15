package com.example.data.model

enum class AppLanguage {
    ODIA, ENGLISH, HINDI
}

data class Hotspot(
    val id: String,
    val name: String,
    val odiaName: String,
    val category: String,
    val description: String,
    val location: String,
    val rating: Double,
    val isFeatured: Boolean = false,
    val timing: String = "6:00 AM - 6:00 PM",
    val distanceKm: Double = 12.5
)

data class NewsArticle(
    val id: String,
    val title: String,
    val odiaTitle: String,
    val snippet: String,
    val odiaSnippet: String,
    val category: String,
    val timeAgo: String,
    val source: String = "Balasore Live Bureau",
    val isBookmarked: Boolean = false
)

data class WeatherInfo(
    val tempCelsius: Int = 29,
    val condition: String = "Partly Cloudy",
    val highLow: String = "32° / 25°",
    val humidity: String = "74%",
    val windSpeedKmh: String = "18 km/h",
    val tideStatus: String = "Low Tide at 02:45 PM (Receding 5 km)",
    val seaCondition: String = "Safe for Beach Walkers",
    val cycloneAlert: String? = null
)

data class EmergencyContact(
    val id: String,
    val name: String,
    val odiaName: String,
    val number: String,
    val category: String,
    val is24x7: Boolean = true,
    val description: String = ""
)

data class TransitSchedule(
    val id: String,
    val routeName: String,
    val origin: String,
    val destination: String,
    val time: String,
    val type: String, // Train or Bus
    val status: String = "On Time"
)
