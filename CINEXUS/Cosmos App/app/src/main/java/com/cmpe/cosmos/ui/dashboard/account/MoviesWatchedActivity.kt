package com.cmpe.cosmos.ui.dashboard.account

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesWatchedActivity : ComponentActivity() {

    private val viewModel: UserVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {

                val userId = this.intent.getIntExtra("userId", 0)

                LaunchedEffect(key1 = Unit) {
                    viewModel.getMoviesWatched(userId)
                }
                val movieList by viewModel.movies.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Movies watched in past 30 days:",
                            fontWeight = FontWeight.SemiBold
                        )
                        when (movieList) {
                            is UserVM.MoviesWatchedState.Success -> {

                                val result =
                                    (movieList as UserVM.MoviesWatchedState.Success).response

                                LazyColumn(
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    items(result) { movie ->
                                        MovieWatchedCard(movie)
                                    }
                                }
                            }

                            is UserVM.MoviesWatchedState.Error -> {
                                Column(
                                    Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = (movieList as UserVM.MoviesWatchedState.Error).message)
                                }
                            }

                            is UserVM.MoviesWatchedState.Loading -> {
                                LoadingComponent()
                            }
                        }
                    }
                }
            }
        }
    }
}