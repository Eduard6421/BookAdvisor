package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.Book
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

        if (CurrentUser.instance.favouriteGenres.map { it.id }.contains(currentGenre.id)) {

            addToFavouriteButton.text = "Already a favourite genre"
        }
        else {

            addToFavouriteButton.text = " + Add to favourite genres"

            addToFavouriteButton.setOnClickListener {

                CurrentUser.instance.addGenreToFavourites(currentGenre)
                UserApiManager.updateUser(CurrentUser.instance)
                addToFavouriteButton.text = "Already a favourite genre"

                Toast.makeText(this, "Successfully added ${currentGenre.name} to favourites", Toast.LENGTH_LONG).show()
            }
        }



        BookApiManager.getBooksByGenre(currentGenre, onSuccess = {

            genreBooksRecyclerView.layoutManager = LinearLayoutManager(this)
            genreBooksRecyclerView.adapter = HorizontalBookAdapter(it, object : HorizontalBookAdapter.OnBookClickListener {
                override fun onBookClick(book: Book) {

                    val intent = Intent(this@GenreActivity, Book::class.java)
                    intent.putExtra("book", book)
                    this@GenreActivity.startActivity(intent)
                }

            })
        })
    }

    private fun inflateViews() {

        genreTitleTextView = findViewById(R.id.genre_activity_title)
        addToFavouriteButton = findViewById(R.id.genre_activity_add_to_fav_button)
        genreBooksRecyclerView = findViewById(R.id.genre_activity_books_recyclerview)
    }
}
