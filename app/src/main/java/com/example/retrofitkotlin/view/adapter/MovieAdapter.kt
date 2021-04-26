package com.example.retrofitkotlin.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitkotlin.databinding.ItemLayoutBinding
import com.example.retrofitkotlin.model.Movie
import com.example.retrofitkotlin.view.fragment.MovieFragmentDirections

class MovieAdapter(list: MutableList<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var items = list
    private lateinit var binding: ItemLayoutBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    fun updateMovieList(movies: List<Movie>) {
        items.clear()
        items.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        holder.apply {
            bind(::createOnClickListener, movie)
            itemView.tag = position
        }
    }

    override fun getItemCount(): Int = items.size

    private fun createOnClickListener(movie: Movie) =
        View.OnClickListener {
            it.findNavController().navigate(
                MovieFragmentDirections.actionDetailsFragment(movie),
            )
        }

    class ViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: (Movie) -> View.OnClickListener, item: Movie) {
            binding.apply {
                clickListener = listener.invoke(item)
                movie = item
                executePendingBindings()
            }
        }
    }
}