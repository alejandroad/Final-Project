package com.example.lab4.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab4.AlbumActivity
import com.example.lab4.MainActivity
import com.example.lab4.R
import com.example.lab4.data.Datasource
import com.example.lab4.databinding.ActivityAlbumListBinding
import com.example.lab4.databinding.AlbumItemBinding
import java.lang.ref.WeakReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class AlbumCardAdapter(
    private val context: Context?,
    val listener: Listener?
    ): RecyclerView.Adapter<AlbumCardAdapter.AlbumCardViewHolder>() {

    //pull database
    val list : Listener? = listener
    private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCardViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
        val viewList: View = adapterLayout.inflate(R.layout.album_item, parent, false)
        Log.d("On create view holder", "in creating view holder")

        //return view to viewHolder
        return AlbumCardViewHolder(viewList, this.list)
    }

    override fun getItemCount(): Int {
        var count = 1
        storageRef.child("users/${auth.currentUser?.uid}/albums/").listAll().addOnSuccessListener { result ->
            // go through all the files/folders

            Log.d("get item count", "${result.items.size}")

        }
                .addOnFailureListener { except ->
                   Log.d("get Item Count", "Album size 0")
                }
        return count
    }



    override fun onBindViewHolder(holder: AlbumCardAdapter.AlbumCardViewHolder, position: Int) {
        Log.d("OnBindViewholder", "In bind view holder")
        var userID = auth.currentUser?.uid

        val userRef: StorageReference =
            storageRef.child("users/${userID}/albums/")
        userRef.listAll().addOnSuccessListener { result ->
                // go through all the files/folders
            Log.d("album card adapter", "In ${result.prefixes[position].path}")

                result.prefixes[position].listAll().addOnSuccessListener {pics ->
                    if (pics.items.isNotEmpty()) {
                        Log.d("album cover", "cover: ${pics.items[0]}")
                        Glide.with(context!!)
                            .load(pics.items[0])
                            .into(holder.albumCover!!)
                    }
                }
                    .addOnFailureListener { except ->
                        Log.d("album retrieve error", "${except}")
                    }
                holder.albumName?.text = result.prefixes[position].name
            }
            .addOnFailureListener {
                Toast.makeText(context, "Unable to fetch album!", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }

        // Set the image resource for the current album cover
        //val resources = context?.resources
        holder.albumBtn?.setOnClickListener(holder)
    }


    class AlbumCardViewHolder(view: View?, list: Listener?): RecyclerView.ViewHolder(view!!), View.OnClickListener{
        // Declare and initialize all of the list item UI component
        var albumCover: ImageView? = view?.findViewById(R.id.albumCover)
        var albumName: TextView? = view?.findViewById(R.id.albumTitle)
        var listener: WeakReference<Listener>? = WeakReference<Listener>(list)
        var albumBtn: Button? = view?.findViewById(R.id.albumButton)

        override fun onClick(p0: View?) {
            Log.d("Album Screen", "${this.adapterPosition} Album Clicked")
            listener?.get()?.onButtonClicked(p0!!, this.adapterPosition)
        }

    }
}