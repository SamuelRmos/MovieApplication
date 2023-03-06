package com.example.movie.di

import com.example.movie.repository.MovieRepository
import com.example.movie.usecase.MovieUseCase
import com.example.movie.usecase.MovieUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesUseCase(movieRepository: MovieRepository): MovieUseCase {
        return MovieUseCaseImpl(movieRepository)
    }
}