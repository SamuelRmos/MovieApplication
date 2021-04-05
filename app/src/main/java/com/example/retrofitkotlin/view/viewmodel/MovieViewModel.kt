package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitkotlin.di.IoDispatcher
import com.example.retrofitkotlin.di.MainDispatcher
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.util.CategoryEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job = SupervisorJob()
    private val mUiScope = CoroutineScope(mainDispatcher + job)
    private val mIoScope = CoroutineScope(ioDispatcher + job)

    fun fetchMovies(
        id: CategoryEnum,
        isConnected: Boolean
    ): LiveData<MutableList<TmdMovie>> {

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
}