package com.example.lab4.data

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.example.lab4.R
import com.example.lab4.model.Album

object Datasource {
    val images_1 = mutableListOf<Int>(
         R.drawable.img_1,
         R.drawable.img_2,
         R.drawable.img_3,
         R.drawable.img_4,
         R.drawable.img_5,
         R.drawable.img_6,
         R.drawable.img_7,
         R.drawable.img_8)

    val albumLibrary: List<Album> = listOf<Album>(
        Album(
            images_1,
            "Test Album"
        )
    )

}