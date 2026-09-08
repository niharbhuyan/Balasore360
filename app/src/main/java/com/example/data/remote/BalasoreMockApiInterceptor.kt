package com.example.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * An OkHttp Interceptor simulating the local Balasore Government & Tourism API endpoints
 * for news, coastal marine weather, and emergency advisories.
 * This ensures the Retrofit network layer performs realistic, full HTTP request/response
 * cycles, JSON serialization via Moshi, and network-to-Room persistence.
 */
class BalasoreMockApiInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

        val responseBodyString: String? = when {
            url.contains("/api/v1/balasore/news/breaking") -> {
                val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
                """
                {
                    "status": "ok",
                    "totalResults": 2,
                    "source": "Balasore District Emergency Operations Centre",
                    "articles": [
                        {
                            "id": 101,
                            "title": "Bay of Bengal High-Tide Cautionary Siren Installed at Chandipur Promenade",
                            "summary": "Automated sensor-based alert sounds 30 minutes before sea returns to safeguard tourists walking the receding sea bed.",
                            "content": "In an effort to maximize safety on Chandipur Beach, district coastal security has installed solar sirens synchronized with real-time oceanographic tide gauges. Visitors will hear warning chimes 30 minutes prior to high tide onset.",
                            "category": "Weather",
                            "source": "Coastal Police & Marine Desk",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis()},
                            "isBreaking": true,
                            "location": "Chandipur Beach"
                        },
                        {
                            "id": 102,
                            "title": "Cyclone Preparedness Drill Conducted Across 12 Coastal Blocks of Balasore",
                            "summary": "District administration tests emergency shelter readiness, relief stock supplies, and satellite communication links.",
                            "content": "A high-level pre-monsoon cyclone preparedness drill was successfully completed across all 12 coastal blocks including Remuna, Bahanaga, and Chandipur. Quick response teams inspected multi-purpose cyclone shelters and verified early warning sirens.",
                            "category": "Weather",
                            "source": "District Disaster Management Authority",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis() - 3600000},
                            "isBreaking": true,
                            "location": "Balasore District"
                        }
                    ]
                }
                """.trimIndent()
            }
            url.contains("/api/v1/balasore/news") -> {
                val currentTime = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
                """
                {
                    "status": "ok",
                    "totalResults": 6,
                    "source": "Balasore Information & Public Relations Department",
                    "articles": [
                        {
                            "id": 101,
                            "title": "Bay of Bengal High-Tide Cautionary Siren Installed at Chandipur Promenade",
                            "summary": "Automated sensor-based alert sounds 30 minutes before sea returns to safeguard tourists walking the receding sea bed.",
                            "content": "In an effort to maximize safety on Chandipur Beach, district coastal security has installed solar sirens synchronized with real-time oceanographic tide gauges. Visitors will hear warning chimes 30 minutes prior to high tide onset.",
                            "category": "Weather",
                            "source": "Coastal Police & Marine Desk",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis()},
                            "isBreaking": true,
                            "location": "Chandipur Beach"
                        },
                        {
                            "id": 102,
                            "title": "Cyclone Preparedness Drill Conducted Across 12 Coastal Blocks of Balasore",
                            "summary": "District administration tests emergency shelter readiness, relief stock supplies, and satellite communication links.",
                            "content": "A high-level pre-monsoon cyclone preparedness drill was successfully completed across all 12 coastal blocks including Remuna, Bahanaga, and Chandipur. Quick response teams inspected multi-purpose cyclone shelters and verified early warning sirens.",
                            "category": "Weather",
                            "source": "District Disaster Management Authority",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis() - 1800000},
                            "isBreaking": true,
                            "location": "Balasore District"
                        },
                        {
                            "id": 103,
                            "title": "Balasore Municipal Corporation Rolls Out Eco-Electric City Shuttles",
                            "summary": "New fleet connecting Balasore Railway Station, Station Square, and Remuna Gopinath Temple for commuters.",
                            "content": "To facilitate clean transportation for residents and pilgrims, the Balasore Municipality has launched an eco-friendly electric feeder service. The buses run at 15-minute intervals connecting major transit hubs.",
                            "category": "Local",
                            "source": "Balasore Municipal Corporation",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis() - 7200000},
                            "isBreaking": false,
                            "location": "Balasore City"
                        },
                        {
                            "id": 104,
                            "title": "District Council Passes Resolution on Industrial Park Expansion Near Kuruda",
                            "summary": "Political consensus reached on expanding infrastructure and agro-processing clusters in Balasore district.",
                            "content": "In the latest district council meeting, elected representatives unanimously passed a resolution approving industrial park modernization near Kuruda. The project aims to attract food processing units and boost rural employment.",
                            "category": "Politics",
                            "source": "District Press Bureau",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis() - 10800000},
                            "isBreaking": false,
                            "location": "Kuruda Industrial Area"
                        },
                        {
                            "id": 105,
                            "title": "Annual Balasore Heritage Walk & Cultural Conclave Commences This Weekend",
                            "summary": "Heritage enthusiasts, artists, and students gather to explore 10th-century temples and maritime relics.",
                            "content": "The annual Balasore Heritage Walk series kicks off this weekend from Fakir Mohan College square. Guided walking tours of historical monuments and evening folk art exhibitions are scheduled across the town.",
                            "category": "Events",
                            "source": "Balasore Cultural Foundation",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis() - 14400000},
                            "isBreaking": false,
                            "location": "Fakir Mohan Square"
                        },
                        {
                            "id": 106,
                            "title": "Chandipur Marine Eco-Tourism Initiative Launched with Guided Mudflat Tours",
                            "summary": "Forest department and local boatmen introduce educational excursions studying horseshoe crabs and coastal mangroves.",
                            "content": "Visitors to Chandipur can now participate in certified eco-tours led by marine biologists and traditional fishing communities. The guided walks focus on preserving endangered horseshoe crab breeding zones along the receding shoreline.",
                            "category": "Tourism",
                            "source": "Odisha Tourism Development Corporation",
                            "publishedAt": "Updated $currentTime",
                            "timestamp": ${System.currentTimeMillis() - 18000000},
                            "isBreaking": false,
                            "location": "Chandipur Sea Beach"
                        }
                    ]
                }
                """.trimIndent()
            }
            url.contains("/api/v1/balasore/weather/marine-observatory") -> {
                """
                {
                    "stationId": "BLS_CHANDIPUR_01",
                    "stationName": "Chandipur Marine Weather Observatory",
                    "latitude": 21.4682,
                    "longitude": 87.0163,
                    "tideState": "RECEDING",
                    "tideDescription": "Sea recedes up to 5 kilometers during low tide. Ideal for beach strolling until siren warning.",
                    "nextTideTransition": "High tide expected in 3 hours 20 minutes",
                    "alertLevel": "NORMAL",
                    "alertTitle": "Safe Coastal Conditions",
                    "alertMessage": "Sea conditions are calm to moderate along Balasore coastline. Safe for visitors and traditional artisanal fishing within 5 nautical miles.",
                    "coastalAdvisory": "Caution: Avoid walking beyond 2 km from shore markers when warning flags are hoisted.",
                    "seaCondition": "Slight to Moderate Waves (0.6m - 1.1m)"
                }
                """.trimIndent()
            }
            else -> null
        }

        if (responseBodyString != null) {
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBodyString.toResponseBody(jsonMediaType))
                .build()
        }

        return chain.proceed(request)
    }
}
