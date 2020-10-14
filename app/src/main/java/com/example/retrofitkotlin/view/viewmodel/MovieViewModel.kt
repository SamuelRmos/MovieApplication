package com.example.retrofitkotlin.view.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import kotlinx.coroutines.*

@Suppress("DEPRECATION")
class MovieViewModel(
    private val movieRepositoryImpl: MovieRepositoryImpl,
    movieApplication: MovieApplication,
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job = SupervisorJob()
    private val isConnected = isNetworkAvailable(movieApplication)

    private val mUiScope = CoroutineScope(mainDispatcher + job)
    private val mIoScope = CoroutineScope(ioDispatcher + job)

    val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()

    fun fetchMovies() {

        if (popularMoviesLiveData.value == null) {
            mUiScope.launch {
                try {
                    val data = mIoScope.async {
                        return@async movieRepositoryImpl.getPopularMovies(isConnected)
                    }.await()

                    try {
                        popularMoviesLiveData.value = data
                    } catch (e: Exception) {
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
}