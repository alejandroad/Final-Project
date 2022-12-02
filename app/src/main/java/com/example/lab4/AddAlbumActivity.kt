package com.example.lab4


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat.startActivity
import com.example.lab4.databinding.ActivityAddAlbumBinding
import com.example.lab4.data.Datasource
import com.example.lab4.data.Datasource.albumLibrary
import com.example.lab4.data.Datasource.images_1
import com.example.lab4.model.Album


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
