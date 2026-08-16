package com.balasore360.data

import io.github.jan.supabase.postgrest.from

class SupabaseBusinessRepository : BusinessRepository {
    override suspend fun getPublishedBusinesses(): Result<List<Business>> = runCatching {
        SupabaseClientProvider.client
            .from("businesses")
            .select {
                filter {
                    eq("published", true)
                }
            }
            .decodeList<BusinessDto>()
            .map(BusinessDto::toDomain)
    }
}
