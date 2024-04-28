package com.cmpe.cosmos.data.sources

import com.cmpe.cosmos.data.remote.models.TheaterResponse
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeResponse
import com.cmpe.cosmos.data.util.SafeResult

interface TheaterDataSource {
    suspend fun getTheaters(
        location: String
    ): SafeResult<List<TheaterResponse>>

    suspend fun postTheaterShowtime(
        movieId: String,
        locationId: String
    ): SafeResult<List<TheaterShowtimeResponse>>
}