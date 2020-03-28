package com.example.retrofitkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val seleted = MutableLiveData<Int>()

    fun select(item: Int) {
        seleted.value = item
    }
}