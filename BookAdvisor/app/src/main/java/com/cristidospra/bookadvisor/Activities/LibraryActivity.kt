package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class LibraryActivity : NavigationMenuActivity() {

    lateinit var readingListsRecyclerView: RecyclerView
    lateinit var addReadingListButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        inflateViews()
    }


    private fun inflateViews() {

        readingListsRecyclerView = findViewById(R.id.library_reading_lists_recyclerview)
        addReadingListButton = findViewById(R.id.library_add_reading_list_button)
    }
}
