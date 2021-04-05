
package com.example.retrofitkotlin.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.retrofitkotlin.MovieApplication
import com.example.retrofitkotlin.binding.ImageBinding.setBackImage
import com.example.retrofitkotlin.databinding.FragmentMoviesBinding
import com.example.retrofitkotlin.extensions.hide
import com.example.retrofitkotlin.util.CategoryEnum
import com.example.retrofitkotlin.view.adapter.MovieAdapter
import com.example.retrofitkotlin.view.viewmodel.MovieViewModel
import com.example.retrofitkotlin.view.viewmodel.ViewModelFactory
import javax.inject.Inject

class MovieFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val movieViewModel: MovieViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory())
            .get(MovieViewModel::class.java)
    }

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
    ): View? {
        MovieApplication.apiComponent.inject(this)
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        binding.refreshLayout.apply {
            setOnRefreshListener(this@MovieFragment)
            setDistanceToTriggerSync(500)
        }
        subscribeUi()
        return binding.root
    }

    private fun getPosterHome() {
        val list = movieViewModel.getListMovies()
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

    private fun subscribeUi() {
        with(binding) {
            rvMostPopular.adapter = mPopularAdapter
            getListMovie(CategoryEnum.POPULAR, mPopularAdapter)
            rvRated.adapter = mRatedAdapter
            getListMovie(CategoryEnum.RATED, mRatedAdapter)
            rvToday.adapter = mTodayAdapter
            getListMovie(CategoryEnum.TODAY, mTodayAdapter)
            rvClassic.adapter = mClassicAdapter
            getListMovie(CategoryEnum.CLASSIC, mClassicAdapter)
        }
    }

    private fun getListMovie(categoryId: CategoryEnum, adapter: MovieAdapter) {
        movieViewModel.fetchMovies(categoryId).observe(viewLifecycleOwner, Observer {
            adapter.updateMovieList(it)
            getPosterHome()
            binding.progressbar.hide()
        })
    }

    override fun onRefresh() {
        subscribeUi()
        binding.refreshLayout.isRefreshing = false
    }

}