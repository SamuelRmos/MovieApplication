package com.example.retrofitkotlin.log

import com.example.retrofitkotlin.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

object MovieLog {
    fun initLogging() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}