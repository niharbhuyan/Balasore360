package com.balasore360.data

import com.balasore360.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


data class RemoteBusiness(
    val id: String,
    val name: String,
    val category: String,
    val area: String?,
    val description: String?,
    val rating: Double
)

data class RemoteEvent(
    val id: String,
    val title: String,
    val date: String?,
    val area: String?,
    val category: String?,
    val description: String?
)

data class RemoteNews(
    val id: String,
    val title: String,
    val category: String,
    val summary: String?
)

class BalasoreRepository {
    private val configured: Boolean
        get() = BuildConfig.SUPABASE_URL.isNotBlank() && BuildConfig.SUPABASE_PUBLISHABLE_KEY.isNotBlank()

    suspend fun loadBusinesses(search: String = ""): List<RemoteBusiness> = withContext(Dispatchers.IO) {
        if (!configured) return@withContext emptyList()
        val filters = buildString {
            append("select=id,name,category,area,description,rating")
            append("&published=eq.true")
            if (search.isNotBlank()) {
                val encoded = URLEncoder.encode("%$search%", Charsets.UTF_8.name())
                append("&or=(name.ilike.$encoded,category.ilike.$encoded,area.ilike.$encoded)")
            }
            append("&order=featured.desc,rating.desc&limit=20")
        }
        request("businesses", filters)?.let { json ->
            (0 until json.length()).map { index ->
                val item = json.getJSONObject(index)
                RemoteBusiness(
                    id = item.optString("id"),
                    name = item.optString("name"),
                    category = item.optString("category"),
                    area = item.optString("area").ifBlank { null },
                    description = item.optString("description").ifBlank { null },
                    rating = item.optDouble("rating", 0.0)
                )
            }
        } ?: emptyList()
    }

    suspend fun loadEvents(): List<RemoteEvent> = withContext(Dispatchers.IO) {
        if (!configured) return@withContext emptyList()
        val today = java.time.LocalDate.now().toString()
        val query = "select=id,title,date,area,category,description&published=eq.true&date=gte.$today&order=date.asc&limit=10"
        request("events", query)?.let { json ->
            (0 until json.length()).map { index ->
                val item = json.getJSONObject(index)
                RemoteEvent(
                    id = item.optString("id"),
                    title = item.optString("title"),
                    date = item.optString("date").ifBlank { null },
                    area = item.optString("area").ifBlank { null },
                    category = item.optString("category").ifBlank { null },
                    description = item.optString("description").ifBlank { null }
                )
            }
        } ?: emptyList()
    }

    suspend fun loadNews(): List<RemoteNews> = withContext(Dispatchers.IO) {
        if (!configured) return@withContext emptyList()
        val query = "select=id,title,category,summary&published=eq.true&order=published_at.desc&limit=5"
        request("news", query)?.let { json ->
            (0 until json.length()).map { index ->
                val item = json.getJSONObject(index)
                RemoteNews(
                    id = item.optString("id"),
                    title = item.optString("title"),
                    category = item.optString("category"),
                    summary = item.optString("summary").ifBlank { null }
                )
            }
        } ?: emptyList()
    }

    private fun request(table: String, query: String): JSONArray? {
        val connection = (URL("${BuildConfig.SUPABASE_URL}/rest/v1/$table?$query").openConnection() as HttpURLConnection)
        return try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000
            connection.setRequestProperty("apikey", BuildConfig.SUPABASE_PUBLISHABLE_KEY)
            connection.setRequestProperty("Authorization", "Bearer ${BuildConfig.SUPABASE_PUBLISHABLE_KEY}")
            connection.setRequestProperty("Accept", "application/json")
            if (connection.responseCode !in 200..299) return null
            JSONArray(connection.inputStream.bufferedReader().use { it.readText() })
        } catch (_: Exception) {
            null
        } finally {
            connection.disconnect()
        }
    }
}
