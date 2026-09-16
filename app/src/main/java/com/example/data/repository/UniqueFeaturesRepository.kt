package com.example.data.repository

import com.example.data.model.*

object UniqueFeaturesRepository {

    // 1. Horseshoe Crab Biodiversity & Eco-Guidelines
    val horseshoeCrabSightings = listOf(
        HorseshoeCrabSighting(
            id = "hsc_1",
            reporterName = "Dr. Subhasis Mohapatra (FMU Marine Biology)",
            beachSector = "Chandipur Outer Sandbar (3.2 km seaward)",
            distanceOutKm = 3.2,
            species = "Tachypleus gigas (Indo-Pacific Horseshoe Crab)",
            count = 4,
            condition = "Active Mating Pair in shallow tide pool",
            reportedAgo = "2 hours ago",
            verifiedByFMU = true
        ),
        HorseshoeCrabSighting(
            id = "hsc_2",
            reporterName = "Debasis Patra (Local Marine Naturalist)",
            beachSector = "Balaramgadi Mudflat Confluence",
            distanceOutKm = 1.8,
            species = "Carcinoscorpius rotundicauda (Mangrove Horseshoe Crab)",
            count = 2,
            condition = "Safely returned to deep estuary channel",
            reportedAgo = "5 hours ago",
            verifiedByFMU = true
        ),
        HorseshoeCrabSighting(
            id = "hsc_3",
            reporterName = "Priyabrata Jena (Coastal Volunteer)",
            beachSector = "Mirzapur Casuarina Shoreline",
            distanceOutKm = 2.4,
            species = "Tachypleus gigas",
            count = 1,
            condition = "Burrowing in intertidal moist sand",
            reportedAgo = "Yesterday afternoon",
            verifiedByFMU = true
        )
    )

    val intertidalEcoGuidelines = listOf(
        IntertidalEcoGuideline(
            id = "eco_1",
            title = "Walk Only On Hardened Sand Ridges",
            odiaTitle = "କେବଳ ଶକ୍ତ ବାଲି ଧାରରେ ଚାଲନ୍ତୁ",
            description = "Avoid stepping into deep silt sink-holes and active mating furrows formed by horseshoe crabs.",
            isDo = true
        ),
        IntertidalEcoGuideline(
            id = "eco_2",
            title = "Never Lift Crabs By The Tail (Telson)",
            odiaTitle = "କେବେବି ଲାଞ୍ଜ ଧରି ଉଠାନ୍ତୁ ନାହିଁ",
            description = "Their long telson tail is fragile and used for flipping upright; lifting by it permanently fractures their spine.",
            isDo = false
        ),
        IntertidalEcoGuideline(
            id = "eco_3",
            title = "Report Ghost Fishing Net Entanglements",
            odiaTitle = "ପରିତ୍ୟକ୍ତ ଜାଲ ବିଷୟରେ ଖବର ଦିଅନ୍ତୁ",
            description = "Alert FMU Marine Wildlife rescue hotline or coastal guards if crabs or sea turtles are trapped.",
            isDo = true
        ),
        IntertidalEcoGuideline(
            id = "eco_4",
            title = "Do Not Collect Live Shells or Marine Organisms",
            odiaTitle = "ଜୀବନ୍ତ ଶଙ୍ଖ ବା ଜୀବ ସଂଗ୍ରହ କରନ୍ତୁ ନାହିଁ",
            description = "Chandipur's mudflat ecosystem supports unique migratory shorebirds that feed on these exposed organisms.",
            isDo = false
        )
    )

    // 2. ITR Chandipur Defense Trail & Maritime Exclusion
    val defenseMilestones = listOf(
        DefenseTrailMilestone(
            id = "def_1",
            year = "1989",
            missileName = "Agni-I Historic Test",
            testRange = "ITR Launch Complex-3, Chandipur",
            significance = "India's maiden successful intermediate-range ballistic missile test under the leadership of Dr. APJ Abdul Kalam.",
            rangeKm = "700 - 900 km",
            drKalamMemoir = "\"At Chandipur, the sea and the sky became witness to India's technological self-reliance.\" - Wings of Fire",
            isSeaTest = true
        ),
        DefenseTrailMilestone(
            id = "def_2",
            year = "1994",
            missileName = "Prithvi Surface-to-Surface",
            testRange = "Proof & Experimental Establishment (PXE)",
            significance = "Validated tactical battlefield precision with indigenously designed liquid propulsion engines.",
            rangeKm = "150 - 250 km",
            drKalamMemoir = "\"The roar of Prithvi over the Bay of Bengal echoed our nation's sovereign strength.\"",
            isSeaTest = true
        ),
        DefenseTrailMilestone(
            id = "def_3",
            year = "2015",
            missileName = "Dr. APJ Abdul Kalam Island Dedication",
            testRange = "Wheeler Island Complex, off Chandipur Coast",
            significance = "Government of Odisha renamed Wheeler Island in eternal honor of the 'Missile Man of India' who camped here for months.",
            rangeKm = "Offshore Strategic Base",
            drKalamMemoir = "\"Wheeler Island is my theater of dreams where dreams turned into national defense shields.\"",
            isSeaTest = true
        ),
        DefenseTrailMilestone(
            id = "def_4",
            year = "2024",
            missileName = "Agni-Prime & BrahMos-ER Coastal Battery",
            testRange = "ITR Integrated Trajectory Range",
            significance = "Canisterized road-mobile launches and extended range supersonic cruise missile sea strikes tested with pinpoint telemetry.",
            rangeKm = "1,000 - 2,000 km",
            drKalamMemoir = "Balasore continues to stand as the impregnable missile shield of 1.4 billion citizens.",
            isSeaTest = true
        )
    )

    val maritimeExclusionZone = MaritimeExclusionZone(
        zoneName = "Offshore Sector Bravo (Chandipur - Dhamra Corridor)",
        odiaName = "ଉପକୂଳ ସୁରକ୍ଷା ଜୋନ୍ ବ୍ରାଭୋ",
        coordinates = "21.28° N, 87.05° E to 20.85° N, 87.35° E",
        radiusNauticalMiles = 25,
        activeHours = "08:00 AM to 14:00 PM (Scheduled Tracking)",
        sirenAlert = "No sonic blast scheduled today. Normal radar calibration window in progress.",
        coastalPatrolStatus = "Indian Coast Guard Interceptor Patrol Active",
        fishermenHelpline = "06782-272000 / VHF Channel 16"
    )

    // 3. Remuna "Amrita Keli" Prasad & Artisan Showcase
    val prasadStatus = PrasadStatus(
        templeName = "Khirachora Gopinatha Temple, Remuna",
        nextBatchTime = "12:30 PM (Mid-day)",
        batchPreparation = "Simmering in traditional earthenware Kudua pots over slow wood flame",
        estimatedKuduAvailable = 160,
        tokenDistributionOpen = true,
        recipeHeritage = "Pure cow milk simmered down to rich rabri-cream with pure cardamom, raw sugar, and holy camphor offering.",
        currentRitual = "Madhyanha Dhupa & Bhog Samarpan"
    )

    val balasoreArtisans = listOf(
        BalasoreArtisan(
            id = "art_1",
            artisanName = "Shri Raghunath Sahoo & Masters",
            craftTitle = "GI-Tagged Remuna Bell-Metal & Brass (କଂସା-ପିତ୍ତଳ)",
            odiaCraftTitle = "ରେମୁଣା କଂସା-ପିତ୍ତଳ ବାସନ ଶିଳ୍ପ",
            villageOrCluster = "Kansari Sahi, Remuna (Near Gopinatha Temple)",
            description = "Hand-hammered traditional bell-metal dinner sets, healing singing bowls, and temple worship lamps. 100% pure bronze alloy.",
            phone = "9437152084",
            experienceYears = 38,
            giTagged = true
        ),
        BalasoreArtisan(
            id = "art_2",
            artisanName = "Mata Santoshi Handloom Weavers Society",
            craftTitle = "Balasore Tussar Silk & Natural Dye Handloom",
            odiaCraftTitle = "ବାଲେଶ୍ୱର ତସର ରେଶମ ବସ୍ତ୍ର",
            villageOrCluster = "Soro & Gopalpur Weaving Cluster",
            description = "Organic wild tussar silk sarees, stoles, and handspun fabric dyed with natural forest barks and marigold flowers.",
            phone = "9861245630",
            experienceYears = 26,
            giTagged = true
        ),
        BalasoreArtisan(
            id = "art_3",
            artisanName = "Nilagiri Black Stone Sculptors Guild",
            craftTitle = "Nilagiri Granite & Chlorite Stone Carving",
            odiaCraftTitle = "ନୀଳଗିରି କଳା ମୁଗୁନି ପଥର ଶିଳ୍ପ",
            villageOrCluster = "Nilagiri Rajbati Area",
            description = "Traditional hand-chiseled deities, intricate temple replicas, and Ayurvedic heavy stone mortar-pestles (Silaputa).",
            phone = "9438012399",
            experienceYears = 42,
            giTagged = false
        )
    )

    // 4. Cyclone Shelter & Coastal Resilience
    val detailedCycloneShelters = listOf(
        DetailedCycloneShelter(
            id = "shelter_1",
            name = "Chandipur Multipurpose Cyclone Shelter (MCS)",
            block = "Balasore Sadar",
            capacity = 1200,
            elevationMeters = 8.5,
            solarBackup = true,
            distanceKm = 4.2,
            nodalOfficer = "P. K. Mohanty (Disaster Mgmt)",
            phone = "06782-272210",
            latitude = 21.4682,
            longitude = 87.0125
        ),
        DetailedCycloneShelter(
            id = "shelter_2",
            name = "Bahanaga High School Cyclone Shelter",
            block = "Bahanaga (Coastal)",
            capacity = 950,
            elevationMeters = 7.8,
            solarBackup = true,
            distanceKm = 18.5,
            nodalOfficer = "S. R. Das (Revenue Inspector)",
            phone = "06782-243105",
            latitude = 21.3120,
            longitude = 86.8920
        ),
        DetailedCycloneShelter(
            id = "shelter_3",
            name = "Bhograi Talsari Coastal Safe Haven",
            block = "Bhograi",
            capacity = 1400,
            elevationMeters = 9.2,
            solarBackup = true,
            distanceKm = 72.0,
            nodalOfficer = "B. B. Rout (BDO Office)",
            phone = "06781-232115",
            latitude = 21.6120,
            longitude = 87.4520
        ),
        DetailedCycloneShelter(
            id = "shelter_4",
            name = "Baliapal Subarnarekha Estuary Shelter",
            block = "Baliapal",
            capacity = 1100,
            elevationMeters = 8.0,
            solarBackup = true,
            distanceKm = 44.0,
            nodalOfficer = "K. N. Nayak (Civil Defense)",
            phone = "06781-254202",
            latitude = 21.6420,
            longitude = 87.2830
        )
    )

    val defaultEmergencyKitItems = listOf(
        EmergencyKitItem(
            id = "kit_1",
            name = "10L Bottled Drinking Water & Halazone tablets",
            odiaName = "ପିଇବା ପାଣି ଓ ବିଶୋଧନ ଟାବଲେଟ୍",
            category = "Hydration",
            isChecked = true,
            reason = "Drinking water supply is most critical during tidal surges."
        ),
        EmergencyKitItem(
            id = "kit_2",
            name = "Dry Rations (Chuda, Gur, Biscuits, Milk powder)",
            odiaName = "ଶୁଖିଲା ଖାଦ୍ୟ (ଚୁଡ଼ା, ଗୁଡ଼, ବିସ୍କୁଟ୍)",
            category = "Nutrition",
            isChecked = true,
            reason = "Non-perishable food that requires zero cooking fuel."
        ),
        EmergencyKitItem(
            id = "kit_3",
            name = "Battery / Hand-crank Radio for AIR Weather Alerts",
            odiaName = "ବ୍ୟାଟେରୀ ବା ଟ୍ରାଞ୍ଜିଷ୍ଟର ରେଡିଓ",
            category = "Communication",
            isChecked = true,
            reason = "Cellular towers often fail; FM 100.8 MHz remains broadcasted."
        ),
        EmergencyKitItem(
            id = "kit_4",
            name = "Heavy-Duty Waterproof LED Torch & Spare Cells",
            odiaName = "ୱାଟରପ୍ରୁଫ୍ ଟର୍ଚ୍ଚ ଓ ଅତିରିକ୍ତ ବ୍ୟାଟେରୀ",
            category = "Lighting",
            isChecked = true,
            reason = "Grid power is pre-emptively shut down during cyclones."
        ),
        EmergencyKitItem(
            id = "kit_5",
            name = "Waterproof Pouch with Aadhaar, Land Deeds & Cash",
            odiaName = "ଜଳରୋଧକ ବ୍ୟାଗରେ ଆଧାର ଓ କାଗଜପତ୍ର",
            category = "Documents",
            isChecked = false,
            reason = "Crucial for post-disaster government relief and identity verification."
        ),
        EmergencyKitItem(
            id = "kit_6",
            name = "First Aid Kit, ORS Packets & Chronic Medication",
            odiaName = "ଫାଷ୍ଟ ଏଡ୍ ବକ୍ସ, ORS ଓ ଜରୁରୀ ଔଷଧ",
            category = "Medical",
            isChecked = false,
            reason = "Water-borne pathogens and cuts from debris require prompt treatment."
        ),
        EmergencyKitItem(
            id = "kit_7",
            name = "High-Decibel Signal Whistle",
            odiaName = "ସଙ୍କେତ ହୁଇସିଲ୍",
            category = "Rescue",
            isChecked = false,
            reason = "Allows search and rescue teams (ODRAF/NDRF) to pinpoint your location."
        )
    )

    // 5. Balaramgadi Harbor Catch & Market Rates
    val harborCatchRates = listOf(
        HarborCatchRate(
            id = "hcr_1",
            fishName = "Budhabalanga River Estuary Hilsa (Ilish)",
            odiaName = "ବୁଢ଼ାବଳଙ୍ଗ ମୁହାଣ ଇଲିସି",
            wholesalePriceKg = "₹950 - ₹1,100 / kg",
            retailPriceKg = "₹1,250 - ₹1,450 / kg",
            arrivalTime = "06:15 AM (Morning Trawler Catch)",
            dockLanding = "Jetty #2, Balaramgadi Harbor",
            freshnessRating = "★★★★★ (Gills Deep Pink • Caught 3 hrs ago)",
            buyingTip = "Choose medium sizes (800g - 1.2kg) for maximum natural omega-3 oil and sweet aroma."
        ),
        HarborCatchRate(
            id = "hcr_2",
            fishName = "Sea Tiger Prawns (Bagda)",
            odiaName = "ସମୁଦ୍ର ବାଗଦା ଚିଙ୍ଗୁଡ଼ି",
            wholesalePriceKg = "₹550 - ₹680 / kg",
            retailPriceKg = "₹720 - ₹850 / kg",
            arrivalTime = "05:45 AM & 03:30 PM",
            dockLanding = "Jetty #1 (Main Auction Shed)",
            freshnessRating = "★★★★★ (Translucent Shell • Firm)",
            buyingTip = "Ask for head-on whole catch straight off ice crates before sorting."
        ),
        HarborCatchRate(
            id = "hcr_3",
            fishName = "Silver White Pomfret (Chandi Maacha)",
            odiaName = "ଧଳା ଚାନ୍ଦି ମାଛ",
            wholesalePriceKg = "₹620 - ₹750 / kg",
            retailPriceKg = "₹800 - ₹950 / kg",
            arrivalTime = "06:40 AM",
            dockLanding = "Kasafal Fish Trawler Berth",
            freshnessRating = "★★★★☆ (Bright Mirror Sheen)",
            buyingTip = "Check for clear shiny eyes and resilient flesh that springs back on touch."
        ),
        HarborCatchRate(
            id = "hcr_4",
            fishName = "Estuary Mud Crabs (Jadua Kankada)",
            odiaName = "ନଦୀ ମୁହାଣ ଜାଦୁଆ କଙ୍କଡ଼ା",
            wholesalePriceKg = "₹380 - ₹460 / kg",
            retailPriceKg = "₹500 - ₹620 / kg",
            arrivalTime = "Live Baskets (07:00 AM)",
            dockLanding = "Balaramgadi Confluence Shore",
            freshnessRating = "★★★★★ (100% Live Tied Catch)",
            buyingTip = "Ensure claw strings are intact and shell feels heavy indicating packed meat."
        )
    )

    val harborLandingBells = listOf(
        HarborLandingBell(
            fleetName = "Maa Tarini Deep Sea Fleet (7 Trawlers)",
            harbor = "Balaramgadi Harbor",
            expectedDockTime = "06:30 AM (Just Landed)",
            catchSummary = "Heavy catch of Hilsa, Silver Croaker & Mud Crabs",
            status = "Docking Now"
        ),
        HarborLandingBell(
            fleetName = "Bajarangabali Coastal Artisanal Fishermen",
            harbor = "Bahabalpur Jetty",
            expectedDockTime = "03:45 PM (Afternoon Tide)",
            catchSummary = "Tiger Prawns, Sea Bass & Squid",
            status = "Arriving in 45m"
        )
    )

    // 6. Kuldiha Elephant Corridor & Ecotourism Passport
    val elephantCorridorAlert = ElephantCorridorAlert(
        sector = "Nilagiri - Gohirabhani - Kuldiha Sanctuary Link",
        alertLevel = "YELLOW (Caution - Herd Active)",
        herdCount = 16,
        lastSpottedTime = "Today 05:40 AM near Rissia Dam Salt-lick",
        highwaySpeedLimitKmh = 20,
        advisoryText = "Elephant family herd including 2 calves moving towards perennial water pool. Drive cautiously with dimmed headlights. No loud horns.",
        forestControlPhone = "06782-262270"
    )

    val ecoPassportStamps = listOf(
        EcoPassportStamp(
            id = "stamp_rissia",
            title = "Rissia Dam Reservoir",
            odiaTitle = "ରିସିଆ ଡ୍ୟାମ୍ ଜଳଭଣ୍ଡାର",
            location = "Kuldiha Buffer Zone (32 km)",
            elevation = "118 m ASL",
            keyAttraction = "Migratory wild ducks, kingfishers & tranquil forest reflections",
            isCheckedIn = true
        ),
        EcoPassportStamp(
            id = "stamp_gohirabhani",
            title = "Gohirabhani Watchtower & Salt Lick",
            odiaTitle = "ଗୋହିରାଭାଣୀ ଟାୱାର ଓ ଲୁଣ ଚାଟ",
            location = "Deep Kuldiha Core (38 km)",
            elevation = "142 m ASL",
            keyAttraction = "Prime vantage point for wild elephant herds & spotted deer herds at dusk",
            isCheckedIn = false
        ),
        EcoPassportStamp(
            id = "stamp_panchalingeswar",
            title = "Panchalingeswar Perennial Spring",
            odiaTitle = "ପଞ୍ଚଲିଙ୍ଗେଶ୍ୱର ପାହାଡ଼ ଝରଣା",
            location = "Devagiri Foothills (30 km)",
            elevation = "210 m ASL",
            keyAttraction = "Submerged stone lingas with natural healing mineral mountain spring flow",
            isCheckedIn = true
        ),
        EcoPassportStamp(
            id = "stamp_kuldiha_camp",
            title = "Kuldiha Nature Eco-Cottages",
            odiaTitle = "କୁଲଡିହା ଇକୋ ନେଚର କ୍ୟାମ୍ପ",
            location = "Kuldiha Forest Camp (35 km)",
            elevation = "130 m ASL",
            keyAttraction = "Canopy birding trails, giant Malabar squirrels & night forest sounds",
            isCheckedIn = false
        )
    )
}
