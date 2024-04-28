package com.cmpe.cosmos.ui.dashboard.movie

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.data.entities.Movies

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(movie: Movies, nowShowing: Boolean) {
    val context = LocalContext.current
    ElevatedCard(shape = RoundedCornerShape(4.dp), onClick = {
        val navigate = Intent(context, MovieDetailActivity::class.java).apply {
            putExtra("movieId", movie.movieId.toString())
        }
        context.startActivity(navigate)
    }, enabled = nowShowing) {
        Box(contentAlignment = Alignment.BottomStart) {
            AsyncImage(
                model = ApiEndpoints.BASE_TMDB_URL + ApiEndpoints.POSTER + movie.posterUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                Brush.verticalGradient(
                                    0.5f to Color.Black.copy(alpha = 0F),
                                    1F to Color.Black
                                )
                            )
                        }
                    },
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Crop
            )
            Column {
                Text(
                    text = movie.title,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.White
                )
                Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.rating),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = (movie.rating * 10).toInt().toString() + "%", color = Color.White)
                }
            }
        }
    }
}