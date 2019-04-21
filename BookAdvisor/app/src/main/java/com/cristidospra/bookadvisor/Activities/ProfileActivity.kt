package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.GenreAdapter
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class ProfileActivity : NavigationMenuActivity() {

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

    private var currentProfileUser: User = CurrentUser.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        inflateViews()

        if (intent.hasExtra("user")) {

            currentProfileUser = intent.getSerializableExtra("user") as User
        }

        Utils.loadImage(this, profilePictureImageView, currentProfileUser.profilePicURL)
        profileNameTextView.text = currentProfileUser.fullName()
        profileNrBooksSmallTextView.text = currentProfileUser.nrOfBooks().toString()
        profileNrFollowersTextView.text = currentProfileUser.nrFollowers().toString()
        profileNrFollowingTextView.text = currentProfileUser.nrFollowing().toString()
        profileNrBooksBigTextView.text = currentProfileUser.nrOfBooks().toString()
        profileNrReadBooksTextView.text = currentProfileUser.nrOfReadBooks().toString()
        profileNrWantToReadTextView.text = currentProfileUser.nrOfWantToReadBooks().toString()

        profileReadingListsButton.setOnClickListener {

            val intent = Intent(this, LibraryActivity::class.java)
            intent.putExtra("user", currentProfileUser)
            this.startActivity(intent)
        }

        if (currentProfileUser.email == CurrentUser.instance.email) {

            profileEditSettingsButton.setOnClickListener {

                val intent = Intent(this, SettingsActivity::class.java)
                this.startActivity(intent)
            }
        }
        else {

            profileEditSettingsButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_nav_people, 0, 0, 0)
            if (CurrentUser.instance.isFollowing(currentProfileUser)) {
                profileEditSettingsButton.text = "Following"
                profileEditSettingsButton.setTextColor(Utils.getColor(R.color.colorAccent))
            }
            else {
                profileEditSettingsButton.text = "Follow"
                profileEditSettingsButton.setOnClickListener {

                    UserApiManager.follow(currentProfileUser) {
                        this.recreate()
                    }
                }
            }
        }

        profileAlreadyReadRecyclerView.layoutManager = LinearLayoutManager(this)
        profileAlreadyReadRecyclerView.adapter = HorizontalBookAdapter(currentProfileUser.getReadBooks())

        profileWantToReadRecyclerView.layoutManager = LinearLayoutManager(this)
        profileWantToReadRecyclerView.adapter = HorizontalBookAdapter(currentProfileUser.getWantToReadBooks())

        profileFavouriteGenresRecyclerView.layoutManager = LinearLayoutManager(this)
        profileFavouriteGenresRecyclerView.adapter = GenreAdapter(currentProfileUser.favouriteGenres)
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
