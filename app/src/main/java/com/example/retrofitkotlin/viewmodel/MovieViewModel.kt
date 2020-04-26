package com.example.retrofitkotlin.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.persistence.MovieDao
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.ui.MovieDetailsFragmentDirections
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val movieDao: MovieDao,
    private val movieApplication: MovieApplication
) : ViewModel() {

    private val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
    val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()
    private var movies = movieDao.getMovieList()
    private val isConnected = isNetworkAvailable(movieApplication)

    fun fetchMovies() {
        if ((movies.isEmpty() && isConnected) || isConnected) {
            CoroutineScope(coroutineContext).launch {
                popularMoviesLiveData.postValue(movieRepository.getPopularMovies())
                movieDao.insertMovieList(movieRepository.getPopularMovies())
            }
        } else {
            popularMoviesLiveData.apply { postValue(movies) }
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }

    fun createOnClickListener(): View.OnClickListener{
        return View.OnClickListener {
            it.findNavController().navigate(
                MovieDetailsFragmentDirections
                    .actionMovieFragment()
            )
        }
    }

    fun cancelAllResquests() = coroutineContext.cancel()
}