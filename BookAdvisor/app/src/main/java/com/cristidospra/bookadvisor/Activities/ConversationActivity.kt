package com.cristidospra.bookadvisor.Activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R


class ConversationActivity : NavigationMenuActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var messageSendButton: ImageButton
    private lateinit var messageList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        inflateViews()
    }

    private fun inflateViews() {

        messageEditText = findViewById(R.id.conversation_editText)
        messageSendButton = findViewById(R.id.conversation_send_button)
        messageList = findViewById(R.id.conversation_messages_list_view)
    }

}
