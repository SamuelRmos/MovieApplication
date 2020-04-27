package com.example.retrofitkotlin.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitkotlin.R
import com.example.retrofitkotlin.databinding.FragmentMoviesBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.viewmodel.MovieViewModel
import com.example.retrofitkotlin.viewmodel.MovieViewModelFactory

class MovieFragment : Fragment() {
    private lateinit var viewModel: MovieViewModel
    private lateinit var binding: FragmentMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieViewModelFactory = MovieViewModelFactory()
        viewModel = ViewModelProvider(this, movieViewModelFactory)
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
        context ?: return binding.root

        setAdapter()
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setAdapter() {
        val adapter = MovieAdapter()
        binding.recyclerView.run {
            setVeilLayout(R.layout.item_layout)
            setAdapter(adapter)
            setLayoutManager(layoutManager = GridLayoutManager(this@MovieFragment.context, 2))
            addVeiledItems(20)
        }
        subscribeUi(binding, adapter)
    }
    
    private fun subscribeUi(binding: FragmentMoviesBinding, adapter: MovieAdapter) {
        viewModel.fetchMovies()
        viewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                Handler().postDelayed({
                    binding.recyclerView.unVeil()
                }, 3000)
                adapter.submitList(it)
            }
        })
    }

}