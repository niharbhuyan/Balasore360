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
}
