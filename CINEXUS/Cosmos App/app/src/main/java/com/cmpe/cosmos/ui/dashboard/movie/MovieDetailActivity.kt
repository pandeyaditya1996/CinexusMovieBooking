package com.cmpe.cosmos.ui.dashboard.movie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.data.local.LocationStore
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.util.getFormattedDate
import com.cmpe.cosmos.viewmodel.MovieVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MovieDetailActivity : ComponentActivity() {
    private val movieViewModel: MovieVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val store = LocationStore(this)
                    val location = runBlocking { store.getLocation.first() }
                    val movieId = this.intent.getStringExtra("movieId").toString()
                    val movieDetails by movieViewModel.getMovieDetails(location, movieId.toInt())
                        .collectAsState()
                    LaunchedEffect(key1 = Unit) {
                        movieViewModel.getTheatersShowtime(movieId, location)
                    }
                    val theaterList by movieViewModel.theatersShowtime.collectAsState()

                    when (movieDetails) {
                        is MovieVM.MovieDetailUiState.Success -> {
                            val details =
                                (movieDetails as MovieVM.MovieDetailUiState.Success).movie
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 16.dp, start = 24.dp, end = 24.dp)
                            ) {
                                Row {
                                    AsyncImage(
                                        model = ApiEndpoints.BASE_TMDB_URL + ApiEndpoints.POSTER + details.posterUrl,
                                        contentDescription = "",
                                        modifier = Modifier.height(150.dp),
                                        contentScale = ContentScale.Fit,
                                        placeholder = painterResource(id = R.drawable.placeholder)
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Column {
                                        Text(text = details.title, fontWeight = FontWeight.SemiBold)
                                        Text(text = details.genre)
                                    }
                                }
                                HorizontalDivider(modifier = Modifier.padding(top = 16.dp))

                                var state by remember { mutableIntStateOf(0) }
                                val titles = listOf("Today", "Tomorrow")

                                PrimaryTabRow(
                                    selectedTabIndex = state,
                                    divider = {},
                                    indicator = {}) {
                                    titles.forEachIndexed { index, title ->
                                        Tab(
                                            selected = state == index,
                                            onClick = { state = index },
                                            text = {
                                                Text(
                                                    text = title,
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                    color = if (state == index) MaterialTheme.colorScheme.primary else Color(
                                                        0xFF747474
                                                    )
                                                )
                                            }
                                        )
                                    }
                                }
                                when (theaterList) {
                                    is MovieVM.TheaterShowtimeState.Success -> {
                                        if (state == 0) {
                                            LazyColumn(
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                contentPadding = PaddingValues(vertical = 8.dp)
                                            ) {
                                                items((theaterList as MovieVM.TheaterShowtimeState.Success).response) { theater ->
                                                    if (theater.showtimes.isNotEmpty())
                                                        MovieDetailCard(
                                                            details,
                                                            theater,
                                                            getFormattedDate(false)
                                                        )
                                                }
                                            }
                                        } else if (state == 1) {
                                            LazyColumn(
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                contentPadding = PaddingValues(vertical = 8.dp)
                                            ) {
                                                items((theaterList as MovieVM.TheaterShowtimeState.Success).response) { theater ->
                                                    if (theater.showtimes.isNotEmpty())
                                                        MovieDetailCard(
                                                            details,
                                                            theater,
                                                            getFormattedDate(true)
                                                        )
                                                }
                                            }
                                        }
                                    }

                                    is MovieVM.TheaterShowtimeState.Error -> {
                                        Log.d("Test", "here")
                                    }

                                    is MovieVM.TheaterShowtimeState.Loading -> {
                                        LoadingComponent()
                                    }

                                }
                            }
                        }

                        is MovieVM.MovieDetailUiState.Error -> {

                        }

                        is MovieVM.MovieDetailUiState.Loading -> {
                            LoadingComponent()
                        }
                    }
                }
            }
        }
    }
}