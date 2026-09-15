package com.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Emergency
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.admob.AdMobManager
import com.example.data.model.AppLanguage
import com.example.ui.screens.EssentialsScreen
import com.example.ui.screens.NewsScreen
import com.example.ui.screens.TourismScreen
import com.example.ui.screens.WeatherScreen
import com.example.ui.theme.Balasore360Theme
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate50
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.example.ui.viewmodel.BalasoreViewModel

import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.example.util.PlayStoreUpdateManager
import com.example.util.UpdateUIState

class MainActivity : ComponentActivity() {

    private val viewModel: BalasoreViewModel by viewModels()
    private lateinit var updateManager: PlayStoreUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        updateManager = PlayStoreUpdateManager(this)

        // Crash-proof AdMob initialization
        try {
            AdMobManager.initialize(applicationContext)
        } catch (t: Throwable) {
            Log.e("MainActivity", "Safe AdMob init catch: ${t.message}")
        }

        setContent {
            Balasore360Theme {
                BalasoreApp(
                    viewModel = viewModel,
                    updateManager = updateManager
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (::updateManager.isInitialized) {
            updateManager.checkPendingUpdateCompletion()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::updateManager.isInitialized) {
            updateManager.cleanup()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreApp(
    viewModel: BalasoreViewModel,
    updateManager: PlayStoreUpdateManager
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateState by updateManager.updateState.collectAsState()

    // Activity Result Launcher for Google Play In-App Updates
    val updateLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            Log.w("BalasoreApp", "Update flow failed or cancelled: ${result.resultCode}")
        }
    }

    // Auto-check for updates on app launch
    LaunchedEffect(Unit) {
        updateManager.checkForUpdates(launcher = updateLauncher, autoStartFlexible = true)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("balasore_main_scaffold"),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Balasore 360",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp,
                                    color = OceanBlueDark
                                )
                            )
                            Text(
                                text = "ବାଲେଶ୍ୱର • ଚାନ୍ଦିପୁର ସ୍ପନ୍ଦନ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = BentoSlate500
                                )
                            )
                        }

                        // Language Toggle Pills
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(BentoSlate100)
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            LanguagePill(
                                title = "EN",
                                isSelected = uiState.language == AppLanguage.ENGLISH,
                                onClick = { viewModel.setLanguage(AppLanguage.ENGLISH) }
                            )
                            LanguagePill(
                                title = "ଓଡ଼ି",
                                isSelected = uiState.language == AppLanguage.ODIA,
                                onClick = { viewModel.setLanguage(AppLanguage.ODIA) }
                            )
                            LanguagePill(
                                title = "HI",
                                isSelected = uiState.language == AppLanguage.HINDI,
                                onClick = { viewModel.setLanguage(AppLanguage.HINDI) }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("balasore_bottom_nav"),
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 0) Icons.Default.Explore else Icons.Outlined.Explore,
                            contentDescription = "Hotspots"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ସ୍ଥାନ" else "Explore",
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OceanBlue,
                        selectedTextColor = OceanBlue,
                        indicatorColor = BentoSlate100,
                        unselectedIconColor = BentoSlate400,
                        unselectedTextColor = BentoSlate400
                    )
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 1) Icons.AutoMirrored.Filled.Article else Icons.AutoMirrored.Outlined.Article,
                            contentDescription = "News"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ଖବର" else "News",
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OceanBlue,
                        selectedTextColor = OceanBlue,
                        indicatorColor = BentoSlate100,
                        unselectedIconColor = BentoSlate400,
                        unselectedTextColor = BentoSlate400
                    )
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 2,
                    onClick = { viewModel.selectTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 2) Icons.Default.Cloud else Icons.Outlined.Cloud,
                            contentDescription = "Weather"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ପାଣିପାଗ" else "Weather",
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OceanBlue,
                        selectedTextColor = OceanBlue,
                        indicatorColor = BentoSlate100,
                        unselectedIconColor = BentoSlate400,
                        unselectedTextColor = BentoSlate400
                    )
                )

                NavigationBarItem(
                    selected = uiState.selectedTab == 3,
                    onClick = { viewModel.selectTab(3) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 3) Icons.Default.Emergency else Icons.Outlined.Emergency,
                            contentDescription = "Essentials"
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.language == AppLanguage.ODIA) "ସହାୟତା" else "Essentials",
                            fontSize = 11.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OceanBlue,
                        selectedTextColor = OceanBlue,
                        indicatorColor = BentoSlate100,
                        unselectedIconColor = BentoSlate400,
                        unselectedTextColor = BentoSlate400
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Prominent banner when flexible background update has finished downloading
            if (updateState is UpdateUIState.Downloaded) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = null,
                                tint = Color(0xFF166534),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Update Ready to Install",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF166534)
                                    )
                                )
                                Text(
                                    text = "Restart app to apply the newest features.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF15803D),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                        Button(
                            onClick = { updateManager.completeUpdate() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF166534)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Restart", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = BentoSlate50
            ) {
                when (uiState.selectedTab) {
                    0 -> TourismScreen(
                        hotspots = uiState.hotspots,
                        templeRitualInfo = uiState.templeRitualInfo,
                        literaryTrailPoints = uiState.literaryTrailPoints,
                        dailyIdiom = uiState.dailyIdiom,
                        selectedCategory = uiState.selectedHotspotCategory,
                        searchQuery = uiState.hotspotSearchQuery,
                        language = uiState.language,
                        favoriteIds = uiState.favoriteHotspotIds,
                        onCategorySelected = { viewModel.setHotspotCategory(it) },
                        onSearchChanged = { viewModel.setHotspotSearchQuery(it) },
                        onToggleFavorite = { viewModel.toggleFavoriteHotspot(it) }
                    )
                    1 -> NewsScreen(
                        articles = uiState.newsArticles,
                        riverGauges = uiState.riverGauges,
                        cycloneShelters = uiState.cycloneShelters,
                        selectedCategory = uiState.selectedNewsCategory,
                        language = uiState.language,
                        bookmarkedIds = uiState.bookmarkedIds,
                        isRefreshing = uiState.isRefreshing,
                        onCategorySelected = { viewModel.setNewsCategory(it) },
                        onToggleBookmark = { viewModel.toggleBookmark(it) },
                        onRefresh = { viewModel.refreshAll() }
                    )
                    2 -> WeatherScreen(
                        weather = uiState.weather,
                        tidalClock = uiState.tidalClock,
                        drdoAdvisories = uiState.drdoAdvisories,
                        language = uiState.language
                    )
                    3 -> EssentialsScreen(
                        emergencyContacts = uiState.emergencyContacts,
                        transitList = uiState.transitList,
                        seafoodCatches = uiState.seafoodCatches,
                        language = uiState.language,
                        updateState = updateState,
                        onCheckForUpdates = { updateManager.checkForUpdates(updateLauncher, autoStartFlexible = false) },
                        onTriggerUpdate = { updateManager.startUpdate(updateLauncher) },
                        onCompleteUpdate = { updateManager.completeUpdate() }
                    )
                }
            }
        }
    }
}

@Composable
fun LanguagePill(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) OceanBlue else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else BentoSlate800,
                fontSize = 10.sp
            )
        )
    }
}
