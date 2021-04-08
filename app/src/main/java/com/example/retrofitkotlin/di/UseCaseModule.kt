package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.usecase.MovieUseCase
import com.example.retrofitkotlin.usecase.MovieUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesUseCase(movieRepository: MovieRepository): MovieUseCase =
        MovieUseCaseImpl(movieRepository)
}