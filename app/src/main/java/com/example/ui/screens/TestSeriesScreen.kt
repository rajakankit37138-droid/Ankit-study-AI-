package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.MockTestItem
import com.example.data.models.TestSeriesPackage
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestSeriesScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    BackHandler { viewModel.navigateTo(ScreenDestination.Home) }

    val packages by viewModel.testSeriesPackages.collectAsState()
    val mockTests by viewModel.mockTests.collectAsState()
    val isCouponApplied by viewModel.isCouponVian12Applied.collectAsState()
    val couponMessage by viewModel.couponValidationMessage.collectAsState()
    val slotsClaimed by viewModel.discountSlotsClaimed.collectAsState()
    val enrolledPackages by viewModel.enrolledPackageIds.collectAsState()

    var couponInput by remember { mutableStateOf("VIAN12") }
    var selectedTab by remember { mutableStateOf(0) } // 0: Packages, 1: Live Mock Tests
    var selectedPackageForEnroll by remember { mutableStateOf<TestSeriesPackage?>(null) }
    var activeTestForAttempt by remember { mutableStateOf<MockTestItem?>(null) }
    var testResultDialog by remember { mutableStateOf<Pair<Int, Float>?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(ScreenDestination.Home) },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SpaceCard)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = NeonCyan
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "GOVERNMENT EXAM TEST SERIES",
                    color = LightWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Starting from ₹99 • Use VIAN12 for ₹49 (First 500 Students)",
                    color = NeonCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            // 4K Ultra HD Govt Exam Arena Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .border(1.2.dp, HologramGold.copy(alpha = 0.55f), RoundedCornerShape(18.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_test_series_hero),
                        contentDescription = "Government Exam Test Series Arena",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        SpaceDark.copy(alpha = 0.65f),
                                        SpaceDark.copy(alpha = 0.95f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CyberBadge(text = "4K TEST ARENA", color = HologramGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ALL INDIA RANKING",
                                color = NeonCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Crack SSC • Banking • Railway • UPSC • Defence",
                            color = LightWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Real exam simulation with instant AI negative marking analysis",
                            color = SoftText,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Limited Offer Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF1E1035),
                                    Color(0xFF0F1B38),
                                    Color(0xFF0B242C)
                                )
                            )
                        )
                        .border(1.5.dp, HologramGold.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CyberBadge(text = "LIMITED LAUNCH OFFER", color = HologramGold)
                            Text(
                                text = "🔥 51% OFF",
                                color = AlertRed,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "All Government Exam Test Series @ ₹99",
                            color = LightWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Apply Token 'VIAN12' & Get any series for ONLY ₹49!",
                            color = NeonCyan,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Slots Claimed Progress Bar (max 500 students)
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "First 500 Students Special Slot:",
                                    color = SoftText,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = "$slotsClaimed / 500 Claimed (${500 - slotsClaimed} left!)",
                                    color = HologramGold,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { slotsClaimed / 500f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = HologramGold,
                                trackColor = SpaceCardBorder
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Interactive Token Input Box
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = couponInput,
                                onValueChange = { couponInput = it.uppercase() },
                                placeholder = { Text("Enter token (e.g. VIAN12)", fontSize = 12.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("coupon_input_field"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = if (isCouponApplied) CyberGreen else NeonCyan,
                                    unfocusedBorderColor = SpaceCardBorder,
                                    focusedTextColor = LightWhite,
                                    unfocusedTextColor = LightWhite
                                ),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (isCouponApplied) {
                                        viewModel.removeCouponCode()
                                    } else {
                                        viewModel.applyCouponCode(couponInput)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCouponApplied) CyberGreen else NeonCyan
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("apply_coupon_button")
                            ) {
                                Text(
                                    text = if (isCouponApplied) "Applied ✓" else "Apply",
                                    color = SpaceDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        couponMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = msg,
                                color = if (isCouponApplied) CyberGreen else AlertRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Tab Selector
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SpaceDark,
                    contentColor = NeonCyan,
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("📦 Test Packages (${packages.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("📝 Live Mock Tests (${mockTests.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }
            }

            if (selectedTab == 0) {
                // Test Packages List
                items(packages) { pkg ->
                    val isEnrolled = enrolledPackages.contains(pkg.id)
                    val effectivePrice = if (isCouponApplied) pkg.discountedPrice else pkg.originalPrice

                    GlassCard(
                        borderColor = if (isEnrolled) CyberGreen.copy(alpha = 0.6f) else SpaceCardBorder,
                        backgroundColor = SpaceCard
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CyberBadge(text = pkg.category.uppercase(), color = NeonCyan)
                            if (isEnrolled) {
                                CyberBadge(text = "UNLOCKED ✓", color = CyberGreen)
                            } else {
                                CyberBadge(text = "LIMITED 500 SLOTS", color = HologramGold)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = pkg.title,
                            color = LightWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Exams: ${pkg.examCoverage}",
                            color = SoftText,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats Grid (Tests counts)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SpaceDark)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            TestStatCol(label = "Total Tests", value = "${pkg.totalTests}+")
                            TestStatCol(label = "Full Mocks", value = "${pkg.fullLengthMocks}")
                            TestStatCol(label = "Chapter Tests", value = "${pkg.chapterTests}")
                            TestStatCol(label = "PYQ Papers", value = "${pkg.previousYearPapers}")
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Feature Bullets
                        pkg.features.forEach { feat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = feat, color = LightWhite, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = SpaceCardBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Price and Action Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "₹$effectivePrice",
                                        color = if (isCouponApplied) CyberGreen else LightWhite,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    if (isCouponApplied) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "₹${pkg.originalPrice}",
                                            color = SoftText,
                                            fontSize = 13.sp,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "(VIAN12 Applied)",
                                            color = HologramGold,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = if (isCouponApplied) "₹50 Saved with Token VIAN12" else "Standard Price: ₹99",
                                    color = SoftText,
                                    fontSize = 11.sp
                                )
                            }

                            if (isEnrolled) {
                                Button(
                                    onClick = { selectedTab = 1 },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberGreen),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Start Mock Tests →", color = SpaceDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            } else {
                                Button(
                                    onClick = { selectedPackageForEnroll = pkg },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("enroll_button_${pkg.id}")
                                ) {
                                    Text(
                                        text = "Unlock for ₹$effectivePrice",
                                        color = SpaceDark,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Live Mock Tests List
                items(mockTests) { testItem ->
                    val isUnlocked = enrolledPackages.isNotEmpty() // unlocked if enrolled in any package

                    GlassCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CyberBadge(text = testItem.examCategory, color = CyberViolet)
                            Text(
                                text = "⏱️ ${testItem.durationMinutes} Mins • ${testItem.totalQuestions} Qs",
                                color = SoftText,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = testItem.title,
                            color = LightWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total Marks: ${testItem.totalMarks}", color = SoftText, fontSize = 11.sp)
                            Text(text = "Attempts: ${testItem.totalAttempts}", color = SoftText, fontSize = 11.sp)
                            Text(text = "High Score: ${testItem.highestScore}", color = HologramGold, fontSize = 11.sp)
                        }

                        if (testItem.isAttempted) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CyberGreen.copy(alpha = 0.15f))
                                    .padding(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Your Score: ${testItem.studentScore}/${testItem.totalMarks}", color = CyberGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(text = "Percentile: ${testItem.studentPercentile}%", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (isUnlocked) {
                                Button(
                                    onClick = { activeTestForAttempt = testItem },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                                ) {
                                    Text(if (testItem.isAttempted) "Re-Attempt Test" else "Start Test →", color = SpaceDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            } else {
                                Button(
                                    onClick = { selectedTab = 0 },
                                    colors = ButtonDefaults.buttonColors(containerColor = SpaceCardBorder)
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = HologramGold, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Unlock with Series (₹49)", color = LightWhite, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Checkout / Unlock Confirmation Dialog
    selectedPackageForEnroll?.let { pkg ->
        val effectivePrice = if (isCouponApplied) pkg.discountedPrice else pkg.originalPrice

        AlertDialog(
            onDismissRequest = { selectedPackageForEnroll = null },
            title = {
                Text(text = "Unlock Test Series", color = LightWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(text = pkg.title, color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Complete access to ${pkg.totalTests}+ tests with All-India ranking and AI analysis.", color = SoftText, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = SpaceCardBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Standard Test Series Price:", color = SoftText, fontSize = 12.sp)
                        Text("₹${pkg.originalPrice}", color = LightWhite, fontSize = 12.sp)
                    }

                    if (isCouponApplied) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Token VIAN12 Discount:", color = CyberGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("-₹50", color = CyberGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Net Payable:", color = LightWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("₹$effectivePrice", color = if (isCouponApplied) CyberGreen else NeonCyan, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "⚡ Offer valid for first 500 students only! (${500 - slotsClaimed} slots left)",
                        color = HologramGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.enrollInTestSeries(pkg.id)
                        selectedPackageForEnroll = null
                        selectedTab = 1 // Switch to mock tests
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("Pay ₹$effectivePrice & Unlock", color = SpaceDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedPackageForEnroll = null }) {
                    Text("Cancel", color = SoftText)
                }
            },
            containerColor = SpaceCardElevated
        )
    }

    // Active Mock Test Simulation Dialog
    activeTestForAttempt?.let { test ->
        MockTestSimulatorDialog(
            test = test,
            onClose = { activeTestForAttempt = null },
            onSubmit = { score, percentile ->
                viewModel.recordMockTestAttempt(test.id, score, percentile)
                activeTestForAttempt = null
                testResultDialog = score to percentile
            }
        )
    }

    // Result Celebration Dialog
    testResultDialog?.let { (score, percentile) ->
        AlertDialog(
            onDismissRequest = { testResultDialog = null },
            title = {
                Text(text = "🎉 Test Result & AI Analysis", color = CyberGreen, fontWeight = FontWeight.Bold)
            },
            text = {
                Column {
                    Text(text = "Your Score: $score Marks", color = LightWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "All-India Percentile: $percentile%", color = NeonCyan, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ANKIT AI Analysis: Outstanding accuracy in General Studies & Reasoning! Focus on speed in numerical puzzles to push percentile above 98%. +50 XP Awarded!",
                        color = SoftText,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { testResultDialog = null },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                ) {
                    Text("View Full Analysis", color = SpaceDark, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SpaceCardElevated
        )
    }
}

@Composable
fun TestStatCol(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = SoftText, fontSize = 10.sp)
    }
}

@Composable
fun MockTestSimulatorDialog(
    test: MockTestItem,
    onClose: () -> Unit,
    onSubmit: (score: Int, percentile: Float) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var currentQ by remember { mutableStateOf(1) }

    val sampleQuestion = "Which constitutional amendment introduced the Goods and Services Tax (GST) in India?"
    val sampleOptions = listOf("99th Amendment Act", "101st Amendment Act, 2016", "103rd Amendment Act", "105th Amendment Act")
    val correctIndex = 1

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Live Mock Test", color = LightWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                CyberBadge(text = "⏱️ 59:45 remaining", color = AlertRed)
            }
        },
        text = {
            Column {
                Text(text = "Question $currentQ of ${test.totalQuestions}", color = SoftText, fontSize = 11.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = sampleQuestion, color = LightWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(12.dp))

                sampleOptions.forEachIndexed { idx, opt ->
                    val isSelected = selectedOption == idx
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) ElectricBlue.copy(alpha = 0.35f) else SpaceDark)
                            .border(1.dp, if (isSelected) NeonCyan else SpaceCardBorder, RoundedCornerShape(8.dp))
                            .clickable { selectedOption = idx }
                            .padding(10.dp)
                    ) {
                        Text(text = "${('A' + idx)}. $opt", color = LightWhite, fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val score = if (selectedOption == correctIndex) 168 else 142
                    val percentile = if (selectedOption == correctIndex) 96.8f else 91.2f
                    onSubmit(score, percentile)
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberGreen)
            ) {
                Text("Submit Test", color = SpaceDark, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Exit Test", color = SoftText)
            }
        },
        containerColor = SpaceCardElevated
    )
}
