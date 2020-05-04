package com.example.retrofitkotlin.ui

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.databinding.DetailFragmentBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.viewmodel.DetailViewModelFactory
import com.example.retrofitkotlin.viewmodel.MovieDetailViewModel
import com.example.retrofitkotlin.viewmodel.MovieViewModel

class MovieDetailsFragment : Fragment() {
    private lateinit var viewModel: MovieDetailViewModel

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, DetailViewModelFactory())
            .get(MovieDetailViewModel::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            sharedElementEnterTransition =
                TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setHasOptionsMenu(true)
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

    private fun subscribeUi(binding: DetailFragmentBinding) {
        val movie = viewModel.getMovie(arguments.let {
            MovieDetailsFragmentArgs.fromBundle(it!!).idMovie
        })
        bind(binding, movie, viewModel.createOnClickListener())
        binding.progressBar.hide()
    }

    private fun bind(
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