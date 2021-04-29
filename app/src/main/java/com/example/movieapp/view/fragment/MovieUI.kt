package com.example.movieapp.view.fragment

import android.content.Context
import com.example.movieapp.model.Movie

interface MovieUI {
    fun observeData()
    fun getMovies(isConnected: Boolean)
    fun isNetworkAvailable(context: Context?): Boolean
    fun getPosterHome(list: List<Movie>)
    fun showComponent()
    fun hideComponent()
}