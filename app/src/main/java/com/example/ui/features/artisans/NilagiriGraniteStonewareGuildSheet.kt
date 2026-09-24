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

data class StoneArtisanWorkshop(
    val workshopName: String,
    val masterCraftsman: String,
    val villageLocation: String,
    val specialtyCraft: String,
    val priceEstimate: String,
    val contactPhone: String
)

data class StonewareItemGuide(
    val itemName: String,
    val itemNameOd: String,
    val culinaryUtility: String,
    val seasoningMethod: String
)

val NILAGIRI_WORKSHOPS = listOf(
    StoneArtisanWorkshop(
        workshopName = "Maa Kichakeswari Stone Carving Cooperative",
        masterCraftsman = "Shri Rabindra Maharana (State Awardee)",
        villageLocation = "Baunsabania, Nilagiri",
        specialtyCraft = "Traditional Chlorite Black Stone Bowls (Pathara Kunda), Deity Idols, Mortar-Pestles",
        priceEstimate = "₹350 - ₹12,000 (Based on size & carving)",
        contactPhone = "9437618902"
    ),
    StoneArtisanWorkshop(
        workshopName = "Mitrapur Royal Stoneware Guild",
        masterCraftsman = "Shri Brajabandhu Bindhani",
        villageLocation = "Mitrapur Artisan Lane, Nilagiri",
        specialtyCraft = "Cooking Casseroles (Pathara Kathi), Temple Pillars, Hand-turned Grinding Stones",
        priceEstimate = "₹450 - ₹6,500",
        contactPhone = "9861340912"
    ),
    StoneArtisanWorkshop(
        workshopName = "Utkalika Nilagiri Stoneware Collection",
        masterCraftsman = "Odisha State Handicrafts Corporation",
        villageLocation = "Cinema Bazar Outlet, Balasore Town",
        specialtyCraft = "Certified GI stone tableware, souvenir Konark wheels & miniature statues",
        priceEstimate = "Fixed Government Price",
        contactPhone = "06782262299"
    )
)

val STONEWARE_GUIDES = listOf(
    StonewareItemGuide(
        itemName = "Pathara Kunda (Curd & Dal Cooling Bowl)",
        itemNameOd = "ପଥର କୁଣ୍ଡ (ଦହି ଓ ଡାଲି ପାତ୍ର)",
        culinaryUtility = "Keeps natural curd cool without curdling for 48 hours. Preserves bioactive milk enzymes.",
        seasoningMethod = "Wash in warm water, coat inner surface with pure cold-pressed mustard oil, and dry in shade for 3 days before first use."
    ),
    StonewareItemGuide(
        itemName = "Pathara Kathi (Curry Slow-Cooking Pot)",
        itemNameOd = "ପଥର କାଠି (ତରକାରୀ ରନ୍ଧା ପାତ୍ର)",
        culinaryUtility = "Cooks Kanika, Besara and Dalma on low heat. Distributes thermal heat uniformly without chemical leaching.",
        seasoningMethod = "Boil rice starch water (Peyini) inside the vessel twice to seal stone micro-pores completely."
    ),
    StonewareItemGuide(
        itemName = "Silaputa / Hamamdasta (Granite Spice Mortar)",
        itemNameOd = "ଶିଳପୁତା ଓ ହାମାମଦସ୍ତା (ମସଲା ବଟା ଶିଳ)",
        culinaryUtility = "Traditional spice pounding that retains natural volatile aromatic oils of mustard and cumin.",
        seasoningMethod = "Grind dry raw rice grains repeatedly until fine white powder emerges without grey dust."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NilagiriGraniteStonewareGuildSheet(
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
                .testTag("nilagiri_granite_stoneware_sheet")
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
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🪨", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି କଳା ପଥର ଶିଳ୍ପ ଓ ବାସନ ଗିଲ୍ଡ" else "Nilagiri Granite & Stoneware Guild",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ପଥର ବାସନ • କୁଣ୍ଡ • କାରୁକାର୍ଯ୍ୟ • ଉତ୍କଳିକା" else "Mitrapur & Baunsabania Sculptors • Seasoning Guide",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
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
                color = Color(0xFFF8FAFC),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
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
                                .background(Color(0xFF475569))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Nilagiri Artisan Guild catalog active ($syncTime)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF334155)
                        )
                    }
                    Text(
                        text = "HERITAGE CRAFT",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFF8FAFC),
                contentColor = Color(0xFF334155),
                indicator = {},
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଶିଳ୍ପୀ କାର୍ଯ୍ୟଶାଳା" else "Workshops",
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
                            text = if (language == AppLanguage.ODIA) "ବାସନ ବ୍ୟବହାର ବିଧି" else "Seasoning Guide",
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
                            text = if (language == AppLanguage.ODIA) "ଅର୍ଡର ଓ ବୁକିଂ" else "Utkalika & Orders",
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
                        items(NILAGIRI_WORKSHOPS) { workshop ->
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
                                                text = workshop.workshopName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "👨‍🎨 ${workshop.masterCraftsman}",
                                                fontSize = 12.sp,
                                                color = Color(0xFF0284C7),
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text(
                                            text = workshop.priceEstimate.substringBefore("("),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF334155),
                                            modifier = Modifier
                                                .background(Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "📍 Location: ${workshop.villageLocation}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                    Text(
                                        text = "🔨 Craft: ${workshop.specialtyCraft}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${workshop.contactPhone}"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF475569)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Call Artisan / Workshop", fontSize = 11.sp)
                                    }
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
                        items(STONEWARE_GUIDES) { guide ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = if (language == AppLanguage.ODIA) guide.itemNameOd else guide.itemName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🥗 Kitchen Utility: ${guide.culinaryUtility}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0F766E),
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🧪 Traditional Seasoning: ${guide.seasoningMethod}",
                                        fontSize = 11.sp,
                                        color = Color(0xFF334155),
                                        lineHeight = 16.sp
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
                            color = Color(0xFFF0FDF4),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ନୀଳଗିରି ପଥର ଶିଳ୍ପ ସଂରକ୍ଷଣ ଓ ପ୍ରମାଣୀକରଣ" else "Authenticity & Geographical Indication (GI)",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("• Genuine chlorite stone retains cool temperature to the touch even during peak summer.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Non-stick when seasoned properly: 100% natural substitute to synthetic Teflon cookware.", fontSize = 12.sp, color = Color(0xFF14532D))
                                Text("• Direct artisan doorstep delivery available for custom temple mandap idols and royal garden fountains.", fontSize = 12.sp, color = Color(0xFF14532D))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:06782262299"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF334155)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = "Call", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Call Balasore Utkalika Emporium (06782-262299)", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
