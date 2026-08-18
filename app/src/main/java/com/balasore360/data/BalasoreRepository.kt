package com.balasore360.data

import com.balasore360.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class RemoteBusiness(val id: String, val name: String, val category: String, val area: String?, val description: String?, val rating: Double)
data class RemoteEvent(val id: String, val title: String, val date: String?, val area: String?, val category: String?, val description: String?)
data class RemoteNews(val id: String, val title: String, val category: String, val summary: String?)
data class RemoteEducation(val id: String, val name: String, val type: String, val block: String?, val locality: String?, val address: String?, val latitude: Double?, val longitude: Double?)
data class RemoteHealthcare(val id: String, val name: String, val type: String, val block: String?, val locality: String?, val address: String?, val phone: String?, val latitude: Double?, val longitude: Double?)
data class RemotePrimaryHealthcare(val id: String, val name: String, val type: String, val block: String?, val locality: String?, val address: String?, val latitude: Double?, val longitude: Double?)
data class RemoteSchool(val id: String, val udiseCode: String?, val name: String, val block: String?, val locality: String?, val medium: String?, val board: String?, val address: String?, val latitude: Double?, val longitude: Double?)

class BalasoreRepository {
    private val configured get() = BuildConfig.SUPABASE_URL.isNotBlank() && BuildConfig.SUPABASE_PUBLISHABLE_KEY.isNotBlank()
    suspend fun loadBusinesses(search: String = "") = withContext(Dispatchers.IO) {
        if (!configured) return@withContext emptyList()
        val q = buildString { append("select=id,name,category,area,description,rating&published=eq.true"); if (search.isNotBlank()) { val e = URLEncoder.encode("%$search%", Charsets.UTF_8.name()); append("&or=(name.ilike.$e,category.ilike.$e,area.ilike.$e)") }; append("&order=featured.desc,rating.desc&limit=20") }
        request("businesses", q)?.map { RemoteBusiness(it.s("id"), it.s("name"), it.s("category"), it.n("area"), it.n("description"), it.optDouble("rating", 0.0)) } ?: emptyList()
    }
    suspend fun loadEvents() = withContext(Dispatchers.IO) { if (!configured) return@withContext emptyList(); val today = java.time.LocalDate.now().toString(); request("events", "select=id,title,date,area,category,description&published=eq.true&date=gte.$today&order=date.asc&limit=10")?.map { RemoteEvent(it.s("id"), it.s("title"), it.n("date"), it.n("area"), it.n("category"), it.n("description")) } ?: emptyList() }
    suspend fun loadNews() = withContext(Dispatchers.IO) { if (!configured) return@withContext emptyList(); request("news", "select=id,title,category,summary&published=eq.true&order=published_at.desc&limit=5")?.map { RemoteNews(it.s("id"), it.s("title"), it.s("category"), it.n("summary")) } ?: emptyList() }
    suspend fun loadEducation(search: String = "") = withContext(Dispatchers.IO) {
        if (!configured) return@withContext emptyList()
        val e = if (search.isBlank()) null else URLEncoder.encode("%$search%", Charsets.UTF_8.name())
        val q = buildString { append("select=id,name,institution_type,block_name,locality,address,latitude,longitude&published=eq.true"); if (e != null) append("&or=(name.ilike.$e,institution_type.ilike.$e,block_name.ilike.$e,locality.ilike.$e)"); append("&order=name.asc&limit=100") }
        request("education_institutions", q)?.map { RemoteEducation(it.s("id"), it.s("name"), it.s("institution_type"), it.n("block_name"), it.n("locality"), it.n("address"), it.d("latitude"), it.d("longitude")) } ?: emptyList()
    }
    suspend fun loadHealthcare() = withContext(Dispatchers.IO) { if (!configured) return@withContext emptyList(); request("healthcare_institutions", "select=id,name,facility_type,block,locality,address,phone,latitude,longitude&published=eq.true&order=name.asc&limit=100")?.map { RemoteHealthcare(it.s("id"), it.s("name"), it.s("facility_type"), it.n("block"), it.n("locality"), it.n("address"), it.n("phone"), it.d("latitude"), it.d("longitude")) } ?: emptyList() }
    suspend fun loadPrimaryHealthcare() = withContext(Dispatchers.IO) { if (!configured) return@withContext emptyList(); request("primary_healthcare_facilities", "select=id,name,facility_type,block,locality,address,latitude,longitude&is_published=eq.true&order=name.asc&limit=300")?.map { RemotePrimaryHealthcare(it.s("id"), it.s("name"), it.s("facility_type"), it.n("block"), it.n("locality"), it.n("address"), it.d("latitude"), it.d("longitude")) } ?: emptyList() }
    suspend fun loadSchools2026_27() = withContext(Dispatchers.IO) { if (!configured) return@withContext emptyList(); request("school_2026_27", "select=id,udise_code,name,block,locality,medium,education_board,address,latitude,longitude&is_published=eq.true&order=name.asc&limit=500")?.map { RemoteSchool(it.s("id"), it.n("udise_code"), it.s("name"), it.n("block"), it.n("locality"), it.n("medium"), it.n("education_board"), it.n("address"), it.d("latitude"), it.d("longitude")) } ?: emptyList() }
    private fun request(table: String, query: String): JSONArray? {
        if (!configured) return null
        val c = (URL("${BuildConfig.SUPABASE_URL}/rest/v1/$table?$query").openConnection() as HttpURLConnection)
        return try { c.requestMethod = "GET"; c.connectTimeout = 10_000; c.readTimeout = 10_000; c.setRequestProperty("apikey", BuildConfig.SUPABASE_PUBLISHABLE_KEY); c.setRequestProperty("Authorization", "Bearer ${BuildConfig.SUPABASE_PUBLISHABLE_KEY}"); c.setRequestProperty("Accept", "application/json"); if (c.responseCode !in 200..299) return null; JSONArray(c.inputStream.bufferedReader().use { it.readText() }) } catch (_: Exception) { null } finally { c.disconnect() }
    }
    private fun org.json.JSONObject.s(k: String) = optString(k)
    private fun org.json.JSONObject.n(k: String) = optString(k).ifBlank { null }
    private fun org.json.JSONObject.d(k: String): Double? = if (isNull(k)) null else optDouble(k).takeUnless { it.isNaN() }
}
