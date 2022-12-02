package com.FaceShift.lab4

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.FaceShift.lab4.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogInActivity : AppCompatActivity() {

    // Declare auth
    private lateinit var auth:FirebaseAuth

    // view binding
    private lateinit var binding: ActivityLogInBinding

    // action bar
    private lateinit var actionBar: ActionBar

    // process dialog
    private lateinit var progressDialog:ProgressDialog

    // account data
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // configure action bar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        // configure process dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("One moment...")
        progressDialog.setMessage("Logging in")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebase auth
        auth = Firebase.auth

        checkUser()

        // handle user is trying to sign up
        binding.noAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // handle user forgot their password
        binding.forgotPassword.setOnClickListener {
            auth.sendPasswordResetEmail("alejandroariasdiaz09@gmail.com")
        }

        // user was not logged in
        binding.loginButton.setOnClickListener {
            // before trying to log in, validate data
            validateData()
        }
    }

    private fun validateData() {
        email = binding.emailA.text.toString().trim()
        password = binding.PasswordA.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // incorrect email semantics
            binding.emailA.error = "Incorrect email semantics"
        } else if (TextUtils.isEmpty(password)) {
            // the user did not input a password
            binding.PasswordA.error = "No password imputed"
        } else {
            // data is validated
            signIn()
        }
    }

    private fun signIn() {
        // show progress bar
        progressDialog.show()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->

            if(task.isSuccessful) {
                // successful login
                progressDialog.dismiss()

                // get user info
                val user = auth.currentUser
                val email = auth.currentUser?.email

                Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT).show()
                Log.d("SUCCESS_MSG", "createUserWithEmail:success")

                // open profile activity
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            } else {
                // unsuccessful
                progressDialog.dismiss()
                Toast.makeText(this, "Log in failed due to ${task.exception}", Toast.LENGTH_SHORT).show()
                Log.w("ERROR_MSG", "createUserWithEmail:failure", task.exception)
            }
        }
    }

    private fun checkUser() {
        // if the user is already logged in, go to profile activity that has navigation
        // to the rest of the application

        val currentUser = auth.currentUser
        if(currentUser != null) {
            // this means they are logged in
            // proceed to profile activity
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}