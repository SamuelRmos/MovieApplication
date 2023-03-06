package com.example.movie

import android.app.Application
import com.example.commons.log.MovieLog.initLogging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogging()
    }
}