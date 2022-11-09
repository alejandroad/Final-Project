package com.example.lab4.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.AlbumActivity
import com.example.lab4.MainActivity
import com.example.lab4.R
import com.example.lab4.data.Datasource
import com.example.lab4.databinding.ActivityAlbumListBinding
import com.example.lab4.databinding.AlbumItemBinding

class AlbumCardAdapter(
    private val context: Context?
    ): RecyclerView.Adapter<AlbumCardAdapter.AlbumCardViewHolder>() {

    //pull database
    val albumLibrary = Datasource.albumLibrary

    private lateinit var listIntent: Intent

    class AlbumCardViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        // Declare and initialize all of the list item UI components
        val albumCover: ImageView? = view?.findViewById(R.id.albumCover)
        val albumName: TextView? = view?.findViewById(R.id.albumTitle)
        val albumBtn: Button? = view?.findViewById(R.id.albumButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCardViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
        val viewList: View = adapterLayout.inflate(R.layout.album_item, parent, false)



        //return view to viewHolder
        return AlbumCardViewHolder(viewList)
    }

    override fun getItemCount(): Int {
        return albumLibrary.size
    }

    override fun onBindViewHolder(holder: AlbumCardAdapter.AlbumCardViewHolder, position: Int) {
        // Get the data at the current position
        val currentAlbum = albumLibrary[position]

        // Set the image resource for the current album cover
        //val resources = context?.resources
        holder.albumCover?.setImageResource(currentAlbum.album[0])
        holder.albumName?.text = currentAlbum.title



    }


}