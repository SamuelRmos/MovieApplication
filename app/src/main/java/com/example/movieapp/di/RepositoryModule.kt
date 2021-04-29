package com.example.movieapp.di

import com.example.movieapp.network.MovieApi
import com.example.movieapp.persistence.MovieDao
import com.example.movieapp.repository.DetailRepository
import com.example.movieapp.repository.DetailRepositoryImpl
import com.example.movieapp.repository.MovieRepository
import com.example.movieapp.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDetailRepository(movieDao: MovieDao): DetailRepository =
        DetailRepositoryImpl(movieDao)

    @Provides
    fun provideRetroRepository(movieApi: MovieApi, movieDao: MovieDao): MovieRepository =
        MovieRepositoryImpl(movieApi, movieDao)
}
