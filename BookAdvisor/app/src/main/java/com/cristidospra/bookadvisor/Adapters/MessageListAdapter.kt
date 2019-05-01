package com.cristidospra.bookadvisor.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.cristidospra.bookadvisor.Models.Message
import com.cristidospra.bookadvisor.R


class MessageListAdapter(var currentContext: Context, var messages: ArrayList<Message> = ArrayList()) : BaseAdapter() {

    fun add(message: Message) {

        this.messages.add(message)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val holder = MessageViewHolder()

        val messageInflater = currentContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val message = messages[position]

        var convertView = convertView

        if (message.belongsToCurrentUser) { // this message was sent by us

            convertView = messageInflater.inflate(R.layout.my_message_listitem_layout, null)
            holder.messageContent = convertView.findViewById(R.id.message_body)
            convertView.tag = holder
            holder.messageContent.text = message.content

        } else { // this message was sent by someone else

            convertView = messageInflater.inflate(R.layout.their_message_listiem_layout, null)
            holder.messageContent = convertView.findViewById(R.id.message_body)
            convertView.tag = holder
            holder.messageContent.text = message.content

        }

        return convertView
    }

    override fun getItem(position: Int): Any {
        return messages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return messages.count()
    }

    class MessageViewHolder {

        lateinit var messageContent: TextView
    }
}