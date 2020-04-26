package com.example.retrofitkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.MovieRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val movieDao: MovieDao
) : ViewModel() {

    private val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
    val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()
    var movies = movieDao.getMovieList()

    fun fetchMovies() {
        if (movies.isEmpty()) {
            CoroutineScope(coroutineContext).launch {
                popularMoviesLiveData.postValue(movieRepository.getPopularMovies())
                movieDao.insertMovieList(movieRepository.getPopularMovies())
            }
        } else {
            popularMoviesLiveData.apply { postValue(movies) }
        }
    }

    fun cancelAllResquests() = coroutineContext.cancel()
}