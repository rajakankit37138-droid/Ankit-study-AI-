package com.example.data.repository

import com.example.data.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StudyRepository {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _examReadiness = MutableStateFlow(
        ExamReadinessScore(
            examName = "JEE Main & Advanced 2027",
            overallPercent = 74,
            syllabusCompletion = 68,
            testAccuracy = 78,
            questionSpeedQpm = 1.8f,
            revisionConsistency = 85,
            weakTopicImprovement = 62,
            nextBestAction = "Revise Electrostatics for 45 minutes and complete 20 questions."
        )
    )
    val examReadiness: StateFlow<ExamReadinessScore> = _examReadiness.asStateFlow()

    private val _learningMemory = MutableStateFlow(
        LearningMemory(
            frequentlyWrongTopics = listOf("Pulley Tension Problems", "Trigonometric Product-to-Sum", "Electrochemical Cell EMF", "P-Block Exceptions"),
            strongSubjects = listOf("Modern Physics", "Coordinate Geometry", "Chemical Kinetics", "English Comprehension"),
            weakChapters = listOf("Rotational Motion", "Trigonometric Equations", "Ionic Equilibrium", "Current Affairs Econ"),
            skippedTopics = listOf("Permutations & Combinations (Circ. Arr.)", "Magnetic Properties of Matter"),
            overallAccuracy = 76,
            learningSpeed = "1.8 Questions / Min",
            revisionHistoryDays = 14,
            preferredExplanationStyle = "Step-by-Step with Intuitive Shortcuts"
        )
    )
    val learningMemory: StateFlow<LearningMemory> = _learningMemory.asStateFlow()

    private val _revisionRadarItems = MutableStateFlow(
        listOf(
            RevisionRadarItem(
                id = "rev_1",
                subject = "Physics",
                chapter = "Laws of Motion & Friction",
                urgency = UrgencyLevel.HIGH,
                retentionScorePercent = 42,
                daysSinceMistake = 2,
                reason = "3 calculation & concept slips in pseudo-force questions; retention decaying rapidly.",
                recommendedStudyMinutes = 45,
                quickNotesSummary = "ΣF = ma in inertial frame; add -m*a_frame in non-inertial frame. Static friction f_s ≤ μ_s*N adjusts up to limiting value.",
                practiceQuestions = listOf(
                    PracticeQuestion(
                        id = "q_rev_1",
                        subject = "Physics",
                        chapter = "Laws of Motion",
                        difficulty = "Medium",
                        questionText = "A 5 kg block rests on a rough horizontal surface (μ = 0.4). A horizontal force of 15 N is applied. The friction force acting on the block is (g = 10 m/s²):",
                        options = listOf("20 N", "15 N", "0 N", "5 N"),
                        correctIndex = 1,
                        explanation = "Maximum static friction is f_lim = μ*N = 0.4 * (5 * 10) = 20 N. Since applied force (15 N) < f_lim, block remains stationary. Static friction equals applied force = 15 N.",
                        stepByStep = "1. Normal force N = mg = 50 N.\n2. Limiting friction f_lim = μ*N = 20 N.\n3. Since F_app = 15 N < 20 N, motion does not start.\n4. Static friction is self-adjusting = 15 N.",
                        anotherMethod = "Check threshold: If F_applied <= μ_s * N, then f = F_applied directly!",
                        realLifeExample = "Trying to push a heavy wardrobe gently: it doesn't move and friction opposes your exact push."
                    )
                )
            ),
            RevisionRadarItem(
                id = "rev_2",
                subject = "Mathematics",
                chapter = "Trigonometry & Identities",
                urgency = UrgencyLevel.MEDIUM,
                retentionScorePercent = 58,
                daysSinceMistake = 4,
                reason = "High JEE weightage (8-10%); formula recall time is 25% slower than optimal.",
                recommendedStudyMinutes = 35,
                quickNotesSummary = "sin(A±B) = sinA cosB ± cosA sinB. Transforming products to sums: 2 sinA cosB = sin(A+B) + sin(A-B).",
                practiceQuestions = listOf(
                    PracticeQuestion(
                        id = "q_rev_2",
                        subject = "Mathematics",
                        chapter = "Trigonometry",
                        difficulty = "Medium",
                        questionText = "The value of cos(20°) cos(40°) cos(80°) is:",
                        options = listOf("1/2", "1/4", "1/8", "1/16"),
                        correctIndex = 2,
                        explanation = "Standard formula: cos(θ) cos(60°-θ) cos(60°+θ) = (1/4) cos(3θ). Here θ = 20°, so (1/4) cos(60°) = (1/4)*(1/2) = 1/8.",
                        stepByStep = "1. Notice θ=20°, 60°-20°=40°, 60°+20°=80°.\n2. Use identity cos θ cos(60-θ) cos(60+θ) = 1/4 cos(3θ).\n3. 1/4 * cos(60°) = 1/4 * 1/2 = 1/8.",
                        anotherMethod = "Multiply and divide by 2 sin(20°) to trigger repeated double-angle formulas sin(2A) = 2 sinA cosA.",
                        realLifeExample = "Used in signal modulation and Fourier series harmonics decomposition."
                    )
                )
            ),
            RevisionRadarItem(
                id = "rev_3",
                subject = "Chemistry",
                chapter = "Chemical Bonding & VSEPR",
                urgency = UrgencyLevel.LOW,
                retentionScorePercent = 65,
                daysSinceMistake = 6,
                reason = "Last revised 6 days ago; hybridisation rules need periodic memory refresh.",
                recommendedStudyMinutes = 25,
                quickNotesSummary = "Steric number = Bond pairs + Lone pairs. SN=4 -> sp3 (Tetrahedral / Pyramidal / Bent). Dipole moment zero for symmetric shapes.",
                practiceQuestions = listOf(
                    PracticeQuestion(
                        id = "q_rev_3",
                        subject = "Chemistry",
                        chapter = "Chemical Bonding",
                        difficulty = "Easy",
                        questionText = "What is the shape and hybridisation of XeF4 molecule?",
                        options = listOf("Tetrahedral, sp³", "Square Planar, sp³d²", "See-saw, sp³d", "Octahedral, sp³d²"),
                        correctIndex = 1,
                        explanation = "Xe has 8 valence electrons. 4 bond pairs with F and 2 lone pairs. Steric number = 4 + 2 = 6 (sp³d²). Lone pairs occupy axial positions to minimize repulsion, yielding square planar geometry.",
                        stepByStep = "1. Valence e- of Xe = 8.\n2. 4 single bonds -> 4 electrons used, 4 remain = 2 lone pairs.\n3. Total steric number = 4 + 2 = 6 -> sp³d².\n4. Electron geometry octahedral, molecular geometry square planar.",
                        anotherMethod = "Valence Shell Electron Pair Repulsion table lookup: AB4E2 is always Square Planar.",
                        realLifeExample = "Noble gas compounds used to study ultra-reactive fluoride transfer chemistry."
                    )
                )
            )
        )
    )
    val revisionRadarItems: StateFlow<List<RevisionRadarItem>> = _revisionRadarItems.asStateFlow()

    private val _mistakeBook = MutableStateFlow(
        listOf(
            MistakeItem(
                id = "mst_1",
                exam = "JEE Main",
                subject = "Physics",
                chapter = "Laws of Motion",
                questionText = "A lift accelerates downwards with a = 2 m/s². What is apparent weight of 60 kg person? (g=10)",
                studentAnswer = "720 N (used g + a)",
                correctAnswer = "480 N (use g - a)",
                aiExplanation = "Concept mistake: When accelerating downwards, inertial pseudo-force points UPWARDS opposing gravity. Apparent weight = m(g - a) = 60*(10-2) = 480 N.",
                category = MistakeCategory.CONCEPT,
                timesRepeated = 2
            ),
            MistakeItem(
                id = "mst_2",
                exam = "JEE Main",
                subject = "Mathematics",
                chapter = "Integration",
                questionText = "Evaluate ∫ x * e^(2x) dx from 0 to 1.",
                studentAnswer = "(e² + 1)/4 (sign error during uv rule)",
                correctAnswer = "(e² + 1)/4 with correct signs -> (e² + 1)/4",
                aiExplanation = "Calculation mistake: By parts: u = x, v = e^(2x)/2. uv - ∫ v du gives x*e^(2x)/2 - e^(2x)/4. Watch minus signs closely!",
                category = MistakeCategory.CALCULATION
            ),
            MistakeItem(
                id = "mst_3",
                exam = "UPSC / SSC",
                subject = "General Knowledge",
                chapter = "Indian Constitution",
                questionText = "Which Constitutional Amendment Act reduced voting age from 21 to 18 years?",
                studentAnswer = "42nd Amendment Act",
                correctAnswer = "61st Amendment Act, 1988",
                aiExplanation = "Memory mistake: 42nd is mini-constitution (1976). Voting age reduction was enacted by the 61st Constitutional Amendment Act in 1988 (effective 1989).",
                category = MistakeCategory.MEMORY
            ),
            MistakeItem(
                id = "mst_4",
                exam = "NEET / JEE",
                subject = "Chemistry",
                chapter = "Thermodynamics",
                questionText = "For an isolated system, the change in entropy ΔS_universe is:",
                studentAnswer = "Zero (confused with adiabatic reversible)",
                correctAnswer = "Always ≥ 0",
                aiExplanation = "Silly mistake: Did not check spontaneity. For spontaneous processes in an isolated system, ΔS > 0. For reversible, ΔS = 0.",
                category = MistakeCategory.SILLY
            )
        )
    )
    val mistakeBook: StateFlow<List<MistakeItem>> = _mistakeBook.asStateFlow()

    private val _studyPlanTasks = MutableStateFlow(
        listOf(
            StudyPlanTask("t1", "06:00 - 07:30 AM", "Physics", "Rotational Dynamics", "Solve 15 Moment of Inertia Numericals", 90, isCompleted = true),
            StudyPlanTask("t2", "09:00 - 10:30 AM", "Mathematics", "Definite Integrals", "Revise King's Property & PYQ", 90, isCompleted = true),
            StudyPlanTask("t3", "02:00 - 03:30 PM", "Chemistry", "Coordination Compounds", "CFT Splitting & Isomerism", 90, isCompleted = false),
            StudyPlanTask("t4", "05:00 - 06:00 PM", "Revision Radar", "Laws of Motion & Friction", "Complete Today's Radar Mini-Test", 60, isCompleted = false),
            StudyPlanTask("t5", "08:00 - 09:30 PM", "Practice", "Mock Test 45-Min Sprint", "25 Mixed Questions Speed Test", 90, isCompleted = false)
        )
    )
    val studyPlanTasks: StateFlow<List<StudyPlanTask>> = _studyPlanTasks.asStateFlow()

    private val _dailyGoals = MutableStateFlow(
        listOf(
            DailyGoal(
                id = "dg_1",
                title = "Deep Focus Study Time",
                targetValue = 360,
                currentValue = 270,
                unit = "mins",
                isCompleted = false,
                category = "focus",
                xpReward = 80
            ),
            DailyGoal(
                id = "dg_2",
                title = "Solve High-Yield PYQs",
                targetValue = 45,
                currentValue = 35,
                unit = "questions",
                isCompleted = false,
                category = "questions",
                xpReward = 70
            ),
            DailyGoal(
                id = "dg_3",
                title = "Smart Revision Radar Topics",
                targetValue = 2,
                currentValue = 2,
                unit = "topics",
                isCompleted = true,
                category = "revision",
                xpReward = 60
            ),
            DailyGoal(
                id = "dg_4",
                title = "Spaced Repetition Flashcards",
                targetValue = 20,
                currentValue = 20,
                unit = "cards",
                isCompleted = true,
                category = "flashcards",
                xpReward = 50
            ),
            DailyGoal(
                id = "dg_5",
                title = "10-Minute Speed Mock Sprint",
                targetValue = 1,
                currentValue = 1,
                unit = "test",
                isCompleted = true,
                category = "sprint",
                xpReward = 50
            )
        )
    )
    val dailyGoals: StateFlow<List<DailyGoal>> = _dailyGoals.asStateFlow()

    private val _subjectProgressTrackers = MutableStateFlow(
        listOf(
            SubjectProgressTracker(
                id = "subj_phy",
                subjectName = "Physics",
                totalChapters = 28,
                completedChapters = 20,
                syllabusPercent = 71,
                accuracyPercent = 78,
                weeklyHoursSpent = 12.5f,
                weakTopic = "Rotational Motion",
                strongTopic = "Modern Physics & Optics",
                colorHex = 0xFF00E5FF
            ),
            SubjectProgressTracker(
                id = "subj_math",
                subjectName = "Mathematics",
                totalChapters = 30,
                completedChapters = 19,
                syllabusPercent = 63,
                accuracyPercent = 74,
                weeklyHoursSpent = 14.0f,
                weakTopic = "Trigonometric Equations",
                strongTopic = "Coordinate Geometry & Vectors",
                colorHex = 0xFF4361EE
            ),
            SubjectProgressTracker(
                id = "subj_chem",
                subjectName = "Chemistry",
                totalChapters = 26,
                completedChapters = 21,
                syllabusPercent = 81,
                accuracyPercent = 86,
                weeklyHoursSpent = 11.5f,
                weakTopic = "Ionic Equilibrium & P-Block",
                strongTopic = "Chemical Kinetics & Bonding",
                colorHex = 0xFF00F5D4
            ),
            SubjectProgressTracker(
                id = "subj_gs",
                subjectName = "General Studies & Aptitude",
                totalChapters = 24,
                completedChapters = 16,
                syllabusPercent = 67,
                accuracyPercent = 80,
                weeklyHoursSpent = 8.0f,
                weakTopic = "Macro Economics Inflation",
                strongTopic = "Indian Constitution & Polity",
                colorHex = 0xFFFFD166
            )
        )
    )
    val subjectProgressTrackers: StateFlow<List<SubjectProgressTracker>> = _subjectProgressTrackers.asStateFlow()

    private val _flashcards = MutableStateFlow(
        listOf(
            FlashcardItem("fc_1", "Physics", "Rotational Motion", "Parallel Axis Theorem", "I = I_cm + M * d² (Where d is distance between CM axis and parallel axis)", "I = I_cm + Md²"),
            FlashcardItem("fc_2", "Mathematics", "Calculus", "Leibnitz Rule for Differentiation under Integral", "d/dx [∫_{u(x)}^{v(x)} f(t,x) dt] = f(v(x),x)·v'(x) - f(u(x),x)·u'(x) + ∫ (∂f/∂x) dt", "Leibnitz Integral Rule"),
            FlashcardItem("fc_3", "Chemistry", "Physical Chemistry", "Arrhenius Equation for Rate Constant", "k = A * e^(-Ea / RT). Graph of ln(k) vs 1/T has slope = -Ea / R", "ln(k) = ln(A) - Ea/RT"),
            FlashcardItem("fc_4", "Polity / GK", "Constitution", "Writs under Article 32 & 226", "Habeas Corpus (To have the body), Mandamus (We command), Prohibition, Quo-Warranto, Certiorari", "5 Constitutional Writs")
        )
    )
    val flashcards: StateFlow<List<FlashcardItem>> = _flashcards.asStateFlow()

    private val _currentAffairs = MutableStateFlow(
        listOf(
            CurrentAffairsItem(
                id = "ca_1",
                title = "National Green Hydrogen Mission: India Crosses New Milestone",
                category = "Science & Tech",
                dateText = "Today's Briefing",
                summary = "Ministry of New and Renewable Energy announced pilot electrolyser setups under the Strategic Interventions for Green Hydrogen Transition (SIGHT) scheme.",
                keyPoints = listOf(
                    "Target: 5 MMT of green hydrogen production by 2030",
                    "Reduces 50 MMT of greenhouse gas emissions annually",
                    "High relevance for UPSC GS-3, SSC, and State PSC"
                ),
                quizQuestion = PracticeQuestion(
                    id = "ca_q1",
                    subject = "Current Affairs",
                    chapter = "Environment & Tech",
                    difficulty = "Medium",
                    questionText = "Under India's National Green Hydrogen Mission, what is the annual production target set for the year 2030?",
                    options = listOf("2 MMT", "5 MMT", "10 MMT", "15 MMT"),
                    correctIndex = 1,
                    explanation = "The Government has targeted developing green hydrogen production capacity of at least 5 Million Metric Tonnes (MMT) per annum by 2030.",
                    stepByStep = "National Green Hydrogen Mission was approved in January 2023 with an initial outlay of ₹19,744 crore.",
                    anotherMethod = "Fact recall: 5 MMT = 50 MMT CO2 reduction target.",
                    realLifeExample = "Used in heavy industries like green steel and fertiliser manufacturing."
                )
            ),
            CurrentAffairsItem(
                id = "ca_2",
                title = "RBI Updates Framework for Domestic Systemically Important Banks (D-SIBs)",
                category = "Economy",
                dateText = "Banking & Finance",
                summary = "The Reserve Bank of India reiterated State Bank of India, HDFC Bank, and ICICI Bank as D-SIBs, requiring additional common equity tier 1 capital.",
                keyPoints = listOf(
                    "D-SIBs are banks deemed 'Too Big to Fail'",
                    "Subject to higher regulatory supervision and additional capital surcharges",
                    "Critical for Banking Exams (SBI PO, IBPS PO, RBI Grade B)"
                ),
                quizQuestion = PracticeQuestion(
                    id = "ca_q2",
                    subject = "Banking Awareness",
                    chapter = "Financial Institutions",
                    difficulty = "Easy",
                    questionText = "Which of the following banks is currently NOT designated as a D-SIB by the Reserve Bank of India?",
                    options = listOf("State Bank of India", "HDFC Bank", "ICICI Bank", "Punjab National Bank"),
                    correctIndex = 3,
                    explanation = "Currently, only SBI, HDFC Bank, and ICICI Bank are designated as Domestic Systemically Important Banks (D-SIBs) in India.",
                    stepByStep = "1. D-SIB framework started in 2014.\n2. SBI was designated first, followed by ICICI (2015) and HDFC (2017).",
                    anotherMethod = "Three giants mnemonic: SBI + 2 largest private lenders (HDFC + ICICI).",
                    realLifeExample = "Protects the Indian financial ecosystem from systemic bank collapse."
                )
            )
        )
    )
    val currentAffairs: StateFlow<List<CurrentAffairsItem>> = _currentAffairs.asStateFlow()

    private val _libraryItems = MutableStateFlow(
        listOf(
            LibraryItem(
                id = "lib_1",
                title = "Complete JEE Formula Cheat Sheet — Physics Mechanics",
                targetClass = "Class 11-12 / JEE",
                subject = "Physics",
                chapter = "Kinematics & Mechanics",
                category = "Formulas",
                difficulty = "Medium",
                readTimeMinutes = 12,
                contentMarkdown = """
                    # Physics Mechanics Cheat Sheet
                    ### 1. Kinematics
                    * v = u + at
                    * s = ut + 0.5 * at^2
                    * v^2 = u^2 + 2as
                    * Projectile Max Height: H = (u^2 * sin^2(θ)) / (2g)
                    * Time of Flight: T = (2u * sin(θ)) / g
                    * Range: R = (u^2 * sin(2θ)) / g
                    
                    ### 2. Work, Energy & Power
                    * Work: W = ∫ F · dr
                    * Work-Energy Theorem: W_net = ΔK
                    * Conservative Force: F = -dU/dr
                """.trimIndent()
            ),
            LibraryItem(
                id = "lib_2",
                title = "Organic Chemistry High-Yield Named Reactions & Mechanisms",
                targetClass = "JEE / NEET",
                subject = "Chemistry",
                chapter = "Aldehydes, Ketones & Amines",
                category = "Short Notes",
                difficulty = "Hard",
                readTimeMinutes = 20,
                contentMarkdown = """
                    # Organic Named Reactions
                    ### 1. Aldol Condensation
                    * Requires at least one alpha-hydrogen.
                    * Reagent: Dilute NaOH / KOH.
                    * Product: beta-hydroxy aldehyde / ketone, followed by dehydration to alpha,beta-unsaturated carbonyl.
                    
                    ### 2. Cannizzaro Reaction
                    * For aldehydes with NO alpha-hydrogen (e.g. HCHO, PhCHO).
                    * Reagent: 50% Concentrated NaOH.
                    * Disproportionation (Redox): One molecule oxidised to acid salt, another reduced to alcohol.
                """.trimIndent()
            ),
            LibraryItem(
                id = "lib_3",
                title = "Indian Polity: Constitutional Articles & Landmark Judgments",
                targetClass = "UPSC / State PSC / SSC",
                subject = "General Studies",
                chapter = "Indian Constitution",
                category = "Definitions",
                difficulty = "Medium",
                readTimeMinutes = 15,
                contentMarkdown = """
                    # Indian Polity Master Digest
                    * Kesavananda Bharati (1973): Basic Structure Doctrine.
                    * Maneka Gandhi (1978): Article 21 requires 'Procedure established by law' to be just, fair and reasonable.
                    * Navtej Singh Johar (2018): Decriminalisation of Sec 377.
                    * KS Puttaswamy (2017): Right to Privacy is Fundamental under Art 21.
                """.trimIndent()
            )
        )
    )
    val libraryItems: StateFlow<List<LibraryItem>> = _libraryItems.asStateFlow()

    fun toggleTaskCompletion(taskId: String) {
        _studyPlanTasks.value = _studyPlanTasks.value.map { task ->
            if (task.id == taskId) {
                val next = !task.isCompleted
                if (next) awardXp(35)
                task.copy(isCompleted = next)
            } else task
        }
    }

    fun toggleDailyGoal(goalId: String) {
        _dailyGoals.value = _dailyGoals.value.map { goal ->
            if (goal.id == goalId) {
                val nextState = !goal.isCompleted
                val nextVal = if (nextState) goal.targetValue else (goal.targetValue / 2)
                if (nextState) awardXp(goal.xpReward)
                goal.copy(isCompleted = nextState, currentValue = nextVal)
            } else goal
        }
    }

    fun incrementDailyGoal(goalId: String, increment: Int) {
        _dailyGoals.value = _dailyGoals.value.map { goal ->
            if (goal.id == goalId) {
                val newVal = (goal.currentValue + increment).coerceAtLeast(0)
                val completed = newVal >= goal.targetValue
                if (completed && !goal.isCompleted) awardXp(goal.xpReward)
                goal.copy(currentValue = newVal, isCompleted = completed)
            } else goal
        }
    }

    fun updateSubjectProgress(subjectId: String, completedIncrement: Int) {
        _subjectProgressTrackers.value = _subjectProgressTrackers.value.map { tracker ->
            if (tracker.id == subjectId) {
                val newCompleted = (tracker.completedChapters + completedIncrement).coerceIn(0, tracker.totalChapters)
                val newSyllabus = ((newCompleted.toFloat() / tracker.totalChapters) * 100).toInt()
                awardXp(30)
                tracker.copy(completedChapters = newCompleted, syllabusPercent = newSyllabus)
            } else tracker
        }
    }

    fun rearrangeMissedSchedule() {
        val currentTasks = _studyPlanTasks.value
        // Intelligently rearrange: uncompleted tasks are redistributed and tagged as rescheduled
        val updated = currentTasks.map { task ->
            if (!task.isCompleted) {
                task.copy(
                    isRescheduled = true,
                    goalDescription = "⚡ AI Re-balanced: ${task.goalDescription}"
                )
            } else task
        }
        _studyPlanTasks.value = updated
    }

    fun applyGeneratedSchedule(tasks: List<StudyPlanTask>) {
        _studyPlanTasks.value = tasks
    }

    fun addMistake(mistake: MistakeItem) {
        _mistakeBook.value = listOf(mistake) + _mistakeBook.value
    }

    fun updateLanguage(lang: String) {
        _userProfile.value = _userProfile.value.copy(preferredLanguage = lang)
    }

    fun updateTargetExam(exam: String) {
        _userProfile.value = _userProfile.value.copy(targetExam = exam)
        _examReadiness.value = _examReadiness.value.copy(examName = exam)
    }

    fun awardXp(amount: Int) {
        val current = _userProfile.value
        val newXp = current.totalXp + amount
        val newCoins = current.studyCoins + (amount / 10)
        val newLevel = (newXp / 500) + 1
        _userProfile.value = current.copy(
            totalXp = newXp,
            studyCoins = newCoins,
            level = newLevel
        )
    }
}
