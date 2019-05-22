package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.request.RequestOptions
import com.cristidospra.bookadvisor.Adapters.VerticalBookAdapter
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.BookApiManager
import com.cristidospra.bookadvisor.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class ScanBookActivity : NavigationMenuActivity() {

    lateinit var photoImageView: ImageView
    lateinit var booksRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_book)

        inflateViews()

        photoImageView.setOnClickListener {

            startCropImageActivity()
        }
    }

    private fun inflateViews() {

        photoImageView = findViewById(R.id.scan_book_photo)
        booksRecyclerView = findViewById(R.id.scan_book_possible_books)
    }


    private fun startCropImageActivity() {

        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this@ScanBookActivity)
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
                    .apply(RequestOptions().transforms(CenterInside()))
                    .into(photoImageView)

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

            BookApiManager.sendScannedBook(photo) {

                booksRecyclerView.layoutManager = LinearLayoutManager(this@ScanBookActivity, LinearLayoutManager.HORIZONTAL, false)
                booksRecyclerView.adapter = VerticalBookAdapter(it, object : VerticalBookAdapter.OnBookClickListener {
                    override fun onBookClick(book: Book) {

                        val intent = Intent(this@ScanBookActivity, BookActivity::class.java)
                        intent.putExtra("book", book)
                        this@ScanBookActivity.startActivity(intent)
                    }
                })
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
