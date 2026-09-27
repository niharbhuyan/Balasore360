package com.example.ui.features.emergency

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
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.theme.*

data class SnakeSpeciesInfo(
    val nameEn: String,
    val nameOd: String,
    val venomType: String,
    val visualMarkers: String,
    val isVenomous: Boolean
)

data class HospitalAsvStock(
    val hospitalNameEn: String,
    val hospitalNameOd: String,
    val vialsAvailable: Int,
    val hasVentilatorIcu: Boolean,
    val emergencyPhone: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreSnakebiteAntiVenomSheet(
    language: AppLanguage,
    dailyPulse: DailyBalasorePulse,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val hospitals = listOf(
        HospitalAsvStock(
            hospitalNameEn = "Fakir Mohan MCH Balasore (Apex Hospital)",
            hospitalNameOd = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ହସ୍ପିଟାଲ",
            vialsAvailable = dailyPulse.fmmchAsvVialsAvailable,
            hasVentilatorIcu = true,
            emergencyPhone = "06782262002"
        ),
        HospitalAsvStock(
            hospitalNameEn = "Nilagiri Sub-Divisional Hospital (SDH)",
            hospitalNameOd = "ନୀଳଗିରି ଉପଖଣ୍ଡ ଡାକ୍ତରଖାନା",
            vialsAvailable = dailyPulse.nilagiriAsvVials,
            hasVentilatorIcu = true,
            emergencyPhone = "06782233221"
        ),
        HospitalAsvStock(
            hospitalNameEn = "Jaleswar Community Health Centre (CHC)",
            hospitalNameOd = "ଜଳେଶ୍ୱର ଗୋଷ୍ଠୀ ସ୍ୱାସ୍ଥ୍ୟ କେନ୍ଦ୍ର",
            vialsAvailable = dailyPulse.jaleswarAsvVials,
            hasVentilatorIcu = false,
            emergencyPhone = "06781222044"
        ),
        HospitalAsvStock(
            hospitalNameEn = "Soro Community Health Centre (CHC)",
            hospitalNameOd = "ସୋରୋ ଗୋଷ୍ଠୀ ସ୍ୱାସ୍ଥ୍ୟ କେନ୍ଦ୍ର",
            vialsAvailable = 18,
            hasVentilatorIcu = false,
            emergencyPhone = "06788220022"
        )
    )

    val species = listOf(
        SnakeSpeciesInfo(
            nameEn = "Common Krait (Bungarus caeruleus)",
            nameOd = "ଚିତି ସାପ (ପ୍ରାଣଘାତୀ ନ୍ୟୁରୋଟକ୍ସିକ୍)",
            venomType = "Neurotoxic (Painless bite at night)",
            visualMarkers = "Steel blue with thin white paired crossbars.",
            isVenomous = true
        ),
        SnakeSpeciesInfo(
            nameEn = "Russell's Viper (Daboia russelii)",
            nameOd = "ଚନ୍ଦନ ବୋଡ଼ା (ହେମୋଟକ୍ସିକ୍)",
            venomType = "Hemotoxic & Vasculotoxic (Swelling)",
            visualMarkers = "Three chains of dark brown oval spots on body.",
            isVenomous = true
        ),
        SnakeSpeciesInfo(
            nameEn = "Monocled Cobra (Naja kaouthia)",
            nameOd = "ଗୋଖର / ନାଗ (ନ୍ୟୁରୋଟକ୍ସିକ୍)",
            venomType = "Neurotoxic & Necrotic",
            visualMarkers = "O-shaped mark on hood, commonly in paddy fields.",
            isVenomous = true
        ),
        SnakeSpeciesInfo(
            nameEn = "Indian Rat Snake (Ptyas mucosa)",
            nameOd = "ଧମଣା (ବିଷହୀନ)",
            venomType = "Non-venomous (Farmer's ally)",
            visualMarkers = "Yellowish-brown with dark lip scales.",
            isVenomous = false
        )
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .testTag("snakebite_antivenom_sheet")
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
                            .background(Color(0xFFFEF2F2)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = "Snakebite Icon",
                            tint = Color(0xFFDC2626),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ସର୍ପାଘାତ ଓ ଆଣ୍ଟି-ଭେନମ୍ ରାଡାର" else "Balasore Snakebite & ASV Network",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ASV ଔଷଧ ମହଜୁଦ ଓ ୧୦୮ ଜରୁରୀ ସହାୟତା" else "Hospital ASV Vial Inventory & 108 Dispatch",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = BentoSlate600,
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("snakebite_close_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BentoSlate600)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Critical 108 Ambulance Call Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                border = BorderStroke(1.5.dp, Color(0xFFFCA5A5)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଜରୁରୀକାଳୀନ ଆମ୍ବୁଲାନ୍ସ ୧୦୮" else "Immediate 108 Ambulance",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF991B1B)
                            )
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "🚫 ବନ୍ଧନି (Tourniquet) ବାନ୍ଧନ୍ତୁ ନାହିଁ! ରୋଗୀଙ୍କୁ ଶାନ୍ତ ରଖି ସିଧା ହସ୍ପିଟାଲ୍ ଆଣନ୍ତୁ।" else "DO NOT tie tight tourniquets or cut wound. Keep patient calm and immobile.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFB91C1C),
                                fontSize = 11.sp
                            )
                        )
                    }
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:108")
                            }
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("snakebite_108_call_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("108", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = if (language == AppLanguage.ODIA) "ଡାକ୍ତରଖାନା ଆଣ୍ଟି-ସ୍ନେକ୍ ଭେନମ୍ (ASV) ଷ୍ଟକ୍" else "Live Hospital ASV Vial Availability",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoSlate800
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(hospitals) { hosp ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, BentoSlate200),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) hosp.hospitalNameOd else hosp.hospitalNameEn,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = BentoSlate900
                                    )
                                )
                                Text(
                                    text = if (hosp.hasVentilatorIcu) "ICU Ventilators Available • 24x7 Doctor" else "First-Aid & ASV Loading Dose Unit",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = BentoSlate600
                                    )
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFDCFCE7),
                                    border = BorderStroke(1.dp, Color(0xFF86EFAC))
                                ) {
                                    Text(
                                        text = "${hosp.vialsAvailable} Vials",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF166534),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL).apply {
                                            data = Uri.parse("tel:${hosp.emergencyPhone}")
                                        }
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call Hospital",
                                        tint = OceanBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱରର ପ୍ରମୁଖ ବିଷାକ୍ତ ସର୍ପ ପରିଚୟ" else "Local Snake Species Quick Identifier",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate800
                        )
                    )
                }

                items(species) { s ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (s.isVenomous) Color(0xFFFFF7ED) else Color(0xFFF0FDF4)
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (s.isVenomous) Color(0xFFFED7AA) else Color(0xFFBBF7D0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (language == AppLanguage.ODIA) s.nameOd else s.nameEn,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.5.sp,
                                        color = if (s.isVenomous) Color(0xFF9A3412) else Color(0xFF166534)
                                    )
                                )
                                Text(
                                    text = if (s.isVenomous) "⚠️ VENOMOUS" else "🟢 NON-VENOMOUS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (s.isVenomous) Color(0xFFC2410C) else Color(0xFF15803D)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = s.visualMarkers,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.5.sp,
                                    color = BentoSlate700
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
