package com.example.retrofitkotlin

import android.app.Application
import android.content.Context
import com.example.retrofitkotlin.di.*
import com.example.retrofitkotlin.util.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MovieApplication : Application()