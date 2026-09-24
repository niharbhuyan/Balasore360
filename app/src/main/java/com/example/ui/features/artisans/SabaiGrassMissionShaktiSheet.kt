package com.example.ui.features.artisans

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

data class SabaiProductItem(
    val productName: String,
    val productNameOd: String,
    val craftCategory: String,
    val directShgPrice: String,
    val description: String,
    val artisanCluster: String
)

data class MissionShaktiHub(
    val hubName: String,
    val hubNameOd: String,
    val locationAddress: String,
    val contactPerson: String,
    val phoneNumber: String,
    val openHours: String
)

val SABAI_PRODUCTS = listOf(
    SabaiProductItem(
        productName = "Braided Dining Placemats & Coasters (Set of 6)",
        productNameOd = "ସବାଇ ଘାସ ଡାଇନିଂ ମ୍ୟାଟ୍ ଓ କୋଷ୍ଟର ସେଟ୍",
        craftCategory = "Dining & Home Decor",
        directShgPrice = "₹350 - ₹550 (Direct Fair Price)",
        description = "Heat-resistant, washable natural golden fiber hand-braided with organic cotton thread.",
        artisanCluster = "Nilagiri & Mitrapur SHG Federation"
    ),
    SabaiProductItem(
        productName = "Eco Storage Laundry Hamper & Planter Basket",
        productNameOd = "ସବାଇ ଝୁଡ଼ି ଓ ଲଣ୍ଡ୍ରି ବାସ୍କେଟ୍",
        craftCategory = "Eco-Storage",
        directShgPrice = "₹450 - ₹850",
        description = "Sturdy coil-woven cylindrical baskets with vegan leather handles. 100% biodegradable.",
        artisanCluster = "Khantapara Mahila Mandal"
    ),
    SabaiProductItem(
        productName = "Natural Vegetable-Dyed Floor Rug / Chatai",
        productNameOd = "ପ୍ରାକୃତିକ ରଙ୍ଗର ସବାଇ ଦଉଡ଼ି ଚଟେଇ",
        craftCategory = "Floor Coverings",
        directShgPrice = "₹600 - ₹1,200",
        description = "Durable cooling floor mat dyed with indigenous turmeric, lac and catechu tree bark.",
        artisanCluster = "Srijang Tribal Cooperative"
    ),
    SabaiProductItem(
        productName = "Executive Conference Folder & Laptop Sleeve",
        productNameOd = "ସବାଇ ଅଫିସ୍ ଫୋଲ୍ଡର ଓ ବ୍ୟାଗ୍",
        craftCategory = "Lifestyle & Stationery",
        directShgPrice = "₹300 - ₹650",
        description = "Modern sustainable office accessories woven by trained tribal youth artisans.",
        artisanCluster = "Balasore Mission Shakti Innovation Center"
    )
)

val MISSION_SHAKTI_HUBS = listOf(
    MissionShaktiHub(
        hubName = "Subarnarekha Mission Shakti Haat",
        hubNameOd = "ସୁବର୍ଣ୍ଣରେଖା ମିଶନ ଶକ୍ତି ହାଟ",
        locationAddress = "Near Gandhi Smriti Bhavan, Balasore Town",
        contactPerson = "District Mission Coordinator (DSWO)",
        phoneNumber = "06782-262190",
        openHours = "10:00 AM - 08:30 PM (Daily)"
    ),
    MissionShaktiHub(
        hubName = "Nilagiri Sub-Divisional SHG Craft Center",
        hubNameOd = "ନୀଳଗିରି ମହିଳା ସମବାୟ ହସ୍ତଶିଳ୍ପ କେନ୍ଦ୍ର",
        locationAddress = "Rajbati Road, Nilagiri Town",
        contactPerson = "Nilagiri Block Project Coordinator",
        phoneNumber = "06782-233405",
        openHours = "09:30 AM - 05:00 PM (Mon-Sat)"
    ),
    MissionShaktiHub(
        hubName = "ORMAS Balasore Rural Mart",
        hubNameOd = "ଓରମାସ ଗ୍ରାମୀଣ ମାର୍ଟ",
        locationAddress = "Collectorate Square, Balasore",
        contactPerson = "Deputy CEO ORMAS",
        phoneNumber = "06782-261230",
        openHours = "10:00 AM - 07:00 PM"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SabaiGrassMissionShaktiSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .testTag("sabai_grass_mission_shakti_sheet")
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
                            .background(Color(0xFFFEF3C7)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🧺", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ସବାଇ ଘାସ ଓ ମିଶନ ଶକ୍ତି ହାଟ" else "Sabai Grass & Mission Shakti SHG",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି ସୁବର୍ଣ୍ଣ ଘାସ • ମହିଳା ସ୍ୱୟଂ ସହାୟକ ଗୋଷ୍ଠୀ" else "Golden Grass of Nilagiri • Fair-Trade Crafts",
                            fontSize = 12.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF1F5F9),
                contentColor = Color(0xFFB45309),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ହସ୍ତଶିଳ୍ପ କାଟାଲଗ୍" else "Craft Catalog",
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
                            text = if (language == AppLanguage.ODIA) "ମିଶନ ଶକ୍ତି ହାଟ" else "SHG Outlets",
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
                            text = if (language == AppLanguage.ODIA) "ଚାଷ ଓ ପରମ୍ପରା" else "Heritage Story",
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
                        items(SABAI_PRODUCTS) { item ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = if (language == AppLanguage.ODIA) item.productNameOd else item.productName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = item.directShgPrice,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFB45309),
                                            modifier = Modifier
                                                .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.description,
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "📍 Woven by: ${item.artisanCluster}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF166534),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(0.82f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(MISSION_SHAKTI_HUBS) { hub ->
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
                                                text = if (language == AppLanguage.ODIA) hub.hubNameOd else hub.hubName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "📍 ${hub.locationAddress}",
                                                fontSize = 12.sp,
                                                color = Color(0xFF475569)
                                            )
                                            Text(
                                                text = "🕒 Hours: ${hub.openHours}",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${hub.phoneNumber}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Contact", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.82f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFFFFBEB),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସବାଇ ଘାସ: ମୟୂରଭଞ୍ଜ ଓ ନୀଳଗିରିର ସୁନା ଘାସ" else "The Golden Fiber of Nilagiri Foothills",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Known scientifically as Eulaliopsis binata, Sabai grass grows abundantly on the undulating laterite hill tracts of Nilagiri. For centuries, Santhal, Kolha, and tribal women twisted the coarse grass into durable cords. Today, through Mission Shakti, over 8,000 women artisans craft internationally exported eco-friendly lifestyle products.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F),
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
