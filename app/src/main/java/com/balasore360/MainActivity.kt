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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.balasore360.data.Business
import com.balasore360.data.Event
import com.balasore360.data.News
import com.balasore360.ui.BusinessViewModel
import com.balasore360.ui.EventViewModel
import com.balasore360.ui.NewsViewModel

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

@Composable
private fun Balasore360App() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Home", "Explore", "Services", "Profile")

    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
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
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Welcome to Balasore", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Your local digital hub — built to grow into Balasore 360.")
        }
        items(features) { FeatureCard(it) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreScreen(modifier: Modifier = Modifier) {
    val newsViewModel: NewsViewModel = viewModel()
    val eventViewModel: EventViewModel = viewModel()
    val newsState by newsViewModel.uiState.collectAsStateWithLifecycle()
    val eventState by eventViewModel.uiState.collectAsStateWithLifecycle()
    val refreshing = newsState.isRefreshing || eventState.isRefreshing

    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            newsViewModel.refresh()
            eventViewModel.refresh()
        },
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Explore Balasore", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Local news and upcoming events")
                Spacer(Modifier.height(8.dp))
            }
            item { Text("Latest News", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
            contentItems(newsState.isLoading, newsState.errorMessage, newsState.items.isEmpty(), "No published news is available yet.", newsViewModel::refresh, "Loading news…")
            if (!newsState.isLoading && newsState.errorMessage == null) {
                items(newsState.items, key = { it.id }) { NewsCard(it) }
            }
            item { Text("Upcoming Events", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
            contentItems(eventState.isLoading, eventState.errorMessage, eventState.items.isEmpty(), "No published events are available yet.", eventViewModel::refresh, "Loading events…")
            if (!eventState.isLoading && eventState.errorMessage == null) {
                items(eventState.items, key = { it.id }) { EventCard(it) }
            }
        }
    }
}

private fun LazyListScope.contentItems(
    isLoading: Boolean,
    errorMessage: String?,
    isEmpty: Boolean,
    emptyMessage: String,
    onRetry: () -> Unit,
    loadingMessage: String
) {
    when {
        isLoading -> item {
            Column(Modifier.fillMaxWidth().padding(vertical = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text(loadingMessage)
            }
        }
        errorMessage != null -> item {
            Column(Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Couldn't load content", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(errorMessage)
                Spacer(Modifier.height(8.dp))
                Button(onClick = onRetry) { Text("Try again") }
            }
        }
        isEmpty -> item { Text(emptyMessage, modifier = Modifier.padding(vertical = 8.dp)) }
    }
}

@Composable
private fun ServicesScreen(modifier: Modifier = Modifier) {
    val viewModel: BusinessViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LazyColumn(modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Local Services", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Published businesses from Balasore 360")
        }
        when {
            state.isLoading -> item { LoadingState() }
            state.errorMessage != null -> item { ErrorState("Couldn't load businesses", state.errorMessage!!, viewModel::refresh) }
            state.businesses.isEmpty() -> item { Text("No published businesses are available yet.") }
            else -> items(state.businesses, key = { it.id }) { BusinessCard(it) }
        }
    }
}

@Composable private fun LoadingState() {
    Column(Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@Composable private fun ErrorState(title: String, message: String, onRetry: () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontWeight = FontWeight.Bold)
        Text(message)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Try again") }
    }
}

@Composable private fun NewsCard(news: News) {
    Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
        Text(news.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        news.category?.let { Text(it, style = MaterialTheme.typography.labelLarge) }
        news.summary?.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        news.publishedAt?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
    } }
}

@Composable private fun EventCard(event: Event) {
    Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
        Text(event.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        event.date?.let { Text("Date: $it", style = MaterialTheme.typography.labelLarge) }
        event.category?.let { Text(it, style = MaterialTheme.typography.labelMedium) }
        event.area?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        event.description?.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        event.provider?.let { Text("By $it", style = MaterialTheme.typography.bodySmall) }
    } }
}

@Composable private fun ProfileScreen(modifier: Modifier = Modifier) = EmptyState(modifier, "Profile", "Account, saved places, preferences and personalization will be connected here.")

@Composable private fun EmptyState(modifier: Modifier, title: String, description: String) {
    Column(modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable private fun FeatureCard(feature: Feature) {
    Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(Modifier.size(44.dp), shape = MaterialTheme.shapes.medium, tonalElevation = 2.dp) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) { Text(feature.title.take(1), fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.size(14.dp))
        Column(Modifier.weight(1f)) {
            Text(feature.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(3.dp))
            Text(feature.description, style = MaterialTheme.typography.bodyMedium)
        }
    } }
}

@Composable private fun BusinessCard(business: Business) {
    Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(business.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            if (business.verified) Text("✓ Verified", style = MaterialTheme.typography.labelMedium)
        }
        business.category?.let { Text(it, style = MaterialTheme.typography.labelLarge) }
        business.address?.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        business.area?.takeIf { it.isNotBlank() }?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        business.rating?.let { Text("★ ${"%.1f".format(it)} · ${business.reviewCount ?: 0} reviews") }
    } }
}
