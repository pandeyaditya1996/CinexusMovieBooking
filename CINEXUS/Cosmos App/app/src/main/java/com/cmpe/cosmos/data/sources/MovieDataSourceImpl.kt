package com.cmpe.cosmos.data.sources

import com.cmpe.cosmos.data.remote.MovieService
import com.cmpe.cosmos.data.remote.models.MovieResponse
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.data.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class MovieDataSourceImpl @Inject constructor(
    private val movieService: MovieService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieDataSource {
    override suspend fun getMovies(location: String): SafeResult<List<MovieResponse>> {
        return safeApiCall(dispatcher) {
            movieService.getMovies(location)
        }
    }

    override suspend fun getMoviesByTheaters(theaterId: Int): SafeResult<List<MovieResponse>> {
        return safeApiCall(dispatcher) {
            movieService.getMoviesByTheaters(theaterId)
        }
    }
}