package com.cristidospra.bookadvisor

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.cristidospra.bookadvisor.Activities.*
import com.cristidospra.bookadvisor.Adapters.HorizontalBookAdapter
import com.cristidospra.bookadvisor.Dialogs.SearchedBooksDialog
import com.cristidospra.bookadvisor.Models.Book
import com.cristidospra.bookadvisor.Networking.BookApiManager
import com.cristidospra.bookadvisor.Utils.Utils
import com.google.android.material.navigation.NavigationView


open class NavigationMenuActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout
    private lateinit var drawerLayout: DrawerLayout
    protected lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navigationHeaderView: View

    protected lateinit var searchField: EditText
    protected lateinit var searchIcon: ImageView
    protected lateinit var messagesIcon: ImageView

    companion object {

        var currentActivity: Int = R.id.navigation_my_library

        private const val LIBRARY_ITEM = Menu.FIRST
        private const val CATEGORIES_ITEM = Menu.FIRST + 1
        private const val RECOMMENDED_ITEM = Menu.FIRST + 2
        private const val SCAN_ITEM = Menu.FIRST + 3
        private const val PEOPLE_ITEM = Menu.FIRST + 4
        private const val SETTINGS_ITEM = Menu.FIRST + 5
    }

    //Force app to be in portrait orientation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

        searchField = findViewById(R.id.actionbar_search_field)
        searchIcon = findViewById(R.id.actionbar_search_icon)
        messagesIcon = findViewById(R.id.actionbar_messages_icon)

        searchField.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                var handled = false

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    val booksDialog = SearchedBooksDialog(this@NavigationMenuActivity, searchField.text.toString())
                    booksDialog.show()

                    handled = true
                }
                return handled
            }

        })


        searchIcon.setOnClickListener {

            if (searchField.visibility == View.GONE) {
                searchField.visibility = View.VISIBLE
            }
            else {
                searchField.visibility = View.GONE
            }
        }

        messagesIcon.setOnClickListener {
            createIntent(InboxActivity::class.java)
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun setContentView(layoutResId: Int) {

        drawerLayout = layoutInflater.inflate(R.layout.activity_navigation_menu, null) as DrawerLayout
        frameLayout = drawerLayout.findViewById(R.id.navigation_frame)

        layoutInflater.inflate(layoutResId, frameLayout, true)

        super.setContentView(drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Closed)

        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView = drawerLayout.findViewById(R.id.navigation_view)


        // Triggered when an item is clicked
        navigationView.setNavigationItemSelectedListener {

            // Highlight the selected item
            resetAllMenuItemsColor(navigationView)
            setColorForMenuItem(it, R.color.colorNavActive)

            // Start the specific activity for the selected item
            when (it.itemId) {
                R.id.navigation_my_library -> createIntent(LibraryActivity::class.java)
                R.id.navigation_explore_categories -> createIntent(CategoriesActivity::class.java)
                R.id.navigation_recommended_books -> createIntent(RecommendedActivity::class.java)
                R.id.navigation_scan_books -> createIntent(ScanBookActivity::class.java)
                R.id.navigation_find_people -> createIntent(FindPeopleActivity::class.java)
                R.id.navigation_settings -> createIntent(SettingsActivity::class.java)
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        navigationHeaderView = navigationView.getHeaderView(0)
        val profilePic = navigationHeaderView.findViewById<ImageView>(R.id.navigation_profile_pic)
        val nameTextView = navigationHeaderView.findViewById<TextView>(R.id.navigation_name)


        Utils.loadPersonImage(this, profilePic, CurrentUser.instance.profilePic())

        // Click on profile picture sends you to "my profile"
        profilePic.setOnClickListener {

            resetAllMenuItemsColor(navigationView)
            createIntent(ProfileActivity::class.java)
        }

        nameTextView.text = CurrentUser.instance.fullName()

        if (navigationView.menu.findItem(currentActivity) != null) {
            setColorForMenuItem(navigationView.menu.findItem(currentActivity), R.color.colorNavActive)
        }
    }




    /** Navigate to the given activity
     * @param cls Activity to start
     */
    private fun createIntent(cls:Class<*>) {

        val intent = Intent(this, cls)
        this.startActivity(intent)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }




    /** Set the color of a certain menu item
     *
     * @param menuItem Selected MenuItem object
     * @param color Color resource id
     *
     */
    private fun setColorForMenuItem(menuItem: MenuItem, @ColorRes color: Int) {

        val spanString = SpannableString(menuItem.title.toString())
        spanString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length, 0)
        menuItem.title = spanString
        menuItem.icon.setColorFilter(ContextCompat.getColor(this, color), PorterDuff.Mode.SRC_IN)
        currentActivity = menuItem.itemId
    }



    /** Reset the color of all list items to default
     *
     * @param navigationView NavigationView containing items to reset
     */
    private fun resetAllMenuItemsColor(navigationView: NavigationView) {
        for (i in 0 until navigationView.menu.size()) {
            setColorForMenuItem(navigationView.menu.getItem(i), R.color.colorNavInactive)
        }
    }

}
