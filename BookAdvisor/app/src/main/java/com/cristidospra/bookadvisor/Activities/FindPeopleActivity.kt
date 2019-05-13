package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.PersonFollowingAdapter
import com.cristidospra.bookadvisor.Adapters.PersonNewAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class FindPeopleActivity : NavigationMenuActivity() {

    lateinit var followingSearchEditText: EditText
    lateinit var followingPeopleRecyclerView: RecyclerView
    lateinit var newPeopleSearchEditText: EditText
    lateinit var newPeopleRecyclerView: RecyclerView

    private lateinit var following: ArrayList<User>
    private lateinit var newPeople: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_people)

        inflateViews()

        following = CurrentUser.instance.following
        /* TODO: endpoint to get new people */

        followingSearchEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                followingPeopleRecyclerView.swapAdapter(PersonFollowingAdapter(filteredUsers(following, s.toString()), object : PersonFollowingAdapter.OnPersonClickListener {
                    override fun onPersonClick(user: User) {

                        val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                        intent.putExtra("user", user)
                        this@FindPeopleActivity.startActivity(intent)
                    }


                }), true)
            }

        })
        followingPeopleRecyclerView.layoutManager = LinearLayoutManager(this)
        followingPeopleRecyclerView.adapter = PersonFollowingAdapter(following, object : PersonFollowingAdapter.OnPersonClickListener {
            override fun onPersonClick(user: User) {

                val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                intent.putExtra("user", user)
                this@FindPeopleActivity.startActivity(intent)
            }
        })

        newPeopleSearchEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                newPeopleRecyclerView.swapAdapter(PersonNewAdapter(filteredUsers(newPeople, s.toString()), object : PersonNewAdapter.OnPersonClickListener {
                    override fun onPersonClick(user: User) {

                        val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                        intent.putExtra("user", user)
                        this@FindPeopleActivity.startActivity(intent)
                    }

                }), true)
            }

        })
        newPeopleRecyclerView.layoutManager = LinearLayoutManager(this)
        newPeopleRecyclerView.adapter = PersonNewAdapter(newPeople, object: PersonNewAdapter.OnPersonClickListener {
            override fun onPersonClick(user: User) {

                val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                intent.putExtra("user", user)
                this@FindPeopleActivity.startActivity(intent)            }

        })
    }

    private fun inflateViews() {

        followingSearchEditText = findViewById(R.id.people_search_following)
        followingPeopleRecyclerView = findViewById(R.id.people_following_recyclerview)
        newPeopleSearchEditText = findViewById(R.id.people_search_new)
        newPeopleRecyclerView = findViewById(R.id.people_new_recyclerview)
    }

    private fun filteredUsers(users: ArrayList<User>, text: String) : ArrayList<User> {

        return users.filter {
            it.fullName().toLowerCase().contains(text.toLowerCase())
        } as ArrayList<User>
    }
}
