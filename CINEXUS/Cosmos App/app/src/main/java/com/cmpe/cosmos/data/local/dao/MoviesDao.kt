package com.cmpe.cosmos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cmpe.cosmos.data.entities.Movies
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies WHERE location = :location")
    fun getMovies(location: String): Flow<List<Movies>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<Movies>)

    @Query("SELECT * FROM movies WHERE location = :location AND movieId = :movieId")
    fun getMovieDetails(location: String, movieId: Int): Flow<Movies>
}