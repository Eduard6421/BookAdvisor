package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class HorizontalBookAdapter(private val books: ArrayList<Book>): RecyclerView.Adapter<HorizontalBookAdapter.BookViewHolder>() {

    private lateinit var usedContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_listitem_horizontal_layout, parent, false)
        this.usedContext = parent.context

        return BookViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return books.count()
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        val book = books[position]

        Utils.loadImage(usedContext, holder.coverImageView, book.coverURL)
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.authorsToString()
        holder.ratingBar.rating = book.rating
        holder.ratingValueTextView.text = book.rating.toString()
        holder.nrRatesTextView.text = ("out of ${book.nrRates()} rates")
        holder.nrPagesTextView.text = ("${book.nrPages} pages")
        holder.releaseDateTextView.text = ("since ${book.releaseDateString}")
    }


    class BookViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val coverImageView: ImageView = view.findViewById(R.id.book_lisitiem_horizontal_cover)
        val titleTextView: TextView = view.findViewById(R.id.book_lisitiem_horizontal_title)
        val authorTextView: TextView = view.findViewById(R.id.book_lisitiem_horizontal_author)
        val ratingBar: RatingBar = view.findViewById(R.id.book_lisitiem_horizontal_ratingbar)
        val ratingValueTextView: TextView = view.findViewById(R.id.book_lisitiem_horizontal_ratingtext)
        val nrRatesTextView: TextView = view.findViewById(R.id.book_lisitiem_horizontal_nrrates)
        val nrPagesTextView: TextView = view.findViewById(R.id.book_lisitiem_horizontal_nrpages)
        val releaseDateTextView: TextView = view.findViewById(R.id.book_lisitiem_horizontal_release_date)
    }
}