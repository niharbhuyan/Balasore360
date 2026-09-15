package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.NewsArticle
import com.example.ui.components.AdMobBannerCard
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

@Composable
fun NewsScreen(
    articles: List<NewsArticle>,
    selectedCategory: String,
    language: AppLanguage,
    bookmarkedIds: Set<String>,
    isRefreshing: Boolean,
    onCategorySelected: (String) -> Unit,
    onToggleBookmark: (String) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories = listOf("All", "Infrastructure", "Defense", "Culture", "Civic Alert")

    val filteredArticles = articles.filter { article ->
        selectedCategory == "All" || article.category == selectedCategory
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

        // Category Filter Chips
        item {
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
