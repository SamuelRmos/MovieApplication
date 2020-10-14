package com.example.retrofitkotlin.view.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.retrofitkotlin.R
import com.example.retrofitkotlin.databinding.DetailFragmentBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.model.TmdMovie

class MovieDetailsFragment : Fragment() {

    private lateinit var binding: DetailFragmentBinding
    private val arg: MovieDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.image_shared_element_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startPostponedEnterTransition()
        binding = DetailFragmentBinding.inflate(inflater, container, false)

        val movieActivity = activity as AppCompatActivity
        binding.apply {
            activity = movieActivity
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(arg.movie)
    }

    private fun bind(item: TmdMovie) {
        binding.clickListener = View.OnClickListener {
            it.findNavController().navigate(MovieDetailsFragmentDirections.actionMovieFragment())
        }

        binding.apply {
            movie = item
            executePendingBindings()
        }
        binding.progressBar.hide()
    }
}