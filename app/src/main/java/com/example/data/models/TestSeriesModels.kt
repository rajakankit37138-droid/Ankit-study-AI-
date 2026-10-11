package com.example.data.models

data class TestSeriesPackage(
    val id: String,
    val title: String,
    val category: String, // "All Govt Exams", "SSC", "Railway", "Banking", "UPSC & State PSC", "Defence", "Police & Teaching"
    val originalPrice: Int = 99,
    val discountedPrice: Int = 49,
    val totalTests: Int,
    val fullLengthMocks: Int,
    val chapterTests: Int,
    val previousYearPapers: Int,
    val examCoverage: String,
    val features: List<String>,
    val enrolledStudents: Int = 368,
    val maxDiscountStudents: Int = 500,
    val rating: Float = 4.95f
)

data class MockTestItem(
    val id: String,
    val packageId: String,
    val title: String,
    val examCategory: String,
    val totalQuestions: Int,
    val totalMarks: Int,
    val durationMinutes: Int,
    val totalAttempts: Int,
    val highestScore: Int,
    val averageScore: Float,
    val isAttempted: Boolean = false,
    val studentScore: Int? = null,
    val studentPercentile: Float? = null
)
