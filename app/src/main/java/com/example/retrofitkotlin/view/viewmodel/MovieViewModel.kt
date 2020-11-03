package com.example.retrofitkotlin.view.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepositoryImpl
import com.example.retrofitkotlin.util.CategoryEnum
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

    fun fetchMovies(id: CategoryEnum): MutableLiveData<MutableList<TmdMovie>> {
        val popularMoviesLiveData = MutableLiveData<MutableList<TmdMovie>>()
        if (popularMoviesLiveData.value == null) {
            mUiScope.launch {
                try {
                    val data = mIoScope.async {
                        return@async movieRepositoryImpl.getListMovies(isConnected, id)
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

    fun getListMovies(): MutableList<TmdMovie> {
        var data = mutableListOf<TmdMovie>()
        mUiScope.launch {
            try {
                data = mIoScope.async {
                    return@async movieRepositoryImpl.getListMovies(
                        isConnected,
                        CategoryEnum.TODAY
                    )!!
                }.await()
            } catch (e: Exception) {
            }
        }
        return data
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo!!.isConnectedOrConnecting
    }
}