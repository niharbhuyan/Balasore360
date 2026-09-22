package com.example.ui.features.heritage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.BalasoreFestivalCountdown
import com.example.data.model.TempleScheduleItem
import com.example.data.repository.SmartFeaturesRepository
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import java.util.concurrent.TimeUnit

@Composable
fun BaleswarPanjikaCalendarSheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val panjika = remember { SmartFeaturesRepository.getTodayPanjika() }
    val festivals = SmartFeaturesRepository.festivalCountdowns
    val temples = SmartFeaturesRepository.templeSchedules

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("baleswar_panjika_sheet"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF431407), Color(0xFF9A3412), Color(0xFFC2410C))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "ODIA PANJIKA & CULTURAL CLOCK",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFFED7AA),
                                    fontSize = 10.sp
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFF97316),
                            modifier = Modifier.size(10.dp)
                        ) {}
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପାଞ୍ଜି ଓ ସଂସ୍କୃତି କ୍ୟାଲେଣ୍ଡର" else "Baleswar Panjika & Festival Pulse",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଦୈନିକ ତିଥି, ନକ୍ଷତ୍ର, ଅମୃତବେଳା, ରାହୁକାଳ, ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ଓ ବୋଇତ ବନ୍ଦାଣ ଲାଇଭ୍ କାଉଣ୍ଟଡାଉନ୍।"
                        else
                            "Authentic Odia astronomical almanac, live countdowns to Chadak Mela & Boita Bandana, and temple alati schedules.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    )
                }
            }
        }

        // Today's Odia Almanac Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFFED7AA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = panjika.odiaYearMonth,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF9A3412)
                            )
                        )
                        Text(
                            text = panjika.englishDate,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PanjikaPill(
                            title = if (language == AppLanguage.ODIA) "ତିଥି" else "Tithi",
                            value = if (language == AppLanguage.ODIA) panjika.odiaTithi else panjika.tithi,
                            modifier = Modifier.weight(1f)
                        )
                        PanjikaPill(
                            title = if (language == AppLanguage.ODIA) "ନକ୍ଷତ୍ର" else "Nakshatra",
                            value = if (language == AppLanguage.ODIA) panjika.odiaNakshatra else panjika.nakshatra,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PanjikaPill(
                            title = "Surya Udaya / Astha",
                            value = "${panjika.suryaUdaya} - ${panjika.suryaAstha}",
                            modifier = Modifier.weight(1f)
                        )
                        PanjikaPill(
                            title = "Amruta Bela (ଶୁଭ ସମୟ)",
                            value = panjika.amrutaBela.substringBefore("&").trim(),
                            modifier = Modifier.weight(1f),
                            highlightColor = Color(0xFF059669)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF2F2),
                        border = BorderStroke(1.dp, Color(0xFFFECACA)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Rahu Kala: ${panjika.rahuKala}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF991B1B),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }

        // Section: Upcoming Balasore Cultural Festivals
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ପ୍ରମୁଖ ଯାତ୍ରା ଓ ମେଳା (Live Countdown)" else "Major Balasore Festivals & Melas",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Historical traditions, penance yatras and coastal celebrations",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }

        items(festivals, key = { it.id }) { fest ->
            FestivalCountdownCard(fest = fest, language = language)
        }

        // Section: Sacred Temple Alati & Prasad Schedules
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = if (language == AppLanguage.ODIA) "ପ୍ରସିଦ୍ଧ ମନ୍ଦିର ଆଳତି ଓ ପ୍ରସାଦ ସମୟ" else "Temple Darshan, Alati & Prasad Schedules",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Text(
                    text = "Remuna Khirachora, Panchalingeswar, Emami Jagannath & Bhusandheswar",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }

        items(temples, key = { it.templeName }) { temple ->
            TempleScheduleCard(temple = temple, language = language)
        }
    }
}

@Composable
private fun PanjikaPill(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    highlightColor: Color? = null
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = highlightColor ?: MaterialTheme.colorScheme.onSurface
                ),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun FestivalCountdownCard(fest: BalasoreFestivalCountdown, language: AppLanguage) {
    val diffMillis = fest.targetDateEpochMillis - System.currentTimeMillis()
    val daysRemaining = TimeUnit.MILLISECONDS.toDays(diffMillis).coerceAtLeast(0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (language == AppLanguage.ODIA) fest.odiaFestivalName else fest.festivalName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = if (language == AppLanguage.ODIA) fest.odiaLocation else fest.location,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = OceanBlue
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF7ED),
                    border = BorderStroke(1.dp, Color(0xFFFDBA74))
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$daysRemaining",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = Color(0xFFC2410C)
                        )
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଦିନ ବାକି" else "DAYS LEFT",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF9A3412)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (language == AppLanguage.ODIA) fest.odiaDescription else fest.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Key Ritual: ${fest.keyRitual}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun TempleScheduleCard(temple: TempleScheduleItem, language: AppLanguage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = if (language == AppLanguage.ODIA) temple.odiaTempleName else temple.templeName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "Presiding Deity: ${temple.deity}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF0FDF4),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Mangala Alati", fontSize = 10.sp, color = Color(0xFF166534))
                        Text(text = temple.morningAlati, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF14532D))
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFFFBEB),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Sandhya Arati", fontSize = 10.sp, color = Color(0xFF854D0E))
                        Text(text = temple.sandhyaArati, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF713F12))
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Prasad: ${temple.specialBhog}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB45309)
                )
            )
        }
    }
}
