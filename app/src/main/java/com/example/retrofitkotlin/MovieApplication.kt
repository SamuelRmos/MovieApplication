package com.example.retrofitkotlin

import android.app.Application
import android.content.Context
import com.example.retrofitkotlin.di.*
import com.example.retrofitkotlin.util.Constants

class MovieApplication : Application() {

    companion object {
        lateinit var ctx: Context
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
            .persistenceModule(PersistenceModule())
            .appModule(AppModule(this))
            .detailRepositoryModule(DetailRepositoryModule())
            .build()
        return apiComponent
    }
}