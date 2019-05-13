package com.cristidospra.bookadvisor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.R

class GenreAdapter(private val genres: ArrayList<Genre>, private val onGenreClickListener: OnGenreClickListener): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.small_genre_lisitem_layout, parent, false)

        return GenreViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return genres.count()
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {

        val genre = genres[position]

        holder.name.text = genre.name

        holder.itemView.setOnClickListener {
            onGenreClickListener.onGenreClick(genre)
        }
    }

    class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name: TextView = view.findViewById(R.id.small_genre_name)
    }

    interface OnGenreClickListener {
        fun onGenreClick(genre: Genre)
    }
}