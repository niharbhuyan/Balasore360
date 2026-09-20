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
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories = listOf("All", "Politics", "Sports", "Emergency", "Development", "Infrastructure", "Defense", "Culture", "Civic Alert")

    // Filter articles by category and local search keywords across titles and body snippets
    val filteredArticles = articles.filter { article ->
        val matchesCategory = selectedCategory == "All" || article.category == selectedCategory
        val query = searchQuery.trim()
        val matchesSearch = query.isEmpty() ||
            article.title.contains(query, ignoreCase = true) ||
            article.odiaTitle.contains(query, ignoreCase = true) ||
            article.snippet.contains(query, ignoreCase = true) ||
            article.odiaSnippet.contains(query, ignoreCase = true) ||
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
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସଦ୍ୟତମ ସ୍ଥାନୀୟ ଖବର ଓ ସୂଚନା" else "Hyper-local updates & district press bulletins",
                        style = MaterialTheme.typography.bodyMedium.copy(color = BentoSlate500)
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(BentoSlate100)
                        .testTag("refresh_news_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = OceanBlue
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
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD))
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
                                        color = BentoSlate900,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "Central Water Commission & SRC Odisha",
                                    style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
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
                }
            }
        }

        // Category Filter Chips & Local Search Field
        item {
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
                            text = if (language == AppLanguage.ODIA) "ଖବର ଓ ଆର୍ଟିକିଲ୍ ଖୋଜନ୍ତୁ..." else "Search headlines, topics, or keywords...",
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
                        .padding(bottom = 10.dp)
                        .testTag("news_search_text_field")
                )
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    val isSelected = cat == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelected(cat) },
                        label = {
                            Text(
                                text = cat,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue,
                            selectedLabelColor = Color.White,
                            containerColor = BentoSlate100,
                            labelColor = BentoSlate700
                        ),
                        border = null,
                        shape = RoundedCornerShape(10.dp)
                    )
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
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, BentoSlate200)
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
                                color = BentoSlate900
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
fun NewsCard(
    article: NewsArticle,
    language: AppLanguage,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("news_card_${article.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BentoSlate100)
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
                    style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate400)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.ODIA) article.odiaTitle else article.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoSlate900,
                    lineHeight = 22.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (language == AppLanguage.ODIA) article.odiaSnippet else article.snippet,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = BentoSlate600,
                    lineHeight = 20.sp
                )
            )

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
