package com.example.retrofitkotlin.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.retrofitkotlin.databinding.DetailFragmentBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.view.viewmodel.MovieDetailViewModel
import com.example.retrofitkotlin.view.viewmodel.ViewModelFactory

class MovieDetailsFragment : Fragment() {

    private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory())
            .get(MovieDetailViewModel::class.java)
    }

    private lateinit var binding: DetailFragmentBinding
    private val arg: MovieDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DetailFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        val movieActivity = activity as AppCompatActivity
        binding.apply {
            activity = movieActivity
        }

        setHasOptionsMenu(true)
        subscribeUi()
        return binding.root
    }

    private fun subscribeUi() {

        val movie = viewModel.getMovie(arg.idMovie)
        bind(binding, movie)
        binding.progressBar.hide()
    }

    private fun bind(binding: DetailFragmentBinding, item: TmdMovie) {

        binding.clickListener = View.OnClickListener {
            it.findNavController().navigate(MovieDetailsFragmentDirections.actionMovieFragment())
        }

        binding.apply {
            tmdbDetail = item
            executePendingBindings()
        }
    }

}