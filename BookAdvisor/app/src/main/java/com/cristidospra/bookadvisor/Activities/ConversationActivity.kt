package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cristidospra.bookadvisor.NavigationMenuActivity
import com.cristidospra.bookadvisor.R

class ConversationActivity : NavigationMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
    }
}
