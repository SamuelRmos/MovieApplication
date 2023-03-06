package com.example.movie.di

import com.example.movie.network.MovieApi
import com.example.movie.persistence.MovieDao
import com.example.movie.repository.DetailRepository
import com.example.movie.repository.DetailRepositoryImpl
import com.example.movie.repository.MovieRepository
import com.example.movie.repository.MovieRepositoryImpl
import com.example.commons.service.ConnectionService
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
    fun provideDetailRepository(movieDao: MovieDao): DetailRepository {
        return DetailRepositoryImpl(movieDao)
    }

    @Provides
    fun provideRetroRepository(
        movieApi: MovieApi,
        movieDao: MovieDao,
        connectionService: ConnectionService
    ): MovieRepository = MovieRepositoryImpl(movieApi, movieDao, connectionService)
}
