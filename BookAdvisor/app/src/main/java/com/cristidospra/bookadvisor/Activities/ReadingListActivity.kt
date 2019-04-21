package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.GenreAdapter
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class ReadingListActivity : NavigationMenuActivity() {

    lateinit var readingListTitleTextView: TextView
    lateinit var readingListGenresRecyclerView: RecyclerView
    lateinit var readingListBooksRecyclerView: RecyclerView

    private lateinit var currentReadingList: ReadingList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_readinglist)

        inflateViews()

        currentReadingList = intent.getSerializableExtra("reading_list") as ReadingList

        readingListBooksRecyclerView.layoutManager = LinearLayoutManager(this)
        readingListBooksRecyclerView.adapter = HorizontalBookAdapter(currentReadingList.books)

        readingListGenresRecyclerView.layoutManager = LinearLayoutManager(this)
        readingListGenresRecyclerView.adapter = GenreAdapter(currentReadingList.genres)
    }

    private fun inflateViews() {

        readingListTitleTextView = findViewById(R.id.readinglist_title)
        readingListGenresRecyclerView = findViewById(R.id.readinglist_genres_recyclerview)
        readingListBooksRecyclerView = findViewById(R.id.readinglist_books_recyclerview)
    }
}
