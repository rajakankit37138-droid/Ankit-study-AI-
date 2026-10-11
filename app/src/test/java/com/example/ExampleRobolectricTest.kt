package com.example

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ai.AnkitAiEngine
import com.example.data.local.AppDatabase
import com.example.data.local.LibraryMaterialEntity
import com.example.data.repository.StudyRepository
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ANKIT STUDY AI", appName)
    }

    @Test
    fun `verify study repository initial values and XP progression`() {
        val repository = StudyRepository()
        val initialXp = repository.userProfile.value.totalXp

        repository.awardXp(50)
        assertEquals(initialXp + 50, repository.userProfile.value.totalXp)

        val readiness = repository.examReadiness.value
        assertTrue(readiness.overallPercent in 0..100)
        assertNotNull(readiness.nextBestAction)
    }

    @Test
    fun `verify mind map generation hierarchy`() {
        val node = AnkitAiEngine.generateMindMap("Laws of Motion")
        assertEquals("Laws of Motion", node.title)
        assertTrue(node.children.isNotEmpty())
    }

    @Test
    fun `verify study plan schedule rearrangement`() {
        val repository = StudyRepository()
        repository.rearrangeMissedSchedule()
        val rescheduledTasks = repository.studyPlanTasks.value.filter { it.isRescheduled }
        assertTrue(rescheduledTasks.isNotEmpty())
    }

    @Test
    fun `verify Room database insertion and bookmarking`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val dao = db.libraryMaterialDao()

        val item = LibraryMaterialEntity(
            id = "test_1",
            title = "Kinematics Formula Guide",
            description = "Test Description",
            content = "v = u + at",
            type = "Formulas",
            subject = "Physics",
            targetExam = "JEE",
            imageUrl = "https://example.com/image.jpg",
            tags = "Physics, Kinematics"
        )
        dao.insertMaterial(item)

        val retrieved = dao.getMaterialById("test_1")
        assertNotNull(retrieved)
        assertEquals("Kinematics Formula Guide", retrieved?.title)

        dao.updateBookmark("test_1", true)
        val bookmarked = dao.getMaterialById("test_1")
        assertTrue(bookmarked?.isBookmarked == true)

        db.close()
    }

    @Test
    fun `verify token VIAN12 discounts test series from 99 to 49 rupees`() {
        val application = ApplicationProvider.getApplicationContext<Context>() as Application
        val viewModel = MainViewModel(application)

        // Verify packages start with standard price 99 and discounted price 49
        val pkg = viewModel.testSeriesPackages.value.first()
        assertEquals(99, pkg.originalPrice)
        assertEquals(49, pkg.discountedPrice)

        // Apply coupon VIAN12
        viewModel.applyCouponCode("VIAN12")
        assertTrue(viewModel.isCouponVian12Applied.value)

        // Verify invalid coupon does not apply
        viewModel.applyCouponCode("INVALIDCODE")
        assertTrue(!viewModel.isCouponVian12Applied.value)

        // Reapply VIAN12 and verify enrollment & slots (maximum 500)
        viewModel.applyCouponCode("vian12")
        assertTrue(viewModel.isCouponVian12Applied.value)
        viewModel.enrollInTestSeries(pkg.id)
        assertTrue(viewModel.enrolledPackageIds.value.contains(pkg.id))
        assertTrue(viewModel.discountSlotsClaimed.value <= 500)
    }

    @Test
    fun `verify Gemini AI study schedule generation and application`() = runBlocking {
        val schedule = AnkitAiEngine.generateCustomStudySchedule(
            examGoal = "JEE Main & Advanced",
            availableHours = 6,
            prepLevel = "Intermediate",
            focusSubjects = listOf("Physics", "Mathematics"),
            includeRevision = true,
            includeMockSprint = true
        )

        assertTrue(schedule.isNotEmpty())
        assertTrue(schedule.any { it.subject.contains("Physics", ignoreCase = true) })
        assertTrue(schedule.any { it.subject.contains("Revision", ignoreCase = true) })

        val application = ApplicationProvider.getApplicationContext<Context>() as Application
        val viewModel = MainViewModel(application)
        viewModel.generateCustomAiSchedule(
            examGoal = "JEE Main & Advanced",
            hours = 6,
            prepLevel = "Intermediate",
            subjects = listOf("Physics", "Mathematics"),
            includeRevision = true,
            includeMockSprint = true
        )
        viewModel.applyAiScheduleToToday()
    }
}
