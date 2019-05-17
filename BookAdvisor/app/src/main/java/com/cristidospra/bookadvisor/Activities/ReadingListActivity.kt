package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.GenreAdapter
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Genre
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

        readingListTitleTextView.text = currentReadingList.title

        readingListBooksRecyclerView.layoutManager = LinearLayoutManager(this)
        readingListBooksRecyclerView.adapter = HorizontalBookAdapter(currentReadingList.books, object : HorizontalBookAdapter.OnBookClickListener {
            override fun onBookClick(book: Book) {

                val intent = Intent(this@ReadingListActivity, BookActivity::class.java)
                intent.putExtra("book", book)
                this@ReadingListActivity.startActivity(intent)
            }
        })

        readingListGenresRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        readingListGenresRecyclerView.adapter = GenreAdapter(currentReadingList.genres, object: GenreAdapter.OnGenreClickListener {

            override fun onGenreClick(genre: Genre) {

                val intent = Intent(this@ReadingListActivity, GenreActivity::class.java)
                intent.putExtra("genre", genre)
                this@ReadingListActivity.startActivity(intent)
            }

        })
    }

    private fun inflateViews() {

        readingListTitleTextView = findViewById(R.id.readinglist_title)
        readingListGenresRecyclerView = findViewById(R.id.readinglist_genres_recyclerview)
        readingListBooksRecyclerView = findViewById(R.id.readinglist_books_recyclerview)
    }
}
