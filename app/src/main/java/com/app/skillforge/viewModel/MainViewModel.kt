package com.app.skillforge.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.skillforge.api.RetrofitClient
import com.app.skillforge.models.ApiResponse
import com.app.skillforge.models.Category
import com.app.skillforge.models.Course
import com.app.skillforge.models.Lesson
import com.app.skillforge.repository.Repository
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: ApiResponse) : UiState()
    data class Error(val message: String) : UiState()
}

class MainViewModel(private val repository: Repository = Repository(RetrofitClient.api)) : ViewModel() {
    private val _state = MutableLiveData<UiState>(UiState.Loading)
    val state: LiveData<UiState> = _state

    init {
        loadData()
    }

    fun loadData() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            val result = repository.fetchData()
            result.onSuccess { data ->
                _state.value = UiState.Success(data)
            }.onFailure { error ->
                _state.value = UiState.Error(error.message ?: "Something went wrong")
            }
        }
    }

    fun getCategories(): List<Category> {
        val s = _state.value
        return if (s is UiState.Success) s.data.categories else emptyList()
    }

    fun getCourse(categoryIndex: Int, courseIndex: Int): Course? {
        val categories = getCategories()
        return categories.getOrNull(categoryIndex)?.courses?.getOrNull(courseIndex)
    }

    fun findCourseIndices(course: Course): Pair<Int, Int>? {
        val categories = getCategories()
        for ((catIdx, category) in categories.withIndex()) {
            for ((courseIdx, c) in category.courses.withIndex()) {
                if (c.title == course.title) return Pair(catIdx, courseIdx)
            }
        }
        return null
    }
}