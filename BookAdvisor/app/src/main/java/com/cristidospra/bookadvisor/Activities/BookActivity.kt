package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.GenreAdapter
import com.cristidospra.bookadvisor.Adapters.ReviewAdapter
import com.cristidospra.bookadvisor.Dialogs.AddReviewDialog
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.BookApiManager
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class BookActivity : NavigationMenuActivity() {

    lateinit var bookCoverImageView: ImageView
    lateinit var bookTitleTextView: TextView
    lateinit var bookAuthorTextView: TextView
    lateinit var bookRatingBar: RatingBar
    lateinit var bookRatingValueTextView: TextView
    lateinit var bookNrRatesTextView: TextView
    lateinit var addToReadingListsButton: Button
    lateinit var bookPrologueTextView: TextView
    lateinit var bookGenresRecyclerView: RecyclerView
    lateinit var bookReviewsRecyclerView: RecyclerView
    lateinit var bookReviewsAdapter: ReviewAdapter
    lateinit var bookAddReviewPlus: ImageView


    private lateinit var currentBook: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        inflateViews()

        if (intent.hasExtra("book")) {
            currentBook = intent.getSerializableExtra("book") as Book

            BookApiManager.getBook(currentBook.id) {

                currentBook = it

                Utils.loadBookImage(this, bookCoverImageView, currentBook.coverURL())
                bookTitleTextView.text = currentBook.title
                bookAuthorTextView.text = currentBook.authorsToString()
                bookRatingBar.rating = currentBook.rating
                bookRatingValueTextView.text = currentBook.rating.toString()
                bookNrRatesTextView.text = ("out of ${currentBook.nrRates()} rates")
                bookPrologueTextView.text = currentBook.prologue


                addToReadingListsButton.setOnClickListener {

                    val intent = Intent(this, AddToLibraryActivity::class.java)
                    intent.putExtra("book", currentBook)
                    startActivity(intent)
                }

                bookGenresRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                bookGenresRecyclerView.adapter = GenreAdapter(currentBook.genres, object: GenreAdapter.OnGenreClickListener {

                    override fun onGenreClick(genre: Genre) {

                        val intent = Intent(this@BookActivity, GenreActivity::class.java)
                        intent.putExtra("genre", genre)
                        this@BookActivity.startActivity(intent)
                    }

                })


                bookReviewsAdapter = ReviewAdapter(currentBook.reviews)
                bookReviewsRecyclerView.layoutManager = LinearLayoutManager(this)
                bookReviewsRecyclerView.adapter = bookReviewsAdapter

                bookAddReviewPlus.setOnClickListener {

                    val reviewDialog = AddReviewDialog(this) {review ->

                        BookApiManager.addReview(currentBook, review)

                        currentBook.reviews.add(review)
                        bookReviewsAdapter.notifyDataSetChanged()

                    }

                    reviewDialog.show()
                }

            }

        }

    }

    private fun inflateViews() {

        bookCoverImageView = findViewById(R.id.book_cover)
        bookTitleTextView = findViewById(R.id.book_about_title)
        bookAuthorTextView = findViewById(R.id.book_about_author)
        bookRatingBar = findViewById(R.id.book_about_ratingbar)
        bookRatingValueTextView = findViewById(R.id.book_about_rating_value)
        bookNrRatesTextView = findViewById(R.id.book_about_review_count)
        addToReadingListsButton = findViewById(R.id.book_button_read)
        bookPrologueTextView = findViewById(R.id.book_about_prologue_content)
        bookGenresRecyclerView = findViewById(R.id.book_tags_recyclerview)
        bookReviewsRecyclerView = findViewById(R.id.book_reviews_recyclerview)
        bookAddReviewPlus = findViewById(R.id.book_activity_add_review)
    }
}
