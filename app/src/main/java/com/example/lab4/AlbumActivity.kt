package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab4.adapter.AlbumCardAdapter
import com.example.lab4.databinding.ActivityAlbumListBinding

public class AlbumActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlbumListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gridRecyclerView.adapter = AlbumCardAdapter(applicationContext)

        // Specify fixed size to improve performance
        binding.gridRecyclerView.setHasFixedSize(true)

        // Enable up button for backward navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}