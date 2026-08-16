package com.balasore360.data

import io.github.jan.supabase.postgrest.from

interface MarketRepository {
    suspend fun getMarketItems(): Result<List<MarketItem>>
}

class SupabaseMarketRepository : MarketRepository {
    override suspend fun getMarketItems(): Result<List<MarketItem>> = runCatching {
        SupabaseClientProvider.client
            .from("market")
            .select()
            .decodeList<MarketDto>()
            .map(MarketDto::toDomain)
    }
}
