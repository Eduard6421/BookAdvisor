package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Models.Review
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

class ReviewAdapter(private val reviews: ArrayList<Review>, private val onPersonClickListener: OnPersonClickListener): RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private lateinit var usedContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_listitem_layout, parent, false)
        this.usedContext = parent.context

        return ReviewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reviews.count()
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {

        val review = reviews[position]

        Utils.loadPersonImage(usedContext, holder.profilePicImageView, review.user.profilePic())
        holder.userNameTextView.text = review.user.fullName()
        holder.postedDateTextView.text = review.timeStamp()
        holder.ratingBar.rating = review.givenRating
        holder.contentTextView.text = review.text

        holder.itemView.setOnClickListener {
            onPersonClickListener.onPersonClick(review.user)
        }
    }

    class ReviewViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val profilePicImageView: ImageView = view.findViewById(R.id.review_listitem_profilepic)
        val userNameTextView: TextView = view.findViewById(R.id.review_listitem_name)
        val postedDateTextView: TextView = view.findViewById(R.id.review_listitem_post_date)
        val ratingBar: RatingBar = view.findViewById(R.id.review_listitem_rating)
        val contentTextView: TextView = view.findViewById(R.id.review_listitem_content)
    }

    interface OnPersonClickListener {
        fun onPersonClick(user: User)
    }
}