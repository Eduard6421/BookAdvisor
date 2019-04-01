package com.cristidospra.bookadvisor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.R

class ReadingListAdapter(val readingLists: ArrayList<ReadingList>) : RecyclerView.Adapter<ReadingListAdapter.ReadingListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingListViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.readinglist_listitem_layout, parent, false)

        return ReadingListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return readingLists.count()
    }

    override fun onBindViewHolder(holder: ReadingListViewHolder, position: Int) {

        val readingList = readingLists[position]

        holder.title.text = readingList.title
        holder.nrBooks.text = (readingList.nrOfBooks().toString() + " books")
    }


    class ReadingListViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.readinglist_listitem_name)
        val nrBooks: TextView = view.findViewById(R.id.readinglist_listitem_nrbooks)
    }
}