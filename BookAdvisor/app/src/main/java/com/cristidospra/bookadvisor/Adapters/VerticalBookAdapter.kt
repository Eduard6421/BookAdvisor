package com.cristidospra.bookadvisor.Adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.ReadStatus
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class VerticalBookAdapter(private val books: ArrayList<Book>): RecyclerView.Adapter<VerticalBookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_lisitem_vertical_layout, parent, false)

        return BookViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return books.count()
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        val book = books[position]

        //holder.coverImageView -- cu glide

        when(book.readStatus) {

            ReadStatus.READ -> {
                holder.wantToReadButton.text = "Already read"
                holder.wantToReadButton.setBackgroundTintList(ColorStateList.valueOf(Utils.getColor(R.color.colorTransparentWhite)))
            }
            ReadStatus.WANT_TO_READ -> {
                holder.wantToReadButton.text = "Read"
            }
            ReadStatus.NOTHING -> {
                holder.wantToReadButton.text = "Want to read"
            }
        }

    }


    class BookViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val coverImageView: ImageView = view.findViewById(R.id.book_lisitiem_vertical_cover)
        val wantToReadButton: Button = view.findViewById(R.id.book_listitem_vertical_button)
    }
}