package com.example.ui.features.transit

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.ui.theme.*

data class TotoRouteFare(
    val routeId: String,
    val fromPlace: String,
    val fromPlaceOdia: String,
    val toPlace: String,
    val toPlaceOdia: String,
    val distanceKm: Double,
    val sharedTotoFare: Int,
    val reservedAutoFare: Int,
    val estTravelTimeMins: Int,
    val frequencyStatus: String, // Very Frequent (Every 2-5 min), Frequent, Regular
    val keyStops: String
)

val BALASORE_TOTO_ROUTES = listOf(
    TotoRouteFare(
        routeId = "route_bls_chandipur",
        fromPlace = "Balasore Railway Station",
        fromPlaceOdia = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ",
        toPlace = "Chandipur Sea Beach",
        toPlaceOdia = "ଚାନ୍ଦିପୁର ସମୁଦ୍ର ବେଳାଭୂମି",
        distanceKm = 14.5,
        sharedTotoFare = 35,
        reservedAutoFare = 220,
        estTravelTimeMins = 32,
        frequencyStatus = "Every 5 mins from Station North Gate",
        keyStops = "Station ➔ FM Circle ➔ Bampada ➔ ITI Square ➔ Chandipur Market ➔ Beach"
    ),
    TotoRouteFare(
        routeId = "route_bls_remuna",
        fromPlace = "Balasore Railway Station / Bus Stand",
        fromPlaceOdia = "ରେଳ ଷ୍ଟେସନ / ବସ୍ ଷ୍ଟାଣ୍ଡ",
        toPlace = "Remuna Khirachora Gopinath",
        toPlaceOdia = "ରେମୁଣା ଖିରଚୋରା ଗୋପୀନାଥ",
        distanceKm = 8.2,
        sharedTotoFare = 20,
        reservedAutoFare = 140,
        estTravelTimeMins = 20,
        frequencyStatus = "High Frequency (Every 3 mins)",
        keyStops = "Sahadevkhunta ➔ Cinema Chhak ➔ ITI ➔ Remuna Golei ➔ Mandir Gate"
    ),
    TotoRouteFare(
        routeId = "route_fmu_campus",
        fromPlace = "Sahadevkhunta Central Bus Stand",
        fromPlaceOdia = "ସହଦେବଖୁଣ୍ଟା କେନ୍ଦ୍ରୀୟ ବସ୍ ଷ୍ଟାଣ୍ଡ",
        toPlace = "FM University New Campus (Nuapadhi)",
        toPlaceOdia = "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ ନୂଆ କ୍ୟାମ୍ପସ୍",
        distanceKm = 12.0,
        sharedTotoFare = 30,
        reservedAutoFare = 180,
        estTravelTimeMins = 26,
        frequencyStatus = "Frequent (Every 6 mins during university hours)",
        keyStops = "Bus Stand ➔ Phandi Chhak ➔ Kuruda Bypass ➔ Nuapadhi ➔ FMU Main Gate"
    ),
    TotoRouteFare(
        routeId = "route_emami_jagannath",
        fromPlace = "Balasore Railway Station",
        fromPlaceOdia = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ",
        toPlace = "Emami Jagannath Temple",
        toPlaceOdia = "ଇମାମୀ ଜଗନ୍ନାଥ ମନ୍ଦିର",
        distanceKm = 6.5,
        sharedTotoFare = 20,
        reservedAutoFare = 120,
        estTravelTimeMins = 16,
        frequencyStatus = "Regular (Every 5 mins)",
        keyStops = "Station ➔ Motiganj ➔ Remuna Golei ➔ Emami Complex"
    ),
    TotoRouteFare(
        routeId = "route_dhh_hospital",
        fromPlace = "Balasore Railway Station",
        fromPlaceOdia = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ",
        toPlace = "DHH & Medical College (FMMCH)",
        toPlaceOdia = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଡାକ୍ତରଖାନା ଓ ମେଡ଼ିକାଲ କଲେଜ",
        distanceKm = 2.8,
        sharedTotoFare = 10,
        reservedAutoFare = 60,
        estTravelTimeMins = 8,
        frequencyStatus = "Continuous 24x7 Shuttle",
        keyStops = "Station ➔ Cinema Chhak ➔ Phandi Chhak ➔ DHH Casualty Gate"
    ),
    TotoRouteFare(
        routeId = "route_kuruda_industrial",
        fromPlace = "Cinema Chhak / Sahadevkhunta",
        fromPlaceOdia = "ସିନେମା ଛକ / ସହଦେବଖୁଣ୍ଟା",
        toPlace = "Kuruda Industrial Estate & NOCCI",
        toPlaceOdia = "କୁରୁଡ଼ା ଶିଳ୍ପାଞ୍ଚଳ ଓ ନୋସି ପାର୍କ",
        distanceKm = 9.0,
        sharedTotoFare = 25,
        reservedAutoFare = 150,
        estTravelTimeMins = 22,
        frequencyStatus = "Regular (Every 8 mins)",
        keyStops = "Sahadevkhunta ➔ Angargadia ➔ Kuruda Square ➔ NOCCI Business Park"
    ),
    TotoRouteFare(
        routeId = "route_balaramgadi_fish",
        fromPlace = "Balasore Railway Station",
        fromPlaceOdia = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ",
        toPlace = "Balaramgadi Fishing Harbor",
        toPlaceOdia = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର",
        distanceKm = 16.0,
        sharedTotoFare = 40,
        reservedAutoFare = 240,
        estTravelTimeMins = 35,
        frequencyStatus = "Morning Peak (5:00 AM - 10:00 AM)",
        keyStops = "Station ➔ Chandipur Road ➔ Chaumukh Chhak ➔ Estuary Harbor Gate"
    )
)

/**
 * Balasore Toto & Auto Fare Card & Calculator.
 * Provides transparent RTA municipal fare rates, route lookup, passenger rights against overcharging,
 * and direct one-touch access to traffic helpline and municipal stands.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreTotoFareCardSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedFilter by remember { mutableStateOf("All Routes") }
    var rideMode by remember { mutableStateOf("Shared Toto") } // "Shared Toto" or "Reserved Auto"
    var isNightTravel by remember { mutableStateOf(false) } // 10 PM - 5 AM (+20%)

    val filterOptions = listOf("All Routes", "Railway Station", "Chandipur", "Remuna", "University", "DHH Hospital")

    val displayedRoutes = remember(selectedFilter) {
        if (selectedFilter == "All Routes") {
            BALASORE_TOTO_ROUTES
        } else {
            BALASORE_TOTO_ROUTES.filter {
                it.fromPlace.contains(selectedFilter, ignoreCase = true) ||
                it.toPlace.contains(selectedFilter, ignoreCase = true)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .testTag("balasore_toto_fare_sheet")
        ) {
            // Header Bar
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
                            .background(Color(0xFFFEF08A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🛺", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଟୋଟୋ ଓ ଅଟୋ ଭଡ଼ା ଚାର୍ଟ" else "Balasore Toto & Auto Fare Card",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପୌରପାଳିକା ନିର୍ଦ୍ଧାରିତ ମୂଲ୍ୟ • ଅତିରିକ୍ତ ଦାବି ପ୍ରତିରୋଧ" else "Official Municipal RTA Rates • Anti-Overcharging Guide",
                            style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate500)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.88f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Interactive Fare Mode & Night Travel Switcher
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଯାତ୍ରା ପ୍ରକାର ବାଛନ୍ତୁ:" else "Select Travel Category:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate700
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = rideMode == "Shared Toto",
                                    onClick = { rideMode = "Shared Toto" },
                                    label = { Text("⚡ Shared Toto (ପ୍ରତି ସିଟ୍)") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = OceanBlue,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )

                                FilterChip(
                                    selected = rideMode == "Reserved Auto",
                                    onClick = { rideMode = "Reserved Auto" },
                                    label = { Text("🚖 Reserved Auto (ରିଜର୍ଭ)") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF0F766E),
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isNightTravel) Color(0xFFFEF3C7) else Color(0xFFF1F5F9))
                                    .clickable { isNightTravel = !isNightTravel }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = if (isNightTravel) "🌙" else "☀️", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "ରାତ୍ରିକାଳୀନ ଯାତ୍ରା (୧୦ PM - ୫ AM)" else "Night Travel Rate (10:00 PM - 05:00 AM)",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isNightTravel) Color(0xFF92400E) else BentoSlate800
                                        )
                                        Text(
                                            text = if (language == AppLanguage.ODIA) "+୨୦% ନିୟମିତ ନିୟମ ଅନୁଯାୟୀ" else "Official +20% RTA surcharge applied",
                                            fontSize = 11.sp,
                                            color = if (isNightTravel) Color(0xFFB45309) else BentoSlate500
                                        )
                                    }
                                }
                                Switch(
                                    checked = isNightTravel,
                                    onCheckedChange = { isNightTravel = it },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFD97706))
                                )
                            }
                        }
                    }
                }

                // Filter Chips Row
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.testTag("toto_filter_chips_row")
                    ) {
                        items(filterOptions) { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF0284C7),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Official Route Fare Cards
                items(displayedRoutes) { route ->
                    val baseFare = if (rideMode == "Shared Toto") route.sharedTotoFare else route.reservedAutoFare
                    val finalFare = if (isNightTravel) (baseFare * 1.20).toInt() else baseFare

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.testTag("toto_route_${route.routeId}")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "📍", fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.ODIA) route.fromPlaceOdia else route.fromPlace,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = BentoSlate900
                                            )
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 2.dp)
                                    ) {
                                        Text(text = "🏁", fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (language == AppLanguage.ODIA) route.toPlaceOdia else route.toPlace,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0369A1)
                                            )
                                        )
                                    }
                                }

                                // Fare Badge
                                Column(horizontalAlignment = Alignment.End) {
                                    Surface(
                                        color = if (rideMode == "Shared Toto") Color(0xFFEFF6FF) else Color(0xFFF0FDF4),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, if (rideMode == "Shared Toto") Color(0xFF93C5FD) else Color(0xFF86EFAC))
                                    ) {
                                        Text(
                                            text = "₹$finalFare",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (rideMode == "Shared Toto") Color(0xFF1D4ED8) else Color(0xFF15803D)
                                            )
                                        )
                                    }
                                    Text(
                                        text = if (rideMode == "Shared Toto") "per passenger" else "full auto",
                                        fontSize = 10.sp,
                                        color = BentoSlate500
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "📏 ${route.distanceKm} km • ⏱️ ~${route.estTravelTimeMins} mins",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Text(
                                    text = route.frequencyStatus,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF0F766E),
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF8FAFC)
                            ) {
                                Text(
                                    text = "🛑 Stops: ${route.keyStops}",
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                // Anti-Overcharging & Passenger Rights Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFFB45309),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଯାତ୍ରୀ ଅଧିକାର ଓ ନିୟମାବଳୀ" else "Passenger Protection & Fair Fare Rules",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (language == AppLanguage.ODIA)
                                    "• ଟୋଟୋରେ ଧାର୍ଯ୍ୟ ସିଟ୍ ସଂଖ୍ୟା ୪ ରୁ ଅଧିକ ବସିବା ବେଆଇନ।\n• ରାତି ୧୦ ଟା ପରେ ସର୍ବାଧିକ ୨୦% ଅତିରିକ୍ତ ଭଡ଼ା ଆଇନସମ୍ମତ।\n• ସାମାନ ପାଇଁ ୨୦ କେଜି ପର୍ଯ୍ୟନ୍ତ କୌଣସି ଅଲଗା ଶୁଳ୍କ ଲାଗେନାହିଁ।\n• ଅଯଥା ଦାବି କଲେ ଟ୍ରାଫିକ ହେଲ୍ପଲାଇନରେ ଯୋଗାଯୋଗ କରନ୍ତୁ।"
                                else
                                    "• E-Rickshaw maximum passenger capacity is 4 adults + luggage.\n• Night surcharge of up to 20% applies only between 10:00 PM and 05:00 AM.\n• Free luggage allowance up to 20 kg per passenger.\n• Report refusal or extortion to Balasore Municipal Police.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF78350F),
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }

                // Direct Help & Complaint Helplines
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262100"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Traffic Police (262100)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("National SOS (112)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}
