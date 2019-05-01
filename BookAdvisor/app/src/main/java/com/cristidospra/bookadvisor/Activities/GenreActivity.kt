package com.cristidospra.bookadvisor.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.BookApiManager
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R

class GenreActivity : NavigationMenuActivity() {

    lateinit var genreTitleTextView: TextView
    lateinit var addToFavouriteButton: Button
    lateinit var genreBooksRecyclerView: RecyclerView

    private lateinit var currentGenre: Genre

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre)

        inflateViews()

        currentGenre = intent.getSerializableExtra("genre") as Genre

        genreTitleTextView.text = currentGenre.name

        addToFavouriteButton.setOnClickListener {

            CurrentUser.instance.addGenreToFavourites(currentGenre)
            UserApiManager.updateUser(CurrentUser.instance)
        }

        BookApiManager.getBooksByGenre(currentGenre, onSuccess = {

            genreBooksRecyclerView.layoutManager = LinearLayoutManager(this)
            genreBooksRecyclerView.adapter = HorizontalBookAdapter(it)
        })
    }

    private fun inflateViews() {

        genreTitleTextView = findViewById(R.id.genre_activity_title)
        addToFavouriteButton = findViewById(R.id.genre_activity_add_to_fav_button)
        genreBooksRecyclerView = findViewById(R.id.genre_activity_books_recyclerview)
    }
}
