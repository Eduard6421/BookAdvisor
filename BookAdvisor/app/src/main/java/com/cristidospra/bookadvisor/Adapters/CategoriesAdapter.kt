package com.cristidospra.bookadvisor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.R

class CategoriesAdapter(private val categories: ArrayList<Genre>, private val onCategoryClickListener: OnCategoryClickListener) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.big_genre_listitem_layout, parent, false)

        return CategoryViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.count()
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = categories[position]

        holder.name.text = category.name

        holder.itemView.setOnClickListener {
            onCategoryClickListener.onCategoryClick(category)
        }
    }


    class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val name: TextView = view.findViewById(R.id.big_genre_listitem_name)
    }

    interface OnCategoryClickListener {
        fun onCategoryClick(category: Genre)
    }
}