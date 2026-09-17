package com.example.ui.features.transit

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.openInGoogleMaps

data class WildlifeSpecies(
    val id: String,
    val commonName: String,
    val scientificName: String,
    val odiaName: String,
    val bestSpottingArea: String,
    val conservationStatus: String
)

/**
 * Kuldiha Eco-Tourism Safari Companion with interactive wildlife checklist.
 */
@Composable
fun KuldihaSafariCompanionSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val speciesList = remember {
        listOf(
            WildlifeSpecies("sp_1", "Asian Elephant", "Elephas maximus", "ଏସିଆନ ହାତୀ (କୁଲଡିହା ପଲ)", "Risa Waterhole & Chemchata Salt Lick", "Endangered"),
            WildlifeSpecies("sp_2", "Indian Giant Squirrel", "Ratufa indica", "ମାଳବାର ମୂଷା / ପଟ୍ଟମୂଷା", "Dense Sal canopy near Gohirabhola", "Vulnerable"),
            WildlifeSpecies("sp_3", "Malabar Pied Hornbill", "Anthracoceros coronatus", "ମାଳବାର ଚିତ୍ରିତ ଧନେଶ", "Upper valley fruit trees & stream beds", "Near Threatened"),
            WildlifeSpecies("sp_4", "Indian Leopard (Pugmarks)", "Panthera pardus fusca", "କଲରାପତରିଆ ବାଘ", "Rocky ridges & night camera traps", "Vulnerable"),
            WildlifeSpecies("sp_5", "Sambar & Spotted Deer", "Rusa unicolor", "ଶମ୍ବର ଓ ଚିତ୍ରା ହରିଣ", "Grassy clearings near Purunapani", "Least Concern"),
            WildlifeSpecies("sp_6", "Indian Pangolin", "Manis crassicaudata", "ବଜ୍ରକାପ୍ତା", "Termite mounds near forest fringes", "Endangered")
        )
    }

    // Interactive sighting state
    val sightedMap = remember {
        mutableStateMapOf<String, Boolean>(
            "sp_1" to true, // Sample sighting
            "sp_2" to true
        )
    }

    val sightedCount = sightedMap.values.count { it }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("kuldiha_safari_companion_sheet")
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF059669), Color(0xFF047857))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Forest,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Kuldiha Safari Companion",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "କୁଲଡିହା ବନ୍ୟପ୍ରାଣୀ ଅଭୟାରଣ୍ୟ ସଫାରୀ ଗାଇଡ୍",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF059669),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFD1FAE5)
            ) {
                Text(
                    text = "$sightedCount / ${speciesList.size} Sighted",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Safari Gate & Timings Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
            border = BorderStroke(1.dp, Color(0xFFA7F3D0))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⏰ Safari Gate Shifts",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF065F46)
                    )
                    Text(
                        text = "Chemchata & Gohirabhola",
                        fontSize = 11.sp,
                        color = Color(0xFF047857),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("• Morning Shift: 06:00 AM - 10:00 AM", fontSize = 11.sp, color = Color(0xFF064E3B))
                    Text("• Afternoon Shift: 02:30 PM - 05:30 PM", fontSize = 11.sp, color = Color(0xFF064E3B))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Permit: ₹40/adult • Vehicle: ₹100 • Guide: ₹200", fontSize = 10.sp, color = Color(0xFF065F46))

                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:06782262334") // Balasore Wildlife Division
                            }
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp)
                    ) {
                        Icon(Icons.Default.Call, null, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Forest Office", fontSize = 10.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive Wildlife Checklist
        Text(
            text = "Wildlife Sighting Checklist (Tap to Mark)",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(6.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(speciesList, key = { it.id }) { sp ->
                val isSighted = sightedMap[sp.id] == true

                Card(
                    onClick = { sightedMap[sp.id] = !isSighted },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSighted) Color(0xFFF0FDF4) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isSighted) Color(0xFF86EFAC) else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSighted,
                            onCheckedChange = { sightedMap[sp.id] = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF059669),
                                uncheckedColor = Color(0xFF94A3B8)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = sp.commonName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSighted) Color(0xFF065F46) else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isSighted) Color(0xFFD1FAE5) else Color(0xFFF1F5F9)
                                ) {
                                    Text(
                                        text = if (isSighted) "✓ SPOTTED" else sp.conservationStatus,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSighted) Color(0xFF065F46) else Color(0xFF64748B)
                                    )
                                }
                            }

                            Text(
                                text = "${sp.odiaName} • ${sp.scientificName}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            )

                            Text(
                                text = "📍 ${sp.bestSpottingArea}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = Color(0xFF059669)
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Save Safari Checklist", fontWeight = FontWeight.Bold)
        }
    }
}
