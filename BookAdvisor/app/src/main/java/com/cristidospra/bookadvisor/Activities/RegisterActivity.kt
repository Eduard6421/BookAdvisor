package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.cristidospra.bookadvisor.R

class RegisterActivity : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var confirmPasswordEditText: EditText
    lateinit var registerButton: Button
    lateinit var registrationMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inflateViews()
    }

    fun inflateViews() {

        emailEditText = findViewById(R.id.register_user_text)
        passwordEditText = findViewById(R.id.register_password_text)
        confirmPasswordEditText = findViewById(R.id.register_confirm_password_text)
        registerButton = findViewById(R.id.register_button)
        registrationMessage = findViewById(R.id.registration_complete_text)
    }
}
