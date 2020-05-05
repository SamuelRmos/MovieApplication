package com.example.retrofitkotlin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitkotlin.databinding.DetailFragmentBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.viewmodel.DetailViewModelFactory
import com.example.retrofitkotlin.viewmodel.MovieDetailViewModel

class MovieDetailsFragment : Fragment() {
    private lateinit var viewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, DetailViewModelFactory())
            .get(MovieDetailViewModel::class.java)
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
        val movieActivity = activity as AppCompatActivity
        binding.apply {
            activity = movieActivity
        }
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