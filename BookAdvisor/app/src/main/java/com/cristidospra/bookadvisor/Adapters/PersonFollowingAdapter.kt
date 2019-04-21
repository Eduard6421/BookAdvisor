package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class PersonFollowingAdapter(private val people: ArrayList<User>) : RecyclerView.Adapter<PersonFollowingAdapter.PersonViewHolder>() {

    private lateinit var usedContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.person_following_listitem_layout, parent, false)
        this.usedContext = parent.context

        return PersonViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return people.count()
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {

        val person = people[position]

        Utils.loadImage(usedContext, holder.profilePictureImageView, person.profilePicURL)
        holder.nameTextView.text = person.fullName()
        holder.readingTextView.text = ""
        holder.messageImageView.setOnClickListener {

            /*TODO: go to inbox */
        }
    }


    class PersonViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val profilePictureImageView: ImageView = view.findViewById(R.id.person_following_listitem_profile_pic)
        val nameTextView: TextView = view.findViewById(R.id.person_following_listitem_name)
        val readingTextView: TextView = view.findViewById(R.id.person_following_listitem_reading)
        val messageImageView: ImageView = view.findViewById(R.id.person_following_listitem_message)
    }
}