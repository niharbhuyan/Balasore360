package com.example.ui.features.heritage

import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class HeritageWalkStop(
    val stepNumber: Int,
    val title: String,
    val odiaTitle: String,
    val eraAndNation: String,
    val description: String,
    val whatToLookFor: String,
    val lat: Double,
    val lng: Double
)

/**
 * Colonial "Farashidinga & Olandeshuda" Maritime Heritage Walk Sheet.
 */
@Composable
fun ColonialHeritageWalkSheet(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val stops = listOf(
        HeritageWalkStop(
            stepNumber = 1,
            title = "Dutch Cemetery & Obelisk Tombs (1696)",
            odiaTitle = "ଓଲନ୍ଦାଜ ସମାଧିସ୍ଥଳ (ଓଲାନ୍ଦେସହୁଦା)",
            eraAndNation = "1675–1824 • Dutch East India Company (VOC)",
            description = "Established during Balasore's golden trade era when Dutch merchants exchanged Odia textiles and saltpeter for Indonesian spices.",
            whatToLookFor = "Historic obelisk of Michael Jansens (died 1696) with archaic Dutch calligraphy and merchant seals.",
            lat = 21.4985,
            lng = 86.9320
        ),
        HeritageWalkStop(
            stepNumber = 2,
            title = "Farashidinga (French Loge Enclave)",
            odiaTitle = "ଫରାସୀଡିଙ୍ଗା ଐତିହାସିକ ଘାଟ",
            eraAndNation = "1673–1947 • French East India Company",
            description = "A sovereign French enclave along the Budhabalanga riverbank until 1947, where French merchant ships anchored and hoisted the tricolor.",
            whatToLookFor = "French border stones, ancient brick customs masonry, and historic riverside steps where sloops docked.",
            lat = 21.4925,
            lng = 86.9390
        ),
        HeritageWalkStop(
            stepNumber = 3,
            title = "Barabati English Factory & Port House",
            odiaTitle = "ବାରବାଟୀ ଇଂରେଜ କୋଠି ଓ ବନ୍ଦର ମୁଖ୍ୟାଳୟ",
            eraAndNation = "1642–1947 • British East India Company",
            description = "One of the earliest British trading settlements in India, granted by the Mughal Governor Shah Shuja to Dr. Gabriel Boughton.",
            whatToLookFor = "Colonial archways, old armory foundations, and deep river anchorage berths.",
            lat = 21.4950,
            lng = 86.9280
        ),
        HeritageWalkStop(
            stepNumber = 4,
            title = "Dinamardanga (Danish Settlement)",
            odiaTitle = "ଦିନାମାରଡ଼ଙ୍ଗା ଡେନିସ୍ ବନ୍ଦର ସ୍ମାରକୀ",
            eraAndNation = "1636–1845 • Danish East India Company",
            description = "Danish crown merchants operated trading vessels here trading fine Balasore sanis, calicos, and brass metalwork.",
            whatToLookFor = "River inlet where Danish schooners took refuge during monsoons.",
            lat = 21.5010,
            lng = 86.9350
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("colonial_heritage_walk_sheet")
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
                                listOf(Color(0xFF7C3AED), Color(0xFF6D28D9))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🏰", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Colonial Maritime Heritage Walk",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ଫରାସୀଡିଙ୍ଗା ଓ ଓଲନ୍ଦାଜ ବନ୍ଦର ଐତିହ୍ୟ ପରିକ୍ରମା",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF6D28D9),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF3E8FF)
            ) {
                Text(
                    text = "4-Stop Trail",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D28D9)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // History intro
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFAF5FF),
            border = BorderStroke(1.dp, Color(0xFFE9D5FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.HistoryEdu, null, tint = Color(0xFF7C3AED), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Before Calcutta rose, 17th-century Balasore was India's premier international port where Dutch, French, Danish, and English flagships docked side-by-side along the Budhabalanga river.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF581C87), lineHeight = 16.sp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Self-Guided Walking Trail Stops",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(8.dp))

        stops.forEach { stop ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF7C3AED),
                                modifier = Modifier.size(24.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${stop.stepNumber}",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stop.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Text(
                        text = stop.odiaTitle,
                        fontSize = 11.sp,
                        color = Color(0xFF7C3AED),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 32.dp, top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF3E8FF),
                        modifier = Modifier.padding(start = 32.dp)
                    ) {
                        Text(
                            text = stop.eraAndNation,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B21A8)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stop.description,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(start = 32.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFFBEB),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp)
                    ) {
                        Text(
                            text = "🔍 Eye for Detail: ${stop.whatToLookFor}",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 10.sp,
                            color = Color(0xFF92400E),
                            lineHeight = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            openInGoogleMaps(context, stop.lat, stop.lng, stop.title)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp)
                            .height(36.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Navigation, null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Navigate to Stop #${stop.stepNumber}", fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Done with Heritage Walk", fontWeight = FontWeight.Bold)
        }
    }
}
