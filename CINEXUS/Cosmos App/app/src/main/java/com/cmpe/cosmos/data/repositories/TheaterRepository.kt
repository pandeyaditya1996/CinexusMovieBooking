package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.entities.Theaters
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeResponse
import com.cmpe.cosmos.data.util.SafeResult
import kotlinx.coroutines.flow.Flow

interface TheaterRepository {
    suspend fun getTheatersFromApi(location: String): SafeResult<List<Theaters>>

    suspend fun saveTheaters(theaters: List<Theaters>)

    suspend fun getTheaters(location: Int): Flow<List<Theaters>>

    suspend fun getTheatersShowtimeFromApi(
        movieId: String,
        locationId: String
    ): SafeResult<List<TheaterShowtimeResponse>>

}