package com.example.retrofitkotlin.di

import android.content.Context
import androidx.room.Room
import com.example.retrofitkotlin.R
import com.example.retrofitkotlin.persistence.AppDataBase
import com.example.retrofitkotlin.persistence.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDataBase =
        Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            context.getString(R.string.movie_db)
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideMovieDao(appDataBase: AppDataBase): MovieDao = appDataBase.movieDao()
}