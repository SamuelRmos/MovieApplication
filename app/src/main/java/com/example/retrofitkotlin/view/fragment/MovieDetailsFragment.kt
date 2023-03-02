//package com.example.retrofitkotlin.view.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import androidx.navigation.findNavController
//import androidx.navigation.fragment.navArgs
//import com.example.retrofitkotlin.databinding.MovieDetailFragmentBinding
//import com.example.commons.extensions.hide
//import com.example.retrofitkotlin.model.Movie
//
//class MovieDetailsFragment : Fragment(), MovieDetailsUI {
//
//    private lateinit var binding: MovieDetailFragmentBinding
//    private val arg: MovieDetailsFragmentArgs by navArgs()
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        startPostponedEnterTransition()
//        binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
//
//        val movieActivity = activity as AppCompatActivity
//        binding.apply {
//            activity = movieActivity
//        }
//        setHasOptionsMenu(true)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        bindData(arg.movie)
//    }
//
//    override fun bindData(item: Movie) {
//        binding.clickListener = View.OnClickListener {
//            it.findNavController().navigate(MovieDetailsFragmentDirections.actionMovieFragment())
//        }
//
//        binding.apply {
//            movie = item
//        }
//        binding.progressBar.hide()
//    }
//}