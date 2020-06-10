package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.MovieApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule constructor(private val movieApplication: MovieApplication) {

    @Singleton
    @Provides
    fun provideMovieApplication(): MovieApplication = movieApplication
}