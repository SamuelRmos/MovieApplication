package com.example.retrofitkotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.data.TmdMovie
import com.example.retrofitkotlin.databinding.ItemLayoutBinding
import com.example.retrofitkotlin.ui.MovieDetailsFragment
import com.example.retrofitkotlin.ui.MovieFragmentDirections

class MovieAdapter : ListAdapter<TmdMovie, MovieAdapter.ViewHolder>(DiffCallback()) {

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        val movie = getItem(position)

        holder.apply {
            bind(createOnClickListener(position), movie)
            itemView.tag = movie
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private fun createOnClickListener(position: Int): View.OnClickListener {
        return View.OnClickListener {
           val direction = MovieFragmentDirections.actionDetailsFragment(position)
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