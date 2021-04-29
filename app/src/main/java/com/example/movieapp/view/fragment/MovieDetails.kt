package com.example.movieapp.view.fragment

import com.example.movieapp.model.Movie

interface MovieDetailsUI {
    fun bindData(item: Movie)
}