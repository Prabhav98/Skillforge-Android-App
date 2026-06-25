package com.app.skillforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.skillforge.R
import com.app.skillforge.databinding.ItemLessonBinding
import com.app.skillforge.models.Lesson

class LessonAdapter(
    private val lessons: List<Lesson>,
    private val onLessonClick: (Int) -> Unit
) : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemLessonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson, position: Int) {
            val ctx = itemView.context

            binding.tvLessonTitle.text = lesson.title
            binding.tvLessonDuration.text = "${lesson.durationMinutes} min"

            if (lesson.isFree) {
                // Free lesson — clickable
                binding.iconBg.setBackgroundResource(R.drawable.bg_teal_light_circle)
                binding.ivIcon.setImageResource(R.drawable.ic_play_arrow)
                binding.ivIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.teal))
                binding.tvBadge.visibility = View.VISIBLE

                binding.root.isClickable = true
                binding.root.isFocusable = true
                binding.root.alpha = 1f
                binding.root.setOnClickListener { onLessonClick(position) }
            } else {
                // Locked lesson — show toast on tap
                binding.iconBg.setBackgroundResource(R.drawable.bg_gray_circle)
                binding.ivIcon.setImageResource(R.drawable.ic_lock)
                binding.ivIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.text_secondary))
                binding.tvBadge.visibility = View.GONE
                binding.root.alpha = 0.7f

                binding.root.isClickable = true
                binding.root.isFocusable = true
                binding.root.setOnClickListener {
                    Toast.makeText(ctx, "Enroll to unlock this lesson", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLessonBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lessons[position], position)
    }

    override fun getItemCount() = lessons.size
}