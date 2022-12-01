package com.example.lab4;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import android.view.View
import android.widget.Toast
import com.example.lab4.adapter.AlbumCardAdapter
import com.example.lab4.adapter.Listener
import kotlinx.coroutines.*
import com.example.lab4.data.Datasource
import com.example.lab4.databinding.ActivitySlideshowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

public class SlideshowActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlideshowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlideshowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth: FirebaseAuth = Firebase.auth
        GlobalScope.launch {
            for (images in Datasource.images_1) {
                Log.d("Slideshow", ".")
                var ref = intent.getStringExtra("storageRef")

                binding.imageView.setImageResource(images)
                delay(500L)
            }
        }
        binding.backButton.setOnClickListener {
        startActivity(Intent(this, AlbumActivity::class.java))
    }
        binding.shareButton.setOnClickListener {
            startActivity(Intent(this, AlbumActivity::class.java))
        }
        // Enable up button for backward navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}