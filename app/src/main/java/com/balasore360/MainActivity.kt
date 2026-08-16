package com.balasore360

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class Feature(val title: String, val description: String)

private val features = listOf(
    Feature("Local Services", "Discover useful services and businesses around Balasore."),
    Feature("Places & Explore", "Find places, attractions and local points of interest."),
    Feature("Local Updates", "Keep the app ready for future news, events and announcements."),
    Feature("Community", "A foundation for local listings, profiles and community features.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { Balasore360App() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Balasore360App() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Home", "Explore", "Services", "Profile")

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = { TopAppBar(title = { Text("Balasore 360", fontWeight = FontWeight.Bold) }) },
                bottomBar = {
                    NavigationBar {
                        tabs.forEachIndexed { index, label ->
                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                icon = { Text(label.take(1)) },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            ) { padding ->
                when (selectedTab) {
                    0 -> HomeScreen(Modifier.padding(padding))
                    1 -> ExploreScreen(Modifier.padding(padding))
                    2 -> ServicesScreen(Modifier.padding(padding))
                    else -> ProfileScreen(Modifier.padding(padding))
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Welcome to Balasore", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Your local digital hub — built to grow into Balasore 360.")
        }
        item { Spacer(Modifier.height(4.dp)) }
        items(features) { feature -> FeatureCard(feature) }
    }
}

@Composable
private fun ExploreScreen(modifier: Modifier = Modifier) {
    EmptyState(modifier, "Explore Balasore", "Places, attractions, events and local discovery will live here.")
}

@Composable
private fun ServicesScreen(modifier: Modifier = Modifier) {
    EmptyState(modifier, "Local Services", "Business listings, categories, search and contact actions will be added here.")
}

@Composable
private fun ProfileScreen(modifier: Modifier = Modifier) {
    EmptyState(modifier, "Profile", "Account, saved places, preferences and personalization will be connected here.")
}

@Composable
private fun EmptyState(modifier: Modifier, title: String, description: String) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun FeatureCard(feature: Feature) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(modifier = Modifier.size(44.dp), shape = MaterialTheme.shapes.medium, tonalElevation = 2.dp) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(feature.title.take(1), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.size(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(feature.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(3.dp))
                Text(feature.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
