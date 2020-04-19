package com.example.retrofitkotlin.di

import com.example.retrofitkotlin.AppModule
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.ui.MovieFragment
import com.example.retrofitkotlin.viewmodel.MovieViewModel
import com.example.retrofitkotlin.viewmodel.MovieViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ApiModule::class])
interface ApiComponent {
    fun inject(retrofitRepository: MovieRepository)
    fun inject(movieViewModel: MovieViewModel)
    fun inject(movieFragment: MovieFragment)
    fun inject(movieViewModelFactory: MovieViewModelFactory)
}