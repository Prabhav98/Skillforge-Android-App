package com.app.skillforge.models

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("categories")
    val categories: List<Category>
)

data class Category(
    @SerializedName("name")
    val name: String,
    @SerializedName("courses")
    val courses: List<Course>
)

data class Course(
    @SerializedName("title")
    val title: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("durationHours")
    val durationHours: Double,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String,
    @SerializedName("level")
    val level: String,
    @SerializedName("instructor")
    val instructor: Instructor,
    @SerializedName("lessons")
    val lessons: List<Lesson>,
    @SerializedName("reviewsCount")
    val reviewsCount: Int = 0,
    @SerializedName("tags")
    val tags: List<String> = emptyList(),
    @SerializedName("description")
    val description: String = "",
    @SerializedName("price")
    val price: String = "Free"
)

data class Instructor(
    @SerializedName("name")
    val name: String,
    @SerializedName("avatarUrl")
    val avatarUrl: String,
    @SerializedName("title")
    val title: String
)

data class Lesson(
    @SerializedName("title")
    val title: String,
    @SerializedName("durationMinutes")
    val durationMinutes: Int,
    @SerializedName("isFree")
    val isFree: Boolean,
    @SerializedName("content")
    val content: String = ""
)