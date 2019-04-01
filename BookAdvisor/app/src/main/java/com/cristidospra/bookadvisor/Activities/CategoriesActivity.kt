package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.R

class CategoriesActivity : AppCompatActivity() {

    lateinit var categoriesRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        inflateViews()
    }

    private fun inflateViews() {
        categoriesRecyclerView = findViewById(R.id.categories_genres_recyclerview)
    }
}
