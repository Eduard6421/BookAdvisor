package com.cristidospra.bookadvisor.Activities

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.ConversationAdapter
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R

class InboxActivity : NavigationMenuActivity() {

    lateinit var messagesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        inflateViews()

        UserApiManager.getConversations {

            messagesRecyclerView.adapter = ConversationAdapter(it)
        }
    }

    private fun inflateViews() {

        messagesRecyclerView = findViewById(R.id.inbox_convo_list)
    }
}
