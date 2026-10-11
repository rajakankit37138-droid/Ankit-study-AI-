package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_materials")
data class LibraryMaterialEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val content: String,
    val type: String, // "Study Material", "Notes", "Curated Article", "Formulas", "PYQ"
    val subject: String, // "Physics", "Chemistry", "Mathematics", "Biology", "Polity & GS", "Reasoning"
    val targetExam: String, // "JEE Main & Advanced", "NEET", "UPSC", "SSC CGL", "All Exams"
    val author: String = "Ankit Rajak (ANKIT AI)",
    val imageUrl: String,
    val tags: String, // Comma-separated: "Mechanics, Newton, Laws"
    val readTimeMinutes: Int = 10,
    val isBookmarked: Boolean = false,
    val downloadsCount: Int = 1200,
    val rating: Float = 4.9f,
    val timestamp: Long = System.currentTimeMillis()
)
