package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.retrofitkotlin.di.IoDispatcher
import com.example.retrofitkotlin.di.MainDispatcher
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.repository.MovieRepository
import com.example.retrofitkotlin.util.CategoryEnum
import com.example.retrofitkotlin.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

sealed class MovieViewAction {
    open class Success(val list: MutableList<TmdMovie>) : MovieViewAction()
    open class Loading(val loading: Boolean) : MovieViewAction()
    open class Error(val message: String) : MovieViewAction()
}

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, ioDispatcher) {

    private val _actionView by lazy { SingleLiveEvent<MovieViewAction>() }
    val actionView: LiveData<MovieViewAction>
        get() = _actionView

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