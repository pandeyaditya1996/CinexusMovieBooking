package com.cmpe.cosmos.ui.dashboard.account

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.local.UserStore
import com.cmpe.cosmos.ui.auth.LoginActivity
import com.cmpe.cosmos.viewmodel.UserVM
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(modifier: Modifier, userViewModel: UserVM) {

    val context = LocalContext.current
    val store = UserStore(context)

    val user = runBlocking { store.getUser().first() }
    var myPoints = user.rewards.toString()

    if (user.membership == "guest") {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(text = "Please ")
                Text(
                    modifier = Modifier.clickable {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    },
                    text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
            Text(text = " to access Membership benefits.")
        }
    } else {
        LaunchedEffect(key1 = Unit) {
            userViewModel.getRewardPoints(user.userId)
        }

        val rewardPoints by userViewModel.rewardPoints.collectAsState()


        when (rewardPoints) {
            is UserVM.RewardPointsState.Success -> {
                myPoints =
                    (rewardPoints as UserVM.RewardPointsState.Success).response.rewardPoints.toString()
            }

            is UserVM.RewardPointsState.Error -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = (rewardPoints as UserVM.RewardPointsState.Error).message)
                }
            }

            is UserVM.RewardPointsState.Loading -> {
                //LoadingComponent()
            }
        }
        Column(
            modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(24.dp))
            ElevatedCard(
                shape = RoundedCornerShape(4.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color(0xFFFFFFFF)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Membership: ", fontSize = 18.sp)
                    Text(
                        text = user.membership.uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(id = R.drawable.reward),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "My Points:"
                                )
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(
                                    text = myPoints,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                item {
                    ElevatedCard(
                        onClick = {
                            val navigate = Intent(context, MyTicketsActivity::class.java).apply {
                                putExtra("userId", user.userId)
                            }
                            context.startActivity(navigate)
                        },
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(id = R.drawable.tickets),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(text = "My Tickets")
                        }
                    }
                }

                item {
                    ElevatedCard(
                        onClick = {
                            val navigate =
                                Intent(context, MoviesWatchedActivity::class.java).apply {
                                    putExtra("userId", user.userId)
                                }
                            context.startActivity(navigate)
                        },
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(id = R.drawable.movies_watched),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(text = "Movies Watched")
                        }
                    }
                }

                item {
                    ElevatedCard(
                        onClick = {
                            val navigate =
                                Intent(context, CancelBookingActivity::class.java).apply {
                                    putExtra("userId", user.userId)
                                }
                            context.startActivity(navigate)
                        },
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = Color(0xFFFFFFFF)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(id = R.drawable.cancel_booking),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                            Text(text = "Cancel Booking")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.size(32.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, LoginActivity::class.java))
                    (context as Activity).finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 56.dp)
            ) {
                Text(text = "Logout")
            }

        }
    }
}