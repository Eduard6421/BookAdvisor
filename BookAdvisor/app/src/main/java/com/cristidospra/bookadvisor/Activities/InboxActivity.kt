package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class InboxActivity : NavigationMenuActivity() {

    lateinit var messagesListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        inflateViews()
    }

    private fun inflateViews() {

        messagesListView = findViewById(R.id.inbox_convo_list)
    }
}
