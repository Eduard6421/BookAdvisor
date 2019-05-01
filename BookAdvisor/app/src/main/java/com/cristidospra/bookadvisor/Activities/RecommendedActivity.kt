package com.cristidospra.bookadvisor.Activities

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.RecommendationAdapter
import com.cristidospra.bookadvisor.Models.Recommendation
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R

class RecommendedActivity : NavigationMenuActivity() {

    lateinit var recommendationsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommended)

        inflateViews()

        UserApiManager.getRecommendedBooks {

            recommendationsRecyclerView.layoutManager = LinearLayoutManager(this)
            recommendationsRecyclerView.adapter = RecommendationAdapter(it)
        }
    }

    private fun inflateViews() {

        recommendationsRecyclerView = findViewById(R.id.recommendations_recyclerview)
    }
}
