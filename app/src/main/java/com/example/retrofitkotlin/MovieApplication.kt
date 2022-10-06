package com.example.retrofitkotlin

import android.app.Application
import com.example.retrofitkotlin.log.MovieLog.initLogging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogging()
    }
}