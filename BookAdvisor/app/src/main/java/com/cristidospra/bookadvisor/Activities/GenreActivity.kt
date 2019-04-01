package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.R

class GenreActivity : AppCompatActivity() {

    lateinit var genreTitleTextView: TextView
    lateinit var addToFavouriteButton: Button
    lateinit var genreBooksRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        inflateViews()
    }

    private fun inflateViews() {

        genreTitleTextView = findViewById(R.id.genre_activity_title)
        addToFavouriteButton = findViewById(R.id.genre_activity_add_to_fav_button)
        genreBooksRecyclerView = findViewById(R.id.genre_activity_books_recyclerview)
    }
}
