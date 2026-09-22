package com.example.data.repository

import java.util.Calendar

data class DoctorProfile(
    val id: String,
    val name: String,
    val odiaName: String,
    val qualification: String,
    val specialty: String,
    val experienceYears: Int,
    val affiliationHospital: String,
    val chamberName: String,
    val chamberAddress: String,
    val consultationFee: Int,
    val phone: String,
    val daysOfWeek: String,
    val morningTiming: String,
    val eveningTiming: String,
    val emergencyCallAvailable: Boolean,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 4.8f,
    val specialInterests: List<String> = emptyList()
)

data class MedicineStore(
    val id: String,
    val name: String,
    val odiaName: String,
    val areaLocation: String,
    val fullAddress: String,
    val phone: String,
    val alternatePhone: String? = null,
    val is24x7: Boolean,
    val openingHours: String,
    val hasHomeDelivery: Boolean,
    val deliveryRadiusKm: Int,
    val emergencyMedicines: List<String>,
    val licensedPharmacistName: String,
    val latitude: Double,
    val longitude: Double,
    val isJanAushadhiGeneric: Boolean = false
)

data class PolyclinicCenter(
    val id: String,
    val name: String,
    val odiaName: String,
    val category: String, // Multi-specialty Polyclinic, Diagnostic & Imaging Hub, Daycare & Minor OT
    val address: String,
    val landmark: String,
    val phone: String,
    val openHours: String,
    val facilities: List<String>,
    val sampleCollectionTimings: String,
    val reportDeliverySameDay: Boolean,
    val visitingSpecialistsList: List<String>,
    val latitude: Double,
    val longitude: Double,
    val ambulanceSupport: Boolean = true
)

data class PathologyTestPackage(
    val name: String,
    val description: String,
    val estimatedPrice: String,
    val sampleType: String, // e.g. "Fasting Blood", "Random Blood & Urine", "Blood"
    val reportTurnaroundHours: Int
)

data class PathologyLab(
    val id: String,
    val name: String,
    val odiaName: String,
    val accreditation: String, // e.g. "NABL Accredited & ICMR Approved", "ISO 9001:2015 Certified"
    val address: String,
    val landmark: String,
    val phone: String,
    val alternatePhone: String? = null,
    val homeCollectionPhone: String? = null,
    val openingHours: String,
    val fastingCollectionHours: String,
    val homeSampleCollectionAvailable: Boolean,
    val sameDayDigitalReports: Boolean,
    val testTurnaroundDescription: String,
    val popularPackages: List<PathologyTestPackage>,
    val specialEquipments: List<String>,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 4.8f
)

data class PrivateHospital(
    val id: String,
    val name: String,
    val odiaName: String,
    val category: String, // Multi-Speciality Hospital, Maternity & Nursing Home, Surgical Centre, Eye Hospital
    val totalBeds: Int,
    val icuBedsCount: Int,
    val nicuBedsCount: Int,
    val emergencyPhone: String,
    val receptionPhone: String,
    val ambulancePhone: String,
    val address: String,
    val landmark: String,
    val is24x7Emergency: Boolean = true,
    val hasBloodStorageOrBank: Boolean = false,
    val hasDialysisUnit: Boolean = false,
    val hasNicuPicU: Boolean = false,
    val hasInhousePharmacy24x7: Boolean = true,
    val hasInhousePathology24x7: Boolean = true,
    val acceptedSchemes: List<String>, // BSKY/BSKY Nabin, Ayushman Bharat, Private TPA Cashless, ESI
    val specialtiesAvailable: List<String>,
    val keySurgeonsAndDoctors: List<String>,
    val facilities: List<String>,
    val opdTimings: String,
    val visitingHours: String,
    val latitude: Double,
    val longitude: Double,
    val rating: Float = 4.7f
)

object HealthcareRepository {

    val doctorsList = listOf(
        DoctorProfile(
            id = "doc_1",
            name = "Dr. Asit Kumar Mohanty",
            odiaName = "ଡା. ଅସିତ କୁମାର ମହାନ୍ତି",
            qualification = "MBBS, MD (General Medicine)",
            specialty = "General Medicine",
            experienceYears = 18,
            affiliationHospital = "Fakir Mohan Medical College & Hospital (FMMCH), Remuna",
            chamberName = "Apollo Clinic & Specialty Chamber",
            chamberAddress = "Vivekananda Marg, Near FM Golapokhari, Balasore",
            consultationFee = 400,
            phone = "06782262440",
            daysOfWeek = "Mon - Sat",
            morningTiming = "08:00 AM - 09:30 AM",
            eveningTiming = "06:00 PM - 09:00 PM",
            emergencyCallAvailable = true,
            latitude = 21.4942,
            longitude = 86.9318,
            rating = 4.9f,
            specialInterests = listOf("Diabetes Mellitus", "Hypertension", "Tropical Fevers", "Infectious Diseases")
        ),
        DoctorProfile(
            id = "doc_2",
            name = "Dr. Subhasmita Rout",
            odiaName = "ଡା. ଶୁଭସ୍ମିତା ରାଉତ",
            qualification = "MBBS, MD (Pediatrics), DCH",
            specialty = "Pediatrics & Child Health",
            experienceYears = 14,
            affiliationHospital = "Balasore District Headquarters Hospital (DHH)",
            chamberName = "Shishu Suraksha Clinic",
            chamberAddress = "Opposite Zilla School, Cinema Chhak, Balasore",
            consultationFee = 350,
            phone = "06782263155",
            daysOfWeek = "Mon - Sat (Sun Emergency Only)",
            morningTiming = "08:30 AM - 10:00 AM",
            eveningTiming = "05:00 PM - 08:30 PM",
            emergencyCallAvailable = true,
            latitude = 21.4965,
            longitude = 86.9351,
            rating = 4.9f,
            specialInterests = listOf("Neonatal Care", "Pediatric Asthma", "Vaccination", "Childhood Nutrition")
        ),
        DoctorProfile(
            id = "doc_3",
            name = "Dr. Pradosh Ranjan Das",
            odiaName = "ଡା. ପ୍ରଦୋଷ ରଞ୍ଜନ ଦାସ",
            qualification = "MBBS, MS (Orthopedics), DNB (Ortho)",
            specialty = "Orthopedics & Joint Surgery",
            experienceYears = 16,
            affiliationHospital = "FMMCH Medical College, Balasore",
            chamberName = "Kalinga Bone & Joint Care Chamber",
            chamberAddress = "Station Road, Near Balasore Rly Station",
            consultationFee = 500,
            phone = "06782262889",
            daysOfWeek = "Mon - Sat",
            morningTiming = "07:30 AM - 09:00 AM",
            eveningTiming = "06:30 PM - 09:30 PM",
            emergencyCallAvailable = true,
            latitude = 21.5015,
            longitude = 86.9388,
            rating = 4.8f,
            specialInterests = listOf("Arthritis Management", "Trauma & Fracture Surgery", "Spine Care", "Sports Injuries")
        ),
        DoctorProfile(
            id = "doc_4",
            name = "Dr. Madhumita Senapati",
            odiaName = "ଡା. ମଧୁମିତା ସେନାପତି",
            qualification = "MBBS, MS (Obstetrics & Gynecology)",
            specialty = "Gynecology & Obstetrics",
            experienceYears = 20,
            affiliationHospital = "Matru Mangala & DHH Balasore",
            chamberName = "Matrushree Women's Wellness Clinic",
            chamberAddress = "OT Road, Near Police Line, Balasore",
            consultationFee = 450,
            phone = "06782264512",
            daysOfWeek = "Daily (Except Thursday Evening)",
            morningTiming = "08:00 AM - 10:00 AM",
            eveningTiming = "05:30 PM - 08:30 PM",
            emergencyCallAvailable = true,
            latitude = 21.4920,
            longitude = 86.9295,
            rating = 4.9f,
            specialInterests = listOf("High-Risk Pregnancy", "Laparoscopic Gynecology", "Infertility Counselling", "PCOS Care")
        ),
        DoctorProfile(
            id = "doc_5",
            name = "Dr. Bikram Keshari Jena",
            odiaName = "ଡା. ବିକ୍ରମ କେଶରୀ ଜେନା",
            qualification = "MBBS, MD (Cardiology), DM (Cardiology)",
            specialty = "Cardiology",
            experienceYears = 15,
            affiliationHospital = "Visiting Consultant, FMMCH & Apollo Clinic",
            chamberName = "Balasore Heart & Cardiac Centre",
            chamberAddress = "Motiganj Bazar, Main Road, Balasore",
            consultationFee = 600,
            phone = "06782261900",
            daysOfWeek = "Wed, Fri, Sun",
            morningTiming = "09:00 AM - 01:00 PM",
            eveningTiming = "04:00 PM - 08:00 PM",
            emergencyCallAvailable = true,
            latitude = 21.4880,
            longitude = 86.9250,
            rating = 4.9f,
            specialInterests = listOf("Coronary Angiography", "Heart Failure Management", "Echocardiography", "Preventive Cardiology")
        ),
        DoctorProfile(
            id = "doc_6",
            name = "Dr. Soumya Ranjan Panda",
            odiaName = "ଡା. ସୌମ୍ୟ ରଞ୍ଜନ ପଣ୍ଡା",
            qualification = "MBBS, MS (ENT), DLO",
            specialty = "ENT (Ear, Nose, Throat)",
            experienceYears = 12,
            affiliationHospital = "Fakir Mohan Medical College & Hospital",
            chamberName = "Panda ENT & Hearing Care",
            chamberAddress = "Remuna Golei, Remuna Road, Balasore",
            consultationFee = 350,
            phone = "06782255220",
            daysOfWeek = "Mon - Sat",
            morningTiming = "08:00 AM - 09:30 AM",
            eveningTiming = "06:00 PM - 09:00 PM",
            emergencyCallAvailable = false,
            latitude = 21.5210,
            longitude = 86.8790,
            rating = 4.7f,
            specialInterests = listOf("Micro Ear Surgery", "Sinus Endoscopy", "Allergic Rhinitis", "Voice Disorders")
        ),
        DoctorProfile(
            id = "doc_7",
            name = "Dr. Tanushree Patra",
            odiaName = "ଡା. ତନୁଶ୍ରୀ ପାତ୍ର",
            qualification = "MBBS, MD (Dermatology, Venereology & Leprosy)",
            specialty = "Dermatology & Skin Care",
            experienceYears = 11,
            affiliationHospital = "Skin & Cosmetology Centre",
            chamberName = "Dermacare Skin Clinic",
            chamberAddress = "Kuruda Chhak, NH-16 Junction, Balasore",
            consultationFee = 400,
            phone = "06782276330",
            daysOfWeek = "Mon - Sat",
            morningTiming = "09:00 AM - 12:00 PM",
            eveningTiming = "05:00 PM - 08:00 PM",
            emergencyCallAvailable = false,
            latitude = 21.4720,
            longitude = 86.9110,
            rating = 4.8f,
            specialInterests = listOf("Psoriasis & Eczema", "Cosmetic Dermatology", "Fungal Skin Infections", "Hair Fall Treatment")
        ),
        DoctorProfile(
            id = "doc_8",
            name = "Dr. Debasis Tripathy",
            odiaName = "ଡା. ଦେବାଶିଷ ତ୍ରିପାଠୀ",
            qualification = "BDS, MDS (Oral & Maxillofacial Surgery)",
            specialty = "Dental Surgery",
            experienceYears = 13,
            affiliationHospital = "Tripathy Dental Hospital & Implant Centre",
            chamberName = "Tripathy Dental Speciality Clinic",
            chamberAddress = "Near Sovarampur Overbridge, Balasore",
            consultationFee = 300,
            phone = "06782265900",
            daysOfWeek = "Mon - Sat (Sun 10 AM - 1 PM)",
            morningTiming = "09:30 AM - 01:00 PM",
            eveningTiming = "05:30 PM - 09:00 PM",
            emergencyCallAvailable = true,
            latitude = 21.4855,
            longitude = 86.9372,
            rating = 4.8f,
            specialInterests = listOf("Dental Implants", "Root Canal Therapy (RCT)", "Wisdom Tooth Surgery", "Orthodontic Braces")
        )
    )

    val medicineStoresList = listOf(
        MedicineStore(
            id = "med_st_1",
            name = "DHH 24x7 Emergency Chemist",
            odiaName = "ଡି.ଏଚ୍.ଏଚ୍. ୨୪ ଘଣ୍ଟିଆ ଜରୁରୀକାଳୀନ ଔଷଧ ଭଣ୍ଡାର",
            areaLocation = "DHH Balasore Campus",
            fullAddress = "Near Main Casualty & Trauma Ward, DHH Campus, Balasore",
            phone = "06782262024",
            alternatePhone = "06782262020",
            is24x7 = true,
            openingHours = "24 Hours (Day & Night 365 Days)",
            hasHomeDelivery = false,
            deliveryRadiusKm = 0,
            emergencyMedicines = listOf("Anti-Snake Venom (ASV)", "Anti-Rabies Vaccine (ARV)", "Human Albumin", "Cardiac Sorbitrate", "Emergency Oxygen", "IV Fluids"),
            licensedPharmacistName = "Prashanta Behera (D.Pharm)",
            latitude = 21.4934,
            longitude = 86.9325,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_2",
            name = "Pradhan Mantri Bhartiya Jan Aushadhi Kendra (FMMCH)",
            odiaName = "ପ୍ରଧାନମନ୍ତ୍ରୀ ଜନ ଔଷଧି କେନ୍ଦ୍ର (ଏଫ୍.ଏମ୍.ଏମ୍.ସି.ଏଚ୍)",
            areaLocation = "Remuna FMMCH",
            fullAddress = "Gate No. 2, Fakir Mohan Medical College Campus, Remuna, Balasore",
            phone = "06782255010",
            alternatePhone = "9437155620",
            is24x7 = true,
            openingHours = "24 Hours Open for Patients",
            hasHomeDelivery = true,
            deliveryRadiusKm = 8,
            emergencyMedicines = listOf("Affordable Generic Medicines (50-90% Discount)", "Insulin (Human & Analog)", "Antihypertensives", "Antibiotics", "Surgical Gauze & Catheters"),
            licensedPharmacistName = "Subrat Kumar Das (B.Pharm)",
            latitude = 21.5284,
            longitude = 86.8647,
            isJanAushadhiGeneric = true
        ),
        MedicineStore(
            id = "med_st_3",
            name = "Apollo Pharmacy 24x7 Station Bazar",
            odiaName = "ଆପୋଲୋ ଫାର୍ମାସୀ ଷ୍ଟେସନ ବଜାର",
            areaLocation = "Station Bazar",
            fullAddress = "Holding No. 142, Station Road, Near Railway Reservation Counter, Balasore",
            phone = "06782264100",
            alternatePhone = "18605000101",
            is24x7 = true,
            openingHours = "24x7 Round-The-Clock Service",
            hasHomeDelivery = true,
            deliveryRadiusKm = 10,
            emergencyMedicines = listOf("Cold-Chain Insulin Storage", "Cardiac Emergency Kits", "Asthma Inhalers & Rotacaps", "Nebulizer Machines", "Pediatric Emergency Syrups"),
            licensedPharmacistName = "Rakesh Pattnayak (M.Pharm)",
            latitude = 21.5020,
            longitude = 86.9395,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_4",
            name = "Maa Tarini Medical Hall & Surgical Store",
            odiaName = "ମାଆ ତାରିଣୀ ମେଡିକାଲ ହଲ୍ ଓ ସର୍ଜିକାଲ",
            areaLocation = "Cinema Chhak",
            fullAddress = "Main Commercial Road, Opposite City Hospital, Cinema Chhak, Balasore",
            phone = "06782262788",
            alternatePhone = "9437088421",
            is24x7 = false,
            openingHours = "07:30 AM - 11:30 PM (Emergency Call Available)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 6,
            emergencyMedicines = listOf("Wheelchairs & Walkers", "Digital BP & Glucometers", "Pulse Oximeters", "Adult Diapers & Urine Bags", "Ortho Belts & Braces"),
            licensedPharmacistName = "Tapan Kumar Mohanty (D.Pharm)",
            latitude = 21.4960,
            longitude = 86.9348,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_5",
            name = "Royal Chemist & Druggist",
            odiaName = "ରୟାଲ କେମିଷ୍ଟ ମୋତିଗଞ୍ଜ",
            areaLocation = "Motiganj Bazar",
            fullAddress = "Grand Road, Motiganj Market Complex, Balasore",
            phone = "06782263312",
            alternatePhone = "9861044320",
            is24x7 = false,
            openingHours = "08:00 AM - 10:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 5,
            emergencyMedicines = listOf("Pediatric Drops", "Chronic Care Refills", "Cardiac Care Medicines", "Anti-Allergy Drugs"),
            licensedPharmacistName = "Manoranjan Panigrahi",
            latitude = 21.4885,
            longitude = 86.9248,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_6",
            name = "Soro LifeCare 24h Medical Store",
            odiaName = "ସୋରୋ ଲାଇଫ୍ କେୟାର ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Soro Sub-division",
            fullAddress = "Near Community Health Centre (CHC) Main Gate, Soro, Balasore",
            phone = "06788220140",
            alternatePhone = "9437299015",
            is24x7 = true,
            openingHours = "24 Hours (Sub-divisional Emergency Chemist)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 12,
            emergencyMedicines = listOf("Anti-Snake Venom", "Tetanus Toxoid", "Delivery & Maternity Kits", "Emergency Antibiotics"),
            licensedPharmacistName = "Dipti Ranjan Barik",
            latitude = 21.2890,
            longitude = 86.6900,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_7",
            name = "Jaleswar Border 24x7 Pharma Care",
            odiaName = "ଜଳେଶ୍ୱର ବର୍ଡର ଫାର୍ମା କେୟାର",
            areaLocation = "Jaleswar Municipality",
            fullAddress = "Station Road, Near GK Bhattar CHC, Jaleswar, Balasore",
            phone = "06781222450",
            alternatePhone = "9778012390",
            is24x7 = true,
            openingHours = "24x7 Highway & Border Emergency Chemist",
            hasHomeDelivery = true,
            deliveryRadiusKm = 10,
            emergencyMedicines = listOf("Highway Trauma First Aid", "Burn Care Dressings", "Oxygen Cylinders", "Emergency Pain Injections"),
            licensedPharmacistName = "Bimalendu Giri",
            latitude = 21.8020,
            longitude = 87.2080,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_8",
            name = "Nilagiri Hillview Medical Store",
            odiaName = "ନୀଳଗିରି ହିଲଭ୍ୟୁ ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Nilagiri Sub-division",
            fullAddress = "Rajbati Road, Near Nilagiri Sub-divisional Hospital, Nilagiri",
            phone = "06782233210",
            alternatePhone = "9438055112",
            is24x7 = false,
            openingHours = "07:00 AM - 10:00 PM (Night Emergency Counter on Bell)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 15,
            emergencyMedicines = listOf("Anti-Snake Venom (Forest area priority)", "Malaria Diagnostic Kits & ACT", "Pediatric ORS", "Antiseptic Creams"),
            licensedPharmacistName = "Ashok Kumar Singha",
            latitude = 21.4600,
            longitude = 86.7650,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_9",
            name = "Subasini Medical Store (24 Hours)",
            odiaName = "ସୁବାସିନୀ ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Station Road",
            fullAddress = "In front of Durga Mandap, Near Reliance Smart, Station Road, Balasore",
            phone = "06782262145",
            alternatePhone = "9437081245",
            is24x7 = true,
            openingHours = "24 Hours (Round-The-Clock Emergency)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 8,
            emergencyMedicines = listOf("Anti-Rabies Vaccine (ARV)", "Anti-Snake Venom (ASV)", "Cardiac Emergency Injections", "Cold-Chain Insulin", "Nebulizer Respules"),
            licensedPharmacistName = "Debashis Mohapatra (B.Pharm)",
            latitude = 21.4998,
            longitude = 86.9372,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_10",
            name = "Urmila Medicals (24x7 Emergency)",
            odiaName = "ଉର୍ମିଳା ମେଡିକାଲ୍ସ",
            areaLocation = "Azimabad OT Road",
            fullAddress = "Near Station Square, OT Road, Azimabad, Balasore",
            phone = "06782264880",
            alternatePhone = "9437264880",
            is24x7 = true,
            openingHours = "24x7 Open (Day & Night Emergency)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 9,
            emergencyMedicines = listOf("Emergency Trauma Dressing", "IV Cannula & Infusion Sets", "Human Albumin", "Cardiac Sorbitrate", "Pediatric Antibiotics"),
            licensedPharmacistName = "Ranjit Kumar Jena (D.Pharm)",
            latitude = 21.4925,
            longitude = 86.9280,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_11",
            name = "Devee Medical Store",
            odiaName = "ଦେବୀ ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Station Square OT Road",
            fullAddress = "Station Square, Opposite State Bank ATM, OT Road, Balasore",
            phone = "06782263150",
            alternatePhone = "9861233150",
            is24x7 = false,
            openingHours = "07:30 AM - 11:00 PM (Emergency Window on Request)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 7,
            emergencyMedicines = listOf("Digital Blood Pressure Monitors", "Accu-Chek Glucometers & Strips", "Asthma Inhalers", "Pediatric Drops", "Chronic Hypertension Refills"),
            licensedPharmacistName = "Bikash Chandra Dey (B.Pharm)",
            latitude = 21.4975,
            longitude = 86.9330,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_12",
            name = "Geeta Medical Store",
            odiaName = "ଗୀତା ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Zilla School Road",
            fullAddress = "Near Hotel Panchajanya, Zilla School Road, Azimabad, Balasore",
            phone = "06782265210",
            alternatePhone = "9437165210",
            is24x7 = false,
            openingHours = "08:00 AM - 10:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 6,
            emergencyMedicines = listOf("Diabetic Insulin Cartridges", "Orthopedic Heat Belts & Creams", "General Antibiotics", "Oral Rehydration Solutions"),
            licensedPharmacistName = "Pratap Kumar Panda",
            latitude = 21.4940,
            longitude = 86.9270,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_13",
            name = "Ankit Medicine Store",
            odiaName = "ଅଙ୍କିତ ମେଡିସିନ ଷ୍ଟୋର",
            areaLocation = "Adm Square OT Road",
            fullAddress = "Near Public School, Collectorate ADM Square, OT Road, Balasore",
            phone = "06782263900",
            alternatePhone = "9437063900",
            is24x7 = false,
            openingHours = "08:00 AM - 11:00 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 8,
            emergencyMedicines = listOf("Trauma Antiseptic Dressing", "Surgical Sutures & Blades", "Anti-Pyretic Infusions", "Nebulizer Masks"),
            licensedPharmacistName = "Ankit Kumar Sahu (M.Pharm)",
            latitude = 21.4910,
            longitude = 86.9260,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_14",
            name = "Pradhan Mantri Jan Aushadhi Kendra (Sahadevkhunta)",
            odiaName = "ପ୍ରଧାନମନ୍ତ୍ରୀ ଜନ ଔଷଧି କେନ୍ଦ୍ର (ସହଦେବଖୁଣ୍ଟା)",
            areaLocation = "Sahadevkhunta",
            fullAddress = "Near Sahadevkhunta Bus Stand, Market Complex, Balasore",
            phone = "06782251020",
            alternatePhone = "9437451020",
            is24x7 = false,
            openingHours = "08:30 AM - 09:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 7,
            emergencyMedicines = listOf("Affordable Generic Medicines (60-90% Discount)", "Generic Metformin & Glimepiride", "Telmisartan & Amlodipine", "Paracetamol & Pantoprazole Generics"),
            licensedPharmacistName = "Sanjay Kumar Das (D.Pharm)",
            latitude = 21.4980,
            longitude = 86.9420,
            isJanAushadhiGeneric = true
        ),
        MedicineStore(
            id = "med_st_15",
            name = "Swasti Shuvam Medicine Store",
            odiaName = "ସ୍ୱସ୍ତି ଶୁଭମ ମେଡିସିନ ଷ୍ଟୋର",
            areaLocation = "ITI Square Fakir Mohan Nagar",
            fullAddress = "ITI Chhak, Fakir Mohan Nagar, Balasore",
            phone = "06782266110",
            alternatePhone = "9861066110",
            is24x7 = false,
            openingHours = "08:00 AM - 10:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 6,
            emergencyMedicines = listOf("Student First-Aid Kits", "Sports Pain Relief Sprays", "Burn Ointments", "Antibiotic Eye & Ear Drops"),
            licensedPharmacistName = "Shuvam Sekhar Behera (B.Pharm)",
            latitude = 21.4990,
            longitude = 86.9180,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_16",
            name = "Apollo Pharmacy (Remuna Golei Ganeswarpur)",
            odiaName = "ଆପୋଲୋ ଫାର୍ମାସୀ (ରେମୁଣା ଗୋଲେଇ)",
            areaLocation = "Remuna Golei",
            fullAddress = "Holding No. 88, NH-16 Link Road, Ganeswarpur, Remuna Golei, Balasore",
            phone = "06782254100",
            alternatePhone = "18605000101",
            is24x7 = true,
            openingHours = "24x7 Round-The-Clock Service",
            hasHomeDelivery = true,
            deliveryRadiusKm = 10,
            emergencyMedicines = listOf("24x7 Cold Chain Vaccines", "Cardiac Care & Statins", "Advanced Oncology Supportive Care", "Pediatric Nutrition & Diapers"),
            licensedPharmacistName = "Sourav Bhattacharya (M.Pharm)",
            latitude = 21.5120,
            longitude = 86.9050,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_17",
            name = "Arogya Medicare (Remuna Golai)",
            odiaName = "ଆରୋଗ୍ୟ ମେଡିକେୟାର (ରେମୁଣା ଗୋଲାଇ)",
            areaLocation = "Remuna Golei Kukudapada",
            fullAddress = "Near Kukudapada Chhak, Remuna Golai Road, Balasore",
            phone = "06782256200",
            alternatePhone = "9438056200",
            is24x7 = false,
            openingHours = "07:30 AM - 10:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 8,
            emergencyMedicines = listOf("Emergency Surgical Equipment", "Orthopedic Knee & Ankle Supports", "Critical Care Injections", "Saline & Dextrose IV Infusions"),
            licensedPharmacistName = "Manas Ranjan Giri (B.Pharm)",
            latitude = 21.5150,
            longitude = 86.8980,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_18",
            name = "Medplus Pharmacy (Sovarampur)",
            odiaName = "ମେଡପ୍ଲସ୍ ଫାର୍ମାସୀ (ଶୋଭାରାମପୁର)",
            areaLocation = "Sovarampur",
            fullAddress = "Main Road, Near Flyover Bridge, Sovarampur, Balasore",
            phone = "06782245300",
            alternatePhone = "04067006700",
            is24x7 = false,
            openingHours = "08:00 AM - 11:00 PM (App Home Delivery Available)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 10,
            emergencyMedicines = listOf("Prescription Chronic Medicine Refills", "Medplus Quality Generic Discounts", "Baby Formulas & Gripe Water", "Surgical Disposables"),
            licensedPharmacistName = "Kunal Kumar Nayak",
            latitude = 21.4780,
            longitude = 86.9200,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_19",
            name = "Raghunath Jew Medical Store",
            odiaName = "ରଘୁନାଥ ଜିଉ ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Sovarampur",
            fullAddress = "Near Raghunath Temple Square, Sovarampur Road, Balasore",
            phone = "06782246120",
            alternatePhone = "9437146120",
            is24x7 = false,
            openingHours = "07:30 AM - 10:00 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 6,
            emergencyMedicines = listOf("Elderly Geriatric Daily Medicines", "Diabetes Care Packs", "Digestive & Antacid Syrups", "Vitamins & Mineral Supplements"),
            licensedPharmacistName = "Narayan Chandra Mohanty",
            latitude = 21.4750,
            longitude = 86.9180,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_20",
            name = "Saikrupa Medical Store (Kuruda)",
            odiaName = "ସାଇକୃପା ମେଡିକାଲ ଷ୍ଟୋର (କୁରୁଡା)",
            areaLocation = "Kuruda NH-16",
            fullAddress = "Kuruda Market Complex, NH-16 Highway Junction, Balasore",
            phone = "06782271400",
            alternatePhone = "9861071400",
            is24x7 = true,
            openingHours = "24x7 Open (Highway Emergency Counter)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 10,
            emergencyMedicines = listOf("Highway Trauma First Aid", "Sterile Bandages & Cotton", "Pain Relief & Anti-Spasmodic Injections", "Anti-Snake Venom (ASV)"),
            licensedPharmacistName = "Rajendra Kumar Sethi (D.Pharm)",
            latitude = 21.4710,
            longitude = 86.9140,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_21",
            name = "Hari Omm Medical Store (Kuruda)",
            odiaName = "ହରି ଓମ୍ ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Kuruda Market",
            fullAddress = "Near Indian Oil Petrol Pump, Kuruda Chhak, Balasore",
            phone = "06782272210",
            alternatePhone = "9437272210",
            is24x7 = false,
            openingHours = "08:00 AM - 10:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 7,
            emergencyMedicines = listOf("Burn Dressings & Silver Sulfadiazine", "Tetanus Toxoid Vaccine", "Antibiotic Ointments", "Electrolyte Hydration Sachets"),
            licensedPharmacistName = "Om Prakash Patra",
            latitude = 21.4690,
            longitude = 86.9120,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_22",
            name = "Jan Aushadhi Kendra (Kuruda Junction)",
            odiaName = "ଜନ ଔଷଧି କେନ୍ଦ୍ର (କୁରୁଡା ଜଙ୍କସନ)",
            areaLocation = "Kuruda NH-16",
            fullAddress = "Shop No. 4, Commercial Complex, Kuruda Bypass, Balasore",
            phone = "06782273150",
            alternatePhone = "9438073150",
            is24x7 = false,
            openingHours = "08:30 AM - 09:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 8,
            emergencyMedicines = listOf("Low-cost Generic Essential Drugs", "BP & Heart Generics", "Anti-Diabetic Generics", "Generic Pain Relief"),
            licensedPharmacistName = "Santosh Kumar Mallick (D.Pharm)",
            latitude = 21.4730,
            longitude = 86.9160,
            isJanAushadhiGeneric = true
        ),
        MedicineStore(
            id = "med_st_23",
            name = "Jay Jagannath Medicine Store",
            odiaName = "ଜୟ ଜଗନ୍ନାଥ ମେଡିସିନ ଷ୍ଟୋର",
            areaLocation = "Manikhamb Hospital Road",
            fullAddress = "Manikhamb, Near Puruna Balasore Road, Hospital Approach, Balasore",
            phone = "06782261880",
            alternatePhone = "9437161880",
            is24x7 = false,
            openingHours = "08:00 AM - 10:00 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 5,
            emergencyMedicines = listOf("Maternal Care Injections", "Pediatric Cough & Cold Drops", "Fever Syrups", "Antiseptic Washes"),
            licensedPharmacistName = "Jagannath Mohanty",
            latitude = 21.4870,
            longitude = 86.9380,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_24",
            name = "THE Health ZONE Pharmacy",
            odiaName = "ଦ ହେଲ୍ଥ ଜୋନ୍ ଫାର୍ମାସୀ",
            areaLocation = "Nayabazar Kachery",
            fullAddress = "Near Kachery, M.K. Deb Street, Nayabazar, Balasore",
            phone = "06782267500",
            alternatePhone = "9861067500",
            is24x7 = false,
            openingHours = "08:30 AM - 10:30 PM",
            hasHomeDelivery = true,
            deliveryRadiusKm = 7,
            emergencyMedicines = listOf("Skin & Dermatology Speciality Medicines", "Hair & Scalp Treatments", "Cardiac Life-saving Drugs", "Multivitamins"),
            licensedPharmacistName = "Amitabh Das (B.Pharm)",
            latitude = 21.4965,
            longitude = 86.9450,
            isJanAushadhiGeneric = false
        ),
        MedicineStore(
            id = "med_st_25",
            name = "Draupadi Medical Store (Chandipur Road)",
            odiaName = "ଦ୍ରୌପଦୀ ମେଡିକାଲ ଷ୍ଟୋର",
            areaLocation = "Chandipur Road",
            fullAddress = "Chandipur Sea Beach Road, Near DRDO Cantonment Gate, Balasore",
            phone = "06782270320",
            alternatePhone = "9437370320",
            is24x7 = false,
            openingHours = "07:00 AM - 10:00 PM (Emergency Night Call Available)",
            hasHomeDelivery = true,
            deliveryRadiusKm = 10,
            emergencyMedicines = listOf("Jellyfish & Marine Sting Topical Creams", "Anti-Allergy Injectables", "Heat Stroke Hydration Kits", "First Aid Splints & Dressing"),
            licensedPharmacistName = "Trilochan Behera (D.Pharm)",
            latitude = 21.4720,
            longitude = 87.0120,
            isJanAushadhiGeneric = false
        )
    )

    val polyclinicsList = listOf(
        PolyclinicCenter(
            id = "poly_1",
            name = "Balasore Poly Clinic & Diagnostic Centre",
            odiaName = "ବାଲେଶ୍ୱର ପଲି କ୍ଲିନିକ୍ ଓ ଡାଇଗ୍ନୋଷ୍ଟିକ୍ ସେଣ୍ଟର",
            category = "Multi-specialty Polyclinic & Imaging",
            address = "Motiganj Bazar, Main Road, Balasore",
            landmark = "Near State Bank of India Motiganj Branch",
            phone = "06782263550",
            openHours = "06:30 AM - 10:00 PM (Daily)",
            facilities = listOf("4D Colour Doppler USG", "Digital 500mA X-Ray", "Fully Automated Biochemistry Analyzer", "Computerized ECG", "Echocardiography", "Visiting Specialist Chambers"),
            sampleCollectionTimings = "06:30 AM - 08:30 PM (Fasting samples: 06:30 - 10:30 AM)",
            reportDeliverySameDay = true,
            visitingSpecialistsList = listOf("Dr. B. K. Jena (Cardiology - Wed/Fri)", "Dr. P. R. Das (Orthopedics - Mon/Thu)", "Dr. M. Senapati (Gynecology - Daily)"),
            latitude = 21.4882,
            longitude = 86.9252,
            ambulanceSupport = true
        ),
        PolyclinicCenter(
            id = "poly_2",
            name = "Kalinga Healthcare & Diagnostic Polyclinic",
            odiaName = "କଳିଙ୍ଗ ହେଲଥକେୟାର ଓ ଡାଇଗ୍ନୋଷ୍ଟିକ୍ ପଲିକ୍ଲିନିକ୍",
            category = "Multi-specialty Daycare & Pathology",
            address = "Station Road, Vivekananda Marg Junction, Balasore",
            landmark = "Opposite Old Bus Stand, Station Road",
            phone = "06782264880",
            openHours = "06:00 AM - 09:30 PM",
            facilities = listOf("Complete Blood Profile (CBC)", "Thyroid & Hormone Assay", "Digital X-Ray", "Pulmonary Function Test (PFT)", "Daycare Saline & Injection Beds", "Physiotherapy Unit"),
            sampleCollectionTimings = "06:00 AM - 08:00 PM (Home Sample Pickup Available)",
            reportDeliverySameDay = true,
            visitingSpecialistsList = listOf("Dr. A. K. Mohanty (Medicine - Mon to Sat)", "Dr. S. Panda (ENT - Daily)", "Dr. T. Patra (Skin - Tue/Sat)"),
            latitude = 21.5005,
            longitude = 86.9380,
            ambulanceSupport = true
        ),
        PolyclinicCenter(
            id = "poly_3",
            name = "Apollo Diagnostics & Multi-Speciality Clinic",
            odiaName = "ଆପୋଲୋ ଡାଇଗ୍ନୋଷ୍ଟିକ୍ସ ଓ ମଲ୍ଟି ସ୍ପେଶାଲିଟି କ୍ଲିନିକ୍",
            category = "NABL Accredited Diagnostics & Super Speciality",
            address = "Holding 78, Vivekananda Marg, Near FM College Chhak, Balasore",
            landmark = "Next to Apollo Pharmacy, Vivekananda Marg",
            phone = "06782262445",
            openHours = "07:00 AM - 09:00 PM",
            facilities = listOf("NABL Accredited Pathology", "Lipid & Cardiac Profiles", "HbA1c Diabetes Tracking", "Dietician & Nutritionist Consultations", "Tele-consultation with Apollo Chennai/Hyd"),
            sampleCollectionTimings = "07:00 AM - 07:30 PM (Barcoded Samples with Online Portal)",
            reportDeliverySameDay = true,
            visitingSpecialistsList = listOf("Super-specialist Visiting Roster (Neurology, Nephrology, Oncology monthly)"),
            latitude = 21.4945,
            longitude = 86.9320,
            ambulanceSupport = false
        ),
        PolyclinicCenter(
            id = "poly_4",
            name = "Remuna Modern Daycare Polyclinic",
            odiaName = "ରେମୁଣା ମଡର୍ଣ୍ଣ ଡେ-କେୟାର ପଲିକ୍ଲିନିକ୍",
            category = "Daycare & Preventive Health Hub",
            address = "Remuna Golei, Medical College Bypass Road, Balasore",
            landmark = "2 km before FMMCH Medical College Campus",
            phone = "06782255880",
            openHours = "07:00 AM - 10:00 PM",
            facilities = listOf("Pre-Operative Fitness Checks", "Wound Dressing & Minor Stitching OT", "Nebulization Station", "Childhood Immunization Point", "Ultrasound Scanning"),
            sampleCollectionTimings = "07:00 AM - 08:00 PM",
            reportDeliverySameDay = true,
            visitingSpecialistsList = listOf("Dr. S. Rout (Pediatrics)", "Dr. D. Tripathy (Dental)", "General Duty Medical Officers (24x7)"),
            latitude = 21.5220,
            longitude = 86.8770,
            ambulanceSupport = true
        ),
        PolyclinicCenter(
            id = "poly_5",
            name = "Curewell Polyclinic & Advanced Pathology",
            odiaName = "କ୍ୟୋରୱେଲ ପଲିକ୍ଲିନିକ୍ ଓ ପାଥୋଲୋଜି",
            address = "OT Road, Near SP Office & Reserve Police, Balasore",
            category = "Advanced Pathology & Diagnostics",
            landmark = "Near District Collectorate Crossing",
            phone = "06782261770",
            openHours = "06:30 AM - 09:30 PM",
            facilities = listOf("Automated Hematology Analyzer", "Electrolyte & Arterial Blood Gas (ABG)", "Digital Mammography", "Bone Mineral Density (BMD)", "Senior Citizen Health Checkups"),
            sampleCollectionTimings = "06:30 AM - 08:30 PM",
            reportDeliverySameDay = true,
            visitingSpecialistsList = listOf("Dr. M. Senapati (Gynecology)", "Visiting Urologist (Every Sunday)"),
            latitude = 21.4930,
            longitude = 86.9300,
            ambulanceSupport = false
        ),
        PolyclinicCenter(
            id = "poly_6",
            name = "Nilagiri Sub-divisional Polyclinic & Wellness Centre",
            odiaName = "ନୀଳଗିରି ପଲିକ୍ଲିନିକ୍ ଓ ୱେଲନେସ୍ କେନ୍ଦ୍ର",
            category = "Rural Healthcare & Diagnostic Hub",
            address = "Main Market Road, Near Town Police Station, Nilagiri",
            landmark = "Opposite Raja Jagannath Temple Entrance, Nilagiri",
            phone = "06782233400",
            openHours = "07:00 AM - 08:30 PM",
            facilities = listOf("Routine Blood & Urine Testing", "Fever Profile & Dengue/Malaria Rapid Kits", "X-Ray & ECG", "Ayurvedic & Herbal Wellness Wing", "First-Aid Daycare"),
            sampleCollectionTimings = "07:00 AM - 06:00 PM",
            reportDeliverySameDay = true,
            visitingSpecialistsList = listOf("Visiting Orthopedic Surgeon (Saturdays)", "General Physicians Daily"),
            latitude = 21.4590,
            longitude = 86.7640,
            ambulanceSupport = true
        )
    )

    /**
     * Compute dynamic daily doctor on-duty status based on day of week and current hour.
     */
    fun getDoctorDailyStatus(doctor: DoctorProfile): String {
        val cal = Calendar.getInstance()
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val hour = cal.get(Calendar.HOUR_OF_DAY)

        val isSunday = dayOfWeek == Calendar.SUNDAY

        return when {
            isSunday && doctor.emergencyCallAvailable -> "🚨 Sunday Emergency On-Call Only"
            isSunday -> "🏖️ Chamber Closed on Sunday"
            hour in 8..12 -> "🟢 Morning Chamber Open: ${doctor.morningTiming}"
            hour in 12..16 -> "⏳ Afternoon Break • Evening Session starts ${doctor.eveningTiming.substringBefore("-").trim()}"
            hour in 17..21 -> "🟢 Evening Chamber Active: ${doctor.eveningTiming}"
            hour >= 22 || hour < 7 -> "🌙 Night Closed • Next Chamber: Tomorrow ${doctor.morningTiming}"
            else -> "📋 OPD Registration Open"
        }
    }

    /**
     * Compute if a medicine store is open right now based on 24x7 flag and current time.
     */
    fun isStoreOpenNow(store: MedicineStore): Boolean {
        if (store.is24x7) return true
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        // Regular stores usually open 07:30 to 22:30
        return hour in 7..22
    }

    /**
     * Compute polyclinic diagnostic sample collection status right now.
     */
    fun getDiagnosticCollectionStatus(): String {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return when {
            hour in 6..10 || (hour == 10 && minute <= 30) -> "🩸 FASTING SAMPLE WINDOW ACTIVE (06:30 AM - 10:30 AM): Fast-track blood draws for Sugar, Lipid, Thyroid."
            hour in 11..16 -> "🔬 ROUTINE TESTING & IMAGING IN PROGRESS: X-Ray, USG, ECG & Urine profiles active."
            hour in 17..20 -> "📄 EVENING REPORT PICKUP & DIGITAL DISPATCH: Lab reports ready for counter collection & WhatsApp."
            else -> "🌙 NIGHT LAB ON STANDBY: Emergency STAT investigations active at DHH & FMMCH."
        }
    }

    val pathologyLabsList = listOf(
        PathologyLab(
            id = "path_1",
            name = "Dr. Lal PathLabs - Balasore Reference Lab",
            odiaName = "ଡାକ୍ତର ଲାଲ୍ ପାଥ୍‌ଲ୍ୟାବ୍ସ ରେଫରେନ୍ସ ଲ୍ୟାବ୍",
            accreditation = "NABL Accredited & CAP Certified",
            address = "Holding 112, Vivekananda Marg, Near FM Golapokhari, Balasore",
            landmark = "Opposite Old Collectorate Road, Vivekananda Marg",
            phone = "06782264900",
            alternatePhone = "01149885050",
            homeCollectionPhone = "9437166500",
            openingHours = "06:30 AM - 09:00 PM (Mon - Sun)",
            fastingCollectionHours = "06:30 AM - 11:00 AM (Fasting Sugar & Lipid Window)",
            homeSampleCollectionAvailable = true,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Routine reports in 4-6 hours via WhatsApp & Online Portal",
            popularPackages = listOf(
                PathologyTestPackage("SwasthFit Super Full Body Panel", "84 tests including CBC, Lipid, Liver, Kidney, Thyroid, Sugar & Urine", "₹1,499", "Fasting Blood & Urine", 6),
                PathologyTestPackage("Complete Hemogram (CBC + ESR)", "24 parameters with automated 6-part cell counter", "₹280", "EDTA Whole Blood", 3),
                PathologyTestPackage("Comprehensive Lipid Profile", "Cholesterol, Triglycerides, HDL, LDL, VLDL ratio", "₹450", "12h Fasting Blood", 4),
                PathologyTestPackage("Thyroid Function Panel (T3, T4, TSH)", "Chemiluminescence Immunoassay (CLIA) methodology", "₹399", "Serum Blood", 4),
                PathologyTestPackage("HbA1c Glycated Hemoglobin", "HPLC Gold Standard method for 3-month diabetes average", "₹380", "Whole Blood", 4)
            ),
            specialEquipments = listOf("Beckman Coulter 6-Part Hematology", "Roche Cobas 6000 Immuno-Chemistry", "Bio-Rad D-10 HPLC Analyzer"),
            latitude = 21.4940,
            longitude = 86.9315,
            rating = 4.9f
        ),
        PathologyLab(
            id = "path_2",
            name = "Apollo Diagnostics Pathology & Blood Testing Hub",
            odiaName = "ଆପୋଲୋ ଡାଇଗ୍ନୋଷ୍ଟିକ୍ସ ପାଥୋଲୋଜି ହବ୍",
            accreditation = "NABL Accredited • 100% Barcoded Samples",
            address = "Holding 78, Vivekananda Marg, Balasore",
            landmark = "Adjacent to Apollo Pharmacy, Near FM College Chhak",
            phone = "06782262445",
            alternatePhone = "18605000777",
            homeCollectionPhone = "9437288410",
            openingHours = "07:00 AM - 08:30 PM (Daily)",
            fastingCollectionHours = "07:00 AM - 10:30 AM",
            homeSampleCollectionAvailable = true,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Digital SMS report download link within 4 hours of processing",
            popularPackages = listOf(
                PathologyTestPackage("Apollo ProHealth Master Check", "Complete Blood Count, HbA1c, LFT, KFT, Vitamin D, Vitamin B12, Urine", "₹1,850", "Fasting Blood & Urine", 6),
                PathologyTestPackage("Liver Function Test (LFT)", "Bilirubin, SGOT, SGPT, Alkaline Phosphatase, Protein/Albumin", "₹550", "Serum Blood", 4),
                PathologyTestPackage("Kidney Function Test (KFT / RFT)", "Urea, Creatinine, Uric Acid, Calcium, Electrolytes", "₹500", "Serum Blood", 4),
                PathologyTestPackage("Vitamin D (25-OH) & Vitamin B12 Duo", "Advanced automated electrochemiluminescence assay", "₹999", "Fasting Blood", 8)
            ),
            specialEquipments = listOf("Abbott Architect ci4100 Integrated System", "Siemens Centaur XP Immunoassay", "Barcoded BD Vacutainer System"),
            latitude = 21.4947,
            longitude = 86.9322,
            rating = 4.8f
        ),
        PathologyLab(
            id = "path_3",
            name = "Agilus Diagnostics (Formerly SRL)",
            odiaName = "ଏଜିଲସ୍ ଡାଇଗ୍ନୋଷ୍ଟିକ୍ସ (ପୂର୍ବରୁ ଏସ୍.ଆର୍.ଏଲ୍)",
            accreditation = "NABL Accredited & ISO 15189 Certified",
            address = "Grand Road, Motiganj Market Complex, Balasore",
            landmark = "Near LIC Divisional Office Branch, Motiganj",
            phone = "06782261880",
            alternatePhone = "9437022190",
            homeCollectionPhone = "9437022190",
            openingHours = "06:30 AM - 09:30 PM",
            fastingCollectionHours = "06:30 AM - 11:00 AM",
            homeSampleCollectionAvailable = true,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Same-day verified digital reports signed by MD Pathologists",
            popularPackages = listOf(
                PathologyTestPackage("Agilus Active Care Complete", "78 Parameters covering metabolic, renal, hepatic, cardiac risk", "₹1,299", "10-12h Fasting Blood", 5),
                PathologyTestPackage("Cardiac Risk Markers (Troponin-I + hs-CRP)", "Quantitative cardiac enzymes for chest pain evaluation", "₹850", "STAT Plasma", 2),
                PathologyTestPackage("Dengue NS1 Antigen + IgG/IgM Antibody", "Rapid automated immunofluorescence test", "₹650", "Serum Blood", 3),
                PathologyTestPackage("Urine Complete Analysis & Microscopy", "Automated urine flow cytometry and strip analyzer", "₹180", "Midstream Urine", 2)
            ),
            specialEquipments = listOf("Sysmex XN-550 Automated Hematology", "Vitros 5600 Integrated Dry-Chemistry System"),
            latitude = 21.4880,
            longitude = 86.9245,
            rating = 4.8f
        ),
        PathologyLab(
            id = "path_4",
            name = "Metropolis Healthcare Clinical Laboratory",
            odiaName = "ମେଟ୍ରୋପଲିସ୍ ହେଲଥକେୟାର କ୍ଲିନିକାଲ ଲ୍ୟାବୋରେଟୋରୀ",
            accreditation = "CAP & NABL Accredited Multi-National Lab",
            address = "Station Road, Cinema Chhak Square, Balasore",
            landmark = "Opposite District Court Complex, Balasore",
            phone = "06782265100",
            alternatePhone = "9321272715",
            homeCollectionPhone = "9321272715",
            openingHours = "07:00 AM - 08:30 PM",
            fastingCollectionHours = "07:00 AM - 10:30 AM",
            homeSampleCollectionAvailable = true,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Fast-track smart reports with historical graph trends",
            popularPackages = listOf(
                PathologyTestPackage("Metropolis TruHealth Smart Panel", "Vital organ screening with comprehensive risk indices", "₹1,350", "Fasting Blood", 6),
                PathologyTestPackage("Allergy Screening & IgE Total", "Automated immunoblot allergy sensitivity profiling", "₹750", "Serum Blood", 8),
                PathologyTestPackage("Serum Electrolytes (Na, K, Cl, Bicarbonate)", "Direct Ion Selective Electrode (ISE) determination", "₹420", "Serum Blood", 2)
            ),
            specialEquipments = listOf("Roche Cobas c501 Chemistry", "Ortho Vitros 350", "Mindray BC-6800 Plus"),
            latitude = 21.4970,
            longitude = 86.9355,
            rating = 4.7f
        ),
        PathologyLab(
            id = "path_5",
            name = "Balasore Central 24x7 Emergency STAT Lab",
            odiaName = "ବାଲେଶ୍ୱର ସେଣ୍ଟ୍ରାଲ ଜରୁରୀକାଳୀନ ପାଥୋଲୋଜି ଲ୍ୟାବ୍",
            accreditation = "District Health Board Registered • 24x7 Emergency STAT",
            address = "Hospital Road, Near DHH Casualty Entrance, Balasore",
            landmark = "50 meters from DHH Main Casualty & Blood Bank",
            phone = "06782263120",
            alternatePhone = "06782262024",
            homeCollectionPhone = null,
            openingHours = "24 Hours (Round-The-Clock Trauma Testing)",
            fastingCollectionHours = "06:00 AM - 11:30 AM (Routine) • 24x7 Emergency STAT",
            homeSampleCollectionAvailable = false,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Emergency STAT blood tests within 45 to 60 minutes",
            popularPackages = listOf(
                PathologyTestPackage("Emergency Trauma Profile", "Blood Grouping, Crossmatching, CBC, PT/INR, Serum Electrolytes", "₹450", "Emergency Blood", 1),
                PathologyTestPackage("Blood Sugar Fasting & PP", "Hexokinase enzymatic method with instant digital slip", "₹90", "Fasting & Post-Prandial", 1),
                PathologyTestPackage("Malaria (Pf/Pv Antigen) & Chikungunya Rapid", "Bivalent rapid diagnostic card test", "₹220", "Whole Blood Fingerprick", 1),
                PathologyTestPackage("Arterial Blood Gas (ABG) & Lactate", "Critical care bedside gasometry analyzer", "₹650", "Heparinized Arterial Blood", 1)
            ),
            specialEquipments = listOf("Radiometer ABL90 FLEX Blood Gas", "Mindray BC-3000 Plus", "Erba Chem 7 Semi-Automated"),
            latitude = 21.4938,
            longitude = 86.9328,
            rating = 4.9f
        ),
        PathologyLab(
            id = "path_6",
            name = "Thyrocare Aarogyam Blood Collection Point",
            odiaName = "ଥାଇରୋକେୟାର ଆରୋଗ୍ୟମ ବ୍ଲଡ୍ କଲେକ୍ସନ ପଏଣ୍ଟ",
            accreditation = "NABL Accredited Central Processing Lab Network",
            address = "Holding 42, Station Road, Balasore",
            landmark = "Near Balasore Railway Station Reservation Counter",
            phone = "06782262330",
            alternatePhone = "9437155200",
            homeCollectionPhone = "9437155200",
            openingHours = "06:30 AM - 08:00 PM",
            fastingCollectionHours = "06:30 AM - 11:00 AM",
            homeSampleCollectionAvailable = true,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Barcoded vacuum tubes processed at central robotic facility within 12h",
            popularPackages = listOf(
                PathologyTestPackage("Aarogyam 1.3 Preventive Health Package", "89 Tests: Lipid, Cardiac, Iron deficiency, Thyroid, Vitamin, Kidney", "₹1,200", "12h Fasting Blood & Urine", 12),
                PathologyTestPackage("Aarogyam Female Wellness Profile", "Hormonal screening with Vitamin D3, B12, Iron and Thyroid", "₹1,400", "Fasting Blood", 12),
                PathologyTestPackage("Thyroid Screen Ultrasensitive TSH", "Electro-Chemiluminescence Immuno-Assay", "₹199", "Serum Blood", 6)
            ),
            specialEquipments = listOf("Siemens ADVIA Centaur XP", "Abbott Architect i2000SR"),
            latitude = 21.5018,
            longitude = 86.9390,
            rating = 4.7f
        ),
        PathologyLab(
            id = "path_7",
            name = "Soro Sub-Divisional Clinical Pathology Centre",
            odiaName = "ସୋରୋ ସବ୍-ଡିଭିଜନାଲ କ୍ଲିନିକାଲ ପାଥୋଲୋଜି ସେଣ୍ଟର",
            accreditation = "Certified Clinical Pathology Facility",
            address = "Main Market Road, Near Soro Bus Stand, Soro",
            landmark = "200 meters from Soro Community Health Centre (CHC)",
            phone = "06788220550",
            alternatePhone = "9437299330",
            homeCollectionPhone = "9437299330",
            openingHours = "06:30 AM - 09:00 PM",
            fastingCollectionHours = "06:30 AM - 10:30 AM",
            homeSampleCollectionAvailable = true,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Same-day morning blood reports delivered by 03:00 PM",
            popularPackages = listOf(
                PathologyTestPackage("Soro Routine Health Screen", "CBC, Fasting Blood Glucose, Urine Routine, Serum Creatinine", "₹380", "Fasting Blood & Urine", 4),
                PathologyTestPackage("Pregnancy Antenatal Profile (ANC)", "Blood Group, Rh typing, VDRL, HIV, HBsAg, CBC, Urine Sugar/Albumin", "₹520", "Blood & Urine", 4),
                PathologyTestPackage("Widal Test & Typhoid IgM Screen", "Slide and tube agglutination for enteric fever diagnosis", "₹180", "Serum Blood", 3)
            ),
            specialEquipments = listOf("ERBA Mannheim Chem 5x", "Sysmex 3-Part Cell Counter"),
            latitude = 21.2885,
            longitude = 86.6895,
            rating = 4.6f
        ),
        PathologyLab(
            id = "path_8",
            name = "Nilagiri Diagnostic & Blood Testing Hub",
            odiaName = "ନୀଳଗିରି ଡାଇଗ୍ନୋଷ୍ଟିକ୍ ଓ ରକ୍ତ ପରୀକ୍ଷା କେନ୍ଦ୍ର",
            accreditation = "Regional Clinical Laboratory Service",
            address = "Rajbati Road, Nilagiri Town, Balasore",
            landmark = "Opposite Jagannath Temple Chhak, Nilagiri",
            phone = "06782233620",
            alternatePhone = "9438012110",
            homeCollectionPhone = "9438012110",
            openingHours = "07:00 AM - 08:30 PM",
            fastingCollectionHours = "07:00 AM - 10:30 AM",
            homeSampleCollectionAvailable = true,
            sameDayDigitalReports = true,
            testTurnaroundDescription = "Digital WhatsApp reports with local paper printout service",
            popularPackages = listOf(
                PathologyTestPackage("Tribal & Forest Region Fever Screen", "Malaria Smear, Dengue Rapid, CBC, Typhoid Screen, Platelet Count", "₹420", "Blood Sample", 3),
                PathologyTestPackage("Senior Citizen Basic Wellness Check", "Blood Sugar, Lipid Profile, Serum Creatinine, Uric Acid, Urine Routine", "₹580", "12h Fasting", 4),
                PathologyTestPackage("Routine Urine Routine & Microscopic Examination", "Physical, Chemical and Microscopic sediment examination", "₹120", "Fresh Early Morning Urine", 2)
            ),
            specialEquipments = listOf("Microscope with HD Imaging", "Automated Bio-chemistry Analyzer"),
            latitude = 21.4605,
            longitude = 86.7645,
            rating = 4.6f
        )
    )

    val privateHospitalsList = listOf(
        PrivateHospital(
            id = "hosp_1",
            name = "Jyoti Hospital & Research Centre",
            odiaName = "ଜ୍ୟୋତି ହସ୍ପିଟାଲ ଓ ରିସର୍ଚ୍ଚ ସେଣ୍ଟର",
            category = "Multi-Speciality Hospital",
            totalBeds = 120,
            icuBedsCount = 18,
            nicuBedsCount = 8,
            emergencyPhone = "06782263300",
            receptionPhone = "06782263301",
            ambulancePhone = "9437088900",
            address = "Kuruda, Near NH-16 By-pass, Balasore",
            landmark = "NH-16 Kuruda Junction, Opposite Indian Oil Petrol Pump",
            is24x7Emergency = true,
            hasBloodStorageOrBank = true,
            hasDialysisUnit = true,
            hasNicuPicU = true,
            hasInhousePharmacy24x7 = true,
            hasInhousePathology24x7 = true,
            acceptedSchemes = listOf("BSKY Card Accepted", "Ayushman Bharat PM-JAY", "Cashless TPA (Star Health, MediAssist, ICICI)", "ESI Empanelled"),
            specialtiesAvailable = listOf("Cardiology & CCU", "Orthopedics & Joint Replacement", "General & Laparoscopic Surgery", "Nephrology & Dialysis", "Neurology", "Obstetrics & Gynecology"),
            keySurgeonsAndDoctors = listOf("Dr. B. K. Mohapatra (MS Ortho, Joint Replacement)", "Dr. S. K. Nayak (MD, DM Cardiology)", "Dr. R. P. Das (Laparoscopic Surgeon)"),
            facilities = listOf("Modular Operation Theatres (4 OTs)", "18-Bed High Dependency & Multi-Para Monitor ICU", "Continuous Hemodialysis 10-Machine Wing", "24x7 In-house Blood Storage Unit", "Advanced 32-Slice CT Scan & Ultrasound", "Fully Automated 24x7 Critical Pathology"),
            opdTimings = "09:00 AM - 01:30 PM & 04:30 PM - 08:30 PM",
            visitingHours = "11:00 AM - 12:30 PM & 05:00 PM - 07:00 PM",
            latitude = 21.4720,
            longitude = 86.9150,
            rating = 4.8f
        ),
        PrivateHospital(
            id = "hosp_2",
            name = "Life Care Hospital & Trauma Centre",
            odiaName = "ଲାଇଫ କେୟାର ହସ୍ପିଟାଲ ଓ ଟ୍ରମା ସେଣ୍ଟର",
            category = "Multi-Speciality Hospital",
            totalBeds = 85,
            icuBedsCount = 12,
            nicuBedsCount = 6,
            emergencyPhone = "06782265511",
            receptionPhone = "06782265512",
            ambulancePhone = "9437255111",
            address = "OT Road, Near Bus Stand & ITI Chhak, Balasore",
            landmark = "Behind Central Bank of India, OT Road",
            is24x7Emergency = true,
            hasBloodStorageOrBank = true,
            hasDialysisUnit = true,
            hasNicuPicU = true,
            hasInhousePharmacy24x7 = true,
            hasInhousePathology24x7 = true,
            acceptedSchemes = listOf("BSKY Card Accepted", "Ayushman Bharat", "HDFC ERGO Cashless", "Vipul Medcorp"),
            specialtiesAvailable = listOf("Accident & Polytrauma Care", "Neurosurgery", "General Medicine", "Pediatrics & Neonatology", "Urology & Kidney Stone Care"),
            keySurgeonsAndDoctors = listOf("Dr. A. C. Parida (MCh Neurosurgery)", "Dr. P. K. Panda (MS General Surgery)", "Dr. M. K. Sen (MD Pediatrics & Neonatal Fellow)"),
            facilities = listOf("24x7 Trauma Resuscitation Bay", "12-Bed Critical Care Unit (Ventilator Support)", "Holmium Laser Kidney Stone Treatment", "Level-II NICU with Phototherapy & Radiant Warmers", "Digital C-Arm Fluoroscopy Machine"),
            opdTimings = "08:30 AM - 01:00 PM & 04:00 PM - 08:00 PM",
            visitingHours = "10:30 AM - 12:00 PM & 04:30 PM - 06:30 PM",
            latitude = 21.4985,
            longitude = 86.9315,
            rating = 4.7f
        ),
        PrivateHospital(
            id = "hosp_3",
            name = "Maa Tarini Nursing Home & Maternity Care",
            odiaName = "ମାଆ ତାରିଣୀ ନର୍ସିଂହୋମ୍ ଓ ମାତୃ ସେବା କେନ୍ଦ୍ର",
            category = "Maternity & Nursing Home",
            totalBeds = 45,
            icuBedsCount = 4,
            nicuBedsCount = 6,
            emergencyPhone = "06782264420",
            receptionPhone = "06782264422",
            ambulancePhone = "9861044220",
            address = "Station Road, Near FM College Square, Balasore",
            landmark = "Opposite Radharani English Medium School",
            is24x7Emergency = true,
            hasBloodStorageOrBank = false,
            hasDialysisUnit = false,
            hasNicuPicU = true,
            hasInhousePharmacy24x7 = true,
            hasInhousePathology24x7 = true,
            acceptedSchemes = listOf("BSKY Card Accepted (Maternity & C-Section)", "Ayushman Bharat", "Private Insurance Cashless"),
            specialtiesAvailable = listOf("Gynecology & High-Risk Pregnancy", "Normal & Painless Deliveries", "Laparoscopic Hysterectomy", "Neonatology & Child Immunization", "Infertility Counselling"),
            keySurgeonsAndDoctors = listOf("Dr. (Mrs.) Sunita Jena (DGO, MS Ob-Gyn)", "Dr. S. K. Mahalik (MD Pediatrics)", "Dr. N. K. Ghosh (Anesthesiologist)"),
            facilities = listOf("Modern Labor Suite with Fetal Doppler Monitors", "Dedicated Neonatal Care with Warmers & Incubators", "Minor & Major Laparoscopic OT", "Air-conditioned Deluxe & General Postnatal Wards", "24-Hour Obstetric Emergency Team"),
            opdTimings = "09:00 AM - 02:00 PM & 05:00 PM - 09:00 PM",
            visitingHours = "11:00 AM - 01:00 PM & 05:00 PM - 07:30 PM",
            latitude = 21.4960,
            longitude = 86.9360,
            rating = 4.8f
        ),
        PrivateHospital(
            id = "hosp_4",
            name = "Seva Nursing Home & Surgical Clinic",
            odiaName = "ସେବା ନର୍ସିଂହୋମ୍ ଓ ସର୍ଜିକାଲ କ୍ଲିନିକ",
            category = "Maternity & Nursing Home",
            totalBeds = 40,
            icuBedsCount = 4,
            nicuBedsCount = 3,
            emergencyPhone = "06782262270",
            receptionPhone = "06782262271",
            ambulancePhone = "9437162270",
            address = "Azimabad, Near Police Line Square, Balasore",
            landmark = "Adjacent to Sub-Post Office Azimabad",
            is24x7Emergency = true,
            hasBloodStorageOrBank = false,
            hasDialysisUnit = false,
            hasNicuPicU = true,
            hasInhousePharmacy24x7 = true,
            hasInhousePathology24x7 = true,
            acceptedSchemes = listOf("BSKY Card", "Ayushman Bharat", "Raksha TPA Cashless"),
            specialtiesAvailable = listOf("General & GI Surgery", "Hernia, Appendix & Gallbladder Laparoscopy", "Obstetrics & Gynaecological Surgeries", "Trauma Wound Repair"),
            keySurgeonsAndDoctors = listOf("Dr. D. C. Barik (MS General Surgery)", "Dr. S. Tripathy (DGO)", "Dr. K. C. Sahoo (Consultant Physician)"),
            facilities = listOf("Twin Laminar Airflow Operation Theatres", "Post-operative Recovery Room with Pulse Oximetry", "Emergency Dressing & Stitching Unit", "Affordable Daycare Beds", "Round-the-clock Resident Doctors"),
            opdTimings = "08:00 AM - 12:30 PM & 04:30 PM - 08:30 PM",
            visitingHours = "10:00 AM - 12:00 PM & 04:00 PM - 06:00 PM",
            latitude = 21.4840,
            longitude = 86.9205,
            rating = 4.6f
        ),
        PrivateHospital(
            id = "hosp_5",
            name = "Kalinga Super Speciality Hospital & Critical Care",
            odiaName = "କଳିଙ୍ଗ ସୁପର ସ୍ପେଶାଲିଟି ହସ୍ପିଟାଲ",
            category = "Multi-Speciality Hospital",
            totalBeds = 90,
            icuBedsCount = 14,
            nicuBedsCount = 6,
            emergencyPhone = "06782266800",
            receptionPhone = "06782266801",
            ambulancePhone = "9438066800",
            address = "Januganj Golei, Remuna Road, Balasore",
            landmark = "Beside Januganj Overbridge, NH Connecting Road",
            is24x7Emergency = true,
            hasBloodStorageOrBank = true,
            hasDialysisUnit = true,
            hasNicuPicU = true,
            hasInhousePharmacy24x7 = true,
            hasInhousePathology24x7 = true,
            acceptedSchemes = listOf("BSKY Card Accepted", "Ayushman Bharat", "All Major Corporate Insurance TPAs", "Paramount Health"),
            specialtiesAvailable = listOf("Critical Care & Pulmonology", "Gastroenterology & Endoscopy", "Cardiology & Interventional Consults", "Orthopedics & Spine", "Nephrology"),
            keySurgeonsAndDoctors = listOf("Dr. M. K. Rath (MD, IDCCM Critical Care)", "Dr. S. K. Biswal (DM Gastroenterology)", "Dr. P. K. Singh (MS Orthopedics)"),
            facilities = listOf("14-Bed Advanced ICU with Invasive Ventilators", "Video Endoscopy & Colonoscopy Suite", "Hemodialysis Unit (German Fresenius Machines)", "Advanced Ultrasound & 500mA Digital X-Ray", "24-Hour Emergency Cardiac Life Support Ambulance"),
            opdTimings = "09:00 AM - 02:00 PM & 05:00 PM - 09:00 PM",
            visitingHours = "11:30 AM - 01:00 PM & 05:30 PM - 07:30 PM",
            latitude = 21.5080,
            longitude = 86.9080,
            rating = 4.7f
        ),
        PrivateHospital(
            id = "hosp_6",
            name = "Drishti Eye Hospital & Phaco Laser Surgery Centre",
            odiaName = "ଦୃଷ୍ଟି ଚକ୍ଷୁ ଚିକିତ୍ସାଳୟ ଓ ଫାକୋ ଲେଜର କେନ୍ଦ୍ର",
            category = "Eye Hospital & Surgical Centre",
            totalBeds = 30,
            icuBedsCount = 0,
            nicuBedsCount = 0,
            emergencyPhone = "06782261990",
            receptionPhone = "06782261991",
            ambulancePhone = "9437161990",
            address = "Vivekananda Marg, Near FM College Chhak, Balasore",
            landmark = "Above Bata Showroom, First & Second Floor",
            is24x7Emergency = false,
            hasBloodStorageOrBank = false,
            hasDialysisUnit = false,
            hasNicuPicU = false,
            hasInhousePharmacy24x7 = false,
            hasInhousePathology24x7 = false,
            acceptedSchemes = listOf("BSKY Card Accepted (Cataract Surgery)", "National Blindness Control Program", "Selected Private TPA Cashless"),
            specialtiesAvailable = listOf("Micro-Incision Cataract (MICS & Phaco)", "Glaucoma Screening & Management", "Diabetic Retinopathy Laser", "Cornea & Ocular Surface Disorders", "Pediatric Refraction & Squint Evaluation"),
            keySurgeonsAndDoctors = listOf("Dr. R. N. Routray (MS Ophthalmology, Senior Eye Surgeon)", "Dr. P. Pattnaik (Cornea & Refractive Specialist)"),
            facilities = listOf("Alcon Infiniti Phacoemulsification System", "Topcon Automated Perimeter for Glaucoma", "Non-mydriatic Fundus Camera", "Modern Modular Eye OT with HEPA filtration", "Daycare Eye Recovery Lounge"),
            opdTimings = "08:30 AM - 01:30 PM & 04:30 PM - 08:30 PM (Sunday Evening Closed)",
            visitingHours = "10:00 AM - 01:00 PM & 04:30 PM - 07:30 PM",
            latitude = 21.4935,
            longitude = 86.9325,
            rating = 4.8f
        ),
        PrivateHospital(
            id = "hosp_7",
            name = "Soro Multi-Speciality Hospital & Trauma Care",
            odiaName = "ସୋରୋ ମଲ୍ଟି-ସ୍ପେଶାଲିଟି ହସ୍ପିଟାଲ ଓ ଟ୍ରମା କେୟାର",
            category = "Multi-Speciality Hospital",
            totalBeds = 55,
            icuBedsCount = 8,
            nicuBedsCount = 4,
            emergencyPhone = "06788220600",
            receptionPhone = "06788220601",
            ambulancePhone = "9437299600",
            address = "NH-16 By-pass Road, Near Soro Overbridge, Soro",
            landmark = "1 km from Soro Bus Stand towards Bhadrak Highway",
            is24x7Emergency = true,
            hasBloodStorageOrBank = true,
            hasDialysisUnit = true,
            hasNicuPicU = true,
            hasInhousePharmacy24x7 = true,
            hasInhousePathology24x7 = true,
            acceptedSchemes = listOf("BSKY Card Accepted", "Ayushman Bharat", "MediAssist Cashless", "National Highway Accident Scheme"),
            specialtiesAvailable = listOf("Highway Polytrauma & Fracture Management", "Emergency Resuscitation & ICU", "General Surgery & Hernia", "Obstetric Deliveries & C-Section", "Dialysis Support"),
            keySurgeonsAndDoctors = listOf("Dr. B. K. Sahu (MS Orthopedics)", "Dr. N. R. Mohanty (MD Medicine, Intensivist)", "Dr. (Mrs.) L. Dash (Gynecologist)"),
            facilities = listOf("24x7 Emergency Highway Trauma Care", "8-Bed ICU with Ventilators & Cardiac Defibrillator", "5-Bed Hemodialysis Unit", "Automated Clinical Biochemistry Laboratory", "Full-time Highway Life Support Ambulance"),
            opdTimings = "08:30 AM - 01:00 PM & 04:30 PM - 08:30 PM",
            visitingHours = "10:30 AM - 12:30 PM & 04:30 PM - 06:30 PM",
            latitude = 21.2910,
            longitude = 86.6850,
            rating = 4.6f
        ),
        PrivateHospital(
            id = "hosp_8",
            name = "Jaleswar General & Maternity Nursing Home",
            odiaName = "ଜଳେଶ୍ୱର ଜେନେରାଲ ଓ ମ୍ୟାଟରନିଟି ନର୍ସିଂହୋମ୍",
            category = "Maternity & Nursing Home",
            totalBeds = 40,
            icuBedsCount = 4,
            nicuBedsCount = 4,
            emergencyPhone = "06781223400",
            receptionPhone = "06781223401",
            ambulancePhone = "9437423400",
            address = "Station Bazar Road, Near Puruna Bazar, Jaleswar",
            landmark = "Adjacent to Jaleswar Sub-divisional Hospital Approach",
            is24x7Emergency = true,
            hasBloodStorageOrBank = false,
            hasDialysisUnit = false,
            hasNicuPicU = true,
            hasInhousePharmacy24x7 = true,
            hasInhousePathology24x7 = true,
            acceptedSchemes = listOf("BSKY Card Accepted", "Ayushman Bharat PM-JAY", "Selected Private Insurance"),
            specialtiesAvailable = listOf("Maternity Normal Delivery & C-Section", "Pediatrics & Newborn Care", "General Surgery", "Internal Medicine & Diabetes Control"),
            keySurgeonsAndDoctors = listOf("Dr. H. K. Roy (MS Surgery)", "Dr. S. K. Pramanik (DGO)", "Dr. A. B. Dey (Child Specialist)"),
            facilities = listOf("24x7 Emergency Maternity Ward", "Newborn Resuscitation & Phototherapy Nursery", "Emergency Operation Theatre", "Digital X-Ray & Routine Pathology", "Ambulance for Critical Transfer to Balasore / Cuttack"),
            opdTimings = "08:00 AM - 01:00 PM & 05:00 PM - 09:00 PM",
            visitingHours = "11:00 AM - 01:00 PM & 05:00 PM - 07:00 PM",
            latitude = 21.8020,
            longitude = 87.2150,
            rating = 4.6f
        )
    )

    /**
     * Compute dynamic daily status of a hospital.
     */
    fun getHospitalDailyStatus(hospital: PrivateHospital): String {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)

        return when {
            hospital.is24x7Emergency && (hour >= 22 || hour < 6) ->
                "🚨 24x7 Emergency & Casualty Active • Resident Medical Officer & Trauma Bay on Duty"
            hour in 9..13 ->
                "👨‍⚕️ Morning Specialist OPD & Consultant Chambers Active • Registration Open"
            hour in 14..16 ->
                "🏥 Afternoon Inpatient Care & Elective OT Procedures in Progress"
            hour in 17..20 ->
                "🩺 Evening Consultant OPD Active • Patient Diagnostic Consultations"
            hour in 21..22 ->
                "🌙 Night Transition • 24x7 Emergency, ICU & Casualty Ready"
            else ->
                "🟢 Emergency & Inpatient Services Active"
        }
    }

    /**
     * Compute dynamic daily status of a pathology lab.
     */
    fun getPathologyLabDailyStatus(lab: PathologyLab): String {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        return when {
            lab.id == "path_5" -> "🚨 24x7 Emergency STAT Blood Lab Active Now (Casualty Trauma Ready)"
            hour in 6..10 || (hour == 10 && minute <= 30) -> "🩸 FASTING SAMPLE DRAW ACTIVE NOW (${lab.fastingCollectionHours})"
            hour in 11..16 -> "🔬 Routine Blood & Sample Processing Ongoing • Phlebotomists on Duty"
            hour in 17..20 -> "📄 Evening Report Pickup & WhatsApp Digital Dispatch Active"
            hour >= 21 || hour < 6 -> "🌙 Night Standby • Morning Fasting Draws start tomorrow at 06:30 AM"
            else -> "🟢 Sample Collection Active"
        }
    }

    /**
     * Compute phlebotomist home collection status right now.
     */
    fun isHomeCollectionActiveNow(): String {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 6..11 -> "🟢 Phlebotomist Home Sample Visits Active Now (Morning Fasting Draws in Progress)"
            in 12..16 -> "📅 Afternoon Home Collection Booking Open for Tomorrow Morning"
            in 17..20 -> "🛵 Evening Sample Pickup & Tomorrow Morning Slot Reservation Active"
            else -> "🌙 Home Phlebotomist Booking Open Online & WhatsApp for 06:30 AM Tomorrow"
        }
    }
}

