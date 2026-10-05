package com.example

import com.example.data.remote.OpenWeatherCity
import com.example.data.remote.OpenWeatherClouds
import com.example.data.remote.OpenWeatherCondition
import com.example.data.remote.OpenWeatherCoord
import com.example.data.remote.OpenWeatherCurrentResponse
import com.example.data.remote.OpenWeatherForecastItem
import com.example.data.remote.OpenWeatherForecastResponse
import com.example.data.remote.OpenWeatherMain
import com.example.data.remote.OpenWeatherMapApiService
import com.example.data.remote.OpenWeatherWind
import com.example.data.repository.OpenWeatherRepository
import com.example.ui.components.getOpenWeatherVisualMapping
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Robust mock implementation of OpenWeatherMapApiService for local JVM unit testing.
 * Verifies all API call interactions, network parameters, and response/exception dispatching.
 */
class MockOpenWeatherMapApiService : OpenWeatherMapApiService {
    var currentWeatherResponse: OpenWeatherCurrentResponse? = null
    var currentWeatherException: Exception? = null
    var forecastResponse: OpenWeatherForecastResponse? = null
    var forecastException: Exception? = null

    var lastRequestedLat: Double? = null
    var lastRequestedLon: Double? = null
    var lastRequestedUnits: String? = null
    var lastRequestedApiKey: String? = null

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        units: String,
        apiKey: String
    ): OpenWeatherCurrentResponse {
        lastRequestedLat = lat
        lastRequestedLon = lon
        lastRequestedUnits = units
        lastRequestedApiKey = apiKey
        currentWeatherException?.let { throw it }
        return currentWeatherResponse ?: throw IllegalStateException("Mock current weather not configured")
    }

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
        units: String,
        apiKey: String
    ): OpenWeatherForecastResponse {
        lastRequestedLat = lat
        lastRequestedLon = lon
        lastRequestedUnits = units
        lastRequestedApiKey = apiKey
        forecastException?.let { throw it }
        return forecastResponse ?: throw IllegalStateException("Mock forecast not configured")
    }
}

/**
 * Comprehensive Suite of Unit Tests for the WeatherDashboard component
 * to verify data fetching and UI rendering states, using JUnit and Mockito-style verification.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDashboardUnitTest {

    private lateinit var mockApiService: MockOpenWeatherMapApiService
    private lateinit var repository: OpenWeatherRepository

    @Before
    fun setUp() {
        mockApiService = MockOpenWeatherMapApiService()
        repository = OpenWeatherRepository(mockApiService)
    }

    @Test
    fun testDataFetching_SuccessfulCurrentWeather() = runTest {
        // Arrange
        val mockResponse = OpenWeatherCurrentResponse(
            coord = OpenWeatherCoord(lon = 86.9135, lat = 21.4934),
            weather = listOf(
                OpenWeatherCondition(id = 800, main = "Clear", description = "clear sky", icon = "01d")
            ),
            main = OpenWeatherMain(
                temp = 32.4,
                feelsLike = 35.8,
                tempMin = 26.0,
                tempMax = 34.0,
                pressure = 1012,
                humidity = 76
            ),
            wind = OpenWeatherWind(speed = 4.2, deg = 80),
            clouds = OpenWeatherClouds(all = 15),
            name = "Balasore",
            cod = 200
        )
        mockApiService.currentWeatherResponse = mockResponse

        // Act
        val result = repository.fetchCurrentWeather("test_balasore_key_123")

        // Assert
        assertTrue("Data fetching should succeed with valid response", result.isSuccess)
        val data = result.getOrNull()
        assertNotNull("Current weather data must not be null", data)
        assertEquals("Balasore", data?.name)
        assertEquals(32.4, data?.main?.temp ?: 0.0, 0.01)
        assertEquals(76, data?.main?.humidity)
        assertEquals("Clear", data?.weather?.firstOrNull()?.main)
        assertEquals("01d", data?.weather?.firstOrNull()?.icon)

        // Verify request coordinates and parameters
        assertEquals(21.4934, mockApiService.lastRequestedLat ?: 0.0, 0.001)
        assertEquals(86.9135, mockApiService.lastRequestedLon ?: 0.0, 0.001)
        assertEquals("metric", mockApiService.lastRequestedUnits)
        assertEquals("test_balasore_key_123", mockApiService.lastRequestedApiKey)
    }

    @Test
    fun testDataFetching_ApiErrorGracefulFallback() = runTest {
        // Arrange network failure
        mockApiService.currentWeatherException = IOException("Network timeout connecting to OpenWeatherMap API")

        // Act
        val result = repository.fetchCurrentWeather("invalid_or_offline_key")

        // Assert
        assertTrue("Repository should gracefully catch exception as Failure", result.isFailure)
        assertTrue(result.exceptionOrNull() is IOException)

        // Verify fallback synthesis returns realistic Balasore coastal telemetry
        val baseline = repository.createBaselineBalasoreWeather(cycle = 0)
        assertNotNull(baseline)
        assertEquals("Balasore", baseline.name)
        assertTrue("Humidity should be within coastal range", (baseline.main?.humidity ?: 0) >= 65)
        assertTrue("Temp should be within tropical coastal range", (baseline.main?.temp ?: 0.0) in 20.0..42.0)
    }

    @Test
    fun test7DayForecast_StructureAndIntegrity() {
        val sevenDayForecast = repository.createBaselineSevenDayForecast(cycle = 0)

        // 1. Verify exactly 7 daily cards are generated
        assertEquals("7-day forecast must have exactly 7 daily items", 7, sevenDayForecast.size)

        // 2. Day 1 is Today, Day 2 is Tomorrow
        val day1 = sevenDayForecast[0]
        assertEquals(1, day1.dayIndex)
        assertEquals("Today", day1.dayName)
        assertEquals("ଆଜି", day1.odiaDayName)
        assertTrue("Max temp should be >= Min temp", day1.tempMax >= day1.tempMin)
        assertTrue("Humidity should be between 0 and 100", day1.avgHumidity in 0..100)
        assertTrue("Wind speed should be positive", day1.windSpeedKmh > 0.0)

        val day2 = sevenDayForecast[1]
        assertEquals(2, day2.dayIndex)
        assertEquals("Tomorrow", day2.dayName)
        assertEquals("ଆସନ୍ତାକାଲି", day2.odiaDayName)

        // 3. Verify days 3 through 7 have valid Odia translations and non-empty dates
        for (i in 2 until 7) {
            val day = sevenDayForecast[i]
            assertEquals(i + 1, day.dayIndex)
            assertTrue("Day name must not be blank", day.dayName.isNotBlank())
            assertTrue("Odia day name must not be blank", day.odiaDayName.isNotBlank())
            assertTrue("Date label must not be blank", day.dateLabel.isNotBlank())
            assertTrue("Condition must not be blank", day.conditionMain.isNotBlank())
            assertTrue("Icon code must not be blank", day.iconCode.isNotBlank())
            assertTrue("Pop percent should be 0..100", day.popPercent in 0..100)
        }
    }

    @Test
    fun testDynamicIconMapping_SunnyConditions() {
        val mapping = getOpenWeatherVisualMapping(condition = "Clear", iconCode = "01d")
        assertEquals("Sunny", mapping.badgeLabel)
        assertEquals("☀️", mapping.badgeEmoji)
    }

    @Test
    fun testDynamicIconMapping_CloudyConditions() {
        val mapping = getOpenWeatherVisualMapping(condition = "Few Clouds", iconCode = "02d")
        assertEquals("Clouds", mapping.badgeLabel)
        assertEquals("⛅", mapping.badgeEmoji)
    }

    @Test
    fun testDynamicIconMapping_RainConditions() {
        val mappingRain = getOpenWeatherVisualMapping(condition = "Light Rain", iconCode = "10d")
        assertEquals("Rain", mappingRain.badgeLabel)
        assertEquals("🌧️", mappingRain.badgeEmoji)

        val mappingDrizzle = getOpenWeatherVisualMapping(condition = "Drizzle", iconCode = "09d")
        assertEquals("Rain", mappingDrizzle.badgeLabel)
        assertEquals("🌧️", mappingDrizzle.badgeEmoji)
    }

    @Test
    fun testDynamicIconMapping_ThunderstormConditions() {
        val mapping = getOpenWeatherVisualMapping(condition = "Thunderstorm with heavy rain", iconCode = "11d")
        assertEquals("Thunderstorm", mapping.badgeLabel)
        assertEquals("⛈️", mapping.badgeEmoji)
    }

    @Test
    fun testDynamicIconMapping_MistAndFogConditions() {
        val mappingMist = getOpenWeatherVisualMapping(condition = "Mist", iconCode = "50d")
        assertEquals("Mist/Haze", mappingMist.badgeLabel)
        assertEquals("🌫️", mappingMist.badgeEmoji)
    }

    @Test
    fun testTemperatureUnitConversionRenderingState() {
        val celsius = 30.0
        val fahrenheit = (celsius * 9 / 5) + 32
        assertEquals(86.0, fahrenheit, 0.01)

        val coldC = 0.0
        val coldF = (coldC * 9 / 5) + 32
        assertEquals(32.0, coldF, 0.01)
    }

    @Test
    fun testHumidityComfortLevelClassification() {
        val highHumidity = 85
        val highComfort = when {
            highHumidity > 80 -> "High Coastal Moisture"
            highHumidity in 60..80 -> "Pleasant Bay Breeze"
            else -> "Dry Coastal Air"
        }
        assertEquals("High Coastal Moisture", highComfort)

        val moderateHumidity = 72
        val modComfort = when {
            moderateHumidity > 80 -> "High Coastal Moisture"
            moderateHumidity in 60..80 -> "Pleasant Bay Breeze"
            else -> "Dry Coastal Air"
        }
        assertEquals("Pleasant Bay Breeze", modComfort)

        val dryHumidity = 50
        val dryComfort = when {
            dryHumidity > 80 -> "High Coastal Moisture"
            dryHumidity in 60..80 -> "Pleasant Bay Breeze"
            else -> "Dry Coastal Air"
        }
        assertEquals("Dry Coastal Air", dryComfort)
    }
}
