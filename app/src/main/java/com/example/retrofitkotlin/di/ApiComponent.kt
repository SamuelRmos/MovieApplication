package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.repository.DetailRepository
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.view.viewmodel.ViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        ApiModule::class,
        PersistenceModule::class,
        RepositoryModule::class]
)
interface ApiComponent {
    fun inject(retrofitRepository: MovieRepository)
    fun inject(detailRepository: DetailRepository)
    fun inject(viewModelFactory: ViewModelFactory)
}