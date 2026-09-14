package com.example.ui.util

/**
 * Supported display languages for the Balasore 360 platform.
 * Supports English, Odia (ଓଡ଼ିଆ), and Hindi (हिन्दी).
 */
enum class AppLanguage(
    val code: String,
    val nativeLabel: String,
    val fullDisplay: String,
    val scriptTag: String
) {
    ENGLISH(
        code = "English",
        nativeLabel = "English",
        fullDisplay = "English (Default)",
        scriptTag = "EN"
    ),
    ODIA(
        code = "Odia",
        nativeLabel = "ଓଡ଼ିଆ",
        fullDisplay = "ଓଡ଼ିଆ (Odia)",
        scriptTag = "OR"
    ),
    HINDI(
        code = "Hindi",
        nativeLabel = "हिन्दी",
        fullDisplay = "हिन्दी (Hindi)",
        scriptTag = "HI"
    );

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}

/**
 * Centralized multi-language dictionary for real-time UI localization across
 * English, Odia, and Hindi.
 */
object AppStrings {

    fun appName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Balasore 360"
        AppLanguage.ODIA -> "ବାଲେଶ୍ୱର ୩୬୦"
        AppLanguage.HINDI -> "बालेश्वर ३६०"
    }

    fun brandName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Nihar Sales"
        AppLanguage.ODIA -> "ନିହାର ସେଲ୍ସ"
        AppLanguage.HINDI -> "निहार सेल्स"
    }

    fun appSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Digital Hub & Tourism Portal"
        AppLanguage.ODIA -> "ବାଲେଶ୍ୱର ଡିଜିଟାଲ୍ ସୂଚନା ଓ ପର୍ଯ୍ୟଟନ ପୋର୍ଟାଲ୍"
        AppLanguage.HINDI -> "बालेश्वर डिजिटल हब एवं पर्यटन पोर्टल"
    }

    // Navigation Tabs
    fun tabTourism(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Tourism"
        AppLanguage.ODIA -> "ପର୍ଯ୍ୟଟନ"
        AppLanguage.HINDI -> "पर्यटन स्थल"
    }

    fun tabNews(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Local News"
        AppLanguage.ODIA -> "ବାଲେଶ୍ୱର ଖବର"
        AppLanguage.HINDI -> "ताज़ा समाचार"
    }

    fun tabWeather(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Weather & Alerts"
        AppLanguage.ODIA -> "ପାଣିପାଗ ଓ ସତର୍କତା"
        AppLanguage.HINDI -> "मौसम एवं अलर्ट"
    }

    fun tabEssentials(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Directory & Info"
        AppLanguage.ODIA -> "ଜରୁରୀ ସେବା"
        AppLanguage.HINDI -> "आवश्यक सेवाएं"
    }

    // Search and Filters
    fun searchHint(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Search news, beaches, temples, weather..."
        AppLanguage.ODIA -> "ଖବର, ବେଳାଭୂମି, ମନ୍ଦିର, ପାଣିପାଗ ଖୋଜନ୍ତୁ..."
        AppLanguage.HINDI -> "समाचार, समुद्र तट, मंदिर, मौसम खोजें..."
    }

    fun quickFilterLabel(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Quick filter:"
        AppLanguage.ODIA -> "ଦ୍ରୁତ ଫିଲ୍ଟର୍:"
        AppLanguage.HINDI -> "त्वरित फ़िल्टर:"
    }

    fun favorites(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Favorites"
        AppLanguage.ODIA -> "ପସନ୍ଦିତା ସ୍ଥାନ"
        AppLanguage.HINDI -> "पसंदीदा स्थल"
    }

    fun allCategory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "All"
        AppLanguage.ODIA -> "ସମସ୍ତ"
        AppLanguage.HINDI -> "सभी"
    }

    fun beachCategory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Beach"
        AppLanguage.ODIA -> "ବେଳାଭୂମି"
        AppLanguage.HINDI -> "समुद्र तट"
    }

    fun templeCategory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Temple"
        AppLanguage.ODIA -> "ମନ୍ଦିର"
        AppLanguage.HINDI -> "मंदिर"
    }

    fun wildlifeCategory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Wildlife"
        AppLanguage.ODIA -> "ବନ୍ୟପ୍ରାଣୀ"
        AppLanguage.HINDI -> "वन्यजीव"
    }

    fun heritageCategory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Heritage"
        AppLanguage.ODIA -> "ଐତିହ୍ୟ"
        AppLanguage.HINDI -> "धरोहर"
    }

    fun portCategory(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Port"
        AppLanguage.ODIA -> "ବନ୍ଦର"
        AppLanguage.HINDI -> "बंदरगाह"
    }

    fun categoryLabel(category: String, lang: AppLanguage): String {
        return when (category.lowercase()) {
            "all" -> allCategory(lang)
            "beach" -> beachCategory(lang)
            "temple" -> templeCategory(lang)
            "wildlife" -> wildlifeCategory(lang)
            "heritage" -> heritageCategory(lang)
            "port" -> portCategory(lang)
            else -> category
        }
    }

    // Offline / Connectivity
    fun retry(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Retry"
        AppLanguage.ODIA -> "ପୁନର୍ବାର ଚେଷ୍ଟା କରନ୍ତୁ"
        AppLanguage.HINDI -> "पुनः प्रयास करें"
    }

    fun offlineTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "No Connection • Offline Mode Active"
        AppLanguage.ODIA -> "ସଂଯୋଗ ନାହିଁ • ଅଫଲାଇନ୍ ମୋଡ୍ ସକ୍ରିୟ"
        AppLanguage.HINDI -> "इंटरनेट नहीं है • ऑफ़लाइन मोड सक्रिय"
    }

    fun offlineSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Showing Room cached news, weather & hotspots"
        AppLanguage.ODIA -> "ରୁମ୍ କ୍ୟାସ୍ ସାଇତା ଖବର, ପାଣିପାଗ ଓ ଦର୍ଶନୀୟ ସ୍ଥାନ ଉପଲବ୍ଧ"
        AppLanguage.HINDI -> "रूम डेटाबेस में सुरक्षित समाचार, मौसम और पर्यटन स्थल"
    }

    fun roomCachedPill(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Room Cached"
        AppLanguage.ODIA -> "ରୁମ୍ରେ ସାଇତା"
        AppLanguage.HINDI -> "सुरक्षित डेटा"
    }

    fun offlineRoomPill(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Offline (Room)"
        AppLanguage.ODIA -> "ଅଫଲାଇନ୍ (ରୁମ୍)"
        AppLanguage.HINDI -> "ऑफ़लाइन (रूम)"
    }

    fun syncingPill(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Syncing..."
        AppLanguage.ODIA -> "ଅପଡେଟ୍ ହେଉଛି..."
        AppLanguage.HINDI -> "सिंक हो रहा है..."
    }

    // Distance and Landmark details
    fun kmFromBalasore(km: Int, lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "$km km from Balasore"
        AppLanguage.ODIA -> "ବାଲେଶ୍ୱରଠାରୁ $km କି.ମି."
        AppLanguage.HINDI -> "बालेश्वर से $km कि.मी."
    }

    fun bestTimeToVisit(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Best Time"
        AppLanguage.ODIA -> "ଉତ୍ତମ ସମୟ"
        AppLanguage.HINDI -> "सर्वोत्तम समय"
    }

    fun timings(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Timings"
        AppLanguage.ODIA -> "ସମୟ"
        AppLanguage.HINDI -> "समय"
    }

    fun entryFee(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Entry Fee"
        AppLanguage.ODIA -> "ପ୍ରବେଶ ଶୁଳ୍କ"
        AppLanguage.HINDI -> "प्रवेश शुल्क"
    }

    fun specialty(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Specialty"
        AppLanguage.ODIA -> "ବିଶେଷତ୍ୱ"
        AppLanguage.HINDI -> "विशेषता"
    }

    fun localTip(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Local Tip"
        AppLanguage.ODIA -> "ସ୍ଥାନୀୟ ପରାମର୍ଶ"
        AppLanguage.HINDI -> "स्थानीय सुझाव"
    }

    fun openInMaps(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Explore on Maps"
        AppLanguage.ODIA -> "ମ୍ୟାପ୍ରେ ଦେଖନ୍ତୁ"
        AppLanguage.HINDI -> "मानचित्र पर देखें"
    }

    // Language Section Settings
    fun languageSettingTitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Language & Script Preference"
        AppLanguage.ODIA -> "ଭାଷା ଓ ଲିପି ପସନ୍ଦ"
        AppLanguage.HINDI -> "भाषा एवं लिपि प्राथमिकता"
    }

    fun languageSettingDescription(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Choose your display language for titles, summaries, and annotations"
        AppLanguage.ODIA -> "ଆପଣଙ୍କ ପସନ୍ଦର ଭାଷା ବାଛନ୍ତୁ (ଇଂରାଜୀ, ଓଡ଼ିଆ, ହିନ୍ଦୀ)"
        AppLanguage.HINDI -> "शीर्षक, विवरण एवं सांस्कृतिक सूचनाओं हेतु अपनी भाषा चुनें"
    }

    // Weather Metrics
    fun feelsLike(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Feels Like"
        AppLanguage.ODIA -> "ଅନୁଭୂତ ତାପମାତ୍ରା"
        AppLanguage.HINDI -> "अनुभूत तापमान"
    }

    fun humidity(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Humidity"
        AppLanguage.ODIA -> "ଆର୍ଦ୍ରତା"
        AppLanguage.HINDI -> "आर्द्रता"
    }

    fun windSpeed(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Wind Speed"
        AppLanguage.ODIA -> "ପବନର ଗତି"
        AppLanguage.HINDI -> "हवा की गति"
    }

    fun rainProbability(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Rain Probability"
        AppLanguage.ODIA -> "ବର୍ଷା ସମ୍ଭାବନା"
        AppLanguage.HINDI -> "बारिश की संभावना"
    }

    fun tideVanishingSea(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Chandipur: Vanishing Sea (Low Tide)"
        AppLanguage.ODIA -> "ଚାନ୍ଦିପୁର: ଅଦୃଶ୍ୟ ସମୁଦ୍ର (ସମୁଦ୍ର ଭଟ୍ଟା)"
        AppLanguage.HINDI -> "चांदीपुर: लुप्त होता समुद्र (कम ज्वार)"
    }

    fun tideIncoming(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Chandipur: Incoming Tide (Return to Shore)"
        AppLanguage.ODIA -> "ଚାନ୍ଦିପୁର: ଜୁଆର ଆସୁଛି (କୂଳକୁ ଫେରନ୍ତୁ)"
        AppLanguage.HINDI -> "चांदीपुर: ज्वार आ रहा है (तट पर लौटें)"
    }

    fun tideReceding(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Chandipur: Sea Receding Outwards"
        AppLanguage.ODIA -> "ଚାନ୍ଦିପୁର: ସମୁଦ୍ର ପଛକୁ ଯାଉଛି"
        AppLanguage.HINDI -> "चांदीपुर: समुद्र पीछे हट रहा है"
    }

    // AI Assistant
    fun askAiButton(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Ask Balasore AI"
        AppLanguage.ODIA -> "ବାଲେଶ୍ୱର AI କୁ ପଚାରନ୍ତୁ"
        AppLanguage.HINDI -> "बालेश्वर AI से पूछें"
    }

    fun aiAssistantSubtitle(lang: AppLanguage): String = when (lang) {
        AppLanguage.ENGLISH -> "Grounded District Guide & Live Info"
        AppLanguage.ODIA -> "ଜିଲ୍ଲା ସମ୍ପର୍କିତ ନିର୍ଭରଯୋଗ୍ୟ ସୂଚନା"
        AppLanguage.HINDI -> "ज़िला स्तरीय प्रामाणिक जानकारी"
    }
}
