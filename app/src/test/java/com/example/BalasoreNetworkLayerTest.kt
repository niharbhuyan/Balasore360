package com.example

import com.example.data.remote.BalasoreApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreNetworkLayerTest {

    private val apiService = BalasoreApiService.create()

    @Test
    fun `fetch news via Retrofit network layer parses valid articles`() = runTest {
        val response = apiService.getBalasoreNews()
        assertEquals("ok", response.status)
        assertNotNull(response.articles)
        assertTrue(response.articles!!.isNotEmpty())

        val firstArticle = response.articles!!.first()
        assertNotNull(firstArticle.title)
        assertNotNull(firstArticle.category)
        assertNotNull(firstArticle.content)

        val entity = firstArticle.toEntity(isBookmarked = true)
        assertEquals(firstArticle.title, entity.title)
        assertTrue(entity.isBookmarked)
    }

    @Test
    fun `fetch breaking news via Retrofit returns emergency alerts`() = runTest {
        val response = apiService.getBreakingNews()
        assertEquals("ok", response.status)
        assertNotNull(response.articles)
        assertTrue(response.articles!!.all { it.isBreaking == true })
    }

    @Test
    fun `fetch marine weather observatory via Retrofit returns Chandipur data`() = runTest {
        val response = apiService.getMarineObservatoryData()
        assertEquals("BLS_CHANDIPUR_01", response.stationId)
        assertNotNull(response.tideState)
        assertNotNull(response.tideDescription)
        assertNotNull(response.alertTitle)
    }
}
