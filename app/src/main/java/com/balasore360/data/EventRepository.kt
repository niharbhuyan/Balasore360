package com.balasore360.data

import io.github.jan.supabase.postgrest.from

interface EventRepository {
    suspend fun getPublishedEvents(): Result<List<Event>>
}

class SupabaseEventRepository : EventRepository {
    override suspend fun getPublishedEvents(): Result<List<Event>> = runCatching {
        SupabaseClientProvider.client
            .from("events")
            .select {
                filter { eq("published", true) }
            }
            .decodeList<EventDto>()
            .map(EventDto::toDomain)
    }
}
