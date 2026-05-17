package com.gyan.campuscompass.model

enum class TravelCompanionType { SOLO, FRIENDS, COUPLE, FAMILY }
enum class ComfortLevel { BUDGET, MODERATE, PREMIUM }
enum class CrowdLevel { PEACEFUL, MODERATE, CROWDED }

data class PostImage(
    val url: String = "",
    val caption: String = ""
)

data class Post(
    val id: String = "",
    val author: User = User(),
    val category: PostCategory = PostCategory.EDUCATIONAL,
    val destinationCity: String = "",
    val destinationState: String = "",
    val venueName: String = "",
    val transportMode: String = "",
    val transportCost: Double = 0.0,
    val accommodationName: String = "",
    val accommodationCost: Double = 0.0,
    val foodCost: Double = 0.0,
    val travelTips: String = "",
    val travelBlog: String = "",
    val images: List<PostImage> = emptyList(),
    val collegeAllowance: Double? = null,
    val likesCount: Int = 0,
    val likedBy: List<String> = emptyList(),
    val dislikesCount: Int = 0,
    val commentsCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    
    // New Fields (Requirement 29)
    val numberOfDays: Int = 1,
    val bestTimeToVisit: String = "",
    val travelCompanion: TravelCompanionType = TravelCompanionType.FRIENDS,
    val comfortLevel: ComfortLevel = ComfortLevel.MODERATE,
    val safetyRating: Int = 5,
    val internetAvailability: Int = 5,
    val foodQualityRating: Int = 5,
    val crowdLevel: CrowdLevel = CrowdLevel.MODERATE,
    
    // Tags (Requirement 30)
    val tags: List<String> = emptyList(),
    
    // Cover Artwork (Requirement 33)
    val coverArtworkIndex: Int = 0,

    // SOS Information (Requirement 82)
    val emergencyContactName: String = "",
    val emergencyContactNumber: String = "",
    val nearbyHospital: String = "",
    val nearbyPoliceStation: String = "",
    val emergencyNotes: String = "",
    val safetyInstructions: String = "",
    val emergencyTransport: String = "",
    val safeAccommodationNotes: String = ""
) {
    val totalBudget: Double
        get() = transportCost + accommodationCost + foodCost
}

val postTags = listOf(
    "Educational", "Adventure", "College Trip", "Budget Friendly", 
    "Hidden Gem", "Nature", "Religious", "Historical", "Nightlife", "Food Spot"
)

fun getMockPosts(): List<Post> {
    return listOf(
        Post(
            id = "1",
            author = User("u1", "campus_rider", studentYear = "3rd Year", isDemo = true),
            category = PostCategory.EDUCATIONAL,
            destinationCity = "Bangalore",
            destinationState = "Karnataka",
            venueName = "IISc Bangalore",
            transportMode = "Train",
            transportCost = 800.0,
            accommodationName = "Hostel",
            accommodationCost = 500.0,
            foodCost = 400.0,
            travelTips = "Carry an extension board!",
            collegeAllowance = 1500.0,
            likesCount = 45,
            commentsCount = 12,
            tags = listOf("Educational", "College Trip", "Budget Friendly"),
            coverArtworkIndex = 1,
            // SOS Data
            emergencyContactName = "IISc Security",
            emergencyContactNumber = "080-22932400",
            nearbyHospital = "Columbia Asia Hospital",
            nearbyPoliceStation = "Sadashivnagar Police Station",
            emergencyNotes = "Always keep your ID card handy for campus entry.",
            safetyInstructions = "Avoid late night solo walks outside the main gate.",
            emergencyTransport = "Campus Shuttle available 24/7",
            safeAccommodationNotes = "Official guest house is the safest option."
        ),
        Post(
            id = "2",
            author = User("u2", "travel_senpai", studentYear = "4th Year", isDemo = true),
            category = PostCategory.NON_EDUCATIONAL,
            destinationCity = "Manali",
            destinationState = "Himachal Pradesh",
            venueName = "Mall Road",
            transportMode = "Bus",
            transportCost = 1200.0,
            accommodationName = "Zostel",
            accommodationCost = 2500.0,
            foodCost = 1500.0,
            travelTips = "Book bus tickets 2 weeks early for discounts",
            collegeAllowance = null,
            likesCount = 89,
            commentsCount = 24,
            tags = listOf("Adventure", "Nature", "Hidden Gem"),
            coverArtworkIndex = 2,
            // SOS Data
            emergencyContactName = "Adventure Hub Rescue",
            emergencyContactNumber = "98160-12345",
            nearbyHospital = "Mission Hospital Manali",
            nearbyPoliceStation = "Manali Police HQ",
            emergencyNotes = "Altitude sickness is common; keep Diamox.",
            safetyInstructions = "Don't trek after sunset.",
            emergencyTransport = "Local 4x4 Taxis",
            safeAccommodationNotes = "Verified hostels are better for solo travelers."
        ),
        Post(
            id = "3",
            author = User("u3", "budget_nomad", studentYear = "2nd Year", isDemo = true),
            category = PostCategory.NON_EDUCATIONAL,
            destinationCity = "Goa",
            destinationState = "Goa",
            venueName = "Anjuna Beach",
            transportCost = 1500.0,
            accommodationCost = 3000.0,
            foodCost = 2000.0,
            likesCount = 156,
            commentsCount = 42,
            tags = listOf("Nightlife", "Food Spot", "Budget Friendly"),
            coverArtworkIndex = 4,
            // SOS Data
            emergencyContactName = "Beach Patrol",
            emergencyContactNumber = "100",
            nearbyHospital = "Manipal Hospital Goa",
            nearbyPoliceStation = "Anjuna Police Station",
            emergencyNotes = "Stay hydrated and watch out for strong currents.",
            safetyInstructions = "Stick to well-lit areas at night.",
            emergencyTransport = "Rent a scooty for quick movement.",
            safeAccommodationNotes = "Avoid beach shacks for overnight stay if alone."
        ),
        Post(
            id = "4",
            author = User("u4", "college_wanderer", studentYear = "1st Year", isDemo = true),
            category = PostCategory.NON_EDUCATIONAL,
            destinationCity = "Hampi",
            destinationState = "Karnataka",
            venueName = "Virupaksha Temple",
            transportCost = 600.0,
            accommodationCost = 1200.0,
            foodCost = 800.0,
            likesCount = 210,
            commentsCount = 31,
            tags = listOf("Historical", "Nature", "Religious"),
            coverArtworkIndex = 5
        )
    )
}
