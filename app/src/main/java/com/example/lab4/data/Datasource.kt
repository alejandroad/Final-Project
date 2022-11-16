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
        R.drawable.img_8,
        R.drawable.img_9,
        R.drawable.img_10,
        R.drawable.img_11,
        R.drawable.img_12,
        R.drawable.img_13,
        R.drawable.img_14,
        R.drawable.img_15,
        R.drawable.img_16,
        R.drawable.img_17,
        R.drawable.img_18,
        R.drawable.img_19,
        R.drawable.img_20,
        R.drawable.img_21,
        R.drawable.img_22,
        R.drawable.img_23,
        R.drawable.img_24,
        R.drawable.img_25,
        R.drawable.img_26,
        R.drawable.img_27,
        R.drawable.img_28,
        R.drawable.img_29,
    )

    val albumLibrary: List<Album> = listOf<Album>(
        Album(
            images_1,
            "Test Album"
        ), Album(
            images_1,
            "Foo Album"
        ), Album(
            images_1,
            "Bar Album"
        ), Album(
            images_1,
            "Fourth Album"
        )
    )

}