package com.cmpe.cosmos.data.repositories

import com.cmpe.cosmos.data.entities.Theaters
import com.cmpe.cosmos.data.local.dao.TheatersDao
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeResponse
import com.cmpe.cosmos.data.sources.TheaterDataSource
import com.cmpe.cosmos.data.util.SafeResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TheaterRepositoryImpl @Inject constructor(
    private val remoteDataSource: TheaterDataSource,
    private val theatersDao: TheatersDao
) : TheaterRepository {
    override suspend fun getTheatersFromApi(location: String): SafeResult<List<Theaters>> {
        return when (val result = remoteDataSource.getTheaters(location)) {
            is SafeResult.Success -> {
                saveTheaters(result.data.map {
                    Theaters(
                        theaterId = it.theaterId!!,
                        locationId = it.locationId!!,
                        name = it.name!!,
                        seatingCapacity = it.seatingCapacity!!,
                        distance = it.distance!!,
                        coordinates = it.coordinates!!,
                        address = it.address!!
                    )
                }
                )
                SafeResult.Success(result.data.map {
                    Theaters(
                        theaterId = it.theaterId!!,
                        locationId = it.locationId!!,
                        name = it.name!!,
                        seatingCapacity = it.seatingCapacity!!,
                        distance = it.distance!!,
                        coordinates = it.coordinates!!,
                        address = it.address!!
                    )
                })
            }


            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }

    override suspend fun saveTheaters(theaters: List<Theaters>) {
        theatersDao.insertAllTheaters(theaters)
    }

    override suspend fun getTheaters(location: Int): Flow<List<Theaters>> {
        return theatersDao.getTheaters(location)
    }

    override suspend fun getTheatersShowtimeFromApi(
        movieId: String,
        locationId: String
    ): SafeResult<List<TheaterShowtimeResponse>> {
        return when (val result = remoteDataSource.postTheaterShowtime(movieId, locationId)) {

            is SafeResult.Success -> SafeResult.Success(
                result.data
            )

            is SafeResult.Failure -> SafeResult.Failure(result.exception)
            SafeResult.NetworkError -> SafeResult.NetworkError
        }
    }
}