package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.view.fragment.MovieFragment
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
    fun inject(viewModelFactory: ViewModelFactory)
    fun inject(movieFragment: MovieFragment)
}