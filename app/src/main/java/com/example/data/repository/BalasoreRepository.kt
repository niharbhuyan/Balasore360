package com.example.data.repository

import com.example.data.model.EmergencyContact
import com.example.data.model.Hotspot
import com.example.data.model.NewsArticle
import com.example.data.model.TransitSchedule
import com.example.data.model.WeatherInfo

object BalasoreRepository {

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
            source = "Balasore Express News"
        ),
        NewsArticle(
            id = "news_2",
            title = "Chandipur DRDO Facility Successfully Tests Advanced Coastal Defense System",
            odiaTitle = "ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ଘାଟିରୁ ଅତ୍ୟାଧୁନିକ ପ୍ରତିରକ୍ଷା ପ୍ରଣାଳୀର ସଫଳ ପରୀକ୍ଷଣ",
            snippet = "The Integrated Test Range at Chandipur reported mission success with precise radar tracking across the Bay of Bengal coastline.",
            odiaSnippet = "ଆଇଟିଆର ଚାନ୍ଦିପୁର କ୍ଷେପଣାସ୍ତ୍ର ଘାଟିରୁ ସ୍ୱଦେଶୀ ଜ୍ଞାନକୌଶଳରେ ନିର୍ମିତ ପ୍ରତିରକ୍ଷା ପ୍ରଣାଳୀ ଲକ୍ଷ୍ୟଭେଦ କରିଛି।",
            category = "Defense",
            timeAgo = "1 hour ago",
            source = "Defense Updates Odisha"
        ),
        NewsArticle(
            id = "news_3",
            title = "Annual Remuna Khirachora Gopinath Mahotsav Dates Announced",
            odiaTitle = "ପ୍ରସିଦ୍ଧ ରେମୁଣା କ୍ଷୀରଚୋରା ଗୋପୀନାଥ ମହୋତ୍ସବ ତାରିଖ ଘୋଷଣା",
            snippet = "Cultural troops, traditional Sankirtan singers, and craft artisans from across Odisha will gather for the 5-day spiritual celebration.",
            odiaSnippet = "ପାରମ୍ପରିକ ସଂକୀର୍ତ୍ତନ ଏବଂ ସାଂସ୍କୃତିକ କାର୍ଯ୍ୟକ୍ରମ ସହ ପାଞ୍ଚ ଦିନ ଧରି ଚାଲିବ ରେମୁଣା ବାର୍ଷିକ ଉତ୍ସବ।",
            category = "Culture",
            timeAgo = "3 hours ago",
            source = "Odisha Sambad"
        ),
        NewsArticle(
            id = "news_4",
            title = "Subarnarekha River Basin Flood Monitoring Sensors Deployed by District Disaster Authority",
            odiaTitle = "ସୁବର୍ଣ୍ଣରେଖା ଅବବାହିକାରେ ବନ୍ୟା ନିୟନ୍ତ୍ରଣ ପାଇଁ ସ୍ୱୟଂକ୍ରିୟ ସେନ୍ସର ସ୍ଥାପନ",
            snippet = "Real-time water level telemetry alerts will be transmitted to coastal Bhograi and Jaleswar panchayats for early monsoon preparedness.",
            odiaSnippet = "ଜଳସ୍ତର ଉପରେ ନଜର ରଖିବାକୁ ଭୋଗରାଇ ଏବଂ ଜଳେଶ୍ୱର ବ୍ଲକରେ ସେନ୍ସର ସ୍ଥାପିତ ହୋଇଛି।",
            category = "Civic Alert",
            timeAgo = "5 hours ago",
            source = "Balasore District Admin"
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
}
