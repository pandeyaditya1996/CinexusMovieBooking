package com.cmpe.cosmos.ui.dashboard.theater

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmpe.cosmos.R
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.util.getFormattedDate
import com.cmpe.cosmos.viewmodel.MovieVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TheaterDetailActivity : ComponentActivity() {
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
                    val theaterId = this.intent.getStringExtra("theaterId").toString()
                    val name = this.intent.getStringExtra("name").toString()
                    val address = this.intent.getStringExtra("address").toString()
                    val distance = this.intent.getStringExtra("distance").toString()
                    val coordinates = this.intent.getStringExtra("coordinates").toString()
                    LaunchedEffect(key1 = Unit) {
                        movieViewModel.getMoviesByTheaters(theaterId = theaterId.toInt())
                    }
                    val movieList by movieViewModel.movieShowtime.collectAsState()

                    when (movieList) {
                        is MovieVM.MovieShowtimeState.Success -> {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = name, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.size(16.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.pin),
                                        contentDescription = ""
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(text = address)
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 28.dp)
                                ) {
                                    Text(text = "$distance away")
                                    Spacer(modifier = Modifier.size(4.dp))
                                    Row(modifier = Modifier.clickable {
                                        val geoUri =
                                            Uri.parse("http://maps.google.com/maps?q=loc:${coordinates} ($name)")
                                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                                        mapIntent.setPackage("com.google.android.apps.maps")
                                        startActivity(mapIntent)
                                    }, verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.direction),
                                            contentDescription = ""
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(text = "Directions", fontSize = 12.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                                HorizontalDivider()
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
                                if (state == 0) {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(vertical = 8.dp)
                                    ) {
                                        items((movieList as MovieVM.MovieShowtimeState.Success).response) { movie ->
                                            TheaterDetailCard(
                                                movie,
                                                getFormattedDate(false),
                                                name,
                                                theaterId
                                            )
                                        }
                                    }
                                } else if (state == 1) {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        contentPadding = PaddingValues(vertical = 8.dp)
                                    ) {
                                        items((movieList as MovieVM.MovieShowtimeState.Success).response) { movie ->
                                            TheaterDetailCard(
                                                movie,
                                                getFormattedDate(true),
                                                name,
                                                theaterId
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        is MovieVM.MovieShowtimeState.Error -> {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = (movieList as MovieVM.MovieShowtimeState.Error).message)
                            }

                        }

                        is MovieVM.MovieShowtimeState.Loading -> {
                            LoadingComponent()
                        }
                    }
                }
            }
        }
    }
}

