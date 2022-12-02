package com.example.lab4;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.lab4.adapter.AlbumCardAdapter
import com.example.lab4.adapter.Listener
import com.example.lab4.databinding.ActivityAlbumListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


public class AlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumListBinding

    class AlbumListener : Listener {
        override fun onButtonClicked(view: View, pos: Int) {
            startActivity(view.context, Intent(view.context, SlideshowActivity::class.java).putExtra("position", pos), null)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAlbumListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val albumlistener = AlbumListener()
        binding.gridRecyclerView.adapter = AlbumCardAdapter(applicationContext, albumlistener)

        // Specify fixed size to improve performance
        //binding.gridRecyclerView.setHasFixedSize(true)

        // Enable up button for backward navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.newAlbumButton.setOnClickListener {
            startActivity(Intent(this, AddAlbumActivity::class.java))
        }
    }
}