package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ItineraryItemEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Hotspot
import kotlinx.coroutines.delay

/**
 * Interactive travel itinerary component in Jetpack Compose:
 * - Allows users to select tourism hotspots in Balasore and save them to a local Room-based plan.
 * - Displays entrance animations on hotspot list items when saved.
 * - Shows real-time weather icons using Coil.
 * - Integrates Google Maps SDK MapView displaying pinned locations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreItineraryPlanner(
    itineraryItems: List<ItineraryItemEntity>,
    availableHotspots: List<Hotspot>,
    language: AppLanguage,
    onAddToItinerary: (Hotspot, Int, String, String) -> Unit,
    onRemoveFromItinerary: (Long) -> Unit,
    onClearItinerary: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDayFilter by remember { mutableIntStateOf(0) } // 0 = All Days, 1 = Day 1, 2 = Day 2, 3 = Day 3
    var selectedItemForMap by remember { mutableStateOf<ItineraryItemEntity?>(null) }
    var isAddSheetOpen by remember { mutableStateOf(false) }
    var newlyAddedItemId by remember { mutableStateOf<Long?>(null) }
    var previousItemCount by remember { mutableIntStateOf(itineraryItems.size) }

    // When a user saves a new location to the itinerary, trigger the highlight & animation
    androidx.compose.runtime.LaunchedEffect(itineraryItems.size) {
        if (itineraryItems.size > previousItemCount) {
            val newestItem = itineraryItems.lastOrNull()
            if (newestItem != null) {
                newlyAddedItemId = newestItem.id
                selectedItemForMap = newestItem
                // If the new item is on a different day, switch filter to that day or All
                if (selectedDayFilter != 0 && selectedDayFilter != newestItem.dayNumber) {
                    selectedDayFilter = newestItem.dayNumber
                }
            }
        }
        previousItemCount = itineraryItems.size
    }

    // Filter items based on day
    val filteredItems = remember(itineraryItems, selectedDayFilter) {
        if (selectedDayFilter == 0) itineraryItems
        else itineraryItems.filter { it.dayNumber == selectedDayFilter }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("interactive_itinerary_planner_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Title + Subtitle + Add Destination Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditCalendar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଭ୍ରମଣ ଯୋଜନା" else "Balasore Travel Itinerary",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରୁମ୍ ଡାଟାବେସ୍ ଓ ଜିପିଏସ୍ ମ୍ୟାପ୍ ସହିତ" else "Room persistence • Live weather • GPS pins",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Button(
                    onClick = { isAddSheetOpen = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier
                        .height(34.dp)
                        .testTag("open_add_to_itinerary_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Destination",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଯୋଡନ୍ତୁ" else "Add Spot",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Day Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dayOptions = listOf("All Stops (${itineraryItems.size})", "Day 1", "Day 2", "Day 3")
                dayOptions.forEachIndexed { index, title ->
                    FilterChip(
                        selected = selectedDayFilter == index,
                        onClick = { selectedDayFilter = index },
                        label = { Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        shape = RoundedCornerShape(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Google Maps SDK MapView Section
            Text(
                text = "Pinned Itinerary Route (Google Maps)",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            ItineraryMapView(
                itineraryItems = filteredItems,
                selectedItem = selectedItemForMap,
                onSelectItem = { selectedItemForMap = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Hotspot List Header & Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Planned Stops (${filteredItems.size})",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                if (itineraryItems.isNotEmpty()) {
                    Text(
                        text = "Clear All",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .clickable { onClearItinerary() }
                            .padding(4.dp)
                            .testTag("clear_itinerary_button")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No destinations in this day yet.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedButton(
                            onClick = { isAddSheetOpen = true },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Plan Your First Stop", fontSize = 12.sp)
                        }
                    }
                }
            } else {
                // Hotspot List Items with Entrance Animation
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    filteredItems.forEachIndexed { index, item ->
                        ItineraryItemRow(
                            index = index + 1,
                            item = item,
                            isSelected = selectedItemForMap?.id == item.id,
                            isNewlyAdded = newlyAddedItemId == item.id,
                            onFocusOnMap = { selectedItemForMap = item },
                            onRemove = { onRemoveFromItinerary(item.id) }
                        )
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet: Add Hotspot to Itinerary
    if (isAddSheetOpen) {
        AddHotspotToItinerarySheet(
            availableHotspots = availableHotspots,
            onDismiss = { isAddSheetOpen = false },
            onSave = { hotspot, day, time, notes ->
                onAddToItinerary(hotspot, day, time, notes)
                isAddSheetOpen = false
            }
        )
    }
}

/**
 * Individual hotspot item row within the itinerary list,
 * featuring smooth spring entrance animations when created/saved.
 */
@Composable
fun ItineraryItemRow(
    index: Int,
    item: ItineraryItemEntity,
    isSelected: Boolean,
    isNewlyAdded: Boolean,
    onFocusOnMap: () -> Unit,
    onRemove: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    // Trigger entrance animation immediately on composition
    LaunchedEffect(Unit) {
        delay(30)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it / 3 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ) + fadeIn(animationSpec = tween(400)) + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        val borderColor by animateColorAsState(
            targetValue = if (isSelected) MaterialTheme.colorScheme.primary
            else if (isNewlyAdded) Color(0xFF10B981)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
            label = "borderColor"
        )

        val elevation = if (isSelected) 4.dp else 1.dp

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onFocusOnMap() }
                .testTag("itinerary_item_${item.id}"),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ),
            border = BorderStroke(if (isSelected || isNewlyAdded) 1.5.dp else 0.5.dp, borderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = elevation)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Sequence badge + Details
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.secondaryContainer
                            )
                    ) {
                        Text(
                            text = "$index",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = "Day ${item.dayNumber} • ${item.timeSlot}",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                            if (item.category.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = item.category,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            if (isNewlyAdded) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF10B981)
                                ) {
                                    Text(
                                        text = "JUST ADDED ✨",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.hotspotName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        )

                        if (item.notes.isNotBlank()) {
                            Text(
                                text = item.notes,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Right: Real-time Weather Icon via Coil + Actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    HotspotWeatherBadge(
                        iconUrl = item.weatherIconUrl,
                        weatherCondition = item.weatherCondition,
                        tempC = item.tempC
                    )

                    IconButton(
                        onClick = onFocusOnMap,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("pin_focus_button_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Pin on Map",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("remove_itinerary_button_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove from Itinerary",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Bottom sheet enabling users to select any Balasore hotspot,
 * choose Day 1/2/3, select a time slot, and save it to Room.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHotspotToItinerarySheet(
    availableHotspots: List<Hotspot>,
    onDismiss: () -> Unit,
    onSave: (Hotspot, Int, String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedHotspot by remember { mutableStateOf(availableHotspots.firstOrNull()) }
    var selectedDay by remember { mutableIntStateOf(1) }
    var selectedTimeSlot by remember { mutableStateOf("10:00 AM") }
    var customNotes by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }

    val filteredHotspots = remember(availableHotspots, searchQuery) {
        if (searchQuery.isBlank()) availableHotspots
        else availableHotspots.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.odiaName.contains(searchQuery, ignoreCase = true) ||
            it.category.contains(searchQuery, ignoreCase = true)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Sheet Title
            Text(
                text = "Add Destination to Itinerary",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            )
            Text(
                text = "Select a Balasore hotspot to save into your local Room itinerary plan",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Day Selector
            Text(
                text = "Itinerary Day",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 2, 3).forEach { day ->
                    FilterChip(
                        selected = selectedDay == day,
                        onClick = { selectedDay = day },
                        label = { Text("Day $day") },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Time Slot Selector
            Text(
                text = "Scheduled Time Slot",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            val timeOptions = listOf("08:30 AM", "11:00 AM", "02:30 PM", "05:00 PM", "07:30 PM")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(timeOptions) { time ->
                    FilterChip(
                        selected = selectedTimeSlot == time,
                        onClick = { selectedTimeSlot = time },
                        label = { Text(time, fontSize = 11.sp) },
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hotspot Selection
            Text(
                text = "Choose Hotspot (${filteredHotspots.size} available)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Chandipur, Remuna, Talasari...", fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredHotspots) { hotspot ->
                    val isPicked = selectedHotspot?.id == hotspot.id
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedHotspot = hotspot }
                            .testTag("hotspot_pick_${hotspot.id}"),
                        shape = RoundedCornerShape(10.dp),
                        color = if (isPicked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(
                            if (isPicked) 1.5.dp else 0.5.dp,
                            if (isPicked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = hotspot.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "${hotspot.odiaName} • ${hotspot.category}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                            if (isPicked) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Notes TextField
            OutlinedTextField(
                value = customNotes,
                onValueChange = { customNotes = it },
                label = { Text("Personal Notes (Optional)", fontSize = 11.sp) },
                placeholder = { Text("e.g. Try special prasad, photograph receding tides", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action: Save to Room Button
            Button(
                onClick = {
                    val hotspot = selectedHotspot ?: return@Button
                    onSave(hotspot, selectedDay, selectedTimeSlot, customNotes)
                },
                enabled = selectedHotspot != null,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_itinerary_item_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save to Room Itinerary Plan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
