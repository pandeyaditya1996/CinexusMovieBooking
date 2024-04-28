package com.cmpe.cosmos.data.remote.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(

    @SerializedName("movie_id") var movieId: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("release_date") var releaseDate: String? = null,
    @SerializedName("duration") var duration: Int? = null,
    @SerializedName("genre") var genre: String? = null,
    @SerializedName("rating") var rating: Double? = null,
    @SerializedName("poster_url") var posterUrl: String? = null,
    @SerializedName("banner_url") var bannerUrl: String? = null,
    @SerializedName("censor_rating") var censorRating: String? = null,
    @SerializedName("language") var language: String? = null,
    @SerializedName("cast_and_crew") var castAndCrew: String? = null,
    @SerializedName("director") var director: String? = null,
    @SerializedName("currently_running") var currentlyRunning: Int? = null,
    @SerializedName("showtimes") var showtimes: ArrayList<Showtimes> = arrayListOf()

)