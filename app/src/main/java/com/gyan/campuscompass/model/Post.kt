package com.gyan.campuscompass.model

data class Post(
    val author: User,
    val category: PostCategory,
    val destinationCity: String,
    val transportMode: String,
    val accommodationDetails: String,
    val travelTips: String,
    val collegeAllowance: Double?,
    val likesCount: Int,
    val dislikesCount: Int,
    val commentsCount: Int
)

fun getMockPosts(): List<Post> {
    return listOf(
        Post(
            author = User("", "Rahul", "3rd Year, IT"),
            category = PostCategory.EDUCATIONAL,
            destinationCity = "Bangalore",
            transportMode = "Train - Sleeper",
            accommodationDetails = "Provided by organizers",
            travelTips = "Carry an extension board!",
            collegeAllowance = 1500.0,
            likesCount = 45,
            dislikesCount = 2,
            commentsCount = 12
        ),
        Post(
            author = User("", "Priya", "4th Year, CS"),
            category = PostCategory.NON_EDUCATIONAL,
            destinationCity = "Manali",
            transportMode = "Volvo Bus",
            accommodationDetails = "Zostel",
            travelTips = "Book bus tickets 2 weeks early for discounts",
            collegeAllowance = null,
            likesCount = 89,
            dislikesCount = 5,
            commentsCount = 24
        )
    )
}
