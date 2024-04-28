package com.cmpe.cosmos.ui.dashboard.account

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
import com.cmpe.cosmos.ui.dashboard.DashboardActivity
import com.cmpe.cosmos.ui.theme.CosmosTheme

class TicketsCancelledActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current

                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.payment))

                    val progress by animateLottieCompositionAsState(composition)

                    val activity = (LocalContext.current as? Activity)


                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        LottieAnimation(
                            modifier = Modifier.size(150.dp),
                            composition = composition,
                            speed = 0.7f
                        )

                        if (progress == 1.0f) {
                            context.startActivity(Intent(context, DashboardActivity::class.java))
                            activity?.finish()
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(modifier = Modifier.size(24.dp))
                            Text(text = "Tickets cancelled successfully", fontSize = 24.sp)
                        }

                    }

                }
            }
        }
    }
}
