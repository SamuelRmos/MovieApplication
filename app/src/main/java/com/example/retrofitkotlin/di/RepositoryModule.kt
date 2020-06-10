package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.DetailRepository
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.network.TmdbApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideDetailRepository(movieDao: MovieDao): DetailRepository = DetailRepository(movieDao)

    @Singleton
    @Provides
    fun provideRetroRepository(movieApi: TmdbApi, movieDao: MovieDao): MovieRepository =
        MovieRepository(movieApi, movieDao)
}
