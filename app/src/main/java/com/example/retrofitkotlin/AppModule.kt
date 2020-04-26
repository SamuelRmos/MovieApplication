package com.example.retrofitkotlin

import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(private val movieApplication: MovieApplication) {
    @Provides
    fun provideMovieApplication(): MovieApplication = movieApplication
}