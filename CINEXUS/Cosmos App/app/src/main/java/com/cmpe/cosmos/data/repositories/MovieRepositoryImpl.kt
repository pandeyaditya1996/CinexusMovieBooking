package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.entities.Movies
import com.cmpe.cosmos.data.local.dao.MoviesDao
import com.cmpe.cosmos.data.sources.MovieDataSource
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.model.MovieModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: MovieDataSource,
    private val moviesDao: MoviesDao
) : MovieRepository {
    override suspend fun getMoviesFromApi(location: String): SafeResult<List<Movies>> {
        return when (val result = remoteDataSource.getMovies(location)) {
            is SafeResult.Success -> {
                saveMovies(result.data.map {
                    Movies(
                        movieId = it.movieId!!,
                        title = it.title!!,
                        description = it.description!!,
                        releaseDate = it.releaseDate!!,
                        duration = it.duration!!,
                        genre = it.genre!!,
                        rating = it.rating!!,
                        location = location,
                        posterUrl = it.posterUrl!!,
                        bannerUrl = it.bannerUrl!!,
                        censorRating = it.censorRating!!,
                        language = it.language!!,
                        castAndCrew = it.castAndCrew!!,
                        director = it.director!!,
                        currentlyRunning = it.currentlyRunning!!
                    )
                }
                )
                SafeResult.Success(result.data.map {
                    Movies(
                        movieId = it.movieId!!,
                        title = it.title!!,
                        description = it.description!!,
                        releaseDate = it.releaseDate!!,
                        duration = it.duration!!,
                        genre = it.genre!!,
                        rating = it.rating!!,
                        location = location,
                        posterUrl = it.posterUrl!!,
                        bannerUrl = it.bannerUrl!!,
                        censorRating = it.censorRating!!,
                        language = it.language!!,
                        castAndCrew = it.castAndCrew!!,
                        director = it.director!!,
                        currentlyRunning = it.currentlyRunning!!
                    )
                })
            }


            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun getMovies(location: String): Flow<List<Movies>> {
        return moviesDao.getMovies(location)
    }

    override fun getMovieDetails(location: String, movieId: Int): Flow<Movies> {
        return moviesDao.getMovieDetails(location, movieId)
    }

    override suspend fun getMoviesByTheatersFromApi(theaterId: Int): SafeResult<List<MovieModel>> {
        return when (val result = remoteDataSource.getMoviesByTheaters(theaterId)) {
            is SafeResult.Success -> {
                SafeResult.Success(result.data.map {
                    MovieModel(
                        movieId = it.movieId!!,
                        title = it.title!!,
                        description = it.description!!,
                        releaseDate = it.releaseDate!!,
                        duration = it.duration!!,
                        genre = it.genre!!,
                        rating = it.rating!!,
                        location = "",
                        posterUrl = it.posterUrl!!,
                        bannerUrl = it.bannerUrl!!,
                        censorRating = it.censorRating!!,
                        language = it.language!!,
                        castAndCrew = it.castAndCrew!!,
                        director = it.director!!,
                        currentlyRunning = it.currentlyRunning!!,
                        showtimes = it.showtimes
                    )
                })
            }

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun saveMovies(movies: List<Movies>) {
        moviesDao.insertAllMovies(movies)
    }
}