package com.balasore360.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Reusable Saved screen. Main navigation can wire this into the bottom bar incrementally. */
@Composable
fun FavoritesScreen(
    savedItems: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Saved", style = MaterialTheme.typography.headlineMedium)
                Text("Your saved Balasore360 places, businesses and events.")
            }
        }
        if (savedItems.isEmpty()) {
            item { Text("Nothing saved yet. Tap the save button on an item to keep it here.") }
        } else {
            items(savedItems) { (id, title) ->
                Card {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(title, style = MaterialTheme.typography.titleMedium)
                        Text(id, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
