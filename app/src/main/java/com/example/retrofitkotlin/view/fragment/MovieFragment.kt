package com.example.retrofitkotlin.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.databinding.FragmentMoviesBinding
import com.example.retrofitkotlin.view.adapter.MovieAdapter
import com.example.retrofitkotlin.view.viewmodel.MovieViewModel
import com.example.retrofitkotlin.view.viewmodel.ViewModelFactory

class MovieFragment : Fragment() {

    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory())
            .get(MovieViewModel::class.java)
    }

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var mMovieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMoviesBinding.inflate(
            inflater,
            container,
            false
        )
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {

        mMovieAdapter =
            MovieAdapter(arrayListOf())
        binding.recyclerView.adapter = mMovieAdapter
        movieViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
            mMovieAdapter.updateMovieList(it)
        })
    }
}