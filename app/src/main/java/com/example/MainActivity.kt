package com.example

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

class MainActivity : ComponentActivity() {

    private val viewModel: BalasoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Crash-proof AdMob initialization
        try {
            AdMobManager.initialize(applicationContext)
        } catch (t: Throwable) {
            Log.e("MainActivity", "Safe AdMob init catch: ${t.message}")
        }

        setContent {
            Balasore360Theme {
                BalasoreApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalasoreApp(viewModel: BalasoreViewModel) {
    val uiState by viewModel.uiState.collectAsState()

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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = BentoSlate50
        ) {
            when (uiState.selectedTab) {
                0 -> TourismScreen(
                    hotspots = uiState.hotspots,
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
                    language = uiState.language
                )
                3 -> EssentialsScreen(
                    emergencyContacts = uiState.emergencyContacts,
                    transitList = uiState.transitList,
                    language = uiState.language
                )
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
