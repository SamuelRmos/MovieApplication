package com.example.retrofitkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.retrofitkotlin.databinding.DetailFragmentBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.viewmodel.DetailViewModelFactory
import com.example.retrofitkotlin.viewmodel.MovieDetailViewModel

class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProvider(this, DetailViewModelFactory())
            .get(MovieDetailViewModel::class.java)
    }

    private val arg: MovieDetailsFragmentArgs by navArgs()

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

        setHasOptionsMenu(true)
        subscribeUi(binding)
        return binding.root
    }

    private fun subscribeUi(binding: DetailFragmentBinding) {

        val movie = viewModel.getMovie(arg.idMovie)
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