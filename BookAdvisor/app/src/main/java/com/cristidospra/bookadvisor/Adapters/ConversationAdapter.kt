package com.cristidospra.bookadvisor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.R

class ConversationAdapter(private val conversations: ArrayList<Conversation>): RecyclerView.Adapter<ConversationAdapter.ConversationHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inbox_listitem_layout, parent, false)

        return ConversationHolder(itemView)
    }

    override fun getItemCount(): Int {
        return conversations.count()
    }

    override fun onBindViewHolder(holder: ConversationHolder, position: Int) {

        val conversation = conversations[position]

        //holder.profilePicImageView -- cu glide
        holder.nameTextView.text = conversation.user.fullName()
        holder.dateTextView.text = conversation.lastMessage.sentDate.toString()
    }


    class ConversationHolder(view: View): RecyclerView.ViewHolder(view) {

        val profilePicImageView: ImageView = view.findViewById(R.id.inbox_item_profilepic)
        val nameTextView: TextView = view.findViewById(R.id.inbox_item_name_textview)
        val dateTextView: TextView = view.findViewById(R.id.inbox_item_last_date)
    }
}