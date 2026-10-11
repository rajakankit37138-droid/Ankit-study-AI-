package com.example.data.models

data class UserProfile(
    val name: String = "Aman Kumar",
    val studentClass: String = "Class 12 / Dropper",
    val stream: String = "Science (PCM)",
    val targetExam: String = "JEE Main & Advanced",
    val examDate: String = "May 15, 2027",
    val dailyStudyGoalMinutes: Int = 360, // 6 hours
    val studyStreakDays: Int = 7,
    val totalXp: Int = 4250,
    val level: Int = 8,
    val levelTitle: String = "Quantum Scholar",
    val studyCoins: Int = 840,
    val preferredLanguage: String = "Hinglish", // English, Hindi, Hinglish
    val explanationStyle: String = "Step-by-Step with Intuition",
    val badges: List<String> = listOf("🔥 7-Day Titan", "⚡ Speed Solver", "🎯 Radar Master", "🌌 Concept Prodigy"),
    val email: String = "student@ankitstudy.ai",
    val phone: String = "+91 98765 43210"
)

enum class MistakeCategory(val label: String, val icon: String, val description: String) {
    CONCEPT("Concept Mistake", "🧠", "Misunderstood fundamental rule or theory"),
    CALCULATION("Calculation Mistake", "🔢", "Arithmetic or algebraic sign slip"),
    MEMORY("Memory Mistake", "💾", "Forgot specific constant, formula, or year"),
    SILLY("Silly Mistake", "⚡", "Misread question stem or option wording"),
    TIME_MANAGEMENT("Time-Management", "⏱️", "Rushed under clock pressure")
}

data class MistakeItem(
    val id: String,
    val exam: String,
    val subject: String,
    val chapter: String,
    val questionText: String,
    val studentAnswer: String,
    val correctAnswer: String,
    val aiExplanation: String,
    val category: MistakeCategory,
    val timestamp: Long = System.currentTimeMillis(),
    val timesRepeated: Int = 1,
    val isResolved: Boolean = false
)

enum class UrgencyLevel(val label: String, val colorHex: Long) {
    HIGH("🔴 High Urgency", 0xFFFF3366),
    MEDIUM("🟠 Medium Urgency", 0xFFFB8500),
    LOW("🟡 Preventive", 0xFFFFB703)
}

data class RevisionRadarItem(
    val id: String,
    val subject: String,
    val chapter: String,
    val urgency: UrgencyLevel,
    val retentionScorePercent: Int, // e.g. 42%
    val daysSinceMistake: Int,
    val reason: String,
    val recommendedStudyMinutes: Int,
    val quickNotesSummary: String,
    val practiceQuestions: List<PracticeQuestion>
)

data class PracticeQuestion(
    val id: String,
    val subject: String,
    val chapter: String,
    val difficulty: String, // Easy, Medium, Hard, Expert
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val stepByStep: String,
    val anotherMethod: String,
    val realLifeExample: String
)

data class ExamReadinessScore(
    val examName: String,
    val overallPercent: Int, // e.g. 74%
    val syllabusCompletion: Int, // e.g. 68%
    val testAccuracy: Int, // e.g. 78%
    val questionSpeedQpm: Float, // e.g. 1.8 q/min
    val revisionConsistency: Int, // e.g. 85%
    val weakTopicImprovement: Int, // e.g. 62%
    val nextBestAction: String
)

data class StudyPlanTask(
    val id: String,
    val timeSlot: String,
    val subject: String,
    val chapter: String,
    val goalDescription: String,
    val durationMinutes: Int,
    val isCompleted: Boolean = false,
    val isRescheduled: Boolean = false
)

data class DailyGoal(
    val id: String,
    val title: String,
    val targetValue: Int,
    val currentValue: Int,
    val unit: String,
    val isCompleted: Boolean = false,
    val category: String = "general", // "focus", "questions", "revision", "flashcards", "sprint"
    val xpReward: Int = 50
)

data class SubjectProgressTracker(
    val id: String,
    val subjectName: String,
    val totalChapters: Int,
    val completedChapters: Int,
    val syllabusPercent: Int,
    val accuracyPercent: Int,
    val weeklyHoursSpent: Float,
    val weakTopic: String,
    val strongTopic: String,
    val colorHex: Long = 0xFF00E5FF
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val photoDescription: String? = null,
    val audioQuery: Boolean = false,
    val quickActions: List<String> = emptyList()
)

data class LibraryItem(
    val id: String,
    val title: String,
    val targetClass: String, // Class 11-12, JEE, NEET, UPSC etc.
    val subject: String,
    val chapter: String,
    val category: String, // Short Notes, Formulas, Definitions, PYQ, Solved Papers, MCQs, Revision Sheets
    val difficulty: String,
    val readTimeMinutes: Int,
    val contentMarkdown: String,
    val downloadsCount: Int = 1240,
    val isBookmarked: Boolean = false
)

data class MindMapNode(
    val id: String,
    val title: String,
    val subtitle: String,
    val nodeType: String, // "Chapter", "Concept", "Formula", "Example", "HighYield"
    val children: List<MindMapNode> = emptyList()
)

data class FlashcardItem(
    val id: String,
    val subject: String,
    val chapter: String,
    val frontPrompt: String,
    val backExplanation: String,
    val keyFormula: String? = null,
    val intervalDays: Int = 1,
    val isKnown: Boolean? = null
)

data class CurrentAffairsItem(
    val id: String,
    val title: String,
    val category: String, // Government Schemes, Science & Tech, Sports, National, International, Economy, Appointments
    val dateText: String,
    val summary: String,
    val keyPoints: List<String>,
    val quizQuestion: PracticeQuestion
)

data class LearningMemory(
    val frequentlyWrongTopics: List<String>,
    val strongSubjects: List<String>,
    val weakChapters: List<String>,
    val skippedTopics: List<String>,
    val overallAccuracy: Int,
    val learningSpeed: String,
    val revisionHistoryDays: Int,
    val preferredExplanationStyle: String
)
