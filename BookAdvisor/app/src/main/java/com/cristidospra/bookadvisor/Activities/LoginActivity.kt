package com.cristidospra.bookadvisor.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cristidospra.bookadvisor.CurrentUser
import com.cristidospra.bookadvisor.FirebaseManager
import com.cristidospra.bookadvisor.Networking.LoginApiManager
import com.cristidospra.bookadvisor.Networking.UserApiManager
import com.cristidospra.bookadvisor.R
import com.cristidospra.bookadvisor.Utils.Utils
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var signInButton: Button
    lateinit var forgotPasswordTextView: TextView
    lateinit var registerTextView: TextView
    lateinit var rememberMeCheckbox: CheckBox
    lateinit var wrongCredentialsTextView: TextView

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inflateViews()

        firebaseAuth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences(Utils.SHARED_PREF, Context.MODE_PRIVATE)

        if (sharedPreferences.contains(Utils.SHARED_PREF_EMAIL)) {
            emailEditText.setText(sharedPreferences.getString(Utils.SHARED_PREF_EMAIL, ""))
            passwordEditText.setText(sharedPreferences.getString(Utils.SHARED_PREF_PASSWORD, ""))
            rememberMeCheckbox.isChecked = true
        }

        signInButton.setOnClickListener {

            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            LoginApiManager.login(email, password,
                onSuccess = {token ->

                    if (token.isValid()) {

                        if (rememberMeCheckbox.isChecked) {

                            val spEditor = sharedPreferences.edit()

                            spEditor.putString(Utils.SHARED_PREF_EMAIL, email)
                            spEditor.putString(Utils.SHARED_PREF_PASSWORD, password)
                            spEditor.apply()
                        }
                        else {
                            val spEditor = sharedPreferences.edit()

                            spEditor.remove(Utils.SHARED_PREF_EMAIL)
                            spEditor.remove(Utils.SHARED_PREF_PASSWORD)
                            spEditor.apply()
                        }

                        FirebaseManager.signIn(email, password)


                        CurrentUser.instance.authToken = token.token

                        UserApiManager.getUser(email) {user ->

                            CurrentUser.instance.fromUser(user)
                            this.startActivity(Intent(this, LibraryActivity::class.java))

                            finish()
                        }
                    }
                },
                onFailure = {

                    wrongCredentialsTextView.visibility = View.VISIBLE
                })

            Utils.closeKeyboard(this, emailEditText)
            Utils.closeKeyboard(this, passwordEditText)
        }

        forgotPasswordTextView.setOnClickListener {

            this.startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        registerTextView.setOnClickListener {

            this.startActivity(Intent(this, RegisterActivity::class.java))
        }
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
