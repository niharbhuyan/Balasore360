package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.EmergencyContact
import com.example.data.model.TransitSchedule
import com.example.ui.components.AdMobBannerCard
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

@Composable
fun EssentialsScreen(
    emergencyContacts: List<EmergencyContact>,
    transitList: List<TransitSchedule>,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("essentials_screen_list"),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ଜରୁରୀକାଳୀନ ଓ ସହାୟତା ଡିରେକ୍ଟୋରୀ" else "Emergency & City Essentials",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = BentoSlate900
                    )
                )
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଡାକ୍ତରଖାନା, ପୋଲିସ ଓ ପରିବହନ ସମୟସୂଚୀ" else "Instant 1-tap dialer for hospitals, police & transit schedules",
                    style = MaterialTheme.typography.bodyMedium.copy(color = BentoSlate500)
                )
            }
        }

        // Emergency Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Emergency,
                    contentDescription = "Emergency",
                    tint = Color.Red,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Emergency Helplines (24x7)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate900
                    )
                )
            }
        }

        // Emergency Contacts List
        items(emergencyContacts, key = { it.id }) { contact ->
            EmergencyContactCard(
                contact = contact,
                language = language,
                onCall = {
                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.number}"))
                    context.startActivity(dialIntent)
                }
            )
        }

        // Transit Section Title
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Train,
                    contentDescription = "Transit",
                    tint = OceanBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Balasore Trains & Bus Schedules",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BentoSlate900
                    )
                )
            }
        }

        // Transit List
        items(transitList, key = { it.id }) { item ->
            TransitScheduleCard(item = item)
        }

        // AdMob Banner Placement in Essentials
        item {
            AdMobBannerCard(
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
fun EmergencyContactCard(
    contact: EmergencyContact,
    language: AppLanguage,
    onCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("emergency_card_${contact.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, BentoSlate100)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (contact.number == "112" || contact.number == "101") Color(0xFFFFECEB) else Color(0xFFE0F2FE)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (contact.category.contains("Police")) Icons.Default.LocalPolice else Icons.Default.LocalHospital,
                        contentDescription = "Contact Icon",
                        tint = if (contact.number == "112" || contact.number == "101") Color.Red else OceanBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (language == AppLanguage.ODIA) contact.odiaName else contact.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = contact.description,
                        style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                    )
                    Text(
                        text = "Dial: ${contact.number}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = OceanBlueDark
                        )
                    )
                }
            }

            // Call Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDCFCE7))
                    .clickable { onCall() }
                    .testTag("call_button_${contact.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun TransitScheduleCard(
    item: TransitSchedule,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BentoSlate100)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(BentoSlate100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (item.type == "Train") Icons.Default.Train else Icons.Default.DirectionsBus,
                        contentDescription = item.type,
                        tint = OceanBlue,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = item.routeName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Text(
                        text = "${item.origin} → ${item.destination}",
                        style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.time,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = OceanBlueDark
                    )
                )
                Text(
                    text = item.status,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = EmeraldGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}
