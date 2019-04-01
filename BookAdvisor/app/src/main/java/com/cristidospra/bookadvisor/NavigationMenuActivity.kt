package com.cristidospra.bookadvisor

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


open class NavigationMenuActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout
    private lateinit var drawerLayout: DrawerLayout
    protected lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private lateinit var navigationHeaderView: View

    companion object {

        var currentActivity: Int = R.id.navigation_my_library

        private const val CALENDAR_ITEM = Menu.FIRST
        private const val EMPLOYEES_ITEM = Menu.FIRST + 1
        private const val CLIENTS_ITEM = Menu.FIRST + 2
        private const val MY_PROFILE_ITEM = Menu.FIRST + 3
    }

    //Force app to be in portrait orientation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setCustomView(R.layout.action_bar_layout)

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
                /*R.id.navigation_calendar -> createIntent(CalendarActivity::class.java)
                R.id.navigation_employees -> createIntent(EmployeesActivity::class.java)
                R.id.navigation_clients -> createIntent(ClientsActivity::class.java)
                R.id.navigation_my_profile -> createIntent(ProfileActivity::class.java)*/
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        navigationHeaderView = navigationView.getHeaderView(0)
        val profilePic = navigationHeaderView.findViewById<ImageView>(R.id.navigation_profile_pic)
        val emailTextView = navigationHeaderView.findViewById<TextView>(R.id.navigation_name)

        // Add profile picture
       /* Glide.with(this)
            .load(CurrentUser.instance.profilePic())
            .apply(RequestOptions().placeholder(R.drawable.ic_default_profile))
            .apply(RequestOptions().transforms(CenterInside(), CircleCrop()))
            .into(profilePic)*/

        // Click on profile picture sends you to "my profile"
        profilePic.setOnClickListener {

            resetAllMenuItemsColor(navigationView)
            setColorForMenuItem(navigationView.menu.findItem(R.id.navigation_scan_books), R.color.colorNavActive)
            //createIntent(ProfileActivity::class.java)
        }

        //emailTextView.text = CurrentUser.instance.workEmail

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
