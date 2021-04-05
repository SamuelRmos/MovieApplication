package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.view.adapter.MovieAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMovieAdapter(): MovieAdapter = MovieAdapter(arrayListOf())
}