package com.example.retrofitkotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.databinding.FragmentMoviesBinding
import com.example.retrofitkotlin.viewmodel.MovieViewModel
import com.example.retrofitkotlin.viewmodel.MovieViewModelFactory

class MovieFragment : Fragment() {
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: FragmentMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieViewModel = ViewModelProvider(this, MovieViewModelFactory())
            .get(MovieViewModel::class.java)
    }

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

        binding.apply { viewModel = movieViewModel }
        context ?: return binding.root

        setAdapter()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setAdapter() {
        val adapter = MovieAdapter()
        binding.recyclerView.run {
            setAdapter(adapter)
        }
        subscribeUi(adapter)
    }

    private fun subscribeUi(adapter: MovieAdapter) {
        movieViewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

}