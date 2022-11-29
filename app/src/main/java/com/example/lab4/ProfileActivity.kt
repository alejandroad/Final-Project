package com.example.lab4

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatDelegate
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


        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.darkModeSwitch.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.darkModeSwitch.isChecked = false
            }
        }

        binding.camera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        binding.albumsBtn.setOnClickListener {
            startActivity(Intent(this, AlbumActivity::class.java))
        }
        binding.darkModeSwitch.setOnClickListener {

            if (binding.darkModeSwitch.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun checkUser() {
        val user = auth.currentUser

        if (user != null) {
            // user is logged in
            val text = "Logged in as ${user.email}"
            binding.loginMsg.text = text
        } else {
            // user is not logged in
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }

    override fun recreate() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        startActivity(intent)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}