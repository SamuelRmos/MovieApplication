package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.DetailRepository
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class DetailRepositoryModule {

    @Singleton
    @Provides
    fun provideDetailRepository(): DetailRepository = DetailRepository()
}
