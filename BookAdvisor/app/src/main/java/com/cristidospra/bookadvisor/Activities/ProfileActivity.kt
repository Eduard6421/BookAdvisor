package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.R

class ProfileActivity : AppCompatActivity() {

    lateinit var profilePictureImageView: ImageView
    lateinit var profileNameTextView: TextView
    lateinit var profileNrBooksSmallTextView: TextView
    lateinit var profileNrFollowersTextView: TextView
    lateinit var profileNrFollowingTextView: TextView
    lateinit var profileNrBooksBigTextView: TextView
    lateinit var profileNrReadBooksTextView: TextView
    lateinit var profileNrWantToReadTextView: TextView
    lateinit var profileReadingListsButton: Button
    lateinit var profileEditSettingsButton: Button
    lateinit var profileAlreadyReadRecyclerView: RecyclerView
    lateinit var profileWantToReadRecyclerView: RecyclerView
    lateinit var profileFavouriteGenresRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        inflateViews()
    }

    private fun inflateViews() {

        profilePictureImageView = findViewById(R.id.profile_picture)
        profileNameTextView = findViewById(R.id.profile_name)
        profileNrBooksSmallTextView = findViewById(R.id.profile_nr_books)
        profileNrFollowersTextView = findViewById(R.id.profile_nr_followers)
        profileNrFollowingTextView = findViewById(R.id.profile_nr_following)
        profileNrBooksBigTextView = findViewById(R.id.profile_nr_books2)
        profileNrReadBooksTextView = findViewById(R.id.profile_nr_books_read)
        profileNrWantToReadTextView = findViewById(R.id.profile_nr_want_to_read)
        profileReadingListsButton = findViewById(R.id.profile_readinglists_button)
        profileEditSettingsButton = findViewById(R.id.profile_editsettings_button)
        profileAlreadyReadRecyclerView = findViewById(R.id.profile_already_read_recyclerview)
        profileWantToReadRecyclerView = findViewById(R.id.profile_want_to_read_recyclerview)
        profileFavouriteGenresRecyclerView = findViewById(R.id.profile_favourite_genres_recyclerview)
    }
}
