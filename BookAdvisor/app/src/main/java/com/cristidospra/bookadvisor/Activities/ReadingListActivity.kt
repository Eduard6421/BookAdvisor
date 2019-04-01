package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.R

class ReadingListActivity : AppCompatActivity() {

    lateinit var readingListTitleTextView: TextView
    lateinit var readingListGenresRecyclerView: RecyclerView
    lateinit var readingListBooksRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readinglist)

        inflateViews()
    }

    private fun inflateViews() {

        readingListTitleTextView = findViewById(R.id.readinglist_title)
        readingListGenresRecyclerView = findViewById(R.id.readinglist_genres_recyclerview)
        readingListBooksRecyclerView = findViewById(R.id.readinglist_books_recyclerview)
    }
}
