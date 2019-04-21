package com.cristidospra.bookadvisor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cristidospra.bookadvisor.Models.*
import com.cristidospra.bookadvisor.Networking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity: AppCompatActivity() {

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.main_button)

        Review()

        BookApiManager.addReview(Book(1), Review(CurrentUser.instance, givenRating = 3.5f, text = "In the university world, those who give hard exams are regarded as scum. But, those who give ML projects to their students in their last year are worse than scum"))
    }
}
