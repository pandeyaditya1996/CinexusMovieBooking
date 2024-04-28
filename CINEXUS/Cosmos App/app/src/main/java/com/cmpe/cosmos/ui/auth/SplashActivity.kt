package com.cmpe.cosmos.ui.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cmpe.cosmos.R
import com.cmpe.cosmos.ui.theme.CosmosTheme

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ComposeLottieAnimation(modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun ComposeLottieAnimation(modifier: Modifier) {

    val context = LocalContext.current

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cosmos))

    val progress by animateLottieCompositionAsState(composition)

    val activity = (LocalContext.current as? Activity)

    LottieAnimation(
        modifier = modifier,
        composition = composition
    )

    if (progress == 1.0f) {
        context.startActivity(Intent(context, LoginActivity::class.java))
        activity?.finish()
    }
}