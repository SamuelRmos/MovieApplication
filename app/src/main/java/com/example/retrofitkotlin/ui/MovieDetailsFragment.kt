package com.example.retrofitkotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.databinding.DetailFragmentBinding
import com.example.retrofitkotlin.util.Constants
import com.example.retrofitkotlin.util.ImageBinding
import com.example.retrofitkotlin.util.hide
import com.example.retrofitkotlin.viewmodel.MovieViewModel

class MovieDetailsFragment : Fragment() {
    private lateinit var viewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        val binding = DetailFragmentBinding.inflate(inflater, container, false)
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
                }!!))
                binding.progressBar.hide()
            }
        })
    }

    fun bind(binding: DetailFragmentBinding, item: TmdMovie) {
        binding.apply {
            tmdbDetail = item
            executePendingBindings()
        }
    }
}