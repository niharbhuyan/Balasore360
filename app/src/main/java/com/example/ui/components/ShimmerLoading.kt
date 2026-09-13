package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.LocalBentoPalette

/**
 * Creates an animated shimmer brush that transitions smoothly across skeleton shapes.
 * Automatically adapts between Light and Dark mode palettes for visual harmony.
 */
@Composable
fun shimmerBrush(
    showShimmer: Boolean = true,
    targetValue: Float = 1300f
): Brush {
    if (!showShimmer) {
        val staticColor = if (LocalBentoPalette.current.isDark) {
            Color(0xFF1E293B)
        } else {
            Color(0xFFE2E8F0)
        }
        return Brush.linearGradient(listOf(staticColor, staticColor))
    }

    val isDark = LocalBentoPalette.current.isDark
    val shimmerColors = if (isDark) {
        listOf(
            Color(0xFF1E293B),
            Color(0xFF334155),
            Color(0xFF475569),
            Color(0xFF334155),
            Color(0xFF1E293B)
        )
    } else {
        listOf(
            Color(0xFFE2E8F0),
            Color(0xFFEDF2F7),
            Color(0xFFFFFFFF),
            Color(0xFFEDF2F7),
            Color(0xFFE2E8F0)
        )
    }

    val transition = rememberInfiniteTransition(label = "shimmer_infinite_transition")
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate_anim"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnimation - 350f, translateAnimation - 350f),
        end = Offset(translateAnimation, translateAnimation)
    )
}

/**
 * Extension modifier that applies a clipping shape and animated shimmer background.
 */
@Composable
fun Modifier.shimmerPlaceholder(
    shape: Shape = RoundedCornerShape(8.dp),
    showShimmer: Boolean = true
): Modifier = this
    .clip(shape)
    .background(shimmerBrush(showShimmer = showShimmer))

/**
 * Basic rectangular or rounded placeholder box with shimmer animation.
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    width: Dp? = null,
    height: Dp? = null
) {
    val sizeModifier = when {
        width != null && height != null -> modifier.size(width, height)
        width != null -> modifier.width(width)
        height != null -> modifier.height(height)
        else -> modifier
    }
    Box(
        modifier = sizeModifier.shimmerPlaceholder(shape = shape)
    )
}

/**
 * High-craft active network sync banner displayed during network fetch or pull-to-refresh.
 * Gives immediate visual feedback with an animated gradient pulse.
 */
@Composable
fun ShimmerSyncBanner(
    message: String,
    odiaMessage: String? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = BentoBlueLight,
        border = BorderStroke(1.dp, BentoBorder),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("shimmer_sync_banner")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(shimmerBrush()),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CloudSync,
                    contentDescription = "Syncing",
                    tint = BentoPrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = BentoPrimaryBlue
                )
                if (odiaMessage != null) {
                    Text(
                        text = odiaMessage,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = BentoSlate500
                    )
                }
            }

            // Pulsing shimmer pill indicator
            Box(
                modifier = Modifier
                    .size(width = 44.dp, height = 10.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(shimmerBrush())
            )
        }
    }
}

// -----------------------------------------------------------------------------------------
// NEWS SKELETON COMPONENTS
// -----------------------------------------------------------------------------------------

/**
 * Skeleton placeholder matching the Breaking News Banner on the News feed.
 */
@Composable
fun NewsBreakingBannerSkeleton(
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
            .testTag("news_breaking_skeleton")
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon placeholder
            ShimmerBox(
                shape = RoundedCornerShape(10.dp),
                width = 36.dp,
                height = 36.dp
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                // "BREAKING ALERT" tag line skeleton
                ShimmerBox(
                    shape = RoundedCornerShape(4.dp),
                    width = 90.dp,
                    height = 10.dp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Headline lines
                ShimmerBox(
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth(0.92f),
                    height = 14.dp
                )
            }
        }
    }
}

/**
 * Skeleton card matching [BentoNewsCard] in layout, spacing, and dimensions.
 */
@Composable
fun NewsCardSkeleton(
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
            .testTag("news_card_skeleton")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Top Row: Category pill skeleton + reading time / timestamp skeletons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category pill skeleton
                ShimmerBox(
                    shape = RoundedCornerShape(20.dp),
                    width = 72.dp,
                    height = 20.dp
                )

                // Timestamp & read-time skeleton
                ShimmerBox(
                    shape = RoundedCornerShape(6.dp),
                    width = 96.dp,
                    height = 12.dp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Headline skeleton (2 lines)
            ShimmerBox(
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth(0.95f),
                height = 17.dp
            )
            Spacer(modifier = Modifier.height(6.dp))
            ShimmerBox(
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth(0.70f),
                height = 17.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Summary description skeleton (2 lines)
            ShimmerBox(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(0.98f),
                height = 13.dp
            )
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBox(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(0.82f),
                height = 13.dp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Bottom row: Source badge skeleton + Action button skeletons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerBox(
                    shape = RoundedCornerShape(8.dp),
                    width = 110.dp,
                    height = 14.dp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ShimmerBox(shape = CircleShape, width = 28.dp, height = 28.dp)
                    ShimmerBox(shape = CircleShape, width = 28.dp, height = 28.dp)
                }
            }
        }
    }
}

/**
 * Column of news article skeletons rendered while news articles are loading from network.
 */
@Composable
fun NewsListSkeleton(
    count: Int = 4,
    showBreakingBanner: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("news_skeleton_list")
    ) {
        if (showBreakingBanner) {
            NewsBreakingBannerSkeleton()
        }
        repeat(count) {
            NewsCardSkeleton()
        }
    }
}

// -----------------------------------------------------------------------------------------
// TOURISM HOTSPOTS SKELETON COMPONENTS
// -----------------------------------------------------------------------------------------

/**
 * Skeleton card matching [HotspotBentoGridCard] for the 2-column Grid View.
 */
@Composable
fun HotspotGridCardSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("hotspot_grid_card_skeleton")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Visual Image Header Skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(118.dp)
                    .shimmerPlaceholder(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            ) {
                // Category pill skeleton on top start
                ShimmerBox(
                    shape = RoundedCornerShape(12.dp),
                    width = 54.dp,
                    height = 18.dp,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )

                // Favorite button placeholder on top end
                ShimmerBox(
                    shape = CircleShape,
                    width = 30.dp,
                    height = 30.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )

                // Distance badge placeholder on bottom start
                ShimmerBox(
                    shape = RoundedCornerShape(8.dp),
                    width = 46.dp,
                    height = 18.dp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Name skeleton
                ShimmerBox(
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth(0.85f),
                    height = 14.dp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Odia script name skeleton
                ShimmerBox(
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth(0.60f),
                    height = 11.dp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Short description line
                ShimmerBox(
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth(0.95f),
                    height = 11.dp
                )
                Spacer(modifier = Modifier.height(3.dp))
                ShimmerBox(
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth(0.70f),
                    height = 11.dp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom chips row: Rating chip + timing chip
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ShimmerBox(
                        shape = RoundedCornerShape(10.dp),
                        width = 44.dp,
                        height = 18.dp
                    )
                    ShimmerBox(
                        shape = RoundedCornerShape(10.dp),
                        width = 48.dp,
                        height = 18.dp
                    )
                }
            }
        }
    }
}

/**
 * Skeleton card matching [HotspotBentoCard] for the 1-column List View.
 */
@Composable
fun HotspotListCardSkeleton(
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
            .testTag("hotspot_list_card_skeleton")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // Hotspot Name
                    ShimmerBox(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth(0.75f),
                        height = 18.dp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Odia Script Name
                    ShimmerBox(
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth(0.45f),
                        height = 12.dp
                    )
                }

                // Favorite icon skeleton
                ShimmerBox(
                    shape = CircleShape,
                    width = 32.dp,
                    height = 32.dp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description lines
            ShimmerBox(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(0.95f),
                height = 13.dp
            )
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBox(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(0.70f),
                height = 13.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom metadata badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ShimmerBox(
                    shape = RoundedCornerShape(12.dp),
                    width = 130.dp,
                    height = 24.dp
                )

                ShimmerBox(
                    shape = RoundedCornerShape(20.dp),
                    width = 65.dp,
                    height = 22.dp
                )
            }
        }
    }
}

/**
 * Skeleton placeholder for the Bento Top Weather and Coastal Status grid.
 */
@Composable
fun BentoTopGridSkeleton(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("bento_top_grid_skeleton")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Weather Card skeleton
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier
                    .weight(1.3f)
                    .height(130.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    ShimmerBox(shape = RoundedCornerShape(6.dp), width = 70.dp, height = 12.dp)
                    ShimmerBox(shape = RoundedCornerShape(8.dp), width = 90.dp, height = 28.dp)
                    ShimmerBox(shape = RoundedCornerShape(4.dp), width = 110.dp, height = 12.dp)
                }
            }

            // Tides & Coastal Conditions card skeleton
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier
                    .weight(1f)
                    .height(130.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    ShimmerBox(shape = RoundedCornerShape(6.dp), width = 60.dp, height = 12.dp)
                    ShimmerBox(shape = RoundedCornerShape(8.dp), width = 75.dp, height = 24.dp)
                    ShimmerBox(shape = RoundedCornerShape(4.dp), width = 65.dp, height = 12.dp)
                }
            }
        }
    }
}

/**
 * Skeleton placeholder for the Hero Bento destination card.
 */
@Composable
fun BentoHeroSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("bento_hero_skeleton")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerPlaceholder(shape = RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBox(shape = RoundedCornerShape(6.dp), width = 80.dp, height = 14.dp)
                ShimmerBox(shape = RoundedCornerShape(6.dp), width = 160.dp, height = 22.dp)
                ShimmerBox(shape = RoundedCornerShape(12.dp), width = 100.dp, height = 28.dp)
            }
        }
    }
}
