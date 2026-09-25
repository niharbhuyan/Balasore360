package com.example.data.repository

import android.content.Context
import com.example.data.model.BalasoreForecastDay
import com.example.data.model.BalasoreForecastHour
import com.example.data.model.CycloneShelter
import com.example.data.model.DailyBalasoreIdiom
import com.example.data.model.DrdoAdvisory
import com.example.data.model.EmergencyContact
import com.example.data.model.Hotspot
import com.example.data.model.LiteraryTrailPoint
import com.example.data.model.NewsArticle
import com.example.data.model.RiverGauge
import com.example.data.model.SeafoodCatch
import com.example.data.model.TempleRitualInfo
import com.example.data.model.TidalClockData
import com.example.data.model.TransitSchedule
import com.example.data.model.WeatherInfo

object BalasoreRepository {

    // 1. Chandipur Tidal Clock & Safe Walk Data
    val tidalClock = TidalClockData(
        currentPhase = "Vanishing Sea (Low Tide)",
        odiaPhase = "ଭଟ୍ଟା ସମୟ (ସମୁଦ୍ର ଅପସାରିତ)",
        distanceRecededKm = 4.8,
        safeWalkMinutesRemaining = 135,
        highTideTime = "08:15 PM",
        lowTideTime = "02:30 PM",
        tidalCoefficient = "Semi-Diurnal Spring Tide",
        biodiversitySightings = listOf(
            "Rare Horseshoe Crabs (Living fossils / Tachypleus gigas) spotted at Sandbar #2",
            "Swarms of Red Ghost Crabs active around northern casuarina dunes",
            "Conch shells, sea dollars, and mudskippers exposed in shallow sand ripples"
        )
    )

    // 2. DRDO ITR Coastal Advisories & Defense Milestones
    val drdoAdvisories = listOf(
        DrdoAdvisory(
            id = "drdo_1",
            title = "Coastal Waters Cleared for Artisanal Fishing",
            odiaTitle = "ଉପକୂଳ ଜଳରାଶି ମତ୍ସ୍ୟଜୀବୀଙ୍କ ପାଇଁ ମୁକ୍ତ ଘୋଷଣା",
            dateOrTime = "Today • Valid until 18:00 hrs",
            status = "CLEARED FOR FISHING",
            seaZone = "Chandipur to Kasafal (Zone Alpha)",
            fishermenAdvisory = "All registered fishing boats and trawlers can operate normally up to 25 nautical miles.",
            sonicBoomWarning = false,
            heritageMilestone = "Historical: Chandipur was the launchpad for India's first Prithvi missile test in 1988."
        ),
        DrdoAdvisory(
            id = "drdo_2",
            title = "Scheduled Radar & Electronic Telemetry Tracking Window",
            odiaTitle = "ନିର୍ଦ୍ଧାରିତ ରାଡାର ଓ ଟେଲିମେଟ୍ରି ଟ୍ରାକିଂ ଅବଧି",
            dateOrTime = "Tomorrow 09:30 AM - 13:00 PM",
            status = "COASTAL NOTICE",
            seaZone = "Abdul Kalam Island Corridor (Zone Bravo)",
            fishermenAdvisory = "Trawlers are advised to avoid deep-sea corridor 15 km south of Dhamra-Chandipur boundary.",
            sonicBoomWarning = true,
            heritageMilestone = "BrahMos supersonic cruise missile coastal batteries regularly validate pinpoint strike accuracy here."
        )
    )

    // 3. Remuna Khirachora Bhog & Darshan Data
    val templeRitualInfo = TempleRitualInfo(
        templeName = "Khirachora Gopinatha Temple, Remuna",
        odiaTempleName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ଜିଉ, ରେମୁଣା",
        nextBhogDistribution = "12:30 PM (Mid-day) & 07:30 PM (Sandhya)",
        bhogName = "Amruta Keli (ଦିବ୍ୟ କ୍ଷୀର ଭୋଗ)",
        bhogAvailabilityNote = "Handcrafted condensed milk cooked in terracotta pots using centuries-old sacred recipe",
        currentDarshanStatus = "Open • Mangala Alati Completed",
        mangalaAlati = "05:30 AM",
        sandhyaArati = "06:45 PM",
        pahadaTime = "01:00 PM - 04:00 PM",
        pilgrimageCircuit = listOf(
            "1. Remuna Gopinatha (9 km from city center)",
            "2. Panchalingeswar Mountain Spring (30 km)",
            "3. Emami Jagannath Temple, Januganj (6.5 km)",
            "4. Baba Bhusandeswar Giant Shiva Linga, Bhograi (85 km)"
        )
    )

    // 4. River Gauge Telemetry & Flood Early Warning
    val riverGauges = listOf(
        RiverGauge(
            riverName = "Subarnarekha River",
            odiaRiverName = "ସୁବର୍ଣ୍ଣରେଖା ନଦୀ",
            stationName = "Rajghat Station (Jaleswar)",
            currentLevelMeters = 8.42,
            warningLevelMeters = 9.45,
            dangerLevelMeters = 10.36,
            status = "NORMAL",
            trend = "Steady"
        ),
        RiverGauge(
            riverName = "Budhabalanga River",
            odiaRiverName = "ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ",
            stationName = "NH-16 Bridge (Balasore City)",
            currentLevelMeters = 6.85,
            warningLevelMeters = 7.80,
            dangerLevelMeters = 8.53,
            status = "NORMAL",
            trend = "Falling (-0.05m/hr)"
        ),
        RiverGauge(
            riverName = "Jalaka River",
            odiaRiverName = "ଜଳକା ନଦୀ",
            stationName = "Mathani Station (Basta)",
            currentLevelMeters = 5.20,
            warningLevelMeters = 5.50,
            dangerLevelMeters = 6.50,
            status = "ALERT",
            trend = "Rising slowly (+0.02m/hr)"
        )
    )

    val cycloneShelters = listOf(
        CycloneShelter(
            name = "Bhograi Multipurpose Cyclone Shelter",
            block = "Bhograi (Coastal)",
            capacityPeople = 1200,
            nodalContact = "Block Disaster Officer",
            phone = "06781-232115"
        ),
        CycloneShelter(
            name = "Jaleswar Flood Relief Center",
            block = "Jaleswar (Subarnarekha Basin)",
            capacityPeople = 850,
            nodalContact = "Tahasildar Office Jaleswar",
            phone = "06781-222045"
        ),
        CycloneShelter(
            name = "Chandipur Sea-Front Shelter #3",
            block = "Balasore Sadar",
            capacityPeople = 600,
            nodalContact = "Marine Fishery Inspector",
            phone = "06782-272210"
        )
    )

    // 5. Bahabalpur & Kasafal Fresh Seafood Catch Index
    val seafoodCatches = listOf(
        SeafoodCatch(
            id = "sf_1",
            name = "Bay of Bengal Hilsa (Ilish)",
            odiaName = "ବଙ୍ଗୋପସାଗର ଇଲିସି",
            harbor = "Balaramgadi Fish Landing Center",
            priceRangeKg = "₹850 - ₹1,200 / kg",
            qualityGrade = "Grade A (Silver Fresh)",
            freshness = "Caught 4 hrs ago",
            peakSeason = "Monsoon to Early Winter (Jul - Nov)"
        ),
        SeafoodCatch(
            id = "sf_2",
            name = "Chandipur Black Tiger Prawns",
            odiaName = "ବାଗଦା ଚିଙ୍ଗୁଡ଼ି (ବ୍ଲାକ୍ ଟାଇଗର)",
            harbor = "Bahabalpur Harbor",
            priceRangeKg = "₹580 - ₹750 / kg",
            qualityGrade = "Export Quality (Head-on)",
            freshness = "Landed 2 hrs ago",
            peakSeason = "Round the Year • Peak Autumn"
        ),
        SeafoodCatch(
            id = "sf_3",
            name = "White Silver Pomfret",
            odiaName = "ଧଳା ଚାନ୍ଦି ମାଛ",
            harbor = "Kasafal Marine Center",
            priceRangeKg = "₹650 - ₹900 / kg",
            qualityGrade = "Premium Medium & Large",
            freshness = "Morning Boat Trawl",
            peakSeason = "September to February"
        ),
        SeafoodCatch(
            id = "sf_4",
            name = "Mud & Sea Crabs",
            odiaName = "ସମୁଦ୍ର ଓ ନଦୀ ମୁହାଣ କଙ୍କଡ଼ା",
            harbor = "Balaramgadi Harbor",
            priceRangeKg = "₹420 - ₹550 / kg",
            qualityGrade = "Live Catch (Green Shell)",
            freshness = "Live in Sea Baskets",
            peakSeason = "All Seasons"
        )
    )

    // 6. Fakir Mohan Literary Trail & Balasore Dialect
    val literaryTrailPoints = listOf(
        LiteraryTrailPoint(
            id = "lit_1",
            title = "Shanti Kanan (ଶାନ୍ତି କାନନ)",
            odiaTitle = "ବ୍ୟାସକବିଙ୍କ ସାଧନା ସ୍ଥଳୀ - ଶାନ୍ତି କାନନ",
            location = "Mallikashpur, Balasore City",
            excerpt = "Where Vyasa Kabi Fakir Mohan Senapati penned iconic classics including 'Chha Mana Atha Guntha' and 'Rebati'.",
            historicalContext = "A serene botanical sanctuary preserved with the author's original study desk, mango groves, and bronze statue."
        ),
        LiteraryTrailPoint(
            id = "lit_2",
            title = "Historic Utkal Press Heritage",
            odiaTitle = "ପ୍ରାଚୀନ ଉତ୍କଳ ପ୍ରେସ ପୃଷ୍ଠଭୂମି",
            location = "Motiganj Market, Balasore",
            excerpt = "The pioneering printing press that sparked the renaissance of Odia language and literature in the late 19th century.",
            historicalContext = "Established in 1868, this historic site printed the first independent Odia newspaper 'Bodhadayini'."
        )
    )

    val dailyIdiom = DailyBalasoreIdiom(
        wordOrIdiom = "ବାଲେଶ୍ୱରୀ ଶବ୍ଦ: କାକସ୍ନାନ (Kakashnana)",
        odiaScript = "କାକସ୍ନାନ • ଅର୍ଥ: ଅଳ୍ପ ସମୟରେ ଖୁବ୍ ଶୀଘ୍ର ଗାଧୋଇବା",
        meaning = "A very quick, dip-and-go bath (literally 'crow's bath') used affectionately across North Balasore households.",
        literaryWork = "Frequently used in Fakir Mohan Senapati's rural social satires.",
        funContext = "Commonly heard during chilly winter mornings before school or morning temple visits."
    )

    val hotspots = listOf(
        Hotspot(
            id = "chandipur",
            name = "Chandipur Beach",
            odiaName = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
            category = "Beach & Coast",
            description = "Famous vanishing sea phenomenon where the sea recedes up to 5 kilometers during low tide, revealing horseshoe crabs and shells. Near the Integrated Test Range (ITR).",
            location = "Chandipur, 16 km from Balasore Station",
            rating = 4.8,
            isFeatured = true,
            timing = "Open 24 hours (Best during sunrise/sunset)",
            distanceKm = 16.0
        ),
        Hotspot(
            id = "khirachora",
            name = "Khirachora Gopinatha Temple",
            odiaName = "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର",
            category = "Heritage & Spiritual",
            description = "Historic 12th-century temple in Remuna known for the divine 'Amruta Keli' (condensed milk bhog) offering and visit by Sri Chaitanya Mahaprabhu.",
            location = "Remuna, 9 km from Balasore City",
            rating = 4.9,
            isFeatured = true,
            timing = "06:00 AM - 12:30 PM, 04:00 PM - 08:30 PM",
            distanceKm = 9.2
        ),
        Hotspot(
            id = "panchalingeswar",
            name = "Panchalingeswar Temple",
            odiaName = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ମନ୍ଦିର",
            category = "Nature & Pilgrimage",
            description = "Nestled on Devagiri hill in Nilagiri, five Shiva lingas are constantly bathed by a pristine mountain perennial spring water stream.",
            location = "Devagiri Hill, Nilagiri, Balasore",
            rating = 4.7,
            isFeatured = true,
            timing = "05:30 AM - 06:30 PM",
            distanceKm = 30.5
        ),
        Hotspot(
            id = "talasari",
            name = "Talasari & Udaipur Beach",
            odiaName = "ତାଳସାରୀ ଏବଂ ଉଦୟପୁର ବେଳାଭୂମି",
            category = "Beach & Coast",
            description = "Serene, palm-fringed coast with red crabs and calm Subarnarekha river mouth confluence at the Odisha-Bengal border.",
            location = "Bhograi Block, 88 km from Balasore",
            rating = 4.6,
            isFeatured = false,
            timing = "Open 24 hours",
            distanceKm = 88.0
        ),
        Hotspot(
            id = "kuldiha",
            name = "Kuldiha Wildlife Sanctuary",
            odiaName = "କୁଲଡିହା ବନ୍ୟପ୍ରାଣୀ ଅଭୟାରଣ୍ୟ",
            category = "Eco-Tourism",
            description = "Dense sal forest habitat home to Asian elephants, leopards, giant squirrels, and diverse bird species near Rissia nature camp.",
            location = "Nilagiri range, Balasore",
            rating = 4.7,
            isFeatured = false,
            timing = "06:00 AM - 05:00 PM (Winter permit required)",
            distanceKm = 35.0
        ),
        Hotspot(
            id = "emami_jagannath",
            name = "Emami Jagannath Temple",
            odiaName = "ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର",
            category = "Spiritual Architecture",
            description = "Magnificent modern Kalinga-style stone temple complex built with intricate carvings, manicured gardens, and peaceful courtyard.",
            location = "Remuna Januganj Road, Balasore",
            rating = 4.8,
            isFeatured = false,
            timing = "06:00 AM - 08:30 PM",
            distanceKm = 6.5
        )
    )

    val newsArticles = listOf(
        NewsArticle(
            id = "news_1",
            title = "Balasore Railway Station Redevelopment Under Amrit Bharat Enters Final Phase",
            odiaTitle = "ଅମୃତ ଭାରତ ଯୋଜନାରେ ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ ନବୀକରଣ ଅନ୍ତିମ ପର୍ଯ୍ୟାୟରେ",
            snippet = "Modern passenger concourse, skywalk connectivity, and regional Odishan terracotta aesthetic facades are scheduled for unveiling next month.",
            odiaSnippet = "ବାଲେଶ୍ୱର ଷ୍ଟେସନରେ ଆଧୁନିକ ୱେଟିଂ ହଲ୍, ସ୍କାଏୱାକ୍ ଓ ଟେରାକୋଟା କଳାକୃତି ସହ ନୂତନ ସୁବିଧା ସୁଯୋଗ ସମ୍ପନ୍ନ ହେବାକୁ ଯାଉଛି।",
            category = "Infrastructure",
            timeAgo = "10 mins ago",
            source = "Balasore Express News",
            content = "The ambitious Amrit Bharat redevelopment initiative at Balasore Railway Station (BLS) has crossed 90% civil completion under South Eastern Railway. The station features an expanded 12-meter-wide central foot-over-bridge skywalk with integrated escalators and passenger lifts connecting platforms 1 through 4. A multi-tier parking concourse, dedicated EV charging bays, and executive air-conditioned lounge are nearing inauguration. The exterior facade proudly integrates Balasore heritage motifs, drawing inspiration from Emami Jagannath Temple architecture and traditional Baleswari terracotta panels.",
            odiaContent = "ଦକ୍ଷିଣ ପୂର୍ବ ରେଳପଥ ଅଧୀନରେ ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନର ଅମୃତ ଭାରତ ପୁନଃବିକାଶ ପ୍ରକଳ୍ପ ୯୦ ପ୍ରତିଶତ କାର୍ଯ୍ୟ ସମାପ୍ତ କରିଛି। ପ୍ଲାଟଫର୍ମ ୧ ରୁ ୪ କୁ ସଂଯୋଗ କରୁଥିବା ୧୨ ମିଟର ଚଉଡ଼ା ସ୍କାଏୱାକ୍, ଏସ୍କାଲେଟର, ଲିଫ୍ଟ ଏବଂ ଇଭି ଚାର୍ଜିଂ ସୁବିଧା ଉଦ୍ଘାଟନ ପାଇଁ ପ୍ରସ୍ତୁତ। ବାଲେଶ୍ୱରର ଐତିହ୍ୟ ଟେରାକୋଟା ଓ ଏମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର କଳାଶୈଳୀରେ ଷ୍ଟେସନ ଅଗ୍ରଭାଗ ସଜ୍ଜିତ ହୋଇଛି।"
        ),
        NewsArticle(
            id = "news_2",
            title = "Chandipur DRDO Facility Successfully Tests Advanced Coastal Defense System",
            odiaTitle = "ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ଘାଟିରୁ ଅତ୍ୟାଧୁନିକ ପ୍ରତିରକ୍ଷା ପ୍ରଣାଳୀର ସଫଳ ପରୀକ୍ଷଣ",
            snippet = "The Integrated Test Range at Chandipur reported mission success with precise radar tracking across the Bay of Bengal coastline.",
            odiaSnippet = "ଆଇଟିଆର ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ଘାଟିରୁ ସ୍ୱଦେଶୀ ଜ୍ଞାନକୌଶଳରେ ନିର୍ମିତ ପ୍ରତିରକ୍ଷା ପ୍ରଣାଳୀ ଲକ୍ଷ୍ୟଭେଦ କରିଛି।",
            category = "Defense",
            timeAgo = "1 hour ago",
            source = "Defense Updates Odisha",
            content = "The Defence Research and Development Organisation (DRDO) and Integrated Test Range (ITR) at Chandipur successfully flight-tested an indigenous advanced coastal defense missile system from Launch Complex III. Equipped with cutting-edge active radio frequency seekers and precision guidance avionics, the interceptor intercepted an offshore high-speed aerial unmanned target over the Bay of Bengal. Maritime telemetry stations at Wheeler Island (Dr. APJ Abdul Kalam Island) and Dhamra coastline confirmed flawless guidance metrics.",
            odiaContent = "ଚାନ୍ଦିପୁର ସ୍ଥିତ ଆଇଟିଆର (Integrated Test Range) ଲଞ୍ଚ କମ୍ପ୍ଲେକ୍ସ-୩ ରୁ ପ୍ରତିରକ୍ଷା ଗବେଷଣା ସଂସ୍ଥା (DRDO) ଦ୍ୱାରା ଏକ ଉନ୍ନତ ଉପକୂଳ ପ୍ରତିରକ୍ଷା କ୍ଷେପଣାସ୍ତ୍ରର ସଫଳ ଉତକ୍ଷେପଣ କରାଯାଇଛି। ଡକ୍ଟର ଏପିଜେ ଅବଦୁଲ କଲାମ ଦ୍ୱୀପ ଏବଂ ଧାମରା ଉପକୂଳର ରାଡାର ଟ୍ରାକିଂ ସେଣ୍ଟର ସଠିକ୍ ଲକ୍ଷ୍ୟଭେଦ ନିଶ୍ଚିତ କରିଛନ୍ତି।"
        ),
        NewsArticle(
            id = "news_3",
            title = "Annual Remuna Khirachora Gopinath Mahotsav Dates Announced",
            odiaTitle = "ପ୍ରସିଦ୍ଧ ରେମୁଣା କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମହୋତ୍ସବ ତାରିଖ ଘୋଷଣା",
            snippet = "Cultural troops, traditional Sankirtan singers, and craft artisans from across Odisha will gather for the 5-day spiritual celebration.",
            odiaSnippet = "ପାରମ୍ପରିକ ସଂକୀର୍ତ୍ତନ ଏବଂ ସାଂସ୍କୃତିକ କାର୍ଯ୍ୟକ୍ରମ ସହ ପାଞ୍ଚ ଦିନ ଧରି ଚାଲିବ ରେମୁଣା ବାର୍ଷିକ ଉତ୍ସବ।",
            category = "Culture",
            timeAgo = "3 hours ago",
            source = "Odisha Sambad",
            content = "The temple administration and Balasore District Cultural Council have officially announced the itinerary for the celebrated Remuna Khirachora Gopinath Mahotsav. Millions of devotees are expected to relish the legendary condensed milk offering 'Amrita Keli' (Khira Bhog). Alongside devotional sankirtan troupes and Odissi performances, a traditional handicrafts fair featuring Remuna brass-metal utensils and handloom will run for five days with dedicated shuttle buses operating from Balasore Bus Stand.",
            odiaContent = "ରେମୁଣା କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର ଟ୍ରଷ୍ଟ ଓ ଜିଲ୍ଲା ସଂସ୍କୃତି ପରିଷଦ ପକ୍ଷରୁ ବାର୍ଷିକ ମହୋତ୍ସବର ତାରିଖ ଘୋଷଣା ହୋଇଛି। ପ୍ରସିଦ୍ଧ 'ଅମୃତ କେଳି' କ୍ଷୀର ଭୋଗ ପାଇଁ ଭକ୍ତଙ୍କ ଭିଡ଼କୁ ଦୃଷ୍ଟିରେ ରଖି ସ୍ୱତନ୍ତ୍ର ବ୍ୟବସ୍ଥା କରାଯାଇଛି। ସାଂସ୍କୃତିକ ମଞ୍ଚରେ ଓଡ଼ିଶୀ ନୃତ୍ୟ, ସଂକୀର୍ତ୍ତନ ଏବଂ କଂସା-ପିତ୍ତଳ ଶିଳ୍ପ ପ୍ରଦର୍ଶନୀ ଅନୁଷ୍ଠିତ ହେବ।"
        ),
        NewsArticle(
            id = "news_4",
            title = "Subarnarekha River Basin Flood Monitoring Sensors Deployed by District Disaster Authority",
            odiaTitle = "ସୁବର୍ଣ୍ଣରେଖା ଅବବାହିକାରେ ବନ୍ୟା ନିୟନ୍ତ୍ରଣ ପାଇଁ ସ୍ୱୟଂକ୍ରିୟ ସେନ୍ସର ସ୍ଥାପନ",
            snippet = "Real-time water level telemetry alerts will be transmitted to coastal Bhograi and Jaleswar panchayats for early monsoon preparedness.",
            odiaSnippet = "ଜଳସ୍ତର ଉପରେ ନଜର ରଖିବାକୁ ଭୋଗରାଇ ଏବଂ ଜଳେଶ୍ୱର ବ୍ଲକରେ ସେନ୍ସର ସ୍ଥାପିତ ହୋଇଛି।",
            category = "Emergency",
            timeAgo = "5 hours ago",
            source = "Balasore District Admin",
            content = "To prevent sudden monsoon flash floods along northern Balasore, the District Disaster Management Authority (DDMA) in collaboration with Central Water Commission has installed solar-powered acoustic flood telemetry gauges at Rajghat, Jamsholaghat, and Bhograi embankments. When water levels cross danger levels (10.36 meters at Rajghat), automated siren broadcasts and SMS alerts will reach over 48 coastal gram panchayats within 90 seconds.",
            odiaContent = "ଉତ୍ତର ବାଲେଶ୍ୱରରେ ବନ୍ୟା ନିୟନ୍ତ୍ରଣ ଓ ସତର୍କତା ପାଇଁ କେନ୍ଦ୍ରୀୟ ଜଳ ଆୟୋଗ ଏବଂ ଜିଲ୍ଲା ବିପର୍ଯ୍ୟୟ ପରିଚାଳନା କର୍ତ୍ତୃପକ୍ଷ ରାଜଘାଟ, ଜାମଶୋଳାଘାଟ ଓ ଭୋଗରାଇ ନିକଟରେ ସୌରଚାଳିତ ସ୍ୱୟଂକ୍ରିୟ ସେନ୍ସର ବସାଇଛନ୍ତି। ବିପଦ ସଙ୍କେତ ଟପିଲେ ସଙ୍ଗେ ସଙ୍ଗେ ସାଇରନ ଓ ଏସଏମଏସ ଜରିଆରେ ଗ୍ରାମବାସୀଙ୍କୁ ସତର୍କ କରାଯିବ।"
        ),
        NewsArticle(
            id = "news_5",
            title = "Balasore Municipal Corporation Reviews Urban Drainage and Smart City Projects",
            odiaTitle = "ବାଲେଶ୍ୱର ପୌରପାଳିକା ପକ୍ଷରୁ ସହରୀ ଡ୍ରେନେଜ୍ ଓ ବିକାଶ କାର୍ଯ୍ୟର ସମୀକ୍ଷା",
            snippet = "Key political representatives and district collectors meet to finalize the masterplan for underground cabling and rainwater drainage.",
            odiaSnippet = "ସହରର ଉନ୍ନୟନ ଏବଂ ଭୂତଳ କେବୁଲିଂ ବ୍ୟବସ୍ଥା ନେଇ ପ୍ରଶାସନିକ ବୈଠକ ଅନୁଷ୍ଠିତ ହୋଇଛି।",
            category = "Politics",
            timeAgo = "6 hours ago",
            source = "Utkal Mail",
            content = "A high-level steering meeting led by local legislators and the Balasore Municipal Commissioner reviewed the ₹85-crore urban drainage masterplan for Cinema Chhak, Motiganj, Gopalgaon, and Azimabad. The municipal council has instructed contractors to clean primary stormwater culverts before the monsoon peak and fast-track underground electrical cabling along OT Road to prevent cyclone wind damage.",
            odiaContent = "ବାଲେଶ୍ୱର ପୌରପାଳିକା ପରିସରରେ ଆୟୋଜିତ ଉଚ୍ଚସ୍ତରୀୟ ସମୀକ୍ଷା ବୈଠକରେ ସିନେମା ଛକ, ମୋତିଗଞ୍ଜ, ଗୋପାଳଗାଁ ଏବଂ ଅଜିମାବାଦ ଅଞ୍ଚଳରେ ୮୫ କୋଟି ଟଙ୍କାର ଡ୍ରେନେଜ୍ ମାଷ୍ଟରପ୍ଲାନକୁ ତ୍ୱରାନ୍ୱିତ କରିବା ପାଇଁ ନିର୍ଦ୍ଦେଶ ଦିଆଯାଇଛି। ଓଟି ରୋଡରେ ଭୂତଳ ବିଦ୍ୟୁତ୍ କେବୁଲିଂ କାର୍ଯ୍ୟ ମଧ୍ୟ ଜାରି ରହିଛି।"
        ),
        NewsArticle(
            id = "news_6",
            title = "Balasore Stadium to Host Inter-District Youth Cricket and Athletics Championship",
            odiaTitle = "ବାଲେଶ୍ୱର ଷ୍ଟାଡିୟମରେ ଆନ୍ତଃଜିଲ୍ଲା କ୍ରିକେଟ ଓ ଆଥଲେଟିକ୍ସ ପ୍ରତିଯୋଗିତା ଆୟୋଜିତ",
            snippet = "Over 450 young athletes from northern coastal districts will participate in the week-long athletic meet starting this Friday.",
            odiaSnippet = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା କ୍ରୀଡା ସଂଘ ଦ୍ୱାରା ଯୁବ ଖେଳାଳିମାନଙ୍କ ପାଇଁ ରାଜ୍ୟସ୍ତରୀୟ ମ୍ୟାଚ୍ ଆୟୋଜନ।",
            category = "Sports",
            timeAgo = "8 hours ago",
            source = "Odisha Sports Bureau",
            content = "Balasore District Sports Association has announced that the newly renovated Permit Field turf and athletic tracks at Balasore Stadium will host the Northern Odisha Inter-District U-19 Cricket Cup and Track & Field Meet. Teams from Balasore, Mayurbhanj, Bhadrak, and Jajpur will compete under floodlights with state selectors attending to scout budding sports talent.",
            odiaContent = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା କ୍ରୀଡ଼ା ସଂଘ ପକ୍ଷରୁ ବାଲେଶ୍ୱର ଷ୍ଟାଡିୟମ (ପରମିଟ୍ ପଡ଼ିଆ) ଠାରେ ଉତ୍ତର ଓଡ଼ିଶା ଆନ୍ତଃଜିଲ୍ଲା ଅଣ୍ଡର-୧୯ କ୍ରିକେଟ ଏବଂ ଆଥଲେଟିକ୍ସ ଚାମ୍ପିଅନସିପ୍ ଆରମ୍ଭ ହେବ। ମୟୂରଭଞ୍ଜ, ଭଦ୍ରକ, ଯାଜପୁର ଓ ବାଲେଶ୍ୱରରୁ ୪୫୦ରୁ ଉର୍ଦ୍ଧ୍ୱ ପ୍ରତିଯୋଗୀ ଅଂଶଗ୍ରହଣ କରିବେ।"
        ),
        NewsArticle(
            id = "news_7",
            title = "NH-60 Six-Laning & Balasore Coastal Industrial Corridor Development Approved",
            odiaTitle = "ଜାତୀୟ ରାଜପଥ-୬୦ ସମ୍ପ୍ରସାରଣ ଏବଂ ଉପକୂଳ ଶିଳ୍ପ କରିଡର ବିକାଶ ମଞ୍ଜୁର",
            snippet = "New mega industrial logistics hub near Kuruda approved to boost coastal aquaculture exports and port transit connectivity.",
            odiaSnippet = "କୁରୁଡା ନିକଟରେ ନୂତନ ଶିଳ୍ପ କରିଡର ଓ ଯୋଗାଯୋଗ ବ୍ୟବସ୍ଥାକୁ ମିଳିଲା ସରକାରୀ ଅନୁମୋଦନ।",
            category = "Development",
            timeAgo = "12 hours ago",
            source = "Prameya News",
            content = "The National Highways Authority of India (NHAI) and Odisha State Industrial Development Corporation have sanctioned the six-lane expansion of the NH-60 segment linking Balasore to Jaleswar and Kharagpur. A specialized 200-acre multimodal logistics park at Kuruda will house deep freezing storage, dry docks, and electronic testing facilities, drastically cutting transit time for local seafood and plastic manufacturing units.",
            odiaContent = "ବାଲେଶ୍ୱରରୁ ଜଳେଶ୍ୱର ଦେଇ ଖଡ଼ଗପୁର ସଂଯୋଗ କରୁଥିବା ଜାତୀୟ ରାଜପଥ-୬୦ ର ୬ ଲେନ ସମ୍ପ୍ରସାରଣ କାର୍ଯ୍ୟ ଅନୁମୋଦିତ ହୋଇଛି। କୁରୁଡ଼ା ଠାରେ ୨୦୦ ଏକର ପରିମିତ ମଲ୍ଟିମୋଡାଲ ଲଜିଷ୍ଟିକ୍ସ ପାର୍କ ନିର୍ମାଣ ହେବ, ଯାହା ମାଧ୍ୟମରେ ସାମୁଦ୍ରିକ ଉତ୍ପାଦ ରପ୍ତାନି ଓ ଶିଳ୍ପ କ୍ଷେତ୍ରକୁ ନୂତନ ଗତି ମିଳିବ।"
        ),
        NewsArticle(
            id = "news_8",
            title = "Balasore Town Ring Road & Station Square Beautification Drive Initiated",
            odiaTitle = "ବାଲେଶ୍ୱର ସହର ରିଙ୍ଗ ରୋଡ୍ ଓ ଷ୍ଟେସନ ଛକ ସୌନ୍ଦର୍ଯ୍ୟକରଣ କାର୍ଯ୍ୟ ଆରମ୍ଭ",
            snippet = "Smart LED streetlights, modern pedestrian crossings, and native garden landscaping underway to ease traffic between OT Road and Cinema Chhak.",
            odiaSnippet = "ସହରରେ ଟ୍ରାଫିକ୍ ନିୟନ୍ତ୍ରଣ ଓ ସୌନ୍ଦର୍ଯ୍ୟକରଣ ପାଇଁ ଓଟି ରୋଡ୍ ଓ ସିନେମା ଛକ ମଧ୍ୟରେ ସ୍ମାର୍ଟ ଲାଇଟ୍ ଏବଂ ଫୁଟପାଥ୍ ନିର୍ମାଣ।",
            category = "Local",
            timeAgo = "30 mins ago",
            source = "Balasore Municipal Wire",
            content = "Traffic congestion between Phandi Chhak, Sahadevkhunta Bus Stand, and OT Road will soon ease as the town Ring Road expansion is underway. The initiative includes decorative solar LED street lighting, pedestrian-safe brick sidewalks, roundabouts with statues of Fakir Mohan Senapati and Bagha Jatin, and green median landscaping utilizing native flowering bougainvillea shrubs.",
            odiaContent = "ଫାଣ୍ଡି ଛକ, ସହଦେବଖୁଣ୍ଟା ବସ୍ ଷ୍ଟାଣ୍ଡ ଓ ଓଟି ରୋଡରେ ଟ୍ରାଫିକ୍ ସମସ୍ୟାର ସମାଧାନ ପାଇଁ ରିଙ୍ଗ ରୋଡ୍ ନିର୍ମାଣ ଜାରି ରହିଛି। ବ୍ୟାସକବି ଫକୀର ମୋହନ ସେନାପତି ଏବଂ ବାଘା ଯତୀନଙ୍କ ପ୍ରତିମୂର୍ତ୍ତି ସହ ସୌନ୍ଦର୍ଯ୍ୟକରଣ, ସ୍ମାର୍ଟ ଏଲଇଡି ଲାଇଟ୍ ଓ ପଦଚାରୀ ରାସ୍ତା ନିର୍ମାଣ କରାଯାଉଛି।"
        ),
        NewsArticle(
            id = "news_9",
            title = "Balaramgadi Port Modern Marine Fish & Sea Crab Trading Hub Inaugurated",
            odiaTitle = "ବଳରାମଗଡ଼ି ମୁହାଣରେ ଆଧୁନିକ ମାଛ ଓ କଙ୍କଡ଼ା ବଜାର ଉଦ୍ଘାଟିତ",
            snippet = "Advanced cold chain lockers and direct coastal auction platforms opened for local fishermen, boosting aquaculture exports.",
            odiaSnippet = "ମତ୍ସ୍ୟଜୀବୀମାନଙ୍କ ସୁବିଧା ପାଇଁ ଅତ୍ୟାଧୁନିକ ଶୀତଳ ଭଣ୍ଡାର ଓ ନିଲାମ କେନ୍ଦ୍ର କାର୍ଯ୍ୟକ୍ଷମ ହୋଇଛି।",
            category = "Local",
            timeAgo = "2 hours ago",
            source = "Coastal Odisha Bureau",
            content = "The scenic estuary where the Budhabalanga River joins the Bay of Bengal at Balaramgadi has received a major technological boost with the opening of a modern fish landing terminal. Featuring computerized digital auction boards, flake-ice plants, hygienic gutting sheds, and direct cold-storage vans, local fishermen can now sell prized Hilsa (Ilish), pomfret, and tiger prawns without middleman deductions.",
            odiaContent = "ବୁଢ଼ାବଳଙ୍ଗ ନଦୀ ମୁହାଣ ବଳରାମଗଡ଼ି ଠାରେ ନୂତନ ମତ୍ସ୍ୟ ଅବତରଣ କେନ୍ଦ୍ର ଖୋଲିଛି। କମ୍ପ୍ୟୁଟରୀକୃତ ନିଲାମ ପ୍ରଣାଳୀ, ବରଫ କାରଖାନା ଓ ଶୀତଳ ଭଣ୍ଡାର ମାଧ୍ୟମରେ ସ୍ଥାନୀୟ ମତ୍ସ୍ୟଜୀବୀମାନେ ସିଧାସଳଖ ଇଲିସି, ଚିଙ୍ଗୁଡ଼ି ଓ କଙ୍କଡ଼ାର ଉଚିତ୍ ମୂଲ୍ୟ ପାଇପାରିବେ।"
        ),
        NewsArticle(
            id = "news_10",
            title = "IMD Issues Bay of Bengal Weather Alert: High Tide & Evening Showers for Balasore Coast",
            odiaTitle = "ବାଲେଶ୍ୱର ଉପକୂଳରେ କାଳବୈଶାଖୀ ଓ ଉଚ୍ଚ ଜୁଆର ସତର୍କତା ଜାରି କଲା ପାଣିପାଗ ବିଭାଗ",
            snippet = "Coastal gusts of 35-45 km/h predicted near Chandipur and Talasari; fishermen advised to stay within designated safe harbor zones.",
            odiaSnippet = "ଚାନ୍ଦିପୁର ଏବଂ ତାଳସାରୀ ଉପକୂଳରେ ୩୫ ରୁ ୪୫ କିମି ବେଗରେ ପବନ ଓ ବର୍ଷା ସମ୍ଭାବନା, ମତ୍ସ୍ୟଜୀବୀଙ୍କୁ ସତର୍କ ରହିବାକୁ ପରାମର୍ଶ।",
            category = "Weather",
            timeAgo = "40 mins ago",
            source = "IMD Coastal Warning Center",
            content = "The India Meteorological Department (IMD) regional centre in Bhubaneswar has warned of a low-pressure formation over the northwest Bay of Bengal triggering squally winds of 35-45 km/h along Chandipur, Kasafal, and Talasari beaches. Sea conditions will remain rough during evening high tide. Beachgoers are cautioned not to venture past designated boundary markers, and mechanized fishing boats have returned to safe anchorages.",
            odiaContent = "ଭାରତୀୟ ପାଣିପାଗ ବିଭାଗ (IMD) ପକ୍ଷରୁ ଉତ୍ତର-ପଶ୍ଚିମ ବଙ୍ଗୋପସାଗରରେ ସୃଷ୍ଟ ଲଘୁଚାପ ଯୋଗୁଁ ଚାନ୍ଦିପୁର, କସାଫଳ ଓ ତାଳସାରୀ ଉପକୂଳରେ ଘଣ୍ଟାପ୍ରତି ୩୫-୪୫ କିଲୋମିଟର ବେଗରେ ଝଡ଼ ପବନ ଓ ସମୁଦ୍ର ଅଶାନ୍ତ ରହିବା ନେଇ ସତର୍କତା ଜାରି କରାଯାଇଛି। ସନ୍ଧ୍ୟା ଜୁଆର ସମୟରେ ପର୍ଯ୍ୟଟକମାନଙ୍କୁ ସମୁଦ୍ର ମଧ୍ୟକୁ ନଯିବାକୁ ଅନୁରୋଧ କରାଯାଇଛି।"
        ),
        NewsArticle(
            id = "news_11",
            title = "Chandipur Vanishing Sea Records Spring Low Tide Receding 5.2 km into Bay",
            odiaTitle = "ଚାନ୍ଦିପୁର ବେଳାଭୂମିରେ ୫.୨ କିଲୋମିଟର ପଛକୁ ହଟିଲା ସମୁଦ୍ର; ପର୍ଯ୍ୟଟକଙ୍କ ଭିଡ଼",
            snippet = "Unique tidal phenomenon exposes extensive intertidal sandbars, rare red ghost crabs, and endangered horseshoe crab habitats.",
            odiaSnippet = "ଅପରାହ୍ନରେ ସମୁଦ୍ର ଜଳ ପଛକୁ ହଟିବା ପରେ ପର୍ଯ୍ୟଟକମାନେ ଚାଲି ଚାଲି ଗଭୀର ସମୁଦ୍ର ଶଯ୍ୟାର ଦୃଶ୍ୟ ଉପଭୋଗ କରୁଛନ୍ତି।",
            category = "Weather",
            timeAgo = "4 hours ago",
            source = "Odisha Coastal Ecology",
            content = "Nature lovers and coastal researchers flocked to Chandipur Beach this afternoon as the astronomical spring tide caused the shallow continental shelf water to recede an extraordinary 5.2 kilometers. The dry intertidal seabed revealed colonies of red ghost crabs scurrying along mudflats and several pairs of ancient horseshoe crabs (living fossils). Marine biology volunteers accompanied visitors to educate them on biodiversity conservation while strictly ensuring everyone returned before the tidal reversal.",
            odiaContent = "ଚାନ୍ଦିପୁର ବେଳାଭୂମିରେ ପ୍ରାକୃତିକ ଭଟ୍ଟା ସମୟରେ ସମୁଦ୍ର ଜଳରାଶି ପ୍ରାୟ ୫.୨ କିଲୋମିଟର ପଛକୁ ଅପସାରିତ ହୋଇଛି। ବାଲୁକା ଶଯ୍ୟାରେ ବିରଳ ଲାଲ କଙ୍କଡ଼ା ଓ ଜୀବନ୍ତ ଜୀବାଶ୍ମ କୁହାଯାଉଥିବା ରାଜକଙ୍କଡ଼ା (Horseshoe Crab) ଦେଖିବାକୁ ପର୍ଯ୍ୟଟକଙ୍କ ଭିଡ଼ ଜମିଥିଲା। ସମୁଦ୍ର ଫେରିବା ପୂର୍ବରୁ ଫେରିଆସିବା ପାଇଁ ଲାଇଫଗାର୍ଡମାନେ ସତର୍କ କରାଇଥିଲେ।"
        ),
        NewsArticle(
            id = "news_12",
            title = "District Red Cross & DHH Balasore Activate 24x7 Emergency Blood & Heat Relief Cells",
            odiaTitle = "ବାଲେଶ୍ୱର ରେଡ୍ କ୍ରସ୍ ଓ ମୁଖ୍ୟ ଡାକ୍ତରଖାନାରେ ୨୪ ଘଣ୍ଟିଆ ଜରୁରୀକାଳୀନ ରକ୍ତ ସେବା କାର୍ଯ୍ୟକ୍ଷମ",
            snippet = "Specialized rapid response emergency units and on-call donor registry mobilized to support coastal trauma and maternity centers.",
            odiaSnippet = "ଜରୁରୀକାଳୀନ ଚିକିତ୍ସା ଓ ରକ୍ତ ସେବାକୁ ତ୍ୱରାନ୍ୱିତ କରିବା ପାଇଁ ସ୍ୱତନ୍ତ୍ର ଡାକ୍ତରୀ ଦଳ ଓ ଦାତା ନେଟୱାର୍କ ପ୍ରସ୍ତୁତ।",
            category = "Emergency",
            timeAgo = "1 hour ago",
            source = "DHH Emergency Desk",
            content = "The Indian Red Cross Society Balasore District Branch and District Headquarters Hospital (DHH) have streamlined their 24x7 emergency blood bank inventory. A special helpline (+91 6782 262002) has been activated for O-negative, AB-negative, and rare blood components. Dedicated mobile vans and cooling hydration kiosks have also been stationed across Sahadevkhunta, Station Square, and Remuna to assist heat-affected patients and highway trauma cases.",
            odiaContent = "ବାଲେଶ୍ୱର ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ (DHH) ଓ ରେଡକ୍ରସ ରକ୍ତଭଣ୍ଡାର ପକ୍ଷରୁ ୨୪ ଘଣ୍ଟିଆ ଜରୁରୀକାଳୀନ ହେଲ୍ପଲାଇନ ସେବା ଆରମ୍ଭ କରାଯାଇଛି। ଦୁର୍ଲଭ ନେଗେଟିଭ୍ ରକ୍ତ ଗ୍ରୁପ୍ ଆବଶ୍ୟକ କରୁଥିବା ରୋଗୀ ଓ ଟ୍ରମା କେନ୍ଦ୍ର ପାଇଁ ସ୍ୱତନ୍ତ୍ର ଦାତା ନେଟୱାର୍କ ସହ ସହଦେବଖୁଣ୍ଟା ଓ ଷ୍ଟେସନ ଛକରେ ସ୍ୱାସ୍ଥ୍ୟ ସହାୟତା ଶିବିର ଖୋଲାଯାଇଛି।"
        ),
        NewsArticle(
            id = "news_13",
            title = "Talasari & Bichitrapur Coastal Mangrove Eco-Tourism Expansion Announced",
            odiaTitle = "ତାଳସାରୀ ଓ ବିଚିତ୍ରପୁର ଉପକୂଳ ଇକୋ-ଟୁରିଜମ୍ ସମ୍ପ୍ରସାରଣ ଘୋଷଣା",
            snippet = "Modern eco-cottages, solar speedboats, and protected mudflat trails launched to preserve red ghost crab zones.",
            odiaSnippet = "ତାଳସାରୀ ବେଳାଭୂମି ଓ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବନରେ ପର୍ଯ୍ୟଟକଙ୍କ ପାଇଁ ସୌର ବୋଟିଂ ଏବଂ ପରିବେଶ ଅନୁକୂଳ କଟେଜ୍ ସୁବିଧା।",
            category = "Coastal",
            timeAgo = "2 hours ago",
            source = "Odisha Coastal Tourism Board",
            content = "The Forest and Tourism Department has sanctioned an extensive eco-tourism upgrade along the Talasari-Udaipur beach belt and Bichitrapur mangrove wetlands at the Odisha-West Bengal coastal border. The project incorporates battery-operated solar safari boats, elevated timber nature walkways across mangrove mudflats, and dedicated conservation zones to safeguard the nesting grounds of red ghost crabs. Trained local fishermen self-help groups have been deployed as certified ecoguides to guide coastal travelers.",
            odiaContent = "ଜଙ୍ଗଲ ଓ ପର୍ଯ୍ୟଟନ ବିଭାଗ ପକ୍ଷରୁ ତାଳସାରୀ ବେଳାଭୂମି ଏବଂ ବିଚିତ୍ରପୁର ହେନ୍ତାଳବନ ଅଞ୍ଚଳରେ ଇକୋ-ଟୁରିଜମ୍ ପ୍ରକଳ୍ପ ଆରମ୍ଭ ହୋଇଛି। ସୌରଚାଳିତ ବୋଟିଂ, କାଠର ପ୍ରକୃତି ପଦଚାରୀ ରାସ୍ତା ଏବଂ ବିରଳ ଲାଲ କଙ୍କଡ଼ା ସଂରକ୍ଷଣ ପାଇଁ ସ୍ୱତନ୍ତ୍ର ପଦକ୍ଷେପ ନିଆଯାଇଛି। ସ୍ଥାନୀୟ ମତ୍ସ୍ୟଜୀବୀମାନଙ୍କୁ ପ୍ରଶିକ୍ଷଣ ଦିଆଯାଇ ଇକୋ-ଗାଇଡ୍ ଭାବେ ନିଯୁକ୍ତି ଦିଆଯାଇଛି।"
        ),
        NewsArticle(
            id = "news_14",
            title = "Panchalingeswar Temple Perennial Mountain Stream Darshan Facilitated for Devotees",
            odiaTitle = "ପବିତ୍ର ପାହାଡ଼ୀ ଝରଣା ଦର୍ଶନ ପାଇଁ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପୀଠରେ ଶ୍ରଦ୍ଧାଳୁଙ୍କ ଗହଳି",
            snippet = "District administration installs anti-slip safety railings and queue management shades along the Devagiri hill shrine.",
            odiaSnippet = "ନୀଳଗିରି ଦେବଗିରି ପାହାଡ଼ ଉପରେ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପୀଠରେ ଶ୍ରଦ୍ଧାଳୁଙ୍କ ସୁରକ୍ଷା ପାଇଁ ଷ୍ଟେନଲେସ ଷ୍ଟିଲ ରେଲିଂ ଓ ଛାତ ନିର୍ମିତ।",
            category = "Spiritual",
            timeAgo = "3 hours ago",
            source = "Balasore Devaswom Board",
            content = "Devotees visiting the sacred Panchalingeswar Temple atop Devagiri Hill in Nilagiri can now experience hassle-free worship of the five Shiva Lingas constantly submerged in cold perennial spring water. The district administration and endowment commission have installed non-slip textured stone pathways, stainless steel safety banisters along the waterfall steps, and drinking water kiosks. Special medical aid outposts have been set up for senior citizen pilgrims hiking up the sacred hill.",
            odiaContent = "ନୀଳଗିରିର ଦେବଗିରି ପାହାଡ଼ ଶୀର୍ଷରେ ଅବସ୍ଥିତ ପ୍ରସିଦ୍ଧ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ମନ୍ଦିରରେ ସ୍ୱତନ୍ତ୍ର ସୁରକ୍ଷା ବ୍ୟବସ୍ଥା କରାଯାଇଛି। ସବୁଦିନିଆ ପାହାଡ଼ୀ ଝରଣା ପାଣିରେ ବୁଡ଼ି ରହିଥିବା ପାଞ୍ଚଟି ଶିବଲିଙ୍ଗଙ୍କୁ ଦର୍ଶନ କରିବା ପାଇଁ ଷ୍ଟେନଲେସ୍ ଷ୍ଟିଲ ରେଲିଂ, ଅଣ-ଖସଡ଼ା ପଥର ଚଟାଣ ଓ ପାନୀୟ ଜଳ ବ୍ୟବସ୍ଥା ହୋଇଛି।"
        ),
        NewsArticle(
            id = "news_15",
            title = "Emami Jagannath Temple Januganj Hosts Grand Evening Alati & Odissi Devotional Recital",
            odiaTitle = "ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିରରେ ସ୍ୱତନ୍ତ୍ର ସନ୍ଧ୍ୟା ଆଳତି ଓ ଓଡ଼ିଶୀ ଭଜନ ସନ୍ଧ୍ୟା",
            snippet = "Centuries of Utkal spiritual heritage celebrated in the modern Kalinga-style stone courtyard with sacred Amruta Keli distribution.",
            odiaSnippet = "ଜାନୁଗଞ୍ଜ ସ୍ଥିତ କଳିଙ୍ଗ ଶୈଳୀର ପ୍ରାଚୀନ କାରୁକାର୍ଯ୍ୟପୂର୍ଣ୍ଣ ମନ୍ଦିରରେ ହଜାର ହଜାର ଭକ୍ତଙ୍କ ସମାଗମ।",
            category = "Spiritual",
            timeAgo = "5 hours ago",
            source = "Shri Jagannath Seva Sangha",
            content = "The majestic Emami Jagannath Temple at Remuna-Januganj bypass attracted thousands of coastal devotees for the special Sandhya Alati and classical Odissi flute performance. The sacred shrine complex, renowned for its ornate Kalinga stone sculptures and lush surrounding flora, held continuous devotional chanting. Temple trust volunteers ensured disciplined darshan for visitors traveling from across Balasore, Mayurbhanj, and West Bengal.",
            odiaContent = "ବାଲେଶ୍ୱର ଜାନୁଗଞ୍ଜ ସ୍ଥିତ ଏମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର ପରିସରରେ ମନୋରମ ସନ୍ଧ୍ୟା ଆଳତି ଓ ଶାସ୍ତ୍ରୀୟ ଓଡ଼ିଶୀ ବଂଶୀ ବାଦନ ଅନୁଷ୍ଠିତ ହୋଇଛି। କଳିଙ୍ଗ ସ୍ଥାପତ୍ୟ କଳାର ଏହି ଭବ୍ୟ ମନ୍ଦିରରେ ଭକ୍ତମାନେ ଶାନ୍ତିପୂର୍ଣ୍ଣ ଭାବେ ଶ୍ରୀଜଗନ୍ନାଥଙ୍କ ଦର୍ଶନ କରିବା ସହ ପ୍ରସାଦ ସେବନ କରିଛନ୍ତି।"
        ),
        NewsArticle(
            id = "news_16",
            title = "Chandipur Marine Life Volunteers Tag Endangered Horseshoe Crabs During Vanishing Tide",
            odiaTitle = "ଚାନ୍ଦିପୁର ବେଳାଭୂମିରେ ରାଜକଙ୍କଡ଼ା (ହର୍ସସୁ କ୍ରାବ୍) ଗବେଷଣା ଓ ଟ୍ୟାଗିଂ କାର୍ଯ୍ୟକ୍ରମ",
            snippet = "FM University zoology department and state biodiversity council monitor living fossil colonies along intertidal mudflats.",
            odiaSnippet = "ସମୁଦ୍ର ଅପସାରିତ ହେବା ପରେ ବାଲୁକା ଶଯ୍ୟାରେ ବିରଳ ରାଜକଙ୍କଡ଼ାଙ୍କ ସଂରକ୍ଷଣ ଉପରେ ଗବେଷକଙ୍କ ଦଳ କାର୍ଯ୍ୟ କରୁଛନ୍ତି।",
            category = "Coastal",
            timeAgo = "6 hours ago",
            source = "FMU Marine Biology Department",
            content = "A joint research unit from Fakir Mohan University and Odisha Biodiversity Board conducted a field census of living fossil horseshoe crabs (Tachypleus gigas and Carcinoscorpius rotundicauda) exposed on the intertidal flats during Chandipur's vanishing low tide. Over 65 specimens were weighed, tagged with microscopic tracking bands, and safely returned to deep tidal channels. The team urged beachgoers to avoid stepping on sand ripples where crabs burrow to lay eggs.",
            odiaContent = "ଫକୀର ମୋହନ ବିଶ୍ୱବିଦ୍ୟାଳୟ ପ୍ରାଣୀବିଜ୍ଞାନ ବିଭାଗ ଓ ରାଜ୍ୟ ଜୈବବିବିଧତା ବୋର୍ଡ ପକ୍ଷରୁ ଚାନ୍ଦିପୁର ବେଳାଭୂମିରେ ବିରଳ ଜୀବନ୍ତ ଜୀବାଶ୍ମ ରାଜକଙ୍କଡ଼ାଙ୍କ ଉପରେ ସର୍ଭେ କରାଯାଇଛି। ୬୫ ରୁ ଅଧିକ ରାଜକଙ୍କଡ଼ାଙ୍କୁ ଟ୍ୟାଗିଂ କରି ସୁରକ୍ଷିତ ଭାବେ ସମୁଦ୍ରକୁ ଫେରାଇ ଦିଆଯାଇଛି।"
        )
    )

    val emergencyContacts = listOf(
        EmergencyContact(
            id = "em_1",
            name = "Balasore Police Control Room",
            odiaName = "ବାଲେଶ୍ୱର ପୋଲିସ ନିୟନ୍ତ୍ରଣ କକ୍ଷ",
            number = "112",
            category = "Police & Safety",
            is24x7 = true,
            description = "Central emergency response for Balasore town & district"
        ),
        EmergencyContact(
            id = "em_2",
            name = "Fakir Mohan Medical College (DHH Balasore)",
            odiaName = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ହସ୍ପିଟାଲ",
            number = "06782-262024",
            category = "Hospitals & Medical",
            is24x7 = true,
            description = "Emergency trauma ward, ICU, and Outpatient emergency"
        ),
        EmergencyContact(
            id = "em_3",
            name = "Balasore Fire & Disaster Response",
            odiaName = "ବାଲେଶ୍ୱର ଅଗ୍ନିଶମ ଓ ବିପର୍ଯ୍ୟୟ ସେବା",
            number = "101",
            category = "Fire & Rescue",
            is24x7 = true,
            description = "Fire brigade, tree clearance & flood rescue"
        ),
        EmergencyContact(
            id = "em_4",
            name = "Balasore Red Cross Blood Bank",
            odiaName = "ରେଡ କ୍ରସ୍ ରକ୍ତ ଭଣ୍ଡାର (ବାଲେଶ୍ୱର)",
            number = "06782-262143",
            category = "Hospitals & Medical",
            is24x7 = true,
            description = "24/7 blood availability and emergency donor network"
        ),
        EmergencyContact(
            id = "em_5",
            name = "Women Helpline Balasore",
            odiaName = "ମହିଳା ହେଲ୍ପଲାଇନ",
            number = "181",
            category = "Police & Safety",
            is24x7 = true,
            description = "Confidential crisis intervention & legal aid"
        ),
        EmergencyContact(
            id = "em_6",
            name = "Balasore Railway Helpline (BLS Station)",
            odiaName = "ବାଲେଶ୍ୱର ରେଳବାଇ ସହାୟତା",
            number = "139",
            category = "Transport & Transit",
            is24x7 = true,
            description = "Live train running status, PNR & platform assistance"
        )
    )

    val weather = WeatherInfo(
        tempCelsius = 29,
        condition = "Partly Cloudy • Gentle Breeze",
        highLow = "32° / 25°",
        humidity = "72%",
        windSpeedKmh = "16 km/h (SE)",
        tideStatus = "Low Tide: 02:30 PM (Receded ~4.5 km at Chandipur)",
        seaCondition = "Calm • Excellent for beach excursions"
    )

    val forecast3Days = listOf(
        BalasoreForecastDay(
            id = "day_1",
            dayLabel = "Tomorrow",
            dateFormatted = "Thu, 17 Sep",
            odiaDayLabel = "ଆସନ୍ତାକାଲି (ଗୁରୁବାର)",
            highTempC = 31,
            lowTempC = 24,
            condition = "Scattered Coastal Showers & Bay Breeze",
            odiaCondition = "ଉପକୂଳିଆ ବିକ୍ଷିପ୍ତ ବର୍ଷା ଓ ସାମୁଦ୍ରିକ ପବନ",
            weatherIcon = "🌧️",
            rainProbability = 65,
            windSummary = "20 km/h (ENE)",
            humidity = "76%",
            uvIndex = "Moderate (5)",
            marineNotice = "Intertidal beach walk safe before 01:00 PM; mild swells off Balaramgadi estuary.",
            hourlySlots = listOf(
                BalasoreForecastHour("06:00 AM", 24, "⛅", 20, 14),
                BalasoreForecastHour("09:00 AM", 27, "⛅", 35, 18),
                BalasoreForecastHour("12:00 PM", 31, "🌧️", 65, 22),
                BalasoreForecastHour("03:00 PM", 29, "🌦️", 50, 20),
                BalasoreForecastHour("06:00 PM", 27, "☁️", 30, 16),
                BalasoreForecastHour("09:00 PM", 25, "🌙", 15, 12)
            )
        ),
        BalasoreForecastDay(
            id = "day_2",
            dayLabel = "Friday",
            dateFormatted = "Fri, 18 Sep",
            odiaDayLabel = "ଶୁକ୍ରବାର",
            highTempC = 29,
            lowTempC = 23,
            condition = "Kalbaisakhi Thunderstorm & Squall",
            odiaCondition = "କାଳବୈଶାଖୀ ଝଡ଼ ଓ ବଜ୍ରପାତ ଚେତାବନୀ",
            weatherIcon = "⚡",
            rainProbability = 85,
            windSummary = "38 km/h Gusting to 50 km/h",
            humidity = "86%",
            uvIndex = "Low (3)",
            marineNotice = "IMD Orange Alert: Fishermen strictly advised not to venture into deep sea.",
            hourlySlots = listOf(
                BalasoreForecastHour("06:00 AM", 24, "☁️", 40, 18),
                BalasoreForecastHour("09:00 AM", 26, "🌧️", 70, 26),
                BalasoreForecastHour("12:00 PM", 29, "⚡", 85, 40),
                BalasoreForecastHour("03:00 PM", 26, "⛈️", 90, 48),
                BalasoreForecastHour("06:00 PM", 25, "🌧️", 60, 30),
                BalasoreForecastHour("09:00 PM", 24, "🌧️", 45, 22)
            )
        ),
        BalasoreForecastDay(
            id = "day_3",
            dayLabel = "Saturday",
            dateFormatted = "Sat, 19 Sep",
            odiaDayLabel = "ଶନିବାର",
            highTempC = 33,
            lowTempC = 25,
            condition = "Sunny Clear Skies & Gentle Sea Breeze",
            odiaCondition = "ଫର୍ଚ୍ଚା ଖରାଟିଆ ପାଗ ଓ ସୁଲୁସୁଲିଆ ପବନ",
            weatherIcon = "☀️",
            rainProbability = 15,
            windSummary = "14 km/h (SE)",
            humidity = "68%",
            uvIndex = "Very High (8)",
            marineNotice = "Optimal calm conditions. Perfect for Chandipur 4.8 km low-tide excursion.",
            hourlySlots = listOf(
                BalasoreForecastHour("06:00 AM", 25, "🌅", 5, 10),
                BalasoreForecastHour("09:00 AM", 28, "☀️", 10, 12),
                BalasoreForecastHour("12:00 PM", 33, "☀️", 15, 16),
                BalasoreForecastHour("03:00 PM", 32, "🌤️", 10, 15),
                BalasoreForecastHour("06:00 PM", 28, "🌇", 5, 14),
                BalasoreForecastHour("09:00 PM", 26, "✨", 0, 10)
            )
        )
    )

    val transitSchedules = listOf(
        TransitSchedule(
            id = "tr_1",
            routeName = "Balasore - Bhubaneswar Vande Bharat Express",
            origin = "Balasore (BLS)",
            destination = "Bhubaneswar (BBS)",
            time = "07:15 AM",
            type = "Train",
            status = "On Time"
        ),
        TransitSchedule(
            id = "tr_2",
            routeName = "Howrah - Puri Superfast Express",
            origin = "Balasore (BLS)",
            destination = "Puri (PURI)",
            time = "11:40 AM",
            type = "Train",
            status = "On Time"
        ),
        TransitSchedule(
            id = "tr_3",
            routeName = "OSRTC Volvo AC Bus: Balasore to Cuttack",
            origin = "Sahadevkhunta Bus Stand",
            destination = "Badambadi, Cuttack",
            time = "08:30 AM",
            type = "Bus",
            status = "Scheduled"
        ),
        TransitSchedule(
            id = "tr_4",
            routeName = "Balasore Local Shuttle: Station to Chandipur",
            origin = "Balasore Railway Station",
            destination = "OTDC Panthanivas Chandipur",
            time = "Every 30 Mins",
            type = "Bus",
            status = "Active Frequency"
        )
    )

    fun getInstance(context: Context): BalasoreRepository = this

    fun isCacheOlderThan24Hours(): Boolean = false

    suspend fun syncAllData(forceNetwork: Boolean = false): SyncResult = SyncResult(
        isSuccess = true,
        weatherUpdated = true,
        newsArticlesCount = newsArticles.size,
        hotspotsCount = hotspots.size,
        timestamp = System.currentTimeMillis(),
        isOfflineServed = false,
        message = "Sync completed successfully"
    )
}

data class SyncResult(
    val isSuccess: Boolean = true,
    val weatherUpdated: Boolean = true,
    val newsArticlesCount: Int = 0,
    val hotspotsCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isOfflineServed: Boolean = false,
    val message: String = ""
)
