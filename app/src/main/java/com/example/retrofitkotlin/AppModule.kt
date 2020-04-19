package com.example.retrofitkotlin

import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(private var movieApplication: MovieApplication) {
    @Provides
    fun provideMovieApplication(): MovieApplication = movieApplication
}