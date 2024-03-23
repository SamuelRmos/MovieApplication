package com.example.movie.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.ui.RequestState
import com.example.movie.ui.RequestState.Loading
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private var _requestState: MutableState<RequestState> = mutableStateOf(Loading)
    val requestState = _requestState

    fun executeCall(call: suspend () -> Flow<RequestState>) {
        viewModelScope.launch(IO) {
            call().collect {
                _requestState.value = it
            }
        }
    }
}