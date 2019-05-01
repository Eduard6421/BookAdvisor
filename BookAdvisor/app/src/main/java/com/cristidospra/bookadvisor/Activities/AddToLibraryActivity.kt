package com.cristidospra.bookadvisor.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.AddToReadingListAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R

class AddToLibraryActivity : NavigationMenuActivity() {

    lateinit var readingListsRecyclerView: RecyclerView
    lateinit var submitButton: Button

    private lateinit var currentBook: Book

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_library)

        inflateViews()

        currentBook = intent.getSerializableExtra("book") as Book

        val selectedReadingLists: ArrayList<ReadingList> = ArrayList()

        readingListsRecyclerView.layoutManager = LinearLayoutManager(this)
        readingListsRecyclerView.adapter = AddToReadingListAdapter(CurrentUser.instance.readingLists, object : AddToReadingListAdapter.OnCheckedChangeListener {
            override fun onCheckChange(readingList: ReadingList, isChecked: Boolean) {

                if (isChecked) {
                    selectedReadingLists.add(readingList)
                }
                else {
                    selectedReadingLists.remove(readingList)
                }
            }

        })

        submitButton.setOnClickListener {

            for (rl in selectedReadingLists) {

                rl.add(currentBook)
                UserApiManager.updateReadingList(rl)
            }

            Toast.makeText(this, "Book successfully added to selected reading lists!", Toast.LENGTH_LONG).show()

            finish()
        }
    }

    private fun inflateViews() {

        readingListsRecyclerView = findViewById(R.id.add_to_reading_lists_recyclerview)
        submitButton = findViewById(R.id.add_to_readin_lists_confirm)
    }
}
