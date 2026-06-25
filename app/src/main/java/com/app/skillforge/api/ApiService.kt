package com.app.skillforge.api

import com.app.skillforge.models.ApiResponse
import retrofit2.http.GET

interface ApiService {
    @GET("android-assesment/notes/refs/heads/main/data.json")
    suspend fun getData(): ApiResponse
}