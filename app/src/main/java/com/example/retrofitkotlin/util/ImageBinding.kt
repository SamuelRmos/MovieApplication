package com.example.retrofitkotlin.util

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageBinding {
    @JvmStatic
    @BindingAdapter("loadPhoto")
    fun AppCompatImageView.setBackImage(url: String?) {
        url?.let {
            Glide.with(context)
                .load(Constants.baseImageBack + it)
                .into(this)
        }
    }
}