package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.entities.Movies
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMoviesFromApi(location: String): SafeResult<List<Movies>>

    suspend fun saveMovies(movies: List<Movies>)

    suspend fun getMovies(location: String): Flow<List<Movies>>

    fun getMovieDetails(location: String, movieId: Int): Flow<Movies>

    suspend fun getMoviesByTheatersFromApi(theaterId: Int): SafeResult<List<MovieModel>>

}