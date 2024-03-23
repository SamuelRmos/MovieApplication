package com.example.movie.di

import com.example.movie.network.MovieApi
import com.example.movie.repository.DetailRepository
import com.example.movie.repository.DetailRepositoryImpl
import com.example.movie.repository.MovieRepository
import com.example.movie.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideDetailRepository(movieApi: MovieApi): DetailRepository =
        DetailRepositoryImpl(movieApi)

    @Provides
    fun provideRetroRepository(movieApi: MovieApi): MovieRepository =
        MovieRepositoryImpl(movieApi)
}
