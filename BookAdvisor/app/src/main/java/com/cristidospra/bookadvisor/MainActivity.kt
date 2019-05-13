package com.cristidospra.bookadvisor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.cristidospra.bookadvisor.FirebaseManager.addMessage
import com.cristidospra.bookadvisor.FirebaseManager.firebaseDatabase
import com.cristidospra.bookadvisor.Models.*
import com.cristidospra.bookadvisor.Networking.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity: AppCompatActivity() {

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.main_button)

        FirebaseManager.signIn("user1@gmail.com", "test123")

        FirebaseManager.getNewMessage(User(firebasUID = "kwx1Mme8pzVpD8dUbuD14zYdz9G3")) {

            Toast.makeText(this, it.content, Toast.LENGTH_LONG).show()
        }

        button.setOnClickListener {

            val user1 = User(firebasUID = "vJgkIRWkTPeybolzvsD9vWrfcts2")
            val user2 = User(firebasUID = "kwx1Mme8pzVpD8dUbuD14zYdz9G3")

            addMessage(user1, user2, Message(sender = user1, content = "test1"))
            //addMessage(User(firebasUID = "kwx1Mme8pzVpD8dUbuD14zYdz9G3"), User(firebasUID = "vJgkIRWkTPeybolzvsD9vWrfcts2"), Message(content = "test2"))
            /*addMessage(User(firebasUID = "duPUjMUpWDV3XsoztTa1fxDHLgf1"), User(firebasUID = "kwx1Mme8pzVpD8dUbuD14zYdz9G3"), Message(content = "test3"))
            addMessage(User(firebasUID = "dbtm05jwWrSb7aKTGy5v6t9M8An2"), User(firebasUID = "tXFTA3rMf4cz3HLUcfdXVfNWmQn2"), Message(content = "test4"))
            addMessage(User(firebasUID = "kwx1Mme8pzVpD8dUbuD14zYdz9G3"), User(firebasUID = "dbtm05jwWrSb7aKTGy5v6t9M8An2"), Message(content = "test5"))
            addMessage(User(firebasUID = "vJgkIRWkTPeybolzvsD9vWrfcts2"), User(firebasUID = "kwx1Mme8pzVpD8dUbuD14zYdz9G3"), Message(content = "test6"))
*/
        }
    }


}



