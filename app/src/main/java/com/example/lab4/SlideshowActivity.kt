package com.example.lab4;

import android.content.Intent
import android.graphics.BitmapFactory
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.concurrent.TimeUnit

public class SlideshowActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySlideshowBinding
    private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    var position = 0
    var slides: MutableList<String> = mutableListOf()
    var albumName: String = Firebase.auth.currentUser?.uid as String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySlideshowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val auth: FirebaseAuth = Firebase.auth
        var userID = auth.currentUser?.uid
        position = intent.getIntExtra("position", 0)
        val userRef: StorageReference =
            storageRef.child("users/${userID}/albums/")



        userRef.listAll().addOnSuccessListener { result ->
            // go through all the files/folders
            Log.d("slideshow", "In ${result.prefixes[position].path}")
            setName(result.prefixes[position].name)
            result.prefixes[position].listAll().addOnSuccessListener { pics ->
                if (pics.items.isNotEmpty()) {
                    Log.d("slideshow", "first image ${pics.items[0]}")
                }
                for (index in 0..pics.items.size - 1) {
                     addSlide(pics.items[index].path)
                }
            }
        }
        GlobalScope.launch {
            delay(1000L)
            Log.d("slideshow", "images ${slides}")
            playSlideShow()
        }

        binding.restartButton.setOnClickListener {
        playSlideShow()
    }
        binding.backButton.setOnClickListener {
            startActivity(Intent(this, AlbumActivity::class.java), null)
        }
        binding.addButton.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java)
                .putExtra("album", albumName)
                .putExtra("return", true))
        }
        // Enable up button for backward navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun playSlideShow() {
        GlobalScope.launch {

            for (slide in slides) {
                val localFile = File.createTempFile("tempImage", "jpg")
                storageRef.child(slide).getFile(localFile)
                    .addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        binding.imageView!!.setImageBitmap(bitmap)
                        binding.imageView!!.rotation = -90F

                    }
                delay(500L)
            }

        }
    }

    private fun setName(name: String) {
        albumName = name
    }

    private fun addSlide(path: String) {
        slides.add(path)
    }
}