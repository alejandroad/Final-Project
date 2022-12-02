package com.FaceShift.lab4


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.FaceShift.lab4.databinding.ActivityAddAlbumBinding


class AddAlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAlbumBinding

    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "New Album"

        binding.confirmNewAlbumButton.setOnClickListener {

            val albumTitle: String = binding.albumTitle.text.toString()
            //put album
            startActivity(Intent(this, CameraActivity::class.java).putExtra("album", albumTitle))
        }


    }
}
