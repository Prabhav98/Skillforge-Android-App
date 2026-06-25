package com.app.skillforge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.skillforge.R
import com.app.skillforge.adapters.LessonPlayerAdapter
import com.app.skillforge.databinding.FragmentLessonBinding
import com.app.skillforge.models.Course
import com.app.skillforge.models.Lesson
import com.app.skillforge.viewModel.MainViewModel
import com.app.skillforge.viewModel.UiState
import com.bumptech.glide.Glide

class LessonFragment : Fragment() {

    private var _binding: FragmentLessonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private var categoryIndex = 0
    private var courseIndex = 0
    private var lessonIndex = 0
    private var adapter: LessonPlayerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryIndex = arguments?.getInt("categoryIndex") ?: 0
        courseIndex = arguments?.getInt("courseIndex") ?: 0
        lessonIndex = arguments?.getInt("lessonIndex") ?: 0

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state is UiState.Success) {
                val course = viewModel.getCourse(categoryIndex, courseIndex)
                if (course != null) {
                    bindLesson(course, lessonIndex)
                }
            }
        }
    }

    private fun bindLesson(course: Course, currentLessonIndex: Int) {
        val lesson = course.lessons[currentLessonIndex]

        // Player background
        Glide.with(requireContext())
            .load(course.thumbnailUrl)
            .centerCrop()
            .into(binding.ivPlayerBg)

        // Time display
        val totalSecs = lesson.durationMinutes * 60
        binding.tvTotalTime.text = String.format(
            "%02d:%02d", totalSecs / 60, totalSecs % 60
        )

        // Lesson info
        binding.tvLessonLabel.text = getString(
            R.string.lesson_label_format,
            currentLessonIndex + 1,
            course.title.uppercase()
        )
        binding.tvLessonTitle.text = lesson.title
        binding.tvLessonContent.text = lesson.content.ifEmpty {
            "Set up Android Studio and run your first Kotlin file."
        }

        // Lessons recycler
        adapter = LessonPlayerAdapter(course.lessons, currentLessonIndex) { newIndex ->
            lessonIndex = newIndex
            adapter?.updateCurrentIndex(newIndex)
            updateLessonInfo(course.lessons[newIndex], newIndex, course.title)
        }
        binding.rvLessons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLessons.adapter = adapter
    }

    private fun updateLessonInfo(lesson: Lesson, index: Int, courseTitle: String) {
        binding.tvLessonLabel.text = getString(
            R.string.lesson_label_format,
            index + 1,
            courseTitle.uppercase()
        )
        binding.tvLessonTitle.text = lesson.title
        binding.tvLessonContent.text = lesson.content.ifEmpty {
            "Set up Android Studio and run your first Kotlin file."
        }
        val totalSecs = lesson.durationMinutes * 60
        binding.tvTotalTime.text = String.format(
            "%02d:%02d", totalSecs / 60, totalSecs % 60
        )
        binding.tvCurrentTime.text = "00:00"
        binding.progressPlayer.progress = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}