package com.example.commons.log

import com.example.commons.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

object MovieLog {
    fun initLogging() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}