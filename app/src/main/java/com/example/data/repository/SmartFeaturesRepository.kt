package com.example.data.repository

import com.example.data.model.BalasoreFestivalCountdown
import com.example.data.model.BloodDonorContact
import com.example.data.model.CoastalHarborAdvisory
import com.example.data.model.HarborSafetyFlag
import com.example.data.model.HeritageAudioStory
import com.example.data.model.HospitalBedPulse
import com.example.data.model.IncoisPfZZone
import com.example.data.model.OdiaPanjikaDay
import com.example.data.model.RareBloodAlert
import com.example.data.model.TempleScheduleItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SmartFeaturesRepository {

    // 1. INCOIS Ocean Advisory & Fishermen PFZ
    val coastalHarbors: List<CoastalHarborAdvisory> = listOf(
        CoastalHarborAdvisory(
            harborId = "balaramgadi",
            name = "Balaramgadi Estuary Harbor",
            odiaName = "ବଳରାମଗଡ଼ି ମୁହାଣ ମତ୍ସ୍ୟ ବନ୍ଦର",
            currentWaveHeightMeters = 1.2,
            swellPeriodSeconds = 9,
            windSpeedKnots = 14,
            windDirection = "South-South-West (SSW)",
            waterVisibilityMeters = 2.8,
            safetyFlag = HarborSafetyFlag.GREEN_SAFE,
            highTideTime = "08:15 PM",
            lowTideTime = "02:40 PM",
            notice = "Bar-crossing at river mouth clear. Moderate tidal current.",
            odiaNotice = "ମୁହାଣରେ ବାର୍-କ୍ରସିଂ ସ୍ୱାଭାବିକ। ଡଙ୍ଗା ଯାତ୍ରା ପାଇଁ ଅନୁକୂଳ ପରିବେଶ।"
        ),
        CoastalHarborAdvisory(
            harborId = "chandipur",
            name = "Chandipur Coastal Reach",
            odiaName = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି ଓ ତଟବର୍ତ୍ତୀ କ୍ଷେତ୍ର",
            currentWaveHeightMeters = 0.8,
            swellPeriodSeconds = 8,
            windSpeedKnots = 11,
            windDirection = "South-East (SE)",
            waterVisibilityMeters = 3.5,
            safetyFlag = HarborSafetyFlag.GREEN_SAFE,
            highTideTime = "08:30 PM",
            lowTideTime = "02:45 PM",
            notice = "Sea recedes 4.8 km. Safe for mud-flat walking. Keep eye on incoming tide horn.",
            odiaNotice = "ସମୁଦ୍ର ପ୍ରାୟ ୫ କିମି ଅପସାରିତ। ବେଳାଭୂମି ଭ୍ରମଣ ନିରାପଦ।"
        ),
        CoastalHarborAdvisory(
            harborId = "kasafal",
            name = "Kasafal Beach & Estuary",
            odiaName = "କାସାଫାଳ ବେଳାଭୂମି ଓ ଖଣ୍ଡିଆ ମୁହାଣ",
            currentWaveHeightMeters = 1.4,
            swellPeriodSeconds = 10,
            windSpeedKnots = 16,
            windDirection = "South (S)",
            waterVisibilityMeters = 2.2,
            safetyFlag = HarborSafetyFlag.YELLOW_CAUTION,
            highTideTime = "08:45 PM",
            lowTideTime = "02:55 PM",
            notice = "Submerged sandbars shifting near Khandia mouth. Avoid night trawler entry.",
            odiaNotice = "ଖଣ୍ଡିଆ ମୁହାଣରେ ବାଲୁକା ଚଡ଼ା ପରିବର୍ତ୍ତନ। ରାତ୍ରିକାଳୀନ ପ୍ରବେଶ ସତର୍କତା।"
        ),
        CoastalHarborAdvisory(
            harborId = "talasari",
            name = "Talasari & Chaumukh Harbor",
            odiaName = "ତାଳସାରୀ ଓ ଚୌମୁଖ ମତ୍ସ୍ୟ ବନ୍ଦର",
            currentWaveHeightMeters = 1.1,
            swellPeriodSeconds = 9,
            windSpeedKnots = 13,
            windDirection = "South-East (SE)",
            waterVisibilityMeters = 3.0,
            safetyFlag = HarborSafetyFlag.GREEN_SAFE,
            highTideTime = "08:10 PM",
            lowTideTime = "02:30 PM",
            notice = "Morning fish landing beach auction operational. Quiet sea swell.",
            odiaNotice = "ପ୍ରାତଃ ମତ୍ସ୍ୟ ନିଲାମ କେନ୍ଦ୍ର ସ୍ୱାଭାବିକ। ଶାନ୍ତ ସମୁଦ୍ର ତରଙ୍ଗ।"
        )
    )

    val pfzZones: List<IncoisPfZZone> = listOf(
        IncoisPfZZone(
            id = "pfz_balaramgadi_1",
            harborName = "Balaramgadi Confluence",
            odiaHarborName = "ବଳରାମଗଡ଼ି ମୁହାଣ ବାହାର ସମୁଦ୍ର",
            bearingDegrees = 135,
            distanceKm = 18.5,
            latitude = 21.3412,
            longitude = 87.0543,
            depthFathoms = 22,
            expectedSpecies = "Hilsa (Tenualosa ilisha), Pomfret, Ribbonfish",
            odiaSpecies = "ଇଲିଶି, ଚାନ୍ଦି, ଖୟରା ଓ ଧାଉଁଣି ମାଛ",
            seaSurfaceTempCelsius = 28.4,
            chlorophyllConcentration = 1.45,
            safetyStatus = HarborSafetyFlag.GREEN_SAFE
        ),
        IncoisPfZZone(
            id = "pfz_chandipur_deep",
            harborName = "Chandipur Outer Shoals",
            odiaHarborName = "ଚାନ୍ଦିପୁର ଗଭୀର ସମୁଦ୍ର ତଟ",
            bearingDegrees = 110,
            distanceKm = 24.0,
            latitude = 21.3980,
            longitude = 87.1620,
            depthFathoms = 30,
            expectedSpecies = "Tiger Prawns, Sea Bass (Bhetki), Mackerel",
            odiaSpecies = "ବାଘ ଚିଙ୍ଗୁଡ଼ି, ଭେକଟି ଓ କାନାଗୁର୍ତ୍ତା",
            seaSurfaceTempCelsius = 28.1,
            chlorophyllConcentration = 1.62,
            safetyStatus = HarborSafetyFlag.GREEN_SAFE
        ),
        IncoisPfZZone(
            id = "pfz_talasari_subarnarekha",
            harborName = "Subarnarekha Delta Shoal",
            odiaHarborName = "ସୁବର୍ଣ୍ଣରେଖା ଡେଲ୍ଟା ସମୁଦ୍ର କ୍ଷେତ୍ର",
            bearingDegrees = 145,
            distanceKm = 15.0,
            latitude = 21.5720,
            longitude = 87.4980,
            depthFathoms = 18,
            expectedSpecies = "Estuarine Croaker, Threadfin, Crabs",
            odiaSpecies = "କଳା ମାଛ, ସାହାଳ ଓ ଖଣ୍ଡା କଙ୍କଡ଼ା",
            seaSurfaceTempCelsius = 28.6,
            chlorophyllConcentration = 1.88,
            safetyStatus = HarborSafetyFlag.GREEN_SAFE
        )
    )

    // 2. Odia Panjika & Cultural Heritage
    fun getTodayPanjika(): OdiaPanjikaDay {
        val todayStr = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date())
        return OdiaPanjikaDay(
            englishDate = todayStr,
            odiaYearMonth = "୧୪୩୧ ସାଲ, ଆଶ୍ୱିନ ମାସ (ଦେବୀ ପକ୍ଷ)",
            tithi = "Shukla Paksha Navami / Dashami",
            odiaTithi = "ଶୁକ୍ଳ ପକ୍ଷ ନବମୀ / ବିଜୟା ଦଶମୀ",
            paksha = "Shukla Paksha (White Fortnight)",
            nakshatra = "Uttara Ashadha / Shravana",
            odiaNakshatra = "ଉତ୍ତରାଷାଢ଼ା ଓ ଶ୍ରବଣା ନକ୍ଷତ୍ର",
            suryaUdaya = "05:38 AM",
            suryaAstha = "05:46 PM",
            rahuKala = "10:15 AM - 11:45 AM (ଅଶୁଭ ସମୟ)",
            amrutaBela = "06:10 AM - 08:25 AM & 01:15 PM - 03:00 PM (ଶୁଭ ସମୟ)",
            dailyNiti = "Maha Navami Alati, Aparajita Puja and Ayudha Archana in Balasore households",
            odiaDailyNiti = "ମହାନବମୀ ଆଳତି, ଅପରାଜିତା ପୂଜା ଏବଂ ବାଲେଶ୍ୱର ଘରେ ଘରେ ଆୟୁଧ ବନ୍ଦାପନା",
            fastingRule = "Havirsha observance for holy Kartik precursor; vegetarian Prasad at Remuna"
        )
    }

    val festivalCountdowns: List<BalasoreFestivalCountdown> = listOf(
        BalasoreFestivalCountdown(
            id = "fest_chadak",
            festivalName = "Chandaneswar Chadak Mela & Uda Yatra",
            odiaFestivalName = "ଚନ୍ଦନେଶ୍ୱର ଚଡ଼କ ମେଳା ଓ ଉଡ଼ା ଯାତ୍ରା",
            location = "Chandaneswar Shiva Temple, Bhograi",
            odiaLocation = "ଚନ୍ଦନେଶ୍ୱର ପୀଠ, ଭୋଗରାଇ",
            targetDateEpochMillis = System.currentTimeMillis() + (140L * 24 * 3600 * 1000),
            description = "Ancient 13-day penance festival with 50,000+ Patuas fasting, walking on glowing embers & piercing thorns.",
            odiaDescription = "୧୩ ଦିନ ଧରି ଚାଲୁଥିବା ପ୍ରସିଦ୍ଧ ପାଟୁଆ ଭକ୍ତଙ୍କ ନିଷ୍ଠା, ଅଗ୍ନି ପରୀକ୍ଷା ଓ ଉଡ଼ା ଯାତ୍ରା।",
            keyRitual = "Kanta Pata (Thorn Walk), Nila Patua immersion & Pana Sankranti Bhog"
        ),
        BalasoreFestivalCountdown(
            id = "fest_makara",
            festivalName = "Nilagiri Makara Mela & Archery",
            odiaFestivalName = "ନୀଳଗିରି ମକର ମେଳା ଓ ଧନୁର୍ବିଦ୍ୟା",
            location = "Nilagiri Palace Foothills & Kuldiha",
            odiaLocation = "ନୀଳଗିରି ରାଜବାଟୀ ଓ କୁଲଡିହା ପାଦଦେଶ",
            targetDateEpochMillis = System.currentTimeMillis() + (85L * 24 * 3600 * 1000),
            description = "Tribal cultural confluence celebrating winter harvest with traditional bamboo bow tournaments and Makara Chaula.",
            odiaDescription = "ପାରମ୍ପରିକ ଆଦିବାସୀ ଧନୁତୀର ପ୍ରତିଯୋଗିତା, ମକର ଚାଉଳ ଭୋଗ ଓ ସାଂସ୍କୃତିକ ଉତ୍ସବ।",
            keyRitual = "Traditional Archery target contest, Chhau dance & Royal Dalbehera honors"
        ),
        BalasoreFestivalCountdown(
            id = "fest_boita",
            festivalName = "Balaramgadi Boita Bandana",
            odiaFestivalName = "ବଳରାମଗଡ଼ି ବୋଇତ ବନ୍ଦାଣ ଉତ୍ସବ",
            location = "Budhabalanga Estuary, Balaramgadi",
            odiaLocation = "ବୁଢ଼ାବଳଙ୍ଗ ମୁହାଣ, ବଳରାମଗଡ଼ି",
            targetDateEpochMillis = System.currentTimeMillis() + (35L * 24 * 3600 * 1000),
            description = "Kartika Purnima sunrise festival launching miniature boats with earthen lamps to honor Sadhabas maritime voyages.",
            odiaDescription = "କାର୍ତ୍ତିକ ପୂର୍ଣ୍ଣିମାରେ ପ୍ରାଚୀନ ସାଧବ ପରମ୍ପରାକୁ ସ୍ମରଣ କରି ଡଙ୍ଗା ଭସାଣ ଓ ଦୀପଦାନ।",
            keyRitual = "\"Aa-Kaa-Ma-Boi\" singing, coconut-shell boat floatation & sunrise Ganga Aarti"
        ),
        BalasoreFestivalCountdown(
            id = "fest_chandan_yatra",
            festivalName = "Remuna Khirachora Chandan Yatra",
            odiaFestivalName = "ରେମୁଣା କ୍ଷୀରଚୋରା ଚନ୍ଦନ ଯାତ୍ରା",
            location = "Khirachora Gopinatha Temple, Remuna",
            odiaLocation = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ପୀଠ, ରେମୁଣା",
            targetDateEpochMillis = System.currentTimeMillis() + (180L * 24 * 3600 * 1000),
            description = "42-day cooling sandal paste ceremony commemorating Madhavendra Puri's historical quest for sacred Malayagiri sandalwood.",
            odiaDescription = "ମାଧବେନ୍ଦ୍ର ପୁରୀଙ୍କ ସ୍ମୃତିରେ ୪୨ ଦିନ ଧରି ଗୋପୀନାଥଙ୍କ ଶ୍ରୀଅଙ୍ଗରେ ଶୀତଳ ଚନ୍ଦନ ଲେପନ।",
            keyRitual = "Nauka Vihar (Chapa Khela) in temple pond, Special Amrita Keli distribution"
        )
    )

    val templeSchedules: List<TempleScheduleItem> = listOf(
        TempleScheduleItem(
            templeName = "Khirachora Gopinatha Temple",
            odiaTempleName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର (ରେମୁଣା)",
            deity = "Sri Gopinatha, Madana Mohana & Govinda",
            morningAlati = "05:30 AM (ମଙ୍ଗଳ ଆଳତି)",
            madhyannaDhupa = "12:15 PM (ମଧ୍ୟାହ୍ନ ଧୂପ ଓ ଅମୃତ କେଳି)",
            sandhyaArati = "06:45 PM (ସନ୍ଧ୍ୟା ଆଳତି)",
            specialBhog = "Amrita Keli (Sacred condensed milk in red clay pot)",
            darshanStatus = "Open (05:30 AM - 01:00 PM & 03:30 PM - 08:30 PM)"
        ),
        TempleScheduleItem(
            templeName = "Panchalingeswar Hill Temple",
            odiaTempleName = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ିଆ ମନ୍ଦିର",
            deity = "Lord Shiva (Five natural rock Lingams in perennial spring)",
            morningAlati = "06:00 AM (ଜଳାଭିଷେକ ଆଳତି)",
            madhyannaDhupa = "01:00 PM (ଅନ୍ନ ଭୋଗ)",
            sandhyaArati = "06:15 PM (ଦୀପ ଆରାଧନା)",
            specialBhog = "Kheer, Khichdi & Bel leaves offerings",
            darshanStatus = "Open (Perennial spring touches feet after 263 steps)"
        ),
        TempleScheduleItem(
            templeName = "Emami Jagannath Temple",
            odiaTempleName = "ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର (ବାଲେଶ୍ୱର)",
            deity = "Chaturdhadaru Jagannath, Balabhadra, Subhadra, Sudarshana",
            morningAlati = "05:45 AM (ଦ୍ୱାରଫିଟା ଓ ମଙ୍ଗଳ ଆଳତି)",
            madhyannaDhupa = "12:30 PM (ଛପନ ଭୋଗ ପ୍ରସାଦ)",
            sandhyaArati = "07:00 PM (ସନ୍ଧ୍ୟା ଧୂପ)",
            specialBhog = "Temple Mahaprasad counters open at 01:15 PM daily",
            darshanStatus = "Open with modern landscaped garden & water fountains"
        ),
        TempleScheduleItem(
            templeName = "Baba Bhusandheswar Temple",
            odiaTempleName = "ବାବା ଭୂଷଣ୍ଢେଶ୍ୱର ବିରାଟ ଶିବଲିଙ୍ଗ",
            deity = "Asia's massive black granite Lingam (12 ft high, 14 ft width)",
            morningAlati = "05:00 AM (ମହାସ୍ନାନ ଓ ପ୍ରଭାତ ଆଳତି)",
            madhyannaDhupa = "12:00 PM (ମହାଭୋଗ)",
            sandhyaArati = "06:30 PM (ସନ୍ଧ୍ୟା ଶୃଙ୍ଗାର)",
            specialBhog = "Chhena, Panchamruta and Dahi Pakhala",
            darshanStatus = "Open (Subarnarekha river mouth approach)"
        )
    )

    // 3. Raibania Heritage Audio Walk & Lyria Music Prompts
    val heritageStories: List<HeritageAudioStory> = listOf(
        HeritageAudioStory(
            id = "story_raibania",
            titleEn = "The Lost Bastion: Raibania Medieval Fortress",
            titleOd = "ରାଏବଣିଆର ବିରାଟ ଦୁର୍ଗ ଓ ନରସିଂହ ଦେବଙ୍କ ପଦଚିହ୍ନ",
            monumentEn = "Raibania Fort Complex, Jaleswar",
            monumentOd = "ରାଏବଣିଆ ଦୁର୍ଗ, ଜଳେଶ୍ୱର",
            era = "13th Century (Eastern Ganga Dynasty)",
            distanceKm = 48.0,
            narrationScriptEn = "Before you stands the colossal laterite stone ramparts of Raibania, the largest medieval fortification in Eastern India. Commissioned by King Langula Narasimha Deva I after defeating the Delhi Sultanate's invading forces, this citadel featured three inner fortresses, underground passages, moats fed by the Subarnarekha, and 14 watchtowers. As you walk through the Jayachandi gate, notice the carved monolithic laterite stones that withstood centuries of cannon fire.",
            narrationScriptOd = "ଏହା ହେଉଛି ପୂର୍ବ ଭାରତର ସର୍ବବୃହତ୍ ମଧ୍ୟଯୁଗୀୟ ଦୁର୍ଗ - ରାଏବଣିଆ। ତ୍ରୟୋଦଶ ଶତାବ୍ଦୀରେ ଗଙ୍ଗ ବଂଶର ପ୍ରତାପୀ ସମ୍ରାଟ ପ୍ରଥମ ନରସିଂହ ଦେବ ମୁସଲିମ ଆକ୍ରମଣରୁ ଉତ୍କଳକୁ ରକ୍ଷା କରିବା ପାଇଁ ଏହି ଅଜେୟ ଦୁର୍ଗ ନିର୍ମାଣ କରିଥିଲେ। ସୁବର୍ଣ୍ଣରେଖା ନଦୀର ଜଳରେ ପରିଖା ପରିପୂର୍ଣ୍ଣ ଥିଲା ଏବଂ ଦୁର୍ଗ ଭିତରେ ଗୁପ୍ତ ସୁଡ଼ଙ୍ଗ ମଧ୍ୟ ରହିଛି।",
            lyriaMusicPrompt = "Dramatic ancient war drums, bronze trumpets, resonant bamboo flutes and meditative temple chimes evocative of a 13th-century Eastern Indian stone fortress victory march.",
            suggestedAudioTrackName = "Raibania Citadel Anthem (Echo of Narasimha Deva)",
            durationSeconds = 165,
            historicalSignificance = "Guarded Utkal's northern border against Turkic-Afghan invasions from Bengal."
        ),
        HeritageAudioStory(
            id = "story_bhusandheswar",
            titleEn = "Colossus of the Estuary: Baba Bhusandheswar",
            titleOd = "ସୁବର୍ଣ୍ଣରେଖା କୂଳରେ ବିରାଟ କଳାମୁଗୁନି ଶିବଲିଙ୍ଗ",
            monumentEn = "Bhusandheswar Shiva Temple, Bhograi",
            monumentOd = "ଭୂଷଣ୍ଢେଶ୍ୱର ମନ୍ଦିର, ଭୋଗରାଇ",
            era = "Ancient / Treta Yuga Mythology",
            distanceKm = 82.0,
            narrationScriptEn = "Cradled near the mouth of the Subarnarekha River lies one of Asia's most gigantic monolithic black granite Shiva Lingams, measuring over 12 feet in height and 14 feet in circumference. Legend recounts that Ravana was granted this lingam by Lord Shiva to install in Lanka on the condition that it never touch the earth. Wearied by its divine weight, Ravana placed it here, where it rooted into the bedrock forever.",
            narrationScriptOd = "ଭୋଗରାଇ ବ୍ଲକରେ ସୁବର୍ଣ୍ଣରେଖା ନଦୀ କୂଳରେ ପୂଜା ପାଉଥିବା ଭୂଷଣ୍ଢେଶ୍ୱର ଶିବଲିଙ୍ଗ ଏସିଆ ମହାଦେଶର ଏକ ବିଶାଳକାୟ କଳାମୁଗୁନି ପ୍ରସ୍ତର ପ୍ରତୀକ। କିମ୍ବଦନ୍ତୀ ଅନୁସାରେ ରାବଣ ଏହାକୁ ଲଙ୍କା ନେଉଥିବା ବେଳେ ଏଠାରେ ଭୂମିସ୍ପର୍ଶ କରାଇବାରୁ ଏହା ଅଚଳ ହୋଇ ରହିଗଲା।",
            lyriaMusicPrompt = "Mystic low drone tanpura, gentle flowing river ripples, temple bell reverbs and deep sacred Sanskrit chanting evoking divine cosmic tranquility.",
            suggestedAudioTrackName = "Bhusandheswar Pranava (Granite Resonance)",
            durationSeconds = 140,
            historicalSignificance = "One of the most massive single-rock Lingams in the world."
        ),
        HeritageAudioStory(
            id = "story_inchudi",
            titleEn = "The Salt Defiance: Inchudi Satyagraha",
            titleOd = "ଇଞ୍ଚୁଡ଼ି ଲବଣ ସତ୍ୟାଗ୍ରହ: ଭାରତର ଦ୍ୱିତୀୟ ଦାଣ୍ଡି",
            monumentEn = "Inchudi Salt Satyagraha Memorial",
            monumentOd = "ଇଞ୍ଚୁଡ଼ି ସ୍ମୃତି ପୀଠ, ବାଲେଶ୍ୱର",
            era = "April 1930 (Indian Freedom Movement)",
            distanceKm = 22.0,
            narrationScriptEn = "In April 1930, while Mahatma Gandhi marched to Dandi in Gujarat, Gopabandhu Choudhury and Acharya Harihar Das led brave Odia freedom fighters to Inchudi. Amid British lathi charges, women led by Rama Devi and Malati Choudhury gathered coastal brine and broke the repressive salt tax laws. Inchudi became the celebrated second most significant salt defiance ground in all of India.",
            narrationScriptOd = "୧୯୩୦ ମସିହା ଏପ୍ରିଲ ମାସରେ ଗାନ୍ଧିଜୀଙ୍କ ଦାଣ୍ଡି ଯାତ୍ରା ଭଳି ଓଡ଼ିଶାରେ ଇଞ୍ଚୁଡ଼ି ଥିଲା ସ୍ୱାଧୀନତା ସଂଗ୍ରାମର ମୁଖ୍ୟ କେନ୍ଦ୍ର। ଆଚାର୍ଯ୍ୟ ହରିହର ଓ ରମାଦେବୀଙ୍କ ନେତୃତ୍ୱରେ ସମୁଦ୍ର ଲୁଣ ମାରି ଇଂରେଜ ଆଇନ ଅମାନ୍ୟ କରାଯାଇଥିଲା।",
            lyriaMusicPrompt = "Inspirational patriotic violin swell, marching acoustic rhythm, peaceful coastal sea breeze and soulful Indian flute expressing sacrifice and freedom.",
            suggestedAudioTrackName = "Inchudi Vande Utkal (Song of Freedom)",
            durationSeconds = 150,
            historicalSignificance = "Second highest number of salt law violations in India after Dandi."
        ),
        HeritageAudioStory(
            id = "story_fakir_mohan",
            titleEn = "Cradle of Odia Prose: Fakir Mohan & Shanti Kanan",
            titleOd = "ଶାନ୍ତି କାନନ: ବ୍ୟାସକବି ଫକୀର ମୋହନଙ୍କ ପ୍ରେରଣା ସ୍ଥଳୀ",
            monumentEn = "Shanti Kanan & Utkal Press, Balasore",
            monumentOd = "ଶାନ୍ତି କାନନ, ବାଲେଶ୍ୱର",
            era = "Late 19th - Early 20th Century",
            distanceKm = 3.5,
            narrationScriptEn = "Walk beneath the sacred trees of Shanti Kanan where Vyasakabi Fakir Mohan Senapati penned iconic masterworks including 'Chha Mana Atha Guntha' and 'Rebati'. Here in 1868, Balasore pioneered the printing revolution with Utkal Press, successfully defending the Odia language from being supplanted. The garden preserves the poet's samadhi, library, and tranquil creative meditation pond.",
            narrationScriptOd = "ଏହି ଶାନ୍ତି କାନନରେ ବସି ବ୍ୟାସକବି ଫକୀର ମୋହନ ସେନାପତି 'ଛ ମାଣ ଆଠ ଗୁଣ୍ଠ' ଓ 'ରେବତୀ' ଭଳି ଯୁଗାନ୍ତକାରୀ ସାହିତ୍ୟ ସୃଷ୍ଟି କରିଥିଲେ। ୧୮୬୮ରେ ବାଲେଶ୍ୱରର ଉତ୍କଳ ପ୍ରେସ ଓଡ଼ିଆ ଭାଷା ଆନ୍ଦୋଳନକୁ ନୂତନ ଜୀବନ ଦେଇଥିଲା।",
            lyriaMusicPrompt = "Serene acoustic sitar, soft classical Odissi mardala beats, gentle birdsong and wooden flute reflecting 19th century literary reflection.",
            suggestedAudioTrackName = "Shanti Kanan Serenade (The Bard's Solitude)",
            durationSeconds = 135,
            historicalSignificance = "Birthplace of modern realistic Odia literature and language preservation."
        )
    )

    // 4. Blood Donors & Hospital Bed Pulse
    val bloodDonors: List<BloodDonorContact> = listOf(
        BloodDonorContact("d1", "Subrat Kumar Jena", "O+", "Balasore Sadar / OT Road", "9437182901", "2024-07-15"),
        BloodDonorContact("d2", "Debashis Mohapatra", "O-", "Remuna Golei", "9861203492", "2024-06-20", availabilityStatus = "Available - Rare Group"),
        BloodDonorContact("d3", "Priyabrata Biswal", "A+", "Station Bazaar, Balasore", "9937401928", "2024-08-01"),
        BloodDonorContact("d4", "Sasmita Rout", "B+", "FMMCH Campus, Remuna", "9438012934", "2024-05-18"),
        BloodDonorContact("d5", "Ranjan Parida", "AB+", "Soro Town", "9776192837", "2024-07-28"),
        BloodDonorContact("d6", "Manoranjan Das", "A-", "Jaleswar Puruna Bazaar", "9853018274", "2024-06-12", availabilityStatus = "Available - Rare Group"),
        BloodDonorContact("d7", "Alok Chandra Giri", "B-", "Basta Market", "9439281745", "2024-07-02", availabilityStatus = "Available - Rare Group"),
        BloodDonorContact("d8", "Chandan Kumar Nayak", "AB-", "Nilagiri College Road", "9938172635", "2024-04-10", availabilityStatus = "Available - Ultra Rare"),
        BloodDonorContact("d9", "Bikash Barik", "O+", "Chandipur Police Station Road", "9861029384", "2024-08-14"),
        BloodDonorContact("d10", "Goutam Behera", "B+", "Bhograi / Jaleswar", "9778291034", "2024-07-22")
    )

    val rareBloodAlerts: List<RareBloodAlert> = listOf(
        RareBloodAlert(
            id = "alert_bneg_1",
            patientNameOrBed = "Emergency Maternity Ward (Bed 14)",
            bloodGroup = "B Negative (B-)",
            unitsNeeded = 2,
            hospital = "FMMCH Medical College, Balasore",
            contactPerson = "Blood Bank Medical Officer (Dr. S. Mohanty)",
            contactPhone = "06782-262022",
            postedTimeAgo = "25 mins ago",
            isUrgent = true
        ),
        RareBloodAlert(
            id = "alert_oneg_2",
            patientNameOrBed = "Accident Trauma Care (ICU Bed 4)",
            bloodGroup = "O Negative (O-)",
            unitsNeeded = 3,
            hospital = "District Headquarters Hospital (DHH), Balasore",
            contactPerson = "Red Cross Blood Bank In-charge",
            contactPhone = "06782-262100",
            postedTimeAgo = "1 hour ago",
            isUrgent = true
        ),
        RareBloodAlert(
            id = "alert_sdp_3",
            patientNameOrBed = "Dengue Thrombocytopenia Patient",
            bloodGroup = "Single Donor Platelets (SDP) - Any Group",
            unitsNeeded = 1,
            hospital = "Jyoti Hospital, Balasore",
            contactPerson = "Emergency Duty Sister",
            contactPhone = "06782-268888",
            postedTimeAgo = "3 hours ago",
            isUrgent = false
        )
    )

    val hospitalBedPulse: List<HospitalBedPulse> = listOf(
        HospitalBedPulse(
            hospitalId = "fmmch",
            name = "Fakir Mohan Medical College & Hospital (FMMCH)",
            odiaName = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ହସ୍ପିଟାଲ",
            location = "Remuna, Balasore (NH-16)",
            totalIcuBeds = 40,
            availableIcuBeds = 6,
            totalOxygenBeds = 180,
            availableOxygenBeds = 34,
            dialysisSlotsTotal = 15,
            availableDialysisSlots = 3,
            emergencyContact = "06782-262022",
            lastUpdatedMinutesAgo = 12
        ),
        HospitalBedPulse(
            hospitalId = "dhh",
            name = "District Headquarters Hospital (DHH Balasore)",
            odiaName = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ (DHH ବାଲେଶ୍ୱର)",
            location = "Hospital Road, Balasore Town",
            totalIcuBeds = 20,
            availableIcuBeds = 2,
            totalOxygenBeds = 120,
            availableOxygenBeds = 21,
            dialysisSlotsTotal = 10,
            availableDialysisSlots = 1,
            emergencyContact = "06782-262100",
            lastUpdatedMinutesAgo = 8
        ),
        HospitalBedPulse(
            hospitalId = "jyoti",
            name = "Jyoti Hospital & Critical Care",
            odiaName = "ଜ୍ୟୋତି ହସ୍ପିଟାଲ ଓ କ୍ରିଟିକାଲ କେୟାର",
            location = "Kuruda, Balasore",
            totalIcuBeds = 25,
            availableIcuBeds = 5,
            totalOxygenBeds = 60,
            availableOxygenBeds = 14,
            dialysisSlotsTotal = 8,
            availableDialysisSlots = 2,
            emergencyContact = "06782-268888",
            lastUpdatedMinutesAgo = 18
        ),
        HospitalBedPulse(
            hospitalId = "lifeline",
            name = "Balasore Lifeline Hospital",
            odiaName = "ବାଲେଶ୍ୱର ଲାଇଫଲାଇନ ହସ୍ପିଟାଲ",
            location = "Cinema Chhak, Balasore",
            totalIcuBeds = 15,
            availableIcuBeds = 3,
            totalOxygenBeds = 45,
            availableOxygenBeds = 11,
            dialysisSlotsTotal = 6,
            availableDialysisSlots = 2,
            emergencyContact = "06782-255550",
            lastUpdatedMinutesAgo = 25
        )
    )
}
