package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.OdiaFestivalDao
import com.example.data.local.OdiaFestivalEntity
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
 * Repository for Odia Festivals & Cultural Dates in Balasore.
 *
 * Implements local Room Database caching for offline access, dynamic days-remaining
 * calculation based on calendar events, favorite starring, and automated periodic updates.
 */
class OdiaFestivalRepository private constructor(
    private val context: Context,
    private val dao: OdiaFestivalDao = AppDatabase.getInstance(context).odiaFestivalDao()
) {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _lastSyncTime = MutableStateFlow(getFormattedTime())
    val lastSyncTime = _lastSyncTime.asStateFlow()

    private val _autoUpdateCycle = MutableStateFlow(1)
    val autoUpdateCycle = _autoUpdateCycle.asStateFlow()

    /**
     * Reactive stream of all festivals ordered by days remaining.
     */
    val allFestivalsFlow: Flow<List<OdiaFestivalEntity>> = dao.getAllFestivalsFlow()

    /**
     * Reactive stream of user-starred festivals.
     */
    val starredFestivalsFlow: Flow<List<OdiaFestivalEntity>> = dao.getStarredFestivalsFlow()

    /**
     * Filter festivals by category.
     */
    fun getFestivalsByCategoryFlow(category: String): Flow<List<OdiaFestivalEntity>> {
        return if (category == "All" || category == "ସମସ୍ତ") {
            dao.getAllFestivalsFlow()
        } else {
            dao.getFestivalsByCategoryFlow(category)
        }
    }

    /**
     * Search festivals by title or venue.
     */
    fun searchFestivalsFlow(query: String): Flow<List<OdiaFestivalEntity>> {
        return dao.searchFestivalsFlow(query.trim())
    }

    /**
     * Master dataset of authentic Balasore and Odia festivals.
     */
    data class FestivalSeed(
        val festivalId: String,
        val titleEn: String,
        val titleOr: String,
        val monthPeriodEn: String,
        val monthPeriodOr: String,
        val odiaTithi: String,
        val venueEn: String,
        val venueOr: String,
        val category: String,
        val eventMonth: Int, // Calendar.MONTH (0-indexed)
        val eventDay: Int,
        val significanceEn: String,
        val significanceOr: String,
        val ritualsEn: String,
        val ritualsOr: String,
        val specialTransitEn: String,
        val specialTransitOr: String,
        val prasadSpecialty: String
    )

    private val festivalSeeds = listOf(
        FestivalSeed(
            festivalId = "chandaneswar_chadak",
            titleEn = "Chandaneswar Chadak Mela & Uda Yatra",
            titleOr = "ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ମେଳା ଓ ଉଡ଼ା ଯାତ୍ରା",
            monthPeriodEn = "Chaitra (April)",
            monthPeriodOr = "ଚୈତ୍ର ମାସ (ଏପ୍ରିଲ)",
            odiaTithi = "ଚୈତ୍ର ଶୁକ୍ଳ ଚତୁର୍ଦ୍ଦଶୀ / ମହାବିଷୁବ ସଂକ୍ରାନ୍ତି",
            venueEn = "Chandaneswar Shiva Temple, Bhograi Block",
            venueOr = "ଚନ୍ଦନେଶ୍ୱର ଶୈବପୀଠ, ଭୋଗରାଇ ବ୍ଲକ",
            category = "Folk & Fair",
            eventMonth = Calendar.APRIL,
            eventDay = 14,
            significanceEn = "Over 100,000 Patuas observe 13 days of rigorous penance, walking on glowing fire embers and performing aerial pole swings to honor Lord Shiva.",
            significanceOr = "ଲକ୍ଷାଧିକ ଭକ୍ତ ଓ ପାଟୁଆ ନିଆଁ ଉପରେ ଚାଲି, କଣ୍ଟା ଉପରେ ଶୋଇ ଏବଂ ଉଡ଼ା କାଠରେ ଝୁଲି ପ୍ରଭୁ ଚନ୍ଦନେଶ୍ୱରଙ୍କ କଠୋର ବ୍ରତ ପାଳନ କରନ୍ତି।",
            ritualsEn = "Nia Chali (Fire Walk), Kanta Phoda (Thorn Bed), Uda Parba (Aerial Pole Swing)",
            ritualsOr = "ନିଆଁ ଚାଲି, କଣ୍ଟା ଫୋଡ଼ା, କାମିକା ବ୍ରତ, ଆକାଶ ଉଡ଼ା ପର୍ବ ଓ ଘେରା ନୃତ୍ୟ",
            specialTransitEn = "Special round-the-clock OSRTC and CRUT pilgrim shuttles from Balasore Railway Station & Jaleswar.",
            specialTransitOr = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ ଓ ଜଳେଶ୍ୱରରୁ ସ୍ୱତନ୍ତ୍ର ସରକାରୀ ବସ୍ ଓ ଟୋଟୋ ସେବା ଉପଲବ୍ଧ।",
            prasadSpecialty = "ଶୀତଳ ଭୋଗ, ବେଲପତ୍ର, ଧୂପ ଓ ଥଣ୍ଡା ଦହି ପଣା"
        ),
        FestivalSeed(
            festivalId = "balaramgadi_boita_bandana",
            titleEn = "Balaramgadi Boita Bandana Maritime Regatta",
            titleOr = "ବଳରାମଗଡ଼ି ବୋଇତ ବନ୍ଦାଣ ଉତ୍ସବ",
            monthPeriodEn = "Kartika (November)",
            monthPeriodOr = "କାର୍ତ୍ତିକ ମାସ (ନଭେମ୍ବର)",
            odiaTithi = "କାର୍ତ୍ତିକ ପୂର୍ଣ୍ଣିମା (Kartika Purnima)",
            venueEn = "Balaramgadi Estuary / Budhabalanga Sea Mouth",
            venueOr = "ବଳରାମଗଡ଼ି ମୁହାଣ, ଚାନ୍ଦିପୁର ବେଳାଭୂମି ନିକଟ",
            category = "Maritime & Beach",
            eventMonth = Calendar.NOVEMBER,
            eventDay = 15,
            significanceEn = "Dawn maritime celebration where thousands launch miniature handcrafted boats with ghee lamps into the sea, commemorating ancient Kalinga maritime voyages to Bali & Sumatra.",
            significanceOr = "ପ୍ରାଚୀନ କଳିଙ୍ଗର ସାଧବ ପୁଅମାନଙ୍କ ସାମୁଦ୍ରିକ ବାଣିଜ୍ୟ ଯାତ୍ରା ସ୍ମୃତିରେ ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ମୁହାଣରେ ବୋଇତ ଭସା ଓ ଦୀପଦାନ।",
            ritualsEn = "Aa-Ka-Ma-Bai Chanting, Betel Leaf Offering, Sunrise Miniature Boat Launching",
            ritualsOr = "'ଆ-କା-ମା-ବୈ' ଗାନ, କଦଳୀ ପଟୁଆରେ ଘିଅ ଦୀପ ସ୍ଥାପନ ଓ ପ୍ରଭାତ ସ୍ନାନ",
            specialTransitEn = "Direct town shuttle buses and auto rickshaws connecting Balasore Town Hall to Balaramgadi Jetty.",
            specialTransitOr = "ବାଲେଶ୍ୱର ଫାନ୍ଦି ଛକ ଓ ଷ୍ଟେସନରୁ ବଳରାମଗଡ଼ି ଜେଟି ପର୍ଯ୍ୟନ୍ତ ସିଧାସଳଖ ଟାଉନ୍ ବସ୍।",
            prasadSpecialty = "ହବିଷ୍ୟ ଅନ୍ନ, ଖଇ, ମୁଆଁ ଓ କଦଳୀ ଭୋଗ"
        ),
        FestivalSeed(
            festivalId = "remuna_khirachora_chandan",
            titleEn = "Remuna Khirachora Chandan Yatra & Jhulan Utsav",
            titleOr = "ରେମୁଣା କ୍ଷୀରଚୋରା ଚନ୍ଦନ ଯାତ୍ରା ଓ ଝୁଲଣ",
            monthPeriodEn = "Baisakha - Jyestha (May - June)",
            monthPeriodOr = "ବୈଶାଖ - ଜ୍ୟେଷ୍ଠ (ମେ - ଜୁନ୍)",
            odiaTithi = "ଅକ୍ଷୟ ତୃତୀୟା ଠାରୁ ୨୧ ଦିନ ଚନ୍ଦନ ଚାପ",
            venueEn = "Khirachora Gopinath Temple, Remuna",
            venueOr = "ଶ୍ରୀ କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ଦେବସ୍ଥଳୀ, ରେମୁଣା",
            category = "Temple Festival",
            eventMonth = Calendar.MAY,
            eventDay = 10,
            significanceEn = "Sacred 21-day festival where Lord Gopinatha is anointed with aromatic sandalwood paste and taken on evening boat cruises on the sacred Nanda Pokhari tank.",
            significanceOr = "ମାଧବେନ୍ଦ୍ର ପୁରୀଙ୍କ ଭକ୍ତି ପ୍ରେମରେ ପ୍ରଭୁ ଗୋପୀନାଥ କ୍ଷୀର ଚୋରି କରିଥିବା ପବିତ୍ର ସ୍ଥାନ। ୨୧ ଦିନ ଧରି ଚନ୍ଦନ ଚାପ ନୌକା ବିହାର।",
            ritualsEn = "Sandalwood Paste Anointing, Nanda Pokhari Swan Boat Cruise, Special Night Arati",
            ritualsOr = "ଶ୍ରୀଅଙ୍ଗେ ଚନ୍ଦନ ଲେପନ, ନନ୍ଦ ପୋଖରୀରେ ହଂସ ଚାପ ନୌକା ବିହାର ଓ ନିଶାର୍ଦ୍ଧ ଆଳତି",
            specialTransitEn = "City buses every 10 minutes from Remuna Golei on NH-16 to the temple gate.",
            specialTransitOr = "ରେମୁଣା ଗୋଲେଇ NH-16 ଠାରୁ ପ୍ରତି ୧୦ ମିନିଟରେ ସିଟି ଅଟୋ ଓ ବସ୍ ସେବା।",
            prasadSpecialty = "ଅମୃତ କେଳି କ୍ଷୀରି (Famous thick condensed milk pot)"
        ),
        FestivalSeed(
            festivalId = "nilagiri_makara_mela",
            titleEn = "Nilagiri Makara Mela & Tribal Archery Carnival",
            titleOr = "ନୀଳଗିରି ମକର ମେଳା ଓ ଧନୁର୍ବିଦ୍ୟା ମହୋତ୍ସବ",
            monthPeriodEn = "Magha (January)",
            monthPeriodOr = "ମାଘ ମାସ (ଜାନୁଆରୀ)",
            odiaTithi = "ମାଘ ମକର ସଂକ୍ରାନ୍ତି (Makara Sankranti)",
            venueEn = "Swarnachuda Hills & Nilagiri Jagannath Temple",
            venueOr = "ସ୍ୱର୍ଣ୍ଣଚୂଡ଼ ପାହାଡ଼ ପାଦଦେଶ, ନୀଳଗିରି",
            category = "Harvest & Agro",
            eventMonth = Calendar.JANUARY,
            eventDay = 14,
            significanceEn = "Historic cultural congregation featuring traditional Santhal tribal archery competitions, Chhau folk dances, and sacred Makara Chaula offerings.",
            significanceOr = "ସ୍ୱର୍ଣ୍ଣଚୂଡ଼ ପାହାଡ଼ ପାଦଦେଶରେ ଆଦିବାସୀ ସାନ୍ତାଳୀ ଧନୁତୀର ପ୍ରତିଯୋଗିତା, ଛଉ ନୃତ୍ୟ ଓ ମକର ଚାଉଳ ଭୋଗ।",
            ritualsEn = "Makara Chaula Naivedya, Royal Temple Pahi Procession, Archery Duels",
            ritualsOr = "ମକର ଚାଉଳ ନୈବେଦ୍ୟ, ରାଜବାଟୀ ପହି ଶୋଭାଯାତ୍ରା ଓ ତୀରନ୍ଦାଜି ପ୍ରଦର୍ଶନ",
            specialTransitEn = "High-frequency mini buses connecting Sahadevkhunta Bus Terminal to Nilagiri Town.",
            specialTransitOr = "ସହଦେବଖୁଣ୍ଟା ବସ୍ ଟର୍ମିନାଲରୁ ନୀଳଗିରି ସିଧାସଳଖ ମିନି ବସ୍।",
            prasadSpecialty = "ମକର ଚାଉଳ (Rice, coconut, cottage cheese & fruit blend)"
        ),
        FestivalSeed(
            festivalId = "chandipur_beach_festival",
            titleEn = "Chandipur Golden Beach Festival & Maritime Expo",
            titleOr = "ଚାନ୍ଦିପୁର ସୁବର୍ଣ୍ଣ ବେଳାଭୂମି ମହୋତ୍ସବ",
            monthPeriodEn = "Pausha (January)",
            monthPeriodOr = "ପୌଷ ମାସ (ଜାନୁଆରୀ)",
            odiaTithi = "ପୌଷ ପୂର୍ଣ୍ଣିମା ଶୀତକାଳୀନ ମହୋତ୍ସବ",
            venueEn = "Chandipur Beach Promenade & Defense Hub",
            venueOr = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି ପ୍ରମୋନାଡ୍",
            category = "Maritime & Beach",
            eventMonth = Calendar.JANUARY,
            eventDay = 22,
            significanceEn = "5-day tourism festival featuring international sand art installations, DRDO missile pavilion, cultural Odissi evenings, and coastal kite flying.",
            significanceOr = "ବାଲୁକା କଳା ପ୍ରଦର୍ଶନୀ, DRDO କ୍ଷେପଣାସ୍ତ୍ର ମଡେଲ ପ୍ୟାଭିଲିୟନ, ଓଡ଼ିଶୀ ନୃତ୍ୟ ସନ୍ଧ୍ୟା ଓ ସମୁଦ୍ର ମାଛ ଉତ୍ସବ।",
            ritualsEn = "Sand Art Tributes, Evening Cultural Stage, Defense Heritage Exhibition",
            ritualsOr = "ବାଲୁକା ଶିଳ୍ପ ପ୍ରତିଯୋଗିତା, ଆତସବାଜି ପ୍ରଦର୍ଶନ ଓ ସାଂସ୍କୃତିକ କାର୍ଯ୍ୟକ୍ରମ",
            specialTransitEn = "Express tourist shuttles operating every 15 minutes between Balasore Railway Station and Chandipur.",
            specialTransitOr = "ବାଲେଶ୍ୱର ଷ୍ଟେସନରୁ ଚାନ୍ଦିପୁର ପ୍ରତି ୧୫ ମିନିଟରେ ସ୍ୱତନ୍ତ୍ର ଟୁରିଷ୍ଟ ସଟଲ୍ ବସ୍।",
            prasadSpecialty = "ସାମୁଦ୍ରିକ ଖାଦ୍ୟ ମେଳା, ବାଲେଶ୍ୱରୀୟ ରସାବଳି ଓ ଛେନାଝିଲି"
        ),
        FestivalSeed(
            festivalId = "panchalingeswar_jagar",
            titleEn = "Panchalingeswar Maha Shivaratri Jagar Mela",
            titleOr = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ମହାଶିବରାତ୍ରି ଜାଗର ମେଳା",
            monthPeriodEn = "Phalguna (March)",
            monthPeriodOr = "ଫାଲ୍ଗୁନ ମାସ (ମାର୍ଚ୍ଚ)",
            odiaTithi = "ଫାଲ୍ଗୁନ କୃଷ୍ଣ ଚତୁର୍ଦ୍ଦଶୀ (Maha Shivaratri)",
            venueEn = "Panchalingeswar Hill Perennial Stream, Nilagiri",
            venueOr = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପ୍ରାକୃତିକ ଝରଣା ପୀଠ",
            category = "Temple Festival",
            eventMonth = Calendar.MARCH,
            eventDay = 8,
            significanceEn = "Night-long hill festival where devotees touch the 5 naturally submerged rock Shiva Lingas under flowing mountain spring water and watch the Mahadipa climb.",
            significanceOr = "ପାହାଡ଼ର ସ୍ୱଚ୍ଛ ଝରଣା ଜଳରେ ନିମଗ୍ନ ୫ଟି ଶିବଲିଙ୍ଗଙ୍କ ପବିତ୍ର ସ୍ପର୍ଶ ଦର୍ଶନ ଓ ପାହାଡ଼ ଶୀର୍ଷରେ ମହାଦୀପ ଉଠାଣ।",
            ritualsEn = "Stream Jalabhisheka, Night-long Jagar Fasting, Midnight Mahadipa Rising",
            ritualsOr = "ଝରଣା ଜଳରେ ଜଳାଭିଷେକ, ରାତ୍ରି ଉଜାଗର ଓ ମଧ୍ୟରାତ୍ରିରେ ମହାଦୀପ ଉଠାଣ",
            specialTransitEn = "Special night buses and hill sumos from Sergarh Toll Chhak and Balasore Junction.",
            specialTransitOr = "ସେରଗଡ଼ ଟୋଲ୍ ଗେଟ୍ ଓ ବାଲେଶ୍ୱରରୁ ସାରାରାତି ସ୍ୱତନ୍ତ୍ର ବସ୍ ଓ ମ୍ୟାକ୍ସି କ୍ୟାବ୍।",
            prasadSpecialty = "ଘୋଳଦହି, ଭଙ୍ଗ ପଣା, କଦଳୀ ଓ ବେଲପତ୍ର ଭୋଗ"
        ),
        FestivalSeed(
            festivalId = "balasore_zilla_mahotsav",
            titleEn = "Baleswar Zilla Mahotsav & Expo",
            titleOr = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ମହୋତ୍ସବ ଓ ପ୍ରଦର୍ଶନୀ",
            monthPeriodEn = "Margasira (December)",
            monthPeriodOr = "ମାର୍ଗଶୀର ମାସ (ଡିସେମ୍ବର)",
            odiaTithi = "ମାର୍ଗଶୀର ଶୁକ୍ଳପକ୍ଷ ସପ୍ତାହବ୍ୟାପୀ ମହୋତ୍ସବ",
            venueEn = "Gandhi Smruti Bhawan & ITI Grounds, Balasore Town",
            venueOr = "ଗାନ୍ଧୀ ସ୍ମୃତି ଭବନ ଓ ITI ପଡ଼ିଆ, ବାଲେଶ୍ୱର",
            category = "Folk & Fair",
            eventMonth = Calendar.DECEMBER,
            eventDay = 24,
            significanceEn = "7-day district grand fair highlighting Balasore's handlooms, Nilagiri Dokra metalcraft, Sabai grass goods, and regional folk theater.",
            significanceOr = "୭ ଦିନ ଧରି ବାଲେଶ୍ୱରର କୃଷି, ସବାଇ ଘାସ ହସ୍ତଶିଳ୍ପ, ନୀଳଗିରି ଡୋକ୍ରା ଓ ନାଟକର ମହାସମାରୋହ।",
            ritualsEn = "Fakir Mohan Literary Colloquium, Pallishree Mela Artisans Bazaar",
            ritualsOr = "ଫକୀର ମୋହନ ସାହିତ୍ୟ ଆସର, ପଲ୍ଲୀଶ୍ରୀ ମେଳା ଓ କୁଟୀର ଶିଳ୍ପ ବିକ୍ରୟ",
            specialTransitEn = "Located inside town; easily accessible from anywhere in Balasore.",
            specialTransitOr = "ବାଲେଶ୍ୱର ସହରର କେନ୍ଦ୍ରସ୍ଥଳରେ ଅବସ୍ଥିତ, ଚାଲି ଚାଲି ବା ଟୋଟୋରେ ସୁଗମ୍ୟ।",
            prasadSpecialty = "ପାରମ୍ପରିକ ଛେନାପୋଡ଼, ଉଖୁଡ଼ା ଓ କୋରା"
        ),
        FestivalSeed(
            festivalId = "emami_jagannath_ratha_yatra",
            titleEn = "Emami Jagannath Temple Ratha Yatra",
            titleOr = "ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର ରଥଯାତ୍ରା",
            monthPeriodEn = "Ashadha (July)",
            monthPeriodOr = "ଆଷାଢ଼ ମାସ (ଜୁଲାଇ)",
            odiaTithi = "ଆଷାଢ଼ ଶୁକ୍ଳ ଦ୍ୱିତୀୟା (Ratha Yatra)",
            venueEn = "Emami Jagannath Temple, Remuna Road",
            venueOr = "ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର ପରିସର, ରେମୁଣା ରୋଡ଼",
            category = "Temple Festival",
            eventMonth = Calendar.JULY,
            eventDay = 17,
            significanceEn = "Magnificent chariot pulling festival mirroring Puri Dham rituals at Balasore's architectural gem, attracting hundreds of thousands from across the region.",
            significanceOr = "ଶ୍ରୀକ୍ଷେତ୍ର ପୁରୀ ଢାଞ୍ଚାରେ ତିନି ରଥ ଟଣା, ଛେରା ପହଁରା ଓ ଗୁଣ୍ଡିଚା ମନ୍ଦିର ଯାତ୍ରାର ଭବ୍ୟ ଉତ୍ସବ।",
            ritualsEn = "Chera Pahanra, Taladhwaja, Darpadalana & Nandighosha Chariot Pulling",
            ritualsOr = "ପହଣ୍ଡି ବିଜେ, ଛେରା ପହଁରା, ତିନି ରଥ ଟଣା ଓ ବାହୁଡ଼ା ଯାତ୍ରା",
            specialTransitEn = "Designated parking hubs along Remuna bypass with feeder auto rickshaws.",
            specialTransitOr = "NH-16 ରେମୁଣା ବାଇପାସରେ ସ୍ୱତନ୍ତ୍ର ପାର୍କିଂ ଓ ଫିଡର ଟୋଟୋ ସେବା।",
            prasadSpecialty = "ମହାପ୍ରସାଦ, ଅବଢ଼ା ଓ ରଥ ଖଜା"
        ),
        FestivalSeed(
            festivalId = "bhusandeswar_rudrabhisheka",
            titleEn = "Bhusandeswar Maha Rudrabhisheka Mela",
            titleOr = "ଭୂଷଣ୍ଡେଶ୍ୱର ମହାରୁଦ୍ରାଭିଷେକ ମେଳା",
            monthPeriodEn = "Shravana (August)",
            monthPeriodOr = "ଶ୍ରାବଣ ମାସ (ଅଗଷ୍ଟ)",
            odiaTithi = "ଶ୍ରାବଣ ଶୁକ୍ଳ ସୋମବାର (Shravan Mondays)",
            venueEn = "Bhusandeswar Temple, Subarnarekha River Bank, Jaleswar",
            venueOr = "ଭୂଷଣ୍ଡେଶ୍ୱର ଶୈବପୀଠ, ସୁବର୍ଣ୍ଣରେଖା ତଟ, ଜଳେଶ୍ୱର",
            category = "Temple Festival",
            eventMonth = Calendar.AUGUST,
            eventDay = 12,
            significanceEn = "Pilgrims carry sacred river waters on foot to bathe Asia's second-largest monolithic black granite Shiva Lingam (12 ft diameter).",
            significanceOr = "ଏସିଆର ଦ୍ୱିତୀୟ ବୃହତ୍ତମ କଳାମୁଗୁନି ପଥର ଶିବଲିଙ୍ଗରେ ସୁବର୍ଣ୍ଣରେଖା ନଦୀର ପବିତ୍ର ଜଳାଭିଷେକ।",
            ritualsEn = "Bol Bom Kanwariya Procession, Milk & Honey Abhisheka, Maha Arati",
            ritualsOr = "ବୋଲବମ୍ କାଉଡ଼ିଆ ପଦଯାତ୍ରା, କ୍ଷୀର ଓ ମହୁ ଅଭିଷେକ, ଅଖଣ୍ଡ ନାମ ସଂକୀର୍ତ୍ତନ",
            specialTransitEn = "Feeder tempos and buses from Jaleswar Railway Station (18 km).",
            specialTransitOr = "ଜଳେଶ୍ୱର ରେଳ ଷ୍ଟେସନରୁ ସିଧାସଳଖ ଟେମ୍ପୋ ଓ ବସ୍ ଯୋଗାଯୋଗ।",
            prasadSpecialty = "ଗଙ୍ଗାଜଳ, ଶ୍ରୀଫଳ, ପଞ୍ଚାମୃତ ଓ ଲଡୁ ଭୋଗ"
        ),
        FestivalSeed(
            festivalId = "bagha_jatin_memorial",
            titleEn = "Bagha Jatin Balasore Martyrdom Memorial",
            titleOr = "ବାଘା ଯତୀନ ବଳିଦାନ ସ୍ମୃତି ମେଳା",
            monthPeriodEn = "Bhadrava (September)",
            monthPeriodOr = "ଭାଦ୍ରବ ମାସ (ସେପ୍ଟେମ୍ବର)",
            odiaTithi = "୯ ସେପ୍ଟେମ୍ବର ସହିଦ ଦିବସ",
            venueEn = "Chasakhand Memorial Ground, Khantapada",
            venueOr = "ଚାଷାଖଣ୍ଡ ସ୍ମୃତି ପୀଠ, ଖନ୍ତାପଡ଼ା",
            category = "Heritage & Memorial",
            eventMonth = Calendar.SEPTEMBER,
            eventDay = 9,
            significanceEn = "Annual patriotic commemoration honoring revolutionary freedom fighter Jatindranath Mukherjee (Bagha Jatin) who fought British forces at Chasakhand in 1915.",
            significanceOr = "୧୯୧୫ ମସିହାରେ ବ୍ରିଟିଶ ସେନା ବିରୁଦ୍ଧରେ ପ୍ରାଣବଳି ଦେଇଥିବା ବିପ୍ଳବୀ ବାଘା ଯତୀନଙ୍କ ଅମର ସ୍ମୃତିରେ ବାର୍ଷିକ ସଭା।",
            ritualsEn = "Wreath Laying, Freedom Trail March, Youth Martial Arts Exhibition",
            ritualsOr = "ଶ୍ରଦ୍ଧାଞ୍ଜଳି ଅର୍ପଣ, ସ୍ୱାଧୀନତା ପଦଯାତ୍ରା ଓ ଯୁବ କ୍ରୀଡ଼ା ପ୍ରତିଯୋଗିତା",
            specialTransitEn = "Located right on NH-16 at Khantapada; buses stop directly at Chasakhand gate.",
            specialTransitOr = "NH-16 ଖନ୍ତାପଡ଼ା ଛକ ନିକଟରେ ଅବସ୍ଥିତ, ସମସ୍ତ ବସ୍ ସିଧାସଳଖ ରହଣି କରେ।",
            prasadSpecialty = "ଦେଶାତ୍ମବୋଧକ ସ୍ମୃତି ସଭା, ସେବା ସତ୍କାର ଓ ମିଷ୍ଟାନ୍ନ ବଣ୍ଟନ"
        )
    )

    /**
     * Initializes base festival records in Room database if table is empty,
     * or updates remaining days based on current system time.
     */
    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        val existing = dao.getAllFestivalsSync()
        val now = Calendar.getInstance()

        if (existing.isEmpty()) {
            val entities = festivalSeeds.map { seed ->
                val daysRemaining = calculateDaysRemaining(seed.eventMonth, seed.eventDay, now)
                val eventDateStr = "${seed.eventDay} ${getMonthName(seed.eventMonth)} ${now.get(Calendar.YEAR)}"
                OdiaFestivalEntity(
                    festivalId = seed.festivalId,
                    titleEn = seed.titleEn,
                    titleOr = seed.titleOr,
                    monthPeriodEn = seed.monthPeriodEn,
                    monthPeriodOr = seed.monthPeriodOr,
                    odiaTithi = seed.odiaTithi,
                    venueEn = seed.venueEn,
                    venueOr = seed.venueOr,
                    category = seed.category,
                    eventDate = eventDateStr,
                    daysRemaining = daysRemaining,
                    significanceEn = seed.significanceEn,
                    significanceOr = seed.significanceOr,
                    ritualsEn = seed.ritualsEn,
                    ritualsOr = seed.ritualsOr,
                    specialTransitEn = seed.specialTransitEn,
                    specialTransitOr = seed.specialTransitOr,
                    prasadSpecialty = seed.prasadSpecialty,
                    isStarred = seed.festivalId in listOf("chandaneswar_chadak", "balaramgadi_boita_bandana", "remuna_khirachora_chandan"),
                    lastUpdatedTimestamp = System.currentTimeMillis()
                )
            }
            dao.insertFestivals(entities)
        } else {
            // Update live days remaining for all records
            for (item in existing) {
                val seed = festivalSeeds.find { it.festivalId == item.festivalId }
                if (seed != null) {
                    val updatedDays = calculateDaysRemaining(seed.eventMonth, seed.eventDay, now)
                    dao.updateDaysRemaining(item.festivalId, updatedDays, System.currentTimeMillis())
                }
            }
        }
    }

    /**
     * Re-calculates countdown days and simulates live cultural festival telemetry refresh.
     */
    suspend fun refreshFestivalDates(): Result<Unit> = withContext(Dispatchers.IO) {
        _isRefreshing.value = true
        try {
            ensureInitialized()
            val existing = dao.getAllFestivalsSync()
            val now = Calendar.getInstance()

            for (item in existing) {
                val seed = festivalSeeds.find { it.festivalId == item.festivalId }
                if (seed != null) {
                    val updatedDays = calculateDaysRemaining(seed.eventMonth, seed.eventDay, now)
                    dao.updateDaysRemaining(item.festivalId, updatedDays, System.currentTimeMillis())
                }
            }

            _lastSyncTime.value = getFormattedTime()
            _autoUpdateCycle.value += 1
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            _isRefreshing.value = false
        }
    }

    /**
     * Toggles favorite status in Room Database.
     */
    suspend fun toggleStar(id: Long, currentStarred: Boolean) = withContext(Dispatchers.IO) {
        dao.toggleStar(id, !currentStarred)
    }

    private fun calculateDaysRemaining(targetMonth: Int, targetDay: Int, now: Calendar): Int {
        val target = Calendar.getInstance().apply {
            set(Calendar.MONTH, targetMonth)
            set(Calendar.DAY_OF_MONTH, targetDay)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (target.before(now)) {
            // If passed this year, roll forward to next year
            target.add(Calendar.YEAR, 1)
        }

        val diffMillis = target.timeInMillis - now.timeInMillis
        return (diffMillis / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(0)
    }

    private fun getMonthName(monthIndex: Int): String {
        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        return months.getOrElse(monthIndex) { "" }
    }

    private fun getFormattedTime(): String {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return sdf.format(Date())
    }

    companion object {
        @Volatile
        private var INSTANCE: OdiaFestivalRepository? = null

        fun getInstance(context: Context): OdiaFestivalRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: OdiaFestivalRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
