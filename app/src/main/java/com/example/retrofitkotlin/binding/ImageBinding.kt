package com.example.retrofitkotlin.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.retrofitkotlin.util.Constants

object ImageBinding {
    @JvmStatic
    @BindingAdapter("loadPhoto")
    fun AppCompatImageView.setBackImage(url: String?) {
        url?.let {
            Glide.with(context)
                .load(Constants.baseImageBack + it)
                .centerCrop()
                .into(this)
        }
    }
}