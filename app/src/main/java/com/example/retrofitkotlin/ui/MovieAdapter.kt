package com.example.retrofitkotlin.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.model.TmdMovie
import com.example.retrofitkotlin.databinding.ItemLayoutBinding

class MovieAdapter : ListAdapter<TmdMovie, MovieAdapter.ViewHolder>(
    DiffCallback()
) {
    private val items = mutableListOf<TmdMovie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun updateMovieList(movies: List<TmdMovie>) {
        items.clear()
        items.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        holder.apply {
            bind(createOnClickListener(movie.id), movie)
            itemView.tag = position
        }
    }

    private fun createOnClickListener(idMovie: Int): View.OnClickListener {
        return View.OnClickListener {
            val direction = MovieFragmentDirections.actionDetailsFragment(idMovie)
            it.findNavController().navigate(direction)
        }
    }

    class ViewHolder(
        private val binding: ItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: TmdMovie) {
            binding.apply {
                clickListener = listener
                tmdbMovie = item
                executePendingBindings()
            }
        }
    }
}

private class DiffCallback : DiffUtil.ItemCallback<TmdMovie>() {
    override fun areItemsTheSame(oldItem: TmdMovie, newItem: TmdMovie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TmdMovie, newItem: TmdMovie): Boolean {
        return oldItem == newItem
    }
}