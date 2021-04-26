package com.example.retrofitkotlin.view.fragment

import android.content.Context
import com.example.retrofitkotlin.model.Movie

interface MovieUI {
    fun observeData()
    fun getMovies(isConnected: Boolean)
    fun isNetworkAvailable(context: Context?): Boolean
    fun getPosterHome(list: List<Movie>)
    fun showComponent()
    fun hideComponent()
}