package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.ReadStatus
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class VerticalBookAdapter(private val books: ArrayList<Book>, private val onBookClickListener: OnBookClickListener): RecyclerView.Adapter<VerticalBookAdapter.BookViewHolder>() {

    private lateinit var usedContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.book_lisitem_vertical_layout, parent, false)
        this.usedContext = parent.context

        return BookViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return books.count()
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {

        val book = books[position]

        Utils.loadBookImage(usedContext, holder.coverImageView, book.coverURL)

        when(book.readStatus) {

            ReadStatus.READ -> {
                holder.wantToReadButton.text = "Already read"
                holder.wantToReadButton.backgroundTintList = ColorStateList.valueOf(Utils.getColor(R.color.colorTransparentWhite))
            }
            ReadStatus.WANT_TO_READ -> {
                holder.wantToReadButton.text = "Read now"
            }
            ReadStatus.NOTHING -> {
                holder.wantToReadButton.text = "Want to read"
            }
        }

        holder.wantToReadButton.setOnClickListener {

            when(book.readStatus) {

                ReadStatus.READ -> {

                }
                ReadStatus.WANT_TO_READ -> {
                    /*TODO: this */
                }
                ReadStatus.NOTHING -> {

                    /*TODO: this */
                }
            }
        }

        holder.itemView.setOnClickListener {
            onBookClickListener.onBookClick(book)
        }
    }


    class BookViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val coverImageView: ImageView = view.findViewById(R.id.book_lisitiem_vertical_cover)
        val wantToReadButton: Button = view.findViewById(R.id.book_listitem_vertical_button)
    }

    interface OnBookClickListener {
        fun onBookClick(book: Book)
    }
}