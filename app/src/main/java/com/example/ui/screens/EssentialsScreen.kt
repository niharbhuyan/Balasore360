package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.PhonelinkRing
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SetMeal
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.NightlightRound
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.data.admob.AdMobManager
import com.example.data.model.AppLanguage
import com.example.data.model.EmergencyContact
import com.example.data.model.SeafoodCatch
import com.example.data.model.TransitSchedule
import com.example.ui.components.AdMobBannerCard
import com.example.ui.components.AdMobVerificationCard
import com.example.ui.components.AppWatermarkOverlay
import com.example.ui.components.WatermarkOpacity
import com.example.ui.components.WatermarkStyle
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Share
import com.example.ui.viewmodel.ThemeMode
import com.example.ui.viewmodel.UniqueFeatureSheetType
import com.example.util.PlayStoreUpdateManager
import com.example.util.UpdateUIState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EssentialsScreen(
    emergencyContacts: List<EmergencyContact>,
    transitList: List<TransitSchedule>,
    seafoodCatches: List<SeafoodCatch>,
    language: AppLanguage,
    updateState: UpdateUIState = UpdateUIState.Idle,
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    onThemeModeChange: (ThemeMode) -> Unit = {},
    isWatermarkEnabled: Boolean = true,
    watermarkStyle: WatermarkStyle = WatermarkStyle.CENTER_EMBLEM,
    watermarkOpacity: WatermarkOpacity = WatermarkOpacity.MEDIUM,
    onWatermarkToggle: (Boolean) -> Unit = {},
    onWatermarkStyleChange: (WatermarkStyle) -> Unit = {},
    onWatermarkOpacityChange: (WatermarkOpacity) -> Unit = {},
    isHourlyAutoRefreshEnabled: Boolean = true,
    lastHourlyRefreshTimestamp: Long = System.currentTimeMillis(),
    nextHourlyRefreshMinutesRemaining: Int = 60,
    autoRefreshCycleCount: Int = 0,
    onHourlyAutoRefreshToggle: (Boolean) -> Unit = {},
    onTriggerManualHourlyRefresh: () -> Unit = {},
    onCheckForUpdates: () -> Unit = {},
    onTriggerUpdate: () -> Unit = {},
    onCompleteUpdate: () -> Unit = {},
    onOpenFeatureSheet: (UniqueFeatureSheetType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var subscribedRouteIds by remember { mutableStateOf(setOf<String>()) }

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
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ଡାକ୍ତରଖାନା, ପୋଲିସ, ବାହାବଳପୁର ମାଛ ବଜାର ଓ ପରିବହନ" else "Hospitals, police, fresh harbor seafood index & transit schedules",
                    style = MaterialTheme.typography.bodyMedium.copy(color = BentoSlate500)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Quick Launch Utilities
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.TOTO_AUTO_FARE_CARD) }
                            .testTag("essentials_toto_card_btn"),
                        color = Color(0xFFFEF9C3),
                        border = BorderStroke(1.dp, Color(0xFFFDE047))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🛺", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଟୋଟୋ ଭଡ଼ା" else "Toto Fare",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF854D0E)
                                )
                                Text("RTA Chart", fontSize = 9.sp, color = Color(0xFFA16207))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.BALASORE_CAMPUS_CAREER_BOARD) }
                            .testTag("essentials_campus_btn"),
                        color = Color(0xFFE0F2FE),
                        border = BorderStroke(1.dp, Color(0xFF7DD3FC))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎓", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "କ୍ୟାମ୍ପସ୍ ବସ୍" else "FMU Campus",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF075985)
                                )
                                Text("Bus & Notice", fontSize = 9.sp, color = Color(0xFF0369A1))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.BALASORE_AUTO_UPDATE_CENTER) }
                            .testTag("essentials_auto_sync_btn"),
                        color = Color(0xFFF0FDF4),
                        border = BorderStroke(1.dp, Color(0xFF86EFAC))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔄", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଲାଇଭ୍ ସିଙ୍କ୍" else "Auto-Sync",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Text("Every 15m", fontSize = 9.sp, color = Color(0xFF15803D))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Second Quick Launch Utilities Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.BALASORE_JUNCTION_RADAR) }
                            .testTag("essentials_junction_radar_btn"),
                        color = Color(0xFFEFF6FF),
                        border = BorderStroke(1.dp, Color(0xFF93C5FD))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🚆", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଟ୍ରେନ୍ ରାଡାର" else "Rail Radar",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Text("BLS Junction", fontSize = 9.sp, color = Color(0xFF2563EB))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.NIGHT_CHEMIST_SANJEEVANI) }
                            .testTag("essentials_night_rx_btn"),
                        color = Color(0xFFFEF2F2),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💊", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "୨୪h ଔଷଧାଳୟ" else "24h Chemist",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Text("Oxygen SOS", fontSize = 9.sp, color = Color(0xFFDC2626))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.TPNODL_WATCO_MONITOR) }
                            .testTag("essentials_power_water_btn"),
                        color = Color(0xFFFFFBEB),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚡", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ବିଦ୍ୟୁତ୍/ଜଳ" else "Power/Water",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Text("Feeder Status", fontSize = 9.sp, color = Color(0xFFB45309))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Third Quick Launch Utilities Row: Civic, Health & Marine Safety
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.MO_SEVA_KENDRA_CITIZEN) }
                            .testTag("essentials_moseva_btn"),
                        color = Color(0xFFF0FDF4),
                        border = BorderStroke(1.dp, Color(0xFF86EFAC))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🌾", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ମୋ ସେବା" else "Mo Seva",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                                Text("e-District", fontSize = 9.sp, color = Color(0xFF15803D))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.FM_MCH_MEDICAL_COLLEGE_OPD) }
                            .testTag("essentials_fmmch_opd_btn"),
                        color = Color(0xFFFEF2F2),
                        border = BorderStroke(1.dp, Color(0xFFFECACA))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏥", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଏଫଏମ MCH" else "FM MCH OPD",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Text("Specialists", fontSize = 9.sp, color = Color(0xFFDC2626))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.MARINE_FISHERMEN_SAFETY) }
                            .testTag("essentials_marine_safety_btn"),
                        color = Color(0xFFEFF6FF),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚓", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସମୁଦ୍ର SOS" else "Marine SOS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF)
                                )
                                Text("Coast Guard", fontSize = 9.sp, color = Color(0xFF2563EB))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fourth Quick Launch Utilities Row: Nature, SHG Crafts & Scholarships
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.KULDIHA_ECO_CAMP_SAFARI) }
                            .testTag("essentials_kuldiha_safari_btn"),
                        color = Color(0xFFF0FDF4),
                        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🐘", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "କୁଲଡିହା ସଫାରୀ" else "Kuldiha Safari",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF14532D)
                                )
                                Text("Rissia Camp", fontSize = 9.sp, color = Color(0xFF16A34A))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.SABAI_GRASS_MISSION_SHAKTI) }
                            .testTag("essentials_sabai_craft_btn"),
                        color = Color(0xFFFFFBEB),
                        border = BorderStroke(1.dp, Color(0xFFFDE68A))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🧺", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ସବାଇ ଶିଳ୍ପ" else "Sabai Craft",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                                Text("SHG Haat", fontSize = 9.sp, color = Color(0xFFD97706))
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.BALASORE_STUDENT_CAREER_SCHOLARSHIP) }
                            .testTag("essentials_scholarship_btn"),
                        color = Color(0xFFF0F9FF),
                        border = BorderStroke(1.dp, Color(0xFFBAE6FD))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎓", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଛାତ୍ରବୃତ୍ତି" else "Scholarships",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0369A1)
                                )
                                Text("Career Desk", fontSize = 9.sp, color = Color(0xFF0284C7))
                            }
                        }
                    }
                }
            }
        }

        // FEATURE 5: Bahabalpur & Kasafal Fresh Seafood Catch Index & Harbor Rates
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("seafood_catch_index_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SetMeal,
                                    contentDescription = "Seafood",
                                    tint = OceanBlue,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ବାହାବଳପୁର ଓ କସାଫଳ ମତ୍ସ୍ୟ ସୂଚକ" else "Harbor Fresh Seafood Index",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BentoSlate900,
                                        fontSize = 15.sp
                                    )
                                )
                                Text(
                                    text = "Bahabalpur & Kasafal Landing Harbors",
                                    style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "FRESH TODAY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB45309),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tip banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF0FDF4))
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "⚓ Wholesale harbor auction: 05:30 AM – 08:30 AM daily at Bahabalpur Jetty",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF166534),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    seafoodCatches.forEach { catchItem ->
                        SeafoodCatchItemView(item = catchItem, language = language)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.HARBOR_CATCH_RATES) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_harbor_rates_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("🐟", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Live Harbor Trawler Bells & Daily Price Ticker", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Emergency Section Title
        item {
            Spacer(modifier = Modifier.height(10.dp))
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

        // Balasore Medical & Healthcare Network Card (Daily Auto-Updated)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("essentials_medical_network_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                border = BorderStroke(1.dp, Color(0xFFBAE6FD))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🩺", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Medical & Healthcare Network",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0369A1)
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFE0F2FE))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "DAILY AUTO-SYNC",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Real-time doctor OPD schedules, private hospitals & nursing homes, 24x7 pharmacies, generic stores, and diagnostic pathology labs across Balasore.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0C4A6E),
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.PRIVATE_HOSPITAL_DIRECTORY) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("🏥 Hospitals", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.DOCTORS_DIRECTORY) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0369A1)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("🩺 Doctors", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.MEDICINE_STORES_DIRECTORY) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("💊 Medicine", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.PATHOLOGY_LAB_DIRECTORY) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("🔬 Pathology", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.POLYCLINIC_DIRECTORY) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            Text("🏥 Clinics", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Cyclone Shelter Compass & Emergency Kit Action Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("essentials_cyclone_shelter_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                border = BorderStroke(1.dp, Color(0xFFBBF7D0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌀", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Cyclone Shelters & Emergency Kit",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Locate nearest high-ground cyclone shelters with offline GPS coordinates and interactive essential survival pack checklist.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF14532D),
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onOpenFeatureSheet(UniqueFeatureSheetType.CYCLONE_RESILIENCE) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Open Offline Shelter Compass & Checklist", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
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
            val isSubscribed = subscribedRouteIds.contains(item.id)
            TransitScheduleCard(
                item = item,
                isSubscribedToAlerts = isSubscribed,
                onToggleAlertNotifications = {
                    val nextSubscribed = if (isSubscribed) {
                        subscribedRouteIds - item.id
                    } else {
                        subscribedRouteIds + item.id
                    }
                    subscribedRouteIds = nextSubscribed
                    val toastMessage = if (!isSubscribed) {
                        "Push notifications enabled for ${item.routeName} alerts & schedule updates"
                    } else {
                        "Alert notifications muted for ${item.routeName}"
                    }
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }

        // AdMob Banner Placement in Essentials
        item {
            AdMobBannerCard(
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        // App Theme & Appearance Setting Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("app_theme_selection_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (themeMode) {
                                    ThemeMode.SYSTEM -> Icons.Default.BrightnessAuto
                                    ThemeMode.LIGHT -> Icons.Default.LightMode
                                    ThemeMode.DARK -> Icons.Default.DarkMode
                                    ThemeMode.HIGH_CONTRAST_DARK -> Icons.Default.NightlightRound
                                },
                                contentDescription = "Theme Icon",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଆପ୍ ରୂପରେଖା ଓ ଡିସପ୍ଲେ" else "App Theme & Appearance",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = when (themeMode) {
                                    ThemeMode.SYSTEM -> if (language == AppLanguage.ODIA) "ସିଷ୍ଟମ୍ ଡିଫଲ୍ଟ ଅନୁସାରେ ସ୍ୱୟଂକ୍ରିୟ" else "Follows system setting automatically"
                                    ThemeMode.LIGHT -> if (language == AppLanguage.ODIA) "ସର୍ବଦା ଲାଇଟ୍ ମୋଡ୍ (ଉଜ୍ଜ୍ୱଳ)" else "Always light mode"
                                    ThemeMode.DARK -> if (language == AppLanguage.ODIA) "ସର୍ବଦା ଡାର୍କ ମୋଡ୍ (କମ ଆଲୋକ ପାଇଁ ଉପଯୁକ୍ତ)" else "Comfortable dark mode for low light"
                                    ThemeMode.HIGH_CONTRAST_DARK -> if (language == AppLanguage.ODIA) "ନିଶାର୍ଦ୍ଧ ହାଇ-କଣ୍ଟ୍ରାଷ୍ଟ ଡାର୍କ (ଚକ୍ଷୁ ଶ୍ରାନ୍ତି ମୁକ୍ତ)" else "High-contrast OLED black for night news reading"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Theme selector pills
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ThemeOptionPill(
                            title = if (language == AppLanguage.ODIA) "ସିଷ୍ଟମ୍" else "System",
                            icon = Icons.Default.BrightnessAuto,
                            isSelected = themeMode == ThemeMode.SYSTEM,
                            onClick = { onThemeModeChange(ThemeMode.SYSTEM) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_btn_system")
                        )
                        ThemeOptionPill(
                            title = if (language == AppLanguage.ODIA) "ଲାଇଟ୍" else "Light",
                            icon = Icons.Default.LightMode,
                            isSelected = themeMode == ThemeMode.LIGHT,
                            onClick = { onThemeModeChange(ThemeMode.LIGHT) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_btn_light")
                        )
                        ThemeOptionPill(
                            title = if (language == AppLanguage.ODIA) "ଡାର୍କ" else "Dark",
                            icon = Icons.Default.DarkMode,
                            isSelected = themeMode == ThemeMode.DARK,
                            onClick = { onThemeModeChange(ThemeMode.DARK) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_btn_dark")
                        )
                        ThemeOptionPill(
                            title = if (language == AppLanguage.ODIA) "ନିଶାର୍ଦ୍ଧ" else "Night",
                            icon = Icons.Default.NightlightRound,
                            isSelected = themeMode == ThemeMode.HIGH_CONTRAST_DARK,
                            onClick = { onThemeModeChange(ThemeMode.HIGH_CONTRAST_DARK) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("theme_btn_high_contrast")
                        )
                    }
                }
            }
        }

        // Full App Watermark & Official Logo Branding Setting Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("watermark_settings_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                androidx.compose.foundation.Image(
                                    painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_app_icon_circle),
                                    contentDescription = "App Watermark Logo",
                                    modifier = Modifier
                                        .size(26.dp)
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଆପ୍ ୱାଟରମାର୍କ୍ ଓ ଲୋଗୋ" else "Full App Watermark",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଆପ୍ ନାମ ଓ ସରକାରୀ ଲୋଗୋ ସମଗ୍ର ସ୍କ୍ରିନ୍ରେ ସ୍ୱଚ୍ଛ ଭାବେ ପ୍ରଦର୍ଶିତ" else "Translucent name & logo watermark across full app",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        androidx.compose.material3.Switch(
                            checked = isWatermarkEnabled,
                            onCheckedChange = onWatermarkToggle,
                            modifier = Modifier.testTag("watermark_toggle_switch")
                        )
                    }

                    if (isWatermarkEnabled) {
                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = if (language == AppLanguage.ODIA) "ୱାଟରମାର୍କ୍ ଶୈଳୀ (Style):" else "Watermark Presentation Style:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Style selector pills
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            WatermarkOptionPill(
                                title = if (language == AppLanguage.ODIA) "କେନ୍ଦ୍ର ସିଲ୍" else "Center Seal",
                                isSelected = watermarkStyle == WatermarkStyle.CENTER_EMBLEM,
                                onClick = { onWatermarkStyleChange(WatermarkStyle.CENTER_EMBLEM) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("watermark_style_emblem")
                            )
                            WatermarkOptionPill(
                                title = if (language == AppLanguage.ODIA) "ଡାଇଗୋନାଲ୍" else "Diagonal",
                                isSelected = watermarkStyle == WatermarkStyle.DIAGONAL_TILES,
                                onClick = { onWatermarkStyleChange(WatermarkStyle.DIAGONAL_TILES) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("watermark_style_diagonal")
                            )
                            WatermarkOptionPill(
                                title = if (language == AppLanguage.ODIA) "କୋଣ ଷ୍ଟାମ୍ପ" else "Stamp",
                                isSelected = watermarkStyle == WatermarkStyle.CORNER_STAMP,
                                onClick = { onWatermarkStyleChange(WatermarkStyle.CORNER_STAMP) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("watermark_style_stamp")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = if (language == AppLanguage.ODIA) "ଦୃଶ୍ୟମାନତା ଓ ସ୍ୱଚ୍ଛତା (Opacity):" else "Visibility & Opacity:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Opacity selector pills
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            WatermarkOptionPill(
                                title = "Subtle 4.5%",
                                isSelected = watermarkOpacity == WatermarkOpacity.SUBTLE,
                                onClick = { onWatermarkOpacityChange(WatermarkOpacity.SUBTLE) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("watermark_opacity_subtle")
                            )
                            WatermarkOptionPill(
                                title = "Standard 8.5%",
                                isSelected = watermarkOpacity == WatermarkOpacity.MEDIUM,
                                onClick = { onWatermarkOpacityChange(WatermarkOpacity.MEDIUM) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("watermark_opacity_medium")
                            )
                            WatermarkOptionPill(
                                title = "Clear 14%",
                                isSelected = watermarkOpacity == WatermarkOpacity.PROMINENT,
                                onClick = { onWatermarkOpacityChange(WatermarkOpacity.PROMINENT) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("watermark_opacity_prominent")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Interactive live watermark preview box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .border(1.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                .testTag("watermark_preview_box"),
                            contentAlignment = Alignment.Center
                        ) {
                            AppWatermarkOverlay(
                                enabled = true,
                                style = watermarkStyle,
                                opacity = watermarkOpacity
                            )
                            Text(
                                text = "Preview: Active on all screens & tabs",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }

        // Google Play Store Auto-Updates Status & Control Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("play_store_auto_updates_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Autorenew,
                                    contentDescription = "Auto Update",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ଗୁଗୁଲ୍ ପ୍ଲେ ଅଟୋ-ଅପଡେଟ୍" else "Play Store Auto-Updates",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "Version ${com.example.BuildConfig.VERSION_NAME} (Build ${com.example.BuildConfig.VERSION_CODE})",
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Auto-Update: ON",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଗୁଗୁଲ୍ ପ୍ଲେ ଷ୍ଟୋର ମାଧ୍ୟମରେ ସ୍ୱୟଂକ୍ରିୟ ଭାବେ ନୂତନ ସଂସ୍କରଣ ଓ ଚାନ୍ଦିପୁର ଜୁଆର ତଥ୍ୟ ସ୍ୱୟଂ ଅପଡେଟ୍ ହୋଇଥାଏ।"
                        else
                            "Google Play In-App Updates and automatic background updates are active. The Play Store automatically updates this app over Wi-Fi when charging.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoSlate600,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dynamic State Indicator
                    when (updateState) {
                        is UpdateUIState.Checking -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = OceanBlue
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Checking Google Play Store for latest version...",
                                    style = MaterialTheme.typography.bodySmall.copy(color = BentoSlate700)
                                )
                            }
                        }
                        is UpdateUIState.UpdateAvailable -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFFEF3C7))
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = null,
                                        tint = AmberGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "New update available on Google Play (v${updateState.availableVersionCode})!",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF92400E)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onTriggerUpdate,
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlueDark),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Download & Install Update", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        is UpdateUIState.Downloading -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE0F2FE))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Downloading update from Google Play...",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = OceanBlueDark
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                if (updateState.totalBytes > 0) {
                                    val progress = updateState.bytesDownloaded.toFloat() / updateState.totalBytes.toFloat()
                                    LinearProgressIndicator(
                                        progress = { progress },
                                        modifier = Modifier.fillMaxWidth(),
                                        color = OceanBlue
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${updateState.bytesDownloaded / (1024 * 1024)} MB / ${updateState.totalBytes / (1024 * 1024)} MB",
                                        style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                                    )
                                } else {
                                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = OceanBlue)
                                }
                            }
                        }
                        is UpdateUIState.Downloaded -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFDCFCE7))
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "Update downloaded and ready to apply!",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onCompleteUpdate,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Restart App to Complete Update", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        is UpdateUIState.UpToDate -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF0FDF4))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Your app is on the latest version.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF166534)
                                    )
                                )
                            }
                        }
                        is UpdateUIState.Info -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = BentoSlate500,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = updateState.message,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = BentoSlate600,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                        else -> {}
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCheckForUpdates,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = BentoSlate700
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Check Update",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = BentoSlate700
                                )
                            )
                        }

                        Button(
                            onClick = { PlayStoreUpdateManager.openPlayStore(context) },
                            modifier = Modifier.weight(1.1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shop,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Open Play Store",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = {
                            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Balasore 360 v${com.example.BuildConfig.VERSION_NAME} App Bundle")
                                putExtra(Intent.EXTRA_TEXT, "Balasore 360 v${com.example.BuildConfig.VERSION_NAME} (Build ${com.example.BuildConfig.VERSION_CODE}) Signed Release AAB Bundle ready for Google Play Store upload: Balasore360-v${com.example.BuildConfig.VERSION_NAME}-release.aab")
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Share AAB Release Details")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("export_aab_bundle_btn"),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Export AAB Release Info (v${com.example.BuildConfig.VERSION_NAME})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }

        // Hourly Auto-Refresh & Background Data Sync Status Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("hourly_auto_refresh_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Hourly Refresh",
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.ODIA) "ପ୍ରତି ଘଣ୍ଟା ସ୍ୱୟଂକ୍ରିୟ ରିଫ୍ରେଶ୍" else "Hourly Auto-Refresh",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = if (language == AppLanguage.ODIA)
                                        "ପାଣିପାଗ, ଜରୁରୀ ସୂଚନା ଓ ଦୈନିକ ତଥ୍ୟ ପ୍ରତି ଘଣ୍ଟାରେ ସ୍ୱୟଂ ଅପଡେଟ୍ ହୁଏ"
                                    else
                                        "Auto-refreshes weather, emergency alerts & daily pulse every 60 min",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                        Switch(
                            checked = isHourlyAutoRefreshEnabled,
                            onCheckedChange = onHourlyAutoRefreshToggle,
                            modifier = Modifier.testTag("hourly_auto_refresh_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Status pill row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(lastHourlyRefreshTimestamp))
                            Text(
                                text = "Last Sync: $timeStr",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoSlate600,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            if (autoRefreshCycleCount > 0) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• Cycle #$autoRefreshCycleCount",
                                    style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (isHourlyAutoRefreshEnabled) Color(0xFFEFF6FF) else Color(0xFFF1F5F9)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isHourlyAutoRefreshEnabled) "Next in: ${nextHourlyRefreshMinutesRemaining}m" else "Paused",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isHourlyAutoRefreshEnabled) Color(0xFF1D4ED8) else BentoSlate500,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onTriggerManualHourlyRefresh,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trigger_hourly_refresh_now_btn"),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବର୍ତ୍ତମାନ ରିଫ୍ରେଶ୍ କରନ୍ତୁ" else "Refresh All Data Now (Hourly Sync)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        )
                    }
                }
            }
        }

        // Google Play Store Required In-App Privacy Policy & Compliance Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .testTag("privacy_policy_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, BentoSlate100)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Balasore 360",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900
                                )
                            )
                            Text(
                                text = "v${com.example.BuildConfig.VERSION_NAME} • Developer: Nihar Bhuyan",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = BentoSlate500
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFDCFCE7))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Play Store Verified",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Balasore 360 respects your privacy. No personal identifying information is stored or sold. We adhere strictly to Google Play Developer policies.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = BentoSlate600,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF0F9FF))
                            .border(BorderStroke(1.dp, Color(0xFFBAE6FD)), RoundedCornerShape(10.dp))
                            .clickable {
                                val privacyUrl = "https://ais-pre-honzkaxc7yscu2h42zeagl-613265325843.asia-east1.run.app/privacy"
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
                                    context.startActivity(intent)
                                } catch (_: Throwable) {}
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "View Official Privacy Policy",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OceanBlueDark
                            )
                        )
                        Text(
                            text = "Read ↗",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OceanBlue
                            )
                        )
                    }
                }
            }
        }

        // Google AdMob & app-ads.txt Setup and Verification Center
        item {
            AdMobVerificationCard(
                language = language,
                onCopyAppAdsTxt = {
                    AdMobManager.copyAppAdsTxtSnippet(context)
                },
                onShareAppAdsTxt = {
                    AdMobManager.shareAppAdsTxt(context)
                }
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
    isSubscribedToAlerts: Boolean = false,
    onToggleAlertNotifications: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .testTag("transit_card_${item.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (isSubscribedToAlerts) OceanBlue.copy(alpha = 0.5f) else BentoSlate100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                            .background(if (isSubscribedToAlerts) Color(0xFFE0F2FE) else BentoSlate100),
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                                color = if (item.status.contains("Delay", ignoreCase = true) || item.status.contains("Alert", ignoreCase = true)) Color(0xFFDC2626) else EmeraldGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    IconButton(
                        onClick = onToggleAlertNotifications,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(if (isSubscribedToAlerts) Color(0xFFDCFCE7) else BentoSlate100)
                            .testTag("transit_notify_btn_${item.id}")
                    ) {
                        Icon(
                            imageVector = if (isSubscribedToAlerts) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                            contentDescription = "Set Route Alert Push Notification",
                            tint = if (isSubscribedToAlerts) Color(0xFF16A34A) else BentoSlate500,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (isSubscribedToAlerts) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFF0FDF4))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Push alerts active for ${item.routeName} schedule updates & delays",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF15803D),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SeafoodCatchItemView(
    item: SeafoodCatch,
    language: AppLanguage
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8FAFC))
            .border(BorderStroke(1.dp, BentoSlate100), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (language == AppLanguage.ODIA) item.odiaName else item.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = BentoSlate900
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = item.qualityGrade,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534),
                                fontSize = 9.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Harbor: ${item.harbor} • ${item.freshness}",
                    style = MaterialTheme.typography.labelSmall.copy(color = BentoSlate500)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.priceRangeKg,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = OceanBlueDark
                    )
                )
                Text(
                    text = "Wholesale/kg",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = BentoSlate400,
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun ThemeOptionPill(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        modifier = modifier.height(38.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            )
        }
    }
}

@Composable
private fun WatermarkOptionPill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        modifier = modifier.height(36.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
