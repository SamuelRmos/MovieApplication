package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.LiveData
import com.example.retrofitkotlin.di.IoDispatcher
import com.example.retrofitkotlin.di.MainDispatcher
import com.example.retrofitkotlin.model.MovieResponse
import com.example.retrofitkotlin.usecase.MovieUseCase
import com.example.commons.util.SingleLiveEvent
import com.example.retrofitkotlin.view.viewmodel.MovieViewAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MovieViewAction {
    open class SuccessPopularMovie(val list: MovieResponse) : MovieViewAction()
    open class SuccessRatedMovie(val list: MovieResponse) : MovieViewAction()
    open class SuccessTodayMovie(val list: MovieResponse) : MovieViewAction()
    open class SuccessClassicMovie(val list: MovieResponse) : MovieViewAction()
    open class Loading(val loading: Boolean) : MovieViewAction()
    open class ShowComponent : MovieViewAction()
    open class HideComponent : MovieViewAction()
    open class Error(val message: String) : MovieViewAction()
}

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieUseCase: MovieUseCase,
    @MainDispatcher mainDispatcher: CoroutineDispatcher,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseViewModel(mainDispatcher, ioDispatcher) {

    private val _actionView by lazy { SingleLiveEvent<MovieViewAction>() }
    val actionView: LiveData<MovieViewAction>
        get() = _actionView

    fun getMoviesData() {
        fetchRatedMovies()
        fetchPopularMovies()
        fetchTodayMovies()
        fetchClassicMovies()
    }

    private fun fetchPopularMovies() {
        _actionView.value = Loading(true)
        mUiScope.launch { executePopularMovie() }
    }

    private fun fetchRatedMovies() {
        _actionView.value = Loading(true)
        mUiScope.launch { executeRatedMovie() }
    }

    private fun fetchTodayMovies() {
        _actionView.value = Loading(true)
        mUiScope.launch {
            executeTodayMovie()
        }
    }

    private fun fetchClassicMovies() {
        _actionView.value = Loading(true)
        mUiScope.launch {
            executeClassicMovie()
        }
    }

    fun stateLoading(loading: Boolean) {
        if (loading) _actionView.value = HideComponent() else _actionView.value = ShowComponent()
    }

    private suspend fun executePopularMovie() {
        mIoScope.async {
            return@async movieUseCase.executePopularMovies()
        }.await().fold(::showError, ::showSuccessPopularMovie)
    }

    private suspend fun executeRatedMovie() {
        mIoScope.async {
            return@async movieUseCase.executeRatedMovies()
        }.await().fold(::showError, ::showSuccessRatedMovie)
    }

    private suspend fun executeTodayMovie() {
        mIoScope.async {
            return@async movieUseCase.executeTodayMovies()
        }.await().fold(::showError, ::showSuccessTodayMovie)
    }

    private suspend fun executeClassicMovie() {
        mIoScope.async {
            return@async movieUseCase.executeClassicMovies()
        }.await().fold(::showError, ::showSuccessClassicMovie)
    }

    private fun showError(failure: String) {
        _actionView.value = Loading(false)
        _actionView.value = Error(failure)
    }

    private fun showSuccessPopularMovie(value: MovieResponse) {
        _actionView.value = Loading(false)
        _actionView.value = SuccessPopularMovie(value)
    }

    private fun showSuccessTodayMovie(value: MovieResponse) {
        _actionView.value = Loading(false)
        _actionView.value = SuccessTodayMovie(value)
    }

    private fun showSuccessClassicMovie(value: MovieResponse) {
        _actionView.value = Loading(false)
        _actionView.value = SuccessClassicMovie(value)
    }

    private fun showSuccessRatedMovie(value: MovieResponse) {
        _actionView.value = Loading(false)
        _actionView.value = SuccessRatedMovie(value)
    }

}