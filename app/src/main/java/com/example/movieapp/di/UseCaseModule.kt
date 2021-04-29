package com.example.movieapp.di

import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.usecase.MovieUseCase
import com.example.movieapp.usecase.MovieUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesUseCase(movieRepository: MovieRepository): MovieUseCase =
        MovieUseCaseImpl(movieRepository)
}