package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class FindPeopleActivity : NavigationMenuActivity() {

    lateinit var followingSearchEditText: EditText
    lateinit var followingPeopleRecyclerView: RecyclerView
    lateinit var newPeopleSearchEditText: EditText
    lateinit var newPeopleRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_people)

        inflateViews()
    }

    private fun inflateViews() {

        followingSearchEditText = findViewById(R.id.people_search_following)
        followingPeopleRecyclerView = findViewById(R.id.people_following_recyclerview)
        newPeopleSearchEditText = findViewById(R.id.people_search_new)
        newPeopleRecyclerView = findViewById(R.id.people_new_recyclerview)
    }
}
