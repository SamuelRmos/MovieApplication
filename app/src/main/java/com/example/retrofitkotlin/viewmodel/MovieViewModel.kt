package com.example.retrofitkotlin.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.MovieRepository
import kotlinx.coroutines.runBlocking

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val movieDao: MovieDao,
    private val movieApplication: MovieApplication
) : ViewModel() {

    val popularMoviesLiveData = fetchMovies()

    private fun fetchMovies(): MutableLiveData<MutableList<TmdMovie>> {
        val movieList = MutableLiveData<MutableList<TmdMovie>>()
        val movies = movieDao.getMovieList()
        val isConnected = isNetworkAvailable(movieApplication)

        runBlocking {
            if ((movies.isEmpty() && isConnected) || isConnected) {
                movieList.postValue(movieRepository.getPopularMovies())
                movieDao.insertMovieList(movieRepository.getPopularMovies())
            } else movieList.postValue(movieDao.getMovieList())
        }
        return movieList
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}