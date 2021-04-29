package com.example.movieapp.di

import com.example.movieapp.view.adapter.MovieAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMovieAdapter(): MovieAdapter = MovieAdapter(arrayListOf())
}