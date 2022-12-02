package com.FaceShift.lab4.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.FaceShift.lab4.R
import java.lang.ref.WeakReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import java.io.File
import kotlin.math.absoluteValue


class AlbumCardAdapter(
    private val context: Context?,
    val listener: Listener?
) : RecyclerView.Adapter<AlbumCardAdapter.AlbumCardViewHolder>() {

    //pull database
    val list: Listener? = listener

    private var storageRef: StorageReference = FirebaseStorage.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var cnt = 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumCardViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
        val viewList: View = adapterLayout.inflate(R.layout.album_item, parent, false)
        Log.d("On create view holder", "in creating view holder")
        //return view to viewHolder
        updateCount()
        return AlbumCardViewHolder(viewList, this.list)
    }

    override fun getItemCount(): Int {
        updateCount()
        Log.d("Getting Item Count", "${cnt} ...")
        return cnt
    }
    fun updateCount() {

        val auth: FirebaseAuth = Firebase.auth
        var userID = auth.currentUser?.uid
        Log.d("Updating Count", "updating ...")
        storageRef.child("users/${userID}/albums/").listAll().addOnSuccessListener { result ->
            // go through all the files/folders
                if (result.prefixes.isNotEmpty()) {
                    setCount(result.prefixes.size)
                }
            }

    }

    private fun setCount(size: Int) {
        cnt = size.absoluteValue
    }

    override fun onBindViewHolder(holder: AlbumCardAdapter.AlbumCardViewHolder, position: Int) {
        updateCount()
        Log.d("OnBindViewholder", "In bind view holder")
        var userID = auth.currentUser?.uid

        val userRef: StorageReference =
            storageRef.child("users/${userID}/albums/")


        userRef.listAll().addOnSuccessListener { result ->
            // go through all the files/folders
            Log.d("album card adapter", "In ${result.prefixes[position].path}")

            result.prefixes[position].listAll().addOnSuccessListener { pics ->
                if (pics.items.isNotEmpty()) {
                    Log.e("album cover", "cover: ${pics.items[0]}")

                    val localFile = File.createTempFile("tempImage", "jpg")
                    pics.items[0].getFile(localFile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        holder.albumCover!!.setImageBitmap(bitmap)
                        holder.albumCover!!.rotation = -90F
                    }
                }
            }
                .addOnFailureListener { except ->
                    Log.d("album retrieve error", "${except}")
                }
            holder.albumName?.text = result.prefixes[position].name + " " + result.prefixes.size

        }
            .addOnFailureListener {
                Toast.makeText(context, "Unable to fetch album!", Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            }

        // Set the image resource for the current album cover
        holder.albumBtn?.setOnClickListener(holder)
    }


    class AlbumCardViewHolder(view: View?, list: Listener?) : RecyclerView.ViewHolder(view!!),
        View.OnClickListener {
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