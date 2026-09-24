package com.example.ui.features.civic

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.firebase.FirestoreService
import com.example.data.model.AppLanguage
import com.example.data.model.FeedbackReport
import com.example.data.model.FeedbackType
import com.example.ui.components.FeedbackFormCard
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modal Bottom Sheet allowing citizens to submit suggestions or report issues
 * to Cloud Firestore collection 'feedback', and view recent submissions.
 */
@Composable
fun CitizenFeedbackSheet(
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val firestoreService = remember { FirestoreService(context.applicationContext) }

    var selectedTab by remember { mutableStateOf("New Submission") }
    val recentReports = remember { mutableStateListOf<FeedbackReport>() }
    var isLoadingRecent by remember { mutableStateOf(false) }

    fun refreshRecentReports() {
        isLoadingRecent = true
        coroutineScope.launch {
            val result = firestoreService.fetchFeedbackReports(limitCount = 20)
            isLoadingRecent = false
            if (result.isSuccess) {
                recentReports.clear()
                result.getOrNull()?.let { recentReports.addAll(it) }
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshRecentReports()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BentoSlate100)
            .testTag("citizen_feedback_sheet")
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F172A), OceanBlueDark, OceanBlue)
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
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF38BDF8).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "FIRESTORE SYNC • COLLECTION 'FEEDBACK'",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF7DD3FC)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ନାଗରିକ ମତାମତ ଓ ଅଭିଯୋଗ ଡେସ୍କ" else "Balasore Citizen Feedback & Reports",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଆପଣଙ୍କ ପ୍ରସ୍ତାବ ଓ ସମସ୍ୟା ରିପୋର୍ଟ ସିଧାସଳଖ କ୍ଲାଉଡ୍ ଫାୟାରଷ୍ଟୋର (Firestore) ସଂଗ୍ରହ 'feedback'ରେ ସୁରକ୍ଷିତ ଭାବେ ରହିବ।"
                        else
                            "Submit municipal reports, tourism ideas, or app suggestions saved directly to Cloud Firestore collection 'feedback'.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Tab Selector (Submit New vs Recent Submissions)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("New Submission", "Recent Submissions").forEach { tab ->
                    FilterChip(
                        selected = selectedTab == tab,
                        onClick = {
                            selectedTab = tab
                            if (tab == "Recent Submissions") refreshRecentReports()
                        },
                        label = {
                            Text(
                                text = if (tab == "New Submission")
                                    (if (language == AppLanguage.ODIA) "ନୂଆ ମତାମତ ଫର୍ମ" else "New Submission")
                                else
                                    (if (language == AppLanguage.ODIA) "ପୂର୍ବ ଦାଖଲ ସୂଚୀ (${recentReports.size})" else "Recent Submissions (${recentReports.size})"),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        if (selectedTab == "New Submission") {
            item {
                FeedbackFormCard(
                    language = language,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    onSubmittedSuccessfully = { submittedReport ->
                        recentReports.add(0, submittedReport)
                    }
                )
            }
        } else {
            // Recent Submissions List
            if (isLoadingRecent) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Fetching from Firestore 'feedback' collection...", color = BentoSlate600, fontSize = 12.sp)
                    }
                }
            } else if (recentReports.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BentoSlate200)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, tint = BentoSlate600, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (language == AppLanguage.ODIA) "ଏପର୍ଯ୍ୟନ୍ତ କୌଣସି ମତାମତ ଦାଖଲ ହୋଇନାହିଁ।" else "No feedback submitted yet in this session.",
                                color = BentoSlate800,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Be the first to submit a report or suggestion!",
                                color = BentoSlate600,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                }
            } else {
                items(recentReports) { report ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, BentoSlate200)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (report.type == FeedbackType.REPORT) Icons.Default.ReportProblem else Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = if (report.type == FeedbackType.REPORT) Color(0xFFE11D48) else OceanBlue,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = report.title,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = BentoSlate900
                                        )
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BentoSlate100,
                                    border = BorderStroke(0.5.dp, BentoSlate200)
                                ) {
                                    Text(
                                        text = report.category,
                                        color = BentoSlate700,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = report.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate700,
                                    lineHeight = 17.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = BentoSlate200)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "By ${report.userName} • ${report.locality}",
                                    fontSize = 10.5.sp,
                                    color = BentoSlate600
                                )

                                val formattedDate = remember(report.timestamp) {
                                    SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(report.timestamp))
                                }
                                Text(
                                    text = formattedDate,
                                    fontSize = 10.sp,
                                    color = BentoSlate600
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
