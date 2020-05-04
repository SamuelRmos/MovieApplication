package com.example.retrofitkotlin.base

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers

abstract class LiveCoroutinesViewModel : ViewModel() {

    inline fun <T> launchOnViewModelScope(crossinline block: () -> LiveData<T>): LiveData<T> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(block())
        }
    }
}