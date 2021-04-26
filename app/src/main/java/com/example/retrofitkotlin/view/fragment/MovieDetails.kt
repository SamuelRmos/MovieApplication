package com.example.retrofitkotlin.view.fragment

import com.example.retrofitkotlin.model.Movie

interface MovieDetailsUI {
    fun bindData(item: Movie)
}