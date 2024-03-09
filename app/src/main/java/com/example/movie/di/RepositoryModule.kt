package com.example.movie.di

import com.example.movie.network.MovieApi
import com.example.movie.repository.MovieRepository
import com.example.movie.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

//    @Singleton
//    @Provides
//    fun provideDetailRepository(movieDao: MovieDao): DetailRepository {
//        return DetailRepositoryImpl(movieDao)
//    }

    @Provides
    fun provideRetroRepository(movieApi: MovieApi): MovieRepository =
        MovieRepositoryImpl(movieApi)
}
