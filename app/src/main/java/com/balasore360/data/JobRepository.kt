package com.balasore360.data

import io.github.jan.supabase.postgrest.from

interface JobRepository {
    suspend fun getPublishedJobs(): Result<List<Job>>
}

class SupabaseJobRepository : JobRepository {
    override suspend fun getPublishedJobs(): Result<List<Job>> = runCatching {
        SupabaseClientProvider.client
            .from("jobs")
            .select {
                filter { eq("published", true) }
            }
            .decodeList<JobDto>()
            .map(JobDto::toDomain)
    }
}
