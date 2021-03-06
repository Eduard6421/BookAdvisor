package com.cristidospra.bookadvisor

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cristidospra.bookadvisor.FirebaseManager.addMessage
import com.cristidospra.bookadvisor.Models.Message
import com.cristidospra.bookadvisor.Models.User

class MainActivity: AppCompatActivity() {

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.main_button)

        FirebaseManager.signIn("cristi@email.com", "Cristi123")


        /*FirebaseManager.getNewMessage(User(firebasUID = "kwx1Mme8pzVpD8dUbuD14zYdz9G3")) {

            Toast.makeText(this, it.content, Toast.LENGTH_LONG).show()
        }*/

        button.setOnClickListener {

            val user1 = User(firebasUID = "i3WAfCUGZCT80TMKDl3bGi14eg32")
            val user2 = User(firebasUID = "QxTXWEHmhqMSbVolcIdpZS0I6Oq1")

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



