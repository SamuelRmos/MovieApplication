package com.example.retrofitkotlin

import android.app.Application
import android.content.Context
import com.example.retrofitkotlin.di.ApiComponent
import com.example.retrofitkotlin.di.ApiModule
import com.example.retrofitkotlin.di.DaggerApiComponent
import com.example.retrofitkotlin.util.Constants

class MovieApplication : Application() {

    companion object {
        var ctx: Context? = null
        lateinit var apiComponent: ApiComponent
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
        apiComponent = initDaggerComponent()
    }

    private fun initDaggerComponent(): ApiComponent {
        apiComponent = DaggerApiComponent
            .builder()
            .apiModule(ApiModule(Constants.baseURL))
            .build()
        return apiComponent
    }
}