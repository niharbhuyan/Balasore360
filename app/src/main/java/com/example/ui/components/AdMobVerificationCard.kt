package com.example.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.admob.AdMobManager
import com.example.data.model.AppLanguage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark

/**
 * Interactive AdMob & app-ads.txt Setup and Verification Card.
 * Directly addresses Google AdMob app verification requirements and resolves
 * "We couldn't verify Balasore 360 (Android)" with 1-tap copy, share, and clear steps.
 */
@Composable
fun AdMobVerificationCard(
    language: AppLanguage,
    modifier: Modifier = Modifier,
    onCopyAppAdsTxt: () -> Unit = {},
    onShareAppAdsTxt: () -> Unit = {}
) {
    val context = LocalContext.current
    var isTroubleshootingExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("admob_verification_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BentoSlate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Icon, Title & Status Badge
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
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0F2FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = "AdMob",
                            tint = OceanBlueDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) "ଗୁଗୁଲ୍ ଆଡମୋବ୍ ଓ app-ads.txt" else "Google AdMob & app-ads.txt",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = "Publisher: ${AdMobManager.PUBLISHER_ID}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = BentoSlate500,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFDCFCE7),
                    border = BorderStroke(1.dp, Color(0xFF86EFAC))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF166534),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Configured",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // IDs Overview
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "AdMob App ID:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BentoSlate600,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = AdMobManager.ADMOB_APP_ID,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BentoSlate900,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Publisher ID:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BentoSlate600,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = AdMobManager.PUBLISHER_ID,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BentoSlate900,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // app-ads.txt snippet section
            Text(
                text = if (language == AppLanguage.ODIA) "ଅଫିସିଆଲ୍ app-ads.txt ରେକର୍ଡ:" else "Required app-ads.txt Record:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = BentoSlate700
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Code Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = AdMobManager.APP_ADS_TXT_RECORD,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF38BDF8),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons: Copy & Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val copied = AdMobManager.copyAppAdsTxtSnippet(context)
                        if (copied) {
                            Toast.makeText(context, "app-ads.txt record copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                        onCopyAppAdsTxt()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_copy_app_ads_txt"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBlue)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Copy Snippet",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                OutlinedButton(
                    onClick = {
                        AdMobManager.shareAppAdsTxt(context)
                        onShareAppAdsTxt()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_share_app_ads_txt"),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BentoSlate200)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = BentoSlate700
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Share Record",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = BentoSlate700
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Troubleshooter Accordion for "We couldn't verify Balasore 360 (Android)"
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFEF3C7).copy(alpha = 0.5f),
                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isTroubleshootingExpanded = !isTroubleshootingExpanded }
                    .testTag("admob_troubleshoot_header")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA)
                                    "AdMob Verification Fix (ଆଡମୋବ୍ ଯାଞ୍ଚ ସମାଧାନ)"
                                else
                                    "Fix 'We couldn't verify Balasore 360'",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            )
                        }
                        Icon(
                            imageVector = if (isTroubleshootingExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isTroubleshootingExpanded) "Collapse" else "Expand",
                            tint = Color(0xFFB45309),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    AnimatedVisibility(
                        visible = isTroubleshootingExpanded,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = "Why AdMob shows this warning:\n" +
                                        "Google AdMob verifies your app by reading your developer website listed in Google Play Console. If the website is missing or does not have app-ads.txt at its root, verification fails.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF78350F),
                                    lineHeight = 18.sp,
                                    fontSize = 11.5.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "How to complete verification in 4 steps:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            StepItem(
                                stepNumber = "1",
                                text = "Check Play Console Website: In Google Play Console, go to Store presence → Store settings → Developer contact details → Website. Note the exact domain (e.g. yoursite.com)."
                            )
                            StepItem(
                                stepNumber = "2",
                                text = "Upload app-ads.txt to Root: Publish a text file at https://<your-domain>/app-ads.txt containing: google.com, pub-4880243637225183, DIRECT, f08c47fec0942fa0"
                            )
                            StepItem(
                                stepNumber = "3",
                                text = "Test in Browser: Open https://<your-domain>/app-ads.txt in Chrome or browser. Ensure the exact text appears without HTML tags or redirects."
                            )
                            StepItem(
                                stepNumber = "4",
                                text = "Check for Updates: In Google AdMob, go back to Verify app and click 'Check for updates'. AdMob's crawler will verify your app within 1-2 hours."
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItem(
    stepNumber: String,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF92400E),
            modifier = Modifier.size(16.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stepNumber,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF78350F),
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        )
    }
}
