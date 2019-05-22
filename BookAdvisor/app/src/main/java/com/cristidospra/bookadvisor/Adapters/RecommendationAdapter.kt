package com.cristidospra.bookadvisor.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Activities.BookActivity
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Models.Recommendation
import com.cristidospra.bookadvisor.MyApplication
import com.cristidospra.bookadvisor.R

class RecommendationAdapter(private val recommendations: ArrayList<Recommendation>, private val currentContext: Context) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recommendation_listitem_layout, parent, false)

        return RecommendationViewHolder(itemView)    }

    override fun getItemCount(): Int {

        return recommendations.count()
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {

        val recommendation = recommendations[position]

        holder.title.text = recommendation.title

        holder.booksRecyclerView.layoutManager = LinearLayoutManager(MyApplication.appContext)
        holder.booksRecyclerView.adapter = VerticalBookAdapter(recommendation.books, object : VerticalBookAdapter.OnBookClickListener {
            override fun onBookClick(book: Book) {

                val intent = Intent(currentContext, BookActivity::class.java)
                intent.putExtra("book", book)
                currentContext.startActivity(intent)
            }

        })
    }


    class RecommendationViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val title: TextView = view.findViewById(R.id.recommendation_title_text_view)
        val booksRecyclerView: RecyclerView = view.findViewById(R.id.recommendation_books_rv)
    }
}