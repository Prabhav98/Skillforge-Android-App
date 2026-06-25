package com.app.skillforge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.skillforge.R
import com.app.skillforge.databinding.ItemCourseBinding
import com.app.skillforge.models.Course
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class CourseAdapter(
    private val courses: List<Course>,
    private val onCourseClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    onCourseClick(courses[bindingAdapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val course = courses[position]
        val b = holder.binding
        val ctx = holder.itemView.context

        b.tvLevel.text = course.level.uppercase()
        b.tvTitle.text = course.title
        b.tvInstructor.text = course.instructor.name
        b.tvRating.text = String.format("%.1f", course.rating)
        b.tvDuration.text = "${course.durationHours}h"

        val levelColor = if (course.level.equals("Intermediate", true))
            ContextCompat.getColor(ctx, R.color.orange)
        else
            ContextCompat.getColor(ctx, R.color.teal)
        b.tvLevel.setTextColor(levelColor)

        val cornerRadius = (12 * ctx.resources.displayMetrics.density).toInt()
        Glide.with(ctx)
            .load(course.thumbnailUrl)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .placeholder(R.drawable.bg_card)
            .into(b.ivThumbnail)
    }

    override fun getItemCount() = courses.size
}