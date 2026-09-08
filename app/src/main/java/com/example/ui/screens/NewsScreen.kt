package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import com.example.ui.components.SearchEmptyStateCard
import com.example.ui.components.FriendlyEmptyStateCard
import com.example.ui.components.FriendlyEmptyStateType
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.NewsArticleEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.ui.components.CategoryChip
import com.example.ui.components.ReviewsSection
import kotlinx.coroutines.flow.Flow
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBluePill
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoRedBg
import com.example.ui.theme.BentoRedText
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.components.BalasoreSocialHubCard
import com.example.ui.components.SocialMediaQuickRow
import com.example.ui.components.openSocialMediaLink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    articles: List<NewsArticleEntity>,
    selectedCategory: String,
    searchQuery: String,
    selectedArticle: NewsArticleEntity?,
    currentUser: UserEntity? = null,
    onOpenAuth: () -> Unit = {},
    getReviewsForArticle: (String) -> Flow<List<ReviewEntity>> = { kotlinx.coroutines.flow.emptyFlow() },
    onSubmitArticleReview: (articleId: String, articleTitle: String, rating: Int, comment: String, guestName: String?) -> Unit = { _, _, _, _, _ -> },
    onCategorySelect: (String) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onArticleSelect: (NewsArticleEntity?) -> Unit,
    onToggleBookmark: (NewsArticleEntity) -> Unit,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    isOnline: Boolean = true,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        "All",
        "Local",
        "Tourism",
        "Weather",
        "Events",
        "Politics",
        "Read Later"
    )

    val breakingNews = articles.firstOrNull { it.isBreaking }
    val pullToRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = pullToRefreshState,
        indicator = {
            PullToRefreshDefaults.Indicator(
                state = pullToRefreshState,
                isRefreshing = isRefreshing,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = BentoCardWhite,
                color = BentoPrimaryBlue
            )
        },
        modifier = modifier
            .fillMaxSize()
            .testTag("news_pull_to_refresh")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            // Horizontal Scrolling Category Chip Bar at the TOP of the news feed
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("news_category_chips"),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory ||
                                (cat == "Read Later" && (selectedCategory == "Saved" || selectedCategory == "Read Later"))
                        CategoryChip(
                            name = cat,
                            isSelected = isSelected,
                            onClick = { onCategorySelect(cat) }
                        )
                    }
                }
            }

            // Active Search Filter Status Banner
            if (searchQuery.isNotBlank()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = BentoBlueLight,
                        border = BorderStroke(1.dp, BentoBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Filtering news for \"$searchQuery\"",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = BentoPrimaryBlue
                            )
                            Text(
                                text = "${articles.size} updates found",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = BentoSlate700
                            )
                        }
                    }
                }
            }

            // Breaking News Bento Tile
            if (breakingNews != null && searchQuery.isEmpty() && selectedCategory == "All") {
                item {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                        border = BorderStroke(1.dp, BentoRedBg),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .clickable { onArticleSelect(breakingNews) }
                            .testTag("breaking_news_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = BentoRedBg,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("📢", fontSize = 16.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "BREAKING ALERT",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp,
                                        fontSize = 10.sp
                                    ),
                                    color = BentoRedText
                                )
                                Text(
                                    text = breakingNews.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = BentoSlate900,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Section Heading with Pull-to-refresh prompt
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isNotEmpty()) "Search Results" else "$selectedCategory Updates",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Pull to refresh",
                            tint = BentoSlate400,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (isRefreshing) "Refreshing..." else "${articles.size} updates • Pull to refresh",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = BentoSlate400
                        )
                    }
                }
            }

            // Articles List in Bento Grid Cards
            if (articles.isEmpty()) {
                item {
                    if (!isOnline) {
                        FriendlyEmptyStateCard(
                            type = FriendlyEmptyStateType.OFFLINE_NO_INTERNET,
                            title = "You're Browsing Offline",
                            odiaTitle = "ଆପଣ ଅଫଲାଇନ ଅଛନ୍ତି (ଇଣ୍ଟରନେଟ ସଂଯୋଗ ନାହିଁ)",
                            subtitle = "Local news has not yet downloaded to your device. Connect to Wi-Fi or mobile data to fetch the latest Balasore updates.",
                            actionButtonText = "Retry Connection",
                            isActionLoading = isRefreshing,
                            onActionClick = onRefresh,
                            secondaryButtonText = if (searchQuery.isNotBlank()) "Clear Filter" else null,
                            onSecondaryClick = if (searchQuery.isNotBlank()) { { onSearchQueryChange("") } } else null,
                            tipsList = listOf(
                                "Once online, Balasore 360 automatically caches news for offline reading.",
                                "Tide and emergency phone numbers in the Essentials tab are always saved locally."
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    } else if (searchQuery.isNotBlank()) {
                        SearchEmptyStateCard(
                            searchQuery = searchQuery,
                            category = selectedCategory,
                            feedType = "News Updates",
                            onClearSearch = { onSearchQueryChange("") },
                            onResetCategory = { onCategorySelect("All") },
                            onSuggestionClick = { onSearchQueryChange(it) },
                            suggestions = listOf("Station", "Chandipur", "Cyclone", "Port", "Heritage")
                        )
                    } else {
                        FriendlyEmptyStateCard(
                            type = FriendlyEmptyStateType.NEWS_NOT_LOADED,
                            title = if (selectedCategory == "Read Later") "No Saved Bookmarks Yet" else "No News Updates Yet",
                            odiaTitle = if (selectedCategory == "Read Later") "କୌଣସି ସାଇତା ଖବର ନାହିଁ" else "ଏ ପର୍ଯ୍ୟନ୍ତ କୌଣସି ନୂତନ ଖବର ଆସିନାହିଁ",
                            subtitle = if (selectedCategory == "Read Later") {
                                "Tap the bookmark ribbon on any Balasore article to save it for quick offline reading here."
                            } else {
                                "There are no new bulletins in \"$selectedCategory\" right now. Pull down to refresh or check back shortly."
                            },
                            actionButtonText = "Check for Updates",
                            isActionLoading = isRefreshing,
                            onActionClick = onRefresh,
                            secondaryButtonText = if (selectedCategory != "All") "Show All News" else null,
                            onSecondaryClick = if (selectedCategory != "All") { { onCategorySelect("All") } } else null,
                            tipsList = listOf(
                                "Pull down on the screen anytime to trigger a live sync.",
                                "You can customize alert categories in Notification Settings."
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            } else {
                items(articles, key = { it.id }) { article ->
                    BentoNewsCard(
                        article = article,
                        onClick = { onArticleSelect(article) },
                        onToggleBookmark = { onToggleBookmark(article) }
                    )
                }

                // Official Balasore 360 Social Media Hub
                item {
                    BalasoreSocialHubCard(
                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp)
                    )
                }
            }
        }
    }

    // Article Detail Modal
    if (selectedArticle != null) {
        ArticleDetailSheet(
            article = selectedArticle,
            currentUser = currentUser,
            reviewsFlow = getReviewsForArticle(selectedArticle.id.toString()),
            onOpenAuth = onOpenAuth,
            onSubmitReview = { rating, comment, guestName ->
                onSubmitArticleReview(selectedArticle.id.toString(), selectedArticle.title, rating, comment, guestName)
            },
            onDismiss = { onArticleSelect(null) },
            onToggleBookmark = { onToggleBookmark(selectedArticle) }
        )
    }
}

/**
 * Bento News Card: Matches HTML "bg-white rounded-3xl p-5 border border-slate-100 shadow-sm"
 */
@Composable
fun BentoNewsCard(
    article: NewsArticleEntity,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("news_article_${article.id}")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Top Row: Category pill + timestamp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BentoBlueLight
                ) {
                    Text(
                        text = article.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = BentoPrimaryBlue,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = article.publishedAt,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = BentoSlate400
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Headline
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 22.sp
                ),
                color = BentoSlate900,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Summary
            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodyMedium,
                color = BentoSlate500,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom row: Source + 'Read Later' Bookmark Button (Room DB Offline Saving)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = "Source: ${article.source}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = BentoPrimaryBlue,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (article.isBookmarked) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BentoBlueLight
                        ) {
                            Text(
                                text = "OFFLINE SAVED",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = BentoPrimaryBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (article.isBookmarked) BentoBlueLight else BentoCardWhite,
                    border = BorderStroke(1.dp, if (article.isBookmarked) BentoPrimaryBlue else BentoBorder),
                    modifier = Modifier
                        .clickable(onClick = onToggleBookmark)
                        .testTag("read_later_button_${article.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (article.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (article.isBookmarked) "Saved to Read Later (Offline in Room DB)" else "Save to Read Later (Offline in Room DB)",
                            tint = if (article.isBookmarked) BentoPrimaryBlue else BentoSlate700,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = if (article.isBookmarked) "Saved Offline" else "Read Later",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = if (article.isBookmarked) BentoPrimaryBlue else BentoSlate700
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailSheet(
    article: NewsArticleEntity,
    currentUser: UserEntity? = null,
    reviewsFlow: Flow<List<ReviewEntity>> = kotlinx.coroutines.flow.emptyFlow(),
    onOpenAuth: () -> Unit = {},
    onSubmitReview: (rating: Int, comment: String, guestName: String?) -> Unit = { _, _, _ -> },
    onDismiss: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BentoCardWhite,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BentoBlueLight
                ) {
                    Text(
                        text = article.category.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = BentoPrimaryBlue,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate500)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = BentoSlate900
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.source,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = BentoPrimaryBlue
                )
                Text(
                    text = article.publishedAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = BentoSlate400
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Highlight Bento Box
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoBlueLight,
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = article.summary,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = BentoSlate700,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Full Article Body
            Text(
                text = article.content,
                style = MaterialTheme.typography.bodyLarge,
                lineHeight = 24.sp,
                color = BentoSlate900
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons (Share, Bookmark)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, article.title)
                            putExtra(Intent.EXTRA_TEXT, "${article.title}\n\n${article.summary}\n\nRead more on Balasore 360 by Nihar Sales")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share News Article"))
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share", tint = BentoSlate700, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share", color = BentoSlate700)
                }

                TextButton(
                    onClick = onToggleBookmark,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, if (article.isBookmarked) BentoPrimaryBlue else BentoBorder),
                    colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                        containerColor = if (article.isBookmarked) BentoBlueLight else Color.Transparent
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("article_detail_read_later_button")
                ) {
                    Icon(
                        imageVector = if (article.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Read Later Bookmark",
                        tint = if (article.isBookmarked) BentoPrimaryBlue else BentoSlate700,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (article.isBookmarked) "Saved (Offline)" else "Read Later",
                        color = if (article.isBookmarked) BentoPrimaryBlue else BentoSlate700,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Offline Room Database Status Banner
            if (article.isBookmarked) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BentoBlueLight,
                    border = BorderStroke(1.dp, BentoPrimaryBlue.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = BentoPrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Full article body saved to Room database for offline viewing anytime.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = BentoPrimaryBlue
                        )
                    }
                }
            }

            // Official Social Channels Bar
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoCardWhite,
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Connect with Balasore 360",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Text(
                            text = "@balasore360",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = BentoPrimaryBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    SocialMediaQuickRow(
                        onPlatformClick = { openSocialMediaLink(context, it.webUrl) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Community Feedback & Comments Section
            ReviewsSection(
                targetType = "NEWS",
                targetId = article.id.toString(),
                targetTitle = article.title,
                reviewsFlow = reviewsFlow,
                currentUser = currentUser,
                onOpenAuth = onOpenAuth,
                onSubmitReview = onSubmitReview
            )
        }
    }
}
