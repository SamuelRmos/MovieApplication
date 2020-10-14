package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.DetailRepositoryImpl
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import com.example.retrofitkotlin.network.TmdbApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideDetailRepository(movieDao: MovieDao): DetailRepositoryImpl = DetailRepositoryImpl(movieDao)

    @Singleton
    @Provides
    fun provideRetroRepository(movieApi: TmdbApi, movieDao: MovieDao): MovieRepositoryImpl =
        MovieRepositoryImpl(movieApi, movieDao)
}
