package com.example.retrofitkotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.databinding.DetailFragmentBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.viewmodel.MovieViewModel
import com.example.retrofitkotlin.viewmodel.MovieViewModelFactory

class MovieDetailsFragment : Fragment() {
    private lateinit var viewModel: MovieViewModel

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

        val binding = DetailFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        context ?: return binding.root
        subscribeUi(binding)
        return binding.root
    }

    fun subscribeUi(binding: DetailFragmentBinding) {
        viewModel.fetchMovies()
        viewModel.popularMoviesLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                bind(binding, it.get(arguments?.let { it1 ->
                    MovieDetailsFragmentArgs.fromBundle(it1).position
                }!!), viewModel.createOnClickListener())
                binding.progressBar.hide()
            }
        })
    }

    fun bind(
        binding: DetailFragmentBinding,
        item: TmdMovie,
        listener: View.OnClickListener
    ) {
        binding.apply {
            tmdbDetail = item
            clickListener = listener
            executePendingBindings()
        }
    }

}