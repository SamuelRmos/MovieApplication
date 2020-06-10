package com.example.retrofitkotlin.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.retrofitkotlin.repository.DetailRepository
import com.example.retrofitkotlin.view.MovieDetailsFragmentDirections

class MovieDetailViewModel constructor(private val repository: DetailRepository) : ViewModel() {

    fun getMovie(id: Int) = repository.getMovieById(id)

    fun createOnClickListener(): View.OnClickListener {
        return View.OnClickListener {
            it.findNavController().navigate(
                MovieDetailsFragmentDirections
                    .actionMovieFragment()
            )
        }
    }
}