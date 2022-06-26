package com.greensakthi.administrator.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.greensakthi.administrator.home.MainActivity
import com.greensakthi.administrator.R
import com.greensakthi.administrator.utils.AppPreferences

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    lateinit var edtEmailAddress: TextInputEditText
    lateinit var edtPassword: TextInputEditText

    lateinit var txtLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        AppPreferences.init(this)

        edtEmailAddress = findViewById(R.id.edtEmailAddress)
        edtPassword = findViewById(R.id.edtPassword)

        txtLogin = findViewById(R.id.txt_login)

        auth = Firebase.auth

        txtLogin.setOnClickListener {

            val emailID = edtEmailAddress.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if(emailID.isEmpty())
                Toast.makeText(this, "Please enter Email ID", Toast.LENGTH_LONG).show()
            else if(password.isEmpty())
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_LONG).show()
            else {

                // all set - moving user to sign in to Firebase
                auth.signInWithEmailAndPassword(emailID, password)
                    .addOnCompleteListener {
                       if(it.isSuccessful) {

                           // Setting LOGGED_IN true
                           AppPreferences.isLogin = true

                           // user signed in successfully - move user to Main Activity
                           val intent = Intent(this, MainActivity::class.java)
                           startActivity(intent)

                       } else {

                           // invalid credentials
                           Toast.makeText(this, "Invalid Credentials. Please try again!", Toast.LENGTH_LONG).show()

                       }
                }


            }

        }



    }
}