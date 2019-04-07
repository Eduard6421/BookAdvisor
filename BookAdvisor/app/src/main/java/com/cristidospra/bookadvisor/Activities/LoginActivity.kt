package com.cristidospra.bookadvisor.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.cristidospra.bookadvisor.Models.User
import com.cristidospra.bookadvisor.Networking.ApiClient
import com.cristidospra.bookadvisor.Networking.UserApiInterface
import com.cristidospra.bookadvisor.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LoginActivity : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var signInButton: Button
    lateinit var forgotPasswordTextView: TextView
    lateinit var registerTextView: TextView
    lateinit var rememberMeCheckbox: CheckBox
    lateinit var wrongCredentialsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inflateViews()

    }

    private fun inflateViews() {

        emailEditText = findViewById(R.id.login_user_text)
        passwordEditText = findViewById(R.id.login_password_text)
        signInButton = findViewById(R.id.login_button)
        forgotPasswordTextView = findViewById(R.id.forgot_password_text)
        registerTextView = findViewById(R.id.register_textview)
        rememberMeCheckbox = findViewById(R.id.remember_me_checkbox)
        wrongCredentialsTextView = findViewById(R.id.invalid_credentials_text)
    }

}
