package com.example.lab4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.lab4.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityProfileBinding

    // Action Bar
    private lateinit var actionBar: ActionBar

    // Firebase
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // config action bar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        // init firebase
        auth = Firebase.auth
        checkUser()

        // logout handler
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        val user = auth.currentUser

        if(user != null) {
            // user is logged in
            val email = user.email
            binding.welcomeTV.text = email
        } else {
            // user is not logged in
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }
}