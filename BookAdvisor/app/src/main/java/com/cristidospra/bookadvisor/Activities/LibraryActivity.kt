package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.ReadingListAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class LibraryActivity : NavigationMenuActivity() {

    lateinit var readingListsRecyclerView: RecyclerView
    lateinit var addReadingListButton: Button

    private var currentLibraryUser: User = CurrentUser.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        inflateViews()

        if (intent.hasExtra("user")) {

            currentLibraryUser = intent.getSerializableExtra("user") as User
        }

        readingListsRecyclerView.layoutManager = LinearLayoutManager(this)
        readingListsRecyclerView.adapter = ReadingListAdapter(currentLibraryUser.readingLists)

        addReadingListButton.setOnClickListener {

            /* TODO:: add reading list */
        }
    }


    private fun inflateViews() {

        readingListsRecyclerView = findViewById(R.id.library_reading_lists_recyclerview)
        addReadingListButton = findViewById(R.id.library_add_reading_list_button)
    }
}
