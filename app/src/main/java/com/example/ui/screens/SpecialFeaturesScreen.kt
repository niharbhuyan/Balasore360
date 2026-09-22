package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Flare
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.daily.DailyBalasorePulse
import com.example.data.model.AppLanguage
import com.example.ui.components.DailyAutoUpdateCard
import com.example.ui.theme.BentoSlate100
import com.example.ui.theme.BentoSlate200
import com.example.ui.theme.BentoSlate400
import com.example.ui.theme.BentoSlate500
import com.example.ui.theme.BentoSlate600
import com.example.ui.theme.BentoSlate800
import com.example.ui.theme.BentoSlate900
import com.example.ui.theme.OceanBlue
import com.example.ui.theme.OceanBlueDark
import com.example.ui.viewmodel.UniqueFeatureSheetType

data class SpecialFeatureItem(
    val sheetType: UniqueFeatureSheetType,
    val titleEn: String,
    val titleOd: String,
    val category: String,
    val iconEmoji: String,
    val descriptionEn: String,
    val descriptionOd: String,
    val tagText: String,
    val primaryColor: Color,
    val borderColor: Color,
    val isAutoUpdated: Boolean = true
)

/**
 * Dedicated Menu / Screen for Balasore Special Features.
 * Provides curated access, search & categorized filtering for all 31 unique hubs
 * and daily auto-updating engines of Balasore district.
 */
@Composable
fun SpecialFeaturesScreen(
    dailyPulse: DailyBalasorePulse,
    language: AppLanguage,
    onRefreshDailyPulse: () -> Unit,
    onOpenFeatureSheet: (UniqueFeatureSheetType) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val allFeatures = remember {
        listOf(
            // Daily Coastal & Intertidal
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.CHANDIPUR_TIDE_TIMER,
                titleEn = "Chandipur Safe Tide Clock",
                titleOd = "ଚାନ୍ଦିପୁର ସୁରକ୍ଷିତ ଭଟ୍ଟା ସମୟ କାଳ",
                category = "Coastal & Estuary",
                iconEmoji = "🌊",
                descriptionEn = "Real-time 1-4 km vanishing sea recession tracker & safe walk window.",
                descriptionOd = "୧-୪ କିଲୋମିଟର ପଛକୁ ଯାଉଥିବା ସମୁଦ୍ରର ସୁରକ୍ଷିତ ଚଲାବୁଲା ସମୟ।",
                tagText = "DAILY TIDE",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.KASAFAL_RED_CRABS,
                titleEn = "Kasafal Red Crab Window",
                titleOd = "କାସାଫାଳ ନାଲି କଙ୍କଡ଼ା ଅଭିଯାନ",
                category = "Daily Pulse",
                iconEmoji = "🦀",
                descriptionEn = "Daily intertidal schedule when millions of red ghost crabs emerge on pristine sands.",
                descriptionOd = "ଭଟ୍ଟା ସମୟରେ ବାଲୁକା ତଟରେ ଲକ୍ଷ ଲକ୍ଷ ନାଲି କଙ୍କଡ଼ାଙ୍କ ବିଚରଣ।",
                tagText = "LIVE TIDAL SCHEDULE",
                primaryColor = Color(0xFFFFF1F2),
                borderColor = Color(0xFFFECDD3)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TALASARI_AUCTION_MONITOR,
                titleEn = "Talasari Fish Auction Clock",
                titleOd = "ତାଳସାରୀ ପ୍ରାତଃ ମତ୍ସ୍ୟ ନିଲାମ",
                category = "Daily Pulse",
                iconEmoji = "🐟",
                descriptionEn = "Live beach auction clock for freshly landed Hilsa, Tiger Prawns and pomfrets.",
                descriptionOd = "ସକାଳେ ଡଙ୍ଗାରୁ ଆସୁଥିବା ଇଲିସି ଓ ବାଘ ଚିଙ୍ଗୁଡ଼ିର ସଦ୍ୟ ନିଲାମ।",
                tagText = "05:00 - 08:30 AM",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALARAMGADI_ESTUARY,
                titleEn = "Balaramgadi Fishing Harbor",
                titleOd = "ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ଓ ମୁହାଣ",
                category = "Coastal & Estuary",
                iconEmoji = "⛵",
                descriptionEn = "Budhabalanga confluence, artisanal boat landings & daily aquaculture rates.",
                descriptionOd = "ବୁଢ଼ାବଳଙ୍ଗ ମୁହାଣ, ମାଛଧରା ଟ୍ରଲର ଏବଂ ଦୈନିକ ମତ୍ସ୍ୟ ଦର।",
                tagText = "AQUACULTURE RATES",
                primaryColor = Color(0xFFECFEFF),
                borderColor = Color(0xFFA5F3FC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BICHITRAPUR_MANGROVE_BOATING,
                titleEn = "Bichitrapur Mangrove Creeks",
                titleOd = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ବୋଟିଂ",
                category = "Wildlife & Nature",
                iconEmoji = "🛶",
                descriptionEn = "Tidal boating schedules through virgin delta mangroves at Subarnarekha mouth.",
                descriptionOd = "ସୁବର୍ଣ୍ଣରେଖା ମୁହାଣରେ ହେନ୍ତାଳବଣ ଭିତରେ ସୁରକ୍ଷିତ ଡଙ୍ଗା ଚାଳନା।",
                tagText = "HIGH-TIDE TIMINGS",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.DARK_SKY_BIOLUMINESCENCE,
                titleEn = "Dagara Dark Sky & Bioluminescence",
                titleOd = "ଡଗରା ନୀଳ ଆଲୋକ ଓ ତାରକା ରାତି",
                category = "Coastal & Estuary",
                iconEmoji = "✨",
                descriptionEn = "Noctiluca blue sea sparkle forecast & stargazing dune visibility guide.",
                descriptionOd = "ରାତିରେ ସମୁଦ୍ର ଢେଉରେ ନୀଳ ଆଲୋକ ଏବଂ ଆକାଶ ନିରୀକ୍ଷଣ ସୂଚକ।",
                tagText = "NIGHT WATCH",
                primaryColor = Color(0xFF0F172A),
                borderColor = Color(0xFF38BDF8)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.RIVER_FLOOD_TELEMETRY,
                titleEn = "Subarnarekha River Flood Telemetry",
                titleOd = "ସୁବର୍ଣ୍ଣରେଖା ବନ୍ୟା ଜଳସ୍ତର ମାପକେନ୍ଦ୍ର",
                category = "Coastal & Estuary",
                iconEmoji = "🌊",
                descriptionEn = "Rajghat & Jamsholaghat live gauge levels, warning marks & dam releases.",
                descriptionOd = "ରାଜଘାଟ ଓ ଜାମଶୋଳାଘାଟ ନଦୀ ଜଳସ୍ତର ଓ ବିପଦ ସଙ୍କେତ ସୂଚନା।",
                tagText = "HYDROLOGY GAUGES",
                primaryColor = Color(0xFFF0FDFA),
                borderColor = Color(0xFF99F6E4)
            ),

            // Heritage & Craft Guilds
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.MATI_MANISHA_ARTISANS,
                titleEn = "Mati O Manisha: Lac Bangles",
                titleOd = "ମାଟି ଓ ମଣିଷ: ବାଲେଶ୍ୱରୀ ଲାଖ ଚୁଡ଼ି",
                category = "Artisans & Heritage",
                iconEmoji = "🎨",
                descriptionEn = "Traditional natural resin lacquer bangles (ଲାଖ ଶଙ୍ଖା) & direct artisan workshops.",
                descriptionOd = "ମୋତିଗଞ୍ଜ ଓ ରେମୁଣାର ପାରମ୍ପରିକ ଲାଖ ଶଙ୍ଖା ଶିଳ୍ପୀଙ୍କ ସହ ସିଧାସଳଖ ସଂଯୋଗ।",
                tagText = "DIRECT FAIR-TRADE",
                primaryColor = Color(0xFFFDF2F8),
                borderColor = Color(0xFFFBCFE8)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.REMUNA_PRASAD_ARTISANS,
                titleEn = "Remuna Khirachora Bhog & Brass Guilds",
                titleOd = "ରେମୁଣା କ୍ଷୀରଚୋରା ଭୋଗ ଓ କଂସା ଶିଳ୍ପ",
                category = "Artisans & Heritage",
                iconEmoji = "🛕",
                descriptionEn = "Live Amrita Keli batch distribution alerts and Kansari bell-metal crafts.",
                descriptionOd = "ଅମୃତ କେଳି ପ୍ରସାଦ ବଣ୍ଟନ ଏବଂ ପ୍ରସିଦ୍ଧ କଂସା ବାସନ ଶିଳ୍ପୀ ପ୍ରଦର୍ଶନୀ।",
                tagText = "TEMPLE RITUALS",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.NILAGIRI_STONE_SABAI,
                titleEn = "Nilagiri Stone Carving & Sabai Grass",
                titleOd = "ନୀଳଗିରି ପଥର ଖୋଦେଇ ଓ ସବାଇ ଘାସ",
                category = "Artisans & Heritage",
                iconEmoji = "🪨",
                descriptionEn = "Utensil stone carving clusters and eco-friendly golden fibre handicrafts.",
                descriptionOd = "ପଥର ବାସନ ନିର୍ମାଣ ଶିଳ୍ପୀ ଏବଂ ଆଦିବାସୀ ସବାଇ ଘାସ ହସ୍ତଶିଳ୍ପ ସମବାୟ।",
                tagText = "GI CRAFT CLUSTERS",
                primaryColor = Color(0xFFF0FDFA),
                borderColor = Color(0xFF99F6E4)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BELL_METAL_ARTISANS,
                titleEn = "Remuna & Soro Bell Metal (କଂସା-ପିତ୍ତଳ)",
                titleOd = "ସୋରୋ ଓ ରେମୁଣା କଂସା-ପିତ୍ତଳ ଶିଳ୍ପ",
                category = "Artisans & Heritage",
                iconEmoji = "🔔",
                descriptionEn = "Master metal smiths crafting traditional temple bells, gongs, and thalis.",
                descriptionOd = "ମନ୍ଦିର ଘଣ୍ଟ, ଥାଳି ଓ ପାରମ୍ପରିକ କଂସା ବାସନ ନିର୍ମାଣକାରୀ।",
                tagText = "HANDCRAFTED HERITAGE",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALESWARIYA_DIALECT_PROVERBS,
                titleEn = "Baleswariya Dialect & Proverbs",
                titleOd = "ବାଲେଶ୍ୱରୀ ଭାଷା ଓ ଢଗଢମାଳି",
                category = "Daily Pulse",
                iconEmoji = "🗣️",
                descriptionEn = "Fakir Mohan Senapati's regional literary idioms and vernacular phrases.",
                descriptionOd = "ବ୍ୟାସକବି ଫକୀର ମୋହନ ସେନାପତିଙ୍କ ଐତିହାସିକ ଢଗ ଓ ବାଲେଶ୍ୱରୀ ଭାଷା।",
                tagText = "DAILY PROVERB",
                primaryColor = Color(0xFFFAF5FF),
                borderColor = Color(0xFFDDD6FE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BILINGUAL_HERITAGE_AUDIO,
                titleEn = "Bilingual Audio Stories & AR Architecture",
                titleOd = "ଦ୍ୱିଭାଷୀ ଐତିହ୍ୟ ଅଡ଼ିଓ ଓ ମନ୍ଦିର AR",
                category = "Artisans & Heritage",
                iconEmoji = "🎧",
                descriptionEn = "Immersive audio narratives in English and Odia plus Kalinga Temple 3D AR.",
                descriptionOd = "ଓଡ଼ିଆ ଓ ଇଂରାଜୀରେ ଐତିହ୍ୟ କାହାଣୀ ଏବଂ କଳିଙ୍ଗ ମନ୍ଦିର AR ଦୃଶ୍ୟ।",
                tagText = "3D AR + AUDIO",
                primaryColor = Color(0xFFFAF5FF),
                borderColor = Color(0xFFE9D5FF)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.COLONIAL_HERITAGE_WALK,
                titleEn = "Colonial Port Walk: Dutch & French Forts",
                titleOd = "ବାଲେଶ୍ୱର ଡଚ୍ ଓ ଫରାସୀ ଐତିହ୍ୟ ପଦଯାତ୍ରା",
                category = "Artisans & Heritage",
                iconEmoji = "🏛️",
                descriptionEn = "Self-guided walk across Dutch Cemetery, Barabati Port, and French Loge.",
                descriptionOd = "୧୭ଶ ଶତାବ୍ଦୀର ଡଚ୍ ସମାଧିପୀଠ, ବାରବାଟୀ ବନ୍ଦର ଓ ଫରାସୀ କୋଠି।",
                tagText = "GPS HERITAGE TRAIL",
                primaryColor = Color(0xFFFAF5FF),
                borderColor = Color(0xFFE9D5FF)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BUDDHIST_JAIN_CIRCUIT,
                titleEn = "Buddhist & Jain Sacred Circuit",
                titleOd = "ଅଯୋଧ୍ୟା ଓ କୁପାରୀ ବୌଦ୍ଧ-ଜୈନ ପୀଠ",
                category = "Artisans & Heritage",
                iconEmoji = "☸️",
                descriptionEn = "Ayodhya Marichi temple, Kupari stupa ruins, and Avukana Buddhist relics.",
                descriptionOd = "ଅଯୋଧ୍ୟାର ମାରୀଚୀ ମନ୍ଦିର, କୁପାରୀ ସ୍ତୂପ ଓ ପ୍ରାଚୀନ ଐତିହ୍ୟ।",
                tagText = "ARCHAEOLOGICAL TRAIL",
                primaryColor = Color(0xFFFAF5FF),
                borderColor = Color(0xFFDDD6FE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.LAKHANNATH_ZAMINDARI,
                titleEn = "Lakhannath Zamindari Moat Fortress",
                titleOd = "ଲକ୍ଷ୍ମଣନାଥ ରାଜବାଟୀ ଓ ପରିଖା ଦୁର୍ଗ",
                category = "Artisans & Heritage",
                iconEmoji = "🏰",
                descriptionEn = "Historical fortified gateway and protective moat on the Odisha-Bengal border.",
                descriptionOd = "ଓଡ଼ିଶା-ବଙ୍ଗ ସୀମାନ୍ତର ଐତିହାସିକ ରାଜବାଟୀ, ପରିଖା ଓ କଳା ସମ୍ପଦ।",
                tagText = "BORDER FORTRESS",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.CHANDANESWAR_CHADAK_MELA,
                titleEn = "Chandaneswar Chadak Mela & Shaivite Vows",
                titleOd = "ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ମେଳା ଓ ଉଡ଼ା ପର୍ବ",
                category = "Artisans & Heritage",
                iconEmoji = "🔥",
                descriptionEn = "Traditional Bhaktas, Kamina vows, and seasonal Shaivite rituals.",
                descriptionOd = "ବାବା ଚନ୍ଦନେଶ୍ୱରଙ୍କ ପ୍ରସିଦ୍ଧ ଚଡ଼କ ମେଳା ଓ ଭକ୍ତଙ୍କ ଉଡ଼ା ପରମ୍ପରା।",
                tagText = "SACRED RITUALS",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),

            // Defense & Nature
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.DEFENSE_TRAIL,
                titleEn = "ITR Missile Defense Trail & NOTAM",
                titleOd = "ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ଘାଟି ଓ ଡ଼. କଲାମ ଟ୍ରେଲ",
                category = "Defense & Wildlife",
                iconEmoji = "🚀",
                descriptionEn = "Dr. Kalam testing heritage, radar telemetry, and coastal NOTAM advisories.",
                descriptionOd = "ଅଗ୍ନି, ପୃଥ୍ବୀ କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷଣ ଇତିହାସ ଓ ସାମୁଦ୍ରିକ ସୁରକ୍ଷା ସୂଚନା।",
                tagText = "AIRSPACE & RADAR STATUS",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.HORSESHOE_CRAB,
                titleEn = "Horseshoe Crab Conservation Radar",
                titleOd = "ଜୀବନ୍ତ ଜୀବାଶ୍ମ ରାଜକଙ୍କଡ଼ା ସୁରକ୍ଷା",
                category = "Defense & Wildlife",
                iconEmoji = "🦀",
                descriptionEn = "450-million-year-old living fossil radar, nesting sandy shoals & reporting.",
                descriptionOd = "ଚାନ୍ଦିପୁର-ଖଣ୍ଡିଆ ମୁହାଣରେ ରାଜକଙ୍କଡ଼ାଙ୍କ ସଂରକ୍ଷଣ ଓ ଅନୁଧ୍ୟାନ।",
                tagText = "LIVING FOSSIL",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.KULDIHA_SAFARI_COMPANION,
                titleEn = "Kuldiha Elephant Safari Companion",
                titleOd = "କୁଲଡିହା ହାତୀ ଅଭୟାରଣ୍ୟ ସଫାରୀ ଗାଇଡ୍",
                category = "Defense & Wildlife",
                iconEmoji = "🐘",
                descriptionEn = "Wildlife species checklist, Rissia nature camp safari rules & tracking.",
                descriptionOd = "ରିଷିଆ ନେଚର କ୍ୟାମ୍ପ, ହାତୀ ଦର୍ଶନ ଓ ସୁରକ୍ଷିତ ଜଙ୍ଗଲ ନିୟମାବଳୀ।",
                tagText = "ECO-SAFARI CHECKLIST",
                primaryColor = Color(0xFFECFDF5),
                borderColor = Color(0xFFA7F3D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.ELEPHANT_PASSPORT,
                titleEn = "Elephant Corridor Live Alert",
                titleOd = "ହାତୀ ଚଳାଚଳ କରିଡର ସତର୍କତା",
                category = "Defense & Wildlife",
                iconEmoji = "🌿",
                descriptionEn = "Real-time forest ranger notices for Similipal-Kuldiha-Hadgarh herd crossings.",
                descriptionOd = "ଶିମିଳିପାଳ-କୁଲଡିହା ମଧ୍ୟରେ ହାତୀ ପଲଙ୍କ ଚଳାଚଳ ସତର୍କ ସୂଚନା।",
                tagText = "CORRIDOR TRACKING",
                primaryColor = Color(0xFFFAF5FF),
                borderColor = Color(0xFFE9D5FF)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.PANCHALINGESWAR_STREAM_SAFETY,
                titleEn = "Panchalingeswar Perennial Hill Stream",
                titleOd = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ିଆ ଝରଣା ଓ ପାହାଚ",
                category = "Defense & Wildlife",
                iconEmoji = "⛰️",
                descriptionEn = "Live stream flow clarity, 263-step moss slip index, and pilgrimage safety.",
                descriptionOd = "ପାହାଡ଼ିଆ ଝରଣାର ଜଳ ପ୍ରବାହ ଏବଂ ୨୬୩ ପାହାଚ ଚଢ଼ିବା ସୁରକ୍ଷା ନିର୍ଦ୍ଦେଶ।",
                tagText = "WATER CASCADE",
                primaryColor = Color(0xFFECFDF5),
                borderColor = Color(0xFFA7F3D0)
            ),

            // Agro & Marine Livelihoods
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.PAN_BARAJA_AGRO,
                titleEn = "Bhograi Pan Baraja Betel Agro-Tourism",
                titleOd = "ଭୋଗରାଇ ପାନ ବରଜ କୃଷି ପର୍ଯ୍ୟଟନ",
                category = "Agro & Food",
                iconEmoji = "🌿",
                descriptionEn = "Sweet and spicy Betel vine plantations exported across India and Bangladesh.",
                descriptionOd = "ସୁଗନ୍ଧିତ ବାଙ୍ଗାଲା ଓ ମିଠା ପାନ ବରଜ ଏବଂ ପାରମ୍ପରିକ ଚାଷ ପଦ୍ଧତି।",
                tagText = "AGRO-HERITAGE",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.HEIRLOOM_RICE_AGRO,
                titleEn = "Heirloom Rice: Kanakchur & Kalajira",
                titleOd = "କନକଚୂଡ଼ ଓ କଳାଜିରା ସୁଗନ୍ଧିତ ଧାନ",
                category = "Agro & Food",
                iconEmoji = "🌾",
                descriptionEn = "Aromatic heritage rice cultivars used exclusively in Remuna Khirachora bhog.",
                descriptionOd = "ରେମୁଣାର ଅମୃତ କେଳି ପାଇଁ ବ୍ୟବହୃତ ଐତିହାସିକ ସୁଗନ୍ଧିତ ଧାନ ଚାଷ।",
                tagText = "NATIVE CROPS",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_FOOD_TRAIL,
                titleEn = "Balasore Food Trail: Khira, Crab & Chhena",
                titleOd = "ବାଲେଶ୍ୱର ଖାଦ୍ୟ ଯାତ୍ରା: ଛେନାଝିଲି ଓ କଙ୍କଡ଼ା ତରକାରୀ",
                category = "Agro & Food",
                iconEmoji = "🍲",
                descriptionEn = "Curated trail of Remuna Khira, Chandipur mud crab masala, and Dalma sweets.",
                descriptionOd = "ବାଲେଶ୍ୱର ସହର ଓ ଚାନ୍ଦିପୁରର ପ୍ରସିଦ୍ଧ ପାରମ୍ପରିକ ସ୍ୱାଦିଷ୍ଟ ବ୍ୟଞ୍ଜନ।",
                tagText = "FOOD CULTURE",
                primaryColor = Color(0xFFFEF3C7),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BUDHABALANGA_RIVER_ANGLING,
                titleEn = "Budhabalanga Estuary Angling",
                titleOd = "ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ମୁହାଣ ମାଛ ଧରା",
                category = "Agro & Food",
                iconEmoji = "🎣",
                descriptionEn = "Traditional hook and line angling windows for Bhetki, Mangrove Jack and threadfins.",
                descriptionOd = "ଭେକଟି ଓ ବାଳିଆ ମାଛ ଧରିବାର ଉପଯୁକ୍ତ ସମୟ ଓ ସୁରକ୍ଷିତ ନଦୀ ତଟ।",
                tagText = "RIVER ANGLING",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),

            // Health & Medical Suites
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.DOCTORS_DIRECTORY,
                titleEn = "Doctors & OPD Duty Roster",
                titleOd = "ଡାକ୍ତର ତାଲିକା ଓ OPD ସମୟ ନିର୍ଘଣ୍ଟ",
                category = "Health & Medical",
                iconEmoji = "🩺",
                descriptionEn = "FMMCH Balasore & District Headquarters Hospital (DHH) emergency roster.",
                descriptionOd = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ ଡାକ୍ତର ତାଲିକା।",
                tagText = "DAILY OPD ROSTER",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.PRIVATE_HOSPITAL_DIRECTORY,
                titleEn = "Private Hospitals & Nursing Homes",
                titleOd = "ବେସରକାରୀ ହସ୍ପିଟାଲ ଓ ନର୍ସିଂହୋମ୍",
                category = "Health & Medical",
                iconEmoji = "🏥",
                descriptionEn = "24x7 emergency admissions, ICU/NICU beds, BSKY cashless desks, and specialty surgeons.",
                descriptionOd = "ଜରୁରୀକାଳୀନ ଆଇସିୟୁ, ବିଏସକେୱାଇ କ୍ୟାସଲେସ୍ ଏବଂ ପ୍ରମୁଖ ସର୍ଜନଙ୍କ ଡାଇରେକ୍ଟୋରି।",
                tagText = "24/7 ICU & SURGERY",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.MEDICINE_STORES_DIRECTORY,
                titleEn = "24x7 Pharmacies & Anti-Venom Stocks",
                titleOd = "୨୪ ଘଣ୍ଟିଆ ଔଷଧ ଦୋକାନ ଓ ସାପ ବିଷ ପ୍ରତିଷେଧକ",
                category = "Health & Medical",
                iconEmoji = "💊",
                descriptionEn = "All-night chemists with phone dials, Google Maps pins, and anti-venom (ASV).",
                descriptionOd = "ଜରୁରୀକାଳୀନ ଔଷଧ ଦୋକାନ ଏବଂ ସାପକାମୁଡ଼ା ପ୍ରତିଷେଧକ ଇଞ୍ଜେକ୍ସନ ସୂଚନା।",
                tagText = "24/7 PHARMACY",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.PATHOLOGY_LAB_DIRECTORY,
                titleEn = "Pathology Labs & Fasting Blood Tests",
                titleOd = "ରକ୍ତ ପରୀକ୍ଷା କେନ୍ଦ୍ର ଓ ଫାଷ୍ଟିଂ ଟେଷ୍ଟ",
                category = "Health & Medical",
                iconEmoji = "🔬",
                descriptionEn = "Fasting sample collections, CT/MRI diagnostic centres, and test fees.",
                descriptionOd = "ଡିଜିଟାଲ ରିପୋର୍ଟ ସହିତ ବାଲେଶ୍ୱରର ପ୍ରମୁଖ ପାଥୋଲୋଜି ଲ୍ୟାବ।",
                tagText = "FASTING DRAWS",
                primaryColor = Color(0xFFFFF1F2),
                borderColor = Color(0xFFFECDD3)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.POLYCLINIC_DIRECTORY,
                titleEn = "Polyclinics, Daycare & Speciality Clinics",
                titleOd = "ପଲିକ୍ଲିନିକ ଓ ବିଶେଷଜ୍ଞ ଡେ-କେୟାର",
                category = "Health & Medical",
                iconEmoji = "🏥",
                descriptionEn = "Multi-specialty doctor chambers, ultrasound USG, and day care clinics.",
                descriptionOd = "ହୃଦରୋଗ, ଶିଶୁରୋଗ ଓ ଅସ୍ଥିଶଲ୍ୟ ବିଶେଷଜ୍ଞଙ୍କ କ୍ଲିନିକ।",
                tagText = "DAYCARE CLINICS",
                primaryColor = Color(0xFFFAF5FF),
                borderColor = Color(0xFFDDD6FE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BLOOD_AND_DIALYSIS_DIRECTORY,
                titleEn = "24x7 Blood Bank & Dialysis Centres",
                titleOd = "ରକ୍ତ ଭଣ୍ଡାର ଓ ଡାଏଲିସିସ୍ କେନ୍ଦ୍ର",
                category = "Health & Medical",
                iconEmoji = "🩸",
                descriptionEn = "Red Cross blood bank live contact, rare blood group desks & DHH dialysis unit.",
                descriptionOd = "ଜିଲ୍ଲା ରେଡକ୍ରସ ରକ୍ତ ଭଣ୍ଡାର ଓ ଡିଏଚ୍ଏଚ୍ ଡାଏଲିସିସ୍ ୟୁନିଟ୍।",
                tagText = "EMERGENCY 24x7",
                primaryColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFECACA)
            ),

            // Emergency & Transit Tools
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.CYCLONE_SHELTER_FINDER,
                titleEn = "Multi-Hazard Cyclone Shelter Finder",
                titleOd = "ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ ଓ ଜରୁରୀକାଳୀନ ମାର୍ଗ",
                category = "Emergency & Transit",
                iconEmoji = "🛡️",
                descriptionEn = "GPS navigation to 60+ coastal cyclone shelters across Bhograi, Sadar, Bahanaga.",
                descriptionOd = "ବାଲେଶ୍ୱର ଉପକୂଳର ୬୦ରୁ ଊର୍ଦ୍ଧ୍ୱ ସୁରକ୍ଷିତ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀର GPS ମାର୍ଗ।",
                tagText = "DISASTER SAFETY",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.CYCLONE_ORAL_HISTORY,
                titleEn = "Cyclone Lore & Elder Warnings",
                titleOd = "ବାତ୍ୟା ଅଭିଜ୍ଞତା ଓ ପୂର୍ବାନୁମାନ ପରମ୍ପରା",
                category = "Emergency & Transit",
                iconEmoji = "🗣️",
                descriptionEn = "Traditional sea-breeze shifts, bird behaviour, and historical cyclone memories.",
                descriptionOd = "ଉପକୂଳ ବାସିନ୍ଦାଙ୍କ ପାରମ୍ପରିକ ପାଣିପାଗ ଅଭିଜ୍ଞତା ଓ ଲୋକକଥା।",
                tagText = "COASTAL RESILIENCE",
                primaryColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFECACA)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TRANSIT_FARE_ESTIMATOR,
                titleEn = "Auto, Taxi & RTO Transit Fare Estimator",
                titleOd = "ଅଟୋ ଓ ଟ୍ୟାକ୍ସି ଭଡ଼ା ନିର୍ଣ୍ଣୟ ଯନ୍ତ୍ର",
                category = "Emergency & Transit",
                iconEmoji = "🚖",
                descriptionEn = "Official fair-rate tariff calculator between Railway Station, Chandipur & Remuna.",
                descriptionOd = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନରୁ ଚାନ୍ଦିପୁର, ରେମୁଣା ଯିବା ପାଇଁ ସରକାରୀ ଭଡ଼ା ସୂଚୀ।",
                tagText = "RTO TARIFF CALCULATOR",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TRAVEL_JOURNAL,
                titleEn = "Balasore Offline Travel Journal",
                titleOd = "ବାଲେଶ୍ୱର ଭ୍ରମଣ ଡାଏରୀ ଓ ଫଟୋ ସଂଗ୍ରହ",
                category = "Emergency & Transit",
                iconEmoji = "📖",
                descriptionEn = "Offline trip scrapbook to pin memory notes, photos, and favourite spots.",
                descriptionOd = "ନିଜ ଭ୍ରମଣ ଅନୁଭୂତି, ଫଟୋ ଓ ଟିପ୍ପଣୀ ଲେଖି ରଖିବାର ଅଫଲାଇନ୍ ଡାଏରୀ।",
                tagText = "PERSONAL JOURNAL",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            // 6 Extended Unique Suites with Auto-Updating Telemetry
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.SALT_PAN_HERITAGE,
                titleEn = "Inchudi Salt Satyagraha & Pans",
                titleOd = "ଇଞ୍ଚୁଡ଼ି ଲବଣ ସତ୍ୟାଗ୍ରହ ଓ ସୌର ଲୁଣ କିଆରୀ",
                category = "Coastal & Estuary",
                iconEmoji = "🧂",
                descriptionEn = "1930 historic salt defiance memorial, solar brine Baumé density & mirror reflection windows.",
                descriptionOd = "୧୯୩୦ ମସିହାର ଐତିହାସିକ ଲବଣ ସତ୍ୟାଗ୍ରହ ସ୍ମୃତି ଏବଂ ସୌର ଲୁଣ ଉତ୍ପାଦନ ତଥ୍ୟ।",
                tagText = "SOLAR BRINE TELEMETRY",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.HILSA_MIGRATION,
                titleEn = "Subarnarekha Hilsa Run & Marine Migration",
                titleOd = "ସୁବର୍ଣ୍ଣରେଖା ଇଲିଶି ମାଛ ଚଳାଚଳ ଓ ଦର",
                category = "Daily Pulse",
                iconEmoji = "🐟",
                descriptionEn = "Monsoon brackish salinity gradient, live Kirtania beach landing clocks and authentic Besara cooking.",
                descriptionOd = "ସୁବର୍ଣ୍ଣରେଖା ମୁହାଣରେ ଇଲିଶି ମାଛର ଆଗମନ, କୀର୍ତ୍ତନିଆ ବନ୍ଦର ଦର ଓ ପ୍ରସ୍ତୁତି ଶୈଳୀ।",
                tagText = "ESTUARY RUN TELEMETRY",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.KULDIHA_ELEPHANT_CORRIDOR,
                titleEn = "Kuldiha Elephant Corridor Radar",
                titleOd = "କୁଲଡିହା ହାତୀ କରିଡର ସୁରକ୍ଷା ଓ ରାଡାର୍",
                category = "Defense & Wildlife",
                iconEmoji = "🐘",
                descriptionEn = "Live forest crossing alert level, Nilagiri transit speeds and watchtower viewing windows.",
                descriptionOd = "ନୀଳଗିରି-ମିତ୍ରପୁର ହାତୀ ଚଳାଚଳ ସୂଚକ, ରାତ୍ରୀକାଳୀନ ସୁରକ୍ଷା ଏବଂ ଟାୱାର ଦୃଶ୍ୟ।",
                tagText = "SANCTUARY RADAR ADVISORY",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.CHHENA_GAJA_HOT_BATCH,
                titleEn = "Balasori Chhena Gaja Hot Batch Clock",
                titleOd = "ବାଲେଶ୍ୱରୀ କାଠ ଚୁଲା ଛେନା ଗଜା",
                category = "Agro & Food",
                iconEmoji = "🍮",
                descriptionEn = "Live wood-fired oven batch timer, cardamom syrup immersion and historic Motiganj halwais.",
                descriptionOd = "କାଠ ଚୁଲାରେ ପ୍ରସ୍ତୁତ ତାଜା ଛେନା ଗଜା ବାହାରିବା ସମୟ ଓ ଐତିହାସିକ ମିଠା ଦୋକାନ ସୂଚୀ।",
                tagText = "WOOD-FIRED KILN CLOCK",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.RIVER_FERRY_SCHEDULE,
                titleEn = "Subarnarekha & Budhabalanga Passenger Ferry",
                titleOd = "ନଦୀ ଡଙ୍ଗା ଘାଟ ଓ ନୌକା ଚଳାଚଳ ସମୟସାରଣୀ",
                category = "Emergency & Transit",
                iconEmoji = "⛴️",
                descriptionEn = "Live tidal current crossability, Chaumukh-Kirtania country boat ghats & tariff guide.",
                descriptionOd = "ଚୌମୁଖ ଓ କୀର୍ତ୍ତନିଆ ଡଙ୍ଗା ଘାଟରେ ଯାତ୍ରୀବାହୀ ନୌକା ଚଳାଚଳ ଓ ଭଡ଼ା ସୂଚୀ।",
                tagText = "LIVE GHAT TELEMETRY",
                primaryColor = Color(0xFFF0FDFA),
                borderColor = Color(0xFF99F6E4)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.ODIA_SAHITYA_REVIVAL,
                titleEn = "Odia Sahitya Revival & Utkal Press Trail",
                titleOd = "ବ୍ୟାସକବି ଫକୀର ମୋହନ ଓ ଶାନ୍ତି କାନନ ସାହିତ୍ୟ ଧାରା",
                category = "Artisans & Heritage",
                iconEmoji = "📜",
                descriptionEn = "Daily classic literature excerpt, 1868 Utkal Press printing revolution & Shanti Kanan museum.",
                descriptionOd = "ଦୈନିକ ସାହିତ୍ୟ ପଂକ୍ତି, ବାଲେଶ୍ୱର ଉତ୍କଳ ପ୍ରେସ ମୁଦ୍ରଣ ବିପ୍ଳବ ଓ ଶାନ୍ତି କାନନ ସ୍ମୃତି।",
                tagText = "DAILY LITERARY PASSAGE",
                primaryColor = Color(0xFFFAF5FF),
                borderColor = Color(0xFFE9D5FF)
            ),
            // Cutting-Edge Suggested Innovations:
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.ASK_BALASORE_AI,
                titleEn = "Ask Balasore AI Companion & Lyria Music",
                titleOd = "ବାଲେଶ୍ୱର AI ସହାୟକ ଓ ଲିରିଆ ସଙ୍ଗୀତ ରଚନା",
                category = "AI Innovations",
                iconEmoji = "✨",
                descriptionEn = "Multi-turn Gemini 3.5 Flash / Pro conversation, Google Search & Maps grounding, and Lyria ambient coastal music generator.",
                descriptionOd = "ଜେମିନି AI ସହିତ କଥାବାର୍ତ୍ତା, ଲାଇଭ୍ ମ୍ୟାପ୍ ସୂଚନା ଏବଂ ଚାନ୍ଦିପୁର ସମୁଦ୍ର ସଙ୍ଗୀତ ସୃଷ୍ଟି।",
                tagText = "GEMINI 3.5 & LYRIA AI",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFF93C5FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.INCOIS_OCEAN_ADVISORY,
                titleEn = "INCOIS Coastal Wave & Fishermen Advisory",
                titleOd = "ସମୁଦ୍ର ଢେଉ, ତରଙ୍ଗ ଓ ମତ୍ସ୍ୟଜୀବୀ ସତର୍କତା",
                category = "Coastal & Estuary",
                iconEmoji = "🌊",
                descriptionEn = "Real-time sea swell height, wave periods, tidal current velocity, and coastal fishing safety flags.",
                descriptionOd = "ଚାନ୍ଦିପୁର, ବଳରାମଗଡ଼ି ଏବଂ ତାଳସାରୀ ଉପକୂଳର ଢେଉ ଉଚ୍ଚତା ଓ ସମୁଦ୍ର ଯାତ୍ରା ସୁରକ୍ଷା।",
                tagText = "INCOIS MET-OCEAN",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFF7DD3FC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.ODIA_PANJIKA_CALENDAR,
                titleEn = "Baleswar Kohinoor Panjika Calendar",
                titleOd = "ବାଲେଶ୍ୱର କୋହିନୂର ପାଞ୍ଜି ଓ ତିଥି ଦର୍ପଣ",
                category = "Artisans & Heritage",
                iconEmoji = "📅",
                descriptionEn = "Traditional Odia solar month, Tithi, Nakshatra, Amrutabela, Rahukala, and coastal temple festival timings.",
                descriptionOd = "ଖୀରଚୋରା ଗୋପୀନାଥ ଓ ଚନ୍ଦନେଶ୍ୱର ମନ୍ଦିର ନୀତି ସହିତ ଶୁଭବେଳା ଓ ତିଥି ବିବରଣୀ।",
                tagText = "ASTRONOMICAL TITHI",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.RAIBANIA_AUDIO_WALK,
                titleEn = "Raibania Fort Medieval Audio Walk",
                titleOd = "ରାଇବଣିଆ ଦୁର୍ଗ ଅଡିଓ ଗାଇଡ୍ ଓ ଇତିହାସ",
                category = "Artisans & Heritage",
                iconEmoji = "🏰",
                descriptionEn = "Immersive multi-stop audio story of Eastern India's largest medieval stone fort with ambient Lyria music generation.",
                descriptionOd = "୧୩ଶ ଶତାବ୍ଦୀର ବିଶାଳ ପଥର ଦୁର୍ଗର ଐତିହାସିକ ଯାତ୍ରା ଓ ଲୋକକଥା।",
                tagText = "AI AUDIO STORY WALK",
                primaryColor = Color(0xFFFDF4FF),
                borderColor = Color(0xFFF0ABFC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BLOOD_AND_BED_PULSE,
                titleEn = "Emergency Blood Donor & ICU Bed Pulse",
                titleOd = "ଜରୁରୀ ରକ୍ତଦାତା ଓ ଡାକ୍ତରଖାନା ବେଡ୍ ସ୍ଥିତି",
                category = "Emergency & Transit",
                iconEmoji = "🩸",
                descriptionEn = "Instant contact blood donor network by blood group, real-time hospital bed & dialysis unit availability.",
                descriptionOd = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ ଓ ବେସରକାରୀ ହସ୍ପିଟାଲ୍ ରକ୍ତ ଭଣ୍ଡାର ଓ ICU ବେଡ୍ ସନ୍ଧାନ।",
                tagText = "LIVE HEALTH PULSE",
                primaryColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFCA5A5)
            )
        )
    }

    val categories = listOf(
        "All",
        "AI Innovations",
        "Daily Pulse",
        "Coastal & Estuary",
        "Artisans & Heritage",
        "Defense & Wildlife",
        "Health & Medical",
        "Agro & Marine",
        "Emergency & Transit"
    )

    val filteredFeatures = remember(searchQuery, selectedCategory) {
        allFeatures.filter { item ->
            val matchesCategory = (selectedCategory == "All" || item.category == selectedCategory)
            val matchesSearch = searchQuery.isBlank() ||
                item.titleEn.contains(searchQuery, ignoreCase = true) ||
                item.titleOd.contains(searchQuery, ignoreCase = true) ||
                item.descriptionEn.contains(searchQuery, ignoreCase = true) ||
                item.tagText.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("special_features_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Banner: Curated Balasore Specialties
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
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
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "BALASORE 360 SPECIALTIES",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 9.5.sp,
                                    color = Color(0xFF7DD3FC)
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF22C55E),
                            modifier = Modifier.size(10.dp)
                        ) {}
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (language == AppLanguage.ODIA) "ବାଲେଶ୍ୱର ବିଶେଷ ସେବା ଓ ଐତିହ୍ୟ" else "Balasore Special Features",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (language == AppLanguage.ODIA)
                            "ଭଟ୍ଟା ସମୟ, ହାତୀ କରିଡର, ଇଲିଶି ମାଛ, ଲବଣ ସତ୍ୟାଗ୍ରହ, ସ୍ୱାସ୍ଥ୍ୟସେବା ଓ ୩୮ଟି ଦୈନିକ ଲାଇଭ୍ ହବ୍‌।"
                        else
                            "38 curated live hubs: private hospitals, tidal clocks, salt pans, Hilsa run, elephant radar, chhena gaja timers, 24x7 healthcare rosters and flood telemetry.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFFE2E8F0),
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }

        // Live Daily Auto-Update Pulse Card
        item {
            DailyAutoUpdateCard(
                dailyPulse = dailyPulse,
                onRefresh = onRefreshDailyPulse,
                onFeatureClick = onOpenFeatureSheet
            )
        }

        // Search Field
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("special_features_search_input"),
                placeholder = {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ବିଶେଷ ସୁବିଧା ଖୋଜନ୍ତୁ (ଯଥା: ଟାଇଡ୍, ଡାକ୍ତର, ଲାଖ, କଙ୍କଡ଼ା)..."
                        else "Search features (e.g. tide, doctors, lac bangles, flood)...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BentoSlate400,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = OceanBlue
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = BentoSlate400
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OceanBlue,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true
            )
        }

        // Category Filter Chips Row
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = OceanBlue,
                            selectedLabelColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) OceanBlue else MaterialTheme.colorScheme.outlineVariant
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        // Feature Count & Quick Info Banner
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredFeatures.size} SPECIAL HUBS AVAILABLE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "TOUCH TO OPEN SHEET",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // Curated Feature Cards List
        items(filteredFeatures, key = { it.sheetType.name }) { feature ->
            SpecialFeatureCardItem(
                item = feature,
                language = language,
                onClick = { onOpenFeatureSheet(feature.sheetType) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun SpecialFeatureCardItem(
    item: SpecialFeatureItem,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag("special_feature_card_${item.sheetType.name.lowercase()}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, item.borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(item.primaryColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = item.iconEmoji, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == AppLanguage.ODIA) item.titleOd else item.titleEn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = item.primaryColor,
                    border = BorderStroke(1.dp, item.borderColor)
                ) {
                    Text(
                        text = item.tagText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.sp,
                            color = BentoSlate800
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (language == AppLanguage.ODIA) item.descriptionOd else item.descriptionEn,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF22C55E),
                        modifier = Modifier.size(6.dp)
                    ) {}
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Real-time Telemetry Active",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF15803D),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onClick() }
                ) {
                    Text(
                        text = if (language == AppLanguage.ODIA) "ଖୋଲନ୍ତୁ" else "Open Hub",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = OceanBlue
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = OceanBlue,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
