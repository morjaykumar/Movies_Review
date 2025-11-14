package com.example.moviesreview

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class ReviewActivity : AppCompatActivity() {

    private lateinit var tvMovieTitle: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var avgRatingBar: RatingBar
    private lateinit var reviewEditText: EditText
    private lateinit var addReviewBtn: Button
    private lateinit var btnHome: Button
    private lateinit var reviewListView: ListView

    private val reviews = mutableListOf<String>()
    private var lastClickTime = 0L
    private var lastClickedItem = -1
    private var totalRating = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        tvMovieTitle = findViewById(R.id.reviewMovieTitle)
        ratingBar = findViewById(R.id.ratingBar)
        avgRatingBar = findViewById(R.id.avgRatingBar)
        reviewEditText = findViewById(R.id.reviewEditText)
        addReviewBtn = findViewById(R.id.addReviewBtn)
        btnHome = findViewById(R.id.btnHome)
        reviewListView = findViewById(R.id.reviewListView)

        // Get movie title from MainActivity
        val movieTitle = intent.getStringExtra("MOVIE_TITLE") ?: "Movie Review"
        tvMovieTitle.text = movieTitle

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reviews)
        reviewListView.adapter = adapter

        // Load saved reviews
        loadReviews(movieTitle, adapter)

        addReviewBtn.setOnClickListener {
            val reviewText = reviewEditText.text.toString().trim()
            val ratingValue = ratingBar.rating

            if (reviewText.isNotEmpty() && ratingValue > 0) {
                val newReview = "⭐ $ratingValue - $reviewText"
                reviews.add(newReview)
                adapter.notifyDataSetChanged()
                reviewEditText.text.clear()

                totalRating += ratingValue
                avgRatingBar.rating = totalRating / reviews.size

                saveReviews(movieTitle)
            } else {
                Toast.makeText(this, "Please enter a review and rating", Toast.LENGTH_SHORT).show()
            }
        }

        reviewListView.setOnItemClickListener { _, _, position, _ ->
            val currentTime = System.currentTimeMillis()
            if (position == lastClickedItem && currentTime - lastClickTime < 500) {
                // Double tap → delete
                reviews.removeAt(position)
                adapter.notifyDataSetChanged()
                saveReviews(movieTitle)
                Toast.makeText(this, "Review deleted", Toast.LENGTH_SHORT).show()
            }
            lastClickedItem = position
            lastClickTime = currentTime
        }

        btnHome.setOnClickListener {
            finish() // Go back to home
        }
    }

    private fun saveReviews(movieTitle: String) {
        val sharedPref = getSharedPreferences("MovieReviews", Context.MODE_PRIVATE)
        val jsonArray = JSONArray(reviews)
        sharedPref.edit().putString(movieTitle, jsonArray.toString()).apply()
    }

    private fun loadReviews(movieTitle: String, adapter: ArrayAdapter<String>) {
        val sharedPref = getSharedPreferences("MovieReviews", Context.MODE_PRIVATE)
        val savedData = sharedPref.getString(movieTitle, null)
        if (savedData != null) {
            val jsonArray = JSONArray(savedData)
            for (i in 0 until jsonArray.length()) {
                reviews.add(jsonArray.getString(i))
                val rating = jsonArray.getString(i).substringAfter("⭐ ").substringBefore(" -").toFloatOrNull() ?: 0f
                totalRating += rating
            }
            if (reviews.isNotEmpty()) {
                avgRatingBar.rating = totalRating / reviews.size
            }
            adapter.notifyDataSetChanged()
        }
    }
}
