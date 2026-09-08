package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.local.UserEntity
import com.example.data.local.WeatherCacheEntity
import com.example.ui.theme.BentoAmberBg
import com.example.ui.theme.BentoAmberText
import com.example.ui.theme.BentoBlueLight
import com.example.ui.theme.BentoBluePill
import com.example.ui.theme.BentoBlueText
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCanvas
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoGold
import com.example.ui.theme.BentoGreenBg
import com.example.ui.theme.BentoGreenText
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoRedBg
import com.example.ui.theme.BentoRedText
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.LocalBentoPalette
import com.example.ui.viewmodel.AppTab
import com.example.ui.theme.TideIncoming
import com.example.ui.theme.TideReceding

@Composable
fun AppHeader(
    weather: WeatherCacheEntity?,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    currentUser: UserEntity? = null,
    onProfileClick: () -> Unit = {},
    isOnline: Boolean = true,
    isSyncing: Boolean = false,
    lastSyncTime: Long = 0L,
    onOfflineStatusClick: () -> Unit = {},
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    selectedTab: AppTab = AppTab.HOTSPOTS,
    hotspotsCount: Int = 0,
    newsCount: Int = 0,
    weatherMatchesCount: Int = 0,
    onSelectTab: (AppTab) -> Unit = {},
    onToggleTheme: () -> Unit = {},
    onAlertsClick: () -> Unit = {},
    hasActiveAlerts: Boolean = false,
    modifier: Modifier = Modifier
) {
    val rotation by rememberInfiniteTransition(label = "refresh").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing)
        ),
        label = "rotate"
    )

    Surface(
        color = BentoCanvas,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Balasore 360 App Logo Emblem
                    Surface(
                        shape = CircleShape,
                        color = BentoCardWhite,
                        shadowElevation = 2.dp,
                        border = BorderStroke(1.5.dp, BentoPrimaryBlue.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .size(52.dp)
                            .testTag("app_logo_header")
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_icon_circle),
                            contentDescription = "Balasore 360 Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column {
                        Text(
                            text = "NIHAR SALES",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                fontSize = 10.sp
                            ),
                            color = BentoPrimaryBlue
                        )
                        Text(
                            text = "BALASORE 360",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            ),
                            color = BentoSlate900
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "ବାଲେଶ୍ୱର • Nihar Sales",
                                style = MaterialTheme.typography.bodySmall,
                                color = BentoSlate500
                            )

                            // Room Sync / Offline Pill Button
                            Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (!isOnline) BentoAmberBg else if (isSyncing) BentoBlueLight else BentoCardWhite,
                            border = BorderStroke(1.dp, if (!isOnline) BentoAmberText.copy(alpha = 0.3f) else BentoBorder),
                            shadowElevation = 0.5.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(onClick = onOfflineStatusClick)
                                .testTag("sync_status_badge")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(
                                            color = if (!isOnline) BentoAmberText else if (isSyncing) BentoPrimaryBlue else BentoGreenText,
                                            shape = CircleShape
                                        )
                                )
                                Text(
                                    text = if (!isOnline) "Offline (Room)" else if (isSyncing) "Syncing..." else "Room Cached",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.5.sp
                                    ),
                                    color = if (!isOnline) BentoAmberText else if (isSyncing) BentoPrimaryBlue else BentoSlate700
                                )
                            }
                        }
                    }
                }
            }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (weather != null) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = BentoCardWhite,
                            border = BorderStroke(1.dp, BentoBorder),
                            shadowElevation = 1.dp
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WbSunny,
                                    contentDescription = "Weather",
                                    tint = BentoPrimaryBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${weather.temperature.toInt()}°C",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoSlate900
                                )
                            }
                        }
                    }

                    // Refresh Button (Triggers Sync Worker & Cache Refresh)
                    Surface(
                        shape = CircleShape,
                        color = BentoBluePill,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onRefresh)
                            .testTag("refresh_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Data",
                                tint = BentoBlueText,
                                modifier = Modifier
                                    .size(20.dp)
                                    .rotate(if (isRefreshing || isSyncing) rotation else 0f)
                            )
                        }
                    }

                    // Alerts & Local Warnings Bell Button
                    Surface(
                        shape = CircleShape,
                        color = if (hasActiveAlerts) BentoRedBg else BentoCardWhite,
                        border = BorderStroke(1.dp, if (hasActiveAlerts) BentoRedText.copy(alpha = 0.4f) else BentoBorder),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onAlertsClick)
                            .testTag("alerts_bell_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (hasActiveAlerts) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = "Local Alerts & Push Notifications",
                                tint = if (hasActiveAlerts) BentoRedText else BentoSlate700,
                                modifier = Modifier.size(20.dp)
                            )
                            if (hasActiveAlerts) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .align(Alignment.TopEnd)
                                        .padding(top = 2.dp, end = 2.dp)
                                        .background(BentoRedText, CircleShape)
                                )
                            }
                        }
                    }

                    // Theme Toggle Button (Light / Dark Mode Switcher)
                    val isAppDark = LocalBentoPalette.current.isDark
                    Surface(
                        shape = CircleShape,
                        color = if (isAppDark) BentoBluePill else BentoCardWhite,
                        border = BorderStroke(1.dp, BentoBorder),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onToggleTheme)
                            .testTag("theme_toggle_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isAppDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (isAppDark) "Switch to Light Mode" else "Switch to Dark Mode",
                                tint = if (isAppDark) BentoGold else BentoSlate700,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // User Profile Button
                    Surface(
                        shape = CircleShape,
                        color = BentoCardWhite,
                        border = BorderStroke(1.5.dp, if (currentUser != null) BentoPrimaryBlue else BentoBorder),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onProfileClick)
                            .testTag("user_profile_header_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (currentUser?.avatarUri != null) {
                                AsyncImage(
                                    model = currentUser.avatarUri,
                                    contentDescription = "Profile",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.size(42.dp)
                                )
                            } else if (currentUser != null) {
                                Text(
                                    text = currentUser.fullName.take(2).uppercase(),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoPrimaryBlue
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Account Login",
                                    tint = BentoSlate500,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Offline Mode Banner Indicator
            if (!isOnline) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BentoAmberBg,
                    border = BorderStroke(1.dp, BentoAmberText.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onOfflineStatusClick)
                        .testTag("offline_notice_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = BentoAmberText,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "Offline Mode • All news, weather tides & tourism hotspots running from Room database",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.Medium),
                            color = BentoAmberText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Main Screen Search Bar
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text(
                        text = when (selectedTab) {
                            AppTab.NEWS -> "Filter Balasore news by keyword..."
                            AppTab.WEATHER -> "Filter weather, tides & forecasts..."
                            AppTab.HOTSPOTS -> "Filter tourism spots & heritage..."
                            AppTab.ESSENTIALS -> "Filter emergency contacts & services..."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = BentoSlate400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (searchQuery.isNotEmpty()) BentoPrimaryBlue else BentoSlate400,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { onSearchQueryChange("") },
                            modifier = Modifier.testTag("main_search_clear_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = BentoSlate400,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BentoCardWhite,
                    unfocusedContainerColor = BentoCardWhite,
                    focusedIndicatorColor = BentoPrimaryBlue,
                    unfocusedIndicatorColor = BentoBorder,
                    focusedTextColor = BentoSlate900,
                    unfocusedTextColor = BentoSlate900
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("main_search_bar_input")
            )

            // Quick Filter Keyword Suggestions Row (Instant 1-tap filtering for current feed)
            if (searchQuery.isBlank()) {
                val suggestions = when (selectedTab) {
                    AppTab.NEWS -> listOf("Cyclone", "Chandipur", "Station", "Port", "Remuna", "Hospital")
                    AppTab.WEATHER -> listOf("Rain", "Cyclone", "Tide", "Chandipur", "Sunny", "Wind")
                    AppTab.HOTSPOTS -> listOf("Beach", "Temple", "Remuna", "Hills", "Kuldiha")
                    AppTab.ESSENTIALS -> listOf("Police", "Hospital", "Ambulance", "Fire", "Disaster")
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Quick filter:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.5.sp
                        ),
                        color = BentoSlate500,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                    suggestions.forEach { keyword ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoCardWhite,
                            border = BorderStroke(1.dp, BentoBorder),
                            shadowElevation = 0.5.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSearchQueryChange(keyword) }
                                .testTag("header_quick_filter_${keyword.lowercase()}")
                        ) {
                            Text(
                                text = keyword,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                color = BentoSlate700,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Results count badges when searching
            if (searchQuery.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Matches:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate500
                    )

                    // News result count pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedTab == AppTab.NEWS) BentoPrimaryBlue else BentoCardWhite,
                        border = BorderStroke(1.dp, if (selectedTab == AppTab.NEWS) BentoPrimaryBlue else BentoBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectTab(AppTab.NEWS) }
                            .testTag("search_filter_news_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Article,
                                contentDescription = null,
                                tint = if (selectedTab == AppTab.NEWS) Color.White else BentoPrimaryBlue,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "News ($newsCount)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (selectedTab == AppTab.NEWS) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                color = if (selectedTab == AppTab.NEWS) Color.White else BentoSlate700
                            )
                        }
                    }

                    // Weather result count pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedTab == AppTab.WEATHER) BentoPrimaryBlue else BentoCardWhite,
                        border = BorderStroke(1.dp, if (selectedTab == AppTab.WEATHER) BentoPrimaryBlue else BentoBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectTab(AppTab.WEATHER) }
                            .testTag("search_filter_weather_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cloud,
                                contentDescription = null,
                                tint = if (selectedTab == AppTab.WEATHER) Color.White else BentoPrimaryBlue,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Weather ($weatherMatchesCount)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (selectedTab == AppTab.WEATHER) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                color = if (selectedTab == AppTab.WEATHER) Color.White else BentoSlate700
                            )
                        }
                    }

                    // Hotspots result count pill
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedTab == AppTab.HOTSPOTS) BentoPrimaryBlue else BentoCardWhite,
                        border = BorderStroke(1.dp, if (selectedTab == AppTab.HOTSPOTS) BentoPrimaryBlue else BentoBorder),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectTab(AppTab.HOTSPOTS) }
                            .testTag("search_filter_hotspots_pill")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                tint = if (selectedTab == AppTab.HOTSPOTS) Color.White else BentoPrimaryBlue,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = "Tourism ($hotspotsCount)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (selectedTab == AppTab.HOTSPOTS) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                ),
                                color = if (selectedTab == AppTab.HOTSPOTS) Color.White else BentoSlate700
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TideStatusBadge(
    tideState: String,
    tideDesc: String,
    modifier: Modifier = Modifier
) {
    val (badgeBg, textColor, label) = when (tideState) {
        "LOW_TIDE" -> Triple(BentoBluePill, BentoPrimaryBlue, "Chandipur: Vanishing Sea (Low Tide)")
        "RECEDING" -> Triple(BentoBluePill, BentoPrimaryBlue, "Chandipur: Sea Receding Outwards")
        "INCOMING" -> Triple(BentoRedBg, BentoRedText, "Chandipur: Incoming Tide (Return to Shore)")
        else -> Triple(BentoBlueLight, BentoPrimaryBlue, "Chandipur: High Tide Waves")
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = badgeBg,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CategoryChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) BentoPrimaryBlue else BentoCardWhite,
        contentColor = if (isSelected) Color.White else BentoSlate700,
        border = if (isSelected) null else BorderStroke(1.dp, BentoBorder),
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier
            .testTag("category_chip_${name.lowercase().replace(" ", "_").replace("&", "and")}")
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

/**
 * Horizontal scrollable tab row on the main screen to seamlessly toggle
 * between 'News', 'Weather', and 'Tourism' feeds.
 */
@Composable
fun MainScrollableTabRow(
    selectedTab: AppTab,
    onSelectTab: (AppTab) -> Unit,
    hotspotsCount: Int,
    newsCount: Int,
    weatherTemp: String? = null,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        Triple(AppTab.HOTSPOTS, "Tourism", Icons.Default.Explore),
        Triple(AppTab.NEWS, "News", Icons.AutoMirrored.Filled.Article),
        Triple(AppTab.WEATHER, "Weather", Icons.Default.Cloud),
        Triple(AppTab.ESSENTIALS, "Essentials", Icons.Default.ContactPhone)
    )

    val selectedIndex = tabs.indexOfFirst { it.first == selectedTab }.coerceAtLeast(0)

    Surface(
        color = BentoCanvas,
        modifier = modifier.fillMaxWidth()
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = BentoPrimaryBlue,
            divider = {},
            indicator = { tabPositions ->
                if (selectedIndex < tabPositions.size) {
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedIndex])
                            .height(3.dp)
                            .padding(horizontal = 14.dp)
                            .background(
                                color = BentoPrimaryBlue,
                                shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                            )
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("horizontal_scrollable_tab_row")
        ) {
            tabs.forEach { (tab, title, icon) ->
                val isSelected = selectedTab == tab
                val badgeText = when (tab) {
                    AppTab.HOTSPOTS -> if (hotspotsCount > 0) "$hotspotsCount" else null
                    AppTab.NEWS -> if (newsCount > 0) "$newsCount" else null
                    AppTab.WEATHER -> weatherTemp
                    AppTab.ESSENTIALS -> "24x7"
                }

                Tab(
                    selected = isSelected,
                    onClick = { onSelectTab(tab) },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("feed_tab_${tab.name.lowercase()}"),
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) BentoPrimaryBlue else BentoSlate400,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) BentoPrimaryBlue else BentoSlate500
                            )
                            if (badgeText != null) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) BentoPrimaryBlue else BentoBlueLight
                                ) {
                                    Text(
                                        text = badgeText,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        color = if (isSelected) Color.White else BentoBlueText,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

/**
 * Informative Empty State Card with Custom Vector Illustration
 * Displayed when no news updates or tourism hotspots match the active search filter.
 */
@Composable
fun SearchEmptyStateCard(
    searchQuery: String,
    category: String,
    feedType: String,
    onClearSearch: () -> Unit,
    onResetCategory: () -> Unit,
    onSuggestionClick: (String) -> Unit,
    suggestions: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("search_empty_state_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Illustrated Hero Container with Vector Asset
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = BentoBlueLight,
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier.size(170.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.img_empty_search),
                        contentDescription = "No results explorer illustration",
                        modifier = Modifier
                            .size(155.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Informative Headline
            Text(
                text = if (searchQuery.isNotBlank()) "No Matches Found" else "No $feedType Found",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.3).sp
                ),
                color = BentoSlate900,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Informative Context & Description
            Text(
                text = if (searchQuery.isNotBlank()) {
                    "We searched across Balasore for \"$searchQuery\" in \"$category\", but couldn't find matching records."
                } else {
                    "There are currently no active $feedType listed under the \"$category\" category."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = BentoSlate500,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            if (suggestions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "💡 Try these Balasore searches:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = BentoPrimaryBlue
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Suggestion chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    suggestions.take(3).forEach { suggestion ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BentoBlueLight,
                            border = BorderStroke(1.dp, BentoBorder),
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onSuggestionClick(suggestion) }
                                .testTag("empty_state_chip_$suggestion")
                        ) {
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = BentoPrimaryBlue,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (searchQuery.isNotBlank()) {
                    Button(
                        onClick = onClearSearch,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BentoPrimaryBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("empty_state_clear_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Clear Filter",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                if (category != "All") {
                    OutlinedButton(
                        onClick = onResetCategory,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BentoBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BentoSlate700
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("empty_state_reset_category_button")
                    ) {
                        Text(
                            text = "Show All",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}

