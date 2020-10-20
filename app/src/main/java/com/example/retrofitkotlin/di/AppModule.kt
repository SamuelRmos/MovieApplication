package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.view.adapter.MovieAdapter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule constructor(private val movieApplication: MovieApplication) {

    @Singleton
    @Provides
    fun provideMovieApplication(): MovieApplication = movieApplication

    @Provides
    fun provideMovieAdapter(): MovieAdapter = MovieAdapter(arrayListOf())
}