package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.repository.DetailRepositoryImpl
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
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
    fun inject(retrofitRepositoryImpl: MovieRepositoryImpl)
    fun inject(detailRepositoryImpl: DetailRepositoryImpl)
    fun inject(viewModelFactory: ViewModelFactory)
}