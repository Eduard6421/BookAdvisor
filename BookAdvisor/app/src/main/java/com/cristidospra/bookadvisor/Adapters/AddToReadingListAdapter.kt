package com.cristidospra.bookadvisor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.ReadingList
import com.cristidospra.bookadvisor.R

class AddToReadingListAdapter(private val readingLists: ArrayList<ReadingList>, private val onCheckedChangeListener: OnCheckedChangeListener) :
    RecyclerView.Adapter<AddToReadingListAdapter.ReadingListViewHolder>() {

    private var checkedPositions: ArrayList<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingListViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.add_to_readinglist_listitem_layout, parent, false)

        return ReadingListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return readingLists.count()
    }

    override fun onBindViewHolder(holder: ReadingListViewHolder, position: Int) {

        val readingList = readingLists[position]

        holder.title.text = readingList.title
        holder.nrBooks.text = (readingList.nrOfBooks().toString() + " books")

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->

           onCheckedChangeListener.onCheckChange(readingList, isChecked)
        }

        holder.checkBox.isChecked = checkedPositions.contains(position)
    }


    class ReadingListViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.add_to_readinglist_listitem_name)
        val nrBooks: TextView = view.findViewById(R.id.add_to_readinglist_listitem_nrbooks)
        val checkBox: CheckBox = view.findViewById(R.id.add_to_readinglist_listitem_checkbox)
    }

    interface OnCheckedChangeListener {
        fun onCheckChange(readingList: ReadingList, isChecked: Boolean)
    }
}