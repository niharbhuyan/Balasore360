package com.example.data.emergency

object EmergencyServicesRepository {

    val allServices: List<EmergencyServiceLocation> = listOf(
        // ================= HOSPITALS & MEDICAL =================
        EmergencyServiceLocation(
            id = "hosp_fmmch",
            name = "Fakir Mohan Medical College & Hospital (FM MCH)",
            odiaName = "ଫକୀର ମୋହନ ମେଡିକାଲ କଲେଜ ଓ ହସ୍ପିଟାଲ",
            category = EmergencyCategory.HOSPITAL,
            latitude = 21.5120,
            longitude = 86.8870,
            address = "Remuna Golei Bypass, Balasore, Odisha 756019",
            odiaAddress = "ରେମୁଣା ଗୋଲେଇ ବାଇପାସ, ବାଲେଶ୍ୱର",
            phone = "06782-262024",
            is24x7 = true,
            capacityOrBeds = "750 Beds • 24/7 Level-1 Trauma & Super-Specialty ICU",
            facilities = listOf("24/7 Emergency Casualty", "Trauma ICU", "Blood Storage", "CT/MRI Imaging", "Oxygen Plant"),
            landmarkNear = "Near Remuna Golei on NH-16"
        ),
        EmergencyServiceLocation(
            id = "hosp_dhh",
            name = "District Headquarters Hospital (DHH Balasore)",
            odiaName = "ଜିଲ୍ଲା ମୁଖ୍ୟ ଚିକିତ୍ସାଳୟ (ବାଲେଶ୍ୱର)",
            category = EmergencyCategory.HOSPITAL,
            latitude = 21.4910,
            longitude = 86.9280,
            address = "Phandi Chhak, OT Road, Balasore 756001",
            odiaAddress = "ଫାଣ୍ଡି ଛକ, ଓ.ଟି. ରୋଡ୍, ବାଲେଶ୍ୱର",
            phone = "06782-262143",
            is24x7 = true,
            capacityOrBeds = "400 Beds • 24/7 Casualty & Maternal Care",
            facilities = listOf("Emergency OPD", "SNCU & Maternity", "Pathology Lab", "Free Ambulance Service"),
            landmarkNear = "Phandi Chhak near Cinema Square"
        ),
        EmergencyServiceLocation(
            id = "hosp_redcross",
            name = "Balasore Red Cross Blood Bank & Center",
            odiaName = "ରେଡ୍ କ୍ରସ୍ ରକ୍ତ ଭଣ୍ଡାର (ବାଲେଶ୍ୱର)",
            category = EmergencyCategory.HOSPITAL,
            latitude = 21.4915,
            longitude = 86.9275,
            address = "DHH Campus, Phandi Chhak, Balasore",
            odiaAddress = "ଡି.ଏଚ୍.ଏଚ୍ ପରିସର, ଫାଣ୍ଡି ଛକ, ବାଲେଶ୍ୱର",
            phone = "06782-262143",
            is24x7 = true,
            capacityOrBeds = "All Blood Groups (A+, B+, O+, AB+, Rare O-)",
            facilities = listOf("24/7 Blood Dispatch", "Platelet Apheresis", "Emergency Component Storage"),
            landmarkNear = "Inside DHH Campus"
        ),
        EmergencyServiceLocation(
            id = "hosp_chandipur_chc",
            name = "Chandipur Coastal Community Health Centre",
            odiaName = "ଚାନ୍ଦିପୁର ଗୋଷ୍ଠୀ ସ୍ୱାସ୍ଥ୍ୟ କେନ୍ଦ୍ର (ସମୁଦ୍ର କୂଳ)",
            category = EmergencyCategory.HOSPITAL,
            latitude = 21.4650,
            longitude = 87.0090,
            address = "Main Beach Road, Chandipur, Balasore 756025",
            odiaAddress = "ମୁଖ୍ୟ ବେଳାଭୂମି ରାସ୍ତା, ଚାନ୍ଦିପୁର, ବାଲେଶ୍ୱର",
            phone = "06782-272230",
            is24x7 = true,
            capacityOrBeds = "50 Beds • Marine First-Aid & Drowning Trauma Unit",
            facilities = listOf("Resuscitation Bay", "Oxygen Cylinders", "Emergency Ambulance"),
            landmarkNear = "1.5 km before Chandipur Beach Promenade"
        ),

        // ================= POLICE STATIONS =================
        EmergencyServiceLocation(
            id = "pol_town",
            name = "Balasore Town Police Station",
            odiaName = "ବାଲେଶ୍ୱର ଟାଉନ୍ ଥାନା",
            category = EmergencyCategory.POLICE,
            latitude = 21.4930,
            longitude = 86.9240,
            address = "Cinema Square, Balasore Town 756001",
            odiaAddress = "ସିନେମା ଛକ, ବାଲେଶ୍ୱର ସହର",
            phone = "06782-262022",
            is24x7 = true,
            capacityOrBeds = "PCR Vans on 24/7 Patrol • City Quick Reaction Team",
            facilities = listOf("Dial 112 Control Unit", "Women Helpdesk", "Traffic Control", "Crime Cell"),
            landmarkNear = "Cinema Square (Station Road confluence)"
        ),
        EmergencyServiceLocation(
            id = "pol_sahadevkhunta",
            name = "Sahadevkhunta Police Station",
            odiaName = "ସହଦେବଖୁଣ୍ଟା ଥାନା (ବସ୍ ଟର୍ମିନାଲ୍)",
            category = EmergencyCategory.POLICE,
            latitude = 21.4880,
            longitude = 86.9320,
            address = "Near Central Bus Stand, Sahadevkhunta, Balasore",
            odiaAddress = "କେନ୍ଦ୍ରୀୟ ବସ୍ ଟର୍ମିନାଲ୍ ନିକଟ, ସହଦେବଖୁଣ୍ଟା",
            phone = "06782-262100",
            is24x7 = true,
            capacityOrBeds = "Interstate Transit & Bus Stand Security",
            facilities = listOf("Transit Passenger Helpdesk", "24/7 Mobile Patrol", "CCTV Surveillance Room"),
            landmarkNear = "Right beside Sahadevkhunta Bus Stand"
        ),
        EmergencyServiceLocation(
            id = "pol_marine_chandipur",
            name = "Chandipur Marine Police Station",
            odiaName = "ଚାନ୍ଦିପୁର ସାମୁଦ୍ରିକ ଥାନା",
            category = EmergencyCategory.POLICE,
            latitude = 21.4680,
            longitude = 87.0140,
            address = "Coastal Road, Chandipur 756025",
            odiaAddress = "ଉପକୂଳ ରାସ୍ତା, ଚାନ୍ଦିପୁର",
            phone = "06782-272100",
            is24x7 = true,
            capacityOrBeds = "Coastal Surveillance High-Speed Interceptor Boats",
            facilities = listOf("Sea Rescue Patrol", "Tourist Beach Guard Post", "DRDO Buffer Security"),
            landmarkNear = "Adjacent to Tourist Watchtower on Chandipur Beach"
        ),
        EmergencyServiceLocation(
            id = "pol_remuna",
            name = "Remuna Police Station",
            odiaName = "ରେମୁଣା ଥାନା",
            category = EmergencyCategory.POLICE,
            latitude = 21.5290,
            longitude = 86.8710,
            address = "Temple Road, Remuna, Balasore 756019",
            odiaAddress = "ମନ୍ଦିର ରାସ୍ତା, ରେମୁଣା, ବାଲେଶ୍ୱର",
            phone = "06782-252220",
            is24x7 = true,
            capacityOrBeds = "Pilgrim Crowd Control & Highway Patrol",
            facilities = listOf("24/7 PCR Vehicle", "Temple Route Safety Desk"),
            landmarkNear = "Near Khirachora Gopinath Temple Entrance"
        ),
        EmergencyServiceLocation(
            id = "pol_nilagiri",
            name = "Nilagiri Police Station",
            odiaName = "ନୀଳଗିରି ଥାନା",
            category = EmergencyCategory.POLICE,
            latitude = 21.4580,
            longitude = 86.7680,
            address = "Main Market Road, Nilagiri 756040",
            odiaAddress = "ମୁଖ୍ୟ ବଜାର, ନୀଳଗିରି",
            phone = "06784-233022",
            is24x7 = true,
            capacityOrBeds = "Sub-Divisional Headquarters Patrol",
            facilities = listOf("Hill Trekker Assistance", "Panchalingeswar Route Patrol"),
            landmarkNear = "Nilagiri Sub-Divisional Court Square"
        ),

        // ================= CYCLONE & FLOOD SHELTERS =================
        EmergencyServiceLocation(
            id = "shelt_sahadevkhunta",
            name = "Sahadevkhunta Multi-Purpose Cyclone Shelter",
            odiaName = "ସହଦେବଖୁଣ୍ଟା ବହୁମୁଖୀ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
            category = EmergencyCategory.CYCLONE_SHELTER,
            latitude = 21.4870,
            longitude = 86.9360,
            address = "Ward 22, Sahadevkhunta, Balasore",
            odiaAddress = "ୱାର୍ଡ ୨୨, ସହଦେବଖୁଣ୍ଟା, ବାଲେଶ୍ୱର",
            phone = "06782-261150",
            is24x7 = true,
            capacityOrBeds = "Capacity: 2,500 Evacuees • Generator Backup & Community Kitchen",
            facilities = listOf("Solar Generator", "Purified RO Water Plant", "Dry Food Storage", "Infant Care Corner"),
            landmarkNear = "Near Sahadevkhunta High School"
        ),
        EmergencyServiceLocation(
            id = "shelt_chandipur",
            name = "Chandipur Coastal Evacuation Shelter",
            odiaName = "ଚାନ୍ଦିପୁର ଉପକୂଳ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
            category = EmergencyCategory.CYCLONE_SHELTER,
            latitude = 21.4660,
            longitude = 87.0160,
            address = "Fishermen Colony, Chandipur 756025",
            odiaAddress = "ମତ୍ସ୍ୟଜୀବୀ କଲୋନୀ, ଚାନ୍ଦିପୁର",
            phone = "06782-272111",
            is24x7 = true,
            capacityOrBeds = "Capacity: 3,000 Coastal Residents • High Plinth Anti-Surge Structure",
            facilities = listOf("Storm Surge Defense Ramp", "Satellite Phone Unit", "Medical First Aid Post"),
            landmarkNear = "300 meters from Casuarina forest belt"
        ),
        EmergencyServiceLocation(
            id = "shelt_balaramgadi",
            name = "Balaramgadi Harbor Cyclone Center",
            odiaName = "ବାହାବଳପୁର ଓ ବଳରାମଗଡ଼ି ମତ୍ସ୍ୟ ବନ୍ଦର ଆଶ୍ରୟ କେନ୍ଦ୍ର",
            category = EmergencyCategory.CYCLONE_SHELTER,
            latitude = 21.4810,
            longitude = 87.0370,
            address = "Balaramgadi Fish Jetty, Chandipur",
            odiaAddress = "ବଳରାମଗଡ଼ି ମାଛ ଜେଟି, ଚାନ୍ଦିପୁର",
            phone = "06782-272144",
            is24x7 = true,
            capacityOrBeds = "Capacity: 1,800 Fisherfolk • Heavy Boat Anchor Lockers",
            facilities = listOf("VHF Marine Radio", "Engine Battery Charging", "Cattle Protection Enclosure"),
            landmarkNear = "At Budhabalanga River Estuary Jetty"
        ),
        EmergencyServiceLocation(
            id = "shelt_kasafal",
            name = "Kasafal Marine Cyclone Shelter",
            odiaName = "କସାଫଳ ସାମୁଦ୍ରିକ ବାତ୍ୟା ଆଶ୍ରୟସ୍ଥଳୀ",
            category = EmergencyCategory.CYCLONE_SHELTER,
            latitude = 21.5250,
            longitude = 87.0850,
            address = "Kasafal Sea Coast, Balasore Sadar",
            odiaAddress = "କସାଫଳ ସମୁଦ୍ର କୂଳ, ବାଲେଶ୍ୱର",
            phone = "06782-275112",
            is24x7 = true,
            capacityOrBeds = "Capacity: 2,200 Persons • Deep Coastal High Alert Zone",
            facilities = listOf("Satellite Emergency Beacon", "Rainwater Harvesting", "Emergency Ration Reserve"),
            landmarkNear = "Kasafal Marine Landing Center"
        ),

        // ================= FIRE & DISASTER RESCUE =================
        EmergencyServiceLocation(
            id = "fire_town",
            name = "Balasore Central Fire & Disaster Station",
            odiaName = "ବାଲେଶ୍ୱର ମୁଖ୍ୟ ଅଗ୍ନିଶମ ଓ ବିପର୍ଯ୍ୟୟ ପ୍ରଶମନ କେନ୍ଦ୍ର",
            category = EmergencyCategory.FIRE_RESCUE,
            latitude = 21.4940,
            longitude = 86.9210,
            address = "Near Police Line, Cinema Square, Balasore 756001",
            odiaAddress = "ପୋଲିସ ଲାଇନ୍ ନିକଟ, ସିନେମା ଛକ, ବାଲେଶ୍ୱର",
            phone = "101",
            is24x7 = true,
            capacityOrBeds = "Hydraulic Water Tenders & ODRAF Flood Rescue Boats",
            facilities = listOf("Tree Cutting Chain Saws", "Inflatable Rescue Boats", "Chemical Foam Tenders"),
            landmarkNear = "Behind Town Hall / Police Line"
        ),
        EmergencyServiceLocation(
            id = "fire_nilagiri",
            name = "Nilagiri Fire Station",
            odiaName = "ନୀଳଗିରି ଅଗ୍ନିଶମ କେନ୍ଦ୍ର",
            category = EmergencyCategory.FIRE_RESCUE,
            latitude = 21.4550,
            longitude = 86.7620,
            address = "Forest Road, Nilagiri 756040",
            odiaAddress = "ଜଙ୍ଗଲ ରାସ୍ତା, ନୀଳଗିରି",
            phone = "06784-233101",
            is24x7 = true,
            capacityOrBeds = "Hill Rescue Squad & Wildfire Equipment",
            facilities = listOf("Mountain Terrain Rescue Truck", "Deep Well Pumping Sets"),
            landmarkNear = "Near Nilagiri Forest Range Office"
        )
    )

    /**
     * Preset simulated coordinate locations for testing and quick exploration.
     */
    val presetLocations = listOf(
        PresetCoordinate(
            id = "loc_station",
            name = "Balasore Junction (Baleswar)",
            odiaName = "ବାଲେଶ୍ୱର ରେଳ ଷ୍ଟେସନ (ଜଙ୍କସନ)",
            lat = 21.4934,
            lng = 86.9135,
            description = "Central Town Hub"
        ),
        PresetCoordinate(
            id = "loc_chandipur",
            name = "Chandipur Beach Promenade",
            odiaName = "ଚାନ୍ଦିପୁର ବେଳାଭୂମି କୂଳ",
            lat = 21.4674,
            lng = 87.0177,
            description = "Coastal Sea Zone"
        ),
        PresetCoordinate(
            id = "loc_remuna",
            name = "Remuna Gopinatha Temple",
            odiaName = "ରେମୁଣା ଗୋପୀନାଥ ମନ୍ଦିର",
            lat = 21.5283,
            lng = 86.8725,
            description = "Northern Suburb"
        ),
        PresetCoordinate(
            id = "loc_kuruda",
            name = "Kuruda Industrial Area",
            odiaName = "କୁରୁଡ଼ା ଶିଳ୍ପାଞ୍ଚଳ",
            lat = 21.4650,
            lng = 86.8950,
            description = "NH-16 South Gateway"
        ),
        PresetCoordinate(
            id = "loc_nilagiri",
            name = "Nilagiri Hill Foothills",
            odiaName = "ନୀଳଗିରି ପାହାଡ଼ ପାଦଦେଶ",
            lat = 21.4580,
            lng = 86.7680,
            description = "Western Highland"
        )
    )

    fun getNearestServices(
        userLat: Double,
        userLng: Double,
        categoryFilter: EmergencyCategory? = null
    ): List<EmergencyServiceLocation> {
        val list = if (categoryFilter != null) {
            allServices.filter { it.category == categoryFilter }
        } else {
            allServices
        }
        return list.sortedBy { it.distanceFrom(userLat, userLng) }
    }
}

data class PresetCoordinate(
    val id: String,
    val name: String,
    val odiaName: String,
    val lat: Double,
    val lng: Double,
    val description: String
)
