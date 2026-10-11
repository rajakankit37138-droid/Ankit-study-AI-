package com.example.ai

import com.example.data.models.PracticeQuestion
import com.example.data.models.MindMapNode
import com.example.data.models.FlashcardItem
import com.example.data.models.StudyPlanTask
import java.util.UUID

object AnkitAiEngine {

    suspend fun getTutorResponse(
        query: String,
        mode: String? = null, // "simple", "step_by_step", "another_method", "example", "test_me"
        language: String = "Hinglish",
        photoDescription: String? = null
    ): String {
        val promptBuilder = StringBuilder()
        promptBuilder.append("User Language Preference: $language\n")
        if (photoDescription != null) {
            promptBuilder.append("User uploaded a photo of question: $photoDescription\n")
        }
        promptBuilder.append("Student Query: $query\n")

        when (mode) {
            "simple" -> promptBuilder.append("Instruction: Explain this in extremely simple, intuitive layman terms with a friendly tone.\n")
            "step_by_step" -> promptBuilder.append("Instruction: Provide a structured, numbered step-by-step breakdown with derivations or logic.\n")
            "another_method" -> promptBuilder.append("Instruction: Provide an alternative shortcut, trick, or alternative theoretical method to solve this.\n")
            "example" -> promptBuilder.append("Instruction: Give a clear, practical, real-world example and a practice problem.\n")
            "test_me" -> promptBuilder.append("Instruction: Challenge the student with a high-yield question on this exact concept to test their grasp.\n")
        }

        // Try Gemini API first
        val apiResult = GeminiApiClient.generateContent(
            prompt = promptBuilder.toString(),
            systemInstruction = "You are ANKIT AI, the premier personal AI teacher in the ANKIT STUDY AI platform managed by Ankit Rajak. Teach clearly in $language. Be encouraging, precise, and exam-oriented."
        )

        if (apiResult.isNotBlank()) {
            return apiResult
        }

        // Fallback intelligent curated response
        return generateOfflineTutorResponse(query, mode, language)
    }

    private fun generateOfflineTutorResponse(query: String, mode: String?, language: String): String {
        val q = query.lowercase()
        return when {
            q.contains("newton") || q.contains("motion") || q.contains("force") -> {
                when (mode) {
                    "simple" -> if (language == "Hindi") {
                        "सरल भाषा में:\n\nन्यूटन का पहला नियम कहता है कि कोई वस्तु तब तक नहीं हिलेगी जब तक कोई बाहरी धक्का या खिंचाव (Force) न लगे। जैसे आपकी किताब टेबल पर तब तक शांत रहेगी जब तक आप उसे हाथ न लगाएं!\n\nदूसरा नियम: Force = Mass × Acceleration (जितनी भारी वस्तु, उतना ज्यादा बल चाहिए रफ्तार देने के लिए)।\n\nतीसरा नियम: हर क्रिया की बराबर और विपरीत प्रतिक्रिया होती है (Action = Reaction)। जैसे बंदूक चलने पर पीछे झटका लगना!"
                    } else {
                        "Simple Hinglish Samjho:\n\n1. First Law (Inertia): Koi bhi object tab tak rest ya constant speed me rahegi jab tak uspar bahar se koi force na lage. Jaise table par rakhi book bina hilaye nahi hilti!\n\n2. Second Law (F = ma): Jitna zyada mass, utna zyada force chahiye speed badhane ke liye. Heavy truck ko accelerate karna bike se mushkil hota hai.\n\n3. Third Law: Action-Reaction pair! Jab aap swim karte ho, pani ko peeche dhakelte ho aur pani aapko aage push karta hai!"
                    }
                    "another_method" -> "⚡ Alternate Shortcut / Trick:\n\nMechanics me free-body diagram (FBD) shortcut:\nΣ F_ext = M_total × a_system\n\nPure system ko ek single mass mankar internal tension/normal forces ko cancel out kar do. Isse pulley and connected block problems bina 3 alag equations banaye 15 seconds me solve ho jate hain!"
                    "example" -> "🎯 Practical Real-life Example:\n\nRocket Launch me Newton's 3rd Law: Rocket combustion chamber se gases bahut high velocity se niche nikalti hain (Action), jisse rocket par upward thrust milta hai (Reaction) aur wo gravity ko beat karke space me chala jata hai!"
                    "test_me" -> "🧪 Quick Concept Test:\n\nEk lift acceleration 'a' se niche aa rahi hai. Lift me khade 60 kg student ka apparent weight kya hoga? (g = 10 m/s²)\nA) 60(g+a)\nB) 60(g-a)\nC) 60g\nD) Zero\n\nApna answer socho aur Step-by-Step button dabao solution check karne ke liye!"
                    else -> "Newton's Laws of Motion - Core Concept Summary:\n\n1. Law of Inertia: Body maintains state of rest/uniform motion unless external net force acts.\n2. Law of Momentum: F = dp/dt = m(dv/dt) = m*a (for constant mass).\n3. Action-Reaction: For every action, there is an equal & opposite reaction acting on DIFFERENT bodies simultaneously.\n\nPro Tip: JEE/NEET me yaad rakhein ki Action and Reaction force kabhi ek dusre ko cancel nahi karte kyunki wo do alag bodies par lagte hain!"
                }
            }
            q.contains("trigonometry") || q.contains("sin") || q.contains("cos") || q.contains("tan") -> {
                when (mode) {
                    "another_method" -> "⚡ Shortcut Trick for Trigonometric Identities:\n\nValue Substitution Method (ASTC trick):\nAgar question options ke sath diya hai, toh standard angle jaise θ = 45° ya θ = 30° put karke match karo! 90% JEE/NDA questions 20 seconds me verify ho jate hain bina lambi identity expand kiye!"
                    "simple" -> "Trigonometry ka basic intuition:\n\nRight-angled triangle me:\n• sin θ = Opposite / Hypotenuse (Kitna uncha utha)\n• cos θ = Adjacent / Hypotenuse (Kitna aage badha)\n• tan θ = Slope (sin θ / cos θ)\n\nUnit Circle me (x, y) = (cos θ, sin θ) hota hai!"
                    else -> "Trigonometry Fundamental Identities:\n\n1. sin²θ + cos²θ = 1\n2. 1 + tan²θ = sec²θ\n3. 1 + cot²θ = cosec²θ\n4. sin(2θ) = 2 sin θ cos θ\n5. cos(2θ) = cos²θ - sin²θ = 2cos²θ - 1 = 1 - 2sin²θ\n\nRevision Tip: Sign convention ke liye 'Add Sugar To Coffee' (Quadrants I: All, II: Sin, III: Tan, IV: Cos positive) yaad rakho!"
                }
            }
            q.contains("constitution") || q.contains("article") || q.contains("polity") -> {
                "Indian Constitution High-Yield Summary (UPSC/SSC):\n\n• Article 14: Equality before law\n• Article 21: Protection of life & personal liberty (Right to Privacy under Puttaswamy judgment)\n• Article 32: Constitutional Remedies (Heart & Soul according to Dr. B.R. Ambedkar)\n• Article 368: Amendment power of Parliament\n\nKey Rule: Basic Structure Doctrine (Kesavananda Bharati Case, 1973) cannot be altered!"
            }
            else -> {
                when (mode) {
                    "simple" -> "Yeh concept ka basic saral funda:\n\n\"$query\"\n\nIsko daily life ke angle se samjhein toh sabse pehle core definition pe dhyan dijiye: iska main maqsad hai input aur output ke beech ka causal relationship samajhna. Jab foundation clear ho jati hai, formulas automatically fit ho jate hain!"
                    "step_by_step" -> "Step-by-Step Analytical Breakdown for: \"$query\"\n\nStep 1: Identify given variables and boundary conditions.\nStep 2: Choose the governing formula or principle.\nStep 3: Check units (SI conversion is where 40% errors happen!).\nStep 4: Solve algebraically before substituting numerical values.\nStep 5: Sanity check answer against physical reality."
                    "another_method" -> "Alternative Shortcut Method:\n\nIs type ke questions me Dimensional Analysis aur Option Elimination lagayein:\n1. Units check karein - agar dimensions match nahi karti toh option eliminate karein.\n2. Extreme boundary cases (e.g. limit x -> 0 or x -> ∞) test karein."
                    "example" -> "Illustrative Real-World Example:\n\nIs concept ko implement karne ke liye standard exam PYQ pattern dekhein jahan 2 distinct parameters vary kiye jate hain. Practice se intuition sharp hoti hai!"
                    "test_me" -> "AI Quick Challenge on this topic:\n\nAgar hum system parameters ko double kar dein jabki surrounding temperature constant rahe, toh net outcome par kya impact aayega?\nA) Doubles\nB) Becomes 4 times\nC) Halved\nD) Remains unchanged"
                    else -> "Hello! Main hoon ANKIT AI, aapka personal AI Teacher (Managed by Ankit Rajak).\n\nAapne pucha: \"$query\"\n\nIs concept ko master karne ke liye niche diye buttons me se chun sakte hain:\n• 'Explain Simply' saral bhasha ke liye\n• 'Step-by-Step' derivation aur numericals ke liye\n• 'Another Method' exam shortcuts aur tricks ke liye\n• 'Test Me' quick quiz ke liye!"
                }
            }
        }
    }

    fun generateMindMap(chapterName: String): MindMapNode {
        return when {
            chapterName.contains("Motion", ignoreCase = true) || chapterName.contains("Newton", ignoreCase = true) -> {
                MindMapNode(
                    id = "root_motion",
                    title = "Laws of Motion",
                    subtitle = "Class 11 Physics / JEE / NEET Weightage: 8%",
                    nodeType = "Chapter",
                    children = listOf(
                        MindMapNode(
                            id = "c1",
                            title = "Inertia & 1st Law",
                            subtitle = "Resistance to velocity change; Equilibrium condition ΣF = 0",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("f1", "Equilibrium Equation", "Σ Fx = 0, Σ Fy = 0", "Formula"),
                                MindMapNode("e1", "Inertial Frames", "Frames with zero acceleration (Earth approx)", "Example")
                            )
                        ),
                        MindMapNode(
                            id = "c2",
                            title = "Momentum & 2nd Law",
                            subtitle = "Rate of change of momentum is proportional to applied force",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("f2", "Force Formula", "F = dp/dt = m·a + v·(dm/dt)", "Formula"),
                                MindMapNode("f3", "Impulse Theorem", "J = ∫ F dt = Δp = m(v - u)", "Formula")
                            )
                        ),
                        MindMapNode(
                            id = "c3",
                            title = "Friction Dynamics",
                            subtitle = "Static, Limiting, Kinetic friction; Angle of repose",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("f4", "Friction Law", "f_s ≤ μ_s·N, f_k = μ_k·N", "Formula"),
                                MindMapNode("q1", "High-Yield JEE Question", "Block on rough incline with horizontal force F", "HighYield")
                            )
                        )
                    )
                )
            }
            chapterName.contains("Electro", ignoreCase = true) -> {
                MindMapNode(
                    id = "root_electro",
                    title = "Electrostatics",
                    subtitle = "Class 12 Physics / JEE / NEET Weightage: 10%",
                    nodeType = "Chapter",
                    children = listOf(
                        MindMapNode(
                            id = "e_c1",
                            title = "Coulomb's Law & Field",
                            subtitle = "Forces between point charges and electric field intensity",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("e_f1", "Coulomb Force", "F = (1/4πε₀) · (q₁q₂/r²)", "Formula"),
                                MindMapNode("e_f2", "Dipole Field", "E_axial = 2kp/r³, E_equatorial = kp/r³", "Formula")
                            )
                        ),
                        MindMapNode(
                            id = "e_c2",
                            title = "Gauss's Law & Flux",
                            subtitle = "Symmetric charge distributions (Spheres, Cylinders, Sheets)",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("e_f3", "Flux Theorem", "Φ = ∮ E·dA = Q_enclosed / ε₀", "Formula")
                            )
                        ),
                        MindMapNode(
                            id = "e_c3",
                            title = "Capacitance & Dielectrics",
                            subtitle = "Parallel plate capacitors, energy density, combinations",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("e_f4", "Capacitor Energy", "U = 1/2 CV² = Q² / (2C)", "Formula"),
                                MindMapNode("e_q1", "High-Yield NEET PYQ", "Dielectric slab insertion with battery connected vs disconnected", "HighYield")
                            )
                        )
                    )
                )
            }
            else -> {
                MindMapNode(
                    id = "root_gen",
                    title = chapterName,
                    subtitle = "Comprehensive AI Generated Study Hierarchy",
                    nodeType = "Chapter",
                    children = listOf(
                        MindMapNode(
                            id = "g_c1",
                            title = "Core Principles & Axioms",
                            subtitle = "Fundamental theories and building blocks",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("g_f1", "Governing Equation", "Standard representation & constraints", "Formula")
                            )
                        ),
                        MindMapNode(
                            id = "g_c2",
                            title = "Mathematical Formulations",
                            subtitle = "Quantitative expressions and parameter dependencies",
                            nodeType = "Concept",
                            children = listOf(
                                MindMapNode("g_f2", "Derived Formula", "High-frequency exam application equation", "Formula")
                            )
                        ),
                        MindMapNode(
                            id = "g_c3",
                            title = "High-Yield Applications & PYQ",
                            subtitle = "Patterns tested repeatedly in competitive exams",
                            nodeType = "HighYield"
                        )
                    )
                )
            }
        }
    }

    suspend fun generateCustomStudySchedule(
        examGoal: String,
        availableHours: Int,
        prepLevel: String,
        focusSubjects: List<String>,
        includeRevision: Boolean,
        includeMockSprint: Boolean
    ): List<StudyPlanTask> {
        val subjectListStr = if (focusSubjects.isNotEmpty()) focusSubjects.joinToString(", ") else "All Core Subjects"
        val prompt = """
            You are ANKIT AI, premier personal AI exam coach and study planner in the ANKIT STUDY AI app managed by Ankit Rajak.
            Generate a personalized daily study schedule.
            
            Target Exam Goal: $examGoal
            Available Daily Hours: $availableHours hours
            Preparation Level: $prepLevel
            Focus / Weak Subjects: $subjectListStr
            Include Spaced Revision: $includeRevision
            Include Timed Mock Sprint: $includeMockSprint
            
            Format each task strictly on a separate line in this exact format:
            TIME | SUBJECT | CHAPTER | GOAL | MINUTES
            
            Example:
            06:00 - 07:30 AM | Physics | Laws of Motion | Solve 15 numerical problems on friction | 90
            09:00 - 10:30 AM | Mathematics | Calculus | Master King's Property and definite integrals | 90
        """.trimIndent()

        val apiResult = GeminiApiClient.generateContent(
            prompt = prompt,
            systemInstruction = "You are ANKIT AI study planner. Output ONLY lines in format: TIME | SUBJECT | CHAPTER | GOAL | MINUTES"
        )

        if (apiResult.isNotBlank()) {
            val parsed = parseScheduleLines(apiResult)
            if (parsed.isNotEmpty()) {
                return parsed
            }
        }

        return generateCuratedScheduleFallback(
            examGoal = examGoal,
            availableHours = availableHours,
            prepLevel = prepLevel,
            focusSubjects = focusSubjects,
            includeRevision = includeRevision,
            includeMockSprint = includeMockSprint
        )
    }

    private fun parseScheduleLines(rawOutput: String): List<StudyPlanTask> {
        val tasks = mutableListOf<StudyPlanTask>()
        val lines = rawOutput.lines()
        var index = 1

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.isBlank() || !trimmed.contains("|")) continue

            val parts = trimmed.split("|").map { it.trim() }
            if (parts.size >= 5) {
                val time = parts[0]
                val subject = parts[1]
                val chapter = parts[2]
                val goal = parts[3]
                val duration = parts[4].filter { it.isDigit() }.toIntOrNull() ?: 60

                tasks.add(
                    StudyPlanTask(
                        id = "ai_task_$index",
                        timeSlot = time,
                        subject = subject,
                        chapter = chapter,
                        goalDescription = goal,
                        durationMinutes = duration
                    )
                )
                index++
            }
        }
        return tasks
    }

    private fun generateCuratedScheduleFallback(
        examGoal: String,
        availableHours: Int,
        prepLevel: String,
        focusSubjects: List<String>,
        includeRevision: Boolean,
        includeMockSprint: Boolean
    ): List<StudyPlanTask> {
        val tasks = mutableListOf<StudyPlanTask>()
        val isJeeOrNeet = examGoal.contains("JEE", ignoreCase = true) || examGoal.contains("NEET", ignoreCase = true)
        val isUpsc = examGoal.contains("UPSC", ignoreCase = true) || examGoal.contains("PSC", ignoreCase = true)
        val isSscOrBank = examGoal.contains("SSC", ignoreCase = true) || examGoal.contains("Bank", ignoreCase = true) || examGoal.contains("Railway", ignoreCase = true)

        var slotIndex = 1

        // Morning Block 1
        val morningSubj = focusSubjects.getOrNull(0) ?: when {
            isJeeOrNeet -> "Physics"
            isUpsc -> "Polity & Governance"
            isSscOrBank -> "Quantitative Aptitude"
            else -> "Core Subject 1"
        }
        val morningChapter = when {
            isJeeOrNeet -> "Rotational Dynamics & Torque"
            isUpsc -> "Constitutional Bodies & Writs"
            isSscOrBank -> "Arithmetic & Percentage Ratios"
            else -> "Fundamental Principles"
        }
        tasks.add(
            StudyPlanTask(
                id = "ai_task_${slotIndex++}",
                timeSlot = "06:00 - 07:30 AM",
                subject = morningSubj,
                chapter = morningChapter,
                goalDescription = "Deep Focus: Complete high-yield concepts & 15 numericals ($prepLevel pace)",
                durationMinutes = 90
            )
        )

        if (availableHours >= 4) {
            val midDaySubj = focusSubjects.getOrNull(1) ?: when {
                isJeeOrNeet -> "Mathematics"
                isUpsc -> "Modern Indian History"
                isSscOrBank -> "Logical Reasoning"
                else -> "Core Subject 2"
            }
            val midDayChapter = when {
                isJeeOrNeet -> "Definite Integrals & Area"
                isUpsc -> "Freedom Struggle (1857–1947)"
                isSscOrBank -> "Syllogisms & Seating Arrangements"
                else -> "Analytical Problem Solving"
            }
            tasks.add(
                StudyPlanTask(
                    id = "ai_task_${slotIndex++}",
                    timeSlot = "09:30 - 11:00 AM",
                    subject = midDaySubj,
                    chapter = midDayChapter,
                    goalDescription = "Problem-solving drill & previous year question patterns",
                    durationMinutes = 90
                )
            )
        }

        if (availableHours >= 6) {
            val afternoonSubj = focusSubjects.getOrNull(2) ?: when {
                isJeeOrNeet -> "Chemistry"
                isUpsc -> "Economy & Macro Indicators"
                isSscOrBank -> "English & Comprehension"
                else -> "Core Subject 3"
            }
            val afternoonChapter = when {
                isJeeOrNeet -> "Chemical Equilibrium & Kinetics"
                isUpsc -> "Fiscal Deficit & Inflation Targeting"
                isSscOrBank -> "Error Spotting & Cloze Test"
                else -> "Comprehensive Review"
            }
            tasks.add(
                StudyPlanTask(
                    id = "ai_task_${slotIndex++}",
                    timeSlot = "02:30 - 04:00 PM",
                    subject = afternoonSubj,
                    chapter = afternoonChapter,
                    goalDescription = "Theory revision & formula flashcard drill",
                    durationMinutes = 90
                )
            )
        }

        if (includeRevision) {
            tasks.add(
                StudyPlanTask(
                    id = "ai_task_${slotIndex++}",
                    timeSlot = "05:30 - 06:15 PM",
                    subject = "Revision Radar",
                    chapter = "Spaced Retrieval Block",
                    goalDescription = "⚡ AI Radar: Revise high-decay topics & Mistake Book slips",
                    durationMinutes = 45
                )
            )
        }

        if (includeMockSprint || availableHours >= 8) {
            tasks.add(
                StudyPlanTask(
                    id = "ai_task_${slotIndex++}",
                    timeSlot = "08:00 - 09:15 PM",
                    subject = "Mock Test Sprint",
                    chapter = "$examGoal Timed Section",
                    goalDescription = "30 Questions timed exam simulation with instant AI rank analysis",
                    durationMinutes = 75
                )
            )
        }

        return tasks
    }
}

