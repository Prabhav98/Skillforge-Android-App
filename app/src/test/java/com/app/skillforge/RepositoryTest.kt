package com.app.skillforge

import com.app.skillforge.api.ApiService
import com.app.skillforge.models.ApiResponse
import com.app.skillforge.models.Category
import com.app.skillforge.models.Course
import com.app.skillforge.models.Instructor
import com.app.skillforge.models.Lesson
import com.app.skillforge.repository.Repository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RepositoryTest {

    // A fake API that returns hardcoded data — no internet needed
    private val fakeApi = object : ApiService {
        override suspend fun getData(): ApiResponse {
            return ApiResponse(
                categories = listOf(
                    Category(
                        name = "Android Development",
                        courses = listOf(
                            Course(
                                title = "Kotlin Fundamentals",
                                rating = 4.7,
                                durationHours = 6.5,
                                thumbnailUrl = "",
                                level = "Beginner",
                                instructor = Instructor("Aarav Sharma", "", "Senior Engineer"),
                                lessons = listOf(
                                    Lesson("Welcome & Setup", 8, true, ""),
                                    Lesson("Variables", 15, true, ""),
                                    Lesson("Functions", 18, false, "")
                                )
                            )
                        )
                    )
                )
            )
        }
    }

    @Test
    fun `repository returns correct data from api`() = runTest {
        // Arrange — create the repo with our fake API
        val repository = Repository(fakeApi)

        // Act — call the function
        val result = repository.fetchData()

        // Assert — check the results
        assertTrue(result.isSuccess)
        val data = result.getOrNull()!!
        assertEquals(1, data.categories.size)
        assertEquals("Kotlin Fundamentals", data.categories[0].courses[0].title)
        assertEquals(3, data.categories[0].courses[0].lessons.size)
    }

    @Test
    fun `repository returns failure when api throws exception`() = runTest {
        val failingApi = object : ApiService {
            override suspend fun getData(): ApiResponse {
                throw RuntimeException("No internet")
            }
        }
        val repository = Repository(failingApi)

        val result = repository.fetchData()

        assertTrue(result.isFailure)
        assertEquals("No internet", result.exceptionOrNull()?.message)
    }

    @Test
    fun `lesson is free when isFree is true`() = runTest {
        val repository = Repository(fakeApi)
        val data = repository.fetchData().getOrNull()!!
        val lessons = data.categories[0].courses[0].lessons

        assertTrue(lessons[0].isFree)        // "Welcome & Setup" is free
        assertTrue(lessons[1].isFree)        // "Variables" is free
        assertEquals(false, lessons[2].isFree) // "Functions" is locked
    }

}