package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.cristidospra.bookadvisor.R

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var sendEmailButton: Button
    lateinit var wrongEmailTextView: TextView
    lateinit var succesContainer: RelativeLayout
    lateinit var succesEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        inflateViews()
    }

    private fun inflateViews() {

        emailEditText = findViewById(R.id.recover_email_field)
        sendEmailButton = findViewById(R.id.send_email_button)
        wrongEmailTextView = findViewById(R.id.email_not_found_text)
        succesContainer = findViewById(R.id.email_recover_succes_container)
        succesEmailTextView = findViewById(R.id.email_recover_success_message_address)
    }
}
