package com.cmpe.cosmos.ui.dashboard.theater

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cmpe.cosmos.data.local.LocationStore
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.viewmodel.MovieVM
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun TheaterScreen(modifier: Modifier, viewModel: MovieVM) {

    val context = LocalContext.current
    val store = LocationStore(context)
    val location = runBlocking { store.getLocation.first() }
    LaunchedEffect(key1 = Unit) {
        viewModel.getTheaters(location)
    }
    val theaterList by viewModel.theaters.collectAsState()

    when (theaterList) {
        is MovieVM.TheaterState.Success -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items((theaterList as MovieVM.TheaterState.Success).response) { theater ->
                    TheaterCard(theater)
                }
            }
        }

        is MovieVM.TheaterState.Error -> {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = (theaterList as MovieVM.TheaterState.Error).message)
            }
        }

        is MovieVM.TheaterState.Loading -> {
            LoadingComponent()
        }
    }
}