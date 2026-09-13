package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.GroundingResponse
import com.example.data.remote.GroundingToolMode
import com.example.data.remote.MapPlaceInfo
import com.example.data.remote.WebCitation
import com.example.ui.theme.BentoBluePill
import com.example.ui.theme.BentoBorder
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.viewmodel.GroundingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreGroundingSheet(
    groundingState: GroundingState,
    onClose: () -> Unit,
    onExecuteQuery: (query: String, mode: GroundingToolMode) -> Unit,
    onModeChange: (GroundingToolMode) -> Unit
) {
    if (!groundingState.isSheetOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var inputQuery by remember(groundingState.query) { mutableStateOf(groundingState.query) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = BentoCardWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(BentoBorder)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = BentoPrimaryBlue.copy(alpha = 0.12f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = BentoPrimaryBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Balasore AI Assistant",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoSlate900
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "gemini-3.5-flash",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = BentoPrimaryBlue
                            )
                            Text(
                                text = " • Grounded",
                                style = MaterialTheme.typography.labelSmall,
                                color = BentoSlate500
                            )
                        }
                    }
                }

                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = BentoSlate500
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grounding Tool Selector Chips
            Text(
                text = "GROUNDING TOOL MODE",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = BentoSlate500
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = groundingState.toolMode == GroundingToolMode.COMBINED,
                    onClick = { onModeChange(GroundingToolMode.COMBINED) },
                    label = { Text("⚡ Search & Maps") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BentoPrimaryBlue,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    )
                )

                FilterChip(
                    selected = groundingState.toolMode == GroundingToolMode.SEARCH_ONLY,
                    onClick = { onModeChange(GroundingToolMode.SEARCH_ONLY) },
                    label = { Text("🔍 Google Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BentoPrimaryBlue,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    )
                )

                FilterChip(
                    selected = groundingState.toolMode == GroundingToolMode.MAPS_ONLY,
                    onClick = { onModeChange(GroundingToolMode.MAPS_ONLY) },
                    label = { Text("📍 Google Maps") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Place,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BentoPrimaryBlue,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick preset suggestions
            Text(
                text = "POPULAR BALASORE QUERIES",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = BentoSlate500
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickPromptChip(
                    text = "Chandipur Tide Timings",
                    onClick = {
                        val q = "What are the Chandipur Beach low and high tide timings today in Balasore, and is it safe to walk on the receded seabed?"
                        inputQuery = q
                        onExecuteQuery(q, GroundingToolMode.SEARCH_ONLY)
                    }
                )

                QuickPromptChip(
                    text = "Panchalingeswar Route & Tips",
                    onClick = {
                        val q = "Google Maps directions and travel guide to Panchalingeswar temple & waterfalls from Balasore station with nearby scenic stops."
                        inputQuery = q
                        onExecuteQuery(q, GroundingToolMode.MAPS_ONLY)
                    }
                )

                QuickPromptChip(
                    text = "Kuldiha Wildlife Safari",
                    onClick = {
                        val q = "Kuldiha Wildlife Sanctuary safari timings, entry permit procedure, and wildlife sighting spots in Balasore."
                        inputQuery = q
                        onExecuteQuery(q, GroundingToolMode.COMBINED)
                    }
                )

                QuickPromptChip(
                    text = "Khirachora Gopinath Prasad",
                    onClick = {
                        val q = "Remuna Khirachora Gopinath temple history, morning and evening aarti timings, and famous Amruta Keli prasad availability."
                        inputQuery = q
                        onExecuteQuery(q, GroundingToolMode.SEARCH_ONLY)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Input Row
            OutlinedTextField(
                value = inputQuery,
                onValueChange = { inputQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("grounding_query_input"),
                placeholder = { Text("Ask about places, news, weather, or routes...") },
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    if (groundingState.isQuerying) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = BentoPrimaryBlue
                        )
                    } else {
                        IconButton(
                            onClick = { onExecuteQuery(inputQuery, groundingState.toolMode) },
                            enabled = inputQuery.isNotBlank(),
                            modifier = Modifier.testTag("grounding_submit_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = if (inputQuery.isNotBlank()) BentoPrimaryBlue else BentoSlate500
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BentoPrimaryBlue,
                    unfocusedBorderColor = BentoBorder
                ),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Loading Indicator
            AnimatedVisibility(visible = groundingState.isQuerying) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = BentoSlate100.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, BentoBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp,
                            color = BentoPrimaryBlue
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Querying gemini-3.5-flash...",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = BentoSlate900
                            )
                            Text(
                                text = when (groundingState.toolMode) {
                                    GroundingToolMode.SEARCH_ONLY -> "Grounded with live Google Search data"
                                    GroundingToolMode.MAPS_ONLY -> "Grounded with Google Maps places & routing"
                                    GroundingToolMode.COMBINED -> "Grounded with Google Search & Google Maps"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = BentoSlate500
                            )
                        }
                    }
                }
            }

            // Error notice
            if (groundingState.errorMessage != null && !groundingState.isQuerying) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEE2E2),
                    border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = groundingState.errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF991B1B),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Grounding Response Display
            val resp = groundingState.response
            if (resp != null && !groundingState.isQuerying) {
                GroundedResponseView(
                    response = resp,
                    context = context
                )
            }
        }
    }
}

@Composable
private fun QuickPromptChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BentoSlate100,
        border = BorderStroke(1.dp, BentoBorder),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
            color = BentoSlate700,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun GroundedResponseView(
    response: GroundingResponse,
    context: Context
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Grounding Badge Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = BentoBluePill
                ) {
                    Text(
                        text = "AI GROUNDED",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = BentoPrimaryBlue,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = BentoSlate100
                ) {
                    Text(
                        text = response.model,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp
                        ),
                        color = BentoSlate700,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Share Action
            IconButton(
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "Balasore 360 AI Grounded Guide"
                        )
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "${response.text}\n\n— Generated via Balasore 360 AI Guide (gemini-3.5-flash)"
                        )
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share AI Answer"))
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = BentoSlate500,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main Text Box
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = BentoSlate100.copy(alpha = 0.45f),
            border = BorderStroke(1.dp, BentoBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = response.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    letterSpacing = 0.2.sp
                ),
                color = BentoSlate900,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Web Citations / Sources (Google Search Grounding)
        if (response.citations.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = BentoPrimaryBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Google Search Sources & Citations (${response.citations.size})",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = BentoSlate700
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                response.citations.forEach { citation ->
                    WebCitationCard(citation = citation, context = context)
                }
            }
        }

        // Map Places (Google Maps Grounding)
        if (response.mapPlaces.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = Color(0xFFEA4335), // Google Maps Red
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Places via Google Maps (${response.mapPlaces.size})",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = BentoSlate700
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                response.mapPlaces.forEach { place ->
                    MapPlaceCard(place = place, context = context)
                }
            }
        }
    }
}

@Composable
private fun WebCitationCard(citation: WebCitation, context: Context) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = BentoCardWhite,
        border = BorderStroke(1.dp, BentoBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(citation.uri))
                    context.startActivity(browserIntent)
                } catch (_: Exception) {}
            }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = BentoPrimaryBlue,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = citation.title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = BentoSlate900,
                    maxLines = 1
                )
                Text(
                    text = citation.uri,
                    style = MaterialTheme.typography.labelSmall,
                    color = BentoSlate500,
                    maxLines = 1
                )
            }
            Icon(
                imageVector = Icons.Default.Explore,
                contentDescription = "Open Link",
                tint = BentoPrimaryBlue,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun MapPlaceCard(place: MapPlaceInfo, context: Context) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BentoCardWhite,
        border = BorderStroke(1.dp, BentoBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                try {
                    val uri = if (!place.uri.isNullOrBlank()) {
                        Uri.parse(place.uri)
                    } else {
                        Uri.parse("geo:0,0?q=${Uri.encode("${place.name}, Balasore, Odisha")}")
                    }
                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(mapIntent)
                } catch (_: Exception) {}
            }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFFEE2E2),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = place.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = BentoSlate900
                )
                if (!place.address.isNullOrBlank()) {
                    Text(
                        text = place.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = BentoSlate500,
                        maxLines = 1
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = BentoPrimaryBlue.copy(alpha = 0.1f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Map,
                        contentDescription = null,
                        tint = BentoPrimaryBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Map",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimaryBlue
                    )
                }
            }
        }
    }
}
