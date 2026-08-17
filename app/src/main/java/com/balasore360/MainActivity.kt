package com.balasore360

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.balasore360.data.BalasoreRepository
import com.balasore360.data.FavoritesStore
import com.balasore360.data.RemoteBusiness
import com.balasore360.data.RemoteEvent
import com.balasore360.data.RemoteNews
import com.balasore360.location.LocationProvider
import com.balasore360.nearby.NearbyItem
import com.balasore360.nearby.NearbyScreen

private data class Place(val id: String, val name: String, val category: String, val description: String)

private val places = listOf(
    Place("place_chandipur", "Chandipur Beach", "Beach • Tourism", "A distinctive Balasore beach known for its changing shoreline and low-tide sea-bed walks."),
    Place("place_khirachora", "Khirachora Gopinath Temple", "Temple • Heritage", "A well-known shrine at Remuna and one of Balasore's important cultural attractions."),
    Place("place_panchalingeswar", "Panchalingeswar", "Temple • Nature", "A hilltop destination near Nilagiri combining a temple, forest surroundings and a natural water cascade."),
    Place("place_talasari", "Talasari Beach", "Beach • Nature", "A scenic coastal destination in the Balasore region, suited to relaxed day trips."),
    Place("place_kuldiha", "Kuldiha Wildlife Sanctuary", "Wildlife • Nature", "A forest destination in the Balasore region known for biodiversity and wildlife experiences."),
    Place("place_bichitrapur", "Bichitrapur Mangrove Reserve", "Nature • Eco", "A mangrove destination for visitors interested in coastal ecosystems and nature."),
    Place("place_balaramgadi", "Balaramgadi", "River • Coast", "A coastal meeting point of river and sea near Chandipur and a local fishing area.")
)

private val categories = listOf(
    "Places & Tourism" to "Beaches, temples, nature and attractions",
    "Local Businesses" to "Discover shops, services and local professionals",
    "Food & Restaurants" to "Explore local food and dining options",
    "Travel & Transport" to "Useful information for getting around",
    "Local Updates" to "Keep the local information directory current"
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Surface(Modifier.fillMaxSize()) { Balasore360App() } } }
    }
}

@Composable
private fun Balasore360App() {
    val context = LocalContext.current
    val favoritesStore = remember { FavoritesStore(context.applicationContext) }
    val locationProvider = remember { LocationProvider(context.applicationContext) }
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var businesses by remember { mutableStateOf<List<RemoteBusiness>>(emptyList()) }
    var events by remember { mutableStateOf<List<RemoteEvent>>(emptyList()) }
    var news by remember { mutableStateOf<List<RemoteNews>>(emptyList()) }
    var savedIds by remember { mutableStateOf(favoritesStore.all()) }
    var loading by remember { mutableStateOf(true) }
    var userLatitude by remember { mutableStateOf<Double?>(null) }
    var userLongitude by remember { mutableStateOf<Double?>(null) }
    var locationLoading by remember { mutableStateOf(false) }
    val repository = remember { BalasoreRepository() }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            locationLoading = true
        }
    }

    LaunchedEffect(Unit) {
        loading = true
        businesses = repository.loadBusinesses()
        events = repository.loadEvents()
        news = repository.loadNews()
        loading = false
    }

    LaunchedEffect(locationLoading) {
        if (!locationLoading) return@LaunchedEffect
        val location = locationProvider.getLastKnownLocation()
        userLatitude = location?.latitude
        userLongitude = location?.longitude
        locationLoading = false
    }

    fun requestLocation() {
        if (locationProvider.hasLocationPermission()) {
            locationLoading = true
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun toggleSaved(id: String) {
        favoritesStore.toggle(id)
        savedIds = favoritesStore.all()
    }

    val savedItems = places.filter { it.id in savedIds }.map { it.id to it.name }
    val nearbyItems = businesses.mapNotNull { business ->
        val latitude = business.latitude
        val longitude = business.longitude
        if (latitude == null || longitude == null) null
        else NearbyItem(business.id, business.name, business.category, latitude, longitude, business.rating)
    }

    Scaffold(bottomBar = {
        NavigationBar {
            listOf("Home", "Nearby", "Explore", "Events", "Saved").forEachIndexed { index, title ->
                NavigationBarItem(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    icon = { Text(listOf("⌂", "⌖", "⌕", "★", "♥")[index]) },
                    label = { Text(title) }
                )
            }
        }
    }) { padding ->
        when (selectedTab) {
            0 -> HomeScreen(Modifier.padding(padding), businesses, events, news, loading, { selectedTab = 2 }, { selectedPlace = it }, ::toggleSaved, savedIds)
            1 -> NearbyScreen(userLatitude, userLongitude, nearbyItems, Modifier.padding(padding), onRequestLocation = ::requestLocation)
            2 -> ExploreScreen(Modifier.padding(padding), searchQuery, { searchQuery = it }, businesses, loading, { selectedPlace = it }, ::toggleSaved, savedIds)
            3 -> EventsScreen(Modifier.padding(padding), events, loading)
            else -> SavedScreen(Modifier.padding(padding), savedItems, ::toggleSaved)
        }
    }

    selectedPlace?.let { place ->
        val isSaved = place.id in savedIds
        AlertDialog(
            onDismissRequest = { selectedPlace = null },
            title = { Text(place.name) },
            text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(place.category, fontWeight = FontWeight.SemiBold); Text(place.description); Text("Balasore 360 local directory", style = MaterialTheme.typography.bodySmall) } },
            confirmButton = { TextButton(onClick = { toggleSaved(place.id); selectedPlace = null }) { Text(if (isSaved) "Remove saved" else "Save place") } },
            dismissButton = { TextButton(onClick = { selectedPlace = null }) { Text("Close") } }
        )
    }
}

@Composable
private fun HomeScreen(modifier: Modifier, businesses: List<RemoteBusiness>, events: List<RemoteEvent>, news: List<RemoteNews>, loading: Boolean, onExplore: () -> Unit, onPlaceClick: (Place) -> Unit, onToggleSaved: (String) -> Unit, savedIds: Set<String>) {
    LazyColumn(modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Spacer(Modifier.height(12.dp)); Text("Balasore 360", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("Your local guide to places, services, events and useful information.") }
        item { Card(Modifier.fillMaxWidth(), RoundedCornerShape(24.dp), CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text("Discover Balasore", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold); Text("Explore local places and live directory content from one app."); Button(onClick = onExplore) { Text("Start Exploring") } } } }
        item { SectionTitle("Live directory") }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) { StatCard("🏪", "Businesses", businesses.size, Modifier.weight(1f)); StatCard("🎉", "Events", events.size, Modifier.weight(1f)); StatCard("📰", "News", news.size, Modifier.weight(1f)) } }
        if (loading) item { Text("Loading local content…") }
        if (news.isNotEmpty()) { item { SectionTitle("Latest updates") }; items(news) { item -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) { Text(item.title, fontWeight = FontWeight.Bold); Text(item.category, style = MaterialTheme.typography.labelMedium); item.summary?.let { Text(it) } } } } }
        item { SectionTitle("Popular places") }
        items(places.take(5)) { place -> PlaceCard(place, place.id in savedIds, { onPlaceClick(place) }, onToggleSaved) }
        if (businesses.isNotEmpty()) { item { SectionTitle("Featured local businesses") }; items(businesses.take(5)) { business -> BusinessCard(business) } }
        item { Text("Public directory information may change. Verify important details with the provider before making decisions.", style = MaterialTheme.typography.bodySmall); Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun ExploreScreen(modifier: Modifier, query: String, onQueryChange: (String) -> Unit, businesses: List<RemoteBusiness>, loading: Boolean, onPlaceClick: (Place) -> Unit, onToggleSaved: (String) -> Unit, savedIds: Set<String>) {
    val filteredPlaces = places.filter { query.isBlank() || "${it.name} ${it.category} ${it.description}".contains(query, true) }
    val filteredBusinesses = businesses.filter { query.isBlank() || "${it.name} ${it.category} ${it.area ?: ""}".contains(query, true) }
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Explore", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("Search the Balasore360 directory."); Spacer(Modifier.height(8.dp)); OutlinedTextField(value = query, onValueChange = onQueryChange, modifier = Modifier.fillMaxWidth(), singleLine = true, label = { Text("Search") }, placeholder = { Text("Try Chandipur, restaurant, shop…") }) }
        item { SectionTitle("Categories") }
        items(categories) { (title, description) -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold); Text(description) } } }
        item { SectionTitle("Places") }
        items(filteredPlaces) { place -> PlaceCard(place, place.id in savedIds, { onPlaceClick(place) }, onToggleSaved) }
        item { SectionTitle("Businesses") }
        if (loading) item { Text("Loading businesses…") }
        items(filteredBusinesses) { business -> BusinessCard(business) }
        if (!loading && filteredBusinesses.isEmpty()) item { Text("No published businesses match this search yet.") }
    }
}

@Composable
private fun EventsScreen(modifier: Modifier, events: List<RemoteEvent>, loading: Boolean) {
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Events", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("Upcoming local events published to Balasore360.") }
        if (loading) item { Text("Loading events…") }
        items(events) { event -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Text(event.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text(listOfNotNull(event.date, event.area, event.category).joinToString(" • ")); event.description?.let { Text(it) } } } }
        if (!loading && events.isEmpty()) item { Text("No upcoming published events are available yet.") }
    }
}

@Composable
private fun SavedScreen(modifier: Modifier, savedItems: List<Pair<String, String>>, onRemove: (String) -> Unit) {
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("Saved", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold); Text("Your saved Balasore360 places.") }
        if (savedItems.isEmpty()) item { Text("Nothing saved yet. Tap the heart on a place to keep it here.") }
        items(savedItems) { (id, title) -> Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold); Text(id, style = MaterialTheme.typography.bodySmall) }; TextButton(onClick = { onRemove(id) }) { Text("Remove") } } } }
    }
}

@Composable
private fun StatCard(symbol: String, title: String, value: Int, modifier: Modifier) { Card(modifier) { Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text(symbol); Text(value.toString(), fontWeight = FontWeight.Bold); Text(title, style = MaterialTheme.typography.labelSmall) } } }

@Composable
private fun BusinessCard(business: RemoteBusiness) { Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(50.dp), RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.secondaryContainer) { Text("🏪", Modifier.padding(13.dp)) }; Spacer(Modifier.size(12.dp)); Column(Modifier.weight(1f)) { Text(business.name, fontWeight = FontWeight.Bold); Text(listOfNotNull(business.category, business.area).joinToString(" • "), style = MaterialTheme.typography.bodySmall); business.description?.let { Text(it) }; if (business.rating > 0) Text("★ ${"%.1f".format(business.rating)}") } } } }

@Composable
private fun PlaceCard(place: Place, saved: Boolean, onClick: () -> Unit, onToggleSaved: (String) -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Surface(Modifier.size(54.dp), RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.secondaryContainer) { Text("📍", Modifier.padding(14.dp)) }; Spacer(Modifier.size(12.dp)); Column(Modifier.weight(1f)) { Text(place.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Text(place.category, style = MaterialTheme.typography.bodySmall); Text(place.description) }; TextButton(onClick = { onToggleSaved(place.id) }) { Text(if (saved) "♥" else "♡") } } }
}

@Composable
private fun SectionTitle(title: String) { Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
