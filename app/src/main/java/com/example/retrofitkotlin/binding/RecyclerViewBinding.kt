package com.example.retrofitkotlin.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.ui.MovieAdapter

@BindingAdapter("adapterMovieList")
fun bindAdapterMovieList(view: RecyclerView, movies: List<TmdMovie>?) {
    movies?.let {
        (view.adapter as? MovieAdapter)?.updateMovieList(it)
    }
}
