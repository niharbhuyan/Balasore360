package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.local.HotspotEntity
import com.example.data.local.ReviewEntity
import com.example.data.local.UserEntity
import com.example.data.local.WeatherCacheEntity
import com.example.ui.components.ReviewsSection
import com.example.ui.util.AppLanguage
import com.example.ui.theme.*
import kotlinx.coroutines.flow.Flow

/**
 * Dedicated Detail View Screen for Tourism Spots in Balasore.
 * Displays hero photography, Odia typography, rich metadata bento tiles,
 * highlights, travel tips, GPS navigation, and community reviews.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HotspotDetailScreen(
    hotspot: HotspotEntity,
    weather: WeatherCacheEntity? = null,
    currentUser: UserEntity? = null,
    reviewsFlow: Flow<List<ReviewEntity>>,
    onOpenAuth: () -> Unit = {},
    onSubmitReview: (rating: Int, comment: String, guestName: String?) -> Unit,
    onNavigateBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onExploreWithMaps: () -> Unit = {},
    selectedLanguage: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    val primaryName = when (selectedLanguage) {
        AppLanguage.ODIA -> if (hotspot.odiaName.isNotBlank()) hotspot.odiaName else hotspot.name
        AppLanguage.HINDI -> if (hotspot.hindiName.isNotBlank()) hotspot.hindiName else hotspot.name
        AppLanguage.ENGLISH -> hotspot.name
    }
    val secondaryName = when (selectedLanguage) {
        AppLanguage.ODIA -> hotspot.name
        AppLanguage.HINDI -> hotspot.name
        AppLanguage.ENGLISH -> hotspot.odiaName
    }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Android back button handling
    BackHandler(onBack = onNavigateBack)

    val imageRes = remember(hotspot.id) {
        getHotspotImageRes(hotspot.id)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("hotspot_detail_screen_${hotspot.id}"),
        containerColor = BentoCardWhite,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = primaryName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = secondaryName,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = BentoPrimaryBlue,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .testTag("hotspot_detail_back_button")
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Tourism Hotspots",
                            tint = BentoSlate900
                        )
                    }
                },
                actions = {
                    // Favorite Toggle Button
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .testTag("hotspot_detail_favorite_button")
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = if (hotspot.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (hotspot.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (hotspot.isFavorite) BentoRedText else BentoSlate700
                        )
                    }

                    // Share Destination Action
                    IconButton(
                        onClick = {
                            shareHotspotDetails(context, hotspot)
                        },
                        modifier = Modifier
                            .testTag("hotspot_detail_share_button")
                            .size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Destination",
                            tint = BentoSlate700
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BentoCardWhite,
                    titleContentColor = BentoSlate900
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    launchHotspotNavigationIntent(context, hotspot)
                },
                containerColor = BentoPrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .testTag("hotspot_detail_fab_directions")
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Directions,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (selectedLanguage) {
                            AppLanguage.ODIA -> "ଦିଗ ନିର୍ଣ୍ଣୟ"
                            AppLanguage.HINDI -> "दिशा-निर्देश"
                            AppLanguage.ENGLISH -> "Get Directions"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(bottom = 96.dp)
        ) {
            // Hero Image Header with Gradient Overlay & Category Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(BentoSlate900)
            ) {
                if (imageRes != null) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = hotspot.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Stylized graphic gradient background with category motif
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
                            imageVector = getHotspotCategoryIcon(hotspot.category),
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.size(110.dp)
                        )
                    }
                }

                // Gradient scrim overlay for contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.35f),
                                    Color.Black.copy(alpha = 0.85f)
                                )
                            )
                        )
                )

                // Badges & Destination Titles at bottom of Hero
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoPrimaryBlue
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = getHotspotCategoryIcon(hotspot.category),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = hotspot.category.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                        ) {
                            Text(
                                text = "${hotspot.distanceKmFromBls} km from BLS",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = primaryName,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White
                    )

                    Text(
                        text = secondaryName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFF93C5FD)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Metadata Bento Grid: 4 Core Quick Stats
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailBentoStatCard(
                        icon = Icons.Default.LocationOn,
                        title = "${hotspot.distanceKmFromBls} km",
                        subtitle = "From BLS Station",
                        accentColor = BentoPrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )
                    DetailBentoStatCard(
                        icon = Icons.Default.Payments,
                        title = hotspot.entryFee,
                        subtitle = "Entry & Passes",
                        accentColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailBentoStatCard(
                        icon = Icons.Default.AccessTime,
                        title = hotspot.timings,
                        subtitle = "Visiting Hours",
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                    DetailBentoStatCard(
                        icon = Icons.Default.CalendarMonth,
                        title = hotspot.bestTimeToVisit,
                        subtitle = "Best Season",
                        accentColor = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Coastal Tide & Weather Alert Snapshot (if applicable)
            if (hotspot.category.equals("Beach", ignoreCase = true) && weather != null) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoBlueLight),
                    border = BorderStroke(1.dp, BentoPrimaryBlue.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BentoPrimaryBlue,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.BeachAccess,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "COASTAL TIDE & SEA CONDITIONS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = BentoPrimaryBlue
                            )
                            Text(
                                text = "${weather.tideState.replace("_", " ")}: ${weather.tideDescription}",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                color = BentoSlate900
                            )
                            Text(
                                text = "Current Temperature: ${weather.temperature.toInt()}°C • ${weather.weatherDescription}",
                                style = MaterialTheme.typography.labelSmall,
                                color = BentoSlate500
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // About This Destination Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = BentoPrimaryBlue,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "About this Destination",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = hotspot.fullDescription,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = BentoSlate700
                    )

                    if (hotspot.specialty.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BentoSlate100.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "SPECIALTY & HISTORICAL SIGNIFICANCE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    ),
                                    color = BentoPrimaryBlue
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = hotspot.specialty,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = BentoSlate900
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Key Highlights Section
            val highlightItems = remember(hotspot.highlights) {
                hotspot.highlights.split(",").map { it.trim() }.filter { it.isNotBlank() }
            }
            if (highlightItems.isNotEmpty()) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = BentoGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Key Highlights",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = BentoSlate900
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            highlightItems.forEach { highlight ->
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = BentoBlueLight,
                                    border = BorderStroke(1.dp, BentoBorder)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = BentoPrimaryBlue,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = highlight,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = BentoSlate900
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Local Insider Tip Card
            if (hotspot.localTip.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "LOCAL INSIDER TRAVEL TIP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = Color(0xFFB45309)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = hotspot.localTip,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                                color = Color(0xFF78350F)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Geographical Coordinates & Map Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
                border = BorderStroke(1.dp, BentoBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = null,
                                tint = BentoPrimaryBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Location & GPS Navigation",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = BentoSlate900
                            )
                        }

                        Text(
                            text = "${"%.4f".format(hotspot.latitude)}°N, ${"%.4f".format(hotspot.longitude)}°E",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = BentoSlate500
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { launchHotspotNavigationIntent(context, hotspot) },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BentoPrimaryBlue,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("hotspot_detail_open_maps_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Directions,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Open Directions in Google Maps",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nearby Circuit & Travel Continuation Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFDCFCE7),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "RECOMMENDED CIRCUIT COMBINATION",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    fontSize = 10.sp
                                ),
                                color = Color(0xFF15803D)
                            )
                            Text(
                                text = "Nearby attractions to visit together",
                                style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate600)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val circuitRecommendation = when {
                        hotspot.category.contains("Beach", ignoreCase = true) ->
                            "Combine with Balaramgadi Fishing Harbor (4 km) for fresh coastal catch & Mirzapur sand dunes for sunset views."
                        hotspot.category.contains("Temple", ignoreCase = true) || hotspot.category.contains("Spiritual", ignoreCase = true) ->
                            "Pair your visit with Remuna's Brass & Bell-Metal Craft Village (2 km) and Emami Jagannath Mandir."
                        hotspot.category.contains("Nature", ignoreCase = true) || hotspot.category.contains("Eco", ignoreCase = true) ->
                            "Ideal full-day safari when combined with Rishia Dam reservoir & Panchalingeswar perennial hill stream."
                        else ->
                            "Explore the historic Balasore Old Town district, Raja Baikuntha Nath De heritage library, and Budhabalanga river ghats."
                    }

                    Text(
                        text = circuitRecommendation,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF14532D),
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Citizen Reviews Section
            ReviewsSection(
                targetType = "HOTSPOT",
                targetId = hotspot.id,
                targetTitle = hotspot.name,
                reviewsFlow = reviewsFlow,
                currentUser = currentUser,
                onOpenAuth = onOpenAuth,
                onSubmitReview = onSubmitReview,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun DetailBentoStatCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BentoCardWhite),
        border = BorderStroke(1.dp, BentoBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Surface(
                shape = CircleShape,
                color = accentColor.copy(alpha = 0.12f),
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = BentoSlate900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = BentoSlate500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun launchHotspotNavigationIntent(context: Context, hotspot: HotspotEntity) {
    try {
        val geoUri = Uri.parse("geo:${hotspot.latitude},${hotspot.longitude}?q=${hotspot.latitude},${hotspot.longitude}(${Uri.encode(hotspot.name)})")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        val chooser = Intent.createChooser(mapIntent, "Navigate to ${hotspot.name}")
        context.startActivity(chooser)
    } catch (_: Exception) {
        try {
            val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${hotspot.latitude},${hotspot.longitude}")
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(webIntent)
        } catch (_: Exception) {
            Toast.makeText(context, "Unable to launch map directions", Toast.LENGTH_SHORT).show()
        }
    }
}

private fun shareHotspotDetails(context: Context, hotspot: HotspotEntity) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Visit ${hotspot.name} in Balasore")
        putExtra(
            Intent.EXTRA_TEXT,
            "${hotspot.name} (${hotspot.odiaName})\n" +
                    "${hotspot.category} • ${hotspot.distanceKmFromBls} km from Balasore Station\n\n" +
                    "${hotspot.shortDescription}\n\n" +
                    "Highlights: ${hotspot.highlights}\n" +
                    "Best Time: ${hotspot.bestTimeToVisit}\n" +
                    "Visiting Hours: ${hotspot.timings}\n\n" +
                    "Discovered via Balasore 360 App by Nihar Sales"
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Tourism Destination"))
}

private fun getHotspotCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "beach", "coast", "beach & coast" -> Icons.Default.BeachAccess
        "temple", "spiritual", "heritage & spiritual" -> Icons.Default.Museum
        "nature", "wildlife", "nature & pilgrimage", "wildlife sanctuary" -> Icons.Default.Park
        "marine", "port" -> Icons.Default.DirectionsBoat
        else -> Icons.Default.Place
    }
}

private fun getHotspotImageRes(id: String): Int? {
    return null
}
