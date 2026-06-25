package com.app.skillforge.repository

import com.app.skillforge.api.ApiService
import com.app.skillforge.models.ApiResponse

class Repository(private val api: ApiService) {

    suspend fun fetchData(): Result<ApiResponse> {
        return try {
            val response = api.getData()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}