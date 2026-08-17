package com.balasore360

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Place(
    val name: String,
    val category: String,
    val description: String
)

private val places = listOf(
    Place("Chandipur Beach", "Beach • Tourism", "A distinctive Balasore beach known for its changing shoreline and low-tide sea-bed walks."),
    Place("Khirachora Gopinath Temple", "Temple • Heritage", "A well-known shrine at Remuna and one of Balasore's important cultural attractions."),
    Place("Panchalingeswar", "Temple • Nature", "A hilltop destination near Nilagiri combining a temple, forest surroundings and a natural water cascade."),
    Place("Talasari Beach", "Beach • Nature", "A scenic coastal destination in the Balasore region, suited to relaxed day trips."),
    Place("Kuldiha Wildlife Sanctuary", "Wildlife • Nature", "A forest destination in the Balasore region known for biodiversity and wildlife experiences."),
    Place("Bichitrapur Mangrove Reserve", "Nature • Eco", "A mangrove destination for visitors interested in coastal ecosystems and nature."),
    Place("Balaramgadi", "River • Coast", "A coastal meeting point of river and sea near Chandipur and a local fishing area.")
)

private val categories = listOf(
    "Places & Tourism" to "Beaches, temples, nature and attractions",
    "Local Businesses" to "Discover shops, services and local professionals",
    "Food & Restaurants" to "Explore local food and dining options",
    "Travel & Transport" to "Useful information for getting around",
    "Local Updates" to "Keep the local information directory current"
)

private val eventTypes = listOf(
    "Community Events" to "Local gatherings, social activities and community programmes.",
    "Festivals & Culture" to "Festivals, cultural programmes and heritage activities.",
    "Markets & Exhibitions" to "Local markets, fairs, exhibitions and public showcases.",
    "Business Meetups" to "Networking, entrepreneurship and local business activities."
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Balasore360App()
                }
            }
        }
    }
}

@Composable
private fun Balasore360App() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    val tabs = listOf("Home", "Explore", "Events", "Profile")
    val symbols = listOf("⌂", "⌕", "★", "●")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Text(symbols[index]) },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeScreen(
                modifier = Modifier.padding(padding),
                onExplore = { selectedTab = 1 },
                onPlaceClick = { selectedPlace = it }
            )
            1 -> ExploreScreen(
                modifier = Modifier.padding(padding),
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onPlaceClick = { selectedPlace = it }
            )
            2 -> EventsScreen(Modifier.padding(padding))
            else -> ProfileScreen(Modifier.padding(padding))
        }
    }

    selectedPlace?.let { place ->
        AlertDialog(
            onDismissRequest = { selectedPlace = null },
            title = { Text(place.name) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(place.category, fontWeight = FontWeight.SemiBold)
                    Text(place.description)
                    Text("Balasore 360 local directory", style = MaterialTheme.typography.bodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedPlace = null }) { Text("Close") }
            }
        )
    }
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onExplore: () -> Unit,
    onPlaceClick: (Place) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))
            Text("Balasore 360", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Your local guide to places, services, events and useful information.")
        }
        item {
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Discover Balasore", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Explore beaches, temples, nature destinations and local categories from one app.")
                    Button(onClick = onExplore) { Text("Start Exploring") }
                }
            }
        }
        item { SectionTitle("Quick access") }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                QuickCard("📍", "Places", Modifier.weight(1f), onClick = onExplore)
                QuickCard("🏪", "Businesses", Modifier.weight(1f), onClick = onExplore)
                QuickCard("🎉", "Events", Modifier.weight(1f), onClick = onExplore)
                QuickCard("📰", "Updates", Modifier.weight(1f), onClick = onExplore)
            }
        }
        item { SectionTitle("Popular places") }
        items(places.take(5)) { place -> PlaceCard(place, onClick = { onPlaceClick(place) }) }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Built for Balasore", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("A growing local directory for residents and visitors. More businesses, events and updates can be added as the platform grows.")
                    OutlinedButton(onClick = onExplore) { Text("Explore directory") }
                }
            }
        }
        item {
            Text("Information should be checked with local authorities or providers before making important travel or service decisions.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ExploreScreen(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onPlaceClick: (Place) -> Unit
) {
    val filteredPlaces = places.filter {
        query.isBlank() || "${it.name} ${it.category} ${it.description}".contains(query, ignoreCase = true)
    }

    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Explore", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Search the Balasore360 directory.")
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search places and categories") },
                placeholder = { Text("Try Chandipur, temple, beach...") }
            )
        }
        item { SectionTitle("Categories") }
        items(categories) { (title, description) ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text(description)
                }
            }
        }
        item { SectionTitle("Places directory") }
        items(filteredPlaces) { place -> PlaceCard(place, onClick = { onPlaceClick(place) }) }
        if (filteredPlaces.isEmpty()) {
            item { Text("No matching places yet. Try another search.") }
        }
    }
}

@Composable
private fun EventsScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Events", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("A structure for local events and community activities.")
        }
        items(eventTypes) { (title, description) ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text(description)
                    OutlinedButton(onClick = {}) { Text("View events") }
                }
            }
        }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("More events coming", fontWeight = FontWeight.Bold)
                    Text("The directory can be expanded with verified event listings, dates, venues and organizer information.")
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(24.dp))
        Text("●", style = MaterialTheme.typography.displayMedium)
        Text("Balasore 360", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Local discovery made simple")
        Spacer(Modifier.height(24.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("About Balasore 360", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Balasore 360 is a local-first discovery app designed to bring places, businesses, events and useful Balasore information together.")
                Text("Version 1.1", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(12.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Directory growth", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Future updates can add verified business profiles, richer place pages, maps, user favourites and live content.")
            }
        }
    }
}

@Composable
private fun QuickCard(symbol: String, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(modifier.clickable(onClick = onClick)) {
        Column(Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(symbol, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(2.dp))
            Text(title, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun PlaceCard(place: Place, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) { Text("📍", modifier = Modifier.padding(14.dp)) }
            Spacer(Modifier.size(12.dp))
            Column(Modifier.weight(1f)) {
                Text(place.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(place.category, style = MaterialTheme.typography.bodySmall)
                Text(place.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}
