package com.example.data.model

data class IncoisPfZZone(
    val id: String,
    val harborName: String,
    val odiaHarborName: String,
    val bearingDegrees: Int,
    val distanceKm: Double,
    val latitude: Double,
    val longitude: Double,
    val depthFathoms: Int,
    val expectedSpecies: String,
    val odiaSpecies: String,
    val seaSurfaceTempCelsius: Double,
    val chlorophyllConcentration: Double,
    val safetyStatus: HarborSafetyFlag
)

enum class HarborSafetyFlag(val label: String, val odiaLabel: String, val hexColor: Long) {
    GREEN_SAFE("Safe for Country Boats & Trawlers", "ଡଙ୍ଗା ଚାଳନା ପାଇଁ ସୁରକ୍ଷିତ", 0xFF22C55E),
    YELLOW_CAUTION("Moderate Swell - Caution Advised", "ମଧ୍ୟମ ଢେଉ - ସତର୍କତା ଆବଶ୍ୟକ", 0xFFF59E0B),
    RED_DANGER("Bar-Crossing Hazard / High Squall", "ମୁହାଣରେ ଉଚ୍ଚ ଢେଉ - ସମୁଦ୍ର ଯାତ୍ରା ନିଷେଧ", 0xFFEF4444)
}

data class CoastalHarborAdvisory(
    val harborId: String,
    val name: String,
    val odiaName: String,
    val currentWaveHeightMeters: Double,
    val swellPeriodSeconds: Int,
    val windSpeedKnots: Int,
    val windDirection: String,
    val waterVisibilityMeters: Double,
    val safetyFlag: HarborSafetyFlag,
    val highTideTime: String,
    val lowTideTime: String,
    val notice: String,
    val odiaNotice: String
)

data class OdiaPanjikaDay(
    val englishDate: String,
    val odiaYearMonth: String,
    val tithi: String,
    val odiaTithi: String,
    val paksha: String, // Sukla or Krushna
    val nakshatra: String,
    val odiaNakshatra: String,
    val suryaUdaya: String,
    val suryaAstha: String,
    val rahuKala: String,
    val amrutaBela: String,
    val dailyNiti: String,
    val odiaDailyNiti: String,
    val fastingRule: String
)

data class BalasoreFestivalCountdown(
    val id: String,
    val festivalName: String,
    val odiaFestivalName: String,
    val location: String,
    val odiaLocation: String,
    val targetDateEpochMillis: Long,
    val description: String,
    val odiaDescription: String,
    val keyRitual: String
)

data class TempleScheduleItem(
    val templeName: String,
    val odiaTempleName: String,
    val deity: String,
    val morningAlati: String,
    val madhyannaDhupa: String,
    val sandhyaArati: String,
    val specialBhog: String,
    val darshanStatus: String
)
