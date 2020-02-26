package com.example.retrofitkotlin.viewmodel

import android.provider.SyncStateContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.Constants
import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.service.RetrofitFactory
import com.example.retrofitkotlin.service.TmdbApi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieViewModel : ViewModel() {

    private val parentJob = Job()
    private val tmdbApi : TmdbApi = RetrofitFactory
                                    .retrofit(Constants.baseURL)
                                    .create(TmdbApi::class.java)

    private val coroutineContext: CoroutineContext
    get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)
    private val respository : MovieRepository = MovieRepository(tmdbApi)

    val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()

    fun fetchMovies(){
        scope.launch {
            val popularMovies = respository.getPopularMovies()
            popularMoviesLiveData.postValue(popularMovies)
        }
    }
    
    fun cancelAllResquests() = coroutineContext.cancel()
}