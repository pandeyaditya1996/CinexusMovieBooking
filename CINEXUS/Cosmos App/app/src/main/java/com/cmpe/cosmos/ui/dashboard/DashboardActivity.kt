package com.cmpe.cosmos.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.local.LocationStore
import com.cmpe.cosmos.ui.auth.LocationActivity
import com.cmpe.cosmos.ui.common.BottomNavigation
import com.cmpe.cosmos.ui.common.NavigationGraph
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.viewmodel.MovieVM
import com.cmpe.cosmos.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {
    private val movieViewModel: MovieVM by viewModels()
    private val userViewModel: UserVM by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = LocationStore(this)
        setContent {
            CosmosTheme {
                val location = store.getLocation.collectAsState(initial = "")
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = {
                                Text(
                                    text = location.value,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.clickable {
                                        startActivity(
                                            Intent(
                                                this,
                                                LocationActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                )
                            },
                                modifier = Modifier.padding(horizontal = 16.dp),
                                navigationIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.location_pin),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                actions = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.search),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                })
                        },
                        bottomBar = { BottomNavigation(navController = navController) }
                    ) {
                        NavigationGraph(
                            navController = navController,
                            modifier = Modifier.padding(it),
                            viewModel = movieViewModel,
                            userViewModel = userViewModel,
                        )
                    }
                }
            }
        }
    }
}