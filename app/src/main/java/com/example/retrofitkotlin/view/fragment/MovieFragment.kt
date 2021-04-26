
package com.example.retrofitkotlin.view.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.retrofitkotlin.binding.ImageBinding.setBackImage
import com.example.retrofitkotlin.databinding.FragmentMoviesBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.extensions.show
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.view.adapter.MovieAdapter
import com.example.retrofitkotlin.view.viewmodel.MovieViewAction
import com.example.retrofitkotlin.view.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MovieFragment"

@AndroidEntryPoint
class MovieFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener, MovieUI {

    private val movieViewModel by viewModels<MovieViewModel>()

    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var mPopularAdapter: MovieAdapter

    @Inject
    lateinit var mRatedAdapter: MovieAdapter

    @Inject
    lateinit var mTodayAdapter: MovieAdapter

    @Inject
    lateinit var mClassicAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.refreshLayout.apply {
            setOnRefreshListener(this@MovieFragment)
        }

        observeData()
        return binding.root
    }

    override fun getMovies(isConnected: Boolean) {
        with(movieViewModel) {
            fetchRatedMovies(isConnected)
            fetchPopularMovies(isConnected)
            fetchTodayMovies(isConnected)
            fetchClassicMovies(isConnected)
        }
    }

    override fun getPosterHome(list: List<Movie>) {
        for (position in 0..10) {
            val imageView = ImageView(context)
            val layout = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            imageView.layoutParams = layout
            imageView.scaleType = ImageView.ScaleType.FIT_XY

            binding.vpPosterMostPopular.apply {
                setInAnimation(context, android.R.anim.slide_in_left)
                setOutAnimation(context, android.R.anim.slide_out_right)
            }

            imageView.setBackImage(list[position].poster_path)
            binding.vpPosterMostPopular.addView(imageView)

        }
    }

    @Suppress("DEPRECATION")
    override fun isNetworkAvailable(context: Context?): Boolean {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo!!.isConnectedOrConnecting
    }

    override fun onRefresh() {
        observeData()
        binding.refreshLayout.isRefreshing = false
    }

    override fun observeData() {
        getMovies(isNetworkAvailable(context))

        movieViewModel.actionView.observe(viewLifecycleOwner, { state ->
            when (state) {
                is MovieViewAction.SuccessPopularMovie -> {
                    binding.rvMostPopular.adapter = mPopularAdapter
                    mPopularAdapter.updateMovieList(state.list.results)
                }

                is MovieViewAction.SuccessRatedMovie -> {
                    binding.rvRated.adapter = mRatedAdapter
                    mRatedAdapter.updateMovieList(state.list.results)
                }

                is MovieViewAction.SuccessTodayMovie -> {
                    binding.rvToday.adapter = mTodayAdapter
                    mTodayAdapter.updateMovieList(state.list.results)
                    getPosterHome(state.list.results)
                }

                is MovieViewAction.SuccessClassicMovie -> {
                    binding.rvClassic.adapter = mClassicAdapter
                    mClassicAdapter.updateMovieList(state.list.results)
                }

                is MovieViewAction.Loading -> movieViewModel.stateLoading(state.loading)
                is MovieViewAction.ShowComponent -> showComponent()
                is MovieViewAction.HideComponent -> hideComponent()
                is MovieViewAction.Error -> {
                    Log.e(TAG, "Error get list movies: ${state.message}")
                }
            }
        })
    }

    override fun showComponent() {
        with(binding) {
            progressbar.hide()
            tvMostPopular.show()
            tvToday.show()
            tvRated.show()
            tvClassic.show()
        }
    }

    override fun hideComponent() {
        with(binding) {
            progressbar.show()
            tvMostPopular.hide()
            tvToday.hide()
            tvRated.hide()
            tvClassic.hide()
        }
    }
}