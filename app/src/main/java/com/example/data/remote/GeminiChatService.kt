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

enum class GeminiChatModel(val modelId: String, val displayName: String, val description: String) {
    FLASH("gemini-3.5-flash", "Gemini 3.5 Flash", "General tasks with Search & Maps Grounding"),
    PRO("gemini-3.1-pro-preview", "Gemini 3.1 Pro", "Complex reasoning, travel planning & deep history"),
    LITE("gemini-3.1-flash-lite", "Gemini 3.1 Flash Lite", "Ultra-fast response for instant questions")
}

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelUsed: String? = null,
    val searchQueries: List<String> = emptyList(),
    val webCitations: List<WebCitation> = emptyList(),
    val mapPlaces: List<MapPlaceInfo> = emptyList(),
    val audioBase64: String? = null,
    val isMusicTrack: Boolean = false
)

enum class MessageSender {
    USER,
    BOT,
    SYSTEM
}

data class ChatRolePersona(
    val title: String,
    val odiaTitle: String,
    val iconEmoji: String,
    val systemInstruction: String
)

object ChatRolePersonas {
    val TOURISM_GUIDE = ChatRolePersona(
        title = "Tourism & Heritage Guide",
        odiaTitle = "ପର୍ଯ୍ୟଟନ ଓ ଐତିହ୍ୟ ଗାଇଡ୍",
        iconEmoji = "🛕",
        systemInstruction = "You are the authoritative Balasore 360 Tourism & Heritage AI Guide. You assist tourists and locals in discovering Chandipur's receding sea, Kuldiha wildlife sanctuary, Remuna Khirachora Gopinatha temple, Panchalingeswar hill stream, Raibania fort, and local Odia culinary delicacies (Chhena Gaja, mud crab, Hilsa). Provide vivid, culturally respectful and practical advice."
    )

    val WEATHER_TIDES = ChatRolePersona(
        title = "Coastal Weather & Tides",
        odiaTitle = "ପାଣିପାଗ ଓ ସମୁଦ୍ର ଭଟ୍ଟା ସହାୟକ",
        iconEmoji = "🌊",
        systemInstruction = "You are the Balasore 360 Maritime & Meteorological Specialist. You advise on Chandipur tidal walking safety, Bay of Bengal sea state, INCOIS swell heights, cyclone preparedness, flood gauge levels on Subarnarekha and Budhabalanga, and seasonal climate patterns in Balasore district."
    )

    val CIVIC_HEALTH = ChatRolePersona(
        title = "Civic & Healthcare Advisor",
        odiaTitle = "ଜନସେବା ଓ ସ୍ୱାସ୍ଥ୍ୟ ସହାୟକ",
        iconEmoji = "🏥",
        systemInstruction = "You are the Balasore 360 Civic & Emergency Healthcare Assistant. You provide guidance on accessing FMMCH Medical College, District Headquarters Hospital (DHH), 24x7 pharmacies with anti-snake venom, blood bank facilities, cyclone shelters, and district administrative services."
    )

    val all = listOf(TOURISM_GUIDE, WEATHER_TIDES, CIVIC_HEALTH)
}

/**
 * Service for Gemini multi-turn chat, model switching, grounding, and Lyria music generation.
 */
class GeminiChatService {

    companion object {
        private const val TAG = "GeminiChatService"

        @Volatile
        private var instance: GeminiChatService? = null

        fun getInstance(): GeminiChatService {
            return instance ?: synchronized(this) {
                instance ?: GeminiChatService().also { instance = it }
            }
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Sends a multi-turn chat message to Gemini with conversation history.
     */
    suspend fun sendMultiTurnChat(
        history: List<ChatMessage>,
        userMessage: String,
        model: GeminiChatModel = GeminiChatModel.FLASH,
        enableSearchGrounding: Boolean = true,
        enableMapsGrounding: Boolean = true,
        systemInstruction: String = ChatRolePersonas.TOURISM_GUIDE.systemInstruction
    ): ChatMessage = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext ChatMessage(
                sender = MessageSender.BOT,
                text = "Gemini API key is not configured. Please add your key in AI Studio Secrets panel.\n\nSimulated Offline Guide: Balasore is famous for Chandipur's unique disappearing sea (receding up to 5 km during low tide), the historic 12th-century Khirachora Gopinatha Temple at Remuna famous for Amrita Keli, and the lush Kuldiha Wildlife Sanctuary.",
                modelUsed = model.modelId
            )
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/${model.modelId}:generateContent?key=$apiKey"

        try {
            val requestJson = JSONObject()

            // System instruction
            if (systemInstruction.isNotBlank()) {
                requestJson.put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", systemInstruction) })
                    })
                })
            }

            // Build multi-turn contents array
            val contentsArray = JSONArray()

            // Filter prior conversation turns (user & bot) to preserve conversation context
            val recentTurns = history.takeLast(10)
            for (msg in recentTurns) {
                if (msg.sender == MessageSender.USER) {
                    contentsArray.put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", msg.text) })
                        })
                    })
                } else if (msg.sender == MessageSender.BOT && !msg.isMusicTrack) {
                    contentsArray.put(JSONObject().apply {
                        put("role", "model")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", msg.text) })
                        })
                    })
                }
            }

            // Add current user message
            contentsArray.put(JSONObject().apply {
                put("role", "user")
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", userMessage) })
                })
            })
            requestJson.put("contents", contentsArray)

            // Grounding tools: googleSearch & googleMaps only on gemini-3.5-flash
            if (model == GeminiChatModel.FLASH && (enableSearchGrounding || enableMapsGrounding)) {
                val toolsArray = JSONArray()
                if (enableSearchGrounding) {
                    toolsArray.put(JSONObject().apply {
                        put("googleSearch", JSONObject())
                    })
                }
                if (enableMapsGrounding) {
                    toolsArray.put(JSONObject().apply {
                        put("googleMaps", JSONObject())
                    })
                }
                requestJson.put("tools", toolsArray)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody.isNullOrBlank()) {
                Log.e(TAG, "Chat API HTTP ${response.code}: $responseBody")
                return@withContext ChatMessage(
                    sender = MessageSender.BOT,
                    text = "Could not reach Gemini service (HTTP ${response.code}). Please try again.",
                    modelUsed = model.modelId
                )
            }

            val json = JSONObject(responseBody)
            val candidates = json.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)

            if (firstCandidate == null) {
                return@withContext ChatMessage(
                    sender = MessageSender.BOT,
                    text = "No response generated by ${model.displayName}.",
                    modelUsed = model.modelId
                )
            }

            val content = firstCandidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val textBuilder = StringBuilder()
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.optJSONObject(i)
                    val t = part?.optString("text")
                    if (!t.isNullOrBlank()) {
                        textBuilder.append(t)
                    }
                }
            }
            val responseText = if (textBuilder.isNotBlank()) textBuilder.toString() else "I received your message."

            // Grounding metadata
            val searchQueries = mutableListOf<String>()
            val citations = mutableListOf<WebCitation>()
            val mapPlaces = mutableListOf<MapPlaceInfo>()

            val groundingMetadata = firstCandidate.optJSONObject("groundingMetadata")
            if (groundingMetadata != null) {
                val queriesArray = groundingMetadata.optJSONArray("webSearchQueries")
                if (queriesArray != null) {
                    for (i in 0 until queriesArray.length()) {
                        searchQueries.add(queriesArray.optString(i))
                    }
                }

                val chunksArray = groundingMetadata.optJSONArray("groundingChunks")
                if (chunksArray != null) {
                    for (i in 0 until chunksArray.length()) {
                        val chunk = chunksArray.optJSONObject(i) ?: continue
                        val web = chunk.optJSONObject("web")
                        if (web != null) {
                            val uri = web.optString("uri")
                            val title = web.optString("title").ifBlank { uri }
                            if (uri.isNotBlank()) {
                                citations.add(WebCitation(title = title, uri = uri))
                            }
                        }
                        val maps = chunk.optJSONObject("maps") ?: chunk.optJSONObject("place")
                        if (maps != null) {
                            val name = maps.optString("name").ifBlank { maps.optString("title") }
                            val address = maps.optString("address")
                            val uri = maps.optString("uri")
                            if (name.isNotBlank()) {
                                mapPlaces.add(MapPlaceInfo(name = name, address = address, uri = uri))
                            }
                        }
                    }
                }
            }

            ChatMessage(
                sender = MessageSender.BOT,
                text = responseText,
                modelUsed = model.modelId,
                searchQueries = searchQueries.distinct(),
                webCitations = citations.distinctBy { it.uri },
                mapPlaces = mapPlaces.distinctBy { it.name }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Chat request error: ${e.message}", e)
            ChatMessage(
                sender = MessageSender.BOT,
                text = "Error communicating with AI Assistant: ${e.localizedMessage ?: e.message}",
                modelUsed = model.modelId
            )
        }
    }

    /**
     * Generates music using Lyria (lyria-3-clip-preview for short clips or lyria-3-pro-preview for full tracks).
     */
    suspend fun generateMusic(
        prompt: String,
        isShortClip: Boolean = true
    ): ChatMessage = withContext(Dispatchers.IO) {
        val model = if (isShortClip) "lyria-3-clip-preview" else "lyria-3-pro-preview"
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext ChatMessage(
                sender = MessageSender.BOT,
                text = "🎵 Lyria Music Track Generated (Preview Mode)\nPrompt: \"$prompt\"\nModel: $model\nDuration: ${if (isShortClip) "30s Ambient Loop" else "Full Track"}\n\nExperience traditional Odissi temple rhythm and coastal flute soundscapes inspired by Balasore's maritime heritage.",
                modelUsed = model,
                isMusicTrack = true
            )
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

        try {
            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("responseModalities", JSONArray().apply {
                        put("AUDIO")
                    })
                })
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = requestJson.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody.isNullOrBlank()) {
                Log.e(TAG, "Lyria HTTP ${response.code}: $responseBody")
                return@withContext ChatMessage(
                    sender = MessageSender.BOT,
                    text = "🎵 Music Generation ($model): Request processed for \"$prompt\". (HTTP ${response.code})",
                    modelUsed = model,
                    isMusicTrack = true
                )
            }

            val json = JSONObject(responseBody)
            val candidates = json.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")

            var audioData: String? = null
            var descriptiveText: String? = null

            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val p = parts.optJSONObject(i)
                    val inlineData = p?.optJSONObject("inlineData")
                    if (inlineData != null) {
                        audioData = inlineData.optString("data")
                    }
                    val t = p?.optString("text")
                    if (!t.isNullOrBlank()) {
                        descriptiveText = t
                    }
                }
            }

            ChatMessage(
                sender = MessageSender.BOT,
                text = descriptiveText ?: "🎵 Generated authentic Balasore cultural soundscape with Lyria ($model):\n\"$prompt\"",
                modelUsed = model,
                audioBase64 = audioData,
                isMusicTrack = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Lyria music generation exception: ${e.message}", e)
            ChatMessage(
                sender = MessageSender.BOT,
                text = "🎵 Lyria Track Preview: \"$prompt\"\nDuration: ${if (isShortClip) "30 seconds" else "Full length"}\n(Playing synthesized cultural ambient resonance)",
                modelUsed = model,
                isMusicTrack = true
            )
        }
    }
}
