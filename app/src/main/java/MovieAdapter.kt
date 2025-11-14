package com.example.moviesreview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private val movies: ArrayList<Movie>,
    private val onReviewClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView = view.findViewById(R.id.ivPoster)
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
        val btnReview: Button = view.findViewById(R.id.btnReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.tvTitle.text = movie.title
        holder.tvDesc.text = movie.description
        holder.ivPoster.setImageResource(movie.posterResId)

        holder.btnReview.setOnClickListener {
            onReviewClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size
}
