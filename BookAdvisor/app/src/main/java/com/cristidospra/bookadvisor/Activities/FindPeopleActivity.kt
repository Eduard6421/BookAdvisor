package com.cristidospra.bookadvisor.Activities

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cristidospra.bookadvisor.Adapters.PersonFollowingAdapter
import com.cristidospra.bookadvisor.Adapters.PersonNewAdapter
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils

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

        followingSearchEditText.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                var handled = false

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    followingPeopleRecyclerView.swapAdapter(PersonFollowingAdapter(this@FindPeopleActivity, filteredUsers(following, followingSearchEditText.text.toString()), object : PersonFollowingAdapter.OnPersonClickListener {
                        override fun onPersonClick(user: User) {

                            val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                            intent.putExtra("user", user)
                            this@FindPeopleActivity.startActivity(intent)
                        }


                    }), true)

                    Utils.closeKeyboard(this@FindPeopleActivity, followingSearchEditText)
                    handled = true
                }
                return handled
            }

        })

        followingPeopleRecyclerView.layoutManager = LinearLayoutManager(this)
        followingPeopleRecyclerView.adapter = PersonFollowingAdapter(this, following, object : PersonFollowingAdapter.OnPersonClickListener {
            override fun onPersonClick(user: User) {

                val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                intent.putExtra("user", user)
                this@FindPeopleActivity.startActivity(intent)
            }
        })

        newPeopleSearchEditText.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                var handled = false

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (newPeopleSearchEditText.text.toString().isNotEmpty()) {
                        UserApiManager.getUserByName(newPeopleSearchEditText.text.toString()) {

                            newPeopleRecyclerView.swapAdapter(PersonNewAdapter(this@FindPeopleActivity, filteredUsers(newPeople, newPeopleSearchEditText.text.toString()), object : PersonNewAdapter.OnPersonClickListener {
                                override fun onPersonClick(user: User) {

                                    val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                                    intent.putExtra("user", user)
                                    this@FindPeopleActivity.startActivity(intent)
                                }

                            }), true)
                        }
                    }
                    else {
                        UserApiManager.getRecommendedPeople {

                            newPeopleRecyclerView.swapAdapter(PersonNewAdapter(this@FindPeopleActivity, filteredUsers(newPeople, newPeopleSearchEditText.text.toString()), object : PersonNewAdapter.OnPersonClickListener {
                                override fun onPersonClick(user: User) {

                                    val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                                    intent.putExtra("user", user)
                                    this@FindPeopleActivity.startActivity(intent)
                                }

                            }), true)
                        }
                    }

                    Utils.closeKeyboard(this@FindPeopleActivity, newPeopleSearchEditText)

                    handled = true
                }
                return handled
            }
        })

        UserApiManager.getRecommendedPeople {

            newPeople = it
            newPeopleRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            newPeopleRecyclerView.adapter = PersonNewAdapter(this, newPeople, object: PersonNewAdapter.OnPersonClickListener {
                override fun onPersonClick(user: User) {

                    val intent = Intent(this@FindPeopleActivity, ProfileActivity::class.java)
                    intent.putExtra("user", user)
                    this@FindPeopleActivity.startActivity(intent)
                }

            })
        }

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
