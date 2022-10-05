package com.example.retrofitkotlin.service

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import timber.log.Timber

class ConnectionService(private val context: Context) {

    @Suppress("DEPRECATION")
    fun isNetworkAvailable() = try {
        val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        cm.activeNetworkInfo!!.isConnectedOrConnecting
    } catch (exception: Exception) {
        Timber.e("Error to get connection", exception)
        false
    }
}