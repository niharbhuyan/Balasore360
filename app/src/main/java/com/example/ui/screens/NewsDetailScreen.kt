package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.NewsArticleEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.ui.components.ReviewsSection
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoRedText
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import kotlinx.coroutines.flow.Flow
import kotlin.math.roundToInt

/**
 * Dedicated Detail View Screen for News Articles in Balasore.
 * Displays hero journalism imagery, headline, reading time metrics,
 * publication metadata, executive summary, complete article narrative,
 * AI search fact-checking grounding, and community discussion.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NewsDetailScreen(
    article: NewsArticleEntity,
    currentUser: UserEntity? = null,
    reviewsFlow: Flow<List<ReviewEntity>>,
    onOpenAuth: () -> Unit = {},
    onSubmitReview: (rating: Int, comment: String, guestName: String?) -> Unit,
    onNavigateBack: () -> Unit,
    onToggleBookmark: () -> Unit,
    onVerifyWithSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Handle Android system back gesture
    BackHandler(onBack = onNavigateBack)

    val imageRes = remember(article.category, article.id) {
        getNewsImageRes(article.category, article.id)
    }

    val readingTime = remember(article.title, article.summary, article.content) {
        val fullText = "${article.title} ${article.summary} ${article.content}".trim()
        val words = fullText.split("\\s+".toRegex()).count { it.isNotBlank() }
        val minutes = kotlin.math.max(1, (words / 140.0).roundToInt())
        "$minutes min read"
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("news_detail_screen_${article.id}"),
        containerColor = BentoCardWhite,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = article.category.uppercase(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        ),
                        color = BentoPrimaryBlue,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .testTag("news_detail_back_button")
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to News List",
                            tint = BentoSlate900
                        )
                    }
                },
                actions = {
                    // Bookmark / Read Later Action
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .testTag("news_detail_bookmark_button")
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (article.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (article.isBookmarked) "Remove from Read Later" else "Save to Read Later",
                            tint = if (article.isBookmarked) BentoPrimaryBlue else BentoSlate700
                        )
                    }

                    // Share Article Action
                    IconButton(
                        onClick = { shareNewsArticle(context, article) },
                        modifier = Modifier
                            .testTag("news_detail_share_button")
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share News Story",
                            tint = BentoSlate700
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BentoCardWhite,
                    titleContentColor = BentoSlate900
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(bottom = 96.dp)
        ) {
            // Visual Journalism Hero Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(BentoSlate900)
            ) {
                if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = article.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(BentoPrimaryBlue, Color(0xFF0F172A))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getNewsCategoryIcon(article.category),
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(90.dp)
                        )
                    }
                }

                // Scrim overlay for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.4f),
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Category & Breaking Badges over Hero Image
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (article.isBreaking) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoRedText
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "BREAKING",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 0.6.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BentoPrimaryBlue
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = getNewsCategoryIcon(article.category),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = article.category.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Article Header & Metadata Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Article Headline
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp
                    ),
                    color = BentoSlate900
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Metadata Bar: Source, Verified Badge, Reading Time, Timestamp
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = "Verified Source",
                            tint = BentoPrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = article.source,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = BentoPrimaryBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = BentoSlate400,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = readingTime,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = BentoSlate500,
                            fontSize = 11.5.sp
                        )
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = BentoSlate400,
                            fontSize = 11.sp
                        )
                        Text(
                            text = article.publishedAt,
                            style = MaterialTheme.typography.bodySmall,
                            color = BentoSlate500,
                            fontSize = 11.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Offline Availability & Verification Banner
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BentoSlate100.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Verified Local Report • Stored in Room database for offline reading",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                            color = BentoSlate700
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Key Summary Bento Highlight Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoBlueLight),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = BentoPrimaryBlue,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Article,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "EXECUTIVE SUMMARY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.6.sp
                                ),
                                color = BentoPrimaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = article.summary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                lineHeight = 22.sp
                            ),
                            color = BentoSlate900
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Full Article Body
                Text(
                    text = "Full Story",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = BentoSlate900
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = article.content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = BentoSlate900
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Bar: Share & Bookmark / Read Later
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { shareNewsArticle(context, article) },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, BentoBorder),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = BentoSlate700,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Share Story",
                            color = BentoSlate700,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Button(
                        onClick = onToggleBookmark,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (article.isBookmarked) BentoBlueLight else BentoPrimaryBlue,
                            contentColor = if (article.isBookmarked) BentoPrimaryBlue else Color.White
                        ),
                        border = if (article.isBookmarked) BorderStroke(1.dp, BentoPrimaryBlue) else null,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("news_detail_save_read_later_button")
                    ) {
                        Icon(
                            imageVector = if (article.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (article.isBookmarked) "Saved (Offline)" else "Read Later",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // AI Grounding / Fact-Checking Button
                Surface(
                    onClick = onVerifyWithSearch,
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF4285F4).copy(alpha = 0.08f),
                    border = BorderStroke(1.5.dp, Color(0xFF4285F4).copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("news_detail_ai_verification_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF1967D2),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Verify with Gemini AI Search Grounding",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF1967D2)
                            )
                            Text(
                                text = "Real-time verification against current Google Search results",
                                style = MaterialTheme.typography.labelSmall,
                                color = BentoSlate500,
                                fontSize = 10.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Article Metadata Card: ID, Category, Timestamp, Offline persistence
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalOffer,
                                contentDescription = null,
                                tint = BentoPrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Article Metadata & Indexing",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = BentoSlate900
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            DetailMetadataChip(label = "Category", value = article.category)
                            DetailMetadataChip(label = "Story ID", value = "#${article.id}")
                            DetailMetadataChip(label = "Coverage", value = "Balasore District")
                            DetailMetadataChip(label = "Status", value = "Archived in Room")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Community Discussion & Reactions Section
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
}

@Composable
private fun DetailMetadataChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = BentoBlueLight,
        border = BorderStroke(1.dp, BentoBorder)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = BentoSlate500
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = BentoSlate900
            )
        }
    }
}

private fun getNewsImageRes(category: String, articleId: Long): Int? {
    return when (category.lowercase()) {
        "tourism", "beach" -> R.drawable.img_chandipur_beach
        "heritage", "temple", "culture" -> R.drawable.img_temple_balasore
        else -> R.drawable.img_feature_graphic
    }
}

private fun getNewsCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "sports" -> Icons.Default.SportsCricket
        "weather" -> Icons.Default.Cloud
        "local", "local news" -> Icons.Default.LocationCity
        "politics" -> Icons.Default.AccountBalance
        "events" -> Icons.Default.Public
        else -> Icons.AutoMirrored.Filled.Article
    }
}

private fun shareNewsArticle(context: Context, article: NewsArticleEntity) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, article.title)
        putExtra(
            Intent.EXTRA_TEXT,
            "${article.title}\n\n" +
                    "${article.summary}\n\n" +
                    "Source: ${article.source} • ${article.publishedAt}\n\n" +
                    "Read full coverage on Balasore 360 App by Nihar Sales"
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share News Story"))
}
