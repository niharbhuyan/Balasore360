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
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.TrendingUp
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
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material3.Switch
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories = listOf("All", "Local", "Coastal", "Spiritual", "Emergency", "Weather", "Culture", "Defense", "Sports", "Development", "Politics")

    val matchesCategoryForArticle: (NewsArticle, String) -> Boolean = { article, cat ->
        when (cat) {
            "All" -> true
            "Local" -> article.category.equals("Local", ignoreCase = true) ||
                       article.category.equals("Infrastructure", ignoreCase = true) ||
                       article.category.contains("Civic", ignoreCase = true)
            "Coastal" -> article.category.equals("Coastal", ignoreCase = true) ||
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

    // Filter articles by category and local search keywords across titles, body snippets, and full narrative content
    val filteredArticles = articles.filter { article ->
        val matchesCategory = matchesCategoryForArticle(article, selectedCategory)
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
                            imageVector = Icons.Default.TrendingUp,
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
                                imageVector = Icons.Default.ManageSearch,
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
                        "Local" -> "📍"
                        "Coastal" -> "🌊"
                        "Spiritual" -> "🛕"
                        "Sports" -> "🏏"
                        "Emergency" -> "🚨"
                        "Weather" -> "🌦️"
                        "Development" -> "🏗️"
                        "Culture" -> "🏛️"
                        "Defense" -> "🚀"
                        "Politics" -> "🗳️"
                        else -> "📰"
                    }
                    val displayLabel = if (language == AppLanguage.ODIA) {
                        when (cat) {
                            "All" -> "ସମସ୍ତ"
                            "Local" -> "ସ୍ଥାନୀୟ"
                            "Coastal" -> "ଉପକୂଳ"
                            "Spiritual" -> "ଆଧ୍ୟାତ୍ମିକ"
                            "Sports" -> "କ୍ରୀଡ଼ା"
                            "Emergency" -> "ଜରୁରୀକାଳୀନ"
                            "Weather" -> "ପାଣିପାଗ"
                            "Development" -> "ବିକାଶ"
                            "Culture" -> "ସଂସ୍କୃତି"
                            "Defense" -> "ପ୍ରତିରକ୍ଷା"
                            "Politics" -> "ରାଜନୀତି"
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

        // Empty state when search keywords produce no matches
        if (filteredArticles.isEmpty()) {
            item {
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

        // Articles List
        items(filteredArticles, key = { it.id }) { article ->
            NewsCard(
                article = article,
                language = language,
                searchQuery = searchQuery,
                isBookmarked = bookmarkedIds.contains(article.id),
                onToggleBookmark = { onToggleBookmark(article.id) },
                onShare = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "${article.title}\n\nRead more on Balasore 360 app."
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            )
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
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

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
                            imageVector = Icons.Default.MenuBook,
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

                Row {
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = BentoSlate400,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) OceanBlue else BentoSlate400,
                            modifier = Modifier.size(18.dp)
                        )
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
