package com.cmpe.cosmos.ui.dashboard.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cmpe.cosmos.data.local.LocationStore
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.viewmodel.MovieVM
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


@Composable
fun MovieScreen(modifier: Modifier, viewModel: MovieVM) {
    var state by remember { mutableIntStateOf(0) }
    val titles = listOf("Now Showing", "Coming Soon")
    val context = LocalContext.current
    val store = LocationStore(context)
    val location = runBlocking { store.getLocation.first() }
    LaunchedEffect(key1 = Unit) {
        viewModel.getMovies(location)
    }
    val movieList by viewModel.movies.collectAsState()

    when (movieList) {
        is MovieVM.State.Success -> {

            val result = (movieList as MovieVM.State.Success).response

            Column(modifier = modifier) {
                PrimaryTabRow(selectedTabIndex = state, divider = {}) {
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

                val nowShowing = result.filter { movies -> movies.currentlyRunning == 1 }
                val upcoming = result.filter { movies -> movies.currentlyRunning == 0 }

                if (state == 0) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(nowShowing) { movie ->
                            MovieCard(movie, true)
                        }
                    }
                } else if (state == 1) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(upcoming) { movie ->
                            MovieCard(movie, false)
                        }
                    }
                }
            }
        }

        is MovieVM.State.Error -> {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = (movieList as MovieVM.State.Error).message)
            }
        }

        is MovieVM.State.Loading -> {
            LoadingComponent()
        }

    }


}
