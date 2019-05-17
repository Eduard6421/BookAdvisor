package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.cristidospra.bookadvisor.Adapters.GenreAdapter
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.FirebaseManager
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class ProfileActivity : NavigationMenuActivity() {

    lateinit var profilePictureImageView: ImageView
    lateinit var profileSendMessageImageView: ImageView
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

        if (intent.hasExtra("user_mail")) {

            val userEmail = intent.getSerializableExtra("user_mail") as String

            UserApiManager.getUser(userEmail) {

                setData(it)
            }
        }
        else if (intent.hasExtra("user_firebaseUID")) {

            val userFirebaseUID = intent.getSerializableExtra("user_firebaseUID") as String

            UserApiManager.getUserByFirebase(userFirebaseUID) {

                setData(it)
            }
        }
        else {

            if (intent.hasExtra("user")) {
                currentProfileUser = intent.getSerializableExtra("user") as User
            }

            setData(currentProfileUser)
        }
    }

    private fun inflateViews() {

        profilePictureImageView = findViewById(R.id.profile_picture)
        profileSendMessageImageView = findViewById(R.id.profile_send_message)
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

    private fun setData(user: User) {

        Utils.loadPersonImage(this, profilePictureImageView, currentProfileUser.profilePic())
        profileNameTextView.text = currentProfileUser.fullName()
        profileNrBooksSmallTextView.text = currentProfileUser.nrOfBooks().toString()
        profileNrFollowersTextView.text = currentProfileUser.nrFollowers().toString()
        profileNrFollowingTextView.text = currentProfileUser.nrFollowing().toString()
        profileNrBooksBigTextView.text = currentProfileUser.nrOfBooks().toString()
        profileNrReadBooksTextView.text = currentProfileUser.nrOfReadBooks().toString()
        profileNrWantToReadTextView.text = currentProfileUser.nrOfWantToReadBooks().toString()

        if (currentProfileUser.email == CurrentUser.instance.email) {

            profileSendMessageImageView.visibility = View.GONE
        }
        else {

            profileSendMessageImageView.visibility = View.VISIBLE

            profileSendMessageImageView.setOnClickListener {

                FirebaseManager.getUserChat(CurrentUser.instance.firebasUID, currentProfileUser.firebasUID) {chatUID ->

                    FirebaseManager.getLastMessage(CurrentUser.instance.firebasUID, currentProfileUser.firebasUID, chatUID) {lastMessage ->
                        val conversation = Conversation(currentProfileUser, lastMessage)

                        val intent = Intent(this, ConversationActivity::class.java)
                        intent.putExtra("conversation", conversation)
                        this.startActivity(intent)
                    }
                }

                /*TODO: test create new conversation or go to the existent one */
            }
        }

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

        profileAlreadyReadRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        profileAlreadyReadRecyclerView.adapter = HorizontalBookAdapter(currentProfileUser.getReadBooks(), object : HorizontalBookAdapter.OnBookClickListener {
            override fun onBookClick(book: Book) {

                val intent = Intent(this@ProfileActivity, BookActivity::class.java)
                intent.putExtra("book", book)
                this@ProfileActivity.startActivity(intent)
            }

        })

        profileWantToReadRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        profileWantToReadRecyclerView.adapter = HorizontalBookAdapter(currentProfileUser.getWantToReadBooks(), object : HorizontalBookAdapter.OnBookClickListener {
            override fun onBookClick(book: Book) {

                val intent = Intent(this@ProfileActivity, BookActivity::class.java)
                intent.putExtra("book", book)
                this@ProfileActivity.startActivity(intent)
            }

        })

        profileFavouriteGenresRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        profileFavouriteGenresRecyclerView.adapter = GenreAdapter(currentProfileUser.favouriteGenres, object: GenreAdapter.OnGenreClickListener {

            override fun onGenreClick(genre: Genre) {

                val intent = Intent(this@ProfileActivity, GenreActivity::class.java)
                intent.putExtra("genre", genre)
                this@ProfileActivity.startActivity(intent)
            }
        })

        if (currentProfileUser.email == CurrentUser.instance.email) {
            profilePictureImageView.setOnClickListener {

                startCropImageActivity()
            }
        }
    }


    private fun startCropImageActivity() {

        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        var photo: Bitmap? = null

        when (requestCode) {

            //When a photo was taken
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {

                val result: CropImage.ActivityResult? = CropImage.getActivityResult(data)

                if (result != null) {

                    when (resultCode) {

                        //If ok, get the photo
                        RESULT_OK -> {

                            val resultUri: Uri = result.uri
                            photo = LoadImageDataTask(resultUri).execute().get()

                        }

                        CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {

                            val error: Exception = result.error
                            Log.e("BookAdvisorCamera", error.message)
                        }

                        else -> {}
                    }
                }
            }
        }

        if (photo != null) {

            runOnUiThread {

                Glide.with(this)
                    .load(photo)
                    .apply(RequestOptions().placeholder(R.drawable.cover_placeholder))
                    .apply(RequestOptions().transforms(CenterInside(), CircleCrop()))
                    .into(profilePictureImageView)

            }

            SendPictureToServerAsync(photo).execute()
        }
    }


    private inner class SendPictureToServerAsync() : AsyncTask<Unit, Unit, Unit>() {

        lateinit var photo: Bitmap

        constructor(photo: Bitmap): this() {
            this.photo = photo
        }

        override fun doInBackground(vararg params: Unit?) {

            UserApiManager.changeProfilePic(photo) {
                CurrentUser.instance.fromUser(it)
            }
        }

    }

    //Load image from memory async
    private inner class LoadImageDataTask() : AsyncTask<Unit, Unit, Bitmap?>() {

        private lateinit var contentUri: Uri

        constructor(uri: Uri) : this() {
            this.contentUri = uri
        }

        override fun doInBackground(vararg params: Unit?): Bitmap? {

            val imageStream = contentResolver.openInputStream(contentUri)
            return BitmapFactory.decodeStream(imageStream)
        }
    }

}
