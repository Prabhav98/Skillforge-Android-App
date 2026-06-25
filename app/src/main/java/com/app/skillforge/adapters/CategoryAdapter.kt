package com.app.skillforge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.skillforge.R
import com.app.skillforge.databinding.ItemCategoryBinding
import com.app.skillforge.models.Category

class CategoryAdapter(
    private val categories: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        val b = holder.binding

        b.tvCategoryName.text = category.name
        b.tvCourseCount.text = holder.itemView.context.getString(
            R.string.courses_count, category.courses.size
        )

        // Set colors based on category type
        when {
            category.name.contains("Android", true) -> {
                b.dotBg.setBackgroundResource(R.drawable.bg_category_teal)
                b.dot.setBackgroundResource(R.drawable.bg_dot_teal)
            }
            category.name.contains("Backend", true) -> {
                b.dotBg.setBackgroundResource(R.drawable.bg_category_green)
                b.dot.setBackgroundResource(R.drawable.bg_dot_green)
            }
            else -> {
                b.dotBg.setBackgroundResource(R.drawable.bg_category_yellow)
                b.dot.setBackgroundResource(R.drawable.bg_dot_amber)
            }
        }
    }

    override fun getItemCount() = categories.size
}