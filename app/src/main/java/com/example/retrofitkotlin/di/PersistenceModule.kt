package com.example.retrofitkotlin.di

import android.content.Context
import androidx.room.Room
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.R
import com.example.retrofitkotlin.persistence.AppDataBase
import com.example.retrofitkotlin.persistence.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideRoomDatabase(): AppDataBase = Room.databaseBuilder(
        context,
        AppDataBase::class.java,
        context.getString(R.string.movie_db))
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideMovieDao(appDataBase: AppDataBase): MovieDao = appDataBase.movieDao()
}