package com.example.retrofitkotlin.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageBinding {
    @JvmStatic
    @BindingAdapter("loadPhoto")
    fun ImageView.setBackImage(url: String?) {
        url?.let {
            Glide.with(context)
                .load(Constants.baseImageBack + it)
                .into(this)
        }
    }
}