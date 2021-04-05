package com.example.retrofitkotlin.view.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.util.CategoryEnum
import kotlinx.coroutines.*

class MovieViewModel(
    private val movieRepository: MovieRepository,
    movieApplication: MovieApplication,
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job = SupervisorJob()
    private val isConnected = isNetworkAvailable(movieApplication)
    private val mUiScope = CoroutineScope(mainDispatcher + job)
    private val mIoScope = CoroutineScope(ioDispatcher + job)
    val listMovies = MutableLiveData<MutableList<TmdMovie>>()

    fun fetchMovies(id: CategoryEnum): MutableLiveData<MutableList<TmdMovie>> {
        val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()
        if (popularMoviesLiveData.value == null) {
            mUiScope.launch {
                try {
                    val data = mIoScope.async {
                        return@async movieRepository.getListMovies(isConnected, id)
                    }.await()

                    try {
                        popularMoviesLiveData.value = data
                    } catch (e: Exception) {
                    }
                } catch (e: Exception) {
                }
            }
        }
        return popularMoviesLiveData
    }

    fun getListMovies() = movieRepository.getMoviePoster()

    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
}