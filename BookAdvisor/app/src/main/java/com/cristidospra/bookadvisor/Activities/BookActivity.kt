package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class BookActivity : NavigationMenuActivity() {

    lateinit var bookCoverImageView: ImageView
    lateinit var bookTitleTextView: TextView
    lateinit var bookAuthorTextView: TextView
    lateinit var bookRatingBar: RatingBar
    lateinit var bookRatingValueTextView: TextView
    lateinit var bookNrRatesTextView: TextView
    lateinit var wantToReadButton: Button
    lateinit var bookPrologueTextView: TextView
    lateinit var bookGenresRecyclerView: RecyclerView
    lateinit var bookReviewsRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        inflateViews()
    }

    private fun inflateViews() {

        bookCoverImageView = findViewById(R.id.book_cover)
        bookTitleTextView = findViewById(R.id.book_about_title)
        bookAuthorTextView = findViewById(R.id.book_about_author)
        bookRatingBar = findViewById(R.id.book_about_ratingbar)
        bookRatingValueTextView = findViewById(R.id.book_about_rating_value)
        bookNrRatesTextView = findViewById(R.id.book_about_review_count)
        wantToReadButton = findViewById(R.id.book_button_read)
        bookPrologueTextView = findViewById(R.id.book_about_prologue_content)
        bookGenresRecyclerView = findViewById(R.id.book_tags_recyclerview)
        bookReviewsRecyclerView = findViewById(R.id.book_reviews_recyclerview)
    }
}
