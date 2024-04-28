package com.cmpe.cosmos.ui.tickets

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.ui.dashboard.DashboardActivity
import com.cmpe.cosmos.ui.theme.CosmosTheme

class TicketsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val seats = this.intent.getStringExtra("seat")
                    val total = this.intent.getStringExtra("total")
                    val title = this.intent.getStringExtra("title")
                    val showtime = this.intent.getStringExtra("showtime")
                    val date = this.intent.getStringExtra("date")
                    val theater = this.intent.getStringExtra("theater")
                    val poster = this.intent.getStringExtra("poster")

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box {
                            Image(
                                modifier = Modifier.fillMaxWidth(),
                                painter = painterResource(id = R.drawable.ticket),
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )

                            Column(modifier = Modifier.padding(24.dp)) {
                                Row {
                                    AsyncImage(
                                        model = ApiEndpoints.BASE_TMDB_URL + ApiEndpoints.POSTER + poster,
                                        contentDescription = "",
                                        modifier = Modifier.height(150.dp),
                                        contentScale = ContentScale.Fit,
                                        placeholder = painterResource(id = R.drawable.placeholder)
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Column {
                                        Text(
                                            text = title.toString(),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(text = theater.toString())
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(text = date.toString())
                                        Spacer(modifier = Modifier.size(4.dp))
                                        Text(text = showtime.toString())
                                    }
                                }
                                Spacer(modifier = Modifier.size(80.dp))

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(
                                        Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = seats.toString(),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(12.dp))
                                    Text(text = "Total amount: \$$total", fontSize = 17.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.size(56.dp))
                        Button(
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@TicketsActivity,
                                        DashboardActivity::class.java
                                    )
                                )
                                finish()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 56.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Book another ticket")
                        }
                    }
                }
            }
        }
    }
}
