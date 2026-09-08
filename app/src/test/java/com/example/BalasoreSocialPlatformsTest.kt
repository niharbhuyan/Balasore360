package com.example

import com.example.ui.components.BALASORE_SOCIAL_PLATFORMS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BalasoreSocialPlatformsTest {

    @Test
    fun `verify all 5 requested social media platforms are registered`() {
        assertEquals(5, BALASORE_SOCIAL_PLATFORMS.size)

        val ids = BALASORE_SOCIAL_PLATFORMS.map { it.id }.toSet()
        assertTrue(ids.contains("social_facebook"))
        assertTrue(ids.contains("social_instagram"))
        assertTrue(ids.contains("social_threads"))
        assertTrue(ids.contains("social_x"))
        assertTrue(ids.contains("social_youtube"))
    }

    @Test
    fun `verify exact social media platform urls match user specifications`() {
        val platformMap = BALASORE_SOCIAL_PLATFORMS.associateBy { it.id }

        val facebook = platformMap["social_facebook"]!!
        assertEquals("Facebook", facebook.name)
        assertEquals("@balasore360", facebook.handle)
        assertTrue(facebook.webUrl.contains("facebook.com/balasore360"))

        val instagram = platformMap["social_instagram"]!!
        assertEquals("Instagram", instagram.name)
        assertEquals("@balasore360", instagram.handle)
        assertTrue(instagram.webUrl.contains("instagram.com/balasore360"))

        val threads = platformMap["social_threads"]!!
        assertEquals("Threads", threads.name)
        assertEquals("@balasore360", threads.handle)
        assertTrue(threads.webUrl.contains("threads.com/balasore360"))

        val x = platformMap["social_x"]!!
        assertEquals("X (Twitter)", x.name)
        assertEquals("@balasore360", x.handle)
        assertTrue(x.webUrl.contains("x.com/balasore360"))

        val youtube = platformMap["social_youtube"]!!
        assertEquals("YouTube", youtube.name)
        assertEquals("@balasore360", youtube.handle)
        assertTrue(youtube.webUrl.contains("youtube.com/@balasore360"))
    }

    @Test
    fun `verify every platform has descriptive info and non-empty handle`() {
        BALASORE_SOCIAL_PLATFORMS.forEach { platform ->
            assertEquals("@balasore360", platform.handle)
            assertTrue(platform.name.isNotBlank())
            assertTrue(platform.description.isNotBlank())
            assertTrue(platform.webUrl.isNotBlank())
        }
    }
}
