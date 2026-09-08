package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900

data class SocialMediaPlatform(
    val id: String,
    val name: String,
    val handle: String,
    val webUrl: String,
    val iconRes: Int,
    val brandColor: Color,
    val badgeBgColor: Color,
    val description: String
)

val BALASORE_SOCIAL_PLATFORMS = listOf(
    SocialMediaPlatform(
        id = "social_facebook",
        name = "Facebook",
        handle = "@balasore360",
        webUrl = "https://www.facebook.com/balasore360",
        iconRes = R.drawable.ic_social_facebook,
        brandColor = Color(0xFF1877F2),
        badgeBgColor = Color(0xFFE7F0FD),
        description = "District updates, community programs, festivals & announcements"
    ),
    SocialMediaPlatform(
        id = "social_instagram",
        name = "Instagram",
        handle = "@balasore360",
        webUrl = "https://www.instagram.com/balasore360",
        iconRes = R.drawable.ic_social_instagram,
        brandColor = Color(0xFFE4405F),
        badgeBgColor = Color(0xFFFDEEF2),
        description = "Chandipur receding tides, temple architecture & scenic reels"
    ),
    SocialMediaPlatform(
        id = "social_threads",
        name = "Threads",
        handle = "@balasore360",
        webUrl = "https://www.threads.com/balasore360",
        iconRes = R.drawable.ic_social_threads,
        brandColor = Color(0xFF111827),
        badgeBgColor = Color(0xFFF3F4F6),
        description = "Daily district conversations, community thoughts & highlights"
    ),
    SocialMediaPlatform(
        id = "social_x",
        name = "X (Twitter)",
        handle = "@balasore360",
        webUrl = "https://www.x.com/balasore360",
        iconRes = R.drawable.ic_social_x,
        brandColor = Color(0xFF0F1419),
        badgeBgColor = Color(0xFFF1F3F5),
        description = "Breaking news, cyclone advisories, rail & civic notices"
    ),
    SocialMediaPlatform(
        id = "social_youtube",
        name = "YouTube",
        handle = "@balasore360",
        webUrl = "https://www.youtube.com/@balasore360",
        iconRes = R.drawable.ic_social_youtube,
        brandColor = Color(0xFFFF0000),
        badgeBgColor = Color(0xFFFFEEEE),
        description = "Full heritage documentaries, drone flyovers & video news"
    )
)

fun openSocialMediaLink(context: Context, url: String) {
    try {
        val targetUri = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            Uri.parse("https://$url")
        } else {
            Uri.parse(url)
        }
        val intent = Intent(Intent.ACTION_VIEW, targetUri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Unable to open link: $url", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Full Bento-Grid Social Media Hub Card showing all official platforms
 * with direct launch links, copy handle affordance, and descriptive context.
 */
@Composable
fun BalasoreSocialHubCard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("social_media_hub_card")
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(BentoBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Social Media",
                            tint = BentoPrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Official Social Channels",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Text(
                            text = "Follow Balasore 360 across all platforms",
                            style = MaterialTheme.typography.bodySmall,
                            color = BentoSlate500
                        )
                    }
                }

                // Global Handle Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BentoBlueLight,
                    border = BorderStroke(1.dp, BentoBorder)
                ) {
                    Text(
                        text = "@balasore360",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = BentoPrimaryBlue,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Icon Row
            SocialMediaQuickRow(
                onPlatformClick = { platform ->
                    openSocialMediaLink(context, platform.webUrl)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = BentoBorder, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // Detailed platform item list
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                BALASORE_SOCIAL_PLATFORMS.forEach { platform ->
                    SocialPlatformRowItem(
                        platform = platform,
                        onOpen = { openSocialMediaLink(context, platform.webUrl) },
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(platform.webUrl))
                            Toast.makeText(context, "${platform.name} link copied!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

/**
 * Compact horizontal row of circular social media brand buttons.
 */
@Composable
fun SocialMediaQuickRow(
    onPlatformClick: (SocialMediaPlatform) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .testTag("social_media_quick_row"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BALASORE_SOCIAL_PLATFORMS.forEach { platform ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onPlatformClick(platform) }
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .testTag("quick_${platform.id}")
            ) {
                Surface(
                    shape = CircleShape,
                    color = platform.badgeBgColor,
                    border = BorderStroke(1.dp, platform.brandColor.copy(alpha = 0.25f)),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = platform.iconRes),
                            contentDescription = platform.name,
                            tint = platform.brandColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = platform.name.split(" ").first(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = BentoSlate700,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Individual row item for each platform with launch action and copy link.
 */
@Composable
fun SocialPlatformRowItem(
    platform: SocialMediaPlatform,
    onOpen: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = BentoSlate100.copy(alpha = 0.5f),
        border = BorderStroke(0.5.dp, BentoBorder),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onOpen)
            .testTag("row_${platform.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = platform.badgeBgColor,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = platform.iconRes),
                            contentDescription = platform.name,
                            tint = platform.brandColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = platform.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = platform.handle,
                            style = MaterialTheme.typography.labelSmall,
                            color = platform.brandColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = platform.description,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = BentoSlate500,
                        maxLines = 1
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onCopy,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Link",
                        tint = BentoSlate400,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onOpen,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = "Open ${platform.name}",
                        tint = platform.brandColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
