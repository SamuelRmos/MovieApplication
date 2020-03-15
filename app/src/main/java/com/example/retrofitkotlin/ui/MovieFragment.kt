package com.example.retrofitkotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitkotlin.MovieAdapter
import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.databinding.FragmentMoviesBinding
import com.example.retrofitkotlin.util.hide
import com.example.retrofitkotlin.viewmodel.MovieViewModel

class MovieFragment : Fragment() {

    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        val binding = FragmentMoviesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = MovieAdapter()
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context,
            LinearLayoutManager.VERTICAL))
        binding.recyclerView.adapter = adapter

        subscribeUi(binding, adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentMoviesBinding, adapter: MovieAdapter) {
        viewModel.fetchMovies()
        viewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
           it?.let {
               adapter.submitList(it)
               binding.progressBar.hide()
           }
        })
    }
}