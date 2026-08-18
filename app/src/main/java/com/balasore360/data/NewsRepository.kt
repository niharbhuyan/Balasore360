package com.balasore360.data

import io.github.jan.supabase.postgrest.from

interface NewsRepository {
    suspend fun getPublishedNews(): Result<List<News>>
}

class SupabaseNewsRepository : NewsRepository {
    override suspend fun getPublishedNews(): Result<List<News>> = runCatching {
        SupabaseClientProvider.client
            .from("news")
            .select {
                filter { eq("published", true) }
            }
            .decodeList<NewsDto>()
            .map(NewsDto::toDomain)
    }
}
