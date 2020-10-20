package com.example.retrofitkotlin.di

import androidx.room.Room
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.R
import com.example.retrofitkotlin.persistence.AppDataBase
import com.example.retrofitkotlin.persistence.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(application: MovieApplication): AppDataBase = Room.databaseBuilder(
        application,
        AppDataBase::class.java,
        application.getString(R.string.movie_db))
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideMovieDao(appDataBase: AppDataBase): MovieDao = appDataBase.movieDao()
}