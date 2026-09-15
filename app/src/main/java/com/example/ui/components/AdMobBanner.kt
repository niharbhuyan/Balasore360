package com.example.ui.components

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.admob.AdMobManager
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate50
import com.example.ui.theme.BentoSlate700
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdMobBanner(
    modifier: Modifier = Modifier,
    adUnitId: String = AdMobManager.activeBannerAdUnitId
) {
    val context = LocalContext.current
    var hasInitError by remember { mutableStateOf(false) }

    if (!AdMobManager.isAvailable || hasInitError) {
        // Graceful fallback: show non-intrusive sponsor placeholder
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(BentoSlate100, RoundedCornerShape(8.dp))
                .border(1.dp, BentoSlate400.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Balasore 360 Partner Space",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = BentoSlate700
            )
        }
        return
    }

    var adViewRef by remember { mutableStateOf<AdView?>(null) }

    DisposableEffect(adUnitId) {
        onDispose {
            try {
                adViewRef?.destroy()
            } catch (_: Throwable) {}
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("admob_banner_container"),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admob_banner_view"),
            factory = { ctx ->
                try {
                    AdView(ctx).apply {
                        setAdSize(AdSize.BANNER)
                        this.adUnitId = adUnitId
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        adViewRef = this
                        val adRequest = AdRequest.Builder().build()
                        loadAd(adRequest)
                    }
                } catch (t: Throwable) {
                    hasInitError = true
                    android.view.View(ctx)
                }
            },
            update = { view ->
                if (view is AdView && view.adUnitId != adUnitId) {
                    try {
                        view.adUnitId = adUnitId
                        val adRequest = AdRequest.Builder().build()
                        view.loadAd(adRequest)
                    } catch (t: Throwable) {
                        hasInitError = true
                    }
                }
            }
        )
    }
}

@Composable
fun AdMobBannerCard(
    modifier: Modifier = Modifier,
    adUnitId: String = AdMobManager.activeBannerAdUnitId
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("admob_banner_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BentoSlate50),
        border = androidx.compose.foundation.BorderStroke(1.dp, BentoSlate100)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SPONSORED",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = BentoSlate400,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            AdMobBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                adUnitId = adUnitId
            )
        }
    }
}
