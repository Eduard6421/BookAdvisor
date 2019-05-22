package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.CategoriesAdapter
import com.cristidospra.bookadvisor.Models.Genre
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.BookApiManager
import com.cristidospra.bookadvisor.R

class CategoriesActivity : NavigationMenuActivity() {

    lateinit var categoriesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        inflateViews()

        BookApiManager.getCategories {

            categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
            categoriesRecyclerView.adapter = CategoriesAdapter(it, object : CategoriesAdapter.OnCategoryClickListener {
                override fun onCategoryClick(category: Genre) {

                    val intent = Intent(this@CategoriesActivity, GenreActivity::class.java)
                    intent.putExtra("genre", category)
                    this@CategoriesActivity.startActivity(intent)
                }
            })
        }
    }

    private fun inflateViews() {
        categoriesRecyclerView = findViewById(R.id.recommendations_recyclerview)
    }
}
