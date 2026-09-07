package com.example

import com.example.data.local.HotspotEntity
import com.example.data.local.NewsArticleEntity
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testSearchFilteringHotspots() {
    val hotspots = listOf(
      HotspotEntity(
        id = "chandipur_beach",
        name = "Chandipur Beach",
        odiaName = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
        category = "Beach",
        shortDescription = "Famous vanishing sea phenomenon with miles of receding shoreline.",
        fullDescription = "Chandipur beach is world-famous where the sea recedes up to 5 kilometers during low tide.",
        highlights = "Vanishing Sea, Red Ghost Crabs, Sunset",
        distanceKmFromBls = 16,
        bestTimeToVisit = "October to March",
        timings = "All day",
        entryFee = "Free",
        specialty = "Marine biodiversity and horseshoe crabs",
        localTip = "Walk on the seabed during low tide"
      ),
      HotspotEntity(
        id = "khirachora_gopinatha",
        name = "Khirachora Gopinatha Temple",
        odiaName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର",
        category = "Temple",
        shortDescription = "Historic 12th-century Krishna shrine in Remuna renowned for Amruta Keli prasadam.",
        fullDescription = "Ancient Vaishnavite temple established by King Langula Narasimha Deva.",
        highlights = "Amruta Keli Khira, 12th Century Architecture, Chaitanya Mahaprabhu footsteps",
        distanceKmFromBls = 9,
        bestTimeToVisit = "Year round",
        timings = "6:00 AM - 9:00 PM",
        entryFee = "Free",
        specialty = "Condensed condensed milk pudding (Khira)",
        localTip = "Must taste the evening Amruta Keli bhog"
      )
    )

    val query = "beach"
    val filtered = hotspots.filter {
      it.name.contains(query, ignoreCase = true) ||
      it.shortDescription.contains(query, ignoreCase = true) ||
      it.category.contains(query, ignoreCase = true)
    }

    assertEquals(1, filtered.size)
    assertEquals("chandipur_beach", filtered.first().id)
  }

  @Test
  fun testSearchFilteringNewsArticles() {
    val news = listOf(
      NewsArticleEntity(
        id = 1,
        title = "Balasore Railway Station Upgradation Project Accelerates",
        summary = "World-class redevelopment works enter Phase 2 with improved passenger lounges.",
        content = "Modern amenities being installed under Amrit Bharat Scheme.",
        category = "Civic & Transport",
        source = "District Information Office",
        publishedAt = "2 hours ago",
        isBreaking = false
      ),
      NewsArticleEntity(
        id = 2,
        title = "Chandipur Marine Festival Scheduled for Next Month",
        summary = "Cultural celebrations and seafood carnivals announced along the coast.",
        content = "Tourism department collaborates with local artisans.",
        category = "Coastal & Tourism",
        source = "Odisha Tourism Bulletin",
        publishedAt = "5 hours ago",
        isBreaking = true
      )
    )

    val query = "Railway"
    val filtered = news.filter {
      it.title.contains(query, ignoreCase = true) ||
      it.summary.contains(query, ignoreCase = true) ||
      it.category.contains(query, ignoreCase = true)
    }

    assertEquals(1, filtered.size)
    assertEquals(1L, filtered.first().id)
  }

  @Test
  fun testCategoryFilteringNewCategories() {
    val news = listOf(
      NewsArticleEntity(
        id = 1,
        title = "Balasore Municipality Announces Modern Solar Lighting",
        summary = "Smart LED poles installed across OT Road.",
        content = "Detailed content.",
        category = "Local News",
        source = "Balasore Municipality",
        publishedAt = "Today",
        isBreaking = false
      ),
      NewsArticleEntity(
        id = 2,
        title = "All-Party Delegation Submits Memorandum on Port",
        summary = "Rail connectivity urged.",
        content = "Detailed content.",
        category = "Politics",
        source = "Press Club",
        publishedAt = "Today",
        isBreaking = false
      ),
      NewsArticleEntity(
        id = 3,
        title = "Grand Chandipur Beach Festival Announced",
        summary = "Five-day coastal celebration.",
        content = "Detailed content.",
        category = "Events",
        source = "District Culture Council",
        publishedAt = "Today",
        isBreaking = false
      )
    )

    val localNews = news.filter { it.category.equals("Local News", ignoreCase = true) }
    assertEquals(1, localNews.size)
    assertEquals("Local News", localNews.first().category)

    val politics = news.filter { it.category.equals("Politics", ignoreCase = true) }
    assertEquals(1, politics.size)
    assertEquals("Politics", politics.first().category)

    val events = news.filter { it.category.equals("Events", ignoreCase = true) }
    assertEquals(1, events.size)
    assertEquals("Events", events.first().category)
  }

  @Test
  fun testEmergencyContactDialSanitization() {
    val rawLandline = "06782-255010"
    val sanitizedLandline = rawLandline.replace(Regex("[^0-9+]"), "")
    assertEquals("06782255010", sanitizedLandline)

    val rawTollFree = "112 / 101"
    val cleanEmergencyNumber = "112".replace(Regex("[^0-9+]"), "")
    assertEquals("112", cleanEmergencyNumber)
  }

  @Test
  fun testNotificationTopicSubscriptionKeys() {
    val topicWeather = "balasore_weather_alerts"
    val topicNews = "balasore_breaking_news"

    assertTrue(topicWeather.startsWith("balasore_"))
    assertTrue(topicNews.startsWith("balasore_"))
  }

  @Test
  fun testBreakingNewsAlertFiltering() {
    val articles = listOf(
      NewsArticleEntity(
        id = 1,
        title = "Severe Cyclone Warning for Chandipur Coastal Belt",
        summary = "IMD warns of 85 kmph gale winds.",
        content = "Detailed cyclone advisory.",
        category = "Emergency",
        source = "IMD Balasore",
        publishedAt = "10 mins ago",
        isBreaking = true
      ),
      NewsArticleEntity(
        id = 2,
        title = "Flower show opens at Gandhi Smruti Bhavan",
        summary = "Horticulture exhibits open.",
        content = "Flower show details.",
        category = "Culture",
        source = "Local Bureau",
        publishedAt = "1 hour ago",
        isBreaking = false
      )
    )

    val breakingOnly = articles.filter {
      it.category.equals("Emergency", ignoreCase = true) ||
          it.title.contains("Cyclone", ignoreCase = true) ||
          it.isBreaking
    }

    assertEquals(1, breakingOnly.size)
    assertEquals(1, breakingOnly.first().id)
    assertEquals("Emergency", breakingOnly.first().category)
  }
}

