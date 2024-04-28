package com.cmpe.cosmos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cmpe.cosmos.data.entities.Movies
import com.cmpe.cosmos.data.entities.Theaters
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeResponse
import com.cmpe.cosmos.data.repositories.MovieRepository
import com.cmpe.cosmos.data.repositories.TheaterRepository
import com.cmpe.cosmos.data.util.SafeResult
import com.cmpe.cosmos.model.Locations
import com.cmpe.cosmos.model.MovieModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieVM @Inject constructor(
    private val movieRepository: MovieRepository,
    private val theaterRepository: TheaterRepository
) : ViewModel() {
    var movies = MutableStateFlow<State>(State.Loading)
        private set

    var theaters = MutableStateFlow<TheaterState>(TheaterState.Loading)
        private set
    var movieShowtime = MutableStateFlow<MovieShowtimeState>(MovieShowtimeState.Loading)
        private set

    var theatersShowtime = MutableStateFlow<TheaterShowtimeState>(TheaterShowtimeState.Loading)
        private set

    fun getMovies(
        location: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = movieRepository.getMoviesFromApi(location)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    movies.value =
                        State.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                movies.value = State.Error(result.message)
            }

            SafeResult.NetworkError -> {
                movies.value = State.Error("Network Error")
            }
        }
    }

    fun getTheaters(
        location: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = theaterRepository.getTheatersFromApi(location)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    theaters.value =
                        TheaterState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                theaters.value = TheaterState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                theaters.value = TheaterState.Error("Network Error")
            }
        }
    }

    fun getMoviesByTheaters(
        theaterId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = movieRepository.getMoviesByTheatersFromApi(theaterId)) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    movieShowtime.value =
                        MovieShowtimeState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                movieShowtime.value = MovieShowtimeState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                movieShowtime.value = MovieShowtimeState.Error("Network Error")
            }
        }
    }

    fun getMovieDetails(location: String, movieId: Int): StateFlow<MovieDetailUiState> {
        return movieRepository.getMovieDetails(location, movieId)
            .map { MovieDetailUiState.Success(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(3000),
                initialValue = MovieDetailUiState.Loading
            )
    }

    fun getTheatersShowtime(
        movieId: String,
        location: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        val locationEnum = Locations from location
        when (val result = theaterRepository.getTheatersShowtimeFromApi(
            movieId,
            locationEnum!!.locationID.toString()
        )) {
            is SafeResult.Success -> {
                result.data.let { safeResult ->
                    theatersShowtime.value =
                        TheaterShowtimeState.Success(safeResult)
                }
            }

            is SafeResult.Failure -> {
                theatersShowtime.value = TheaterShowtimeState.Error(result.message)
            }

            SafeResult.NetworkError -> {
                theatersShowtime.value = TheaterShowtimeState.Error("Network Error")
            }
        }
    }

    sealed class State {
        object Loading : State()
        class Success(val response: List<Movies>) : State()
        class Error(val message: String) : State()
    }

    sealed class TheaterState {
        object Loading : TheaterState()
        data class Success(val response: List<Theaters>) : TheaterState()
        data class Error(val message: String) : TheaterState()
    }

    sealed class MovieShowtimeState {
        object Loading : MovieShowtimeState()
        class Success(val response: List<MovieModel>) : MovieShowtimeState()
        class Error(val message: String) : MovieShowtimeState()
    }

    sealed class MovieDetailUiState {
        object Loading : MovieDetailUiState()
        data class Success(
            val movie: Movies
        ) : MovieDetailUiState()

        data class Error(val exception: String) : MovieDetailUiState()
    }

    sealed class TheaterShowtimeState {
        object Loading : TheaterShowtimeState()
        data class Success(val response: List<TheaterShowtimeResponse>) : TheaterShowtimeState()
        data class Error(val message: String) : TheaterShowtimeState()
    }

}