package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.ConversationAdapter
import com.cristidospra.bookadvisor.FirebaseManager
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R


class InboxActivity : NavigationMenuActivity() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messagesRecyclerViewAdapter: ConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inbox)

        inflateViews()

        messagesRecyclerViewAdapter = ConversationAdapter(ArrayList(), object : ConversationAdapter.OnConversationClickListener {
            override fun onConversationClick(conversation: Conversation) {

                val intent = Intent(this@InboxActivity, ConversationActivity::class.java)
                intent.putExtra("conversation", conversation)
                this@InboxActivity.startActivity(intent)
            }
        })

        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = messagesRecyclerViewAdapter

        FirebaseManager.getConversations { convo ->

            UserApiManager.getUserByFirebase(convo.user.firebasUID) {

                messagesRecyclerViewAdapter.addItem(Conversation(convo.chatUID, it, convo.lastMessage))
                messagesRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun inflateViews() {

        messagesRecyclerView = findViewById(R.id.inbox_convo_list)
    }
}
