package com.cristidospra.bookadvisor.Dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.Review
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils
import java.util.*

class AddReviewDialog(private val currentContext: Context,
                      private val onSave: (Review) -> Unit

) : Dialog(currentContext) {

    lateinit var ratingBar: RatingBar
    lateinit var reviewText: EditText
    lateinit var saveButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.add_review_dialog)

        inflateViews()

        saveButton.setOnClickListener {

            onSave(Review(CurrentUser.instance, ratingBar.rating, Utils.formatDate(Date()), reviewText.text.toString()))

            dismiss()
        }
    }

    private fun inflateViews() {

        ratingBar = findViewById(R.id.add_review_rating)
        reviewText = findViewById(R.id.add_review_text)
        saveButton = findViewById(R.id.add_review_save)
    }
}