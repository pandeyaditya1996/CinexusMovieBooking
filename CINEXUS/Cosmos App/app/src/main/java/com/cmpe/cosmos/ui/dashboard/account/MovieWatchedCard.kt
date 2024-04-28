package com.cmpe.cosmos.ui.dashboard.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.data.remote.models.MovieResponse

@Composable
fun MovieWatchedCard(movie: MovieResponse) {
    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.Start) {
            AsyncImage(
                model = ApiEndpoints.BASE_TMDB_URL + ApiEndpoints.POSTER + movie.posterUrl,
                contentDescription = null,
                modifier = Modifier
                    .height(130.dp)
                    .clip(RoundedCornerShape(4.dp)),
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.FillHeight
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = movie.title!!,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.SemiBold
                )
                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = "Release Date: "
                    )
                    Text(
                        text = movie.releaseDate!!
                    )
                }

                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = "Genre: "
                    )
                    Text(
                        text = movie.genre!!
                    )
                }

                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text(
                        text = "Rating: "
                    )
                    Text(text = (movie.rating!! * 10).toInt().toString() + "%")
                }
            }
        }
    }
}