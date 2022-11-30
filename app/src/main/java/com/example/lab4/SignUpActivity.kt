package com.example.lab4

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.lab4.databinding.ActivityLogInBinding
import com.example.lab4.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivitySignUpBinding

    // Action Bar
    private lateinit var actionBar: ActionBar

    // Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    // Firebase Authentication
    private lateinit var auth: FirebaseAuth

    // Data
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Sign up"
        // enable the back button
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("One moment...")
        progressDialog.setMessage("Creating account")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebase
        auth = Firebase.auth

        // onclick
        binding.SignUpButton.setOnClickListener {
            // validate data
            validateData()
        }
    }

    private fun validateData() {
        email = binding.emailA.text.toString().trim()
        password = binding.PasswordA.text.toString().trim()

        var containsCapitalLetter = false

        for(i in password.indices) {
            if(password[i].isUpperCase()) {
                containsCapitalLetter = true
            }
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // incorrect email semantics
            binding.emailA.error = "Incorrect email semantics"
        } else if (TextUtils.isEmpty(password)) {
            // the user did not input a password
            binding.PasswordA.error = "No password imputed"
        } else if (password.length < 8) {
            // password length is less than 8
            binding.PasswordA.error = "Password must be at least 8 characters long"
        } else if (!containsCapitalLetter) {
            // password must contain one capital letter
            binding.PasswordA.error = "Password must contain at least 1 capital letter"
        }  else {
            createAccount()
        }

    }

    private fun createAccount() {
        // show progress
        progressDialog.show()

        // create account
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {task ->

            if(task.isSuccessful) {
                sendEmailVerification()
                // user created account successfully
                progressDialog.dismiss()
                val user = auth.currentUser
                val email = user!!.email
                Toast.makeText(this, "Account created with $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, ProfileActivity::class.java))
                finish()

            } else {
                // user did not create account successfully
                progressDialog.dismiss()
                Log.d("ERROR_MSG", "user did not create account successfully")
                Toast.makeText(this, "Sign up failed due to ${task.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity
        return super.onSupportNavigateUp()
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }
}