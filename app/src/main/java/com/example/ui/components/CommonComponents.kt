package com.example.ui.components

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.ui.theme.*
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
import com.example.ui.util.AppLanguage
import com.example.ui.util.AppStrings
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
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
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
                            text = AppStrings.brandName(selectedLanguage).uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                fontSize = 10.sp
                            ),
                            color = BentoPrimaryBlue
                        )
                        Text(
                            text = AppStrings.appName(selectedLanguage).uppercase(),
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
                            val subBrand = when (selectedLanguage) {
                                AppLanguage.ODIA -> "ବାଲେଶ୍ୱର • ${AppStrings.brandName(selectedLanguage)}"
                                AppLanguage.HINDI -> "बालेश्वर • ${AppStrings.brandName(selectedLanguage)}"
                                AppLanguage.ENGLISH -> "Balasore • Nihar Sales"
                            }
                            Text(
                                text = subBrand,
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

            // Offline Mode Banner Indicator with Retry Action
            if (!isOnline) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BentoAmberBg,
                    border = BorderStroke(1.dp, BentoAmberText.copy(alpha = 0.35f)),
                    shadowElevation = 0.5.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("offline_notice_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .clickable(onClick = onOfflineStatusClick)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = "Offline indicator",
                                tint = BentoAmberText,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = AppStrings.offlineTitle(selectedLanguage),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BentoAmberText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = AppStrings.offlineSubtitle(selectedLanguage),
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp),
                                    color = BentoAmberText.copy(alpha = 0.85f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Retry Button
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BentoAmberText,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(enabled = !isRefreshing, onClick = onRefresh)
                                .testTag("header_retry_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                if (isRefreshing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(12.dp),
                                        color = Color.White,
                                        strokeWidth = 1.5.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Retry connection",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                Text(
                                    text = if (isRefreshing) "Retrying..." else "Retry",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
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
                            AppTab.NEWS -> when (selectedLanguage) {
                                AppLanguage.ODIA -> "ବାଲେଶ୍ୱର ଖବର ଖୋଜନ୍ତୁ..."
                                AppLanguage.HINDI -> "बालेश्वर समाचार खोजें..."
                                AppLanguage.ENGLISH -> "Filter Balasore news by keyword..."
                            }
                            AppTab.WEATHER -> when (selectedLanguage) {
                                AppLanguage.ODIA -> "ପାଣିପାଗ, ଜୁଆର ଓ ପୂର୍ବାନୁମାନ..."
                                AppLanguage.HINDI -> "मौसम, ज्वार और पूर्वानुमान..."
                                AppLanguage.ENGLISH -> "Filter weather, tides & forecasts..."
                            }
                            AppTab.HOTSPOTS -> when (selectedLanguage) {
                                AppLanguage.ODIA -> "ପର୍ଯ୍ୟଟନ ସ୍ଥଳ ଓ ଐତିହ୍ୟ ଖୋଜନ୍ତୁ..."
                                AppLanguage.HINDI -> "पर्यटन स्थल एवं धरोहर खोजें..."
                                AppLanguage.ENGLISH -> "Filter tourism spots & heritage..."
                            }
                            AppTab.ESSENTIALS -> when (selectedLanguage) {
                                AppLanguage.ODIA -> "ଜରୁରୀକାଳୀନ ସେବା ଓ ନମ୍ବର ଖୋଜନ୍ତୁ..."
                                AppLanguage.HINDI -> "आपातकालीन सेवाएं और संपर्क..."
                                AppLanguage.ENGLISH -> "Filter emergency contacts & services..."
                            }
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

/**
 * Modern Animated Live Marine Status Ticker.
 * Informs the user in real-time about Chandipur vanishing sea tides,
 * walking safety window, and ITR missile range advisory status with live pulsing beacon.
 */
@Composable
fun LiveMarineStatusTicker(
    tidePhase: String,
    safeWalkMins: Int,
    recededKm: Double,
    hasDrdoAlert: Boolean = false,
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val isSafeToWalk = safeWalkMins > 0
    val statusColor = if (isSafeToWalk) Color(0xFF0284C7) else Color(0xFFEF4444)
    val statusBg = if (isSafeToWalk) Color(0xFFF0F9FF) else Color(0xFFFEF2F2)
    val statusBorder = if (isSafeToWalk) Color(0xFFBAE6FD) else Color(0xFFFECACA)

    val tickerText = when (selectedLanguage) {
        AppLanguage.ODIA -> "ଚାନ୍ଦିପୁର: ସମୁଦ୍ର ${recededKm} କିମି ପଛକୁ ଗଲାଣି • ଚାଲିବା ସମୟ ${safeWalkMins} ମିନିଟ୍"
        AppLanguage.HINDI -> "चांदीपुर: समुद्र ${recededKm} किमी पीछे हटा • चलने का समय ${safeWalkMins} मिनट शेष"
        AppLanguage.ENGLISH -> "Chandipur: Sea receded ${recededKm} km • Safe walk: ${safeWalkMins} mins left"
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = statusBg,
        border = BorderStroke(1.dp, statusBorder),
        shadowElevation = 0.5.dp,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("live_marine_ticker")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor.copy(alpha = pulseAlpha))
                )

                Text(
                    text = tickerText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp
                    ),
                    color = if (isSafeToWalk) BentoSlate900 else BentoRedText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isSafeToWalk) BentoPrimaryBlue else BentoRedText,
                modifier = Modifier.padding(start = 6.dp)
            ) {
                Text(
                    text = if (hasDrdoAlert) "ITR ALERT" else "LIVE TIDE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 9.sp,
                        letterSpacing = 0.8.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

/**
 * Coastal Marine Bento Quick-Action Dock.
 * 4 high-priority bento micro-cards giving instantaneous 1-tap utility:
 * 1. Chandipur Vanishing Sea Radar
 * 2. 24x7 Emergency SOS Speed-Dial
 * 3. Balasore Express & Transit Radar
 * 4. Civic & Coastal Warning Bulletins
 */
@Composable
fun BentoQuickActionDock(
    recededKm: Double,
    isSafeToWalk: Boolean,
    safeWalkMins: Int,
    activeAlertsCount: Int,
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    onTideClick: () -> Unit,
    onEmergencySosClick: () -> Unit,
    onTransitClick: () -> Unit,
    onAlertsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Card 1: Chandipur Tide Radar
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoCardWhite,
                border = BorderStroke(1.dp, BentoBorder),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onTideClick)
                    .testTag("dock_tide_radar")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFE0F2FE),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Water,
                                    contentDescription = null,
                                    tint = BentoPrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSafeToWalk) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                        ) {
                            Text(
                                text = "-${recededKm} KM",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                ),
                                color = if (isSafeToWalk) Color(0xFF15803D) else Color(0xFFB91C1C),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (selectedLanguage) {
                            AppLanguage.ODIA -> "ଚାନ୍ଦିପୁର ଜୁଆର"
                            AppLanguage.HINDI -> "चांदीपुर ज्वार"
                            AppLanguage.ENGLISH -> "Chandipur Tide"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                    Text(
                        text = if (isSafeToWalk) "${safeWalkMins}m safe window" else "Sea returning",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = BentoSlate500
                    )
                }
            }

            // Card 2: 24x7 Emergency SOS Dialer
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoCardWhite,
                border = BorderStroke(1.dp, BentoBorder),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onEmergencySosClick)
                    .testTag("dock_emergency_sos")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFEE2E2),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = BentoRedText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = BentoRedBg
                        ) {
                            Text(
                                text = "112 SOS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                ),
                                color = BentoRedText,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (selectedLanguage) {
                            AppLanguage.ODIA -> "ଜରୁରୀକାଳୀନ ଡାଏଲ୍"
                            AppLanguage.HINDI -> "आपातकालीन डायल"
                            AppLanguage.ENGLISH -> "Emergency SOS"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                    Text(
                        text = "Hospital • Police • Fire",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = BentoSlate500
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Card 3: Balasore Express & Transit Radar
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoCardWhite,
                border = BorderStroke(1.dp, BentoBorder),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onTransitClick)
                    .testTag("dock_transit_radar")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Train,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "BLS RADAR",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                ),
                                color = Color(0xFFB45309),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (selectedLanguage) {
                            AppLanguage.ODIA -> "ଟ୍ରେନ୍ ଓ ବସ୍ ସୂଚୀ"
                            AppLanguage.HINDI -> "ट्रेन और बस समय"
                            AppLanguage.ENGLISH -> "Transit & Trains"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                    Text(
                        text = "Express schedules",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = BentoSlate500
                    )
                }
            }

            // Card 4: Local Alerts & DRDO Bulletins
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = BentoCardWhite,
                border = BorderStroke(1.dp, BentoBorder),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable(onClick = onAlertsClick)
                    .testTag("dock_alerts_radar")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (activeAlertsCount > 0) Color(0xFFFEE2E2) else Color(0xFFF1F5F9),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (activeAlertsCount > 0) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                    contentDescription = null,
                                    tint = if (activeAlertsCount > 0) BentoRedText else BentoSlate700,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (activeAlertsCount > 0) BentoRedBg else BentoSlate100
                        ) {
                            Text(
                                text = if (activeAlertsCount > 0) "$activeAlertsCount ACTIVE" else "ALL CLEAR",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 10.sp
                                ),
                                color = if (activeAlertsCount > 0) BentoRedText else BentoSlate700,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = when (selectedLanguage) {
                            AppLanguage.ODIA -> "ସ୍ଥାନୀୟ ସତର୍କତା"
                            AppLanguage.HINDI -> "स्थानीय अलर्ट"
                            AppLanguage.ENGLISH -> "Local Alerts"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoSlate900
                    )
                    Text(
                        text = if (activeAlertsCount > 0) "DRDO & Weather warning" else "Normal conditions",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = BentoSlate500
                    )
                }
            }
        }
    }
}

/**
 * Data model representing an individual filter chip with an identifier, user-facing label,
 * optional iconography, count badge, and test identifier.
 */
data class FilterChipItem(
    val id: String,
    val label: String,
    val icon: ImageVector? = null,
    val count: Int? = null,
    val testTag: String? = null
)

/**
 * Modern Material 3 Filter Chip Group with horizontal scrolling, smooth animated pill indicators,
 * category iconography, dynamic count badges, and accessibility-compliant touch targets (>= 48dp).
 */
@Composable
fun FilterChipGroup(
    chips: List<FilterChipItem>,
    selectedId: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier,
    testTagPrefix: String = "filter_chip",
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(contentPadding),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        chips.forEach { chip ->
            val isSelected = chip.id.equals(selectedId, ignoreCase = true) ||
                    (chip.id == "Read Later" && (selectedId == "Saved" || selectedId == "Read Later")) ||
                    (chip.id == "Local" && (selectedId == "Local News" || selectedId == "Local")) ||
                    (chip.id == "Weather" && (selectedId == "Weather Alerts" || selectedId == "Weather"))

            val animatedBgColor by animateColorAsState(
                targetValue = if (isSelected) BentoPrimaryBlue else BentoCardWhite,
                animationSpec = tween(durationMillis = 200),
                label = "filter_chip_bg"
            )
            val animatedContentColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else BentoSlate700,
                animationSpec = tween(durationMillis = 200),
                label = "filter_chip_content"
            )
            val animatedBorderColor by animateColorAsState(
                targetValue = if (isSelected) BentoPrimaryBlue else BentoBorder,
                animationSpec = tween(durationMillis = 200),
                label = "filter_chip_border"
            )

            val tag = chip.testTag ?: "${testTagPrefix}_${chip.id.lowercase().replace(" ", "_").replace("&", "and")}"

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = animatedBgColor,
                contentColor = animatedContentColor,
                border = BorderStroke(1.dp, animatedBorderColor),
                shadowElevation = if (isSelected) 2.dp else 0.dp,
                modifier = Modifier
                    .testTag(tag)
                    .defaultMinSize(minHeight = 44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .clickable(onClick = { onSelect(chip.id) })
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    if (chip.icon != null) {
                        Icon(
                            imageVector = chip.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else BentoPrimaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    } else if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Text(
                        text = chip.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (chip.count != null && chip.count >= 0) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) Color.White.copy(alpha = 0.25f) else BentoBlueLight,
                            modifier = Modifier.padding(start = 2.dp)
                        ) {
                            Text(
                                text = "${chip.count}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = if (isSelected) Color.White else BentoPrimaryBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    count: Int? = null
) {
    val animatedBgColor by animateColorAsState(
        targetValue = if (isSelected) BentoPrimaryBlue else BentoCardWhite,
        animationSpec = tween(durationMillis = 200),
        label = "category_chip_bg"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else BentoSlate700,
        animationSpec = tween(durationMillis = 200),
        label = "category_chip_content"
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isSelected) BentoPrimaryBlue else BentoBorder,
        animationSpec = tween(durationMillis = 200),
        label = "category_chip_border"
    )

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = animatedBgColor,
        contentColor = animatedContentColor,
        border = BorderStroke(1.dp, animatedBorderColor),
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier
            .testTag("category_chip_${name.lowercase().replace(" ", "_").replace("&", "and")}")
            .defaultMinSize(minHeight = 44.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else BentoPrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            } else if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }

            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            )

            if (count != null && count >= 0) {
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) Color.White.copy(alpha = 0.25f) else BentoBlueLight
                ) {
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = if (isSelected) Color.White else BentoPrimaryBlue,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                    )
                }
            }
        }
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
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        Triple(
            AppTab.HOTSPOTS,
            when (selectedLanguage) {
                AppLanguage.ODIA -> "ପର୍ଯ୍ୟଟନ"
                AppLanguage.HINDI -> "पर्यटन"
                AppLanguage.ENGLISH -> "Tourism"
            },
            Icons.Default.Explore
        ),
        Triple(
            AppTab.NEWS,
            when (selectedLanguage) {
                AppLanguage.ODIA -> "ଖବର"
                AppLanguage.HINDI -> "समाचार"
                AppLanguage.ENGLISH -> "News"
            },
            Icons.AutoMirrored.Filled.Article
        ),
        Triple(
            AppTab.WEATHER,
            when (selectedLanguage) {
                AppLanguage.ODIA -> "ପାଣିପାଗ"
                AppLanguage.HINDI -> "मौसम"
                AppLanguage.ENGLISH -> "Weather"
            },
            Icons.Default.Cloud
        ),
        Triple(
            AppTab.ESSENTIALS,
            when (selectedLanguage) {
                AppLanguage.ODIA -> "ଜରୁରୀ ସେବା"
                AppLanguage.HINDI -> "सेवाएं"
                AppLanguage.ENGLISH -> "Essentials"
            },
            Icons.Default.ContactPhone
        )
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

/**
 * High-visibility, user-friendly offline status strip with an interactive Retry button.
 * Used at the top of content feeds (News, Tourism, Weather) when operating from Room cache.
 */
@Composable
fun OfflineConnectionBanner(
    isOnline: Boolean,
    isRetrying: Boolean = false,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isOnline) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = BentoAmberBg,
            border = BorderStroke(1.dp, BentoAmberText.copy(alpha = 0.35f)),
            shadowElevation = 1.dp,
            modifier = modifier
                .fillMaxWidth()
                .testTag("offline_connection_banner")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BentoAmberText.copy(alpha = 0.15f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = "Offline indicator",
                                tint = BentoAmberText,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "No Connection • Viewing Offline Data",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoAmberText
                            )
                        )
                        Text(
                            text = "Showing Room cached stories & attractions • ଇଣ୍ଟରନେଟ୍ ନାହିଁ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                color = BentoAmberText.copy(alpha = 0.85f)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = BentoAmberText,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(enabled = !isRetrying, onClick = onRetry)
                        .testTag("offline_banner_retry_btn")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        if (isRetrying) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(13.dp),
                                color = Color.White,
                                strokeWidth = 1.7.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Retry connection",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = if (isRetrying) "Retrying..." else "Retry",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }
            }
        }
    }
}


