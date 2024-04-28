package com.cmpe.cosmos.data.sources

import com.cmpe.cosmos.data.remote.MovieService
import com.cmpe.cosmos.data.remote.models.TheaterResponse
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeRequest
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeResponse
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.data.util.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class TheaterDataSourceImpl @Inject constructor(
    private val service: MovieService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TheaterDataSource {
    override suspend fun getTheaters(location: String): SafeResult<List<TheaterResponse>> {
        return safeApiCall(dispatcher) {
            service.getTheaters(location)
        }
    }

    override suspend fun postTheaterShowtime(
        movieId: String,
        locationId: String
    ): SafeResult<List<TheaterShowtimeResponse>> {
        return safeApiCall(dispatcher) {
            service.getTheatersShowtimeByMovie(TheaterShowtimeRequest(movieId, locationId))
        }
    }
}