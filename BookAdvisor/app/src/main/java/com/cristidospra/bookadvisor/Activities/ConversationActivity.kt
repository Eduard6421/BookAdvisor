package com.cristidospra.bookadvisor.Activities

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import com.cristidospra.bookadvisor.Adapters.MessageListAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.FirebaseManager
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.Models.Message
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils
import java.util.*


class ConversationActivity : NavigationMenuActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var messageSendButton: ImageButton

    private lateinit var messageList: ListView
    private lateinit var messageAdapter: MessageListAdapter

    private lateinit var currentConversation: Conversation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        inflateViews()

        if (intent.hasExtra("conversation")) {
            currentConversation = intent.getSerializableExtra("conversation") as Conversation
        }


        messageSendButton.setOnClickListener {

            sendMessage()
        }

        FirebaseManager.getNewMessage(currentConversation.chatUID, currentConversation.user) { messages ->

            messages.sortBy { selector(it) }
            messageAdapter = MessageListAdapter(this, messages)
            messageList.adapter = messageAdapter
            messageAdapter.notifyDataSetChanged()
            messageList.post { messageList.setSelection(messageList.count - 1) }
        }

    }

    private fun inflateViews() {

        messageEditText = findViewById(R.id.conversation_editText)
        messageSendButton = findViewById(R.id.conversation_send_button)
        messageList = findViewById(R.id.conversation_messages_list_view)
    }

    private fun sendMessage() {

        if (messageEditText.text.toString().isNotEmpty()) {

            val messageToSend = Message(CurrentUser.instance, currentConversation.user, Date(), messageEditText.text.toString())
            FirebaseManager.addMessage(CurrentUser.instance, currentConversation.user, messageToSend)
            messageEditText.setText("")
            Utils.closeKeyboard(this, messageEditText)
        }
    }

    private fun selector(m: Message): Date = m.sentDate
}
