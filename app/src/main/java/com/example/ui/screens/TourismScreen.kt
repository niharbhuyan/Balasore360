package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Hotspot
import com.example.ui.components.AdMobBannerCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

@Composable
fun TourismScreen(
    hotspots: List<Hotspot>,
    selectedCategory: String,
    searchQuery: String,
    language: AppLanguage,
    favoriteIds: Set<String>,
    onCategorySelected: (String) -> Unit,
    onSearchChanged: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories = listOf("All", "Beach & Coast", "Heritage & Spiritual", "Nature & Pilgrimage", "Eco-Tourism")

    val filteredHotspots = hotspots.filter { hotspot ->
        (selectedCategory == "All" || hotspot.category == selectedCategory) &&
        (searchQuery.isBlank() || hotspot.name.contains(searchQuery, ignoreCase = true) || hotspot.odiaName.contains(searchQuery, ignoreCase = true))
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tourism_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Hero Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(OceanBlueDark, OceanBlue)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପରିଦର୍ଶନ" else "Discover Balasore",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଚାନ୍ଦିପୁର ବେଳାଭୂମି, କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ଓ କୁଲଡିହା ଅଭୟାରଣ୍ୟ"
                        else
                            "From vanishing sea tides to 12th-century Kalinga temples & wildlife reserves",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    )
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("tourism_search_input"),
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ସ୍ଥାନ ଖୋଜନ୍ତୁ..." else "Search attractions, temples, beaches...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BentoSlate400
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = OceanBlue
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OceanBlue,
                    unfocusedBorderColor = BentoSlate200,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelected(category) },
                        label = {
                            Text(
                                text = category,
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

        // Hotspots List
        items(filteredHotspots, key = { it.id }) { spot ->
            HotspotCard(
                hotspot = spot,
                language = language,
                isFavorite = favoriteIds.contains(spot.id),
                onToggleFavorite = { onToggleFavorite(spot.id) },
                onGetDirections = {
                    val uri = Uri.parse("geo:0,0?q=${Uri.encode("${spot.name}, Balasore, Odisha")}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(mapIntent)
                }
            )
        }

        // AdMob Banner Placement inside tourism feed
        item {
            AdMobBannerCard(
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun HotspotCard(
    hotspot: Hotspot,
    language: AppLanguage,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onGetDirections: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("hotspot_card_${hotspot.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BentoSlate100)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ODIA) hotspot.odiaName else hotspot.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    if (language != AppLanguage.ODIA) {
                        Text(
                            text = hotspot.odiaName,
                            style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else BentoSlate400
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = hotspot.description,
                style = MaterialTheme.typography.bodyMedium.copy(color = BentoSlate700, lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Rating Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(AmberGold.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = AmberGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = hotspot.rating.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate800
                            )
                        )
                    }

                    // Distance
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = BentoSlate400,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${hotspot.distanceKm} km",
                            style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                        )
                    }
                }

                // Directions Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(OceanBlue.copy(alpha = 0.1f))
                        .clickable { onGetDirections() }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Directions,
                        contentDescription = "Directions",
                        tint = OceanBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Map",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue
                        )
                    )
                }
            }
        }
    }
}
