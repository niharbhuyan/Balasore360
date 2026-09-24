package com.example.ui.features.coastal

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import java.text.SimpleDateFormat
import java.util.*

data class MangroveBoatSlot(
    val slotName: String,
    val optimalTideWindow: String,
    val durationMinutes: Int,
    val tariffPerBoat: String,
    val keySightings: String
)

data class CashewArtisanSpot(
    val hubName: String,
    val location: String,
    val specialtyGrade: String,
    val approximatePricePerKg: String,
    val contactPhone: String
)

val BICHITRAPUR_SLOTS = listOf(
    MangroveBoatSlot(
        slotName = "Morning Estuary Eco-Safari (High-Tide Creek)",
        optimalTideWindow = "07:30 AM - 10:30 AM (Water depth ~2.5m)",
        durationMinutes = 60,
        tariffPerBoat = "₹1,200 (8-Seater Motorboat with Lifejackets)",
        keySightings = "Dense mangrove canopy, Horseshoe crabs, Kingfishers, Casuarina sandbars"
    ),
    MangroveBoatSlot(
        slotName = "Afternoon Sea-Confluence Cruise (Subarnarekha Mouth)",
        optimalTideWindow = "02:30 PM - 05:00 PM",
        durationMinutes = 75,
        tariffPerBoat = "₹1,500 (Guided by Eco-Cottage Society)",
        keySightings = "Thousands of red ghost crabs basking on delta silt, Caspian terns, mudskippers"
    )
)

val BHOGRAI_CASHEW_HUBS = listOf(
    CashewArtisanSpot(
        hubName = "Talsari Coastal Cashew Farmers Cooperative",
        location = "Bhograi Block Road, near Talsari Beach",
        specialtyGrade = "W180 Jumbo & W240 Roasted Salted",
        approximatePricePerKg = "₹680 - ₹820 / kg",
        contactPhone = "9437184209"
    ),
    CashewArtisanSpot(
        hubName = "Bichitrapur Forest Eco-Vikas Cashew Unit",
        location = "Kharagpur - Chandaneswar Highway, Bhograi",
        specialtyGrade = "Organic Charcoal Roasted Cashew Kernels",
        approximatePricePerKg = "₹650 - ₹750 / kg",
        contactPhone = "9861294012"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TalsariBichitrapurMangrovePilotSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var syncTime by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        syncTime = sdf.format(Date())
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .testTag("talsari_bichitrapur_sheet")
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFCCFBF1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🦀", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ରାଡାର" else "Talsari & Bichitrapur Mangrove Pilot",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ମୋଟରବୋଟ୍ ଟାଇମ୍ • ନାଲି କଙ୍କଡ଼ା • ଭୋଗରାଇ କାଜୁ ବଜାର" else "Eco-Boats • Red Ghost Crabs • Cashew Hubs",
                            fontSize = 12.sp,
                            color = Color(0xFF0F766E)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sync pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF0FDFA),
                border = BorderStroke(1.dp, Color(0xFF99F6E4)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0D9488))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Subarnarekha Estuary & Bichitrapur live ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF115E59)
                        )
                    }
                    Text(
                        text = "TIDE FAVORS CRUISE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF0F766E),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବୋଟ୍ ସଫାରୀ" else "Mangrove Boats",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନାଲି କଙ୍କଡ଼ା ବେଳାଭୂମି" else "Crab Dunes",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଭୋଗରାଇ କାଜୁ" else "Cashew Nuts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(BICHITRAPUR_SLOTS) { slot ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = slot.slotName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "🌊 Window: ${slot.optimalTideWindow}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF0284C7),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Text(
                                            text = slot.tariffPerBoat.substringBefore("("),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F766E),
                                            modifier = Modifier
                                                .background(Color(0xFFCCFBF1), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "👀 Sightings: ${slot.keySightings}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 16.sp
                                    )
                                    Text(
                                        text = "⏱️ Trip Duration: ${slot.durationMinutes} mins with certified lifejackets",
                                        fontSize = 11.sp,
                                        color = Color(0xFF166534),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFFEF2F2),
                            border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ତାଳସାରୀ ଓ ଉଦୟପୁର ନାଲି କଙ୍କଡ଼ା ସଂରକ୍ଷଣ ନିୟମ" else "Talsari & Udaypur Red Ghost Crab Conservation",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Millions of red ghost crabs (Ocypode macrocera) carpet the sandbar at low tide.", fontSize = 12.sp, color = Color(0xFF7F1D1D))
                                Text("• STRICT BAN: Driving cars/bikes on the intertidal beach is a punishable offense.", fontSize = 12.sp, color = Color(0xFFB91C1C), fontWeight = FontWeight.Bold)
                                Text("• Low tide exposes ~4 km flat bed where walking across to Subarnarekha sandspit is permissible.", fontSize = 12.sp, color = Color(0xFF7F1D1D))
                                Text("• Return to mainland before sea surge begins at high tide.", fontSize = 12.sp, color = Color(0xFF7F1D1D))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06781232230"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call OTDC Panthanivas Chandaneswar / Talsari", fontSize = 12.sp)
                        }
                    }
                }
                2 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(BHOGRAI_CASHEW_HUBS) { cashew ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = cashew.hubName,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${cashew.location}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                            Text(
                                                text = "🥜 Grade: ${cashew.specialtyGrade} (${cashew.approximatePricePerKg})",
                                                fontSize = 11.sp,
                                                color = Color(0xFFB45309),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${cashew.contactPhone}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Call", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
