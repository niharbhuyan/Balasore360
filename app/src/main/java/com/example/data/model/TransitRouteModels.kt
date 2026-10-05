package com.example.data.model

import com.google.android.gms.maps.model.LatLng

/**
 * Data model for a local Bus Stop along a bus route.
 */
data class BusStopPoint(
    val id: String,
    val nameEn: String,
    val nameOr: String,
    val lat: Double,
    val lng: Double,
    val scheduledDepartureOffsetMins: Int,
    val isTransferHub: Boolean = false,
    val landmarkInfo: String = ""
)

/**
 * Data model for an interactive Bus Route overlaid on Google Maps.
 */
data class BusRouteOverlay(
    val id: String,
    val routeNumber: String,
    val routeNameEn: String,
    val routeNameOr: String,
    val originName: String,
    val destinationName: String,
    val distanceKm: Double,
    val durationMins: Int,
    val fareStandard: String,
    val fareAc: String,
    val frequency: String,
    val operatingHours: String,
    val colorArgb: Int,
    val stops: List<BusStopPoint>,
    val pathPoints: List<LatLng>,
    val status: String = "ON_TIME", // ON_TIME, MODERATE_TRAFFIC, DELAYED
    val operator: String = "CRUT Mo Bus Balasore",
    val departuresSummary: List<String> = listOf("06:00 AM", "06:30 AM", "07:00 AM", "07:30 AM", "08:00 AM", "08:30 AM", "09:00 AM", "10:00 AM", "11:30 AM", "01:00 PM", "02:30 PM", "04:00 PM", "05:15 PM", "06:30 PM", "07:45 PM", "08:30 PM")
)

/**
 * Data model for Live Train Status tracking along the Howrah-Bhubaneswar-Puri railway corridor in Balasore.
 */
data class TrainStatusMarker(
    val id: String,
    val trainNumber: String,
    val trainNameEn: String,
    val trainNameOr: String,
    val type: String, // Vande Bharat, Superfast, Express, MEMU Local
    val currentLat: Double,
    val currentLng: Double,
    val speedKmh: Int,
    val statusText: String, // On Time, Delayed 8m, Approaching Platform 2
    val statusType: String, // ON_TIME, DELAYED, AT_STATION, EN_ROUTE
    val delayMinutes: Int = 0,
    val platform: String = "PF 1",
    val origin: String,
    val destination: String,
    val scheduledTimeAtBalasore: String,
    val nextStopName: String,
    val coachPositionInfo: String = "Front: Engine • Mid: C1-C8 (Chair Car) • Rear: E1-E2 (Exec)",
    val headingDegrees: Float = 195f
)

/**
 * Data model for Key Railway Stations in Balasore district.
 */
data class RailwayStationNode(
    val id: String,
    val stationCode: String,
    val nameEn: String,
    val nameOr: String,
    val lat: Double,
    val lng: Double,
    val platformsCount: Int,
    val isJunction: Boolean = false,
    val connectingLines: String = "Howrah-Chennai Mainline",
    val upcomingTrainsCount: Int = 6
)

/**
 * Pre-defined Authentic Balasore Transit & Railway Dataset.
 */
object BalasoreTransitDataSource {

    // Railway Track Polyline Points along the Howrah-Bhubaneswar Mainline through Balasore District
    val BALASORE_RAIL_CORRIDOR_POINTS = listOf(
        LatLng(21.8020, 87.2150), // Jaleswar
        LatLng(21.7450, 87.1350), // Amarda Road
        LatLng(21.6850, 87.0550), // Basta
        LatLng(21.6120, 86.9820), // Rupsa Junction
        LatLng(21.5620, 86.9480), // Haldipada
        LatLng(21.4934, 86.9135), // Balasore Junction
        LatLng(21.4420, 86.8720), // Nilagiri Road
        LatLng(21.4120, 86.8450), // Khantapada
        LatLng(21.3150, 86.8050), // Bahanaga Bazar
        LatLng(21.2850, 86.6920), // Soro
        LatLng(21.1850, 86.5820)  // Markona
    )

    // Key Railway Stations in Balasore District
    val BALASORE_RAILWAY_STATIONS = listOf(
        RailwayStationNode(
            id = "bls_jn",
            stationCode = "BLS",
            nameEn = "Balasore Junction",
            nameOr = "ବାଲେଶ୍ୱର ଜଙ୍କସନ",
            lat = 21.4934,
            lng = 86.9135,
            platformsCount = 4,
            isJunction = true,
            connectingLines = "Howrah-Chennai Mainline • SER Kharagpur Division",
            upcomingTrainsCount = 14
        ),
        RailwayStationNode(
            id = "rupsa_jn",
            stationCode = "ROP",
            nameEn = "Rupsa Junction",
            nameOr = "ରୂପସା ଜଙ୍କସନ",
            lat = 21.6120,
            lng = 86.9820,
            platformsCount = 3,
            isJunction = true,
            connectingLines = "Branch to Baripada & Bangriposi",
            upcomingTrainsCount = 6
        ),
        RailwayStationNode(
            id = "soro_stn",
            stationCode = "SORO",
            nameEn = "Soro Railway Station",
            nameOr = "ସୋରୋ ରେଳ ଷ୍ଟେସନ",
            lat = 21.2850,
            lng = 86.6920,
            platformsCount = 3,
            isJunction = false,
            connectingLines = "Howrah-Chennai Mainline",
            upcomingTrainsCount = 8
        ),
        RailwayStationNode(
            id = "jaleswar_stn",
            stationCode = "JER",
            nameEn = "Jaleswar Railway Station",
            nameOr = "ଜଳେଶ୍ୱର ରେଳ ଷ୍ଟେସନ",
            lat = 21.8020,
            lng = 87.2150,
            platformsCount = 3,
            isJunction = false,
            connectingLines = "Odisha-Bengal Border Corridor",
            upcomingTrainsCount = 10
        ),
        RailwayStationNode(
            id = "bahanaga_stn",
            stationCode = "BNBR",
            nameEn = "Bahanaga Bazar Station",
            nameOr = "ବାହାନଗା ବଜାର ଷ୍ଟେସନ",
            lat = 21.3150,
            lng = 86.8050,
            platformsCount = 3,
            isJunction = false,
            connectingLines = "Automated Electronic Interlocking Mainline",
            upcomingTrainsCount = 4
        )
    )

    // Live Train Status Markers
    val LIVE_TRAIN_STATUSES = listOf(
        TrainStatusMarker(
            id = "tr_vande_bharat",
            trainNumber = "20831",
            trainNameEn = "Howrah - Puri Vande Bharat Express",
            trainNameOr = "ହାୱଡ଼ା - ପୁରୀ ବନ୍ଦେ ଭାରତ ଏକ୍ସପ୍ରେସ୍",
            type = "Vande Bharat Express",
            currentLat = 21.5050,
            currentLng = 86.9180,
            speedKmh = 108,
            statusText = "Approaching Balasore PF 2 • On Time",
            statusType = "ON_TIME",
            delayMinutes = 0,
            platform = "Platform 2",
            origin = "Howrah (HWH)",
            destination = "Puri (PURI)",
            scheduledTimeAtBalasore = "09:05 AM (Dep: 09:07 AM)",
            nextStopName = "Bhadrak (BHC) - ETA 09:40 AM",
            coachPositionInfo = "Front: C1-C4 • Mid: Exec E1-E2 • Rear: C5-C8",
            headingDegrees = 195f
        ),
        TrainStatusMarker(
            id = "tr_dhauli",
            trainNumber = "12821",
            trainNameEn = "Shalimar - Puri Dhauli Superfast Express",
            trainNameOr = "ଶାଲିମାର - ପୁରୀ ଧଉଳି ସୁପରଫାଷ୍ଟ",
            type = "Superfast Express",
            currentLat = 21.6150,
            currentLng = 86.9850,
            speedKmh = 82,
            statusText = "Passed Rupsa Jn • Delayed 6 mins",
            statusType = "DELAYED",
            delayMinutes = 6,
            platform = "Platform 1",
            origin = "Shalimar (SHM)",
            destination = "Puri (PURI)",
            scheduledTimeAtBalasore = "11:15 AM (Exp: 11:21 AM)",
            nextStopName = "Balasore (BLS) - ETA 11:21 AM",
            coachPositionInfo = "Front: Gen/SL • Mid: D1-D5 (Chair Car) • Rear: CC1-CC3",
            headingDegrees = 198f
        ),
        TrainStatusMarker(
            id = "tr_coromandel",
            trainNumber = "12841",
            trainNameEn = "Howrah - MGR Chennai Coromandel Express",
            trainNameOr = "ହାୱଡ଼ା - ଚେନ୍ନାଇ କରମଣ୍ଡଳ ଏକ୍ସପ୍ରେସ୍",
            type = "Superfast Express",
            currentLat = 21.7820,
            currentLng = 87.1950,
            speedKmh = 115,
            statusText = "Crossing Jaleswar Outers • On Time",
            statusType = "ON_TIME",
            delayMinutes = 0,
            platform = "Platform 3",
            origin = "Howrah (HWH)",
            destination = "MGR Chennai Central (MAS)",
            scheduledTimeAtBalasore = "05:10 PM",
            nextStopName = "Balasore (BLS) - ETA 05:10 PM",
            coachPositionInfo = "Front: B1-B5 • Mid: A1-A2, H1 • Rear: S1-S8",
            headingDegrees = 190f
        ),
        TrainStatusMarker(
            id = "tr_memu",
            trainNumber = "08063",
            trainNameEn = "Kharagpur - Bhadrak MEMU Passenger",
            trainNameOr = "ଖଡ଼ଗପୁର - ଭଦ୍ରକ ମେମୁ ପାସେଞ୍ଜର",
            type = "Local MEMU Passenger",
            currentLat = 21.2850,
            currentLng = 86.6920,
            speedKmh = 0,
            statusText = "Halt at Soro Station • On Time",
            statusType = "AT_STATION",
            delayMinutes = 0,
            platform = "Platform 2",
            origin = "Kharagpur (KGP)",
            destination = "Bhadrak (BHC)",
            scheduledTimeAtBalasore = "07:50 AM (Departed)",
            nextStopName = "Markona (MKO) - ETA 09:02 AM",
            coachPositionInfo = "All unreserved 12-coach MEMU EMU rake",
            headingDegrees = 205f
        ),
        TrainStatusMarker(
            id = "tr_utkal",
            trainNumber = "18478",
            trainNameEn = "Yog Nagari Rishikesh - Puri Kalinga Utkal",
            trainNameOr = "ଋଷିକେଶ - ପୁରୀ କଳିଙ୍ଗ ଉତ୍କଳ ଏକ୍ସପ୍ରେସ୍",
            type = "Mail / Express",
            currentLat = 21.4380,
            currentLng = 86.8680,
            speedKmh = 88,
            statusText = "Passed Nilagiri Road • On Time",
            statusType = "EN_ROUTE",
            delayMinutes = 0,
            platform = "Platform 2",
            origin = "Yog Nagari Rishikesh (YNRK)",
            destination = "Puri (PURI)",
            scheduledTimeAtBalasore = "01:25 AM (Departed)",
            nextStopName = "Soro (SORO) - ETA 01:50 AM",
            coachPositionInfo = "Standard 24-Coach LHB Rake with 3A, 2A, 1A & SL",
            headingDegrees = 200f
        )
    )

    // Local Bus Routes with exact GPS path polylines and schedule timings
    val BALASORE_BUS_ROUTES = listOf(
        BusRouteOverlay(
            id = "mo_bus_101",
            routeNumber = "101",
            routeNameEn = "Mo Bus 101: Station ⇄ Chandipur Beach",
            routeNameOr = "ମୋ ବସ୍ ୧୦୧: ଷ୍ଟେସନ ⇄ ଚାନ୍ଦିପୁର ବେଳାଭୂମି",
            originName = "Balasore Railway Station",
            destinationName = "OTDC Panthanivas Chandipur",
            distanceKm = 16.2,
            durationMins = 35,
            fareStandard = "₹20",
            fareAc = "₹25",
            frequency = "Every 20 mins (06:00 AM - 08:30 PM)",
            operatingHours = "06:00 AM - 08:30 PM Daily",
            colorArgb = 0xFF0284C7.toInt(), // Sky Blue
            status = "ON_TIME",
            operator = "CRUT Mo Bus Balasore",
            stops = listOf(
                BusStopPoint("bs_101_1", "Balasore Railway Station", "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ", 21.4934, 86.9135, 0, true, "South Gate Bus Bay"),
                BusStopPoint("bs_101_2", "Cinema Bazar / Golmei Chhak", "ସିନେମା ବଜାର ଛକ", 21.4905, 86.9240, 5, false, "Town Commercial Core"),
                BusStopPoint("bs_101_3", "Sahadevkhunta Main Bus Stand", "ସହଦେବଖୁଣ୍ଟା କେନ୍ଦ୍ରୀୟ ବସ୍ ଷ୍ଟାଣ୍ଡ", 21.4875, 86.9420, 10, true, "Central Intercity Bus Terminal"),
                BusStopPoint("bs_101_4", "ITI Square & Chhak", "ଆଇଟିଆଇ ଛକ", 21.4820, 86.9600, 16, false, "Institutional Zone"),
                BusStopPoint("bs_101_5", "Mirzapur / Marine Police", "ମିର୍ଜାପୁର ମେରାଇନ୍ ଥାନା", 21.4740, 86.9850, 24, false, "Coastal Highway"),
                BusStopPoint("bs_101_6", "Balaramgadi Mouth Bypass", "ବଳରାମଗଡ଼ି ମୁହାଣ ଛକ", 21.4720, 87.0050, 29, false, "Fishing Harbor Junction"),
                BusStopPoint("bs_101_7", "Chandipur Beach & OTDC", "ଚାନ୍ଦିପୁର ବେଳାଭୂମି ଓ ପନ୍ଥନିବାସ", 21.4695, 87.0185, 35, true, "Vanishing Sea Terminus")
            ),
            pathPoints = listOf(
                LatLng(21.4934, 86.9135),
                LatLng(21.4920, 86.9180),
                LatLng(21.4905, 86.9240),
                LatLng(21.4880, 86.9360),
                LatLng(21.4875, 86.9420),
                LatLng(21.4840, 86.9520),
                LatLng(21.4820, 86.9600),
                LatLng(21.4780, 86.9740),
                LatLng(21.4740, 86.9850),
                LatLng(21.4730, 86.9980),
                LatLng(21.4720, 87.0050),
                LatLng(21.4705, 87.0135),
                LatLng(21.4695, 87.0185)
            )
        ),
        BusRouteOverlay(
            id = "mo_bus_102",
            routeNumber = "102",
            routeNameEn = "Mo Bus 102: Station ⇄ Remuna Gopinath Temple",
            routeNameOr = "ମୋ ବସ୍ ୧୦୨: ଷ୍ଟେସନ ⇄ ରେମୁଣା ଗୋପୀନାଥ ମନ୍ଦିର",
            originName = "Balasore Railway Station",
            destinationName = "Khirachora Gopinath Temple Gate",
            distanceKm = 10.5,
            durationMins = 25,
            fareStandard = "₹15",
            fareAc = "₹20",
            frequency = "Every 25 mins (06:15 AM - 08:00 PM)",
            operatingHours = "06:15 AM - 08:00 PM Daily",
            colorArgb = 0xFFD97706.toInt(), // Amber / Orange
            status = "ON_TIME",
            operator = "CRUT Mo Bus Balasore",
            stops = listOf(
                BusStopPoint("bs_102_1", "Balasore Railway Station", "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ", 21.4934, 86.9135, 0, true, "North Gate Auto/Bus Bay"),
                BusStopPoint("bs_102_2", "Phandi Chhak & Shanti Kanan", "ଫାଣ୍ଡି ଛକ ଓ ଶାନ୍ତି କାନନ", 21.5015, 86.9210, 6, false, "Fakir Mohan Memorial"),
                BusStopPoint("bs_102_3", "Januganj / Emami Jagannath", "ଜାନୁଗଞ୍ଜ ଇମାମି ଜଗନ୍ନାଥ ମନ୍ଦିର", 21.5050, 86.8980, 12, true, "Modern Kalinga Temple"),
                BusStopPoint("bs_102_4", "Remuna Golei NH-16 Chhak", "ରେମୁଣା ଗୋଲେଇ ଜାତୀୟ ରାଜପଥ", 21.5180, 86.8920, 18, true, "Highway Interchange"),
                BusStopPoint("bs_102_5", "Remuna Bazar / Block Office", "ରେମୁଣା ବଜାର", 21.5240, 86.8810, 22, false, "Heritage Artisan Hub"),
                BusStopPoint("bs_102_6", "Khirachora Gopinath Temple", "କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମନ୍ଦିର ଦ୍ୱାର", 21.5285, 86.8715, 25, true, "Sacred Amruta Keli Shrine")
            ),
            pathPoints = listOf(
                LatLng(21.4934, 86.9135),
                LatLng(21.4980, 86.9170),
                LatLng(21.5015, 86.9210),
                LatLng(21.5030, 86.9080),
                LatLng(21.5050, 86.8980),
                LatLng(21.5110, 86.8950),
                LatLng(21.5180, 86.8920),
                LatLng(21.5220, 86.8860),
                LatLng(21.5240, 86.8810),
                LatLng(21.5265, 86.8760),
                LatLng(21.5285, 86.8715)
            )
        ),
        BusRouteOverlay(
            id = "mo_bus_103",
            routeNumber = "103",
            routeNameEn = "Mo Bus 103: Station ⇄ Nilagiri & Panchalingeswar",
            routeNameOr = "ମୋ ବସ୍ ୧୦୩: ଷ୍ଟେସନ ⇄ ନୀଳଗିରି ଓ ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର",
            originName = "Balasore Railway Station",
            destinationName = "Panchalingeswar Foothill Bus Bay",
            distanceKm = 31.0,
            durationMins = 55,
            fareStandard = "₹35",
            fareAc = "₹45",
            frequency = "Every 45 mins (06:30 AM - 06:30 PM)",
            operatingHours = "06:30 AM - 06:30 PM Daily",
            colorArgb = 0xFF059669.toInt(), // Emerald Green
            status = "ON_TIME",
            operator = "CRUT Mo Bus / OSRTC",
            stops = listOf(
                BusStopPoint("bs_103_1", "Balasore Railway Station", "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ", 21.4934, 86.9135, 0, true, "South Gate Bay"),
                BusStopPoint("bs_103_2", "Sergarh Toll Plaza NH-16", "ସେରଗଡ଼ ଟୋଲ୍ ପ୍ଲାଜା", 21.4420, 86.8520, 16, true, "NH-16 Flyover"),
                BusStopPoint("bs_103_3", "Mitrapur Forest Gate", "ମିତ୍ରପୁର ଫରେଷ୍ଟ ଗେଟ୍", 21.4400, 86.7900, 30, false, "Eco-Tourism Zone"),
                BusStopPoint("bs_103_4", "Nilagiri Town / Rajbati", "ନୀଳଗିରି ରାଜବାଟୀ ଛକ", 21.4550, 86.7620, 42, true, "Sub-divisional Center"),
                BusStopPoint("bs_103_5", "Panchalingeswar Foot Hill", "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ ତଳ", 21.4320, 86.7180, 55, true, "OTDC Panthasala Bay")
            ),
            pathPoints = listOf(
                LatLng(21.4934, 86.9135),
                LatLng(21.4750, 86.8920),
                LatLng(21.4580, 86.8720),
                LatLng(21.4420, 86.8520),
                LatLng(21.4410, 86.8200),
                LatLng(21.4400, 86.7900),
                LatLng(21.4480, 86.7750),
                LatLng(21.4550, 86.7620),
                LatLng(21.4450, 86.7400),
                LatLng(21.4380, 86.7280),
                LatLng(21.4320, 86.7180)
            )
        ),
        BusRouteOverlay(
            id = "osrtc_talasari",
            routeNumber = "OSRTC-201",
            routeNameEn = "OSRTC Express: Sahadevkhunta ⇄ Jaleswar ⇄ Talasari",
            routeNameOr = "ଓଏସଆରଟିସି: ସହଦେବଖୁଣ୍ଟା ⇄ ଜଳେଶ୍ୱର ⇄ ତାଳସାରୀ",
            originName = "Sahadevkhunta Central Bus Stand",
            destinationName = "Talasari Beach OTDC Terminal",
            distanceKm = 88.0,
            durationMins = 130,
            fareStandard = "₹95",
            fareAc = "₹130",
            frequency = "Hourly Departures (05:30 AM - 07:00 PM)",
            operatingHours = "05:30 AM - 07:00 PM Daily",
            colorArgb = 0xFF7C3AED.toInt(), // Royal Purple
            status = "ON_TIME",
            operator = "OSRTC (Odisha State Road Transport Corporation)",
            stops = listOf(
                BusStopPoint("bs_201_1", "Sahadevkhunta Central Bus Stand", "ସହଦେବଖୁଣ୍ଟା କେନ୍ଦ୍ରୀୟ ବସ୍ ଷ୍ଟାଣ୍ଡ", 21.4875, 86.9420, 0, true, "Platform 4 OSRTC"),
                BusStopPoint("bs_201_2", "Basta Junction / River Bridge", "ବସ୍ତା ଜଙ୍କସନ", 21.6850, 87.0550, 35, true, "Subarnarekha Basin"),
                BusStopPoint("bs_201_3", "Jaleswar Station Chhak", "ଜଳେଶ୍ୱର ଷ୍ଟେସନ ଛକ", 21.8020, 87.2150, 65, true, "Bengal Border Highway"),
                BusStopPoint("bs_201_4", "Bhograi Block Market", "ଭୋଗରାଇ ବ୍ଲକ୍ ମାର୍କେଟ", 21.6500, 87.3800, 95, false, "Rural Trade Hub"),
                BusStopPoint("bs_201_5", "Chandaneswar Temple Terminus", "ଚନ୍ଦନେଶ୍ୱର ଶୈବପୀଠ ଟର୍ମିନାଲ", 21.6180, 87.4820, 115, true, "Pilgrimage Center"),
                BusStopPoint("bs_201_6", "Talasari Beach OTDC Terminal", "ତାଳସାରୀ ବେଳାଭୂମି ଟର୍ମିନାଲ", 21.6060, 87.4520, 130, true, "Red Ghost Crab Coast")
            ),
            pathPoints = listOf(
                LatLng(21.4875, 86.9420),
                LatLng(21.5420, 86.9850),
                LatLng(21.6120, 87.0150),
                LatLng(21.6850, 87.0550),
                LatLng(21.7450, 87.1250),
                LatLng(21.8020, 87.2150),
                LatLng(21.7300, 87.3100),
                LatLng(21.6500, 87.3800),
                LatLng(21.6250, 87.4400),
                LatLng(21.6180, 87.4820),
                LatLng(21.6060, 87.4520)
            )
        )
    )
}
