package com.example.retrofitkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieViewModel(private var movieRepository: MovieRepository) : ViewModel() {

    private val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
    val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()

    fun fetchMovies() {
        CoroutineScope(coroutineContext).launch {
            popularMoviesLiveData.postValue(
                movieRepository.getPopularMovies()
            )
        }
    }

    fun cancelAllResquests() = coroutineContext.cancel()
}