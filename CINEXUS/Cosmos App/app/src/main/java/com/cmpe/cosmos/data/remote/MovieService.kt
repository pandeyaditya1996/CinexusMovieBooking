package com.cmpe.cosmos.data.remote

import com.cmpe.cosmos.data.constant.ApiEndpoints.ENDPOINT_MOVIES
import com.cmpe.cosmos.data.constant.ApiEndpoints.ENDPOINT_MOVIES_BY_THEATERS
import com.cmpe.cosmos.data.constant.ApiEndpoints.ENDPOINT_THEATERS
import com.cmpe.cosmos.data.constant.ApiEndpoints.ENDPOINT_THEATERS_SHOWTIME_BY_MOVIES
import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.remote.models.TheaterResponse
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeRequest
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET(ENDPOINT_MOVIES)
    suspend fun getMovies(@Query("location_name") location: String): List<MovieResponse>

    @GET(ENDPOINT_THEATERS)
    suspend fun getTheaters(@Query("location_name") location: String): List<TheaterResponse>

    @GET("$ENDPOINT_MOVIES_BY_THEATERS{THEATER_ID}/")
    suspend fun getMoviesByTheaters(@Path("THEATER_ID") theaterId: Int): List<MovieResponse>

    @POST(ENDPOINT_THEATERS_SHOWTIME_BY_MOVIES)
    suspend fun getTheatersShowtimeByMovie(@Body theaterShowtimeRequest: TheaterShowtimeRequest): List<TheaterShowtimeResponse>
}