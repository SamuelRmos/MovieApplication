package com.example.retrofitkotlin.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageBinding {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUrl(view: ImageView, url: String){
        Glide.with(view.context).load(Constants.baseUrlImage+url).into(view)
    }
}