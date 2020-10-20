package com.example.retrofitkotlin.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.databinding.FragmentMoviesBinding
import com.example.retrofitkotlin.extensions.waitForTransition
import com.example.retrofitkotlin.view.adapter.MovieAdapter
import com.example.retrofitkotlin.view.viewmodel.MovieViewModel
import com.example.retrofitkotlin.view.viewmodel.ViewModelFactory
import javax.inject.Inject

class MovieFragment : Fragment() {

    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory())
            .get(MovieViewModel::class.java)
    }
    private lateinit var binding: FragmentMoviesBinding
    @Inject
    lateinit var mMovieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MovieApplication.apiComponent.inject(this)
        binding = FragmentMoviesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUi()
        waitForTransition(binding.recyclerView)
    }

    private fun subscribeUi() {
        binding.recyclerView.adapter = mMovieAdapter
        movieViewModel.fetchMovies()
        movieViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
            mMovieAdapter.updateMovieList(it)
        })
    }
}