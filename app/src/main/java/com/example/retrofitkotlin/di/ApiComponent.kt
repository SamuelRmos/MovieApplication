package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.repository.DetailRepository
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.viewmodel.DetailViewModelFactory
import com.example.retrofitkotlin.viewmodel.MovieViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        ApiModule::class,
        PersistenceModule::class,
        DetailRepositoryModule::class]
)
interface ApiComponent {
    fun inject(retrofitRepository: MovieRepository)
    fun inject(detailRepository: DetailRepository)
    fun inject(movieViewModelFactory: MovieViewModelFactory)
    fun inject(detailViewModelFactory: DetailViewModelFactory)
}