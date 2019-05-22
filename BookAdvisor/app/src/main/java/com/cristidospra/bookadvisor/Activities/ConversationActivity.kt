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
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList


class ConversationActivity : NavigationMenuActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var messageSendButton: ImageButton

    private lateinit var messageList: ListView
    private lateinit var messageAdapter: MessageListAdapter
    private var messages: ArrayList<Message> = ArrayList()

    private lateinit var currentConversation: Conversation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        inflateViews()

        if (intent.hasExtra("conversation")) {
            currentConversation = intent.getSerializableExtra("conversation") as Conversation
        }

        /*TODO: get messages from firebase*/
        /*messageAdapter = MessageListAdapter(this, messages)
        messageList.adapter = messageAdapter*/

        messageSendButton.setOnClickListener {

            sendMessage()
        }

        FirebaseManager.getNewMessage(currentConversation.chatUID, currentConversation.user) {

            messageAdapter = MessageListAdapter(this, it)
            messageList.adapter = messageAdapter
            messageAdapter.notifyDataSetChanged()
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
}

/*

-chats
   - chat1_UID
       - members
           - user1_UID
           - user2_UID
           ...
           - userN_UID

       - lastMessageSent: messageUID

   - chat2_UID
       - members
           - user1_UID
           - user2_UID
           ...
           - userN_UID

       - lastMessageSent: messageUID
   ...

   - chatN_UID
       - members
           - user1_UID
           - user2_UID
           ...
           - userN_UID

       - lastMessageSent: messageUID


-chatMessages
   - chat1_UID
     - message1_UID
         - sentBy: userUID
         - messageDate:""
         - messageTime:""
         - message:""

      - message2_UID
         - sentBy: userUID
         - messageDate:""
         - messageTime:""
         - message:""

       ...

       - messageN_UID
         - sentBy: userUID
         - messageDate:""
         - messageTime:""
         - message:""

   - chat2_UID
     - message1_UID
         - sentBy: userUID
         - messageDate:""
         - messageTime:""
         - message:""

      - message2_UID
         - sentBy: userUID
         - messageDate:""
         - messageTime:""
         - message:""

       ...

       - messageN_UID
         - sentBy: userUID
         - messageDate:""
         - messageTime:""
         - message:""
    ...

-userChats
    - user1_UID
       - chat1_UID
       - chat2_UID
       ...
       - chatN_UID

    - user2_UID
       - chat1_UID
       - chat2_UID
       ...
       - chatN_UID
    ...

    - userN_UID
       - chat1_UID
       - chat2_UID
       ...
       - chatN_UID

 */