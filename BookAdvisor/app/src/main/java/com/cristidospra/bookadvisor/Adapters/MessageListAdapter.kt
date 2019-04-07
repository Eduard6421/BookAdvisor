package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.cristidospra.bookadvisor.Models.Message

class MessageListAdapter(private val myContext: Context, private val messages: ArrayList<Message>) : ArrayAdapter<Message>(myContext, 0, messages) {

}