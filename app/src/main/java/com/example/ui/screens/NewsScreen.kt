package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Switch
import com.example.util.ShareHelper
import androidx.compose.material3.Surface
import com.example.ui.viewmodel.ThemeMode
import com.example.ui.theme.LocalBentoPalette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.CycloneShelter
import com.example.data.model.NewsArticle
import com.example.data.model.RiverGauge
import com.example.ui.components.AdMobBannerCard
import com.example.ui.components.NewsListSkeleton
import com.example.ui.components.NewsCardSkeleton
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.example.ui.viewmodel.UniqueFeatureSheetType
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.GraphicEq
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.util.NewsTextToSpeechHelper

@Composable
fun NewsScreen(
    articles: List<NewsArticle>,
    riverGauges: List<RiverGauge>,
    cycloneShelters: List<CycloneShelter>,
    selectedCategory: String,
    language: AppLanguage,
    bookmarkedIds: Set<String>,
    isRefreshing: Boolean,
    onCategorySelected: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onRefresh: () -> Unit,
    onOpenFeatureSheet: (UniqueFeatureSheetType) -> Unit = {},
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onToggleNightMode: () -> Unit = {},
    modifier: Modifier = Modifier,
    isAutoUpdateEnabled: Boolean = true,
    autoUpdateIntervalSeconds: Int = 45,
    onToggleAutoUpdate: (Boolean) -> Unit = {},
    onChangeAutoUpdateInterval: (Int) -> Unit = {},
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {},
    filteredArticles: List<NewsArticle>? = null
) {
    val context = LocalContext.current
    var autoUpdateSecondsLeft by remember { mutableStateOf(autoUpdateIntervalSeconds) }
    var selectedArticleForDetail by remember { mutableStateOf<NewsArticle?>(null) }

    // If an article is selected, display the dedicated NewsDetailScreen with full regional TTS player
    if (selectedArticleForDetail != null) {
        val currentArticle = selectedArticleForDetail!!
        NewsDetailScreen(
            article = currentArticle,
            onNavigateBack = { selectedArticleForDetail = null },
            onToggleBookmark = {
                onToggleBookmark(currentArticle.id)
                selectedArticleForDetail = currentArticle.copy(
                    isBookmarked = !bookmarkedIds.contains(currentArticle.id)
                )
            },
            onToggleNightMode = onToggleNightMode
        )
        return
    }

    // Auto-update countdown and automatic background refresh ticker
    LaunchedEffect(isAutoUpdateEnabled, autoUpdateIntervalSeconds) {
        autoUpdateSecondsLeft = autoUpdateIntervalSeconds
        if (isAutoUpdateEnabled) {
            while (true) {
                delay(1000L)
                if (autoUpdateSecondsLeft > 1) {
                    autoUpdateSecondsLeft -= 1
                } else {
                    autoUpdateSecondsLeft = autoUpdateIntervalSeconds
                    onRefresh()
                }
            }
        }
    }

    val categories = listOf(
        "All",
        "Local News",
        "Defense Alerts",
        "Tourism Updates",
        "Saved Bookmarks",
        "Local Politics",
        "Coastal Alerts",
        "Local",
        "Coastal",
        "Spiritual",
        "Emergency",
        "Weather",
        "Culture",
        "Defense",
        "Sports",
        "Development"
    )

    val matchesCategoryForArticle: (NewsArticle, String) -> Boolean = { article, cat ->
        when (cat) {
            "All" -> true
            "Saved Bookmarks", "Bookmarks", "Saved" ->
                article.isBookmarked || bookmarkedIds.contains(article.id)
            "Local News" ->
                article.category.equals("Local News", ignoreCase = true) ||
                article.category.equals("Local", ignoreCase = true) ||
                article.category.equals("Infrastructure", ignoreCase = true) ||
                article.category.equals("Politics", ignoreCase = true) ||
                article.category.equals("Local Politics", ignoreCase = true) ||
                article.category.equals("Development", ignoreCase = true) ||
                article.category.equals("Sports", ignoreCase = true) ||
                article.category.contains("Civic", ignoreCase = true) ||
                article.category.contains("Municipal", ignoreCase = true) ||
                article.title.contains("municipality", ignoreCase = true) ||
                article.title.contains("station", ignoreCase = true) ||
                article.title.contains("parishad", ignoreCase = true) ||
                article.title.contains("drainage", ignoreCase = true) ||
                article.title.contains("smart city", ignoreCase = true)
            "Defense Alerts" ->
                article.category.equals("Defense Alerts", ignoreCase = true) ||
                article.category.equals("Defense", ignoreCase = true) ||
                article.category.equals("Coastal Alerts", ignoreCase = true) ||
                article.category.equals("Emergency", ignoreCase = true) ||
                article.title.contains("DRDO", ignoreCase = true) ||
                article.title.contains("Missile", ignoreCase = true) ||
                article.title.contains("ITR", ignoreCase = true) ||
                article.title.contains("Defense", ignoreCase = true) ||
                article.title.contains("Alert", ignoreCase = true) ||
                article.title.contains("Storm", ignoreCase = true) ||
                article.title.contains("Flood", ignoreCase = true) ||
                article.title.contains("Siren", ignoreCase = true) ||
                article.title.contains("Warning", ignoreCase = true) ||
                article.content.contains("DRDO", ignoreCase = true) ||
                article.content.contains("missile", ignoreCase = true)
            "Local Politics", "Politics" ->
                article.category.equals("Local Politics", ignoreCase = true) ||
                article.category.equals("Politics", ignoreCase = true) ||
                article.category.contains("Civic", ignoreCase = true) ||
                article.category.contains("Municipal", ignoreCase = true) ||
                article.title.contains("politics", ignoreCase = true) ||
                article.title.contains("parishad", ignoreCase = true) ||
                article.title.contains("municipality", ignoreCase = true) ||
                article.title.contains("ward", ignoreCase = true) ||
                article.title.contains("councillor", ignoreCase = true) ||
                article.title.contains("panchayat", ignoreCase = true) ||
                article.title.contains("collector", ignoreCase = true) ||
                article.title.contains("administration", ignoreCase = true) ||
                article.content.contains("zilla parishad", ignoreCase = true) ||
                article.content.contains("political", ignoreCase = true)
            "Coastal Alerts" ->
                article.category.equals("Coastal Alerts", ignoreCase = true) ||
                article.title.contains("Storm Surge", ignoreCase = true) ||
                article.title.contains("Tide Advisory", ignoreCase = true) ||
                article.title.contains("Navigation Alert", ignoreCase = true) ||
                article.title.contains("Coastal Alert", ignoreCase = true) ||
                article.title.contains("Flood Monitoring", ignoreCase = true) ||
                article.title.contains("Bay of Bengal Weather Alert", ignoreCase = true) ||
                (article.category.equals("Coastal", ignoreCase = true) &&
                        (article.title.contains("Alert", ignoreCase = true) || article.snippet.contains("Alert", ignoreCase = true) || article.content.contains("siren", ignoreCase = true)))
            "Tourism Updates" ->
                article.category.equals("Tourism Updates", ignoreCase = true) ||
                article.category.equals("Tourism", ignoreCase = true) ||
                article.category.equals("Culture", ignoreCase = true) ||
                article.category.equals("Spiritual", ignoreCase = true) ||
                article.title.contains("Tourism", ignoreCase = true) ||
                article.title.contains("Eco-Promenade", ignoreCase = true) ||
                article.title.contains("Eco-Trek", ignoreCase = true) ||
                article.title.contains("Gazebos", ignoreCase = true) ||
                article.title.contains("Vanishing Sea", ignoreCase = true) ||
                article.title.contains("Mahotsav", ignoreCase = true) ||
                article.title.contains("Tourists", ignoreCase = true) ||
                article.title.contains("Eco-Tourism", ignoreCase = true) ||
                article.title.contains("Wildlife Sanctuary", ignoreCase = true) ||
                article.title.contains("Darshan Facilitated", ignoreCase = true) ||
                article.content.contains("tourist", ignoreCase = true) ||
                article.content.contains("visitors", ignoreCase = true)
            "Local" -> article.category.equals("Local", ignoreCase = true) ||
                       article.category.equals("Infrastructure", ignoreCase = true) ||
                       article.category.contains("Civic", ignoreCase = true)
            "Coastal" -> article.category.equals("Coastal", ignoreCase = true) ||
                         article.category.equals("Coastal Alerts", ignoreCase = true) ||
                         article.category.contains("Marine", ignoreCase = true) ||
                         article.category.contains("Tide", ignoreCase = true) ||
                         article.title.contains("Chandipur", ignoreCase = true) ||
                         article.title.contains("Talasari", ignoreCase = true) ||
                         article.title.contains("Balaramgadi", ignoreCase = true) ||
                         article.title.contains("Coastal", ignoreCase = true) ||
                         article.title.contains("Sea", ignoreCase = true) ||
                         article.content.contains("Bay of Bengal", ignoreCase = true) ||
                         article.category.equals("Defense", ignoreCase = true)
            "Spiritual" -> article.category.equals("Spiritual", ignoreCase = true) ||
                           article.category.equals("Culture", ignoreCase = true) ||
                           article.category.contains("Temple", ignoreCase = true) ||
                           article.title.contains("Gopinath", ignoreCase = true) ||
                           article.title.contains("Temple", ignoreCase = true) ||
                           article.title.contains("Mahotsav", ignoreCase = true) ||
                           article.title.contains("Jagannath", ignoreCase = true) ||
                           article.title.contains("Panchalingeswar", ignoreCase = true) ||
                           article.content.contains("devotees", ignoreCase = true) ||
                           article.content.contains("pilgrimage", ignoreCase = true)
            "Sports" -> article.category.contains("Sports", ignoreCase = true) ||
                        article.category.contains("Athletic", ignoreCase = true)
            "Emergency" -> article.category.contains("Emergency", ignoreCase = true) ||
                           article.category.contains("Alert", ignoreCase = true) ||
                           article.category.contains("Disaster", ignoreCase = true)
            "Weather" -> article.category.contains("Weather", ignoreCase = true) ||
                         article.category.contains("Tide", ignoreCase = true) ||
                         article.category.contains("Marine", ignoreCase = true)
            else -> article.category.equals(cat, ignoreCase = true)
        }
    }

    val categoryCounts = remember(articles) {
        categories.associateWith { cat ->
            articles.count { matchesCategoryForArticle(it, cat) }
        }
    }

    val currentNewsTab = when {
        selectedCategory.equals("Defense Alerts", ignoreCase = true) || selectedCategory.equals("Defense", ignoreCase = true) || selectedCategory.equals("Coastal Alerts", ignoreCase = true) || selectedCategory.equals("Emergency", ignoreCase = true) -> 1
        selectedCategory.equals("Tourism Updates", ignoreCase = true) || selectedCategory.equals("Tourism", ignoreCase = true) || selectedCategory.equals("Culture", ignoreCase = true) || selectedCategory.equals("Spiritual", ignoreCase = true) -> 2
        selectedCategory.equals("Local News", ignoreCase = true) || selectedCategory.equals("Local", ignoreCase = true) || selectedCategory.equals("Infrastructure", ignoreCase = true) || selectedCategory.equals("Politics", ignoreCase = true) || selectedCategory.equals("Local Politics", ignoreCase = true) -> 0
        else -> selectedTab.coerceIn(0, 2)
    }

    // Filter articles by category and local search keywords across titles, body snippets, and full narrative content
    val filteredArticles = remember(articles, filteredArticles, selectedCategory, currentNewsTab, searchQuery) {
        if (filteredArticles != null && searchQuery.isEmpty()) {
            filteredArticles
        } else {
            articles.filter { article ->
                val matchesCategory = when (currentNewsTab) {
                    0 -> matchesCategoryForArticle(article, "Local News")
                    1 -> matchesCategoryForArticle(article, "Defense Alerts")
                    2 -> matchesCategoryForArticle(article, "Tourism Updates")
                    else -> matchesCategoryForArticle(article, selectedCategory)
                }
                val query = searchQuery.trim()
                val matchesSearch = query.isEmpty() ||
                    article.title.contains(query, ignoreCase = true) ||
                    article.odiaTitle.contains(query, ignoreCase = true) ||
                    article.snippet.contains(query, ignoreCase = true) ||
                    article.odiaSnippet.contains(query, ignoreCase = true) ||
                    article.content.contains(query, ignoreCase = true) ||
                    article.odiaContent.contains(query, ignoreCase = true) ||
                    article.category.contains(query, ignoreCase = true) ||
                    article.source.contains(query, ignoreCase = true)
                matchesCategory && matchesSearch
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("news_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Section Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ସମାଚାର" else "Balasore Live News",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସଦ୍ୟତମ ସ୍ଥାନୀୟ ଖବର ଓ ସୂଚନା" else "Hyper-local updates & district press bulletins",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("refresh_news_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Material-3 TabRow Component: 'Local News', 'Defense Alerts', and 'Tourism Updates'
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("news_top_level_tabs_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF0F172A) else Color.White
                ),
                border = BorderStroke(1.dp, if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF334155) else BentoSlate200),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସମ୍ବାଦ ବର୍ଗ (CATEGORY)" else "NEWS CATEGORIES",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 0.8.sp,
                                fontSize = 10.sp,
                                color = OceanBlue
                            )
                        )
                        Text(
                            text = when (currentNewsTab) {
                                0 -> if (language == AppLanguage.ODIA) "ସ୍ଥାନୀୟ ପ୍ରଗତି ଓ ଶାସନ" else "Civic & Development"
                                1 -> if (language == AppLanguage.ODIA) "DRDO ଓ ଉପକୂଳ ସତର୍କତା" else "Defense & Alerts"
                                2 -> if (language == AppLanguage.ODIA) "ସଂସ୍କୃତି ଓ ପର୍ଯ୍ୟଟନ" else "Tourism & Heritage"
                                else -> if (language == AppLanguage.ODIA) "ସମସ୍ତ ଖବର" else "All Stories"
                            },
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.5.sp,
                                color = BentoSlate500
                            )
                        )
                    }

                    TabRow(
                        selectedTabIndex = currentNewsTab,
                        containerColor = Color.Transparent,
                        contentColor = OceanBlue,
                        indicator = { tabPositions ->
                            if (currentNewsTab in tabPositions.indices) {
                                TabRowDefaults.SecondaryIndicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[currentNewsTab]),
                                    color = OceanBlue,
                                    height = 3.dp
                                )
                            }
                        },
                        divider = {
                            HorizontalDivider(
                                color = if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF334155) else BentoSlate200,
                                thickness = 1.dp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("news_material3_tab_row")
                    ) {
                        val m3Tabs = listOf(
                            Triple("Local News", if (language == AppLanguage.ODIA) "ସ୍ଥାନୀୟ ଖବର" else "Local News", Icons.Default.LocationCity),
                            Triple("Defense Alerts", if (language == AppLanguage.ODIA) "ପ୍ରତିରକ୍ଷା ସତର୍କତା" else "Defense Alerts", Icons.Default.Shield),
                            Triple("Tourism Updates", if (language == AppLanguage.ODIA) "ପର୍ଯ୍ୟଟନ ଖବର" else "Tourism Updates", Icons.Default.Explore)
                        )

                        m3Tabs.forEachIndexed { index, (catId, label, icon) ->
                            val isSelected = currentNewsTab == index
                            val count = articles.count { matchesCategoryForArticle(it, catId) }
                            val testTagKey = when (index) {
                                0 -> "news_tab_local"
                                1 -> "news_tab_defense"
                                else -> "news_tab_tourism"
                            }

                            Tab(
                                selected = isSelected,
                                onClick = {
                                    onTabSelected(index)
                                    onCategorySelected(catId)
                                },
                                text = {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 11.5.sp,
                                            color = if (isSelected) OceanBlue else (if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF94A3B8) else BentoSlate600)
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                icon = {
                                    BadgedBox(
                                        badge = {
                                            Badge(
                                                containerColor = if (isSelected) OceanBlue else (if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF334155) else BentoSlate200),
                                                contentColor = if (isSelected) Color.White else BentoSlate700
                                            ) {
                                                Text(
                                                    text = "$count",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = label,
                                            tint = if (isSelected) OceanBlue else (if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF94A3B8) else BentoSlate600),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                },
                                modifier = Modifier.testTag(testTagKey)
                            )
                        }
                    }
                }
            }
        }

        // Live Auto-Update Engine Telemetry & Feeds Auto-Refresh Control Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("news_auto_update_control_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAutoUpdateEnabled) {
                        if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF0C243B) else Color(0xFFEFF6FF)
                    } else {
                        if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF1E293B) else BentoSlate100
                    }
                ),
                border = BorderStroke(
                    1.dp,
                    if (isAutoUpdateEnabled) OceanBlue.copy(alpha = 0.45f) else BentoSlate200
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (isAutoUpdateEnabled) EmeraldGreen else BentoSlate400)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAutoUpdateEnabled) {
                                    if (language == AppLanguage.ODIA) "ସ୍ୱୟଂକ୍ରିୟ ଅପଡେଟ୍: ସକ୍ରିୟ" else "LIVE AUTO-UPDATES: ACTIVE"
                                } else {
                                    if (language == AppLanguage.ODIA) "ସ୍ୱୟଂକ୍ରିୟ ଅପଡେଟ୍: ସ୍ଥଗିତ" else "AUTO-UPDATES: PAUSED"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.8.sp,
                                    color = if (isAutoUpdateEnabled) OceanBlue else BentoSlate600
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଅଟୋ ସିଙ୍କ୍" else "Auto-Sync",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Switch(
                                checked = isAutoUpdateEnabled,
                                onCheckedChange = { onToggleAutoUpdate(it) },
                                modifier = Modifier.testTag("news_auto_update_switch")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAutoUpdateEnabled) {
                                if (language == AppLanguage.ODIA) {
                                    "ପରବର୍ତ୍ତୀ ସ୍ୱୟଂକ୍ରିୟ ଖବର ଅପଡେଟ୍ ~${autoUpdateSecondsLeft} ସେକେଣ୍ଡରେ • ସମସ୍ତ ଫିଡ୍ ଲାଇଭ୍"
                                } else {
                                    "Next feed auto-update in ~${autoUpdateSecondsLeft}s • Feeds synchronized live"
                                }
                            } else {
                                if (language == AppLanguage.ODIA) {
                                    "ସ୍ୱୟଂକ୍ରିୟ ଖବର ଅପଡେଟ୍ ସ୍ଥଗିତ • ହାତରେ ସିଙ୍କ୍ କରିବାକୁ ରିଫ୍ରେସ୍ ଦବାନ୍ତୁ"
                                } else {
                                    "Auto-update paused • Tap refresh for manual update"
                                }
                            },
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = onRefresh,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(OceanBlue.copy(alpha = 0.12f))
                                .testTag("news_quick_auto_sync_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Sync now",
                                tint = OceanBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସମୟାନ୍ତର:" else "Interval:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        val intervals = listOf(30 to "30s", 45 to "45s", 60 to "60s", 120 to "2m")
                        intervals.forEach { (sec, label) ->
                            val isSel = autoUpdateIntervalSeconds == sec
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) OceanBlue else (if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF1E293B) else Color.White),
                                border = BorderStroke(1.dp, if (isSel) OceanBlue else BentoSlate200),
                                modifier = Modifier
                                    .clickable { onChangeAutoUpdateInterval(sec) }
                                    .testTag("news_auto_interval_$label")
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp,
                                        color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Global High-Contrast Night-Reading Mode Quick Toggle Bar
        item {
            val isNightMode = themeMode == ThemeMode.HIGH_CONTRAST_DARK
            Surface(
                onClick = onToggleNightMode,
                shape = RoundedCornerShape(14.dp),
                color = if (isNightMode) Color(0xFF0F172A) else MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(1.dp, if (isNightMode) Color(0xFF38BDF8) else Color.Transparent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("news_night_reading_toggle_bar")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isNightMode) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isNightMode) Icons.Default.NightlightRound else Icons.Default.DarkMode,
                                    contentDescription = "Night reading mode",
                                    tint = if (isNightMode) Color(0xFFFBBF24) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isNightMode) {
                                    if (language == AppLanguage.ODIA) "ନିଶାର୍ଦ୍ଧ ହାଇ-କଣ୍ଟ୍ରାଷ୍ଟ ଡାର୍କ ମୋଡ୍ (ସକ୍ରିୟ)" else "High-Contrast Night Mode (Active)"
                                } else {
                                    if (language == AppLanguage.ODIA) "ନିଶାର୍ଦ୍ଧ ଖବର ପାଠ ପାଇଁ ଡାର୍କ ମୋଡ୍" else "Night-Time News Reading Mode"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isNightMode) Color(0xFFFFFFFF) else MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଚକ୍ଷୁ ଶ୍ରାନ୍ତି ଦୂର ଓ ସ୍ପଷ୍ଟ ପଠନ ପାଇଁ OLED କଣ୍ଟ୍ରାଷ୍ଟ" else "Pure black & glare-free high contrast for night reading",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    color = if (isNightMode) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                    Switch(
                        checked = isNightMode,
                        onCheckedChange = { onToggleNightMode() },
                        modifier = Modifier.testTag("news_night_mode_switch")
                    )
                }
            }
        }

        // FEATURE 4: River Water Level & Monsoon Flood Early Warning Telemetry Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("river_flood_telemetry_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Water,
                                    contentDescription = "River Gauge",
                                    tint = OceanBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ନଦୀ ଜଳସ୍ତର ଓ ବନ୍ୟା ସତର୍କତା" else "River Levels & Flood Early Warning",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "Central Water Commission & SRC Odisha",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "MONITORED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF166534),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    riverGauges.forEach { gauge ->
                        RiverGaugeItemView(gauge = gauge, language = language)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Cyclone Shelter Directory
                    CycloneSheltersView(shelters = cycloneShelters, language = language)

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.CYCLONE_RESILIENCE) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("news_open_cyclone_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Open Offline Cyclone Shelter Map & Checklist", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.CITIZEN_CIVIC_EYE) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("news_open_civic_eye_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("📸", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଆମ ବାଲେଶ୍ୱର: ନାଗରିକ ଓ ଡ୍ରେନେଜ୍ ଅଭିଯୋଗ ଦାଖଲ" else "Citizen Civic Eye: Report Potholes & Drains",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Category Filter Chips & Local Search Field
        item {
            val popularSearchKeywords = remember {
                listOf(
                    "Amrit Bharat",
                    "DRDO Chandipur",
                    "High Tide",
                    "Permit Field",
                    "Red Cross Blood",
                    "Balaramgadi Fish",
                    "Subarnarekha",
                    "Remuna Gopinath",
                    "Ring Road"
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Local Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    placeholder = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଶିରୋନାମା ବା ବିବରଣୀରୁ ଖବର ଖୋଜନ୍ତୁ..." else "Search news by title or content keywords...",
                            fontSize = 13.sp,
                            color = BentoSlate400
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search News",
                            tint = OceanBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(
                                onClick = { onSearchQueryChanged("") },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Search",
                                    tint = BentoSlate500,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OceanBlue,
                        unfocusedBorderColor = BentoSlate200,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("news_search_text_field")
                )

                // Quick Trending Keywords Row (Shown when search is empty)
                if (searchQuery.isBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                            contentDescription = null,
                            tint = OceanBlue,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଲୋକପ୍ରିୟ:" else "Trending:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate600
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.testTag("news_trending_tags_row")
                        ) {
                            items(popularSearchKeywords) { kw ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF1F5F9))
                                        .clickable { onSearchQueryChanged(kw) }
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = kw,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BentoSlate700
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Active Search Summary Banner (when searching)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF))
                            .border(BorderStroke(1.dp, Color(0xFFBFDBFE)), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("news_search_summary_banner"),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ManageSearch,
                                contentDescription = null,
                                tint = OceanBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA)
                                    "\"${searchQuery.trim()}\" ପାଇଁ ${filteredArticles.size} ଟି ଖବର ମିଳିଲା (ଶିରୋନାମା ଓ ବିବରଣୀ)"
                                else
                                    "Found ${filteredArticles.size} updates matching \"${searchQuery.trim()}\"",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OceanBlueDark
                            )
                        }

                        Text(
                            text = if (language == AppLanguage.ODIA) "ସଫା କରନ୍ତୁ ✕" else "Clear ✕",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue,
                            modifier = Modifier
                                .clickable { onSearchQueryChanged("") }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
                    .testTag("news_category_filter_row"),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    val count = categoryCounts[cat] ?: 0
                    val emoji = when (cat) {
                        "All" -> "🌐"
                        "Saved Bookmarks", "Bookmarks" -> "🔖"
                        "Local Politics", "Politics" -> "🗳️"
                        "Coastal Alerts" -> "⚠️"
                        "Tourism Updates" -> "🏖️"
                        "Local" -> "📍"
                        "Coastal" -> "🌊"
                        "Spiritual" -> "🛕"
                        "Sports" -> "🏏"
                        "Emergency" -> "🚨"
                        "Weather" -> "🌦️"
                        "Development" -> "🏗️"
                        "Culture" -> "🏛️"
                        "Defense" -> "🚀"
                        else -> "📰"
                    }
                    val displayLabel = if (language == AppLanguage.ODIA) {
                        when (cat) {
                            "All" -> "ସମସ୍ତ"
                            "Saved Bookmarks", "Bookmarks" -> "ସାଇତା ଖବର"
                            "Local Politics", "Politics" -> "ସ୍ଥାନୀୟ ରାଜନୀତି"
                            "Coastal Alerts" -> "ଉପକୂଳ ସତର୍କତା"
                            "Tourism Updates" -> "ପର୍ଯ୍ୟଟନ ଖବର"
                            "Local" -> "ସ୍ଥାନୀୟ"
                            "Coastal" -> "ଉପକୂଳ"
                            "Spiritual" -> "ଆଧ୍ୟାତ୍ମିକ"
                            "Sports" -> "କ୍ରୀଡ଼ା"
                            "Emergency" -> "ଜରୁରୀକାଳୀନ"
                            "Weather" -> "ପାଣିପାଗ"
                            "Development" -> "ବିକାଶ"
                            "Culture" -> "ସଂସ୍କୃତି"
                            "Defense" -> "ପ୍ରତିରକ୍ଷା"
                            else -> cat
                        }
                    } else {
                        cat
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelected(cat) },
                        leadingIcon = {
                            Text(emoji, fontSize = 12.sp)
                        },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = displayLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.White.copy(alpha = 0.25f) else BentoSlate200)
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "$count",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else BentoSlate700
                                    )
                                }
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue,
                            selectedLabelColor = Color.White,
                            containerColor = BentoSlate100,
                            labelColor = BentoSlate700
                        ),
                        border = null,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("news_filter_chip_${cat.lowercase()}")
                    )
                }
            }

            // Active category indicator and quick reset banner
            if (selectedCategory != "All") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val activeLabel = if (language == AppLanguage.ODIA) {
                        when (selectedCategory) {
                            "Local" -> "ସ୍ଥାନୀୟ ଖବର"
                            "Sports" -> "କ୍ରୀଡ଼ା ସମାଚାର"
                            "Emergency" -> "ଜରୁରୀକାଳୀନ ସୂଚନା"
                            "Weather" -> "ପାଣିପାଗ ଓ ଉପକୂଳ ସତର୍କତା"
                            "Development" -> "ବିକାଶମୂଳକ ଖବର"
                            "Culture" -> "ସାଂସ୍କୃତିକ ଖବର"
                            "Defense" -> "ପ୍ରତିରକ୍ଷା ଅପଡେଟ୍"
                            "Politics" -> "ରାଜନୈତିକ ଖବର"
                            else -> selectedCategory
                        }
                    } else {
                        "Filtered by: $selectedCategory"
                    }

                    Text(
                        text = "$activeLabel (${filteredArticles.size})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue
                        )
                    )

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onCategorySelected("All") }
                            .background(Color(0xFFE0F2FE))
                            .padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear category filter",
                            tint = OceanBlue,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସବୁ ଦେଖନ୍ତୁ" else "Show All",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue
                        )
                    }
                }
            }
        }

        // Loading skeleton when data is being fetched or refreshed
        if (isRefreshing) {
            item {
                NewsListSkeleton(count = 4, showBreakingBanner = true)
            }
        } else {
            // Dedicated Offline Reading Mode Banner when viewing Saved Bookmarks
            if (selectedCategory == "Saved Bookmarks" || selectedCategory == "Bookmarks") {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("news_offline_bookmarks_banner"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF0C243B) else Color(0xFFEFF6FF)
                        ),
                        border = BorderStroke(1.dp, OceanBlue.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(OceanBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = "Saved Bookmarks",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ ପଠନ ପାଇଁ ସାଇତା ଖବର" else "Saved Stories for Offline Reading",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color.White else BentoSlate900
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = EmeraldGreen.copy(alpha = 0.15f),
                                        border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = "Room DB",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = EmeraldGreen,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA)
                                        "ଏହି ସମସ୍ତ ${filteredArticles.size} ଟି ଖବର ସ୍ଥାନୀୟ Room ଡାଟାବେସରେ ସାଇତା ଅଛି ଏବଂ ଇଣ୍ଟରନେଟ୍ ବିନା ମଧ୍ୟ ସମ୍ପୂର୍ଣ୍ଣ ପଢ଼ାଯାଇପାରିବ।"
                                    else
                                        "${filteredArticles.size} stories stored locally in Room database. Fully accessible without active internet connection.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (themeMode == ThemeMode.HIGH_CONTRAST_DARK) Color(0xFF94A3B8) else BentoSlate600,
                                        fontSize = 11.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            if (filteredArticles.isEmpty()) {
                item {
                    if (selectedCategory == "Saved Bookmarks" || selectedCategory == "Bookmarks") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .testTag("news_empty_bookmarks_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(OceanBlue.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkBorder,
                                        contentDescription = null,
                                        tint = OceanBlue,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "କୌଣସି ଖବର ସାଇତା ହୋଇନାହିଁ" else "No Saved Stories Yet",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA)
                                        "ଅଫଲାଇନ୍ରେ ପଢ଼ିବା ପାଇଁ ଯେକୌଣସି ଖବରର ବୁକ୍‌ମାର୍କ ଆଇକନ୍ (🔖) ଉପରେ କ୍ଲିକ୍ କରି Room ଡାଟାବେସରେ ସାଇତି ରଖନ୍ତୁ।"
                                    else
                                        "Tap the bookmark icon (🔖) on any news story to save it to your local Room database for offline reading even without internet.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = BentoSlate500,
                                        fontSize = 12.5.sp,
                                        lineHeight = 18.sp
                                    ),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(18.dp))
                                Button(
                                    onClick = { onCategorySelected("All") },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) "ସମସ୍ତ ଖବର ଦେଖନ୍ତୁ" else "Browse All News Stories",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    } else {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .testTag("news_empty_search_card"),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("📰", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "କୌଣସି ଖବର ମିଳିଲା ନାହିଁ" else "No News Articles Found",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA)
                                        "'$searchQuery' ପାଇଁ କୌଣସି ଆର୍ଟିକିଲ୍ ମିଳିଲା ନାହିଁ।"
                                    else
                                        "No articles match the keyword '$searchQuery' in category '$selectedCategory'.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        onSearchQueryChanged("")
                                        onCategorySelected("All")
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Reset Search Filters", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else {
            // Articles List
            items(filteredArticles, key = { it.id }) { article ->
                NewsCard(
                    article = article,
                    language = language,
                    searchQuery = searchQuery,
                    isBookmarked = bookmarkedIds.contains(article.id),
                    onToggleBookmark = { onToggleBookmark(article.id) },
                    onArticleClick = { selectedArticleForDetail = article },
                    onShare = {
                        ShareHelper.shareNews(
                            context = context,
                            article = article,
                            isOdia = language == AppLanguage.ODIA
                        )
                    }
                )
            }
        }
    }

    // AdMob Banner Placement in News feed
    item {
        AdMobBannerCard(
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
}

@Composable
fun HighlightedNewsText(
    text: String,
    query: String,
    style: TextStyle,
    highlightColor: Color = Color(0xFFFEF08A),
    textColor: Color = BentoSlate900,
    modifier: Modifier = Modifier
) {
    val trimmed = query.trim()
    if (trimmed.isEmpty() || !text.contains(trimmed, ignoreCase = true)) {
        Text(text = text, style = style, color = textColor, modifier = modifier)
        return
    }

    val annotated = buildAnnotatedString {
        var currentIndex = 0
        val lowerText = text.lowercase()
        val lowerQuery = trimmed.lowercase()

        while (currentIndex < text.length) {
            val matchIndex = lowerText.indexOf(lowerQuery, currentIndex)
            if (matchIndex == -1) {
                append(text.substring(currentIndex))
                break
            }
            if (matchIndex > currentIndex) {
                append(text.substring(currentIndex, matchIndex))
            }
            val matchEnd = matchIndex + lowerQuery.length
            pushStyle(
                SpanStyle(
                    background = highlightColor,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF78350F)
                )
            )
            append(text.substring(matchIndex, matchEnd))
            pop()
            currentIndex = matchEnd
        }
    }

    Text(text = annotated, style = style, modifier = modifier)
}

@Composable
fun NewsCard(
    article: NewsArticle,
    language: AppLanguage,
    searchQuery: String = "",
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onShare: () -> Unit,
    onArticleClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val ttsHelper = remember { NewsTextToSpeechHelper.getInstance(context) }
    val isTtsSpeaking by ttsHelper.isSpeaking.collectAsStateWithLifecycle()
    val currentlySpeakingId by ttsHelper.currentArticleId.collectAsStateWithLifecycle()
    val isThisArticleSpeaking = isTtsSpeaking && (currentlySpeakingId == article.id)

    val titleText = if (language == AppLanguage.ODIA) article.odiaTitle else article.title
    val snippetText = if (language == AppLanguage.ODIA) article.odiaSnippet else article.snippet
    val contentText = if (language == AppLanguage.ODIA) article.odiaContent else article.content
    val query = searchQuery.trim()

    val isMatchedInTitle = query.isNotEmpty() && titleText.contains(query, ignoreCase = true)
    val isMatchedInSnippet = query.isNotEmpty() && snippetText.contains(query, ignoreCase = true)
    val isMatchedInContent = query.isNotEmpty() && contentText.contains(query, ignoreCase = true)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(enabled = onArticleClick != null) { onArticleClick?.invoke() }
            .testTag("news_card_${article.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Category badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(OceanBlue.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = article.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OceanBlueDark
                            )
                        )
                    }

                    if (isBookmarked) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFDCFCE7),
                            border = BorderStroke(1.dp, Color(0xFF86EFAC))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    modifier = Modifier.size(10.dp),
                                    tint = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଅଫଲାଇନ୍ ସାଇତା" else "Offline Ready (Room)",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = article.timeAgo,
                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Highlighted Title
            HighlightedNewsText(
                text = titleText,
                query = query,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Highlighted Snippet
            HighlightedNewsText(
                text = snippetText,
                query = query,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            )

            // Context Excerpt if keyword matched in full article text but not snippet/title
            if (isMatchedInContent && !isMatchedInTitle && !isMatchedInSnippet) {
                val matchIdx = contentText.indexOf(query, ignoreCase = true)
                val start = (matchIdx - 35).coerceAtLeast(0)
                val end = (matchIdx + query.length + 55).coerceAtMost(contentText.length)
                val excerpt = "${if (start > 0) "..." else ""}${contentText.substring(start, end)}${if (end < contentText.length) "..." else ""}"

                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .border(BorderStroke(1.dp, Color(0xFFFCD34D)), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଆର୍ଟିକିଲ୍ ମଧ୍ୟରେ ମିଳିଥିବା ଅଂଶ:" else "Matched inside article text:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        HighlightedNewsText(
                            text = excerpt,
                            query = query,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, color = Color(0xFF78350F), lineHeight = 18.sp),
                            highlightColor = Color(0xFFFDE68A)
                        )
                    }
                }
            }

            // Expandable Full Story View
            if (contentText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { isExpanded = !isExpanded }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = OceanBlue,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isExpanded) {
                                if (language == AppLanguage.ODIA) "ସମ୍ପୂର୍ଣ୍ଣ ଖବର ବନ୍ଦ କରନ୍ତୁ" else "Collapse Full Story"
                            } else {
                                if (language == AppLanguage.ODIA) "ସମ୍ପୂର୍ଣ୍ଣ ଖବର ପଢ଼ନ୍ତୁ" else "Read Full Story"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue
                        )
                    }

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = OceanBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(BorderStroke(1.dp, BentoSlate100), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        val words = contentText.split("\\s+".toRegex()).size
                        val readingMinutes = (words / 130).coerceAtLeast(1)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ସମ୍ପୂର୍ଣ୍ଣ ବିବରଣୀ" else "Full Detailed Report",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate700
                            )
                            Text(
                                text = if (language == AppLanguage.ODIA) "$readingMinutes ମିନିଟ୍ ପଠନ ($words ଶବ୍ଦ)" else "$readingMinutes min read ($words words)",
                                fontSize = 10.sp,
                                color = BentoSlate400
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        HighlightedNewsText(
                            text = contentText,
                            query = query,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = BentoSlate800,
                                lineHeight = 22.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = onShare,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, OceanBlue.copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = OceanBlue
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("share_full_news_button_${article.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଏହି ସମ୍ପୂର୍ଣ୍ଣ ଖବର ସେୟାର୍ କରନ୍ତୁ (Share)" else "Share This Full Story via ShareSheet",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = article.source,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = BentoSlate400,
                        fontWeight = FontWeight.Medium
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Full Article View Button
                    if (onArticleClick != null) {
                        Surface(
                            onClick = onArticleClick,
                            shape = RoundedCornerShape(8.dp),
                            color = OceanBlue.copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, OceanBlue.copy(alpha = 0.2f)),
                            modifier = Modifier.testTag("open_news_article_${article.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = "Read full article",
                                    tint = OceanBlue,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ପଢ଼ନ୍ତୁ" else "Read",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = OceanBlue
                                    )
                                )
                            }
                        }
                    }

                    // Dedicated Text-To-Speech Listen Button
                    Surface(
                        onClick = {
                            if (isThisArticleSpeaking) {
                                ttsHelper.stop()
                            } else {
                                val tTitle = if (language == AppLanguage.ODIA) article.odiaTitle else article.title
                                val tBody = if (language == AppLanguage.ODIA) {
                                    "${article.odiaSnippet}. ${article.odiaContent.ifBlank { article.odiaSnippet }}"
                                } else {
                                    "${article.snippet}. ${article.content.ifBlank { article.snippet }}"
                                }
                                ttsHelper.speakArticle(
                                    articleId = article.id,
                                    title = tTitle,
                                    content = tBody,
                                    language = language
                                )
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isThisArticleSpeaking) OceanBlue.copy(alpha = 0.2f) else OceanBlue.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, if (isThisArticleSpeaking) OceanBlue else OceanBlue.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 48.dp, minHeight = 44.dp)
                            .testTag("listen_news_button_${article.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (isThisArticleSpeaking) Icons.Default.GraphicEq else Icons.Default.Headphones,
                                contentDescription = if (isThisArticleSpeaking) "Stop Speech" else "Listen to News Audio in ${if (language == AppLanguage.ODIA) "Odia" else "English"}",
                                tint = OceanBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isThisArticleSpeaking) {
                                    if (language == AppLanguage.ODIA) "ବନ୍ଦ (Stop)" else "Stop"
                                } else {
                                    if (language == AppLanguage.ODIA) "ଶୁଣନ୍ତୁ" else "Listen"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OceanBlue
                                )
                            )
                        }
                    }

                    // Dedicated Share Button with Android ShareSheet invocation
                    Surface(
                        onClick = onShare,
                        shape = RoundedCornerShape(8.dp),
                        color = OceanBlue.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, OceanBlue.copy(alpha = 0.2f)),
                        modifier = Modifier.testTag("share_news_button_${article.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = OceanBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ସେୟାର୍" else "Share",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = OceanBlue
                                )
                            )
                        }
                    }

                    // Dedicated Bookmark Button with Room database persistence
                    Surface(
                        onClick = onToggleBookmark,
                        shape = RoundedCornerShape(8.dp),
                        color = if (isBookmarked) OceanBlue.copy(alpha = 0.14f) else BentoSlate100,
                        border = BorderStroke(1.dp, if (isBookmarked) OceanBlue.copy(alpha = 0.4f) else BentoSlate200),
                        modifier = Modifier
                            .defaultMinSize(minWidth = 48.dp, minHeight = 44.dp)
                            .testTag("bookmark_news_button_${article.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (isBookmarked) "Remove from offline bookmarks" else "Save to offline bookmarks",
                                tint = if (isBookmarked) OceanBlue else BentoSlate600,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBookmarked) {
                                    if (language == AppLanguage.ODIA) "ସାଇତା" else "Saved"
                                } else {
                                    if (language == AppLanguage.ODIA) "ବୁକ୍‌ମାର୍କ" else "Save"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBookmarked) OceanBlue else BentoSlate600,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RiverGaugeItemView(
    gauge: RiverGauge,
    language: AppLanguage
) {
    val progress = (gauge.currentLevelMeters / gauge.dangerLevelMeters).coerceIn(0.0, 1.0).toFloat()
    val statusColor = when (gauge.status.uppercase()) {
        "DANGER" -> Color(0xFFDC2626)
        "ALERT" -> Color(0xFFD97706)
        else -> Color(0xFF16A34A)
    }
    val statusBg = when (gauge.status.uppercase()) {
        "DANGER" -> Color(0xFFFEE2E2)
        "ALERT" -> Color(0xFFFEF3C7)
        else -> Color(0xFFDCFCE7)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(BorderStroke(1.dp, BentoSlate100), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) gauge.odiaRiverName else gauge.riverName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = "Station: ${gauge.stationName}",
                        style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = gauge.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = statusColor,
                trackColor = BentoSlate100
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current: ${gauge.currentLevelMeters}m",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate800
                    )
                )
                Text(
                    text = "Warning: ${gauge.warningLevelMeters}m",
                    style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                )
                Text(
                    text = "Danger: ${gauge.dangerLevelMeters}m",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFDC2626)
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Upstream Flow: ${gauge.trend}",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = OceanBlueDark,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
fun CycloneSheltersView(
    shelters: List<CycloneShelter>,
    language: AppLanguage
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEFF6FF))
            .clickable { expanded = !expanded }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Shelters",
                    tint = OceanBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାତ୍ୟା ଓ ବନ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ (Cyclone Shelters)" else "District Cyclone & Flood Shelters",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = OceanBlueDark
                    )
                )
            }

            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand",
                tint = OceanBlue,
                modifier = Modifier.size(18.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                shelters.forEach { shelter ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = shelter.name,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "${shelter.block} • Cap: ${shelter.capacityPeople} people",
                                style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate600)
                            )
                        }

                        IconButton(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${shelter.phone}"))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Call Shelter",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
