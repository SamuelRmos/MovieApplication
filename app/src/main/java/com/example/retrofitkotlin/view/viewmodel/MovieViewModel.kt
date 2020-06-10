package com.example.retrofitkotlin.view.viewmodel

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
    private val movieRepository: MovieRepository, private val movieApplication: MovieApplication
) : ViewModel() {

    val popularMoviesLiveData = fetchMovies()

    private fun fetchMovies(): MutableLiveData<MutableList<TmdMovie>> {

        val movieList = MutableLiveData<MutableList<TmdMovie>>()
        val isConnected = isNetworkAvailable(movieApplication)

        runBlocking {
            movieList.postValue(movieRepository.getPopularMovies(isConnected))
        }

        return movieList
    }

    private fun isNetworkAvailable(context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
}