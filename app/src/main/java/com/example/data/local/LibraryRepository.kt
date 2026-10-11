package com.example.data.local

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LibraryRepository(private val dao: LibraryMaterialDao) {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (dao.getCount() == 0) {
                seedInitialMaterials()
            } else {
                // Automatically upgrade any existing cached records to Ultra HD / 4K resolution
                val existing = dao.getAllMaterialsList()
                for (item in existing) {
                    if (item.imageUrl.contains("w=600")) {
                        val hdUrl = item.imageUrl.replace("w=600", "w=1600").replace("q=80", "q=90")
                        dao.updateImageUrl(item.id, hdUrl)
                    }
                }
            }
        }
    }

    fun getAllMaterials(): Flow<List<LibraryMaterialEntity>> = dao.getAllMaterials()

    fun getBookmarkedMaterials(): Flow<List<LibraryMaterialEntity>> = dao.getBookmarkedMaterials()

    fun searchAndFilter(
        query: String,
        typeFilter: String?,
        subjectFilter: String?,
        examFilter: String? = null,
        onlyBookmarked: Boolean,
        sortBy: String = "Latest" // "Latest", "Top Rated", "Most Read", "Fast Read"
    ): Flow<List<LibraryMaterialEntity>> {
        val baseFlow = if (query.isBlank()) {
            if (onlyBookmarked) dao.getBookmarkedMaterials() else dao.getAllMaterials()
        } else {
            dao.searchMaterials(query.trim())
        }

        return baseFlow.map { list ->
            val filtered = list.filter { item ->
                val matchesType = typeFilter == null || typeFilter == "All" || item.type.equals(typeFilter, ignoreCase = true)
                val matchesSubject = subjectFilter == null || subjectFilter == "All" || item.subject.equals(subjectFilter, ignoreCase = true)
                val matchesExam = examFilter == null || examFilter == "All" || item.targetExam.contains(examFilter, ignoreCase = true)
                val matchesBookmark = !onlyBookmarked || item.isBookmarked
                matchesType && matchesSubject && matchesExam && matchesBookmark
            }

            when (sortBy) {
                "Top Rated" -> filtered.sortedByDescending { it.rating }
                "Most Read" -> filtered.sortedByDescending { it.downloadsCount }
                "Fast Read" -> filtered.sortedBy { it.readTimeMinutes }
                else -> filtered.sortedByDescending { it.timestamp }
            }
        }
    }

    suspend fun toggleBookmark(id: String, currentState: Boolean) {
        dao.updateBookmark(id, !currentState)
    }

    suspend fun insertMaterial(material: LibraryMaterialEntity) {
        dao.insertMaterial(material)
    }

    suspend fun deleteMaterial(id: String) {
        dao.deleteMaterial(id)
    }

    private suspend fun seedInitialMaterials() {
        val initialData = listOf(
            LibraryMaterialEntity(
                id = "mat_1",
                title = "Quantum Mechanics: Wave-Particle Duality & Schrödinger Equation Intuition",
                description = "Master quantum wavefunctions, Heisenberg uncertainty, and probabilistic orbital densities with intuitive physical models.",
                content = """
                    # Quantum Mechanics: Foundations & Applications
                    
                    ### 1. De Broglie Wavelength & Dual Nature
                    Every moving particle exhibits wave-like behavior governed by the relation:
                    λ = h / p = h / (m * v) = h / sqrt(2 * m * E_k)
                    
                    * For accelerated electrons: λ = 1.227 / sqrt(V) nm.
                    * Davisson-Germer Experiment confirmed electron diffraction across nickel crystal lattices.
                    
                    ### 2. Heisenberg Uncertainty Principle
                    Δx · Δp_x ≥ ℏ / 2
                    It is fundamentally impossible to simultaneously determine both exact position and exact linear momentum of a quantum system with arbitrary precision.
                    
                    ### 3. Time-Independent Schrödinger Equation
                    H_hat · Ψ = E · Ψ
                    (-ℏ² / 2m) · (d²Ψ / dx²) + V(x) · Ψ = E · Ψ
                    
                    * |Ψ(x)|² represents the Born probability density of finding the particle in space.
                    * Normalization condition: Integral of |Ψ(x)|² dx over all space = 1.
                    
                    ### 4. High-Yield Exam Takeaway
                    In JEE Advanced / Graduate Physics, zero-point energy of a 1D harmonic oscillator is E_zero = 0.5 * ℏ * ω, proving quantum systems never fully come to rest even at absolute zero!
                """.trimIndent(),
                type = "Curated Article",
                subject = "Physics",
                targetExam = "JEE Main & Advanced",
                author = "Ankit Rajak",
                imageUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=1600&auto=format&fit=crop&q=90",
                tags = "Quantum, Wave-Particle, Modern Physics, Schrödinger",
                readTimeMinutes = 8,
                isBookmarked = true,
                downloadsCount = 3840,
                rating = 4.95f
            ),
            LibraryMaterialEntity(
                id = "mat_2",
                title = "Complete Handwritten Short Notes: Laws of Motion & Pulley Constraints",
                description = "High-yield summary covering inertial pseudo-forces, wedge constraints, and static vs limiting friction thresholds.",
                content = """
                    # Laws of Motion & Advanced Constraints (Short Notes)
                    
                    ### 1. Free Body Diagram (FBD) Best Practices
                    * Identify all external contact forces (Normal N, Tension T, Friction f) and field forces (mg).
                    * In a non-inertial frame accelerating at a_frame, apply pseudo-force F_pseudo = -m * a_frame.
                    
                    ### 2. String & Pulley Constraint Theorem
                    For inextensible strings:
                    Σ (T · a) = 0 and Σ (T · v) = 0
                    Work done by internal tension forces across the entire connected system is identically zero!
                    
                    ### 3. Friction Mechanics
                    * Static Friction: 0 ≤ f_s ≤ f_lim (Self-adjusting to match external shear force).
                    * Limiting Friction: f_lim = μ_s * N.
                    * Kinetic Friction: f_k = μ_k * N (acts opposite to relative sliding velocity).
                """.trimIndent(),
                type = "Notes",
                subject = "Physics",
                targetExam = "JEE Main & Advanced",
                author = "Ankit Rajak (Handwritten Digest)",
                imageUrl = "https://images.unsplash.com/photo-1509228468518-180dd4864904?w=1600&auto=format&fit=crop&q=90",
                tags = "Mechanics, Newton, FBD, Pulley, Friction",
                readTimeMinutes = 12,
                isBookmarked = false,
                downloadsCount = 4920,
                rating = 4.9f
            ),
            LibraryMaterialEntity(
                id = "mat_3",
                title = "Mathematics Integral Calculus Master Formula Sheet & King's Property Tricks",
                description = "Definite integrals, substitution shortcuts, Wallis formula, and symmetry transformations for lightning-fast solving.",
                content = """
                    # Integral Calculus Formula Sheet
                    
                    ### 1. King's Property & Symmetry
                    ∫ [a to b] f(x) dx = ∫ [a to b] f(a + b - x) dx
                    
                    For symmetric limits:
                    ∫ [0 to 2a] f(x) dx = ∫ [0 to a] f(x) dx + ∫ [0 to a] f(2a - x) dx
                    
                    ### 2. Standard Indefinite Integrals
                    * ∫ dx / (x² + a²) = (1/a) * arctan(x/a) + C
                    * ∫ dx / sqrt(a² - x²) = arcsin(x/a) + C
                    * ∫ dx / sqrt(x² ± a²) = ln|x + sqrt(x² ± a²)| + C
                    
                    ### 3. Wallis Formula for High Powers
                    ∫ [0 to π/2] sin^n(x) * cos^m(x) dx = ((n-1)!! * (m-1)!!) / ((n+m)!!) * (π/2 if both even, else 1)
                """.trimIndent(),
                type = "Formulas",
                subject = "Mathematics",
                targetExam = "JEE Main & Advanced",
                author = "ANKIT AI Curated",
                imageUrl = "https://images.unsplash.com/photo-1635070041130-4e0e5a6a4c28?w=1600&auto=format&fit=crop&q=90",
                tags = "Calculus, Integration, Formulas, King's Property",
                readTimeMinutes = 6,
                isBookmarked = true,
                downloadsCount = 5820,
                rating = 4.98f
            ),
            LibraryMaterialEntity(
                id = "mat_4",
                title = "Organic Chemistry: Named Reactions, Electrophiles & Nucleophiles Master Guide",
                description = "Comprehensive breakdown of Aldol, Cannizzaro, Grignard, Friedel-Crafts and Hoffman bromamide degradation.",
                content = """
                    # Organic Chemistry Named Reactions Master Guide
                    
                    ### 1. Aldol Condensation
                    * Substrate: Carbonyls with at least one alpha-hydrogen.
                    * Reagent: Dilute NaOH or Ba(OH)2.
                    * Intermediate: Resonance-stabilized enolate ion attacks electrophilic carbonyl.
                    * Product: beta-hydroxy carbonyl, which upon heating undergoes dehydration to give alpha,beta-unsaturated carbonyl compound.
                    
                    ### 2. Cannizzaro Reaction (Disproportionation)
                    * Substrate: Aldehydes having NO alpha-hydrogen (e.g., HCHO, PhCHO, Furfural).
                    * Reagent: Concentrated 50% KOH / NaOH.
                    * Mechanism: Hydride transfer from tetrahedral gem-diolate intermediate.
                    * Products: One molecule reduced to alcohol, other oxidized to carboxylate salt.
                    
                    ### 3. Grignard Reagent (R-MgX)
                    * Acts as powerful carbon nucleophile (R delta-) and strong base.
                    * Reaction with Formaldehyde yields 1-degree Alcohol.
                    * Other Aldehydes yield 2-degree Alcohol; Ketones yield 3-degree Alcohol.
                """.trimIndent(),
                type = "Study Material",
                subject = "Chemistry",
                targetExam = "JEE / NEET",
                author = "Ankit Rajak",
                imageUrl = "https://images.unsplash.com/photo-1532187863486-abf9dbad1b69?w=1600&auto=format&fit=crop&q=90",
                tags = "Organic, Chemistry, Named Reactions, Aldol, Grignard",
                readTimeMinutes = 15,
                isBookmarked = false,
                downloadsCount = 3410,
                rating = 4.88f
            ),
            LibraryMaterialEntity(
                id = "mat_5",
                title = "Indian Constitution & Landmark Supreme Court Judgments (1973–2026)",
                description = "Curated editorial on Basic Structure Doctrine, Right to Privacy, Federalism, and Article 32 Writs.",
                content = """
                    # Indian Polity Master Digest: Landmark Judgments
                    
                    ### 1. Kesavananda Bharati v. State of Kerala (1973)
                    * Largest constitutional bench (13 judges, 7-6 verdict).
                    * Established the Basic Structure Doctrine: Parliament's amending power under Article 368 cannot alter the core identity of the Constitution.
                    * Key elements of basic structure: Supremacy of the Constitution, Republican & Democratic form, Secularism, Separation of powers, Judicial review.
                    
                    ### 2. Maneka Gandhi v. Union of India (1978)
                    * Expanded Article 21: "Procedure established by law" must be just, fair, and reasonable, incorporating American "Due process of law".
                    * Articles 14, 19, and 21 form the celebrated Golden Triangle.
                    
                    ### 3. Justice K.S. Puttaswamy v. Union of India (2017)
                    * Unanimously declared Right to Privacy as an intrinsic part of the Right to Life and Personal Liberty under Article 21.
                    
                    ### 4. Article 32: Constitutional Remedies
                    * Dr. B.R. Ambedkar called it the "Heart and Soul" of the Indian Constitution.
                    * Five Writs: Habeas Corpus, Mandamus, Prohibition, Certiorari, Quo-Warranto.
                """.trimIndent(),
                type = "Curated Article",
                subject = "Polity & GS",
                targetExam = "UPSC / State PSC / SSC",
                author = "Ankit Rajak (UPSC Mentor)",
                imageUrl = "https://images.unsplash.com/photo-1589829545856-d10d557cf95f?w=1600&auto=format&fit=crop&q=90",
                tags = "Polity, Constitution, UPSC, Supreme Court, Articles",
                readTimeMinutes = 14,
                isBookmarked = true,
                downloadsCount = 4200,
                rating = 4.96f
            ),
            LibraryMaterialEntity(
                id = "mat_6",
                title = "Cell Biology & Genetics: DNA Replication & Mendelian Inheritance",
                description = "High-yield NEET notes on semi-conservative replication, Okazaki fragments, and chromosome mapping.",
                content = """
                    # NEET Biology: Cell & Genetics Master Notes
                    
                    ### 1. DNA Replication Machinery
                    * Meselson & Stahl (1958) proved semi-conservative replication using N-15 isotope and CsCl density gradient centrifugation.
                    * Helicase: Unwinds DNA double helix.
                    * Topoisomerase / Gyrase: Relieves torsional supercoiling tension.
                    * DNA Polymerase III: Synthesizes DNA in 5' to 3' direction.
                    * Lagging Strand: Discontinuous synthesis forming Okazaki fragments, joined by DNA Ligase.
                    
                    ### 2. Mendelian Principles
                    * Law of Segregation (Monohybrid ratio: Genotypic 1:2:1, Phenotypic 3:1).
                    * Law of Independent Assortment (Dihybrid phenotypic ratio: 9:3:3:1).
                    * Incomplete Dominance: Mirabilis jalapa (Snapdragon 1:2:1).
                    * Codominance: ABO blood grouping.
                """.trimIndent(),
                type = "Notes",
                subject = "Biology",
                targetExam = "NEET (Medical)",
                author = "ANKIT AI Medical Faculty",
                imageUrl = "https://images.unsplash.com/photo-1530497610245-94d3c16cda28?w=1600&auto=format&fit=crop&q=90",
                tags = "NEET, Biology, DNA, Genetics, Mendel",
                readTimeMinutes = 11,
                isBookmarked = false,
                downloadsCount = 2890,
                rating = 4.87f
            ),
            LibraryMaterialEntity(
                id = "mat_7",
                title = "JEE Advanced 10-Year Solved PYQ Compilation: Electrostatics & Capacitance",
                description = "Past year questions with multiple solving approaches, dimensional checks, and boundary condition shortcuts.",
                content = """
                    # Electrostatics Solved PYQ Master Compilation
                    
                    ### Problem 1 (JEE Advanced)
                    A dielectric slab of thickness t and dielectric constant K is inserted into a parallel-plate capacitor of plate separation d.
                    
                    Analytical Solution:
                    * Equivalent capacitance is treated as two capacitors in series:
                    C = (epsilon_0 * A) / ((d - t) + (t / K))
                    * Notice limiting cases: As K -> 1, C = (epsilon_0 * A) / d. As t -> d, C = (K * epsilon_0 * A) / d.
                    
                    ### Problem 2 (Gauss Law on Concentric Spheres)
                    * Net charge enclosed in Gaussian surface determines flux.
                    * Conductor interior electric field is always zero under electrostatic equilibrium.
                """.trimIndent(),
                type = "PYQ",
                subject = "Physics",
                targetExam = "JEE Main & Advanced",
                author = "Ankit Rajak",
                imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1600&auto=format&fit=crop&q=90",
                tags = "PYQ, Electrostatics, Capacitance, Solved Papers",
                readTimeMinutes = 18,
                isBookmarked = false,
                downloadsCount = 6120,
                rating = 4.94f
            ),
            LibraryMaterialEntity(
                id = "mat_8",
                title = "Speed Maths & Vedic Arithmetic Tricks for Banking & SSC Exams",
                description = "Multiply 3-digit numbers in 5 seconds, digital sum verification, and cube root calculation shortcuts.",
                content = """
                    # Vedic Mathematics & Speed Arithmetic
                    
                    ### 1. Vertically and Crosswise (Urdhva Tiryagbhyam)
                    Two-digit multiplication:
                    1. Multiply right column.
                    2. Cross-multiply and sum.
                    3. Multiply left column.
                    
                    ### 2. Base Method for Numbers near 100
                    Example: 96 * 94
                    * Deficits: -4 and -6.
                    * Left side: 96 - 6 = 90.
                    * Right side: (-4) * (-6) = 24.
                    * Result: 9024! Instant 2-second computation.
                    
                    ### 3. Digital Root Elimination
                    Sum of digits repeatedly until single digit (modulo 9).
                    The digital root of LHS must equal digital root of RHS in arithmetic equations. Instantly eliminate 3 out of 4 MCQ choices!
                """.trimIndent(),
                type = "Curated Article",
                subject = "Reasoning",
                targetExam = "Banking (IBPS/SBI) & SSC",
                author = "Ankit Rajak",
                imageUrl = "https://images.unsplash.com/photo-1596495578065-6e0763fa1178?w=1600&auto=format&fit=crop&q=90",
                tags = "Speed Maths, Vedic, Banking, SSC, Aptitude",
                readTimeMinutes = 7,
                isBookmarked = true,
                downloadsCount = 5120,
                rating = 4.97f
            ),
            LibraryMaterialEntity(
                id = "mat_9",
                title = "UPSC Civil Services Prelims 2020–2025: Indian Economy & Budget Analysis PYQ",
                description = "Complete solved past exam questions on Monetary Policy, RBI repo corridors, Fiscal Deficit, and Balance of Payments.",
                content = """
                    # UPSC Prelims: Indian Economy & Financial Systems Solved PYQ

                    ### PYQ 1: Monetary Policy Committee (UPSC CSE 2021)
                    Q: Which of the following statements is/are correct regarding the Monetary Policy Committee (MPC)?
                    1. It decides the RBI's benchmark interest rates.
                    2. It is a 12-member body including the Governor of RBI.
                    3. It functions under the chairmanship of the Union Finance Minister.

                    Correct Answer: Only 1 is correct.
                    Detailed Explanation:
                    * Under section 45ZB of the amended RBI Act 1934, the MPC consists of 6 members (3 from RBI, 3 appointed by Central Government).
                    * The Governor of the RBI is the ex-officio Chairperson of the MPC.
                    * The MPC meets at least 4 times a year to set the Policy Repo Rate targeting CPI inflation at 4% ± 2%.

                    ### PYQ 2: External Commercial Borrowings & BoP (UPSC CSE 2023)
                    * Current Account = Trade Balance (Merchandise) + Invisibles (Services + Remittances).
                    * Capital Account includes FDI, FPI, External Commercial Borrowings (ECBs), and NRI deposits.
                    * Remittances by overseas Indians are classified under Invisibles (Current Account Transfers), making India the world's top remittance recipient (> $125 Billion).
                """.trimIndent(),
                type = "PYQ",
                subject = "Polity & GS",
                targetExam = "UPSC",
                author = "Ankit Rajak (UPSC IAS Mentor)",
                imageUrl = "https://images.unsplash.com/photo-1526304640581-d334cdbbf45e?w=1600&auto=format&fit=crop&q=90",
                tags = "UPSC, PYQ, Economy, Budget, Prelims, Solved Papers",
                readTimeMinutes = 16,
                isBookmarked = true,
                downloadsCount = 7430,
                rating = 4.99f
            ),
            LibraryMaterialEntity(
                id = "mat_10",
                title = "SSC CGL Tier 1 & 2: Quantitative Aptitude Geometry & Circle Theorems PYQ",
                description = "100 Repeated Past Exam Questions on Tangent-Secant theorem, Inradius-Circumradius, and Cyclic Quadrilaterals.",
                content = """
                    # SSC CGL Quantitative Aptitude: High-Frequency Circle PYQ

                    ### Theorem 1: Tangent-Secant Intersecting Chords
                    If two secants PAB and PCD intersect outside a circle at point P:
                    PA · PB = PC · PD
                    If PT is a tangent from P:
                    PT² = PA · PB

                    ### Theorem 2: Direct Common Tangent (DCT) vs Transverse Common Tangent (TCT)
                    Let centers be separated by distance d with radii r1 and r2:
                    * Length of Direct Common Tangent = sqrt(d² - (r1 - r2)²)
                    * Length of Transverse Common Tangent = sqrt(d² - (r1 + r2)²)

                    ### SSC CGL 2024 Tier-2 Solved PYQ:
                    Problem: Two circles with radii 9 cm and 4 cm have centers 13 cm apart. Find length of transverse common tangent.
                    Solution:
                    d = 13 cm, r1 = 9 cm, r2 = 4 cm
                    TCT = sqrt(13² - (9 + 4)²) = sqrt(169 - 169) = 0 cm (Circles touch each other externally!).
                    Direct Common Tangent = sqrt(13² - (9 - 4)²) = sqrt(169 - 25) = sqrt(144) = 12 cm. Instant 3 marks!
                """.trimIndent(),
                type = "PYQ",
                subject = "Mathematics",
                targetExam = "SSC CGL",
                author = "Ankit Rajak",
                imageUrl = "https://images.unsplash.com/photo-1509228468518-180dd4864904?w=1600&auto=format&fit=crop&q=90",
                tags = "SSC CGL, Geometry, PYQ, Circles, Quant, Past Papers",
                readTimeMinutes = 14,
                isBookmarked = false,
                downloadsCount = 8920,
                rating = 4.96f
            ),
            LibraryMaterialEntity(
                id = "mat_11",
                title = "NEET Biology Past 10-Year Question Bank: Human Physiology & Endocrine System",
                description = "NCERT-line-by-line past questions with assertion-reason explanations and hormone feedback diagrams.",
                content = """
                    # NEET Past 10-Year Exam Question Bank: Human Physiology

                    ### High-Yield NCERT Exam Questions:
                    
                    Q1 (NEET 2022): Which hormone stimulates the secretion of gastric juice?
                    Ans: Gastrin (secreted by G-cells of pyloric antrum).

                    Q2 (NEET 2023 Assertion-Reason):
                    Assertion: ADH (Vasopressin) facilitates water reabsorption from DCT and collecting duct.
                    Reason: Alcohol intake inhibits ADH release causing diuresis and dehydration.
                    Ans: Both Assertion and Reason are true, and Reason explains clinical dehydration during hangover!

                    ### Hypothalamic-Pituitary Feedback Summary:
                    * TRH -> TSH -> T3/T4 (Inhibits TSH via negative feedback).
                    * GnRH -> LH & FSH (LH stimulates Leydig cells in males, ovulation in females).
                    * Prolactin stimulates lactation; Oxytocin stimulates milk ejection (Neuroendocrine reflex).
                """.trimIndent(),
                type = "PYQ",
                subject = "Biology",
                targetExam = "NEET",
                author = "ANKIT AI Medical Editorial",
                imageUrl = "https://images.unsplash.com/photo-1576086213369-97a306d36557?w=1600&auto=format&fit=crop&q=90",
                tags = "NEET, Biology, PYQ, Physiology, Past Papers, Medical",
                readTimeMinutes = 13,
                isBookmarked = true,
                downloadsCount = 6780,
                rating = 4.98f
            ),
            LibraryMaterialEntity(
                id = "mat_12",
                title = "Curated Study Material: Indian History Modern Freedom Struggle Timeline (1857–1947)",
                description = "Chronological roadmap with Governor Generals, Acts, Satyagrahas, and revolutionary movements.",
                content = """
                    # Modern Indian History: Definitive Freedom Struggle Roadmap

                    ### Key Milestones:
                    * 1857: First War of Independence / Revolt of 1857.
                    * 1885: Formation of Indian National Congress (A.O. Hume, W.C. Bonnerjee presiding).
                    * 1905: Partition of Bengal (Lord Curzon) & Swadeshi Movement launch.
                    * 1907: Surat Split between Moderates (Gokhale) and Extremists (Tilak, Bipin Chandra Pal, Lala Lajpat Rai).
                    * 1916: Lucknow Pact (Congress & Muslim League joint session; Tilak declaration 'Swaraj is my birthright').
                    * 1919: Rowlatt Act & Jallianwala Bagh Massacre (General Dyer; Rabindranath Tagore renounced Knighthood).
                    * 1920–1922: Non-Cooperation Movement (called off after Chauri Chaura incident in Feb 1922).
                    * 1930: Dandi March & Civil Disobedience Movement (Salt Satyagraha, 12 March to 6 April 1930).
                    * 1942: Quit India Movement ('Do or Die' call by Mahatma Gandhi).
                    * 1947: Indian Independence Act (3 June Mountbatten Plan).
                """.trimIndent(),
                type = "Study Material",
                subject = "Polity & GS",
                targetExam = "UPSC / SSC / State PSC",
                author = "Ankit Rajak (ANKIT AI)",
                imageUrl = "https://images.unsplash.com/photo-1461360370896-922624d12aa1?w=1600&auto=format&fit=crop&q=90",
                tags = "History, Modern History, Freedom Struggle, UPSC, Notes",
                readTimeMinutes = 15,
                isBookmarked = false,
                downloadsCount = 4550,
                rating = 4.93f
            )
        )

        dao.insertMaterials(initialData)
    }
}
