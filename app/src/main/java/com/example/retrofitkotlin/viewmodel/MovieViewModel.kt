package com.example.retrofitkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.util.Constants
import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.service.RetrofitFactory
import com.example.retrofitkotlin.service.TmdbApi
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MovieViewModel() : ViewModel() {

    private val parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)
    val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()

    private val tmdbApi: TmdbApi = RetrofitFactory
        .retrofit(Constants.baseURL)
        .create(TmdbApi::class.java)

    private val repository = MovieRepository(tmdbApi)

    fun fetchMovies() {
        scope.launch {
            val popularMovies = repository.getPopularMovies()
            popularMoviesLiveData.postValue(popularMovies)
        }
    }

    fun cancelAllResquests() = coroutineContext.cancel()
}