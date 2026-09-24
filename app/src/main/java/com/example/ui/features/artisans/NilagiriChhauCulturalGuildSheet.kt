package com.example.ui.features.artisans

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

data class ChhauAkhadaTroupe(
    val troupeName: String,
    val troupeNameOd: String,
    val leadGuru: String,
    val styleTradition: String,
    val contactNumber: String,
    val homeVillage: String,
    val keyRepertoire: String
)

data class ChhauMaskCraft(
    val maskCharacter: String,
    val maskCharacterOd: String,
    val materialCraft: String,
    val priceRange: String,
    val artisanVillage: String
)

val CHHAU_AKHADAS = listOf(
    ChhauAkhadaTroupe(
        troupeName = "Nilagiri Rajbati Chhau Nritya Nityananda Akhada",
        troupeNameOd = "ନୀଳଗିରି ରାଜବାଟୀ ଛଉ ନୃତ୍ୟ ନିତ୍ୟାନନ୍ଦ ଆଖଡ଼ା",
        leadGuru = "Guru Ramesh Chandra Mohapatra (Sangeet Natak Akademi Awardee)",
        styleTradition = "Nilagiri Royal Martial Dharpati Style (Sword & Shield)",
        contactNumber = "06782-233215",
        homeVillage = "Rajbati Colony, Nilagiri Town",
        keyRepertoire = "Garuda Vahana, Mahisasura Mardini, Hunter Dance (Shikari)"
    ),
    ChhauAkhadaTroupe(
        troupeName = "Mitrapur Tribal Folk Chhau Sanskrutik Kendra",
        troupeNameOd = "ମିତ୍ରପୁର ଆଦିବାସୀ ଲୋକ ଛଉ ସାଂସ୍କୃତିକ କେନ୍ଦ୍ର",
        leadGuru = "Guru Sukra Majhi & Troupe",
        styleTradition = "Santhali & Kolha Indigenous War Dance Blend",
        contactNumber = "06782-244190",
        homeVillage = "Mitrapur, Nilagiri Block",
        keyRepertoire = "Jambeb, Sabara-Sabaruni, Dhemsa-Chhau Fusion"
    ),
    ChhauAkhadaTroupe(
        troupeName = "Baleswar Zilla Chhau Kalaparishad",
        troupeNameOd = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ଛଉ କଳାପରିଷଦ",
        leadGuru = "Guru Prasant Panda",
        styleTradition = "Mayurbhanj-Nilagiri Regional Synthesis",
        contactNumber = "06782-262840",
        homeVillage = "FM College Road, Balasore Town",
        keyRepertoire = "Megha Duta, Ekalavya, Nataraja Tandava"
    )
)

val CHHAU_MASKS = listOf(
    ChhauMaskCraft(
        maskCharacter = "Devi Durga / Mahisasura Fierce War Mask",
        maskCharacterOd = "ଦେବୀ ଦୁର୍ଗା ଓ ମହିଷାସୁର ମୁଖା",
        materialCraft = "Multilayer papier-mâché, Multani clay & natural vegetable dyes",
        priceRange = "₹850 - ₹1,800",
        artisanVillage = "Gohira Shilpigram, Nilagiri"
    ),
    ChhauMaskCraft(
        maskCharacter = "Garuda & Dancing Peacock Crown",
        maskCharacterOd = "ଗରୁଡ଼ ଓ ନୃତ୍ୟରତ ମୟୂର ମୁଖା",
        materialCraft = "Embossed gilt copper foil, hand-pressed river silt & feathers",
        priceRange = "₹650 - ₹1,400",
        artisanVillage = "Mitrapur Mastercraft Cluster"
    ),
    ChhauMaskCraft(
        maskCharacter = "Tribal Hunter (Shikari) & Royal Lion Mask",
        maskCharacterOd = "ଶିକାରୀ ଓ କେଶରୀ ସିଂହ ମୁଖା",
        materialCraft = "Lightweight waterproof tree resin and organic lac glaze",
        priceRange = "₹500 - ₹1,200",
        artisanVillage = "Nilagiri Rajbati Artisan Guild"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NilagiriChhauCulturalGuildSheet(
    language: AppLanguage,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var calendarSyncTime by remember { mutableStateOf("Just now") }

    LaunchedEffect(Unit) {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        calendarSyncTime = sdf.format(Date())
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
                .testTag("nilagiri_chhau_cultural_guild_sheet")
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
                        Text("🎭", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି ଛଉ ନୃତ୍ୟ ଆଖଡ଼ା ଓ ମୁଖା ଶିଳ୍ପୀ ଗିଲ୍ଡ" else "Nilagiri Chhau Dance & Mask Artisan Guild",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ରାଜକୀୟ ଯୁଦ୍ଧ ନୃତ୍ୟ • ମୁଖା ଶିଳ୍ପ • ଚୈତ୍ର ପର୍ବ କ୍ୟାଲେଣ୍ଡର" else "Royal Martial Dance • Papier-mâché Masks • Rehearsals",
                            fontSize = 12.sp,
                            color = Color(0xFFB45309)
                        )
                    }
                }
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Cultural Calendar Sync Pill
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFFBEB),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD97706))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Guild schedule synchronized at $calendarSyncTime",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF92400E)
                        )
                    }
                    Text(
                        text = "CHAITRA PARVA PREP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB45309)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFFB45309),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଛଉ ଆଖଡ଼ା ଓ ଗୁରୁ" else "Troupes & Gurus",
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
                            text = if (language == AppLanguage.ODIA) "ମୁଖା ଶିଳ୍ପୀ କାଟାଲଗ୍" else "Mask Craft",
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
                            text = if (language == AppLanguage.ODIA) "ଇତିହାସ ଓ ଶୈଳୀ" else "Martial Heritage",
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
                        items(CHHAU_AKHADAS) { troupe ->
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
                                                text = if (language == AppLanguage.ODIA) troupe.troupeNameOd else troupe.troupeName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "👑 Guru: ${troupe.leadGuru}",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = Color(0xFFB45309)
                                            )
                                        }
                                        Button(
                                            onClick = {
                                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${troupe.contactNumber}"))
                                                context.startActivity(intent)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Icon(Icons.Default.Phone, contentDescription = "Book", modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Book", fontSize = 11.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "📍 Village: ${troupe.homeVillage} • Style: ${troupe.styleTradition}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🗡️ Repertoire: ${troupe.keyRepertoire}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF1E293B),
                                        fontWeight = FontWeight.Medium
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
                        items(CHHAU_MASKS) { mask ->
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
                                            text = if (language == AppLanguage.ODIA) mask.maskCharacterOd else mask.maskCharacter,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F172A),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = mask.priceRange,
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
                                        text = "Craft: ${mask.materialCraft}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "📍 Handcrafted at: ${mask.artisanVillage}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF15803D),
                                        fontWeight = FontWeight.SemiBold
                                    )
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
                                    text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି ଛଉ: ରାଜଦରବାରରୁ ବିଶ୍ୱ ମଞ୍ଚ ପର୍ଯ୍ୟନ୍ତ" else "Nilagiri Chhau: Royal Martial Traditions",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Patronized by the erstwhile Bhanja rulers of Nilagiri Princely State, Nilagiri Chhau is an acrobatic martial dance blending Paika war techniques with classical Natyashastra postures. Unlike Purulia Chhau, the Nilagiri-Mayurbhanj variant emphasizes intricate footwork (Chalis and Upalays) and sword battles without heavy speech, accompanied by the thundering beat of the Dhol, Dhamsa, and Mahuri.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF78350F),
                                    lineHeight = 17.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "🥁 Annual High Point: Chaitra Parva (April) at Nilagiri Palace Grounds & Baleswar Mahotsav.",
                                    fontSize = 11.sp,
                                    color = Color(0xFFB45309),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
