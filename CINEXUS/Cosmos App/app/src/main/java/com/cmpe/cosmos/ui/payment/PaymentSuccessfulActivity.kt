package com.cmpe.cosmos.ui.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cmpe.cosmos.R
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.ui.tickets.TicketsActivity

class PaymentSuccessfulActivity : ComponentActivity() {
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
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ComposeLottieAnimation(
                            modifier = Modifier,
                            seats.toString(),
                            total.toString(),
                            title.toString(),
                            showtime.toString(),
                            date.toString(),
                            theater.toString(),
                            poster.toString()
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Hurray!", fontSize = 24.sp)
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(text = "Your tickets are booked", fontSize = 24.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComposeLottieAnimation(
    modifier: Modifier,
    seats: String,
    total: String,
    title: String,
    showtime: String,
    date: String,
    theater: String,
    poster: String
) {

    val context = LocalContext.current

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.payment))

    val progress by animateLottieCompositionAsState(composition)

    val activity = (LocalContext.current as? Activity)

    LottieAnimation(
        modifier = modifier.size(150.dp),
        composition = composition,
        speed = 0.7f
    )

    if (progress == 1.0f) {
        context.startActivity(Intent(context, TicketsActivity::class.java).apply {
            putExtra("seat", seats)
            putExtra("total", total)
            putExtra("title", title)
            putExtra("showtime", showtime)
            putExtra("date", date)
            putExtra("theater", theater)
            putExtra("poster", poster)
        })
        activity?.finish()
    }
}