package com.example.retrofitkotlin.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.databinding.ItemLayoutBinding
import com.example.retrofitkotlin.extensions.toTransitionGroup
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.view.fragment.MovieFragmentDirections

class MovieAdapter(list: MutableList<TmdMovie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var items = list
    private lateinit var binding: ItemLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    fun updateMovieList(movies: List<TmdMovie>) {
        items.clear()
        items.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        holder.apply {
            bind(createOnClickListener(movie), movie)
            itemView.tag = position
        }
    }

    override fun getItemCount(): Int = items.size

    private fun createOnClickListener(movie: TmdMovie): View.OnClickListener {
        return View.OnClickListener {
            it.findNavController().navigate(
                MovieFragmentDirections.actionDetailsFragment(movie),
                FragmentNavigatorExtras(binding.ivPoster.toTransitionGroup())
            )
        }
    }

    class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: TmdMovie) {
            binding.apply {
                clickListener = listener
                movie = item
                executePendingBindings()
            }
        }
    }
}