package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Activities.ConversationActivity
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.FirebaseManager
import com.cristidospra.bookadvisor.Models.Conversation
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class PersonFollowingAdapter(private val usedContext: Context, private val people: ArrayList<User>, private val onPersonClickListener: OnPersonClickListener) : RecyclerView.Adapter<PersonFollowingAdapter.PersonViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.person_following_listitem_layout, parent, false)

        return PersonViewHolder(itemView)
    }

    override fun getItemCount(): Int {

        return people.count()
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {

        val person = people[position]

        Utils.loadPersonImage(usedContext, holder.profilePictureImageView, person.profilePic())
        holder.nameTextView.text = person.fullName()
        holder.readingTextView.text = ""
        holder.messageImageView.setOnClickListener {

            FirebaseManager.getUserChat(CurrentUser.instance.firebasUID, person.firebasUID) { chatUID ->

                FirebaseManager.getLastMessage(CurrentUser.instance.firebasUID, person.firebasUID, chatUID) { lastMessage ->
                    val conversation = Conversation(chatUID, person, lastMessage)

                    val intent = Intent(usedContext, ConversationActivity::class.java)
                    intent.putExtra("conversation", conversation)
                    usedContext.startActivity(intent)
                }
            }
        }

        holder.itemView.setOnClickListener {
            onPersonClickListener.onPersonClick(person)
        }
    }


    class PersonViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val profilePictureImageView: ImageView = view.findViewById(R.id.person_following_listitem_profile_pic)
        val nameTextView: TextView = view.findViewById(R.id.person_following_listitem_name)
        val readingTextView: TextView = view.findViewById(R.id.person_following_listitem_reading)
        val messageImageView: ImageView = view.findViewById(R.id.person_following_listitem_message)
    }

    interface OnPersonClickListener {
        fun onPersonClick(user: User)
    }
}