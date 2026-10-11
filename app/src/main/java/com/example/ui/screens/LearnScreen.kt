package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.automirrored.filled.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.data.local.LibraryMaterialEntity
import com.example.ui.components.CyberBadge
import com.example.ui.components.GlassCard
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.ScreenDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val materials by viewModel.libraryMaterials.collectAsState()
    val searchQuery by viewModel.librarySearchQuery.collectAsState()
    val selectedType by viewModel.libraryTypeFilter.collectAsState()
    val selectedSubject by viewModel.librarySubjectFilter.collectAsState()
    val onlyBookmarked by viewModel.libraryOnlyBookmarked.collectAsState()
    val selectedMaterialForReading by viewModel.selectedMaterialForReading.collectAsState()

    var showCreateNoteDialog by remember { mutableStateOf(false) }

    val selectedExam by viewModel.libraryExamFilter.collectAsState()
    val sortBy by viewModel.librarySortBy.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }

    val materialTypes = listOf("All", "PYQ", "Study Material", "Notes", "Curated Article", "Formulas")
    val subjects = listOf("All", "Physics", "Chemistry", "Mathematics", "Polity & GS", "Biology", "Reasoning")
    val examFilters = listOf("All", "JEE", "NEET", "UPSC", "SSC CGL", "Banking")
    val sortOptions = listOf("Latest", "Top Rated", "Most Read", "Fast Read")

    val quickSearchKeywords = listOf(
        "PYQ" to "PYQ Past Papers",
        "Formulas" to "Formula Sheets",
        "JEE" to "JEE Advanced",
        "NEET" to "NEET Medical",
        "UPSC" to "UPSC Prelims",
        "SSC" to "SSC CGL Quant",
        "Calculus" to "Integral Calculus",
        "Modern Physics" to "Quantum & Atoms",
        "Constitution" to "Polity Judgments"
    )

    val activeFilterCount = (if (selectedType != "All") 1 else 0) +
            (if (selectedSubject != "All") 1 else 0) +
            (if (selectedExam != "All") 1 else 0) +
            (if (onlyBookmarked) 1 else 0) +
            (if (sortBy != "Latest") 1 else 0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpaceDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.linearGradient(listOf(NeonCyan, ElectricBlue))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = SpaceDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SMART LIBRARY",
                        color = LightWhite,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Curated Study Materials, Past Exam Papers & Short Notes",
                    color = SoftText,
                    fontSize = 11.sp
                )
            }

            // Create Note Button
            Button(
                onClick = { showCreateNoteDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("create_note_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = SpaceDark, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Add Note", color = SpaceDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Primary Search Bar with Integrated Clear & Filter Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.setLibrarySearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Search PYQ, past papers, formulas, topics...",
                        color = SoftText,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = if (searchQuery.isNotEmpty()) NeonCyan else SoftText
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.setLibrarySearchQuery("") },
                            modifier = Modifier.testTag("clear_search_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search", tint = LightWhite)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .testTag("library_search_input"),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = SpaceCard,
                    unfocusedContainerColor = SpaceCard,
                    focusedTextColor = LightWhite,
                    unfocusedTextColor = LightWhite,
                    focusedIndicatorColor = NeonCyan,
                    unfocusedIndicatorColor = SpaceCardBorder
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Bookmark Quick Toggle Button
            IconButton(
                onClick = { viewModel.toggleLibraryOnlyBookmarked() },
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (onlyBookmarked) HologramGold.copy(alpha = 0.2f) else SpaceCard)
                    .border(
                        1.dp,
                        if (onlyBookmarked) HologramGold else SpaceCardBorder,
                        RoundedCornerShape(12.dp)
                    )
                    .testTag("library_bookmark_toggle")
            ) {
                Icon(
                    imageVector = if (onlyBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Filter Saved",
                    tint = if (onlyBookmarked) HologramGold else SoftText
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Topic Keywords / Instant Search Pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(quickSearchKeywords) { (keyword, displayLabel) ->
                val isSelected = searchQuery.equals(keyword, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ElectricBlue.copy(alpha = 0.3f) else SpaceCardElevated)
                        .border(
                            1.dp,
                            if (isSelected) ElectricBlue else SpaceCardBorder.copy(alpha = 0.6f),
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            if (isSelected) {
                                viewModel.setLibrarySearchQuery("")
                            } else {
                                viewModel.setLibrarySearchQuery(keyword)
                            }
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (keyword == "PYQ") Icons.Default.Quiz else Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = if (isSelected) NeonCyan else SoftText,
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = displayLabel,
                            color = if (isSelected) LightWhite else SoftText,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Material Type Tabs (All, PYQ, Study Material, Notes, Curated Article, Formulas)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(materialTypes) { type ->
                val isSelected = selectedType == type
                val chipColor = when (type) {
                    "PYQ" -> FlameAmber
                    "Study Material" -> ElectricBlue
                    "Notes" -> CyberViolet
                    "Formulas" -> HologramGold
                    else -> NeonCyan
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) chipColor else SpaceCard)
                        .border(1.dp, if (isSelected) chipColor else SpaceCardBorder, RoundedCornerShape(12.dp))
                        .clickable { viewModel.setLibraryTypeFilter(type) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (type == "PYQ") {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (isSelected) SpaceDark else FlameAmber,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = type,
                            color = if (isSelected) SpaceDark else LightWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Target Exam Filter Row (All, JEE, NEET, UPSC, SSC CGL, Banking)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Target Exam:",
                color = SoftText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 6.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(examFilters) { exam ->
                    val isSelected = selectedExam == exam
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CyberViolet else SpaceDark)
                            .border(1.dp, if (isSelected) CyberViolet else SpaceCardBorder, RoundedCornerShape(8.dp))
                            .clickable { viewModel.setLibraryExamFilter(exam) }
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = exam,
                            color = if (isSelected) Color.White else SoftText,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Subject Filter & Sort Selector Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Subject:",
                color = SoftText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 6.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(subjects) { subj ->
                    val isSelected = selectedSubject == subj
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.25f) else SpaceDark)
                            .border(1.dp, if (isSelected) NeonCyan else SpaceCardBorder, RoundedCornerShape(8.dp))
                            .clickable { viewModel.setLibrarySubjectFilter(subj) }
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = subj,
                            color = if (isSelected) NeonCyan else SoftText,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Active Filter Indicators & Sort Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${materials.size} items found",
                    color = LightWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                if (activeFilterCount > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonCyan.copy(alpha = 0.15f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "$activeFilterCount filtered",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Reset filters link if any filter is active
            if (activeFilterCount > 0 || searchQuery.isNotEmpty()) {
                Text(
                    text = "Clear All Filters",
                    color = AlertRed,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { viewModel.clearAllLibraryFilters() }
                        .padding(4.dp)
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(CyberGreen)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Curated & Verified",
                        color = CyberGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Materials List
        if (materials.isEmpty()) {
            EmptyLibraryState(
                query = searchQuery,
                onClear = { viewModel.clearAllLibraryFilters() }
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(materials, key = { it.id }) { item ->
                    LibraryMaterialCard(
                        material = item,
                        onClick = { viewModel.openMaterialReader(item) },
                        onToggleBookmark = { viewModel.toggleBookmark(item) }
                    )
                }
            }
        }
    }

    // Full Reading View Dialog
    selectedMaterialForReading?.let { material ->
        MaterialReaderDialog(
            material = material,
            onClose = { viewModel.closeMaterialReader() },
            onToggleBookmark = { viewModel.toggleBookmark(material) },
            onAskAi = {
                viewModel.closeMaterialReader()
                viewModel.sendChatQuery("Please explain this concept from the library: ${material.title}", mode = "step_by_step")
                viewModel.navigateTo(ScreenDestination.AnkitAi)
            },
            onMarkRevised = {
                viewModel.repository.awardXp(30)
            },
            onDelete = {
                viewModel.deleteStudyMaterial(material.id)
            }
        )
    }

    // Create Note Dialog
    if (showCreateNoteDialog) {
        CreateNoteDialog(
            onDismiss = { showCreateNoteDialog = false },
            onSave = { title, desc, content, type, subj, exam, imgUrl ->
                viewModel.addCustomStudyMaterial(title, desc, content, type, subj, exam, imgUrl)
                showCreateNoteDialog = false
            }
        )
    }
}

@Composable
fun LibraryMaterialCard(
    material: LibraryMaterialEntity,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    val typeBadgeColor = when (material.type) {
        "Curated Article" -> NeonCyan
        "Notes" -> CyberViolet
        "Formulas" -> HologramGold
        "Study Material" -> ElectricBlue
        "PYQ" -> FlameAmber
        else -> CyberGreen
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SpaceCardBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("library_card_${material.id}"),
        colors = CardDefaults.cardColors(containerColor = SpaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        val context = LocalContext.current
        val imageRequest = remember(material.imageUrl) {
            ImageRequest.Builder(context)
                .data(material.imageUrl)
                .crossfade(true)
                .crossfade(300)
                .allowHardware(true)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()
        }

        Column {
            // Coil Thumbnail Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(SpaceCardElevated)
            ) {
                SubcomposeAsyncImage(
                    model = imageRequest,
                    contentDescription = material.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.linearGradient(listOf(SpaceCardElevated, SpaceCard))),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = NeonCyan,
                                strokeWidth = 2.dp
                            )
                        }
                    },
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.linearGradient(listOf(SpaceCardElevated, SpaceDark))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, tint = SoftText, modifier = Modifier.size(36.dp))
                        }
                    }
                )

                // Dark gradient overlay for bottom text contrast
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xCC090D16))
                            )
                        )
                )

                // Type Badge on Top Left
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                ) {
                    CyberBadge(text = material.type.uppercase(), color = typeBadgeColor)
                }

                // Bookmark Button on Top Right
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0x99090D16))
                ) {
                    Icon(
                        imageVector = if (material.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (material.isBookmarked) HologramGold else LightWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Bottom strip on image
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${material.subject} • ${material.targetExam}",
                        color = LightWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "⏱️ ${material.readTimeMinutes} min read",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Card Body
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = material.title,
                    color = LightWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = material.description,
                    color = SoftText,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                HorizontalDivider(color = SpaceCardBorder.copy(alpha = 0.5f))

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "By ${material.author}",
                        color = SoftText,
                        fontSize = 11.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐ ${material.rating}", color = HologramGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Read Now →",
                            color = NeonCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MaterialReaderDialog(
    material: LibraryMaterialEntity,
    onClose: () -> Unit,
    onToggleBookmark: () -> Unit,
    onAskAi: () -> Unit,
    onMarkRevised: () -> Unit,
    onDelete: () -> Unit
) {
    var hasRevised by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(SpaceDark),
            color = SpaceDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(SpaceCard)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close", tint = LightWhite)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onToggleBookmark,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SpaceCard)
                        ) {
                            Icon(
                                imageVector = if (material.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = if (material.isBookmarked) HologramGold else LightWhite
                            )
                        }

                        if (material.id.startsWith("custom_")) {
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = onDelete,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SpaceCard)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = AlertRed)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Article Content
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Hero Image Banner via Coil
                    item {
                        val context = LocalContext.current
                        val detailImageRequest = remember(material.imageUrl) {
                            ImageRequest.Builder(context)
                                .data(material.imageUrl)
                                .crossfade(true)
                                .crossfade(300)
                                .allowHardware(true)
                                .diskCachePolicy(CachePolicy.ENABLED)
                                .memoryCachePolicy(CachePolicy.ENABLED)
                                .build()
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(1.2.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        ) {
                            SubcomposeAsyncImage(
                                model = detailImageRequest,
                                contentDescription = material.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(SpaceCardElevated),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(28.dp),
                                            color = NeonCyan,
                                            strokeWidth = 2.dp
                                        )
                                    }
                                },
                                error = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(SpaceCardElevated),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.MenuBook,
                                            contentDescription = null,
                                            tint = SoftText,
                                            modifier = Modifier.size(40.dp)
                                        )
                                    }
                                }
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color(0xB3090D16))
                                        )
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(12.dp)
                            ) {
                                CyberBadge(text = "${material.type.uppercase()} • ${material.subject.uppercase()}", color = NeonCyan)
                            }
                        }
                    }

                    // Title & Metadata
                    item {
                        Text(
                            text = material.title,
                            color = LightWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "✍️ ${material.author}", color = SoftText, fontSize = 12.sp)
                            Text(text = "•", color = SoftText, fontSize = 12.sp)
                            Text(text = "⏱️ ${material.readTimeMinutes} min", color = NeonCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text(text = "•", color = SoftText, fontSize = 12.sp)
                            Text(text = "🎯 ${material.targetExam}", color = HologramGold, fontSize = 12.sp)
                        }
                    }

                    // Content Body Box
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(SpaceCard)
                                .border(1.dp, SpaceCardBorder, RoundedCornerShape(14.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = material.content,
                                color = LightWhite,
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    // Bottom Pedagogical Action Buttons
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    hasRevised = true
                                    onMarkRevised()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (hasRevised) CyberGreen else SpaceCardBorder
                                )
                            ) {
                                Text(
                                    text = if (hasRevised) "✓ Revised (+30 XP)" else "Mark Revised",
                                    color = if (hasRevised) SpaceDark else LightWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            Button(
                                onClick = onAskAi,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SpaceDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Ask ANKIT AI",
                                    color = SpaceDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateNoteDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, desc: String, content: String, type: String, subj: String, exam: String, imgUrl: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Notes") }
    var selectedSubject by remember { mutableStateOf("Physics") }
    var imageUrl by remember { mutableStateOf("") }

    val types = listOf("Notes", "Study Material", "Formulas", "Curated Article", "PYQ")
    val subjects = listOf("Physics", "Chemistry", "Mathematics", "Polity & GS", "Biology", "Reasoning")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Add to Local Library (Room DB)", color = LightWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(text = "Title *", color = SoftText, fontSize = 11.sp)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("e.g. Thermodynamics Carnot Engine Notes", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = LightWhite,
                            unfocusedTextColor = LightWhite,
                            focusedBorderColor = NeonCyan
                        ),
                        singleLine = true
                    )
                }

                item {
                    Text(text = "Subject", color = SoftText, fontSize = 11.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(subjects) { s ->
                            val sel = selectedSubject == s
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (sel) NeonCyan else SpaceCard)
                                    .clickable { selectedSubject = s }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(text = s, color = if (sel) SpaceDark else SoftText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text(text = "Material Type", color = SoftText, fontSize = 11.sp)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(types) { t ->
                            val sel = selectedType == t
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (sel) CyberViolet else SpaceCard)
                                    .clickable { selectedType = t }
                                    .padding(horizontal = 8.dp, vertical = 5.dp)
                            ) {
                                Text(text = t, color = if (sel) Color.White else SoftText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text(text = "Short Summary", color = SoftText, fontSize = 11.sp)
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Key formulas, derivation steps, and test warnings", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = LightWhite,
                            unfocusedTextColor = LightWhite,
                            focusedBorderColor = NeonCyan
                        ),
                        singleLine = true
                    )
                }

                item {
                    Text(text = "Full Note Content *", color = SoftText, fontSize = 11.sp)
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Write formulas, definitions, numbered steps...", fontSize = 12.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = LightWhite,
                            unfocusedTextColor = LightWhite,
                            focusedBorderColor = NeonCyan
                        )
                    )
                }

                item {
                    Text(text = "Cover Image URL (Optional - Coil Supported)", color = SoftText, fontSize = 11.sp)
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        placeholder = { Text("https://images.unsplash.com/...", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = LightWhite,
                            unfocusedTextColor = LightWhite,
                            focusedBorderColor = NeonCyan
                        ),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        onSave(title, description.ifBlank { title }, content, selectedType, selectedSubject, "All Exams", imageUrl)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Save to Room DB", color = SpaceDark, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = SoftText)
            }
        },
        containerColor = SpaceCardElevated
    )
}

@Composable
fun EmptyLibraryState(
    query: String,
    onClear: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(SpaceCard)
                .border(1.dp, SpaceCardBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SearchOff, contentDescription = null, tint = SoftText, modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = if (query.isNotBlank()) "No study materials match \"$query\"" else "No materials found in this category",
            color = LightWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Try adjusting your search keywords, material type, or subject filters.",
            color = SoftText,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClear,
            colors = ButtonDefaults.buttonColors(containerColor = SpaceCardElevated),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(brush = Brush.horizontalGradient(listOf(NeonCyan, ElectricBlue)))
        ) {
            Text("Reset Search & Filters", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}
