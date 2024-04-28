package com.cmpe.cosmos.data.sources

import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.util.SafeResult

interface MovieDataSource {
    suspend fun getMovies(
        location: String
    ): SafeResult<List<MovieResponse>>

    suspend fun getMoviesByTheaters(theaterId: Int): SafeResult<List<MovieResponse>>

}