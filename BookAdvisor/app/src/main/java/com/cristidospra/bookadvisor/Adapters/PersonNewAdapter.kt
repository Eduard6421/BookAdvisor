package com.cristidospra.bookadvisor.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.R

class PersonNewAdapter(private val people: ArrayList<User>) : RecyclerView.Adapter<PersonNewAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.person_new_listitem_layout, parent, false)

        return PersonViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return people.count()
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {

        val person = people[position]

        //holder.profilePictureImageView -- cu glide

        holder.nameTextView.text = person.fullName()
    }


    class PersonViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val profilePictureImageView: ImageView = view.findViewById(R.id.person_new_listitem_picture)
        val nameTextView: TextView = view.findViewById(R.id.person_new_listitem_name)
    }
}