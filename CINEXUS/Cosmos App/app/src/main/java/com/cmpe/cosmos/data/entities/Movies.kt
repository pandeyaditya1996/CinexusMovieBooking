package com.cmpe.cosmos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movies(

    @PrimaryKey
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
    val currentlyRunning: Int
)
