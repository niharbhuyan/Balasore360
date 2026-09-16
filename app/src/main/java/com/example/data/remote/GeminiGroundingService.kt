package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

enum class GroundingToolMode {
    SEARCH_ONLY,
    MAPS_ONLY,
    COMBINED
}

data class WebCitation(
    val title: String,
    val uri: String
)

data class MapPlaceInfo(
    val name: String,
    val address: String? = null,
    val uri: String? = null
)

data class GroundingResponse(
    val text: String,
    val searchQueries: List<String> = emptyList(),
    val citations: List<WebCitation> = emptyList(),
    val mapPlaces: List<MapPlaceInfo> = emptyList(),
    val toolModeUsed: GroundingToolMode = GroundingToolMode.SEARCH_ONLY,
    val model: String = "gemini-3.5-flash",
    val isSuccess: Boolean = true,
    val errorMessage: String? = null
)

/**
 * Service for communicating with Gemini 3.5 Flash using direct REST API with:
 * - Google Search Grounding (`googleSearch` tool)
 * - Google Maps Grounding (`googleMaps` tool)
 * - Combined Search & Maps Grounding
 */
class GeminiGroundingService {

    companion object {
        private const val TAG = "GeminiGroundingService"
        private const val MODEL_NAME = "gemini-3.5-flash"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

        @Volatile
        private var instance: GeminiGroundingService? = null

        fun getInstance(): GeminiGroundingService {
            return instance ?: synchronized(this) {
                instance ?: GeminiGroundingService().also { instance = it }
            }
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Query Gemini 3.5 Flash with Google Search Grounding, Google Maps Grounding, or both.
     *
     * @param prompt The user's query or contextual prompt.
     * @param toolMode Which grounding tools to include: SEARCH_ONLY, MAPS_ONLY, or COMBINED.
     * @param systemInstruction Optional system prompt to guide tone or scope (e.g. Balasore district guide).
     */
    suspend fun queryWithGrounding(
        prompt: String,
        toolMode: GroundingToolMode = GroundingToolMode.COMBINED,
        systemInstruction: String = "You are the authoritative Balasore 360 Local & Tourism AI Assistant. Provide accurate, up-to-date information regarding Balasore district, Chandipur, Kuldiha, Panchalingeswar, Nilgiri, weather, and civic services in Odisha. Ground your answers using the provided tools."
    ): GroundingResponse = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "GEMINI_API_KEY is not configured or placeholder. Returning informative notice.")
            return@withContext GroundingResponse(
                text = "Gemini API key is required for live Search & Maps Grounding. Please add your key in the AI Studio Secrets panel. (Currently in offline/cached mode).",
                toolModeUsed = toolMode,
                isSuccess = false,
                errorMessage = "Missing Gemini API Key"
            )
        }

        try {
            // Build Request JSON
            val requestJson = JSONObject().apply {
                // Contents
                val contentsArray = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                }
                put("contents", contentsArray)

                // Tools configuration
                val toolsArray = JSONArray().apply {
                    val toolObj = JSONObject()
                    when (toolMode) {
                        GroundingToolMode.SEARCH_ONLY -> {
                            toolObj.put("googleSearch", JSONObject())
                            put(toolObj)
                        }
                        GroundingToolMode.MAPS_ONLY -> {
                            toolObj.put("googleMaps", JSONObject())
                            put(toolObj)
                        }
                        GroundingToolMode.COMBINED -> {
                            // Add googleSearch and googleMaps tools
                            toolObj.put("googleSearch", JSONObject())
                            put(toolObj)
                            put(JSONObject().apply {
                                put("googleMaps", JSONObject())
                            })
                        }
                    }
                }
                put("tools", toolsArray)

                // System Instruction
                if (systemInstruction.isNotBlank()) {
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", systemInstruction)
                            })
                        })
                    })
                }
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)
            val requestUrl = "$BASE_URL?key=$apiKey"

            val request = Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody.isNullOrBlank()) {
                Log.e(TAG, "Gemini API HTTP Error ${response.code}: $responseBody")
                return@withContext GroundingResponse(
                    text = "Could not complete live grounded search (HTTP ${response.code}). Please try again shortly.",
                    toolModeUsed = toolMode,
                    isSuccess = false,
                    errorMessage = "HTTP ${response.code}: ${response.message}"
                )
            }

            // Parse response JSON
            val nonNullBody = responseBody ?: "{}"
            val json = JSONObject(nonNullBody)
            val candidates = json.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)

            if (firstCandidate == null) {
                return@withContext GroundingResponse(
                    text = "No response generated by Gemini 3.5 Flash.",
                    toolModeUsed = toolMode,
                    isSuccess = false
                )
            }

            // Extract candidate text
            val contentObj = firstCandidate.optJSONObject("content")
            val partsArray = contentObj?.optJSONArray("parts")
            val textBuilder = StringBuilder()
            if (partsArray != null) {
                for (i in 0 until partsArray.length()) {
                    val part = partsArray.optJSONObject(i)
                    val text = part?.optString("text")
                    if (!text.isNullOrBlank()) {
                        textBuilder.append(text)
                    }
                }
            }
            val rawMainText = textBuilder.toString()
            val mainText = if (rawMainText.isBlank()) "Received response from Gemini." else rawMainText

            // Extract Grounding Metadata
            val groundingMetadata = firstCandidate.optJSONObject("groundingMetadata")
            val searchQueries = mutableListOf<String>()
            val citations = mutableListOf<WebCitation>()
            val mapPlaces = mutableListOf<MapPlaceInfo>()

            if (groundingMetadata != null) {
                // Web Search Queries
                val queriesArray = groundingMetadata.optJSONArray("webSearchQueries")
                if (queriesArray != null) {
                    for (i in 0 until queriesArray.length()) {
                        searchQueries.add(queriesArray.optString(i))
                    }
                }

                // Grounding Chunks (Web Citations & Map Places)
                val chunksArray = groundingMetadata.optJSONArray("groundingChunks")
                if (chunksArray != null) {
                    for (i in 0 until chunksArray.length()) {
                        val chunk = chunksArray.optJSONObject(i) ?: continue

                        // Web citation
                        val web = chunk.optJSONObject("web")
                        if (web != null) {
                            val uri = web.optString("uri")
                            val rawTitle = web.optString("title")
                            val title = if (rawTitle.isNullOrBlank()) uri else rawTitle
                            if (uri.isNotBlank()) {
                                citations.add(WebCitation(title = title, uri = uri))
                            }
                        }

                        // Maps place info
                        val maps = chunk.optJSONObject("maps") ?: chunk.optJSONObject("place")
                        if (maps != null) {
                            val rawName = maps.optString("name")
                            val placeName = if (!rawName.isNullOrBlank()) rawName else maps.optString("title")
                            val address = maps.optString("address")
                            val uri = maps.optString("uri")
                            if (placeName.isNotBlank()) {
                                mapPlaces.add(MapPlaceInfo(name = placeName, address = address, uri = uri))
                            }
                        }
                    }
                }
            }

            GroundingResponse(
                text = mainText,
                searchQueries = searchQueries.distinct(),
                citations = citations.distinctBy { it.uri },
                mapPlaces = mapPlaces.distinctBy { it.name },
                toolModeUsed = toolMode,
                model = MODEL_NAME,
                isSuccess = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error executing grounded Gemini query: ${e.message}", e)
            GroundingResponse(
                text = "Failed to fetch grounded results: ${e.localizedMessage ?: e.message}. Please check network connection.",
                toolModeUsed = toolMode,
                isSuccess = false,
                errorMessage = e.message
            )
        }
    }
}
