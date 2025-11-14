package com.example.moviesreview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieList: ArrayList<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerMovies = findViewById(R.id.recyclerMovies)

        // Movie list without rating
        movieList = arrayListOf(
            Movie("Avengers: Endgame", "Superhero action film", R.drawable.avengers),
            Movie("Inception", "Mind-bending thriller", R.drawable.inception),
            Movie("Titanic", "Romantic tragedy film", R.drawable.titanic),
            Movie("Avatar", "Sci-fi adventure on Pandora", R.drawable.avatar)
        )

        // Adapter setup
        movieAdapter = MovieAdapter(movieList) { movie ->
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("MOVIE_TITLE", movie.title)
            startActivity(intent)
        }

        recyclerMovies.layoutManager = LinearLayoutManager(this)
        recyclerMovies.adapter = movieAdapter
    }
}
