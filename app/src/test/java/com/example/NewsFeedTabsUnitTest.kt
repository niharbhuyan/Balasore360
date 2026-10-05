package com.example

import com.example.data.repository.BalasoreRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit Test Suite verifying the top-level news feed filtering tabs:
 * - 'Local News'
 * - 'Defense Alerts'
 * - 'Tourism Updates'
 * - 'All Stories'
 */
class NewsFeedTabsUnitTest {

    private val allArticles = BalasoreRepository.newsArticles

    private fun matchesCategory(cat: String, articleCat: String, title: String, content: String): Boolean {
        return when (cat) {
            "All" -> true
            "Local News" ->
                articleCat.equals("Local News", ignoreCase = true) ||
                articleCat.equals("Local", ignoreCase = true) ||
                articleCat.equals("Infrastructure", ignoreCase = true) ||
                articleCat.equals("Politics", ignoreCase = true) ||
                articleCat.equals("Local Politics", ignoreCase = true) ||
                articleCat.equals("Development", ignoreCase = true) ||
                articleCat.equals("Sports", ignoreCase = true) ||
                articleCat.contains("Civic", ignoreCase = true) ||
                articleCat.contains("Municipal", ignoreCase = true) ||
                title.contains("municipality", ignoreCase = true) ||
                title.contains("station", ignoreCase = true) ||
                title.contains("parishad", ignoreCase = true) ||
                title.contains("drainage", ignoreCase = true) ||
                title.contains("smart city", ignoreCase = true)
            "Defense Alerts" ->
                articleCat.equals("Defense Alerts", ignoreCase = true) ||
                articleCat.equals("Defense", ignoreCase = true) ||
                articleCat.equals("Coastal Alerts", ignoreCase = true) ||
                articleCat.equals("Emergency", ignoreCase = true) ||
                title.contains("DRDO", ignoreCase = true) ||
                title.contains("Missile", ignoreCase = true) ||
                title.contains("ITR", ignoreCase = true) ||
                title.contains("Defense", ignoreCase = true) ||
                title.contains("Alert", ignoreCase = true) ||
                title.contains("Storm", ignoreCase = true) ||
                title.contains("Flood", ignoreCase = true) ||
                title.contains("Siren", ignoreCase = true) ||
                title.contains("Warning", ignoreCase = true) ||
                content.contains("DRDO", ignoreCase = true) ||
                content.contains("missile", ignoreCase = true)
            "Tourism Updates" ->
                articleCat.equals("Tourism Updates", ignoreCase = true) ||
                articleCat.equals("Tourism", ignoreCase = true) ||
                articleCat.equals("Culture", ignoreCase = true) ||
                articleCat.equals("Spiritual", ignoreCase = true) ||
                title.contains("Tourism", ignoreCase = true) ||
                title.contains("Eco-Promenade", ignoreCase = true) ||
                title.contains("Eco-Trek", ignoreCase = true) ||
                title.contains("Gazebos", ignoreCase = true) ||
                title.contains("Vanishing Sea", ignoreCase = true) ||
                title.contains("Mahotsav", ignoreCase = true) ||
                title.contains("Tourists", ignoreCase = true) ||
                title.contains("Eco-Tourism", ignoreCase = true) ||
                title.contains("Wildlife Sanctuary", ignoreCase = true) ||
                title.contains("Darshan Facilitated", ignoreCase = true) ||
                content.contains("tourist", ignoreCase = true) ||
                content.contains("visitors", ignoreCase = true)
            else -> articleCat.equals(cat, ignoreCase = true)
        }
    }

    @Test
    fun testAllStoriesContainsArticles() {
        assertTrue("Articles list must not be empty", allArticles.isNotEmpty())
        val countAll = allArticles.count { article -> matchesCategory("All", article.category, article.title, article.content) }
        assertEquals(allArticles.size, countAll)
    }

    @Test
    fun testLocalNewsTabFilter() {
        val localArticles = allArticles.filter { article -> matchesCategory("Local News", article.category, article.title, article.content) }
        assertTrue("Local News tab must match articles", localArticles.isNotEmpty())

        // Verify that Infrastructure, Politics, Municipal items are matched
        assertTrue("Should include railway station redevelopment", localArticles.any { it.title.contains("Railway Station", ignoreCase = true) })
        assertTrue("Should include municipal smart city", localArticles.any { it.title.contains("Municipal", ignoreCase = true) })
    }

    @Test
    fun testDefenseAlertsTabFilter() {
        val defenseArticles = allArticles.filter { article -> matchesCategory("Defense Alerts", article.category, article.title, article.content) }
        assertTrue("Defense Alerts tab must match articles", defenseArticles.isNotEmpty())

        // Verify that DRDO and flood alerts are matched
        assertTrue("Should include DRDO missile testing", defenseArticles.any { it.title.contains("DRDO", ignoreCase = true) })
        assertTrue("Should include flood alerts", defenseArticles.any { it.title.contains("Flood", ignoreCase = true) })
    }

    @Test
    fun testTourismUpdatesTabFilter() {
        val tourismArticles = allArticles.filter { article -> matchesCategory("Tourism Updates", article.category, article.title, article.content) }
        assertTrue("Tourism Updates tab must match articles", tourismArticles.isNotEmpty())

        // Verify that Remuna Mahotsav and heritage items are matched
        assertTrue("Should include Remuna Khirachora Mahotsav", tourismArticles.any { it.title.contains("Remuna", ignoreCase = true) })
    }

    @Test
    fun testTabsAreMutuallyDistinctCategories() {
        val localArticles = allArticles.filter { article -> matchesCategory("Local News", article.category, article.title, article.content) }
        val defenseArticles = allArticles.filter { article -> matchesCategory("Defense Alerts", article.category, article.title, article.content) }
        val tourismArticles = allArticles.filter { article -> matchesCategory("Tourism Updates", article.category, article.title, article.content) }

        // Core DRDO defense missile test should not be classified as Tourism
        val drdoArticle = allArticles.first { it.title.contains("DRDO", ignoreCase = true) }
        assertTrue(defenseArticles.contains(drdoArticle))
        assertFalse(tourismArticles.contains(drdoArticle))

        // Remuna spiritual mahotsav should not be classified as Defense
        val remunaArticle = allArticles.first { it.title.contains("Remuna", ignoreCase = true) }
        assertTrue(tourismArticles.contains(remunaArticle))
        assertFalse(defenseArticles.contains(remunaArticle))
    }

    @Test
    fun testNewsFeedTabEnumAndFilterObject() {
        val localFiltered = com.example.ui.viewmodel.NewsFeedFilter.filterArticles(allArticles, 0)
        val defenseFiltered = com.example.ui.viewmodel.NewsFeedFilter.filterArticles(allArticles, 1)
        val tourismFiltered = com.example.ui.viewmodel.NewsFeedFilter.filterArticles(allArticles, 2)

        assertTrue(localFiltered.isNotEmpty())
        assertTrue(defenseFiltered.isNotEmpty())
        assertTrue(tourismFiltered.isNotEmpty())

        assertEquals(com.example.ui.viewmodel.NewsFeedTab.LOCAL_NEWS, com.example.ui.viewmodel.NewsFeedTab.fromIndex(0))
        assertEquals(com.example.ui.viewmodel.NewsFeedTab.DEFENSE_ALERTS, com.example.ui.viewmodel.NewsFeedTab.fromIndex(1))
        assertEquals(com.example.ui.viewmodel.NewsFeedTab.TOURISM_UPDATES, com.example.ui.viewmodel.NewsFeedTab.fromIndex(2))

        assertEquals("Local News", com.example.ui.viewmodel.NewsFeedTab.fromIndex(0).title)
        assertEquals("Defense Alerts", com.example.ui.viewmodel.NewsFeedTab.fromIndex(1).title)
        assertEquals("Tourism Updates", com.example.ui.viewmodel.NewsFeedTab.fromIndex(2).title)
    }

    @Test
    fun testViewModelTabFiltering() {
        val viewModel = com.example.ui.viewmodel.BalasoreViewModel()

        // Test Local News tab (0)
        viewModel.selectNewsTab(0)
        assertEquals(0, viewModel.uiState.value.selectedNewsTab)
        assertEquals("Local News", viewModel.uiState.value.selectedNewsCategory)
        assertTrue(viewModel.uiState.value.filteredNewsArticles.isNotEmpty())
        assertTrue(viewModel.uiState.value.filteredNewsArticles.any { it.title.contains("Municipal", ignoreCase = true) || it.title.contains("Station", ignoreCase = true) })

        // Test Defense Alerts tab (1)
        viewModel.selectNewsTab(1)
        assertEquals(1, viewModel.uiState.value.selectedNewsTab)
        assertEquals("Defense Alerts", viewModel.uiState.value.selectedNewsCategory)
        assertTrue(viewModel.uiState.value.filteredNewsArticles.isNotEmpty())
        assertTrue(viewModel.uiState.value.filteredNewsArticles.any { it.title.contains("DRDO", ignoreCase = true) || it.title.contains("Flood", ignoreCase = true) })

        // Test Tourism Updates tab (2)
        viewModel.selectNewsTab(2)
        assertEquals(2, viewModel.uiState.value.selectedNewsTab)
        assertEquals("Tourism Updates", viewModel.uiState.value.selectedNewsCategory)
        assertTrue(viewModel.uiState.value.filteredNewsArticles.isNotEmpty())
        assertTrue(viewModel.uiState.value.filteredNewsArticles.any { it.title.contains("Remuna", ignoreCase = true) || it.title.contains("Vanishing Sea", ignoreCase = true) })
    }
}
