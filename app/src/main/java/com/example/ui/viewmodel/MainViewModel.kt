package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AnkitAiEngine
import com.example.data.local.AppDatabase
import com.example.data.local.LibraryMaterialEntity
import com.example.data.local.LibraryRepository
import com.example.data.models.*
import com.example.data.repository.StudyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

sealed class ScreenDestination {
    object Home : ScreenDestination()
    object Learn : ScreenDestination()
    object AnkitAi : ScreenDestination()
    object Practice : ScreenDestination()
    object Profile : ScreenDestination()
    object RevisionRadar : ScreenDestination()
    object CameraSolver : ScreenDestination()
    object VoiceTeacher : ScreenDestination()
    object MindMap : ScreenDestination()
    object Flashcards : ScreenDestination()
    object FocusMode : ScreenDestination()
    object AdminPanel : ScreenDestination()
    object TestSeries : ScreenDestination()
    object StudyPlan : ScreenDestination()
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository: StudyRepository = StudyRepository()
    val libraryRepository: LibraryRepository = LibraryRepository(
        AppDatabase.getInstance(application).libraryMaterialDao()
    )

    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Home)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    // --- Government Exam Test Series State ---
    val couponInput = MutableStateFlow("VIAN12")
    private val _isCouponVian12Applied = MutableStateFlow(true) // Auto-applied as bonus for first 500 students!
    val isCouponVian12Applied: StateFlow<Boolean> = _isCouponVian12Applied.asStateFlow()

    private val _couponValidationMessage = MutableStateFlow<String?>("Token 'VIAN12' active: ₹50 Instant Discount applied!")
    val couponValidationMessage: StateFlow<String?> = _couponValidationMessage.asStateFlow()

    private val _discountSlotsClaimed = MutableStateFlow(368) // 368 out of 500 claimed
    val discountSlotsClaimed: StateFlow<Int> = _discountSlotsClaimed.asStateFlow()

    private val _enrolledPackageIds = MutableStateFlow<Set<String>>(emptySet())
    val enrolledPackageIds: StateFlow<Set<String>> = _enrolledPackageIds.asStateFlow()

    private val _testSeriesPackages = MutableStateFlow(
        listOf(
            TestSeriesPackage(
                id = "pkg_all_govt",
                title = "All Government Exams Ultimate Test Pass",
                category = "All Govt Exams",
                originalPrice = 99,
                discountedPrice = 49,
                totalTests = 280,
                fullLengthMocks = 90,
                chapterTests = 140,
                previousYearPapers = 50,
                examCoverage = "SSC CGL/CHSL, Railway RRB NTPC/Group D, Banking SBI/IBPS, UPSC/State PSC, Police & Defence",
                features = listOf(
                    "All-India Live Rank & Percentile Prediction",
                    "Detailed Step-by-Step AI Solution Analysis",
                    "Bilingual in Hindi & English (Exact Exam UI)",
                    "Sectional Speed & Accuracy Breakdown"
                ),
                enrolledStudents = 368,
                maxDiscountStudents = 500
            ),
            TestSeriesPackage(
                id = "pkg_ssc",
                title = "SSC Complete Speed & Accuracy Test Series",
                category = "SSC",
                originalPrice = 99,
                discountedPrice = 49,
                totalTests = 120,
                fullLengthMocks = 40,
                chapterTests = 55,
                previousYearPapers = 25,
                examCoverage = "SSC CGL Tier 1 & 2, CHSL, MTS, CPO, GD Constable",
                features = listOf(
                    "TCS Pattern Latest Interface Simulation",
                    "Negative Marking (-0.50) Realistic Grading",
                    "General Awareness Current Affairs Special Mock"
                ),
                enrolledStudents = 210,
                maxDiscountStudents = 500
            ),
            TestSeriesPackage(
                id = "pkg_railway",
                title = "Railway RRB High-Speed Mock Series",
                category = "Railway",
                originalPrice = 99,
                discountedPrice = 49,
                totalTests = 95,
                fullLengthMocks = 35,
                chapterTests = 40,
                previousYearPapers = 20,
                examCoverage = "RRB NTPC CBT 1 & 2, Group D, RRB ALP & Technician, JE",
                features = listOf(
                    "General Science & Reasoning Speed Sprints",
                    "Previous 5 Years Question Repetition Analysis",
                    "Speed-boosting 90-minute timed drills"
                ),
                enrolledStudents = 185,
                maxDiscountStudents = 500
            ),
            TestSeriesPackage(
                id = "pkg_banking",
                title = "Banking & Insurance Prelims + Mains Master Pass",
                category = "Banking",
                originalPrice = 99,
                discountedPrice = 49,
                totalTests = 110,
                fullLengthMocks = 45,
                chapterTests = 45,
                previousYearPapers = 20,
                examCoverage = "SBI PO / Clerk, IBPS PO / Clerk, RRB PO / Clerk, RBI Assistant",
                features = listOf(
                    "Sectional Timing (20 mins per section) Exact Simulation",
                    "High-Level Puzzles & Data Interpretation Focus",
                    "Financial & Banking Awareness Special Booster"
                ),
                enrolledStudents = 145,
                maxDiscountStudents = 500
            ),
            TestSeriesPackage(
                id = "pkg_upsc_psc",
                title = "UPSC CSE & State PSC Prelims GS + CSAT Mock Test",
                category = "UPSC & State PSC",
                originalPrice = 99,
                discountedPrice = 49,
                totalTests = 85,
                fullLengthMocks = 30,
                chapterTests = 35,
                previousYearPapers = 20,
                examCoverage = "UPSC Civil Services, BPSC, UPPSC, MPPSC, RAS, WBPSC, MPSC",
                features = listOf(
                    "Multi-Statement Analytical Questions",
                    "Comprehensive Explanations with NCERT & Standard References",
                    "CSAT Aptitude & Comprehension Accuracy Drills"
                ),
                enrolledStudents = 95,
                maxDiscountStudents = 500
            ),
            TestSeriesPackage(
                id = "pkg_defence",
                title = "Defence & Police Forces Combat Test Series",
                category = "Defence",
                originalPrice = 99,
                discountedPrice = 49,
                totalTests = 75,
                fullLengthMocks = 25,
                chapterTests = 35,
                previousYearPapers = 15,
                examCoverage = "NDA, CDS, AFCAT, CAPF, Delhi Police, UP Police SI / Constable",
                features = listOf(
                    "Mathematics & English Precision Modules",
                    "Physical & Written Exam Dual Guidance Notes",
                    "National Level Merit List Ranking"
                ),
                enrolledStudents = 82,
                maxDiscountStudents = 500
            )
        )
    )
    val testSeriesPackages: StateFlow<List<TestSeriesPackage>> = _testSeriesPackages.asStateFlow()

    private val _mockTests = MutableStateFlow(
        listOf(
            MockTestItem(
                id = "mock_all_1",
                packageId = "pkg_all_govt",
                title = "All Govt Exam All-India Live Mock Test #1",
                examCategory = "All Govt Combined",
                totalQuestions = 100,
                totalMarks = 200,
                durationMinutes = 60,
                totalAttempts = 4280,
                highestScore = 188,
                averageScore = 124.5f
            ),
            MockTestItem(
                id = "mock_ssc_1",
                packageId = "pkg_ssc",
                title = "SSC CGL 2026 Tier-1 Full Length Mock #3",
                examCategory = "SSC CGL",
                totalQuestions = 100,
                totalMarks = 200,
                durationMinutes = 60,
                totalAttempts = 3120,
                highestScore = 178,
                averageScore = 118.2f
            ),
            MockTestItem(
                id = "mock_rrb_1",
                packageId = "pkg_railway",
                title = "Railway RRB NTPC CBT-1 Grand Practice Test",
                examCategory = "RRB NTPC",
                totalQuestions = 100,
                totalMarks = 100,
                durationMinutes = 90,
                totalAttempts = 2890,
                highestScore = 92,
                averageScore = 67.4f
            ),
            MockTestItem(
                id = "mock_bank_1",
                packageId = "pkg_banking",
                title = "SBI / IBPS PO Prelims Speed Championship",
                examCategory = "Banking PO",
                totalQuestions = 100,
                totalMarks = 100,
                durationMinutes = 60,
                totalAttempts = 2410,
                highestScore = 86,
                averageScore = 58.7f
            )
        )
    )
    val mockTests: StateFlow<List<MockTestItem>> = _mockTests.asStateFlow()

    fun applyCouponCode(code: String) {
        val trimmed = code.trim()
        if (trimmed.equals("VIAN12", ignoreCase = true)) {
            _isCouponVian12Applied.value = true
            _couponValidationMessage.value = "🎉 Token 'VIAN12' Applied! Price slashed from ₹99 to ₹49 (Available for first 500 students)."
            repository.awardXp(20)
        } else {
            _isCouponVian12Applied.value = false
            _couponValidationMessage.value = "❌ Invalid token. Enter 'VIAN12' to unlock ₹49 special price for first 500 students!"
        }
    }

    fun removeCouponCode() {
        _isCouponVian12Applied.value = false
        _couponValidationMessage.value = null
    }

    fun enrollInTestSeries(packageId: String) {
        viewModelScope.launch {
            _enrolledPackageIds.value = _enrolledPackageIds.value + packageId
            if (_discountSlotsClaimed.value < 500) {
                _discountSlotsClaimed.value = _discountSlotsClaimed.value + 1
            }
            repository.awardXp(100)
        }
    }

    fun recordMockTestAttempt(testId: String, score: Int, percentile: Float) {
        _mockTests.value = _mockTests.value.map { item ->
            if (item.id == testId) {
                item.copy(isAttempted = true, studentScore = score, studentPercentile = percentile)
            } else item
        }
        repository.awardXp(50)
    }

    // --- Gemini AI Custom Study Schedule Generator State ---
    private val _isAiGeneratingPlan = MutableStateFlow(false)
    val isAiGeneratingPlan: StateFlow<Boolean> = _isAiGeneratingPlan.asStateFlow()

    private val _aiPlanPreview = MutableStateFlow<List<StudyPlanTask>?>(null)
    val aiPlanPreview: StateFlow<List<StudyPlanTask>?> = _aiPlanPreview.asStateFlow()

    fun generateCustomAiSchedule(
        examGoal: String,
        hours: Int,
        prepLevel: String,
        subjects: List<String>,
        includeRevision: Boolean,
        includeMockSprint: Boolean
    ) {
        _isAiGeneratingPlan.value = true
        _aiPlanPreview.value = null
        viewModelScope.launch {
            delay(600)
            val schedule = AnkitAiEngine.generateCustomStudySchedule(
                examGoal = examGoal,
                availableHours = hours,
                prepLevel = prepLevel,
                focusSubjects = subjects,
                includeRevision = includeRevision,
                includeMockSprint = includeMockSprint
            )
            _aiPlanPreview.value = schedule
            _isAiGeneratingPlan.value = false
            repository.awardXp(40)
        }
    }

    fun applyAiScheduleToToday() {
        val plan = _aiPlanPreview.value ?: return
        repository.applyGeneratedSchedule(plan)
        _aiPlanPreview.value = null
        repository.awardXp(50)
    }

    fun dismissAiPlanPreview() {
        _aiPlanPreview.value = null
    }

    val dailyGoals: StateFlow<List<DailyGoal>> = repository.dailyGoals
    val subjectProgressTrackers: StateFlow<List<SubjectProgressTracker>> = repository.subjectProgressTrackers
    val studyPlanTasks: StateFlow<List<StudyPlanTask>> = repository.studyPlanTasks

    fun toggleDailyGoal(id: String) {
        repository.toggleDailyGoal(id)
    }

    fun incrementDailyGoal(id: String, amount: Int) {
        repository.incrementDailyGoal(id, amount)
    }

    fun updateSubjectProgress(subjectId: String, increment: Int) {
        repository.updateSubjectProgress(subjectId, increment)
    }

    fun toggleStudyPlanTask(taskId: String) {
        repository.toggleTaskCompletion(taskId)
    }

    fun rearrangeStudySchedule() {
        repository.rearrangeMissedSchedule()
    }

    // --- Library Search & Filter State ---
    val librarySearchQuery = MutableStateFlow("")
    val libraryTypeFilter = MutableStateFlow("All") // "All", "PYQ", "Study Material", "Notes", "Curated Article", "Formulas"
    val librarySubjectFilter = MutableStateFlow("All") // "All", "Physics", "Chemistry", "Mathematics", "Polity & GS", "Biology", "Reasoning"
    val libraryExamFilter = MutableStateFlow("All") // "All", "JEE", "NEET", "UPSC", "SSC CGL", "Banking"
    val librarySortBy = MutableStateFlow("Latest") // "Latest", "Top Rated", "Most Read", "Fast Read"
    val libraryOnlyBookmarked = MutableStateFlow(false)

    val libraryMaterials: StateFlow<List<LibraryMaterialEntity>> = combine(
        listOf(
            librarySearchQuery,
            libraryTypeFilter,
            librarySubjectFilter,
            libraryExamFilter,
            libraryOnlyBookmarked,
            librarySortBy
        )
    ) { array ->
        FilterParams(
            query = array[0] as String,
            type = array[1] as String,
            subject = array[2] as String,
            exam = array[3] as String,
            onlyBookmarked = array[4] as Boolean,
            sortBy = array[5] as String
        )
    }.flatMapLatest { params ->
        libraryRepository.searchAndFilter(
            query = params.query,
            typeFilter = params.type,
            subjectFilter = params.subject,
            examFilter = params.exam,
            onlyBookmarked = params.onlyBookmarked,
            sortBy = params.sortBy
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedMaterialForReading = MutableStateFlow<LibraryMaterialEntity?>(null)
    val selectedMaterialForReading: StateFlow<LibraryMaterialEntity?> = _selectedMaterialForReading.asStateFlow()

    // --- AI Chat State ---
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                isUser = false,
                text = "Namaste Aman! Main hoon ANKIT AI — aapka 24/7 personal AI teacher & exam coach (Managed by Ankit Rajak).\n\nAap text type kar sakte hain, voice se bol sakte hain, ya photo upload kar sakte hain. Kis subject me help chahiye?",
                quickActions = listOf("Explain Simply", "Step-by-Step", "Another Method", "Give Example", "Test Me")
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _activeMindMap = MutableStateFlow(AnkitAiEngine.generateMindMap("Laws of Motion"))
    val activeMindMap: StateFlow<MindMapNode> = _activeMindMap.asStateFlow()

    private val _activeFlashcardIndex = MutableStateFlow(0)
    val activeFlashcardIndex: StateFlow<Int> = _activeFlashcardIndex.asStateFlow()

    private val _focusSecondsRemaining = MutableStateFlow(25 * 60)
    val focusSecondsRemaining: StateFlow<Int> = _focusSecondsRemaining.asStateFlow()

    private val _isFocusRunning = MutableStateFlow(false)
    val isFocusRunning: StateFlow<Boolean> = _isFocusRunning.asStateFlow()

    private val _cameraProblemSample = MutableStateFlow(
        "A particle of mass m moves under potential V(x) = k*x² / 2. Find ground state energy using Bohr-Sommerfeld quantization rule."
    )
    val cameraProblemSample: StateFlow<String> = _cameraProblemSample.asStateFlow()

    private val _cameraSolutionResult = MutableStateFlow<String?>(null)
    val cameraSolutionResult: StateFlow<String?> = _cameraSolutionResult.asStateFlow()

    fun navigateTo(destination: ScreenDestination) {
        _currentScreen.value = destination
    }

    // --- Library Actions ---
    fun setLibrarySearchQuery(query: String) {
        librarySearchQuery.value = query
    }

    fun setLibraryTypeFilter(type: String) {
        libraryTypeFilter.value = type
    }

    fun setLibrarySubjectFilter(subject: String) {
        librarySubjectFilter.value = subject
    }

    fun toggleLibraryOnlyBookmarked() {
        libraryOnlyBookmarked.value = !libraryOnlyBookmarked.value
    }

    fun openMaterialReader(material: LibraryMaterialEntity) {
        _selectedMaterialForReading.value = material
    }

    fun closeMaterialReader() {
        _selectedMaterialForReading.value = null
    }

    fun toggleBookmark(material: LibraryMaterialEntity) {
        viewModelScope.launch {
            libraryRepository.toggleBookmark(material.id, material.isBookmarked)
            // If currently open in reader, update reading state as well
            if (_selectedMaterialForReading.value?.id == material.id) {
                _selectedMaterialForReading.value = material.copy(isBookmarked = !material.isBookmarked)
            }
        }
    }

    fun addCustomStudyMaterial(
        title: String,
        description: String,
        content: String,
        type: String,
        subject: String,
        targetExam: String,
        imageUrl: String
    ) {
        viewModelScope.launch {
            val newEntity = LibraryMaterialEntity(
                id = "custom_" + UUID.randomUUID().toString().take(8),
                title = title.trim(),
                description = description.trim(),
                content = content.trim(),
                type = type,
                subject = subject,
                targetExam = targetExam,
                author = "My Personal Note",
                imageUrl = imageUrl.ifBlank { "https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=1600&auto=format&fit=crop&q=90" },
                tags = "$subject, Custom Notes",
                readTimeMinutes = maxOf(3, content.split("\\s+".toRegex()).size / 150),
                isBookmarked = true
            )
            libraryRepository.insertMaterial(newEntity)
            repository.awardXp(50)
        }
    }

    fun deleteStudyMaterial(id: String) {
        viewModelScope.launch {
            libraryRepository.deleteMaterial(id)
            if (_selectedMaterialForReading.value?.id == id) {
                _selectedMaterialForReading.value = null
            }
        }
    }

    // --- Chat & Tutoring ---
    fun sendChatQuery(query: String, mode: String? = null, language: String = "Hinglish") {
        if (query.isBlank()) return

        val userMsg = ChatMessage(isUser = true, text = query)
        _chatMessages.value = _chatMessages.value + userMsg

        _isAiThinking.value = true
        viewModelScope.launch {
            val response = AnkitAiEngine.getTutorResponse(
                query = query,
                mode = mode,
                language = language
            )
            _chatMessages.value = _chatMessages.value + ChatMessage(
                isUser = false,
                text = response,
                quickActions = listOf("Explain Simply", "Step-by-Step", "Another Method", "Give Example", "Test Me")
            )
            repository.awardXp(20)
            _isAiThinking.value = false
        }
    }

    fun sendQuickAction(action: String, language: String = "Hinglish") {
        val lastUserMessage = _chatMessages.value.findLast { it.isUser }?.text ?: "Laws of Motion & Friction"
        val mode = when (action) {
            "Explain Simply" -> "simple"
            "Step-by-Step" -> "step_by_step"
            "Another Method" -> "another_method"
            "Give Example" -> "example"
            "Test Me" -> "test_me"
            else -> null
        }
        sendChatQuery(query = lastUserMessage, mode = mode, language = language)
    }

    fun solveCameraProblem(problemText: String) {
        _cameraProblemSample.value = problemText
        _isAiThinking.value = true
        _cameraSolutionResult.value = null
        viewModelScope.launch {
            delay(800)
            val solution = AnkitAiEngine.getTutorResponse(
                query = "Solve this step-by-step with formulas and alternate trick: $problemText",
                mode = "step_by_step",
                language = repository.userProfile.value.preferredLanguage,
                photoDescription = "Question Image Scanned via ANKIT AI Camera"
            )
            _cameraSolutionResult.value = solution
            _isAiThinking.value = false
            repository.awardXp(35)
        }
    }

    fun loadMindMap(chapter: String) {
        _activeMindMap.value = AnkitAiEngine.generateMindMap(chapter)
    }

    fun nextFlashcard(known: Boolean) {
        val currentCards = repository.flashcards.value
        val currentIndex = _activeFlashcardIndex.value
        repository.awardXp(if (known) 15 else 5)
        if (currentIndex < currentCards.size - 1) {
            _activeFlashcardIndex.value = currentIndex + 1
        } else {
            _activeFlashcardIndex.value = 0
        }
    }

    fun toggleFocusTimer() {
        val next = !_isFocusRunning.value
        _isFocusRunning.value = next
        if (next) {
            startTimerCoroutine()
        }
    }

    private fun startTimerCoroutine() {
        viewModelScope.launch {
            while (_isFocusRunning.value && _focusSecondsRemaining.value > 0) {
                delay(1000)
                _focusSecondsRemaining.value = _focusSecondsRemaining.value - 1
            }
            if (_focusSecondsRemaining.value == 0) {
                _isFocusRunning.value = false
                repository.awardXp(100)
            }
        }
    }

    fun resetFocusTimer() {
        _isFocusRunning.value = false
        _focusSecondsRemaining.value = 25 * 60
    }

    fun setLibraryExamFilter(exam: String) {
        libraryExamFilter.value = exam
    }

    fun setLibrarySortBy(sort: String) {
        librarySortBy.value = sort
    }

    fun clearAllLibraryFilters() {
        librarySearchQuery.value = ""
        libraryTypeFilter.value = "All"
        librarySubjectFilter.value = "All"
        libraryExamFilter.value = "All"
        librarySortBy.value = "Latest"
        libraryOnlyBookmarked.value = false
    }

    private data class FilterParams(
        val query: String,
        val type: String,
        val subject: String,
        val exam: String,
        val onlyBookmarked: Boolean,
        val sortBy: String
    )
}
