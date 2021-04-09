package com.example.retrofitkotlin.view.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

open class BaseViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val job = SupervisorJob()
    val mUiScope = CoroutineScope(mainDispatcher + job)
    val mIoScope = CoroutineScope(ioDispatcher + job)
}