package com.cmpe.cosmos.ui.dashboard.theater

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmpe.cosmos.model.MovieModel
import com.cmpe.cosmos.ui.dashboard.movie.AboutMovieActivity
import com.cmpe.cosmos.util.convertTo12HourFormat

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TheaterDetailCard(movie: MovieModel, date: String, theaterName: String, theaterId: String) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = movie.title, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = movie.language + " - 2D")
            Spacer(modifier = Modifier.size(16.dp))
            val context = LocalContext.current
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 3
            ) {
                movie.showtimes.forEach { time ->
                    val time12 = convertTo12HourFormat(time.showtime.toString())
                    Text(modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(
                                context,
                                AboutMovieActivity::class.java
                            ).apply {
                                putExtra("movieId", movie.movieId.toString())
                                putExtra("showtime", time12)
                                putExtra("date", date)
                                putExtra("theater", theaterName)
                                putExtra("theaterId", theaterId)
                                putExtra("discount", time.discount.toString())
                            })
                    }, text = time12, color = Color(0xFF2EBF0A))
                }
            }
        }
    }
}