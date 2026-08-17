package com.balasore360.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SavedScreen(modifier: Modifier = Modifier, savedIds: Set<String>) {
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Saved", style = MaterialTheme.typography.headlineMedium)
        if (savedIds.isEmpty()) {
            Text("Nothing saved yet. Tap the star on a place, business, event or news item to save it here.")
        } else {
            Text("You have ${savedIds.size} saved item${if (savedIds.size == 1) "" else "s"}.")
        }
    }
}
