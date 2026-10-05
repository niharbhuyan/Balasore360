package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.material3.Button
import com.example.ui.components.BalasoreDistrictVectorMap
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.util.Locale
import com.example.data.maps.BalasoreOfflineMapManager
import com.example.data.maps.MapCacheManager
import com.example.data.maps.OfflineNavRoute
import com.example.data.maps.OfflineTurnStep
import com.example.data.maps.BalasoreOfflineTileProvider
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.TileOverlay
import com.google.android.gms.maps.model.TileOverlayOptions
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.data.local.HotspotEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Hotspot
import com.example.data.repository.DefaultData
import com.example.ui.theme.AmberGold
import com.example.ui.theme.BentoCardWhite
import com.example.ui.theme.BentoPrimaryBlue
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate700
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Unified data representation for Google Maps markers in Balasore.
 * Represents either a Tourist Hotspot or a Cyclone Shelter.
 */
sealed class BalasoreMapItem {
    abstract val id: String
    abstract val name: String
    abstract val odiaName: String
    abstract val latitude: Double
    abstract val longitude: Double
    abstract val distanceKm: Double
    abstract val isShelter: Boolean

    data class TouristSpot(
        override val id: String,
        override val name: String,
        override val odiaName: String,
        val category: String,
        val shortDesc: String,
        val timings: String,
        val highlights: String,
        override val latitude: Double,
        override val longitude: Double,
        override val distanceKm: Double,
        val isOpenNow: Boolean = true,
        val tideStatus: String = "Low Tide - Safe to Walk",
        val visitorFlow: String = "Moderate"
    ) : BalasoreMapItem() {
        override val isShelter: Boolean get() = false
    }

    data class CycloneShelterSpot(
        override val id: String,
        override val name: String,
        override val odiaName: String,
        val block: String,
        val capacity: Int,
        val phone: String,
        val locationDesc: String,
        val elevationMeters: Double,
        val hasSolarBackup: Boolean,
        override val latitude: Double,
        override val longitude: Double,
        override val distanceKm: Double,
        val occupancyPercent: Int = 18,
        val operationalStatus: String = "OPERATIONAL_STANDBY",
        val solarBatteryLevel: Int = 98
    ) : BalasoreMapItem() {
        override val isShelter: Boolean get() = true
    }
}

/**
 * Pre-defined coordinates and attributes for Cyclone Shelters in Balasore district.
 * Stored locally and pre-cached for 100% offline access.
 */
val defaultCycloneSheltersMapData = listOf(
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_1",
        name = "Chandipur Coastal Multipurpose Shelter",
        odiaName = "ଚାନ୍ଦିପୁର ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Chandipur Coastal",
        capacity = 1800,
        phone = "06782262234",
        locationDesc = "Near Marine Police Station, Chandipur",
        elevationMeters = 9.2,
        hasSolarBackup = true,
        latitude = 21.4682,
        longitude = 87.0163,
        distanceKm = 1.2,
        occupancyPercent = 14,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 98
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_2",
        name = "Bhograi Coastal Disaster Shelter",
        odiaName = "ଭୋଗରାଇ ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Bhograi",
        capacity = 2400,
        phone = "06781238210",
        locationDesc = "Talasari Beach Embankment, Bhograi",
        elevationMeters = 9.5,
        hasSolarBackup = true,
        latitude = 21.5892,
        longitude = 87.4582,
        distanceKm = 48.0,
        occupancyPercent = 22,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 95
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_3",
        name = "Remuna High School Disaster Shelter",
        odiaName = "ରେମୁଣା ଉଚ୍ଚ ବିଦ୍ୟାଳୟ ଆଶ୍ରୟ କେନ୍ଦ୍ର",
        block = "Remuna",
        capacity = 1400,
        phone = "06782252115",
        locationDesc = "Near Khirachora Gopinath Temple, Remuna",
        elevationMeters = 8.0,
        hasSolarBackup = true,
        latitude = 21.5284,
        longitude = 86.8647,
        distanceKm = 9.5,
        occupancyPercent = 18,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 97
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_4",
        name = "Kasafal Fishermen Disaster Safe Haven",
        odiaName = "କସାଫଳ ମତ୍ସ୍ୟଜୀବୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Chandipur / Sadar",
        capacity = 1600,
        phone = "06782260100",
        locationDesc = "Kasafal Sea Mouth, Balasore",
        elevationMeters = 8.8,
        hasSolarBackup = true,
        latitude = 21.5200,
        longitude = 87.1100,
        distanceKm = 14.2,
        occupancyPercent = 25,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 93
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_5",
        name = "Bahanaga Relief Multipurpose Center",
        odiaName = "ବାହାନଗା ବିପର୍ଯ୍ୟୟ ରିଲିଫ କେନ୍ଦ୍ର",
        block = "Bahanaga",
        capacity = 1500,
        phone = "06782275420",
        locationDesc = "Bahanaga Bazar, NH-16",
        elevationMeters = 7.5,
        hasSolarBackup = true,
        latitude = 21.3150,
        longitude = 86.8050,
        distanceKm = 24.5,
        occupancyPercent = 12,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 99
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_6",
        name = "Baliapal Subarnarekha Basin Shelter",
        odiaName = "ବାଲିଆପାଳ ସୁବର୍ଣ୍ଣରେଖା ଅବବାହିକା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Baliapal",
        capacity = 2100,
        phone = "06781254300",
        locationDesc = "Chaumukh River Embankment, Baliapal",
        elevationMeters = 9.0,
        hasSolarBackup = true,
        latitude = 21.6420,
        longitude = 87.2890,
        distanceKm = 36.8,
        occupancyPercent = 30,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 96
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_7",
        name = "Soro High School Elevated Cyclone Shelter",
        odiaName = "ସୋରୋ ଉଚ୍ଚ ବିଦ୍ୟାଳୟ ବାତ୍ୟା ଆଶ୍ରୟ କେନ୍ଦ୍ର",
        block = "Soro",
        capacity = 1300,
        phone = "06782281200",
        locationDesc = "Soro Town, Balasore",
        elevationMeters = 7.8,
        hasSolarBackup = true,
        latitude = 21.2850,
        longitude = 86.6920,
        distanceKm = 38.0,
        occupancyPercent = 10,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 98
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_8",
        name = "Nilagiri Hill-Slope Disaster Relief Center",
        odiaName = "ନୀଳଗିରି ପାହାଡ଼ ତଳ ବିପର୍ଯ୍ୟୟ କେନ୍ଦ୍ର",
        block = "Nilagiri",
        capacity = 1200,
        phone = "06782233215",
        locationDesc = "Near Nilagiri Palace, Nilagiri",
        elevationMeters = 18.5,
        hasSolarBackup = true,
        latitude = 21.4550,
        longitude = 86.7620,
        distanceKm = 22.0,
        occupancyPercent = 15,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 100
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_9",
        name = "Jaleswar Subarnarekha Multi-purpose Shelter",
        odiaName = "ଜଳେଶ୍ୱର ସୁବର୍ଣ୍ଣରେଖା ବହୁମୁଖୀ ଆଶ୍ରୟସ୍ଥଳ",
        block = "Jaleswar",
        capacity = 1700,
        phone = "06781222400",
        locationDesc = "Station Road, Jaleswar",
        elevationMeters = 8.2,
        hasSolarBackup = true,
        latitude = 21.8020,
        longitude = 87.2150,
        distanceKm = 52.0,
        occupancyPercent = 16,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 97
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_10",
        name = "Simulia Canal Embankment Relief Shelter",
        odiaName = "ସିମୁଳିଆ କେନାଲ ବନ୍ଧ ରିଲିଫ ଆଶ୍ରୟସ୍ଥଳ",
        block = "Simulia",
        capacity = 1100,
        phone = "06782294100",
        locationDesc = "Simulia Block Headquarters",
        elevationMeters = 7.2,
        hasSolarBackup = false,
        latitude = 21.1550,
        longitude = 86.5820,
        distanceKm = 46.0,
        occupancyPercent = 8,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 90
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_11",
        name = "Basta Subarnarekha Basin Flood Relief Center",
        odiaName = "ବସ୍ତା ସୁବର୍ଣ୍ଣରେଖା ବନ୍ୟା ଓ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ",
        block = "Basta",
        capacity = 1650,
        phone = "06781251200",
        locationDesc = "Rajghat Embankment, Basta",
        elevationMeters = 8.4,
        hasSolarBackup = true,
        latitude = 21.6850,
        longitude = 87.0550,
        distanceKm = 32.0,
        occupancyPercent = 20,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 96
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_12",
        name = "Balaramgadi Port Estuary Evacuation Safe Haven",
        odiaName = "ବଳରାମଗଡ଼ି ମୁହାଣ ଉଦ୍ଧାର ଓ ବାତ୍ୟା କେନ୍ଦ୍ର",
        block = "Chandipur / Marine",
        capacity = 1750,
        phone = "06782272210",
        locationDesc = "Near Trawler Fish Landing Jetty, Balaramgadi",
        elevationMeters = 8.6,
        hasSolarBackup = true,
        latitude = 21.4880,
        longitude = 87.0480,
        distanceKm = 12.0,
        occupancyPercent = 28,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 95
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_13",
        name = "Singla Coastal Multipurpose Evacuation Center",
        odiaName = "ସିଙ୍ଗଲା ଉପକୂଳ ବହୁମୁଖୀ ଆଶ୍ରୟସ୍ଥଳ",
        block = "Baliapal / Singla",
        capacity = 1550,
        phone = "06781258100",
        locationDesc = "Singla Gram Panchayat, Baliapal",
        elevationMeters = 8.9,
        hasSolarBackup = true,
        latitude = 21.6500,
        longitude = 87.2780,
        distanceKm = 39.5,
        occupancyPercent = 14,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 97
    ),
    BalasoreMapItem.CycloneShelterSpot(
        id = "cs_14",
        name = "Khantapada NH-16 Highway Disaster Shelter",
        odiaName = "ଖନ୍ତାପଡ଼ା ରାଜପଥ ବିପର୍ଯ୍ୟୟ ଆଶ୍ରୟସ୍ଥଳ",
        block = "Khantapada",
        capacity = 1400,
        phone = "06782277100",
        locationDesc = "Near Khantapada Bypass, NH-16",
        elevationMeters = 7.9,
        hasSolarBackup = true,
        latitude = 21.4120,
        longitude = 86.8450,
        distanceKm = 18.0,
        occupancyPercent = 11,
        operationalStatus = "OPERATIONAL_STANDBY",
        solarBatteryLevel = 99
    )
)

/**
 * Builds Tourist Hotspot map items with rich pre-cached details.
 */
fun buildTouristSpotsMapData(): List<BalasoreMapItem.TouristSpot> {
    return listOf(
        BalasoreMapItem.TouristSpot(
            id = "spot_chandipur",
            name = "Chandipur Vanishing Sea Beach",
            odiaName = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
            category = "Beach & Maritime",
            shortDesc = "World-famous vanishing sea that recedes up to 5 km during low tide.",
            timings = "Open 24x7 • Best at Ebb Tide",
            highlights = "Red Ghost Crabs, Horseshoe Crabs, DRDO ITR testing horizon",
            latitude = 21.4705,
            longitude = 87.0135,
            distanceKm = 16.0,
            isOpenNow = true,
            tideStatus = "Low Tide - Safe to Walk 5km",
            visitorFlow = "Moderate"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_panchalingeswar",
            name = "Panchalingeswar Perennial Stream Shrine",
            odiaName = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପୀଠ",
            category = "Temple & Nature",
            shortDesc = "Five Shiva Lingas permanently submerged beneath a cool perennial mountain spring.",
            timings = "06:00 AM - 08:30 PM",
            highlights = "Devagiri Hill trekking, waterfall darshan, panoramic valley views",
            latitude = 21.4285,
            longitude = 86.7125,
            distanceKm = 30.0,
            isOpenNow = true,
            tideStatus = "Inland Stream Active",
            visitorFlow = "High"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_remuna",
            name = "Khirachora Gopinath Temple Remuna",
            odiaName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର",
            category = "Temple & Heritage",
            shortDesc = "Historic 10th-century shrine famous for the sacred Amruta Keli condensed milk prasad.",
            timings = "05:30 AM - 01:00 PM, 04:00 PM - 09:00 PM",
            highlights = "Amruta Keli Bhog, Utkal architecture, Gaudiya Vaishnavite heritage",
            latitude = 21.5285,
            longitude = 86.8715,
            distanceKm = 9.0,
            isOpenNow = true,
            tideStatus = "Inland Shrine",
            visitorFlow = "High"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_kuldiha",
            name = "Kuldiha Wildlife Sanctuary & Elephant Reserve",
            odiaName = "କୁଲଡ଼ିହା ବନ୍ୟପ୍ରାଣୀ ଅଭୟାରଣ୍ୟ",
            category = "Nature & Safari",
            shortDesc = "Pristine Sal forests home to wild Asian elephants, leopards, and giant squirrels.",
            timings = "06:00 AM - 05:00 PM",
            highlights = "Elephant corridor, Gohirabhola salt lick watchtower, Rissia Dam",
            latitude = 21.3980,
            longitude = 86.7450,
            distanceKm = 35.0,
            isOpenNow = true,
            tideStatus = "Inland Reserve",
            visitorFlow = "Eco-Permit Active"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_talasari",
            name = "Talasari Beach & Red Ghost Crab Dunes",
            odiaName = "ତାଳସାରୀ ବେଳାଭୂମି",
            category = "Beach & Eco-Tourism",
            shortDesc = "Tranquil casuarina-lined beach with colonies of vibrant red ghost crabs.",
            timings = "Open 24x7",
            highlights = "Subarnarekha river mouth, Udaipur border, red ghost crab walks",
            latitude = 21.6060,
            longitude = 87.4520,
            distanceKm = 88.0,
            isOpenNow = true,
            tideStatus = "Low Tide - Safe to Walk",
            visitorFlow = "Moderate"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_bichitrapur",
            name = "Bichitrapur Mangrove Nature Safari",
            odiaName = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ସଫାରୀ",
            category = "Nature & Mangrove",
            shortDesc = "Unique tidal mangrove wetland ecosystem with guided boat safaris.",
            timings = "07:30 AM - 05:00 PM",
            highlights = "Tidal delta boat safari, migratory bird sanctuary, coastal flora",
            latitude = 21.6150,
            longitude = 87.4720,
            distanceKm = 92.0,
            isOpenNow = true,
            tideStatus = "High Tide Required for Boats",
            visitorFlow = "Moderate"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_emami_jagannath",
            name = "Emami Jagannath Temple Januganj",
            odiaName = "ଏମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର",
            category = "Temple & Architecture",
            shortDesc = "Magnificent modern Kalinga-style stone temple with intricate hand-carved friezes.",
            timings = "06:00 AM - 12:30 PM, 04:30 PM - 09:00 PM",
            highlights = "Kalinga stone architecture, temple gardens, Sandhya Alati",
            latitude = 21.5050,
            longitude = 86.8980,
            distanceKm = 5.0,
            isOpenNow = true,
            tideStatus = "Inland Shrine",
            visitorFlow = "Moderate"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_raibania",
            name = "Historic Raibania Medieval Fort Ruins",
            odiaName = "ପ୍ରାଚୀନ ରାଇବଣିଆ ଦୁର୍ଗ",
            category = "Heritage & History",
            shortDesc = "Largest medieval defensive fortress complex in eastern India, built by King Langula Narasingha Deva.",
            timings = "08:00 AM - 06:00 PM",
            highlights = "Laterite stone ramparts, historic moat, archaeological excavation",
            latitude = 21.9120,
            longitude = 86.9450,
            distanceKm = 72.0,
            isOpenNow = true,
            tideStatus = "Inland Fort",
            visitorFlow = "Light"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_balaramgadi",
            name = "Balaramgadi Fishing Port & Estuary",
            odiaName = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ଓ ମୁହାଣ",
            category = "Beach & Port",
            shortDesc = "Picturesque river estuary where Budhabalanga joins the Bay of Bengal.",
            timings = "Open 24x7 • Morning Catch Auction 05:00 AM - 09:00 AM",
            highlights = "Fresh fish auction, deep-sea trawler fleet, sunset estuary photography",
            latitude = 21.4880,
            longitude = 87.0480,
            distanceKm = 18.0,
            isOpenNow = true,
            tideStatus = "Ebb Tide Current",
            visitorFlow = "Active"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_bhujanakhia",
            name = "Hazrat Bhujanakhia Pir Dargah Asthana",
            odiaName = "ଭୁଜାଖିଆ ପୀର ଦରଗାହ",
            category = "Heritage & Spiritual",
            shortDesc = "Centuries-old syncretic Sufi shrine revered by people of all faiths in Balasore town.",
            timings = "06:00 AM - 09:00 PM",
            highlights = "Sacred chuda-bhuga tradition, communal harmony landmark",
            latitude = 21.4910,
            longitude = 86.9280,
            distanceKm = 1.0,
            isOpenNow = true,
            tideStatus = "Town Core",
            visitorFlow = "Moderate"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_chandaneswar",
            name = "Baba Chandaneswar Shiva Temple",
            odiaName = "ଚନ୍ଦନେଶ୍ୱର ଶିବ ମନ୍ଦିର",
            category = "Temple & Heritage",
            shortDesc = "Celebrated pilgrimage center famous for the annual Chadak Mela and Utkal spiritual lore.",
            timings = "05:00 AM - 09:30 PM",
            highlights = "Chadak Mela, sacred pond, Odisha-Bengal border spiritual hub",
            latitude = 21.6210,
            longitude = 87.4760,
            distanceKm = 90.0,
            isOpenNow = true,
            tideStatus = "Inland Shrine",
            visitorFlow = "High"
        ),
        BalasoreMapItem.TouristSpot(
            id = "spot_kasafal_beach",
            name = "Kasafal Beach & Sea Mouth",
            odiaName = "କସାଫଳ ବେଳାଭୂମି",
            category = "Beach & Marine",
            shortDesc = "Untouched serene beach known for coastal fishing, dunes, and migratory sea birds.",
            timings = "Open 24x7",
            highlights = "Isolated sand spits, coastal fishing boats, peaceful seascape",
            latitude = 21.5340,
            longitude = 87.1210,
            distanceKm = 24.0,
            isOpenNow = true,
            tideStatus = "Low Tide Safe",
            visitorFlow = "Light"
        )
    )
}

/**
 * Google Maps view integrating real-time markers for cyclone shelters and major tourist hotspots in Balasore.
 * Includes a smooth camera zoom animation to center the view whenever a marker or card is clicked.
 */
@Composable
fun HotspotsMapView(
    hotspots: List<HotspotEntity> = emptyList(),
    selectedHotspot: HotspotEntity? = null,
    onSelectHotspot: (HotspotEntity?) -> Unit = {},
    onViewHotspotDetails: (HotspotEntity) -> Unit = {},
    language: AppLanguage = AppLanguage.ENGLISH,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Network connectivity initial check
    val isInitiallyOnline = remember {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
            val net = cm?.activeNetwork
            val caps = if (net != null) cm.getNetworkCapabilities(net) else null
            caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } catch (_: Exception) {
            false
        }
    }

    // Dynamic telemetry state for shelters and tourist spots
    var currentTouristSpots by remember { mutableStateOf(buildTouristSpotsMapData()) }
    var currentCycloneShelters by remember { mutableStateOf(defaultCycloneSheltersMapData) }

    // Auto-update controls state
    var isAutoUpdateEnabled by remember { mutableStateOf(true) }
    var autoUpdateIntervalSeconds by remember { mutableIntStateOf(30) }
    var secondsRemaining by remember { mutableIntStateOf(30) }
    var updateCycleCount by remember { mutableIntStateOf(1) }
    var isSyncing by remember { mutableStateOf(false) }

    // Offline mode & TileOverlay state
    var isOfflineModeActive by remember { mutableStateOf(!isInitiallyOnline) }
    var offlineTileOverlay by remember { mutableStateOf<TileOverlay?>(null) }
    var showAutoUpdatePanel by remember { mutableStateOf(false) }

    // MapCacheManager service for pre-fetching and caching landmark tiles
    val mapCacheManager = remember { MapCacheManager.getInstance(context) }
    val cacheStatusMap by mapCacheManager.cacheStatusFlow.collectAsState()
    val isPrefetchingAll by mapCacheManager.isPrefetching.collectAsState()
    val prefetchProgress by mapCacheManager.overallProgress.collectAsState()
    val currentPrefetchTask by mapCacheManager.currentTaskName.collectAsState()
    val cacheSummary = remember(cacheStatusMap, isPrefetchingAll, prefetchProgress) {
        mapCacheManager.getSummaryStatistics()
    }

    // Offline in-app turn-by-turn routing state
    var activeNavRoute by remember { mutableStateOf<OfflineNavRoute?>(null) }
    var currentNavStepIndex by remember { mutableIntStateOf(0) }
    var isSimulatingDrive by remember { mutableStateOf(false) }
    var routePolylineInstance by remember { mutableStateOf<Polyline?>(null) }
    var startNavMarkerInstance by remember { mutableStateOf<Marker?>(null) }
    var destNavMarkerInstance by remember { mutableStateOf<Marker?>(null) }
    var simulatedNavMarkerInstance by remember { mutableStateOf<Marker?>(null) }

    var selectedCategory by remember { mutableStateOf("All") }
    var selectedItem by remember { mutableStateOf<BalasoreMapItem?>(null) }
    var googleMapInstance by remember { mutableStateOf<GoogleMap?>(null) }
    var currentMapType by remember { mutableStateOf(GoogleMap.MAP_TYPE_NORMAL) }

    // Map marker tracking
    val markerToItemMap = remember { mutableStateMapOf<Marker, BalasoreMapItem>() }

    /**
     * Starts offline navigation routing to a cyclone shelter or tourist spot.
     * Uses pre-cached local coordinates and road networks without requiring internet.
     */
    fun startOfflineNavigation(item: BalasoreMapItem) {
        val origin = LatLng(21.4934, 86.9324) // Balasore Central Station & Transit Core
        val route = BalasoreOfflineMapManager.getInstance(context).calculateOfflineRouteToDestination(
            origin = origin,
            dest = LatLng(item.latitude, item.longitude),
            nameEn = item.name,
            nameOr = item.odiaName,
            isShelter = item.isShelter
        )
        activeNavRoute = route
        currentNavStepIndex = 0
        isSimulatingDrive = false

        // Force enable offline tile overlay so the cached local tiles are prominently active
        isOfflineModeActive = true

        googleMapInstance?.let { map ->
            routePolylineInstance?.remove()
            startNavMarkerInstance?.remove()
            destNavMarkerInstance?.remove()
            simulatedNavMarkerInstance?.remove()

            routePolylineInstance = map.addPolyline(
                PolylineOptions()
                    .addAll(route.pathPoints)
                    .color(android.graphics.Color.rgb(6, 182, 212)) // Cyan
                    .width(12f)
                    .geodesic(true)
            )

            startNavMarkerInstance = map.addMarker(
                MarkerOptions()
                    .position(route.origin)
                    .title("Your Origin (Balasore Central)")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            destNavMarkerInstance = map.addMarker(
                MarkerOptions()
                    .position(route.destination)
                    .title(item.name)
                    .snippet(if (item.isShelter) "Safe Cyclone Shelter" else "Tourist Destination")
                    .icon(BitmapDescriptorFactory.defaultMarker(if (item.isShelter) BitmapDescriptorFactory.HUE_ORANGE else BitmapDescriptorFactory.HUE_RED))
            )

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(route.destination, 13.2f), 1000, null)
        }
        Toast.makeText(context, "Offline Turn-by-Turn Route Active • Using Pre-cached Tiles", Toast.LENGTH_SHORT).show()
    }

    /**
     * Exits active offline navigation and restores standard map view.
     */
    fun stopOfflineNavigation() {
        routePolylineInstance?.remove()
        routePolylineInstance = null
        startNavMarkerInstance?.remove()
        startNavMarkerInstance = null
        destNavMarkerInstance?.remove()
        destNavMarkerInstance = null
        simulatedNavMarkerInstance?.remove()
        simulatedNavMarkerInstance = null
        activeNavRoute = null
        isSimulatingDrive = false
        currentNavStepIndex = 0
        googleMapInstance?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(21.4934, 86.9135), 11.2f), 800, null)
    }

    // Simulated vehicle progression loop along offline route
    LaunchedEffect(isSimulatingDrive, activeNavRoute) {
        val route = activeNavRoute ?: return@LaunchedEffect
        if (!isSimulatingDrive) return@LaunchedEffect
        val map = googleMapInstance ?: return@LaunchedEffect

        for ((idx, point) in route.pathPoints.withIndex()) {
            if (!isSimulatingDrive) break
            simulatedNavMarkerInstance?.remove()
            simulatedNavMarkerInstance = map.addMarker(
                MarkerOptions()
                    .position(point)
                    .title("Vehicle Position (Offline)")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            )
            currentNavStepIndex = (idx * route.steps.size / route.pathPoints.size).coerceIn(0, route.steps.lastIndex)
            map.animateCamera(CameraUpdateFactory.newLatLng(point), 800, null)
            delay(1600L)
        }
        isSimulatingDrive = false
    }

    // Dynamic auto-update execution
    fun triggerAutoUpdate() {
        updateCycleCount++
        currentCycloneShelters = currentCycloneShelters.mapIndexed { idx, shelter ->
            val deltaOcc = when ((idx + updateCycleCount) % 4) {
                0 -> 1
                1 -> -1
                2 -> 2
                else -> 0
            }
            val newOcc = (shelter.occupancyPercent + deltaOcc).coerceIn(6, 68)
            val newBat = (shelter.solarBatteryLevel + if ((idx + updateCycleCount) % 3 == 0) 1 else 0).coerceIn(90, 100)
            val newStatus = if (newOcc > 45) "PRE_ALERT_MONITORING" else "OPERATIONAL_STANDBY"
            shelter.copy(
                occupancyPercent = newOcc,
                solarBatteryLevel = newBat,
                operationalStatus = newStatus
            )
        }

        currentTouristSpots = currentTouristSpots.mapIndexed { idx, spot ->
            if (spot.id == "spot_chandipur") {
                val tideStages = listOf(
                    "Low Tide - Sea receded 4.2 km (Safe Walk Window)",
                    "Low Tide - Sea receded 3.8 km (Walk Safe next 2h)",
                    "Ebb Tide Retreating - Phenomenon in progress",
                    "High Tide incoming in 1h 25m - Caution advised"
                )
                spot.copy(
                    tideStatus = tideStages[(updateCycleCount + idx) % tideStages.size],
                    visitorFlow = listOf("Mild Breeze • Low Flow", "Pleasant Breeze • Moderate Flow", "Sunset Walkers • Active Flow").random()
                )
            } else if (spot.category.contains("Beach", ignoreCase = true)) {
                spot.copy(
                    tideStatus = "Calm Estuary • Safe for Boarding",
                    visitorFlow = listOf("Active Fishermen • Low Tourist Flow", "Harbor Busy • Moderate Flow").random()
                )
            } else {
                spot.copy(
                    visitorFlow = listOf("Quiet & Serene", "Devotees Arriving • Moderate Flow", "Peaceful Evening Hours").random()
                )
            }
        }

        selectedItem?.let { sel ->
            val updated = (currentCycloneShelters + currentTouristSpots).find { it.id == sel.id }
            if (updated != null) {
                selectedItem = updated
            }
        }
    }

    // Auto-update countdown ticker loop
    LaunchedEffect(isAutoUpdateEnabled, autoUpdateIntervalSeconds) {
        if (!isAutoUpdateEnabled) return@LaunchedEffect
        secondsRemaining = autoUpdateIntervalSeconds
        while (true) {
            delay(1000L)
            if (secondsRemaining > 1) {
                secondsRemaining--
            } else {
                secondsRemaining = autoUpdateIntervalSeconds
                triggerAutoUpdate()
            }
        }
    }

    // TileOverlay for offline pre-cached mode
    LaunchedEffect(googleMapInstance, isOfflineModeActive) {
        val map = googleMapInstance ?: return@LaunchedEffect
        if (isOfflineModeActive) {
            if (offlineTileOverlay == null) {
                try {
                    val tileProvider = BalasoreOfflineTileProvider(context)
                    offlineTileOverlay = map.addTileOverlay(
                        TileOverlayOptions()
                            .tileProvider(tileProvider)
                            .zIndex(2f)
                    )
                } catch (_: Exception) {}
            }
        } else {
            offlineTileOverlay?.remove()
            offlineTileOverlay = null
        }
    }

    // Filtered items
    val filteredItems = remember(selectedCategory, currentTouristSpots, currentCycloneShelters) {
        when (selectedCategory) {
            "All" -> currentTouristSpots + currentCycloneShelters
            "Tourist Hotspots" -> currentTouristSpots
            "Cyclone Shelters" -> currentCycloneShelters
            "Beach" -> currentTouristSpots.filter { it.category.contains("Beach", ignoreCase = true) }
            "Temple" -> currentTouristSpots.filter { it.category.contains("Temple", ignoreCase = true) }
            "Nature & Heritage" -> currentTouristSpots.filter { !it.category.contains("Beach", ignoreCase = true) && !it.category.contains("Temple", ignoreCase = true) }
            else -> currentTouristSpots + currentCycloneShelters
        }
    }

    val isMapsKeyConfigured = remember {
        com.example.BuildConfig.MAPS_API_KEY.isNotBlank()
    }
    var useVectorEngine by remember { mutableStateOf(!isMapsKeyConfigured) }

    // Initialize MapView lifecycle
    val mapView = remember(useVectorEngine) {
        if (!useVectorEngine && isMapsKeyConfigured) {
            MapView(context).apply {
                onCreate(Bundle())
            }
        } else {
            null
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            mapView?.let { mv ->
                when (event) {
                    Lifecycle.Event.ON_START -> mv.onStart()
                    Lifecycle.Event.ON_RESUME -> mv.onResume()
                    Lifecycle.Event.ON_PAUSE -> mv.onPause()
                    Lifecycle.Event.ON_STOP -> mv.onStop()
                    Lifecycle.Event.ON_DESTROY -> mv.onDestroy()
                    else -> {}
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            try {
                routePolylineInstance?.remove()
                routePolylineInstance = null
                startNavMarkerInstance?.remove()
                startNavMarkerInstance = null
                destNavMarkerInstance?.remove()
                destNavMarkerInstance = null
                simulatedNavMarkerInstance?.remove()
                simulatedNavMarkerInstance = null
                offlineTileOverlay?.remove()
                offlineTileOverlay = null
                mapView?.onDestroy()
            } catch (_: Exception) {}
        }
    }

    /**
     * Helper to perform smooth camera zoom animation to a specified LatLng with desired zoom level.
     */
    fun animateCameraToLocation(lat: Double, lng: Double, zoom: Float = 15.5f) {
        googleMapInstance?.let { map ->
            val target = LatLng(lat, lng)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, zoom)
            map.animateCamera(cameraUpdate, 900, null)
        }
    }

    // Refresh markers on the Google Map whenever filteredItems or map instance changes
    LaunchedEffect(googleMapInstance, filteredItems) {
        val map = googleMapInstance ?: return@LaunchedEffect
        map.clear()
        markerToItemMap.clear()

        filteredItems.forEach { item ->
            val latLng = LatLng(item.latitude, item.longitude)
            val markerOptions = MarkerOptions().position(latLng)
                .title(item.name)
                .snippet(
                    if (item is BalasoreMapItem.CycloneShelterSpot) {
                        "Cyclone Shelter (Cap: ${item.capacity}) • ${item.block} • ${item.occupancyPercent}% Occ"
                    } else if (item is BalasoreMapItem.TouristSpot) {
                        "${item.category} • ${item.tideStatus.take(30)}..."
                    } else ""
                )

            if (item.isShelter) {
                // High-visibility Orange/Red marker for cyclone emergency shelters
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            } else {
                // Azure / Blue marker for tourist destinations and heritage sites
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            }

            val marker = map.addMarker(markerOptions)
            if (marker != null) {
                markerToItemMap[marker] = item
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("balasore_google_map_container")
    ) {
        if (!useVectorEngine && mapView != null) {
            // 1. Interactive Google Map View via AndroidView
            AndroidView(
                factory = {
                    mapView.apply {
                        getMapAsync { map ->
                            googleMapInstance = map
                            map.mapType = currentMapType
                            map.uiSettings.isZoomControlsEnabled = false
                            map.uiSettings.isCompassEnabled = true
                            map.uiSettings.isMapToolbarEnabled = true
                            map.uiSettings.isMyLocationButtonEnabled = false

                            // Center view on Balasore district town
                            val centerBalasore = LatLng(21.4934, 86.9135)
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerBalasore, 11.2f))

                            // Smooth camera zoom animation on marker click:
                            map.setOnMarkerClickListener { clickedMarker ->
                                val item = markerToItemMap[clickedMarker]
                                if (item != null) {
                                    selectedItem = item
                                    // Smooth camera zoom animation centering the view on the clicked location
                                    animateCameraToLocation(item.latitude, item.longitude, 15.5f)
                                }
                                // Show standard info window as well
                                false
                            }

                            map.setOnMapClickListener {
                                selectedItem = null
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            BalasoreDistrictVectorMap(
                activeExploreMode = "ALL_OVERLAYS",
                selectedHotspotCategory = "ALL",
                selectedHotspot = null,
                onSelectHotspot = {},
                selectedBusRoute = null,
                onSelectBusRoute = {},
                selectedTrain = null,
                onSelectTrain = {},
                selectedStation = null,
                onSelectStation = {},
                language = language,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 2. Top Header & Category Filter Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.75f), Color.Transparent)
                    )
                )
                .padding(top = 16.dp, bottom = 8.dp)
        ) {
            // Header Row with Title, Counts & Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = OceanBlue,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ମାନଚିତ୍ର" else "Balasore Interactive Map",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        )
                    }
                    Text(
                        text = "${currentCycloneShelters.size} Shelters • ${currentTouristSpots.size} Hotspots • Pre-cached",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(start = 36.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Sync Now Button
                    IconButton(
                        onClick = {
                            triggerAutoUpdate()
                            secondsRemaining = autoUpdateIntervalSeconds
                            Toast.makeText(context, "Telemetry Auto-Updated with Fresh Status", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Sync Telemetry",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Map Type Toggle Button (Normal / Satellite)
                    IconButton(
                        onClick = {
                            val nextType = if (currentMapType == GoogleMap.MAP_TYPE_NORMAL) {
                                GoogleMap.MAP_TYPE_HYBRID
                            } else {
                                GoogleMap.MAP_TYPE_NORMAL
                            }
                            currentMapType = nextType
                            googleMapInstance?.mapType = nextType
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Toggle Map Type",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Map",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Chips Carousel
            val categories = listOf(
                "All",
                "Tourist Hotspots",
                "Cyclone Shelters",
                "Beach",
                "Temple",
                "Nature & Heritage"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedCategory = category
                            selectedItem = null
                        },
                        label = {
                            val count = when (category) {
                                "All" -> currentCycloneShelters.size + currentTouristSpots.size
                                "Tourist Hotspots" -> currentTouristSpots.size
                                "Cyclone Shelters" -> currentCycloneShelters.size
                                "Beach" -> currentTouristSpots.count { it.category.contains("Beach", ignoreCase = true) }
                                "Temple" -> currentTouristSpots.count { it.category.contains("Temple", ignoreCase = true) }
                                "Nature & Heritage" -> currentTouristSpots.count { !it.category.contains("Beach", ignoreCase = true) && !it.category.contains("Temple", ignoreCase = true) }
                                else -> 0
                            }
                            Text(
                                text = "$category ($count)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.White.copy(alpha = 0.2f),
                            labelColor = Color.White,
                            selectedContainerColor = if (category == "Cyclone Shelters") AmberGold else OceanBlue,
                            selectedLabelColor = Color.White
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) Color.White else Color.White.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Auto-Updates Engine & Offline Pre-cache Control Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.65f)
                ),
                border = BorderStroke(
                    1.dp,
                    if (isOfflineModeActive) AmberGold.copy(alpha = 0.5f) else Color.White.copy(alpha = 0.2f)
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = if (isAutoUpdateEnabled) EmeraldGreen else Color.Gray,
                                modifier = Modifier.size(7.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAutoUpdateEnabled) "Auto-Updates: ${secondsRemaining}s" else "Auto-Updates: Paused",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (isOfflineModeActive) AmberGold.copy(alpha = 0.25f) else Color(0xFF0284C7).copy(alpha = 0.25f),
                                border = BorderStroke(0.8.dp, if (isOfflineModeActive) AmberGold else Color(0xFF38BDF8))
                            ) {
                                Text(
                                    text = if (isOfflineModeActive) "⚡ OFFLINE TILES" else "🌐 ONLINE MAP",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isOfflineModeActive) AmberGold else Color(0xFF38BDF8),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (showAutoUpdatePanel) "Hide" else "Controls",
                                fontSize = 10.sp,
                                color = Color(0xFF38BDF8),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .clickable { showAutoUpdatePanel = !showAutoUpdatePanel }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Expandable Control Panel
                    AnimatedVisibility(visible = showAutoUpdatePanel) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Dynamic Telemetry Auto-Update", fontSize = 11.sp, color = Color.White)
                                Switch(
                                    checked = isAutoUpdateEnabled,
                                    onCheckedChange = { isAutoUpdateEnabled = it },
                                    modifier = Modifier.height(24.dp),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = EmeraldGreen,
                                        checkedTrackColor = EmeraldGreen.copy(alpha = 0.5f)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Offline Vector Tile Provider (No Net)", fontSize = 11.sp, color = Color.White)
                                Switch(
                                    checked = isOfflineModeActive,
                                    onCheckedChange = { isOfflineModeActive = it },
                                    modifier = Modifier.height(24.dp),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = AmberGold,
                                        checkedTrackColor = AmberGold.copy(alpha = 0.5f)
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Interval:", fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f))
                                listOf(15, 30, 60).forEach { sec ->
                                    val isCur = autoUpdateIntervalSeconds == sec
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = if (isCur) OceanBlue else Color.White.copy(alpha = 0.15f),
                                        modifier = Modifier.clickable {
                                            autoUpdateIntervalSeconds = sec
                                            secondsRemaining = sec
                                        }
                                    ) {
                                        Text(
                                            text = "${sec}s",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "Cycle #${updateCycleCount}",
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            // MapCacheManager Bulk Pre-cache Row
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.08f),
                                border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Landmark Map Tiles Cache (${cacheSummary.cachedLandmarksCount}/${cacheSummary.totalLandmarksCount})",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(
                                            text = if (isPrefetchingAll) {
                                                currentPrefetchTask ?: "Caching offline tiles..."
                                            } else {
                                                "${cacheSummary.totalCachedTiles} tiles • ${cacheSummary.totalSizeFormatted} • 100% Offline"
                                            },
                                            fontSize = 9.sp,
                                            color = if (isPrefetchingAll) AmberGold else Color(0xFF38BDF8),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    if (isPrefetchingAll) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            CircularProgressIndicator(
                                                progress = { prefetchProgress },
                                                modifier = Modifier.size(16.dp),
                                                strokeWidth = 2.dp,
                                                color = AmberGold
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${(prefetchProgress * 100).toInt()}%",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                mapCacheManager.prefetchAllLandmarks()
                                                Toast.makeText(context, "Pre-fetching offline map tiles for Balasore landmarks...", Toast.LENGTH_SHORT).show()
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = OceanBlue),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(26.dp)
                                        ) {
                                            Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Cache All", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. Floating Map Controls (Zoom In, Zoom Out, Recenter)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Recenter Balasore Town
            FloatingActionButton(
                onClick = {
                    animateCameraToLocation(21.4934, 86.9135, 11.2f)
                    Toast.makeText(context, "Centered on Balasore Town", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_recenter_button"),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = OceanBlueDark,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Center Balasore",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Smooth Zoom In (+)
            FloatingActionButton(
                onClick = {
                    googleMapInstance?.animateCamera(CameraUpdateFactory.zoomIn(), 400, null)
                },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_zoom_in_button"),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = BentoSlate900,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Zoom In",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Smooth Zoom Out (-)
            FloatingActionButton(
                onClick = {
                    googleMapInstance?.animateCamera(CameraUpdateFactory.zoomOut(), 400, null)
                },
                modifier = Modifier
                    .size(44.dp)
                    .testTag("map_zoom_out_button"),
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = BentoSlate900,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Zoom Out",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 4. Bottom Information Card when a Marker is Clicked
        AnimatedVisibility(
            visible = selectedItem != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            selectedItem?.let { item ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(
                        1.5.dp,
                        if (item.isShelter) AmberGold.copy(alpha = 0.5f) else BentoPrimaryBlue.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp))
                        .testTag("selected_marker_detail_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Header Row: Type Badge + Distance + Close
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (item.isShelter) AmberGold.copy(alpha = 0.15f) else OceanBlue.copy(alpha = 0.12f),
                                border = BorderStroke(
                                    1.dp,
                                    if (item.isShelter) AmberGold.copy(alpha = 0.4f) else OceanBlue.copy(alpha = 0.3f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (item.isShelter) Icons.Default.Shield else Icons.Default.Place,
                                        contentDescription = null,
                                        tint = if (item.isShelter) AmberGold else OceanBlue,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (item.isShelter) "CYCLONE SHELTER" else (item as BalasoreMapItem.TouristSpot).category.uppercase(),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 10.sp,
                                            color = if (item.isShelter) AmberGold else OceanBlue
                                        )
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = BentoSlate200.copy(alpha = 0.6f)
                                ) {
                                    Text(
                                        text = "${item.distanceKm} km from town",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = BentoSlate700,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 10.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(6.dp))

                                IconButton(
                                    onClick = { selectedItem = null },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss",
                                        tint = BentoSlate500,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Title & Odia Name
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = BentoSlate900
                            )
                        )
                        Text(
                            text = item.odiaName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = OceanBlue,
                                fontWeight = FontWeight.Medium
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Detail specs depending on Shelter or Tourist Spot
                        if (item is BalasoreMapItem.CycloneShelterSpot) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFFEF3C7),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Capacity", fontSize = 10.sp, color = BentoSlate600)
                                        Text("${item.capacity} People", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BentoSlate900)
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF1F5F9),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text("Block / Elev", fontSize = 10.sp, color = BentoSlate600)
                                        Text("${item.block} • +${item.elevationMeters}m", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BentoSlate900, maxLines = 1)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (item.occupancyPercent > 50) Color(0xFFFEF2F2) else Color(0xFFF0FDF4),
                                border = BorderStroke(1.dp, if (item.occupancyPercent > 50) Color(0xFFFECACA) else Color(0xFFBBF7D0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Live Occupancy", fontSize = 10.sp, color = BentoSlate600)
                                        Text(
                                            text = "${item.occupancyPercent}% • ${item.operationalStatus.replace("_", " ")}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.occupancyPercent > 50) Color(0xFFDC2626) else Color(0xFF16A34A)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    LinearProgressIndicator(
                                        progress = { item.occupancyPercent / 100f },
                                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                        color = if (item.occupancyPercent > 50) AmberGold else EmeraldGreen,
                                        trackColor = BentoSlate200
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Solar Battery: ⚡ ${item.solarBatteryLevel}%", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = BentoSlate700)
                                        Text("💾 100% Pre-cached Offline", fontSize = 9.sp, color = OceanBlue, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        } else if (item is BalasoreMapItem.TouristSpot) {
                            Text(
                                text = item.shortDesc,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = BentoSlate600,
                                    fontSize = 12.sp
                                ),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF0F9FF),
                                border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("🌊 Live Tide Status", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OceanBlueDark)
                                        Text("💾 Pre-cached Offline", fontSize = 9.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = item.tideStatus,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BentoSlate900
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Visitor Flow: ${item.visitorFlow} • ${item.timings}",
                                        fontSize = 10.sp,
                                        color = BentoSlate600
                                    )
                                }
                            }
                        }

                        // MapCacheManager Landmark Offline Tile Cache Status & Actions
                        val targetCacheStatus = cacheStatusMap[item.id]
                        val isCached = targetCacheStatus?.isCached == true
                        val isPrefetchingThis = targetCacheStatus?.isPrefetching == true

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isCached) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, if (isCached) Color(0xFFBBF7D0) else Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isCached) Icons.Default.CheckCircle else Icons.Default.CloudDownload,
                                        contentDescription = null,
                                        tint = if (isCached) EmeraldGreen else OceanBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isCached) "Offline Map Tiles Cached" else "Map Tiles: Not Cached",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCached) Color(0xFF15803D) else BentoSlate700
                                        )
                                        Text(
                                            text = if (isCached) {
                                                "${targetCacheStatus?.cachedTilesCount} tiles • ${targetCacheStatus?.sizeFormatted} (Ready Offline)"
                                            } else {
                                                "Cache tiles to display on map without internet"
                                            },
                                            fontSize = 9.5.sp,
                                            color = BentoSlate500
                                        )
                                    }
                                }

                                if (isPrefetchingThis) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = OceanBlue
                                    )
                                } else {
                                    TextButton(
                                        onClick = {
                                            mapCacheManager.prefetchLandmark(item.id)
                                            Toast.makeText(context, "Pre-fetching offline map tiles for ${item.name}...", Toast.LENGTH_SHORT).show()
                                        },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (isCached) "Re-Cache" else "Pre-Fetch",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OceanBlue
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Buttons: Directions, Call / Details, Smooth Zoom
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Directions via Google Maps Intent
                            Button(
                                onClick = {
                                    val uri = Uri.parse("geo:${item.latitude},${item.longitude}?q=${item.latitude},${item.longitude}(${Uri.encode(item.name)})")
                                    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                    try {
                                        context.startActivity(mapIntent)
                                    } catch (_: Exception) {
                                        val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${item.latitude},${item.longitude}")
                                        context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (item.isShelter) AmberGold else OceanBlue
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Directions,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Directions", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            // If Cyclone shelter: direct Call button
                            if (item is BalasoreMapItem.CycloneShelterSpot && item.phone.isNotBlank()) {
                                OutlinedButton(
                                    onClick = {
                                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.phone}"))
                                        context.startActivity(dialIntent)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, EmeraldGreen),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldGreen),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp),
                                        tint = EmeraldGreen
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Call Control", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                // Smooth Zoom In & Center Button
                                OutlinedButton(
                                    onClick = {
                                        animateCameraToLocation(item.latitude, item.longitude, 16.5f)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, BentoPrimaryBlue),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NearMe,
                                        contentDescription = null,
                                        modifier = Modifier.size(15.dp),
                                        tint = OceanBlue
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Zoom Closer", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = OceanBlue)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Quick Horizontal Carousel at bottom when no marker is selected
        if (selectedItem == null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                        )
                    )
                    .padding(bottom = 16.dp, top = 8.dp)
            ) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredItems, key = { it.id }) { item ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = BorderStroke(
                                1.dp,
                                if (item.isShelter) AmberGold.copy(alpha = 0.5f) else BentoSlate200
                            ),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .width(210.dp)
                                .clickable {
                                    selectedItem = item
                                    // Smooth camera zoom animation centering the view on the selected card
                                    animateCameraToLocation(item.latitude, item.longitude, 15.5f)
                                }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (item.isShelter) AmberGold.copy(alpha = 0.15f) else OceanBlue.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = if (item.isShelter) "SHELTER" else (item as BalasoreMapItem.TouristSpot).category,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (item.isShelter) AmberGold else OceanBlue,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val isItemCached = cacheStatusMap[item.id]?.isCached == true
                                        if (isItemCached) {
                                            Icon(
                                                imageVector = Icons.Default.CloudDone,
                                                contentDescription = "Cached Offline",
                                                tint = EmeraldGreen,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                        }
                                        Text(
                                            text = "${item.distanceKm} km",
                                            fontSize = 10.sp,
                                            color = BentoSlate500,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BentoSlate900,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (item is BalasoreMapItem.CycloneShelterSpot) {
                                        "${item.occupancyPercent}% occ • ⚡ ${item.solarBatteryLevel}% bat • Tap to zoom"
                                    } else if (item is BalasoreMapItem.TouristSpot) {
                                        "${item.tideStatus.take(24)}... • Tap to zoom"
                                    } else "Tap to zoom on map",
                                    fontSize = 10.sp,
                                    color = if (item.isShelter && (item as BalasoreMapItem.CycloneShelterSpot).occupancyPercent > 50) AmberGold else OceanBlue,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
