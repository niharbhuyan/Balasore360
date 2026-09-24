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
import com.example.ui.components.LanguageSwitcherBannerCard
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
    onLanguageSelected: ((AppLanguage) -> Unit)? = null,
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
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.CITIZEN_CIVIC_EYE,
                titleEn = "Citizen Civic Eye: Clean Balasore",
                titleOd = "ଆମ ବାଲେଶ୍ୱର: ସ୍ୱଚ୍ଛତା ଓ ନାଗରିକ ଅଭିଯୋଗ",
                category = "Emergency & Transit",
                iconEmoji = "📸",
                descriptionEn = "Report road potholes, dark streetlights, drainage blocks & garbage heaps directly to Balasore Municipal wards with tracking.",
                descriptionOd = "ରାସ୍ତା ଖାଲ, ଅଚଳ ଷ୍ଟ୍ରିଟ୍ ଲାଇଟ୍, ଡ୍ରେନେଜ୍ ଓ ଆବର୍ଜନା ସମସ୍ୟା ପୌରପାଳିକାକୁ ଜଣାନ୍ତୁ।",
                tagText = "MUNICIPAL GRIEVANCE",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFF93C5FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TOTO_AUTO_FARE_CARD,
                titleEn = "Balasore Toto & Auto Fare Card",
                titleOd = "ବାଲେଶ୍ୱର ଟୋଟୋ ଓ ଅଟୋ ଭଡ଼ା ସୂଚୀ",
                category = "Emergency & Transit",
                iconEmoji = "🛺",
                descriptionEn = "Municipal fixed-rate fare chart, interactive route calculator, anti-overcharging rules, and direct traffic police helpline.",
                descriptionOd = "ଷ୍ଟେସନ, ଚାନ୍ଦିପୁର, ରେମୁଣା ଓ ଏଫଏମୟୁ ପାଇଁ ପୌର ନିର୍ଦ୍ଧାରିତ ଭଡ଼ା ଏବଂ ଟ୍ରାଫିକ ହେଲ୍ପଲାଇନ।",
                tagText = "RTA FIXED TARIFF",
                primaryColor = Color(0xFFFEFCE8),
                borderColor = Color(0xFFFDE047)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_CAMPUS_CAREER_BOARD,
                titleEn = "Balasore Campus & Career Board",
                titleOd = "ବାଲେଶ୍ୱର କ୍ୟାମ୍ପସ୍, ନିଯୁକ୍ତି ଓ ବସ୍ ସୂଚୀ",
                category = "Emergency & Transit",
                iconEmoji = "🎓",
                descriptionEn = "FM University exam schedules, daily campus shuttle bus timetables, DRDO Chandipur apprentice drives, and scholarships.",
                descriptionOd = "ଏଫଏମୟୁ ନୂଆ କ୍ୟାମ୍ପସ୍ ବସ୍ ସମୟସାରଣୀ, ପରୀକ୍ଷା ତାରିଖ ଏବଂ ଡିଆରଡିଓ ଆପ୍ରେଣ୍ଟିସ ନିଯୁକ୍ତି।",
                tagText = "FMU & DRDO HUB",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFF86EFAC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_AUTO_UPDATE_CENTER,
                titleEn = "Auto-Update & Live Telemetry Center",
                titleOd = "ସ୍ୱୟଂକ୍ରିୟ ନବୀକରଣ ଓ ଲାଇଭ୍ ଟେଲିମେଟ୍ରି କେନ୍ଦ୍ର",
                category = "Daily Pulse",
                iconEmoji = "🔄",
                descriptionEn = "Background synchronization loop, live river/tide/fish status, force refresh trigger, and Play Store update verification.",
                descriptionOd = "ନଦୀ ଜଳସ୍ତର, ସମୁଦ୍ର ଭଟ୍ଟା, ରକ୍ତଭଣ୍ଡାର ଓ ଖବରର ସ୍ୱୟଂକ୍ରିୟ ନବୀକରଣ ଏବଂ ଆପ୍ ଅପଡେଟ୍।",
                tagText = "AUTO-SYNC 15 MINS",
                primaryColor = Color(0xFF0F172A),
                borderColor = Color(0xFF38BDF8)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_JUNCTION_RADAR,
                titleEn = "Balasore Junction (BLS) Train Radar",
                titleOd = "ବାଲେଶ୍ୱର ଜଙ୍କସନ ଟ୍ରେନ ଓ ପ୍ଲାଟଫର୍ମ ରାଡାର",
                category = "Emergency & Transit",
                iconEmoji = "🚆",
                descriptionEn = "Live platform indicators for Vande Bharat, Dhauli & Coromandel, station amenities, cloakroom & RPF 139.",
                descriptionOd = "ବନ୍ଦେ ଭାରତ, ଧଉଳି ଓ କରମଣ୍ଡଳ ପ୍ଲାଟଫର୍ମ ସୂଚନା, କ୍ଲୋକରୁମ୍ ଏବଂ ରେଳ ସୁରକ୍ଷା ବଳ ସହାୟତା।",
                tagText = "LIVE RAIL RADAR",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFF93C5FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALESWARI_PAAN_KRUSHI_MANDI,
                titleEn = "Baleswari Paan & Krushi Mandi Index",
                titleOd = "ବାଲେଶ୍ୱରୀ ପାନ ଓ କୃଷି ମଣ୍ଡି ସୂଚୀ",
                category = "Agro & Food",
                iconEmoji = "🍃",
                descriptionEn = "Daily wholesale mandi rates for Bhograi Mitha Paan, Baliapal Sanchi Paan, cashew nuts & KVK advisories.",
                descriptionOd = "ଭୋଗରାଇ ମିଠା ପାନ, ବାଲିଆପାଳ ସାଞ୍ଚି ପାନ ଓ କାଜୁ ବାଦାମର ଦୈନିକ ହୋଲସେଲ ମଣ୍ଡି ଦର।",
                tagText = "DAILY PAAN MANDI",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFF86EFAC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.NIGHT_CHEMIST_SANJEEVANI,
                titleEn = "24x7 Night Chemist & Sanjeevani Oxygen",
                titleOd = "୨୪x୭ ରାତ୍ରିକାଳୀନ ଔଷଧାଳୟ ଓ ଅକ୍ସିଜେନ ଡେସ୍କ",
                category = "Emergency & Transit",
                iconEmoji = "💊",
                descriptionEn = "All-night pharmacies near DHH, emergency medical oxygen cylinder refills, ICU ambulances & Jan Aushadhi.",
                descriptionOd = "ଡିଏଚ୍ଏଚ୍ ଓ ସହରର ରାତ୍ରି ଔଷଧ ଦୋକାନ, ଜରୁରୀକାଳୀନ ଅମ୍ଳଜାନ ସିଲିଣ୍ଡର ଓ ଆମ୍ବୁଲାନ୍ସ।",
                tagText = "24x7 EMERGENCY RX",
                primaryColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFCA5A5)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALESWAR_MAHOTSAV_CHADAK,
                titleEn = "Baleswar Mahotsav & Festival Calendar",
                titleOd = "ବାଲେଶ୍ୱର ମହୋତ୍ସବ ଓ ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ମେଳା",
                category = "Artisans & Heritage",
                iconEmoji = "🎭",
                descriptionEn = "Chandaneswar Uda Parba penance timetable, Expo cultural stage schedules, and Kartik Purnima boat floating.",
                descriptionOd = "ଚନ୍ଦନେଶ୍ୱର ଉଡ଼ା ପର୍ବ, ଆଇଟିଆଇ ପଡ଼ିଆ ମହୋତ୍ସବ ଏବଂ ତାଳସାରୀ ବୋଇତ ବନ୍ଦାଣ ତିଥି।",
                tagText = "FESTIVAL CALENDAR",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.NILAGIRI_STONE_ARTISAN,
                titleEn = "Nilagiri Green Stone & Dokra Showcase",
                titleOd = "ନୀଳଗିରି ସବୁଜ ପଥର ବାସନ ଓ ଢୋକ୍ରା ଶିଳ୍ପ",
                category = "Artisans & Heritage",
                iconEmoji = "🏺",
                descriptionEn = "GI green chlorite cookware handis, temple sculptures, Kuldiha Dokra brasscraft & direct artisan contacts.",
                descriptionOd = "ନୀଳଗିରି ପଥର କୁଣ୍ଡ, ମନ୍ଦିର ମୂର୍ତ୍ତି ଏବଂ କୁଲଡିହା ଢୋକ୍ରା ପିତ୍ତଳ କଳାକାରଙ୍କ ସିଧାସଳଖ ଯୋଗାଯୋଗ।",
                tagText = "ARTISAN REGISTRY",
                primaryColor = Color(0xFFF1F5F9),
                borderColor = Color(0xFFCBD5E1)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TPNODL_WATCO_MONITOR,
                titleEn = "TPNODL Power & WATCO Water Civic Monitor",
                titleOd = "ବିଦ୍ୟୁତ୍ (TPNODL) ଓ ପାନୀୟ ଜଳ (WATCO) ସୂଚୀ",
                category = "Emergency & Transit",
                iconEmoji = "⚡",
                descriptionEn = "Scheduled substation maintenance cuts, piped municipal water release hours, and ward lineman contacts.",
                descriptionOd = "ଲାଇଭ୍ ବିଦ୍ୟୁତ୍ କାଟ୍ ବିଜ୍ଞପ୍ତି, ପାଇପ୍ ଜଳ ଯୋଗାଣ ସମୟ ଏବଂ ୱାର୍ଡ ଲାଇନମ୍ୟାନ ଫୋନ ନମ୍ବର।",
                tagText = "CIVIC UTILITY PULSE",
                primaryColor = Color(0xFFFEF3C7),
                borderColor = Color(0xFFFCD34D)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TALASARI_MANGROVE_EXPLORER,
                titleEn = "Talasari & Bichitrapur Mangrove Eco-Boat",
                titleOd = "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ବୋଟ୍ ସଫାରି",
                category = "Coastal & Estuary",
                iconEmoji = "🦀",
                descriptionEn = "OFDC mangrove boat safari booking tariffs, high tide creek entry guidelines & red crab colonies.",
                descriptionOd = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ବୋଟିଂ ଟିକେଟ, ସୁବର୍ଣ୍ଣରେଖା ମୁହାଣ ଲାଲ କଙ୍କଡ଼ା ଓ ଦୀଘା ଯୋଗାଯୋଗ।",
                tagText = "OFDC BOAT SAFARI",
                primaryColor = Color(0xFFE0F2FE),
                borderColor = Color(0xFF7DD3FC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.MO_SEVA_KENDRA_CITIZEN,
                titleEn = "Mo Seva Kendra & e-District Desk",
                titleOd = "ମୋ ସେବା କେନ୍ଦ୍ର ଓ ଇ-ପ୍ରଶାସନ ଡେସ୍କ",
                category = "Emergency & Transit",
                iconEmoji = "🌾",
                descriptionEn = "12 Block citizen centers, statutory certificate fee caps (₹30), document checklists & anti-overcharging helpline.",
                descriptionOd = "ବାଲେଶ୍ୱର ଜିଲ୍ଲାର ୧୨ଟି ବ୍ଲକ୍ ମୋ ସେବା କେନ୍ଦ୍ର ଫୋନ ନମ୍ବର, ସରକାରୀ ୟୁଜର ଫିସ୍ ଏବଂ ପ୍ରମାଣପତ୍ର ନିୟମ।",
                tagText = "CITIZEN SERVICES",
                primaryColor = Color(0xFFE0F2FE),
                borderColor = Color(0xFF7DD3FC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.MARINE_FISHERMEN_SAFETY,
                titleEn = "Marine Fishermen Sea-Safety & Radar",
                titleOd = "ମତ୍ସ୍ୟଜୀବୀ ସମୁଦ୍ର ସୁରକ୍ଷା ଓ ଟେଲିମେଟ୍ରି",
                category = "Coastal & Estuary",
                iconEmoji = "⚓",
                descriptionEn = "Balaramgadi & Kasafal swell/wind telemetry, Coast Guard SOS 1554, and marine police station desks.",
                descriptionOd = "ବଳରାମଗଡ଼ି ଓ କାସାଫାଳ ସମୁଦ୍ର ତରଙ୍ଗ ସ୍ଥିତି, ତଟରକ୍ଷୀ ୧୫୫୪ ଜରୁରୀ ସହାୟତା ଏବଂ ମେରାଇନ ଥାନା।",
                tagText = "SEA SAFETY & SOS",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFF93C5FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.KULDIHA_ECO_CAMP_SAFARI,
                titleEn = "Kuldiha Eco-Camp & Safari Desk",
                titleOd = "କୁଲଡିହା ନେଚର କ୍ୟାମ୍ପ ଓ ସଫାରୀ ଡେସ୍କ",
                category = "Daily Pulse",
                iconEmoji = "🐘",
                descriptionEn = "Rissia Nature Camp cottages, Jadachua elephant watchtower spotting times & Nilagiri checkpost permits.",
                descriptionOd = "ରିଷିଆ ନେଚର କ୍ୟାମ୍ପ ବୁକିଂ, ଜଡ଼ାଚୁଆ ହାତୀ ୱାଚଟାୱାର୍ ଏବଂ ନୀଳଗିରି ରେଞ୍ଜ ସଫାରି ଅନୁମତି।",
                tagText = "OFDC ECO-CAMP",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFF86EFAC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.FM_MCH_MEDICAL_COLLEGE_OPD,
                titleEn = "FM MCH Medical College OPD & BSKY",
                titleOd = "ଫକୀର ମୋହନ ମେଡିକାଲ OPD ଓ BSKY ସୂଚୀ",
                category = "Health & Medical",
                iconEmoji = "🏥",
                descriptionEn = "Remuna campus doctor chambers, daily specialist OPD roster, e-Sanjeevani video consults & BSKY helpdesks.",
                descriptionOd = "ଏଫଏମ ମେଡିକାଲ କଲେଜ ବିଭାଗୀୟ OPD ରୋଷ୍ଟର, ଇ-ସଞ୍ଜୀବନୀ ମାଗଣା ପରାମର୍ଶ ଏବଂ BSKY ତାଲିକାଭୁକ୍ତ ହସ୍ପିଟାଲ।",
                tagText = "MCH OPD ROSTER",
                primaryColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFCA5A5)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.SABAI_GRASS_MISSION_SHAKTI,
                titleEn = "Sabai Grass & Mission Shakti SHG Mart",
                titleOd = "ସବାଇ ଘାସ ଓ ମିଶନ ଶକ୍ତି ହାଟ",
                category = "Artisans & Heritage",
                iconEmoji = "🧺",
                descriptionEn = "Nilagiri golden fiber handicraft catalog, direct fair prices without middlemen, and Mission Shakti Haats.",
                descriptionOd = "ନୀଳଗିରି ସୁବର୍ଣ୍ଣ ସବାଇ ଘାସ ହସ୍ତଶିଳ୍ପ, ସିଧାସଳଖ ମହିଳା SHG ମୂଲ୍ୟ ଏବଂ ମିଶନ ଶକ୍ତି କେନ୍ଦ୍ର।",
                tagText = "SHG FAIR TRADE",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_MARITIME_COLONIAL,
                titleEn = "Maritime & Colonial Port Heritage",
                titleOd = "ବାଲେଶ୍ୱର ସାମୁଦ୍ରିକ ଓ ଔପନିବେଶିକ ଇତିହାସ",
                category = "Artisans & Heritage",
                iconEmoji = "⛵",
                descriptionEn = "Farasidinga French Loge enclave, Dutch VOC cemetery, British factory (1633), and Sadhabapua Boita voyages.",
                descriptionOd = "ଫରାସୀଡିଙ୍ଗା, ଡଚ୍ କବରସ୍ତାନ, ଇଂରେଜ୍ କୋଠି (୧୬୩୩) ଏବଂ ପ୍ରାଚୀନ ସାଧବ ବୋଇତ ବନ୍ଦାଣ ଇତିହାସ।",
                tagText = "COLONIAL PORTS",
                primaryColor = Color(0xFFEEF2FF),
                borderColor = Color(0xFFC7D2FE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_STUDENT_CAREER_SCHOLARSHIP,
                titleEn = "Scholarships & Career Guidance Desk",
                titleOd = "ଛାତ୍ରବୃତ୍ତି ଓ କ୍ୟାରିୟର ସହାୟତା ଡେସ୍କ",
                category = "Emergency & Transit",
                iconEmoji = "🎓",
                descriptionEn = "e-Medhabruti, Sudakshya girls technical aid, KALIA scholarship criteria, and Model Career Center.",
                descriptionOd = "ଓଡ଼ିଶା ରାଜ୍ୟ ଛାତ୍ରବୃତ୍ତି ପୋର୍ଟାଲ, ମେଧାବୃତ୍ତି, ସୁଦକ୍ଷା ବାଳିକା ଯୋଜନା ଏବଂ ଜିଲ୍ଲା ନିୟୋଜନ କାର୍ଯ୍ୟାଳୟ।",
                tagText = "SCHOLARSHIPS",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            // 7 New Local Specialized Hubs with Real-Time Telemetry & Utilities
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.DRDO_CHANDIPUR_SAFETY_RADAR,
                titleEn = "DRDO Chandipur Launch & Sea Radar",
                titleOd = "ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷଣ ଓ ସୁରକ୍ଷା ରାଡାର",
                category = "Defense & Wildlife",
                iconEmoji = "🚀",
                descriptionEn = "Live NOTAM airspace/sea exclusion zones, evacuation shelter telemetry, siren protocols & security checkposts.",
                descriptionOd = "କ୍ଷେପଣାସ୍ତ୍ର ପରୀକ୍ଷା ସମୟର ସାମୁଦ୍ରିକ ନିଷିଦ୍ଧାଞ୍ଚଳ, ସୁରକ୍ଷା ସାଇରନ ଓ ସ୍ଥାନାନ୍ତରଣ ଆଶ୍ରୟସ୍ଥଳୀ।",
                tagText = "DRDO NOTAM RADAR",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFF93C5FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.SUBARNAREKHA_SLUICE_FLOOD_RADAR,
                titleEn = "Subarnarekha River & Sluice Gate Radar",
                titleOd = "ସୁବର୍ଣ୍ଣରେଖା ନଦୀ ଓ ସ୍ଲୁଇସ୍ ଗେଟ୍ ବନ୍ୟା ରାଡାର",
                category = "Daily Pulse",
                iconEmoji = "🌊",
                descriptionEn = "Rajghat & Jamshedpur live gauge discharge, 8 coastal sluice gate status, and Bhograi-Jaleswar inundation risks.",
                descriptionOd = "ରାଜଘାଟ ଜଳସ୍ତର, ସ୍ଲୁଇସ୍ ଗେଟ୍ ସ୍ଥିତି, ଜଳ ନିଷ୍କାସନ କ୍ଷମତା ଏବଂ ବନ୍ୟା ସତର୍କତା ବୁଲେଟିନ୍।",
                tagText = "RIVER FLOOD RADAR",
                primaryColor = Color(0xFFE0F2FE),
                borderColor = Color(0xFF38BDF8)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.NILAGIRI_CHHAU_CULTURAL_GUILD,
                titleEn = "Nilagiri Chhau Dance & Cultural Guild",
                titleOd = "ନୀଳଗିରି ଛଉ ନୃତ୍ୟ ଓ ସାଂସ୍କୃତିକ ଗିଲ୍ଡ",
                category = "Artisans & Heritage",
                iconEmoji = "🎭",
                descriptionEn = "Centuries-old royal martial Chhau troupes, traditional clay masks, Akharas, and festive performance calendar.",
                descriptionOd = "ନୀଳଗିରି ରାଜବାଟୀ ଛଉ ନୃତ୍ୟ ପରମ୍ପରା, ମାଟି ମୁଖା ଶିଳ୍ପ, ଆଖଡ଼ା ଓ କଳାକାର ବୁକିଂ।",
                tagText = "MARTIAL HERITAGE",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.DHH_BLOOD_BANK_LIVE_RADAR,
                titleEn = "DHH & Red Cross Blood Bank Live Radar",
                titleOd = "ବାଲେଶ୍ୱର ମୁଖ୍ୟ ଡାକ୍ତରଖାନା ରକ୍ତ ଭଣ୍ଡାର ରାଡାର",
                category = "Health & Medical",
                iconEmoji = "🩸",
                descriptionEn = "Live component units (PRBC, Platelets, FFP), rare negative groups, voluntary camp schedule & SOS requisition.",
                descriptionOd = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ (DHH) ରକ୍ତ ମହଜୁଦ ସ୍ଥିତି, ପ୍ଲେଟଲେଟ୍ ଉପଲବ୍ଧତା ଓ ଜରୁରୀ ସହାୟତା।",
                tagText = "LIVE BLOOD STOCK",
                primaryColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFCA5A5)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.PAN_BARAJA_AQUA_SHRIMP_DESK,
                titleEn = "Paan Baraja & Aqua Shrimp Livelihood Desk",
                titleOd = "ପାନ ବରଜ ଓ ଇକୋ-ଆକ୍ୱା ଚିଙ୍ଗୁଡ଼ି ଡେସ୍କ",
                category = "Agro & Marine",
                iconEmoji = "🍃",
                descriptionEn = "Bhograi betel shade humidity, saline shrimp salinity/DO levels, MPEDA export rates & KVK pest doctor.",
                descriptionOd = "ପାନ ବରଜ ରୋଗ ନିରାକରଣ, ଆକ୍ୱା ଚିଙ୍ଗୁଡ଼ି ଚାଷ ଲାଇଭ୍ ପାରାମିଟର ଓ ରପ୍ତାନି ମଣ୍ଡି ଦର।",
                tagText = "LIVELIHOOD TELEMETRY",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFF86EFAC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_COURT_LEGAL_AID_DESK,
                titleEn = "District Court & DLSA Free Legal Aid",
                titleOd = "ଜିଲ୍ଲା କୋର୍ଟ ଓ ଡିଏଲ୍ଏସ୍ଏ ମାଗଣା ଆଇନ ସେବା",
                category = "Emergency & Transit",
                iconEmoji = "⚖️",
                descriptionEn = "District & Sessions Court cause lists, Lok Adalat schedules, free legal defense counsel & mediation center.",
                descriptionOd = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା କୋର୍ଟ ମାଗଣା ଆଇନ ସହାୟତା, ଲୋକ ଅଦାଲତ ଏବଂ ମଧ୍ୟସ୍ଥତା କେନ୍ଦ୍ର ତଥ୍ୟ।",
                tagText = "FREE LEGAL AID",
                primaryColor = Color(0xFFF8FAFC),
                borderColor = Color(0xFFCBD5E1)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.NH16_HIGHWAY_PATROL_TRAUMA_SOS,
                titleEn = "NH-16 Golden Quadrilateral Highway Patrol & SOS",
                titleOd = "NH-16 ଜାତୀୟ ରାଜପଥ ପାଟ୍ରୋଲିଂ ଓ ଟ୍ରମା ଏସଓଏସ",
                category = "Emergency & Transit",
                iconEmoji = "🚨",
                descriptionEn = "Bahanaga to Jaleswar toll corridor assistance, NHAI 1033 emergency patrol, cranes, and designated trauma centers.",
                descriptionOd = "ବାହାନଗା-ବାଲେଶ୍ୱର-ଜଳେଶ୍ୱର NH-16 ପାଟ୍ରୋଲ୍, ଟୋଲ୍ ପ୍ଲାଜା, କ୍ରେନ୍ ସହାୟତା ଓ ଟ୍ରମା ହସ୍ପିଟାଲ୍।",
                tagText = "HIGHWAY SOS 1033",
                primaryColor = Color(0xFFFFF1F2),
                borderColor = Color(0xFFFDA4AF)
            ),
            // 7 New Automated Hubs with Live Telemetry
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.PANCHALINGESWAR_KULDIHA_SAFARI,
                titleEn = "Panchalingeswar & Kuldiha Safari Companion",
                titleOd = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ଓ କୁଲଡିହା ସଫାରୀ କମ୍ପାନିଅନ୍",
                category = "Defense & Wildlife",
                iconEmoji = "🐘",
                descriptionEn = "Rissia forest checkpost, live elephant corridor telemetry, submerged Shiva Linga spring climb & 4x4 safari jeeps.",
                descriptionOd = "ରିସିଆ ଜଙ୍ଗଲ ଗେଟ୍ ସ୍ଥିତି, ହାତୀ ଚଳାଚଳ ସତର୍କତା, ପାହାଡ଼ ଝରଣା ଜଳସ୍ତର ଓ ୪x୪ ଜିପ୍ ଡ୍ରାଇଭର ସୂଚୀ।",
                tagText = "SAFARI & SPRING RADAR",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFF86EFAC)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.SER_BALASORE_RAILWAY_JUNCTION,
                titleEn = "SER Balasore & Jaleswar Rail Radar",
                titleOd = "ଦକ୍ଷିଣ ପୂର୍ବ ରେଳପଥ ବାଲେଶ୍ୱର ଲାଇଭ୍ ରାଡାର",
                category = "Emergency & Transit",
                iconEmoji = "🚆",
                descriptionEn = "Live platform assignments, Vande Bharat & Dhauli Express coach diagrams, MEMU passenger schedule & coolie tariff.",
                descriptionOd = "ବାଲେଶ୍ୱର ଓ ଜଳେଶ୍ୱର ପ୍ଲାଟଫର୍ମ ଟ୍ରାକର, ବନ୍ଦେ ଭାରତ କୋଚ୍ ଗାଇଡ୍, ମେମୁ ସମୟସାରଣୀ ଓ କୁଲି ମଜୁରୀ।",
                tagText = "SER RAIL RADAR",
                primaryColor = Color(0xFFEEF2FF),
                borderColor = Color(0xFFC7D2FE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.NOCCI_INDUSTRIAL_B2B_SKILL,
                titleEn = "NOCCI Industrial Corridor & B2B Hub",
                titleOd = "ନୋସି ଶିଳ୍ପ କରିଡର ଓ ଦକ୍ଷତା ନିଯୁକ୍ତି ଡେସ୍କ",
                category = "Emergency & Transit",
                iconEmoji = "🏭",
                descriptionEn = "Chhanpur & Remuna industrial park directory, CIPET plastic engineering, ITI apprenticeships & DIC assistance.",
                descriptionOd = "ଛାନପୁର ଓ ରେମୁଣା ଶିଳ୍ପାଞ୍ଚଳ, CIPET ତାଲିମ, କାରଖାନା ନିଯୁକ୍ତି ଓ ଜିଲ୍ଲା ଶିଳ୍ପ କେନ୍ଦ୍ର ସହାୟତା।",
                tagText = "NOCCI MSME & SKILLS",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TALSARI_BICHITRAPUR_MANGROVE_PILOT,
                titleEn = "Talsari & Bichitrapur Mangrove Pilot",
                titleOd = "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ରାଡାର",
                category = "Coastal & Estuary",
                iconEmoji = "🦀",
                descriptionEn = "Subarnarekha estuary motorboat booking, red ghost crab colony rules, and Bhograi farm-gate roasted cashew hubs.",
                descriptionOd = "ବିଚିତ୍ରପୁର ହେନ୍ତାଳବଣ ନୌକାବିହାର, ନାଲି କଙ୍କଡ଼ା ସଂରକ୍ଷଣ ନିୟମ ଓ ଭୋଗରାଇ କାଜୁ ବଜାର।",
                tagText = "MANGROVE & RED CRABS",
                primaryColor = Color(0xFFF0FDFA),
                borderColor = Color(0xFF99F6E4)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.FAKIR_MOHAN_BHASHA_LITERATURE,
                titleEn = "Fakir Mohan Bhasha & Literature Hub",
                titleOd = "ଫକୀର ମୋହନ ଭାଷା ଓ ଓଡ଼ିଆ ସାହିତ୍ୟ ଆର୍କାଇଭ",
                category = "Artisans & Heritage",
                iconEmoji = "📜",
                descriptionEn = "Mallikashpur ancestral home (Shantisneha), authentic Baleswari Dhaga-Dhamali idioms & district central library.",
                descriptionOd = "ଶାନ୍ତିସ୍ନେହ ମଲ୍ଲିକାଶପୁର ବ୍ୟାସକବି ସ୍ମୃତିପୀଠ, ବାଲେଶ୍ୱରୀ ଢଗଢମାଳି ଓ ଜିଲ୍ଲା କେନ୍ଦ୍ରୀୟ ପାଠାଗାର।",
                tagText = "ODIA LITERATURE CRADLE",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALESWARI_CUISINE_SWEET_HERITAGE,
                titleEn = "Baleswari Culinary & Sweet Heritage",
                titleOd = "ବାଲେଶ୍ୱରୀ ଖାଦ୍ୟ ଓ ମିଠା ଐତିହ୍ୟ ଡାଇରୀ",
                category = "Agro & Food",
                iconEmoji = "🍲",
                descriptionEn = "GI-coveted Chhena Mudki verified makers, Nilagiri authentic royal Khaja, Mudhi-Mansha, and Kasafal Sukhua dry fish.",
                descriptionOd = "ଛେନା ମୁଡ଼କି ଜିଆଇ ସୂଚୀ, ନୀଳଗିରି ରାଜକୀୟ ଖଜା, ବାଲେଶ୍ୱରୀ ମୁଢ଼ି ମାଂସ ଝୋଳ ଓ ଶୁଖୁଆ ବଜାର।",
                tagText = "CHHENA MUDKI & SUKHUA",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.TPNODL_WATCO_UTILITY_HOTLINE,
                titleEn = "TPNODL Power & WATCO Water Hotline",
                titleOd = "TPNODL ବିଦ୍ୟୁତ୍ ଓ WATCO ଜଳଯୋଗାଣ ରାଡାର",
                category = "Daily Pulse",
                iconEmoji = "⚡",
                descriptionEn = "33/11kV substation feeder shutdown telemetry, municipal piped tap water ward schedules & 24x7 fuse-off 1912.",
                descriptionOd = "ଟାଉନ୍ ଓ ରେମୁଣା ବିଦ୍ୟୁତ୍ ଫିଡର ସ୍ଥିତି, ପୌରପାଳିକା ପାନୀୟ ଜଳ ସମୟ ଓ ଫିଉଜ୍-ଅଫ୍ ହେଲ୍ପଲାଇନ।",
                tagText = "CIVIC GRID HOTLINE",
                primaryColor = Color(0xFFFEF3C7),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.DHAN_MANDI_MSP_PROCUREMENT,
                titleEn = "Dhan Mandi & MSP Procurement Radar",
                titleOd = "ଧାନ ମଣ୍ଡି ଓ ସରକାରୀ ଏମଏସପି କ୍ରୟ ରାଡାର",
                category = "Agro & Food",
                iconEmoji = "🌾",
                descriptionEn = "PACS mandi tokens across Jaleswar, Soro, Basta, <17% moisture FAQ standards & KVK agronomist hotline.",
                descriptionOd = "ଜଳେଶ୍ୱର, ସୋର, ବସ୍ତା PACS ଧାନ ମଣ୍ଡି ଟୋକନ୍, ଆର୍ଦ୍ରତା ନିୟମ ଓ କୃଷି ବୈଜ୍ଞାନିକ ପରାମର୍ଶ।",
                tagText = "MSP MANDI RADAR",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.NILAGIRI_GRANITE_STONEWARE_GUILD,
                titleEn = "Nilagiri Granite & Stoneware Guild",
                titleOd = "ନୀଳଗିରି କଳା ପଥର ଶିଳ୍ପ ଓ ବାସନ ଗିଲ୍ଡ",
                category = "Artisans & Heritage",
                iconEmoji = "🪨",
                descriptionEn = "Mitrapur chlorite stone carving, traditional Pathara Kunda/Kathi seasoning guides, and Utkalika emporium.",
                descriptionOd = "ନୀଳଗିରି କଳା ପଥର ବାସନ ଶିଳ୍ପୀ, ତେଲ ପାରମ୍ପରିକ ସିଜନିଂ ପ୍ରଣାଳୀ ଓ ଉତ୍କଳିକା ଏମ୍ପୋରିୟମ୍।",
                tagText = "STONEWARE GUILD",
                primaryColor = Color(0xFFF8FAFC),
                borderColor = Color(0xFFCBD5E1)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BAHANAGA_NHAI_RAPID_TRAUMA_NETWORK,
                titleEn = "Bahanaga & NH-16 Rapid Trauma Network",
                titleOd = "ବାହାନଗା ଓ NH-16 ରାପିଡ୍ ଟ୍ରମା ନେଟୱର୍କ",
                category = "Emergency & Transit",
                iconEmoji = "🚨",
                descriptionEn = "NH-16 108 ALS ambulance GPS posts, Balasore DHH apex trauma ICU beds, and Good Samaritan legal protection.",
                descriptionOd = "ବାହାନଗା ଓ NH-16 ୧୦୮ ଆମ୍ବୁଲାନ୍ସ ବେସ୍, ଟ୍ରମା ଆଇସିୟୁ ଶଯ୍ୟା ଏବଂ ସହାୟକ ଆଇନଗତ ସୁରକ୍ଷା।",
                tagText = "HIGHWAY TRAUMA 108",
                primaryColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFECACA)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BHUSANDESWAR_CHANDANESWAR_PILGRIMAGE,
                titleEn = "Bhusandeswar & Chandaneswar Pilgrimage",
                titleOd = "ଭୂଷଣ୍ଡେଶ୍ୱର ଓ ଚନ୍ଦନେଶ୍ୱର ତୀର୍ଥ ରାଡାର",
                category = "Artisans & Heritage",
                iconEmoji = "🛕",
                descriptionEn = "Asia's largest black stone lingam at Bhograi, Baba Chandaneswar Chadak Mela companion, and pilgrim shuttles.",
                descriptionOd = "ଭୂଷଣ୍ଡେଶ୍ୱର ବିରାଟ ଶିବଲିଙ୍ଗ, ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ମେଳା ଏବଂ ଜଳେଶ୍ୱର-ଦୀଘା ତୀର୍ଥ ଶଟଲ।",
                tagText = "ASIA'S LARGEST LINGAM",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.SENIOR_CITIZEN_SEVA_SETU,
                titleEn = "Seva Setu: Senior Citizen Care Desk",
                titleOd = "ସେବା ସେତୁ: ବରିଷ୍ଠ ନାଗରିକ ସହାୟତା ଡେସ୍କ",
                category = "Health & Medical",
                iconEmoji = "🏥",
                descriptionEn = "Verified geriatric home nurses, Nidan doorstep blood tests, and Balasore Police Senior Beat patrol registration.",
                descriptionOd = "ଘରୋଇ ନର୍ସିଂ ସେବା, ମାଗଣା ଡୋରଷ୍ଟେପ୍ ରକ୍ତ ପରୀକ୍ଷା ଏବଂ ବାଲେଶ୍ୱର ପୋଲିସ ବରିଷ୍ଠ ସୁରକ୍ଷା ଡେସ୍କ।",
                tagText = "SENIOR CARE SETU",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_HIGHER_EDUCATION_HUB,
                titleEn = "Balasore Higher Education & Campus Hub",
                titleOd = "ବାଲେଶ୍ୱର ଉଚ୍ଚଶିକ୍ଷା ଓ କ୍ୟାମ୍ପସ ଆଡମିଶନ ହବ୍",
                category = "Emergency & Transit",
                iconEmoji = "🎓",
                descriptionEn = "FMU, FM MCH, BCET, Govt ITI cutoffs, e-Medhabruti/PRERANA scholarship deadlines & Model Career Center.",
                descriptionOd = "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ, ମେଡିକାଲ କଲେଜ କଟ୍-ଅଫ୍, ଛାତ୍ରବୃତ୍ତି ଓ ନିୟୋଜନ ପରାମର୍ଶ।",
                tagText = "CAMPUS & SCHOLARSHIPS",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALASORE_MO_BUS_CRUT_NAVIGATOR,
                titleEn = "Balasore Mo Bus & CRUT Transit Hub",
                titleOd = "ବାଲେଶ୍ୱର ମୋ ବସ୍ ଓ ସହଦେବଖୁଣ୍ଟା ଟର୍ମିନାଲ୍",
                category = "Emergency & Transit",
                iconEmoji = "🚌",
                descriptionEn = "Routes 71-74 live frequencies, student concession pass guidelines & Sahadevkhunta inter-district express bays.",
                descriptionOd = "ସହରୀ ମୋ ବସ୍ ରୁଟ୍, ଛାତ୍ର ରିହାତି ପାସ୍ ଏବଂ ସହଦେବଖୁଣ୍ଟା ଅନ୍ତଃ-ଜିଲ୍ଲା ଏକ୍ସପ୍ରେସ୍ ବସ୍ ସୂଚୀ।",
                tagText = "CRUT MO BUS LIVE",
                primaryColor = Color(0xFFF0FDFA),
                borderColor = Color(0xFF99F6E4)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.AQUA_SHRIMP_HATCHERY_RADAR,
                titleEn = "Subarnarekha Brackish Shrimp Hatchery Radar",
                titleOd = "ସୁବର୍ଣ୍ଣରେଖା ଲୁଣିଜଳ ଚିଙ୍ଗୁଡ଼ି ହେଚେରୀ ରାଡାର୍",
                category = "Agro & Marine",
                iconEmoji = "🦐",
                descriptionEn = "Real-time salinity, pH, dissolved oxygen & export mandi rates across 3,800+ brackish ponds.",
                descriptionOd = "ବାଲିଆପାଳ, ଭୋଗରାଇ ଓ କସାଫଳର ୩,୮୦୦+ ପୋଖରୀର ଲାଇଭ୍ ଲବଣାକ୍ତତା (ppt), pH, ଅମ୍ଳଜାନ ଓ ମଣ୍ଡି ଦର।",
                tagText = "AQUA HATCHERY MPEDA",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.COASTAL_CYCLONE_SHELTER_NETWORK,
                titleEn = "Balasore Coastal 64 Cyclone Shelter Radar",
                titleOd = "ବାଲେଶ୍ୱର ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳ ନେଟୱାର୍କ",
                category = "Emergency & Transit",
                iconEmoji = "🌀",
                descriptionEn = "Real-time generator fuel, solar backup, potable water reserves & evacuation paths for 64 shelters.",
                descriptionOd = "୬୪ କଂକ୍ରିଟ୍ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳର ସୌର ବିଦ୍ୟୁତ, ଡିଜେଲ ଜେନେରେଟର ଓ ଜଳ ସଂରକ୍ଷଣ ଟେଲିମେଟ୍ରି।",
                tagText = "64 SHELTERS OSDMA",
                primaryColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.MARITIME_FOREIGN_LOGE_TRAIL,
                titleEn = "French-Dutch-Danish Foreign Loge Trail",
                titleOd = "ଫରାସୀ, ଡଚ୍ ଓ ଡେନିସ୍ ବାଣିଜ୍ୟ କୋଠି ଐତିହ୍ୟ",
                category = "Artisans & Heritage",
                iconEmoji = "🏛️",
                descriptionEn = "Explore Farashdanga French enclave, Dutch obelisks (1675), and Danish spice warehouses along Budhabalanga.",
                descriptionOd = "ଫରାସୀଡଙ୍ଗା, ଡଚ୍ ସମାଧି (୧୬୭୫) ଓ ଦିନେମାରଡଙ୍ଗାରେ ବାଲେଶ୍ୱରର ଆନ୍ତର୍ଜାତୀୟ ବାଣିଜ୍ୟ ଇତିହାସ।",
                tagText = "FOREIGN LOGE HERITAGE",
                primaryColor = Color(0xFFFDF4FF),
                borderColor = Color(0xFFF5D0FE)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.RURAL_MOBILE_TELEMEDICINE_NETWORK,
                titleEn = "Balasore Rural Mobile Medical Unit Network",
                titleOd = "ବାଲେଶ୍ୱର ଗ୍ରାମୀଣ ମୋବାଇଲ୍ ମେଡିକାଲ୍ ଭ୍ୟାନ୍ ସେବା",
                category = "Health & Medical",
                iconEmoji = "🩺",
                descriptionEn = "Daily village camp roster for 12 Mobile Medical Units, free diagnostic tests & Jan Aushadhi generic medicines.",
                descriptionOd = "ନୀଳଗିରି, ଔପଦା ଓ ଉପକୂଳ ଗ୍ରାମାଞ୍ଚଳରେ ଦୈନିକ ଡାକ୍ତରୀ କ୍ୟାମ୍ପ, ମାଗଣା ପରୀକ୍ଷା ଓ ଜନ ଔଷଧି।",
                tagText = "NHM 12 MMU VANS",
                primaryColor = Color(0xFFECFDF5),
                borderColor = Color(0xFFA7F3D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.BALESWARI_BETEL_PADDY_COOPERATIVE,
                titleEn = "Mati-Miras Betel Leaf & Saline Paddy Radar",
                titleOd = "ବାଲେଶ୍ୱରୀ ପାନ ବରଜ ଓ ଲୁଣା ଧାନ ସମବାୟ ରାଡାର୍",
                category = "Agro & Marine",
                iconEmoji = "🌿",
                descriptionEn = "Bhograi betel leaf export mandi rates, saline-tolerant coastal paddy procurement & PACS cooperatives.",
                descriptionOd = "ଭୋଗରାଇ-ବାଲିଆପାଳ ପାନ ବରଜ ରପ୍ତାନି ଦର, ଲୁଣା ଶଙ୍ଖି ଧାନ ଏବଂ ସମବାୟ ମଣ୍ଡିର ଦୈନିକ ଦର।",
                tagText = "BETEL LEAF & SALINE PADDY",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.COASTAL_CRUT_INTERDISTRICT_BUS_RADAR,
                titleEn = "Balasore Coastal Bus & Fleet Live Radar",
                titleOd = "ବାଲେଶ୍ୱର ଉପକୂଳ ମୋ ବସ୍ ଓ ଏକ୍ସପ୍ରେସ୍ ରାଡାର୍",
                category = "Emergency & Transit",
                iconEmoji = "🚍",
                descriptionEn = "Real-time GPS bus arrivals, digital fares & schedules for Chandipur, Talsari, Kasafal, and Nilagiri routes.",
                descriptionOd = "ଚାନ୍ଦିପୁର, ତାଳସାରୀ, କସାଫଳ ଓ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ବସ୍ ରୁଟ୍, ଜିପିଏସ୍ ଲାଇଭ୍ ଟ୍ରାକିଂ ଓ ଡିଜିଟାଲ୍ ଟିକେଟ୍।",
                tagText = "COASTAL BUS FLEET GPS",
                primaryColor = Color(0xFFF0F9FF),
                borderColor = Color(0xFFBAE6FD)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.HORSESHOE_CRAB_INTERTIDAL_PROTECTION,
                titleEn = "Horseshoe Crab Intertidal Bio-Reserve Watch",
                titleOd = "ଜୀବନ୍ତ ଜୀବାଶ୍ମ ରାଜକଙ୍କଡ଼ା ସଂରକ୍ଷଣ ୱାଚ୍",
                category = "Defense & Wildlife",
                iconEmoji = "🦀",
                descriptionEn = "Track Chandipur's 450-million-year-old living fossils with blue copper blood (LAL) & wildlife rescue helpline.",
                descriptionOd = "୪୫ କୋଟି ବର୍ଷ ପୁରାତନ ନୀଳରକ୍ତ ରାଜକଙ୍କଡ଼ାର ଚାନ୍ଦିପୁର-ବଳରାମଗଡ଼ିରେ ବସାବାନ୍ଧିବା ଓ ସୁରକ୍ଷା ଟେଲିମେଟ୍ରି।",
                tagText = "IUCN LIVING FOSSIL",
                primaryColor = Color(0xFFFFFBEB),
                borderColor = Color(0xFFFDE68A)
            ),
            SpecialFeatureItem(
                sheetType = UniqueFeatureSheetType.CITIZEN_FEEDBACK_PORTAL,
                titleEn = "Citizen Reports & Suggestions Desk",
                titleOd = "ନାଗରିକ ମତାମତ, ପ୍ରସ୍ତାବ ଓ ଅଭିଯୋଗ ଡେସ୍କ",
                category = "Daily Pulse",
                iconEmoji = "📝",
                descriptionEn = "Submit community reports, municipal suggestions & feature feedback directly to Firestore 'feedback' collection.",
                descriptionOd = "ଜନସାଧାରଣଙ୍କ ସମସ୍ୟା, ପରାମର୍ଶ ଓ ନୂତନ ପ୍ରସ୍ତାବ ସିଧାସଳଖ କ୍ଲାଉଡ୍ ଫାୟାରଷ୍ଟୋର 'feedback' ରେ ସଂରକ୍ଷଣ।",
                tagText = "FIRESTORE 'FEEDBACK' SYNC",
                primaryColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0)
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

        // Dynamic Language Switcher Banner Card (English ⇄ Odia)
        if (onLanguageSelected != null) {
            item {
                LanguageSwitcherBannerCard(
                    currentLanguage = language,
                    onLanguageSelected = onLanguageSelected,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
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

        // Quick Access: Auto-Update & Live Telemetry Center
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onOpenFeatureSheet(UniqueFeatureSheetType.BALASORE_AUTO_UPDATE_CENTER) }
                    .testTag("auto_update_center_quick_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                border = BorderStroke(1.dp, Color(0xFF38BDF8))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0369A1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔄", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (language == AppLanguage.ODIA) "ସ୍ୱୟଂକ୍ରିୟ ନବୀକରଣ କେନ୍ଦ୍ର" else "Auto-Update & Telemetry Hub",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4ADE80))
                            )
                        }
                        Text(
                            text = if (language == AppLanguage.ODIA) "୧୫ ମିନିଟ୍ ବ୍ୟବଧାନରେ ତଥ୍ୟ ସିଙ୍କ୍ • ଆପ୍ ଅପଡେଟ୍ ଯାଞ୍ଚ" else "15-min background sync • Force sync now & Play updates",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Open",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
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
