package com.cristidospra.bookadvisor.Dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Networking.BookApiManager
import com.cristidospra.bookadvisor.R

class SearchedBooksDialog(private val currentContext: Context, private val searchString: String) : Dialog(currentContext){

    lateinit var searchText: TextView
    lateinit var booksRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.searched_books_dialog)

        inflateViews()

        searchText.text = "Showing results for \"${searchString}\""

        BookApiManager.getBooksFromSearch(searchString) {

            booksRecyclerView.layoutManager = LinearLayoutManager(currentContext)
            booksRecyclerView.adapter = HorizontalBookAdapter(it, object : HorizontalBookAdapter.OnBookClickListener {
                override fun onBookClick(book: Book) {

                    val intent = Intent(currentContext, Book::class.java)
                    intent.putExtra("book", book)
                    currentContext.startActivity(intent)

                    dismiss()
                }
            })
        }

    }

    private fun inflateViews() {

        searchText = findViewById(R.id.searched_books_text)
        booksRecyclerView = findViewById(R.id.searched_books_rv)
    }
}