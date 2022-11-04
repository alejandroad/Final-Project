package com.example.lab4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonIntent: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraBtn.setOnClickListener() { launchCameraActivity() }
        binding.profileBtn.setOnClickListener() { launchProfileActivity() }

    }

    private fun launchProfileActivity() {
        buttonIntent = Intent(this, CameraActivity::class.java)
        startActivity(buttonIntent)
    }

    private fun launchCameraActivity() {
        buttonIntent = Intent(this, CameraActivity::class.java)
        startActivity(buttonIntent)
    }
}