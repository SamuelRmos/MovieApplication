package com.example.retrofitkotlin.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.retrofitkotlin.di.Constants
import com.example.retrofitkotlin.di.Constants.baseImageBack

object ImageBinding {

    @JvmStatic
    @BindingAdapter("loadPhoto")
    fun ImageView.setBackImage(url: String?) {
        url?.let {
            Glide.with(context)
                .load(baseImageBack + it)
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    fun ImageView.setDetailImage(url: String?) {
        url?.let {
            Glide.with(context)
                .load(baseImageBack + it)
                .into(this)
        }
    }

    fun artworkUrl(image: String): String = baseImageBack + image
}