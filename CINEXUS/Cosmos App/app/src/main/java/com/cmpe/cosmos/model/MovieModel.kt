package com.cmpe.cosmos.model

import com.cmpe.cosmos.data.remote.models.Showtimes

data class MovieModel(
    val movieId: Int,
    val title: String,
    val description: String,
    val releaseDate: String,
    val duration: Int,
    val genre: String,
    val rating: Double,
    val location: String,
    val posterUrl: String,
    val bannerUrl: String,
    val censorRating: String,
    val language: String,
    val castAndCrew: String,
    val director: String,
    val currentlyRunning: Int,
    val showtimes: ArrayList<Showtimes>
)
