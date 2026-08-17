package com.balasore360.nearby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun NearbyScreen(
    userLatitude: Double?,
    userLongitude: Double?,
    items: List<NearbyItem>,
    modifier: Modifier = Modifier,
    maxDistanceMeters: Double = 25_000.0
) {
    val categories = remember(items) { listOf("All") + items.map { it.category }.distinct().sorted() }
    val selectedCategory = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("All") }
    val distances = remember(userLatitude, userLongitude, items) {
        if (userLatitude == null || userLongitude == null) emptyMap()
        else items.associate { it.id to NearbyDistance.meters(userLatitude, userLongitude, it.latitude, it.longitude) }
    }
    val visible = items
        .filter { selectedCategory.value == "All" || it.category == selectedCategory.value }
        .mapNotNull { item -> distances[item.id]?.let { item to it } }
        .filter { it.second <= maxDistanceMeters }
        .sortedBy { it.second }

    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Nearby", style = MaterialTheme.typography.headlineMedium)
                if (userLatitude == null || userLongitude == null) {
                    Text("Allow location access to find places near you.")
                } else {
                    Text("Showing places within ${maxDistanceMeters.roundToInt() / 1000} km, nearest first.")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory.value == category,
                            onClick = { selectedCategory.value = category },
                            label = { Text(category) }
                        )
                    }
                }
            }
        }
        if (userLatitude != null && userLongitude != null) {
            items(visible) { (item, distance) ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.name, style = MaterialTheme.typography.titleMedium)
                        Text(item.category, style = MaterialTheme.typography.bodySmall)
                        Text(if (distance < 1000) "${distance.roundToInt()} m away" else "${"%.1f".format(distance / 1000)} km away")
                        if (item.rating > 0) Text("★ ${"%.1f".format(item.rating)}")
                    }
                }
            }
            if (visible.isEmpty()) item { Text("No nearby places match the selected category.") }
        }
    }
}
