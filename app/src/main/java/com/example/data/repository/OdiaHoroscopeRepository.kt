package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.OdiaHoroscopeDao
import com.example.data.local.OdiaHoroscopeEntity
import com.example.data.remote.AstrologyApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Repository for Odia Daily Horoscope (ଦୈନିକ ରାଶିଫଳ) and Cultural Astrology.
 *
 * Fetches real-time horoscope data from Astrology API, augments it with
 * authentic Odia astrological elements (Tattva, Ruling Graha, Shubha Bela,
 * Rahu Kala, Lucky Gemstone & Chanting Mantra), and stores in Room DB
 * for instant offline accessibility.
 */
class OdiaHoroscopeRepository private constructor(
    private val context: Context,
    private val dao: OdiaHoroscopeDao = AppDatabase.getInstance(context).odiaHoroscopeDao(),
    private val api: AstrologyApiService = AstrologyApiService.create()
) {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _lastSyncTime = MutableStateFlow(getFormattedTimestamp())
    val lastSyncTime = _lastSyncTime.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage = _syncMessage.asStateFlow()

    /**
     * Flow of all 12 Odia zodiac signs from Room Database.
     */
    val horoscopesFlow: Flow<List<OdiaHoroscopeEntity>> = dao.getAllHoroscopesFlow()

    /**
     * Pre-defined baseline 12 Odia signs metadata.
     */
    data class RasiMetadata(
        val signKey: String,
        val signNameEn: String,
        val signNameOr: String,
        val westernApiSign: String,
        val symbol: String,
        val elementOr: String,
        val rulingPlanetOr: String,
        val defaultPredictionOr: String,
        val careerFinanceOr: String,
        val healthOr: String,
        val familyOr: String,
        val shubhaBela: String,
        val rahuKala: String,
        val amritaBela: String,
        val luckyNumber: Int,
        val luckyColor: String,
        val luckyGemstone: String,
        val worshipDeity: String,
        val chantingMantra: String
    )

    val rasiDefinitions = listOf(
        RasiMetadata(
            signKey = "mesha",
            signNameEn = "Aries",
            signNameOr = "ମେଷ",
            westernApiSign = "aries",
            symbol = "♈",
            elementOr = "ଅଗ୍ନି ତତ୍ତ୍ୱ (Fire)",
            rulingPlanetOr = "ମଙ୍ଗଳ (Mars)",
            defaultPredictionOr = "ଆଜି କାର୍ଯ୍ୟକ୍ଷେତ୍ରରେ ନୂତନ ଉତ୍ସାହ ସହ ସଫଳତା ପ୍ରାପ୍ତ ହେବ। ମିତ୍ରଙ୍କ ସହଯୋଗ ମିଳିବ ଏବଂ ଅଟକି ରହିଥିବା କାମ ସମ୍ପୂର୍ଣ୍ଣ ହେବ। ମନ ପ୍ରଫୁଲ୍ଲ ରହିବ।",
            careerFinanceOr = "ବ୍ୟବସାୟରେ ଆର୍ଥିକ ଉନ୍ନତି ଓ ନୂତନ ବିନିଯୋଗ ଲାଭଦାୟକ ହେବ। କର୍ମକ୍ଷେତ୍ରରେ ପଦୋନ୍ନତିର ଯୋଗ ରହିଛି।",
            healthOr = "ସ୍ୱାସ୍ଥ୍ୟ ଉତ୍ତମ ରହିବ। କ୍ରୋଧ ନିୟନ୍ତ୍ରଣ ରଖନ୍ତୁ ଓ ପ୍ରଚୁର ଜଳପାନ କରନ୍ତୁ।",
            familyOr = "ପରିବାରରେ ଆନନ୍ଦର ବାତାବରଣ ରହିବ। ପିତାମାତାଙ୍କ ଆଶୀର୍ବାଦ ମିଳିବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୭:୧୫ - ୦୮:୪୫, ଅପରାହ୍ନ ୦୨:୩୦ - ୦୪:୦୦",
            rahuKala = "ପ୍ରାତଃ ୦୯:୦୦ - ୧୦:୩୦",
            amritaBela = "ସନ୍ଧ୍ୟା ୦୬:୧୦ - ୦୭:୪୫",
            luckyNumber = 9,
            luckyColor = "ଲାଲ୍ ଓ ସିନ୍ଦୂର ରଙ୍ଗ (Crimson Red)",
            luckyGemstone = "ରକ୍ତ ମୁଗା (Red Coral)",
            worshipDeity = "ପ୍ରଭୁ ଶ୍ରୀ ହନୁମାନ ଓ ମା' ମଙ୍ଗଳା",
            chantingMantra = "ଓଁ ହଂ ହନୁମତେ ନମଃ"
        ),
        RasiMetadata(
            signKey = "brusa",
            signNameEn = "Taurus",
            signNameOr = "ବୃଷ",
            westernApiSign = "taurus",
            symbol = "♉",
            elementOr = "ପୃଥିବୀ ତତ୍ତ୍ୱ (Earth)",
            rulingPlanetOr = "ଶୁକ୍ର (Venus)",
            defaultPredictionOr = "ଧୈର୍ଯ୍ୟର ସହ ନିଷ୍ପତ୍ତି ନିଅନ୍ତୁ। ବନ୍ଧୁମାନଙ୍କ ସାହାଯ୍ୟରେ ବଡ଼ ସମସ୍ୟାର ସମାଧାନ ହେବ। ସାମାଜିକ ପ୍ରତିଷ୍ଠା ବୃଦ୍ଧି ପାଇବ।",
            careerFinanceOr = "ନୂତନ ଭାଗୀଦାରୀରେ ଲାଭ ମିଳିବ। ସଞ୍ଚୟ ବୃଦ୍ଧି ପାଇବ ଓ ଅନାବଶ୍ୟକ ଖର୍ଚ୍ଚରୁ ନିବୃତ୍ତ ରୁହନ୍ତୁ।",
            healthOr = "ଗଳା ଓ ଶ୍ୱାସଜନିତ ସାମାନ୍ୟ ଯତ୍ନ ନିଅନ୍ତୁ। ଯୋଗ ଓ ପ୍ରାଣାୟାମ ଉପକାରୀ।",
            familyOr = "ଦାମ୍ପତ୍ୟ ଜୀବନ ମଧୁର ରହିବ। ସନ୍ତାନମାନଙ୍କ ପଢ଼ାରେ ଉନ୍ନତି ଦେଖି ଖୁସି ହେବେ।",
            shubhaBela = "ପ୍ରାତଃ ୦୮:୩୦ - ୧୦:୦୦, ଅପରାହ୍ନ ୦୩:୧୫ - ୦୪:୪୫",
            rahuKala = "ଅପରାହ୍ନ ୦୧:୩୦ - ୦୩:୦୦",
            amritaBela = "ରାତ୍ରି ୦୮:୦୦ - ୦୯:୩୦",
            luckyNumber = 6,
            luckyColor = "ଧଳା ଓ ଆକାଶୀ ନୀଳ (White & Sky Blue)",
            luckyGemstone = "ହୀରା କିମ୍ବା ଓପଲ୍ (Diamond / Opal)",
            worshipDeity = "ମା' ଲକ୍ଷ୍ମୀ ଓ ମହାପ୍ରଭୁ ଜଗନ୍ନାଥ",
            chantingMantra = "ଓଁ ଶୁଂ ଶୁକ୍ରାୟ ନମଃ"
        ),
        RasiMetadata(
            signKey = "mithuna",
            signNameEn = "Gemini",
            signNameOr = "ମିଥୁନ",
            westernApiSign = "gemini",
            symbol = "♊",
            elementOr = "ବାୟୁ ତତ୍ତ୍ୱ (Air)",
            rulingPlanetOr = "ବୁଧ (Mercury)",
            defaultPredictionOr = "ବୌଦ୍ଧିକ କାର୍ଯ୍ୟରେ ପ୍ରଶଂସା ମିଳିବ। ବାଣୀରେ ମଧୁରତା ରଖନ୍ତୁ, ଯାହାଦ୍ୱାରା ଶତ୍ରୁମାନେ ମଧ୍ୟ ମିତ୍ର ହୋଇଯିବେ। ଆଜି ଦିନଟି ଶୁଭଙ୍କର।",
            careerFinanceOr = "ଆଇଟି, ଲେଖାଲେଖି ଓ ଯୋଗାଯୋଗ କ୍ଷେତ୍ରରେ ଥିବା ବ୍ୟକ୍ତିଙ୍କ ପାଇଁ ବଡ଼ ସୁଯୋଗ। ଆର୍ଥିକ ସ୍ଥିତି ସୁଦୃଢ଼ ହେବ।",
            healthOr = "ମାନସିକ ଶାନ୍ତି ଅନୁଭବ ହେବ। ପର୍ଯ୍ୟାପ୍ତ ନିଦ୍ରା ନିଅନ୍ତୁ।",
            familyOr = "ଭାଇଭଉଣୀଙ୍କ ସହ ସମ୍ପର୍କ ସୁଦୃଢ଼ ହେବ। ଗୃହରେ ସୁଖଶାନ୍ତି ବଜାୟ ରହିବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୯:୦୦ - ୧୦:୩୦, ସନ୍ଧ୍ୟା ୦୫:୦୦ - ୦୬:୩୦",
            rahuKala = "ପ୍ରାତଃ ୧୦:୩୦ - ୧୨:୦୦",
            amritaBela = "ପ୍ରାତଃ ୦୬:୩୦ - ୦୭:୫୦",
            luckyNumber = 5,
            luckyColor = "ସବୁଜ ଓ ହଳଦିଆ (Emerald Green & Yellow)",
            luckyGemstone = "ପନ୍ନା (Emerald)",
            worshipDeity = "ପ୍ରଭୁ ଶ୍ରୀ ଗଣେଶ",
            chantingMantra = "ଓଁ ବୁଂ ବୁଧାୟ ନମଃ"
        ),
        RasiMetadata(
            signKey = "karkata",
            signNameEn = "Cancer",
            signNameOr = "କର୍କଟ",
            westernApiSign = "cancer",
            symbol = "♋",
            elementOr = "ଜଳ ତତ୍ତ୍ୱ (Water)",
            rulingPlanetOr = "ଚନ୍ଦ୍ର (Moon)",
            defaultPredictionOr = "ଭାବପ୍ରବଣତା ଉପରେ ନିୟନ୍ତ୍ରଣ ରଖନ୍ତୁ। କୌଣସି ଗୁରୁତ୍ୱପୂର୍ଣ୍ଣ ନିଷ୍ପତ୍ତି ନେବା ପୂର୍ବରୁ ଗୁରୁଜନଙ୍କ ପରାମର୍ଶ ନିଅନ୍ତୁ। ଆଧ୍ୟାତ୍ମିକ ଚିନ୍ତା ବୃଦ୍ଧି ପାଇବ।",
            careerFinanceOr = "କର୍ମକ୍ଷେତ୍ରରେ ସତର୍କତା ଅବଲମ୍ବନ କରନ୍ତୁ। ଜଳଜାତ ଦ୍ରବ୍ୟ ଓ କୃଷି କାରବାରରେ ଲାଭ ହେବ।",
            healthOr = "ପେଟ ଓ ପାଚନ ସମସ୍ୟା ପ୍ରତି ଧ୍ୟାନ ଦିଅନ୍ତୁ। ତେଲ ମସଲା ଯୁକ୍ତ ଖାଦ୍ୟ ତ୍ୟାଗ କରନ୍ତୁ।",
            familyOr = "ମାତୃକୂଳରୁ ସାହାଯ୍ୟ ମିଳିବ। ଘରୋଇ ପରିବେଶ ଶାନ୍ତିପୂର୍ଣ୍ଣ ରହିବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୭:୦୦ - ୦୮:୩୦, ଅପରାହ୍ନ ୦୧:୩୦ - ୦୩:୦୦",
            rahuKala = "ପ୍ରାତଃ ୦୭:୩୦ - ୦୯:୦୦",
            amritaBela = "ଅପରାହ୍ନ ୦୪:୧୫ - ୦୫:୪୫",
            luckyNumber = 2,
            luckyColor = "ମୋତି ଧଳା ଓ ରୂପାଳୀ (Pearl White & Silver)",
            luckyGemstone = "ମୋତି (Pearl)",
            worshipDeity = "ଦେବାଧିଦେବ ମହାଦେବ (ଶିବ)",
            chantingMantra = "ଓଁ ସୋଂ ସୋମାୟ ନମଃ"
        ),
        RasiMetadata(
            signKey = "singha",
            signNameEn = "Leo",
            signNameOr = "ସିଂହ",
            westernApiSign = "leo",
            symbol = "♌",
            elementOr = "ଅଗ୍ନି ତତ୍ତ୍ୱ (Fire)",
            rulingPlanetOr = "ସୂର୍ଯ୍ୟ (Sun)",
            defaultPredictionOr = "ନେତୃତ୍ୱ ନେବାର କ୍ଷମତା ପ୍ରକାଶ ପାଇବ। ରାଜନୀତି ଓ ପ୍ରଶାସନିକ କାର୍ଯ୍ୟରେ ପ୍ରଭାବ ବୃଦ୍ଧି ପାଇବ। ସରକାରୀ ସ୍ତରରୁ ଅନୁକୂଳ ଫଳ ମିଳିବ।",
            careerFinanceOr = "ଉଚ୍ଚପଦସ୍ଥ ଅଧିକାରୀଙ୍କ ପ୍ରଶଂସା ମିଳିବ। ପୈତୃକ ସମ୍ପତ୍ତିରୁ ଲାଭ ମିଳିବାର ସମ୍ଭାବନା।",
            healthOr = "ହୃଦୟ ଓ ରକ୍ତଚାପ ପ୍ରତି ସାବଧାନ ରୁହନ୍ତୁ। ପ୍ରଭାତ ଭ୍ରମଣ ଉପକାରୀ।",
            familyOr = "ପରିବାରରେ ଆପଣଙ୍କ ମତାମତକୁ ଗୁରୁତ୍ୱ ଦିଆଯିବ। ସନ୍ତାନଙ୍କ ସଫଳତାରେ ଆନନ୍ଦିତ ହେବେ।",
            shubhaBela = "ପ୍ରାତଃ ୦୯:୩୦ - ୧୧:୦୦, ଅପରାହ୍ନ ୦୩:୩୦ - ୦୫:୦୦",
            rahuKala = "ଅପରାହ୍ନ ୦୩:୦୦ - ୦୪:୩୦",
            amritaBela = "ପ୍ରାତଃ ୦୮:୦୦ - ୦୯:୨୦",
            luckyNumber = 1,
            luckyColor = "କନକ ସୁବର୍ଣ୍ଣ ଓ କମଳା (Gold & Bright Orange)",
            luckyGemstone = "ମାଣିକ୍ୟ (Ruby)",
            worshipDeity = "ଭଗବାନ ସୂର୍ଯ୍ୟନାରାୟଣ ଓ ମା' ତାରିଣୀ",
            chantingMantra = "ଓଁ ଘୃଣିଃ ସୂର୍ଯ୍ୟାୟ ନମଃ"
        ),
        RasiMetadata(
            signKey = "kanya",
            signNameEn = "Virgo",
            signNameOr = "କନ୍ୟା",
            westernApiSign = "virgo",
            symbol = "♍",
            elementOr = "ପୃଥିବୀ ତତ୍ତ୍ୱ (Earth)",
            rulingPlanetOr = "ବୁଧ (Mercury)",
            defaultPredictionOr = "କର୍ମନିଷ୍ଠା ଓ ବିଚାରବନ୍ତ ଭାବ ଆପଣଙ୍କୁ ଆଗକୁ ନେବ। ବିବାଦୀୟ ପ୍ରସଙ୍ଗରୁ ଦୂରେଇ ରୁହନ୍ତୁ। ଛାତ୍ରଛାତ୍ରୀଙ୍କ ପାଇଁ ଶୁଭ ଦିବସ।",
            careerFinanceOr = "ହିସାବ କିତାବ ଓ ବାଣିଜ୍ୟରେ ସଠିକତା ରଖନ୍ତୁ। ଅଳ୍ପ ପ୍ରୟାସରେ ଅଧିକ ଲାଭ ମିଳିବ।",
            healthOr = "ଚକ୍ଷୁ ଓ ସ୍ନାୟୁଜନିତ ସମସ୍ୟା ଉପଶମ ହେବ। ସନ୍ତୁଳିତ ଆହାର ଗ୍ରହଣ କରନ୍ତୁ।",
            familyOr = "ଆତ୍ମୀୟ ସ୍ୱଜନଙ୍କ ଆଗମନରେ ଘର ଉତ୍ସବମୁଖର ହେବ। ପରସ୍ପର ମଧ୍ୟରେ ସ୍ନେହ ବୃଦ୍ଧି ପାଇବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୮:୦୦ - ୦୯:୩୦, ସନ୍ଧ୍ୟା ୦୬:୩୦ - ୦୮:୦୦",
            rahuKala = "ମଧ୍ୟାହ୍ନ ୧୨:୦୦ - ୦୧:୩୦",
            amritaBela = "ଅପରାହ୍ନ ୦୨:୧୫ - ୦୩:୪୫",
            luckyNumber = 5,
            luckyColor = "ଗାଢ଼ ସବୁଜ ଓ ଧୂସର (Dark Green & Slate)",
            luckyGemstone = "ମରକତ (Emerald)",
            worshipDeity = "ମହାପ୍ରଭୁ ଶ୍ରୀ ଜଗନ୍ନାଥ",
            chantingMantra = "ଓଁ ନମୋ ଭଗବତେ ବାସୁଦେବାୟ"
        ),
        RasiMetadata(
            signKey = "tula",
            signNameEn = "Libra",
            signNameOr = "ତୁଳା",
            westernApiSign = "libra",
            symbol = "♎",
            elementOr = "ବାୟୁ ତତ୍ତ୍ୱ (Air)",
            rulingPlanetOr = "ଶୁକ୍ର (Venus)",
            defaultPredictionOr = "ସମସ୍ତଙ୍କ ସହ ସନ୍ତୁଳନ ରଖି ଚାଲନ୍ତୁ। କଳା, ସାହିତ୍ୟ ଓ ସଙ୍ଗୀତ ପ୍ରତି ରୁଚି ବଢ଼ିବ। ନୂତନ ବନ୍ଧୁତା ସ୍ଥାପନ ହେବ।",
            careerFinanceOr = "ନ୍ୟାୟିକ ଓ ଭାଗୀଦାରୀ ବ୍ୟବସାୟରେ ପ୍ରଚୁର ଲାଭ। ଦୂର ଯାତ୍ରା ସଫଳ ହେବ।",
            healthOr = "ଚର୍ମ ଓ କଟି ସମ୍ବନ୍ଧୀୟ ଯତ୍ନ ନିଅନ୍ତୁ। ପର୍ଯ୍ୟାପ୍ତ ବିଶ୍ରାମ ଆବଶ୍ୟକ।",
            familyOr = "ଜୀବନସାଥୀଙ୍କ ପୂର୍ଣ୍ଣ ସମର୍ଥନ ମିଳିବ। ଗୃହରେ ମାଙ୍ଗଳିକ କାର୍ଯ୍ୟର ଯୋଜନା ହେବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୭:୪୫ - ୦୯:୧୫, ଅପରାହ୍ନ ୦୩:୦୦ - ୦୪:୩୦",
            rahuKala = "ପ୍ରାତଃ ୦୯:୦୦ - ୧୦:୩୦",
            amritaBela = "ସନ୍ଧ୍ୟା ୦୭:୦୦ - ୦୮:୩୦",
            luckyNumber = 6,
            luckyColor = "ଗୋଲାପୀ ଓ କ୍ରିମ୍ (Rose Pink & Cream)",
            luckyGemstone = "ଧଳା ଜିରକନ କିମ୍ବା ହୀରା (Zircon)",
            worshipDeity = "ମା' ଦୁର୍ଗା ଓ ଲକ୍ଷ୍ମୀନାରାୟଣ",
            chantingMantra = "ଓଁ ଦୁଂ ଦୁର୍ଗାୟୈ ନମଃ"
        ),
        RasiMetadata(
            signKey = "bichha",
            signNameEn = "Scorpio",
            signNameOr = "ବିଛା",
            westernApiSign = "scorpio",
            symbol = "♏",
            elementOr = "ଜଳ ତତ୍ତ୍ୱ (Water)",
            rulingPlanetOr = "ମଙ୍ଗଳ (Mars)",
            defaultPredictionOr = "ଗୁପ୍ତ ଶତ୍ରୁଙ୍କ ଷଡ଼ଯନ୍ତ୍ର ନିଷ୍ଫଳ ହେବ। ନିଜ ଆତ୍ମବିଶ୍ୱାସ ବଳରେ ପ୍ରତିକୂଳ ପରିସ୍ଥିତିକୁ ଜୟ କରିବେ। ଆଧ୍ୟାତ୍ମିକ ସାଧନାରେ ମନ ଲାଗିବ।",
            careerFinanceOr = "ଅନୁସନ୍ଧାନ, ଔଷଧ ଓ କାରିଗରୀ କ୍ଷେତ୍ରରେ ଥିବା ବ୍ୟକ୍ତିଙ୍କ ଉନ୍ନତି। ଅପ୍ରତ୍ୟାଶିତ ଧନ ପ୍ରାପ୍ତିର ଯୋଗ।",
            healthOr = "ଶାରୀରିକ ଶକ୍ତି ଉତ୍ତମ ରହିବ। ଗାଡ଼ି ଚଳାଇବା ବେଳେ ସାବଧାନତା ଅବଲମ୍ବନ କରନ୍ତୁ।",
            familyOr = "କୁଟୁମ୍ବ ମଧ୍ୟରେ କୌଣସି ପୁରୁଣା ମତଭେଦର ଅନ୍ତ ଘଟିବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୮:୧୫ - ୦୯:୪୫, ଅପରାହ୍ନ ୦୨:୦୦ - ୦୩:୩୦",
            rahuKala = "ଅପରାହ୍ନ ୦୧:୩୦ - ୦୩:୦୦",
            amritaBela = "ପ୍ରାତଃ ୦୬:୪୫ - ୦୮:୦୦",
            luckyNumber = 9,
            luckyColor = "ଲାଲ୍ ଓ ଗାଢ଼ ମେରୁନ୍ (Deep Red & Maroon)",
            luckyGemstone = "ରକ୍ତ ମୁଗା (Red Coral)",
            worshipDeity = "ପ୍ରଭୁ ଶ୍ରୀ କାର୍ତ୍ତିକେୟ ଓ ହନୁମାନଜୀ",
            chantingMantra = "ଓଁ ଅଙ୍ଗାରକାୟ ନମଃ"
        ),
        RasiMetadata(
            signKey = "dhanu",
            signNameEn = "Sagittarius",
            signNameOr = "ଧନୁ",
            westernApiSign = "sagittarius",
            symbol = "♐",
            elementOr = "ଅଗ୍ନି ତତ୍ତ୍ୱ (Fire)",
            rulingPlanetOr = "ବୃହସ୍ପତି (Jupiter)",
            defaultPredictionOr = "ଭାଗ୍ୟର ସମ୍ପୂର୍ଣ୍ଣ ସାଥ୍ ମିଳିବ। ଧର୍ମାନୁଷ୍ଠାନ ଓ ତୀର୍ଥାଟନର ଯୋଗ ରହିଛି। ଗୁରୁଜନଙ୍କ ଆଶୀର୍ବାଦ ଆପଣଙ୍କ ସମସ୍ତ ବିଘ୍ନ ଦୂର କରିବ।",
            careerFinanceOr = "ଶିକ୍ଷା, ଆଇନ ଓ ଉଚ୍ଚପଦରେ ସଫଳତା। ଆର୍ଥିକ ସମ୍ବଳ ବୃଦ୍ଧି ପାଇବ।",
            healthOr = "ଜାଙ୍ଘ ଓ ଯକୃତ ସମ୍ବନ୍ଧୀୟ ସତର୍କତା। ସକାଳେ ଶୁଦ୍ଧ ବାୟୁ ସେବନ କରନ୍ତୁ।",
            familyOr = "ପରିବାରରେ ଶାନ୍ତି ଓ ସୌହାର୍ଦ୍ଦ୍ୟ ରହିବ। ଛୋଟ ପିଲାଙ୍କ ସହ ସମୟ ବିତାଇ ଆନନ୍ଦ ପାଇବେ।",
            shubhaBela = "ପ୍ରାତଃ ୦୯:୧୫ - ୧୦:୪୫, ଅପରାହ୍ନ ୦୪:୦୦ - ୦୫:୩୦",
            rahuKala = "ଅପରାହ୍ନ ୦୩:୦୦ - ୦୪:୩୦",
            amritaBela = "ପ୍ରାତଃ ୦୭:୧୫ - ୦୮:୪୫",
            luckyNumber = 3,
            luckyColor = "ହଳଦିଆ ଓ ବେଶରୀ (Bright Yellow & Saffron)",
            luckyGemstone = "ପୁଷ୍କରାଜ (Yellow Sapphire)",
            worshipDeity = "ଜଗତଗୁରୁ ଶ୍ରୀ ଦକ୍ଷିଣାମୂର୍ତ୍ତି ଓ ପ୍ରଭୁ ଶ୍ରୀ ଜଗନ୍ନାଥ",
            chantingMantra = "ଓଁ ବୃଂ ବୃହସ୍ପତୟେ ନମଃ"
        ),
        RasiMetadata(
            signKey = "makara",
            signNameEn = "Capricorn",
            signNameOr = "ମକର",
            westernApiSign = "capricorn",
            symbol = "♑",
            elementOr = "ପୃଥିବୀ ତତ୍ତ୍ୱ (Earth)",
            rulingPlanetOr = "ଶନି (Saturn)",
            defaultPredictionOr = "କଠିନ ପରିଶ୍ରମର ମିଠା ଫଳ ମିଳିବ। କୌଣସି ନୂତନ ଦାୟିତ୍ୱ ଗ୍ରହଣ କରିପାରନ୍ତି। ଅଳସୁଆମି ତ୍ୟାଗ କରି କାର୍ଯ୍ୟରେ ଲାଗି ରୁହନ୍ତୁ।",
            careerFinanceOr = "ନିର୍ମାଣ, ଖଣି, ଇଞ୍ଜିନିୟରିଂ ଓ ଶିଳ୍ପରେ ଲାଭ। ସ୍ଥାବର ସମ୍ପତ୍ତି କ୍ରୟର ସୁଯୋଗ।",
            healthOr = "ଆଣ୍ଠୁଗଣ୍ଠି ବାତ ପ୍ରତି ଧ୍ୟାନ ଦିଅନ୍ତୁ। ସୂର୍ଯ୍ୟସ୍ନାନ ଓ ତୈଳ ମର୍ଦ୍ଦନ ଉପକାରୀ।",
            familyOr = "ପରିବାରର ଗୁରୁ ଦାୟିତ୍ୱ ସୁରୁଖୁରୁରେ ତୁଲାଇବେ। ପିତାଙ୍କ ସହଯୋଗ ମିଳିବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୮:୦୦ - ୦୯:୩୦, ସନ୍ଧ୍ୟା ୦୫:୧୫ - ୦୬:୪୫",
            rahuKala = "ମଧ୍ୟାହ୍ନ ୧୨:୦୦ - ୦୧:୩୦",
            amritaBela = "ରାତ୍ରି ୦୮:୧୫ - ୦୯:୪୫",
            luckyNumber = 8,
            luckyColor = "ନୀଳ ଓ କଳା (Royal Blue & Navy)",
            luckyGemstone = "ଇନ୍ଦ୍ରନୀଳ (Blue Sapphire)",
            worshipDeity = "ପ୍ରଭୁ ଶନିଦେବ ଓ ଭଗବାନ ଶିବ",
            chantingMantra = "ଓଁ ଶଂ ଶନୈଶ୍ଚରାୟ ନମଃ"
        ),
        RasiMetadata(
            signKey = "kumbha",
            signNameEn = "Aquarius",
            signNameOr = "କୁମ୍ଭ",
            westernApiSign = "aquarius",
            symbol = "♒",
            elementOr = "ବାୟୁ ତତ୍ତ୍ୱ (Air)",
            rulingPlanetOr = "ଶନି (Saturn)",
            defaultPredictionOr = "ସମାଜସେବା ଓ ଲୋକହିତକର କାର୍ଯ୍ୟରେ ଆଗ୍ରହ ବଢ଼ିବ। ନୂତନ ଚିନ୍ତାଧାରା ଆପଣଙ୍କୁ ଅନ୍ୟମାନଙ୍କଠାରୁ ଅଲଗା ପରିଚୟ ଦେବ। ମନ ଶାନ୍ତ ରହିବ।",
            careerFinanceOr = "ନୂତନ ପ୍ରକଳ୍ପ ଓ ଉଦ୍ଭାବନରେ ସଫଳତା। ଅଟକି ରହିଥିବା ପ୍ରାପ୍ୟ ହସ୍ତଗତ ହେବ।",
            healthOr = "ରକ୍ତ ସଞ୍ଚାଳନ ଓ ଗୋଇଠି ପ୍ରତି ସଚେତନ ରୁହନ୍ତୁ। ଯୋଗାଭ୍ୟାସ କରନ୍ତୁ।",
            familyOr = "ମିତ୍ର ଓ ଆତ୍ମୀୟମାନଙ୍କ ସହ ହସଖୁସିରେ ସମୟ ବିତିବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୭:୩୦ - ୦୯:୦୦, ଅପରାହ୍ନ ୦୩:୦୦ - ୦୪:୩୦",
            rahuKala = "ଅପରାହ୍ନ ୦୧:୩୦ - ୦୩:୦୦",
            amritaBela = "ସନ୍ଧ୍ୟା ୦୬:୩୦ - ୦୭:୫୦",
            luckyNumber = 4,
            luckyColor = "ଆକାଶୀ ଓ ବାଇଗଣୀ (Cyan & Violet)",
            luckyGemstone = "ନୀଳମ (Blue Sapphire)",
            worshipDeity = "ପ୍ରଭୁ ଶ୍ରୀ ହନୁମାନ ଓ ରୁଦ୍ରାବତାର ଶିବ",
            chantingMantra = "ଓଁ ନମଃ ଶିବାୟ"
        ),
        RasiMetadata(
            signKey = "mina",
            signNameEn = "Pisces",
            signNameOr = "ମୀନ",
            westernApiSign = "pisces",
            symbol = "♓",
            elementOr = "ଜଳ ତତ୍ତ୍ୱ (Water)",
            rulingPlanetOr = "ବୃହସ୍ପତି (Jupiter)",
            defaultPredictionOr = "ଦୟା, କରୁଣା ଓ ଭକ୍ତିଭାବ ବୃଦ୍ଧି ପାଇବ। କୌଣସି ଅଭାବୀ ଲୋକଙ୍କୁ ସାହାଯ୍ୟ କରି ଆତ୍ମତୃପ୍ତି ଲାଭ କରିବେ। ସମସ୍ତ କାର୍ଯ୍ୟରେ ଭଗବତ୍ କୃପା ରହିବ।",
            careerFinanceOr = "ଆଧ୍ୟାତ୍ମିକ, କଳା ଓ ପରାମର୍ଶଦାତା କାର୍ଯ୍ୟରେ ଉନ୍ନତି। ବିଦେଶ ସମ୍ପର୍କରୁ ଲାଭ ମିଳିପାରେ।",
            healthOr = "ପାଦ ଓ ନିଦ୍ରାହୀନତା ପ୍ରତି ଯତ୍ନବାନ ରୁହନ୍ତୁ। ଧ୍ୟାନ ଓ ଜପ ଉପକାରୀ।",
            familyOr = "ପରିବାରରେ ଆନନ୍ଦ ଓ ସନ୍ତୋଷ ରହିବ। ଶୁଭ ଖବର ପ୍ରାପ୍ତ ହେବ।",
            shubhaBela = "ପ୍ରାତଃ ୦୮:୩୦ - ୧୦:୦୦, ଅପରାହ୍ନ ୦୨:୩୦ - ୦୪:୦୦",
            rahuKala = "ପ୍ରାତଃ ୧୦:୩୦ - ୧୨:୦୦",
            amritaBela = "ସନ୍ଧ୍ୟା ୦୭:୩୦ - ୦୯:୦୦",
            luckyNumber = 7,
            luckyColor = "ହଳଦିଆ ଓ ସୁନେଲୀ (Yellow & Pale Gold)",
            luckyGemstone = "ପୀତ ପୋଖରାଜ (Yellow Sapphire)",
            worshipDeity = "ପ୍ରଭୁ ଶ୍ରୀ ଜଗନ୍ନାଥ ଓ ଶ୍ରୀ ବଳଭଦ୍ର",
            chantingMantra = "ଓଁ ନମୋ ନାରାୟଣାୟ"
        )
    )

    /**
     * Initializes base horoscopes in database if empty.
     */
    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        val existing = dao.getAllHoroscopesSync()
        if (existing.isEmpty()) {
            val todayStr = getTodayDateString()
            val initialList = rasiDefinitions.map { meta ->
                OdiaHoroscopeEntity(
                    signKey = meta.signKey,
                    signNameEn = meta.signNameEn,
                    signNameOr = meta.signNameOr,
                    symbol = meta.symbol,
                    elementOr = meta.elementOr,
                    rulingPlanetOr = meta.rulingPlanetOr,
                    predictionEn = "Favorable planetary alignments for ${meta.signNameEn} today bringing progressive results in career and harmony at home.",
                    predictionOr = meta.defaultPredictionOr,
                    careerFinanceOr = meta.careerFinanceOr,
                    healthOr = meta.healthOr,
                    familyOr = meta.familyOr,
                    shubhaBela = meta.shubhaBela,
                    rahuKala = meta.rahuKala,
                    amritaBela = meta.amritaBela,
                    luckyNumber = meta.luckyNumber,
                    luckyColor = meta.luckyColor,
                    luckyGemstone = meta.luckyGemstone,
                    worshipDeity = meta.worshipDeity,
                    chantingMantra = meta.chantingMantra,
                    date = todayStr,
                    lastUpdatedTimestamp = System.currentTimeMillis()
                )
            }
            dao.insertHoroscopes(initialList)
        }
    }

    /**
     * Fetches live daily horoscope data from external astrology API.
     * Enriches predictions and updates Room database.
     */
    suspend fun refreshDailyHoroscopes(): Result<Unit> = withContext(Dispatchers.IO) {
        _isRefreshing.value = true
        try {
            ensureInitialized()
            val todayStr = getTodayDateString()
            val updatedEntities = mutableListOf<OdiaHoroscopeEntity>()

            // Query live horoscope for each of the 12 zodiac signs
            for (meta in rasiDefinitions) {
                var apiHoroscopeText: String? = null
                try {
                    // Try Primary Horoscope App API
                    val response = api.getDailyHoroscope(sign = meta.westernApiSign, day = "TODAY")
                    if (response.success == true && !response.data?.horoscopeData.isNullOrBlank()) {
                        apiHoroscopeText = response.data?.horoscopeData?.trim()
                    }
                } catch (_: Exception) {
                    // Try Secondary Ohmanda API
                    try {
                        val ohmanda = api.getOhmandaHoroscope(sign = meta.westernApiSign)
                        if (!ohmanda.horoscope.isNullOrBlank()) {
                            apiHoroscopeText = ohmanda.horoscope.trim()
                        }
                    } catch (_: Exception) {
                        // Fallback will be applied
                    }
                }

                val finalPredictionEn = apiHoroscopeText ?: "Harmonious planetary transits for ${meta.signNameEn} inspire creativity and steady advancement today."
                val finalPredictionOr = if (!apiHoroscopeText.isNullOrBlank()) {
                    // Prepend customized Odia blessing to external astrology insight
                    "${meta.defaultPredictionOr}\n\n[ଜ୍ୟୋତିଷ ଗଣନା]: ${meta.signNameOr} ରାଶିର ଗ୍ରହଚଳନ ଅନୁସାରେ ଆଜିର ଦିନଟି ଫଳପ୍ରଦ ରହିବ।"
                } else {
                    meta.defaultPredictionOr
                }

                updatedEntities.add(
                    OdiaHoroscopeEntity(
                        signKey = meta.signKey,
                        signNameEn = meta.signNameEn,
                        signNameOr = meta.signNameOr,
                        symbol = meta.symbol,
                        elementOr = meta.elementOr,
                        rulingPlanetOr = meta.rulingPlanetOr,
                        predictionEn = finalPredictionEn,
                        predictionOr = finalPredictionOr,
                        careerFinanceOr = meta.careerFinanceOr,
                        healthOr = meta.healthOr,
                        familyOr = meta.familyOr,
                        shubhaBela = meta.shubhaBela,
                        rahuKala = meta.rahuKala,
                        amritaBela = meta.amritaBela,
                        luckyNumber = meta.luckyNumber,
                        luckyColor = meta.luckyColor,
                        luckyGemstone = meta.luckyGemstone,
                        worshipDeity = meta.worshipDeity,
                        chantingMantra = meta.chantingMantra,
                        date = todayStr,
                        lastUpdatedTimestamp = System.currentTimeMillis()
                    )
                )
            }

            dao.insertHoroscopes(updatedEntities)
            _lastSyncTime.value = getFormattedTimestamp()
            _syncMessage.value = "ଦୈନିକ ରାଶିଫଳ ସଫଳତାର ସହ ଅପଡେଟ୍ ହୋଇଛି (Synced from Astrology API)"
            Result.success(Unit)
        } catch (e: Exception) {
            _syncMessage.value = "ଅଫଲାଇନ୍ ରାଶିଫଳ ସକ୍ରିୟ ଅଛି (${e.localizedMessage ?: "Network cached"})"
            Result.failure(e)
        } finally {
            _isRefreshing.value = false
        }
    }

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getFormattedTimestamp(): String {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return sdf.format(Date())
    }

    companion object {
        @Volatile
        private var INSTANCE: OdiaHoroscopeRepository? = null

        fun getInstance(context: Context): OdiaHoroscopeRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OdiaHoroscopeRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
