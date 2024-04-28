package com.cmpe.cosmos.ui.dashboard.movie

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.data.local.LocationStore
import com.cmpe.cosmos.ui.booking.BookingActivity
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.viewmodel.MovieVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class AboutMovieActivity : ComponentActivity() {
    private val movieViewModel: MovieVM by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val store = LocationStore(this)
                    val location = runBlocking { store.getLocation.first() }
                    val movieId = this.intent.getStringExtra("movieId").toString()
                    val showtime = this.intent.getStringExtra("showtime")
                    val date = this.intent.getStringExtra("date")
                    val theater = this.intent.getStringExtra("theater")
                    val theaterId = this.intent.getStringExtra("theaterId")
                    val discount = this.intent.getStringExtra("discount")

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                                navigationIcon = {
                                    Icon(
                                        modifier = Modifier.clickable {
                                            finish()
                                        },
                                        painter = painterResource(id = R.drawable.back),
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                })
                        }
                    ) {
                        val movieDetails by movieViewModel.getMovieDetails(
                            location,
                            movieId.toInt()
                        )
                            .collectAsState()
                        when (movieDetails) {
                            is MovieVM.MovieDetailUiState.Success -> {
                                val details =
                                    (movieDetails as MovieVM.MovieDetailUiState.Success).movie

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Box(contentAlignment = Alignment.BottomCenter) {
                                        AsyncImage(
                                            modifier = Modifier.fillMaxWidth(),
                                            model = ApiEndpoints.BASE_TMDB_URL + ApiEndpoints.BANNER + details.bannerUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Fit,
                                            placeholder = painterResource(id = R.drawable.placeholder)
                                        )
                                        ElevatedCard(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 32.dp)
                                                .offset(y = (36).dp),
                                            colors = CardDefaults.elevatedCardColors(
                                                containerColor = Color(
                                                    0xFFFFFFFF
                                                )
                                            ), shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = details.title,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                Text(
                                                    text = details.genre,
                                                    color = Color(0xFF747474)
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 48.dp, start = 24.dp, end = 24.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Column {
                                            Text(
                                                text = "Censor Rating",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = if (details.censorRating == "true") "R" else "PG",
                                                fontSize = 12.sp,
                                                color = Color(0xFF747474)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = "Release Date",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = details.releaseDate,
                                                fontSize = 12.sp,
                                                color = Color(0xFF747474)
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = "Duration",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = details.duration.toString() + " min",
                                                fontSize = 12.sp,
                                                color = Color(0xFF747474)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                                        Text(
                                            text = "AVAILABLE IN",
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = details.language,
                                            fontSize = 14.sp,
                                            color = Color(0xFF747474)
                                        )
                                        Text(
                                            "2D • IMAX",
                                            fontSize = 14.sp,
                                            color = Color(0xFF747474)
                                        )
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Text(text = "CAST & CREW", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = details.castAndCrew,
                                            fontSize = 14.sp,
                                            color = Color(0xFF747474)
                                        )
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Text(text = "DIRECTOR", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = details.director,
                                            fontSize = 14.sp,
                                            color = Color(0xFF747474)
                                        )
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Text(text = "SYNOPSIS", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = details.description,
                                            fontSize = 14.sp, color = Color(0xFF747474),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 8
                                        )
                                        Spacer(modifier = Modifier.size(24.dp))
                                        val poster =
                                            ApiEndpoints.BASE_TMDB_URL + ApiEndpoints.POSTER + details.posterUrl
                                        Button(
                                            onClick = {
                                                startActivity(
                                                    Intent(
                                                        this@AboutMovieActivity,
                                                        BookingActivity::class.java
                                                    ).apply {
                                                        putExtra("title", details.title)
                                                        putExtra("showtime", showtime)
                                                        putExtra("date", date)
                                                        putExtra("theater", theater)
                                                        putExtra("theaterId", theaterId)
                                                        putExtra("poster", poster)
                                                        putExtra("movieId", movieId)
                                                        putExtra("discount", discount)
                                                    }
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 56.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(text = "Book Tickets")
                                        }
                                        Spacer(modifier = Modifier.size(16.dp))
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
}