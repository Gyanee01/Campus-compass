package com.gyan.campuscompass.model

data class College(
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val state: String = "",
    val logoUrl: String = "",
    val rating: Double = 0.0,
    val description: String = ""
)

fun getMockColleges(): List<College> {
    return listOf(
        College("1", "IISc Bangalore", "Bangalore", "Karnataka", "", 4.9, "India's premier research institute."),
        College("2", "IIT Bombay", "Mumbai", "Maharashtra", "", 4.8, "Top engineering college in Powai."),
        College("3", "SRM Institute", "Chennai", "Tamil Nadu", "", 4.2, "Multi-disciplinary university."),
        College("4", "Manipal Institute", "Manipal", "Karnataka", "", 4.5, "Known for engineering and medical."),
        College("5", "BITS Pilani", "Pilani", "Rajasthan", "", 4.7, "Leading private engineering university.")
    )
}
