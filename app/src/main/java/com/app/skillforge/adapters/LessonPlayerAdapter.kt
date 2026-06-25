package com.app.skillforge.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.skillforge.R
import com.app.skillforge.databinding.ItemLessonPlayerBinding
import com.app.skillforge.models.Lesson

class LessonPlayerAdapter(
    private val lessons: List<Lesson>,
    private var currentIndex: Int = 0,
    private val onLessonClick: (Int) -> Unit
) : RecyclerView.Adapter<LessonPlayerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemLessonPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson, position: Int) {
            val ctx = itemView.context
            val isPlaying = position == currentIndex

            binding.tvTitle.text = lesson.title

            when {
                isPlaying -> {
                    binding.lessonContainer.setBackgroundResource(R.drawable.bg_playing)
                    binding.iconBg.setBackgroundResource(R.drawable.bg_teal_circle)
                    binding.ivIcon.setImageResource(R.drawable.ic_pause)
                    binding.ivIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.white))
                    binding.tvSubtitle.text =
                        ctx.getString(R.string.now_playing_format, lesson.durationMinutes)
                    binding.tvBadge.visibility = View.GONE
                    binding.root.alpha = 1f

                    binding.root.isClickable = true
                    binding.root.isFocusable = true
                    binding.root.setOnClickListener { onLessonClick(position) }
                }

                lesson.isFree -> {
                    binding.lessonContainer.setBackgroundResource(R.drawable.bg_card)
                    binding.iconBg.setBackgroundResource(R.drawable.bg_teal_light_circle)
                    binding.ivIcon.setImageResource(R.drawable.ic_play_arrow)
                    binding.ivIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.teal))
                    binding.tvSubtitle.text = "${lesson.durationMinutes} min"
                    binding.tvBadge.visibility = View.VISIBLE
                    binding.root.alpha = 1f

                    binding.root.isClickable = true
                    binding.root.isFocusable = true
                    binding.root.setOnClickListener { onLessonClick(position) }
                }

                else -> {
                    binding.lessonContainer.setBackgroundResource(R.drawable.bg_card)
                    binding.iconBg.setBackgroundResource(R.drawable.bg_gray_circle)
                    binding.ivIcon.setImageResource(R.drawable.ic_lock)
                    binding.ivIcon.setColorFilter(ContextCompat.getColor(ctx, R.color.text_secondary))
                    binding.tvSubtitle.text = "${lesson.durationMinutes} min"
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLessonPlayerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lessons[position], position)
    }

    override fun getItemCount() = lessons.size

    fun updateCurrentIndex(newIndex: Int) {
        val oldIndex = currentIndex
        currentIndex = newIndex
        notifyItemChanged(oldIndex)
        notifyItemChanged(newIndex)
    }

}