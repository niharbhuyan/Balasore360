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

