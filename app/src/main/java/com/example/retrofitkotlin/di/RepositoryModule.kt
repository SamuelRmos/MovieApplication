package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.network.MovieApi
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.DetailRepository
import com.example.retrofitkotlin.repository.DetailRepositoryImpl
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
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
    fun provideDetailRepository(movieDao: MovieDao): DetailRepository =
        DetailRepositoryImpl(movieDao)

    @Provides
    fun provideRetroRepository(
        movieApi: MovieApi,
        movieDao: MovieDao,
        connectionService: ConnectionService
    ): MovieRepository = MovieRepositoryImpl(movieApi, movieDao, connectionService)
}
