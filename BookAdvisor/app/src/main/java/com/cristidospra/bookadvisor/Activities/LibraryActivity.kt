package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.ReadingListAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Dialogs.AddReadingListDialog
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R

class LibraryActivity : NavigationMenuActivity() {

    lateinit var readingListsRecyclerView: RecyclerView
    lateinit var readingListsAdapter: ReadingListAdapter

    lateinit var addReadingListButton: Button

    private var currentLibraryUser: User = CurrentUser.instance

    /*TODO: add slide on items? */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)

        title = "My Library"

        inflateViews()

        if (intent.hasExtra("user")) {

            currentLibraryUser = intent.getSerializableExtra("user") as User
        }

        readingListsAdapter = ReadingListAdapter(currentLibraryUser.readingLists, object : ReadingListAdapter.OnReadingListClickListener {
            override fun onReadingListClick(rl: ReadingList) {

                val intent = Intent(this@LibraryActivity, ReadingListActivity::class.java)
                intent.putExtra("reading_list", rl)
                this@LibraryActivity.startActivity(intent)
            }

        })


        readingListsRecyclerView.layoutManager = LinearLayoutManager(this)
        readingListsRecyclerView.adapter = readingListsAdapter

        if (currentLibraryUser.email != CurrentUser.instance.email) {
            addReadingListButton.visibility = View.GONE
        }

        addReadingListButton.setOnClickListener {

            val addReadingListDialog = AddReadingListDialog(this) { title ->

                val validTitle = title.trim()

                if (!CurrentUser.instance.readingLists.map {
                    it.title
                }.contains(validTitle)) {

                    val newReadingList = ReadingList(validTitle)

                    CurrentUser.instance.readingLists.add(newReadingList)
                    UserApiManager.addReadingList(newReadingList)
                    readingListsAdapter.notifyDataSetChanged()
                }
                else {

                    Toast.makeText(this, "There is already a reading list name \"$validTitle\"", Toast.LENGTH_LONG).show()
                }

            }

            addReadingListDialog.show()
        }
    }


    private fun inflateViews() {

        readingListsRecyclerView = findViewById(R.id.library_reading_lists_recyclerview)
        addReadingListButton = findViewById(R.id.library_add_reading_list_button)
    }
}
