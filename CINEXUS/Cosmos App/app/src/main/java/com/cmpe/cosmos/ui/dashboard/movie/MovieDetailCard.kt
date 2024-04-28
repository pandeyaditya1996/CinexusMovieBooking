package com.cmpe.cosmos.ui.dashboard.movie

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.entities.Movies
import com.cmpe.cosmos.data.remote.models.TheaterShowtimeResponse
import com.cmpe.cosmos.util.convertTo12HourFormat

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetailCard(movieDetails: Movies, theater: TheaterShowtimeResponse, date: String) {
    val context = LocalContext.current
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = theater.name!!, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = theater.address!!)
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "${theater.distance} away")
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.clickable {
                        val geoUri =
                            Uri.parse("http://maps.google.com/maps?q=loc:${theater.coordinates!!} (${theater.name!!})")
                        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        context.startActivity(mapIntent)
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.direction),
                        contentDescription = ""
                    )
                    Text(text = "Directions")
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            val context = LocalContext.current
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 3
            ) {
                theater.showtimes.forEach { time ->
                    val time12 = convertTo12HourFormat(time.showtime.toString())
                    Text(modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(
                                context,
                                AboutMovieActivity::class.java
                            ).apply {
                                putExtra("movieId", movieDetails.movieId.toString())
                                putExtra("showtime", time12)
                                putExtra("date", date)
                                putExtra("theater", theater.name!!)
                                putExtra("theaterId", theater.theaterId.toString())
                                putExtra("discount", time.discount.toString())
                            })
                    }, text = time12, color = Color(0xFF2EBF0A))
                }
            }
        }
    }
}