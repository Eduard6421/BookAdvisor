package com.cristidospra.bookadvisor.Activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cristidospra.bookadvisor.FirebaseManager
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern


class RegisterActivity : AppCompatActivity() {

    var VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    private val PASSWORD_MIN_LENGTH = 6

    lateinit var firstNameEditText: EditText
    lateinit var lastNameEditText: EditText
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var confirmPasswordEditText: EditText
    lateinit var registerButton: Button
    lateinit var registrationMessage: TextView

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inflateViews()

        firebaseAuth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener {

            /*TODO: email already exists case */

            Utils.closeKeyboard(this, emailEditText)
            Utils.closeKeyboard(this, passwordEditText)
            Utils.closeKeyboard(this, confirmPasswordEditText)

            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email)

            if (!matcher.find()) {

                registrationMessage.text = ("Please insert a valid email address")
                registrationMessage.visibility = View.VISIBLE
            }
            else if (password != confirmPasswordEditText.text.toString()) {

                registrationMessage.text = ("The inserted passwords don't match")
                registrationMessage.visibility = View.VISIBLE
            }
            else if (password.length < PASSWORD_MIN_LENGTH) {

                registrationMessage.text = ("The inserted password must be at least ${PASSWORD_MIN_LENGTH} characters long")
                registrationMessage.visibility = View.VISIBLE
            }
            else {

                FirebaseManager.register(email, password, firstName, lastName) {

                    registrationMessage.text = ("Registration successful!")
                    registrationMessage.visibility = View.VISIBLE
                }
            }
        }
    }

    fun inflateViews() {

        firstNameEditText = findViewById(R.id.register_user_first_name)
        lastNameEditText = findViewById(R.id.register_user_last_name)
        emailEditText = findViewById(R.id.register_user_text)
        passwordEditText = findViewById(R.id.register_password_text)
        confirmPasswordEditText = findViewById(R.id.register_confirm_password_text)
        registerButton = findViewById(R.id.register_button)
        registrationMessage = findViewById(R.id.registration_complete_text)
    }

    private fun checkData() {

    }
}
