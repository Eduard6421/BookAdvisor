package com.cristidospra.bookadvisor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar

class MainActivity : NavigationMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //supportActionBar?.setDisplayHomeAsUpEnabled(false)

        /*toggle.isDrawerIndicatorEnabled = false

         toggle.setToolbarNavigationClickListener {

             print("ceva")

         }*/
    }
}
