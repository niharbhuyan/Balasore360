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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Navigation
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.ExtendedDailyUpdateEngine
import com.example.ui.components.openInGoogleMaps

/**
 * 6. Odia Sahitya Revival Trail (Fakir Mohan Senapati & Radhanath Ray) Sheet
 * Daily literary passages, Utkal Press printing revolution, and Shanti Kanan memorial details.
 */
@Composable
fun OdiaSahityaRevivalSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pulse = remember { ExtendedDailyUpdateEngine.computeExtendedPulse().odiaSahityaPulse }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("odia_sahitya_revival_sheet")
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
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF7C3AED), Color(0xFF6D28D9))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📜", fontSize = 22.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Odia Sahitya Revival Trail",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "ବ୍ୟାସକବି ଫକୀର ମୋହନ ଓ ଶାନ୍ତି କାନନ ସାହିତ୍ୟ ଧାରା",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF7C3AED),
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Daily Literary Excerpt Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
            border = BorderStroke(1.dp, Color(0xFFE9D5FF)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF7C3AED).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "DAILY LITERARY PASSAGE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF7C3AED),
                                letterSpacing = 0.8.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = pulse.authorName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7C3AED)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = Color(0xFF7C3AED),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = pulse.literaryPassageOdia,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A),
                            lineHeight = 22.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = pulse.literaryPassageEnglish,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF475569),
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "From: ${pulse.literaryWorkTitle}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF7C3AED)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Historical Context of Utkal Press
        Text(
            text = "Balasore: Cradle of Modern Odia Literature",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "In 1868, Vyasakabi Fakir Mohan Senapati along with local patrons established the Utkal Press in Balasore, the third printing press in Odisha. It published Bodhadayini and Sambalpur Hiteisini, igniting the Odia language revival movement that successfully defeated attempts to replace Odia with Bengali in local schools.",
            style = MaterialTheme.typography.bodySmall.copy(
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Shanti Kanan Memorial Complex", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = pulse.shantiKananMuseumStatus,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.weight(1f)
            ) {
                Text("Close")
            }
            Button(
                onClick = {
                    openInGoogleMaps(
                        context = context,
                        latitude = 21.4900,
                        longitude = 86.9200,
                        label = "Shanti Kanan, Fakir Mohan Senapati Memorial, Bakharabad, Balasore"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Shanti Kanan")
            }
        }
    }
}
