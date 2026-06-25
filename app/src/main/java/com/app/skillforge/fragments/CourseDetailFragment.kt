package com.app.skillforge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.skillforge.R
import com.app.skillforge.adapters.LessonAdapter
import com.app.skillforge.databinding.FragmentCourseDetailBinding
import com.app.skillforge.models.Course
import com.app.skillforge.viewModel.MainViewModel
import com.app.skillforge.viewModel.UiState

class CourseDetailFragment : Fragment() {
    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var categoryIndex = 0
    private var courseIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryIndex = arguments?.getInt("categoryIndex") ?: 0
        courseIndex = arguments?.getInt("courseIndex") ?: 0

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollView.visibility = View.GONE
                    binding.enrollBar.visibility = View.GONE
                }
                is UiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val course = viewModel.getCourse(categoryIndex, courseIndex)
                    if (course != null) {
                        binding.scrollView.visibility = View.VISIBLE
                        binding.enrollBar.visibility = View.VISIBLE
                        bindCourse(course)
                    }
                }
                is UiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun bindCourse(course: Course) {
        val ctx = requireContext()

        val firstTag = course.tags.firstOrNull() ?: course.title.split(" ").first().lowercase()
        binding.tvTag.text = "// $firstTag"

        binding.tvHeroTitle.text = course.title

        binding.tagsContainer.removeAllViews()
        val tagsList = if (course.tags.isNotEmpty()) course.tags
        else course.title.split(" ").take(3)
        for (tag in tagsList) {
            val tagView = TextView(ctx).apply {
                text = tag
                setTextColor(ContextCompat.getColor(ctx, R.color.white))
                textSize = 11f
                setPadding(
                    dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4)
                )
                setBackgroundResource(R.drawable.bg_tag)
            }
            val params = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { marginEnd = dpToPx(6) }
            binding.tagsContainer.addView(tagView, params)
        }

        binding.tvCourseTitle.text = course.title
        binding.tvSubtitle.text = "Everything you need to start writing ${
            course.title.split(" ").first()
        }"
        binding.tvRating.text = String.format("%.1f", course.rating)
        binding.tvReviews.text = if (course.reviewsCount > 0)
            "%,d".format(course.reviewsCount) else "18,420"
        binding.tvHours.text = "${course.durationHours}h"
        binding.tvLevel.text = course.level

        // Instructor
        val initials = course.instructor.name.split(" ")
            .take(2).joinToString("") { it.take(1) }
        binding.tvInstructorInitials.text = initials
        binding.tvInstructorName.text = course.instructor.name
        binding.tvInstructorTitle.text = course.instructor.title

        binding.tvDescription.text = course.description.ifEmpty {
            "Start from zero and learn ${course.title.split(" ").first()}'s syntax, null safety, " +
                    "collections, and functions. By the end you'll be comfortable reading and " +
                    "writing idiomatic ${course.title.split(" ").first()}."
        }

        val totalMin = course.lessons.sumOf { it.durationMinutes }
        binding.tvLessonsSummary.text = getString(
            R.string.lessons_count_format, course.lessons.size, totalMin
        )

        binding.rvLessons.layoutManager = LinearLayoutManager(ctx)
        binding.rvLessons.adapter = LessonAdapter(course.lessons) { lessonIndex ->
            navigateToLesson(lessonIndex)
        }
    }

    private fun navigateToLesson(lessonIndex: Int) {
        val bundle = bundleOf(
            "categoryIndex" to categoryIndex,
            "courseIndex" to courseIndex,
            "lessonIndex" to lessonIndex
        )
        findNavController().navigate(R.id.action_courseDetail_to_lesson, bundle)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}