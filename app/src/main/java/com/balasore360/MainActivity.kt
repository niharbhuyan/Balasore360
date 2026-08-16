package com.balasore360

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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
    val tabs = listOf("Home", "Explore", "Events", "Profile")
    val tabSymbols = listOf("⌂", "⌕", "★", "●")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Text(tabSymbols[index]) },
                        label = { Text(title) }
                    )
                }
            }
        }
    ) { padding ->
        when (selectedTab) {
            0 -> HomeScreen(Modifier.padding(padding))
            1 -> ExploreScreen(Modifier.padding(padding))
            2 -> EventsScreen(Modifier.padding(padding))
            else -> ProfileScreen(Modifier.padding(padding))
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(Modifier.height(12.dp))
            Text("Balasore 360", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Discover Balasore. Everything local, in one place.", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(8.dp))
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text("Explore your city", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("Find places, local businesses, events and useful services around Balasore.")
                    Spacer(Modifier.height(14.dp))
                    Button(onClick = {}) { Text("Start Exploring") }
                }
            }
        }
        item { SectionTitle("Quick access") }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickCard("📍", "Places", Modifier.weight(1f))
                QuickCard("🏪", "Businesses", Modifier.weight(1f))
                QuickCard("🎉", "Events", Modifier.weight(1f))
            }
        }
        item { SectionTitle("Popular in Balasore") }
        items(listOf(
            "Chandipur Beach" to "Beach • Tourism",
            "Khirachora Gopinath" to "Heritage • Temple",
            "Talasari Beach" to "Beach • Day trip"
        )) { (title, subtitle) -> PlaceCard(title, subtitle) }
        item {
            Spacer(Modifier.height(8.dp))
            Text("Balasore 360 — your local discovery companion.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ExploreScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Explore", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Browse useful local categories.")
        }
        items(listOf(
            "🏖️ Places & Tourism" to "Discover beaches, temples and attractions",
            "🏪 Local Businesses" to "Find shops and services near you",
            "🍽️ Food & Restaurants" to "Explore local food options",
            "🚌 Travel & Transport" to "Useful travel information",
            "📰 Local Updates" to "Stay connected with Balasore"
        )) { (title, description) ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text(description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = {}) { Text("View") }
                }
            }
        }
    }
}

@Composable
private fun EventsScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Events", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("What's happening around Balasore.")
        }
        items(listOf("Community Events" to "Local gatherings and activities", "Festivals" to "Upcoming celebrations", "Business Events" to "Markets, exhibitions and meetups")) { (title, description) ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(4.dp))
                    Text(description)
                    Spacer(Modifier.height(10.dp))
                    Button(onClick = {}) { Text("Explore Events") }
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
        Text("Local discovery made simple", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(18.dp)) {
                Text("About Balasore 360", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("A local-first platform designed to help people discover places, businesses, events and useful information in Balasore.")
            }
        }
    }
}

@Composable
private fun QuickCard(symbol: String, title: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(symbol, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(4.dp))
            Text(title, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun PlaceCard(title: String, subtitle: String) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(54.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) { Text("📍", modifier = Modifier.padding(14.dp)) }
            Spacer(Modifier.size(12.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
}
