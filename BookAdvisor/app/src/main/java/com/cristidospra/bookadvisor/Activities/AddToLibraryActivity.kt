package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.AddToReadingListAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R
import kotlinx.android.synthetic.main.activity_add_to_library.*

class AddToLibraryActivity : NavigationMenuActivity() {

    lateinit var readingListsRecyclerView: RecyclerView
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_library)

        inflateViews()

        readingListsRecyclerView.layoutManager = LinearLayoutManager(this)
        readingListsRecyclerView.adapter = AddToReadingListAdapter(CurrentUser.instance.readingLists)

        submitButton.setOnClickListener {

            /*TODO: this */
        }
    }

    private fun inflateViews() {

        readingListsRecyclerView = findViewById(R.id.add_to_reading_lists_recyclerview)
        submitButton = findViewById(R.id.add_to_readin_lists_confirm)
    }
}
