package com.app.skillforge.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.skillforge.R
import com.app.skillforge.adapters.CategoryAdapter
import com.app.skillforge.adapters.CourseAdapter
import com.app.skillforge.databinding.FragmentHomeBinding
import com.app.skillforge.models.Course
import com.app.skillforge.viewModel.MainViewModel
import com.app.skillforge.viewModel.UiState

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRetry.setOnClickListener { viewModel.loadData() }

        // See all click handlers
        binding.tvSeeAllCategories.setOnClickListener {
            Toast.makeText(requireContext(), "All Categories", Toast.LENGTH_SHORT).show()
            // TODO: navigate to all categories screen
        }

        binding.tvSeeAllCourses.setOnClickListener {
            Toast.makeText(requireContext(), "All Courses", Toast.LENGTH_SHORT).show()
            // TODO: navigate to all courses screen
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> showLoading()
                is UiState.Success -> showContent(state)
                is UiState.Error -> showError(state.message)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentScroll.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
    }

    private fun showError(message: String) {
        binding.progressBar.visibility = View.GONE
        binding.contentScroll.visibility = View.GONE
        binding.errorLayout.visibility = View.VISIBLE
        binding.tvError.text = "Oops! $message"
    }

    private fun showContent(state: UiState.Success) {
        binding.progressBar.visibility = View.GONE
        binding.errorLayout.visibility = View.GONE
        binding.contentScroll.visibility = View.VISIBLE

        val categories = state.data.categories
        val allCourses = categories.flatMap { it.courses }

        // Categories horizontal list
        binding.rvCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategories.adapter = CategoryAdapter(categories)

        // Attach scroll indicator
        setupScrollIndicator()

        // Courses list
        binding.rvCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCourses.adapter = CourseAdapter(allCourses) { course ->
            navigateToCourseDetail(course)
        }
    }

    private fun setupScrollIndicator() {
        binding.rvCategories.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                updateThumbPosition()
            }
        })
        // Initial position after layout
        binding.rvCategories.post { updateThumbPosition() }
    }

    private fun updateThumbPosition() {
        val rv = binding.rvCategories
        val range = rv.computeHorizontalScrollRange()
        val extent = rv.computeHorizontalScrollExtent()
        val offset = rv.computeHorizontalScrollOffset()

        val trackWidth = 120 * resources.displayMetrics.density   // 120dp track
        val thumbWidth = 50 * resources.displayMetrics.density    // 50dp thumb
        val maxThumbTravel = trackWidth - thumbWidth

        val scrollable = (range - extent).toFloat()
        val progress = if (scrollable > 0) offset / scrollable else 0f
        val translation = (progress * maxThumbTravel).coerceIn(0f, maxThumbTravel)

        (binding.scrollThumb.layoutParams as FrameLayout.LayoutParams).apply {
            binding.scrollThumb.translationX = translation
        }
    }

    private fun navigateToCourseDetail(course: Course) {
        val indices = viewModel.findCourseIndices(course) ?: return
        val bundle = bundleOf(
            "categoryIndex" to indices.first,
            "courseIndex" to indices.second
        )
        findNavController().navigate(R.id.action_home_to_courseDetail, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}