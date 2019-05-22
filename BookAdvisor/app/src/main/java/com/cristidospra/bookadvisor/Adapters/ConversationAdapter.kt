package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class ConversationAdapter(private val conversations: ArrayList<Conversation>, private val onConversationClickListener: OnConversationClickListener): RecyclerView.Adapter<ConversationAdapter.ConversationHolder>() {

    private lateinit var usedContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inbox_listitem_layout, parent, false)
        this.usedContext = parent.context

        return ConversationHolder(itemView)
    }

    override fun getItemCount(): Int {
        return conversations.count()
    }

    override fun onBindViewHolder(holder: ConversationHolder, position: Int) {

        val conversation = conversations[position]

        UserApiManager.getUserByFirebase(conversation.user.firebasUID) {

            Utils.loadPersonImage(usedContext, holder.profilePicImageView, it.profilePic())
            holder.nameTextView.text = it.fullName()
            holder.messageTextView.text = conversation.lastMessage?.content ?: ""
            holder.dateTextView.text = conversation.lastMessage?.timeStamp()
        }

        holder.itemView.setOnClickListener {
            onConversationClickListener.onConversationClick(conversation)
        }
    }

    fun addItem(conversation: Conversation) {

        conversations.add(conversation)
        notifyDataSetChanged()
    }

    class ConversationHolder(view: View): RecyclerView.ViewHolder(view) {

        val profilePicImageView: ImageView = view.findViewById(R.id.inbox_item_profilepic)
        val nameTextView: TextView = view.findViewById(R.id.inbox_item_name_textview)
        val messageTextView: TextView = view.findViewById(R.id.inbox_item_last_message)
        val dateTextView: TextView = view.findViewById(R.id.inbox_item_last_date)
    }

    interface OnConversationClickListener {
        fun onConversationClick(conversation: Conversation)
    }
}