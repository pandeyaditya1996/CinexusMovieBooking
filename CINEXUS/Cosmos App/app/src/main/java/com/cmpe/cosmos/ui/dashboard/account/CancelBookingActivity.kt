package com.cmpe.cosmos.ui.dashboard.account

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.util.isTimestampAfterCurrent
import com.cmpe.cosmos.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CancelBookingActivity : ComponentActivity() {
    private val viewModel: UserVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {

                val userId = this.intent.getIntExtra("userId", 0)

                LaunchedEffect(key1 = Unit) {
                    viewModel.getBookings(userId)
                }
                val bookingList by viewModel.bookings.collectAsState()
                val cancel by viewModel.cancelBooking.collectAsState()

                val isNoBooking = remember {
                    mutableStateOf(false)
                }
                val snackbarHostState = remember { SnackbarHostState() }

                when (cancel) {
                    is UserVM.CancelBookingState.Success -> {

                        val result =
                            (cancel as UserVM.CancelBookingState.Success).response
                        if (result.message == "Booking cancelled successfully") {
                            startActivity(Intent(this, TicketsCancelledActivity::class.java))
                            finish()
                        }
                    }

                    is UserVM.CancelBookingState.Error -> {
                        if ((cancel as UserVM.CancelBookingState.Error).message == "HTTP 400 Bad Request") {
                            LaunchedEffect(key1 = Unit) {
                                snackbarHostState.showSnackbar("Bookings made with reward points cannot be refunded")
                            }
                        }
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = (cancel as UserVM.CancelBookingState.Error).message)
                        }
                    }

                    is UserVM.CancelBookingState.Loading -> {
                        //LoadingComponent()
                    }
                }


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        }
                    ) { paddingValues ->

                        if (isNoBooking.value) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "No current bookings available")
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(paddingValues)
                                .padding(top = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Current Bookings",
                                fontWeight = FontWeight.SemiBold
                            )


                            when (bookingList) {
                                is UserVM.UserBookingsState.Success -> {

                                    val result =
                                        (bookingList as UserVM.UserBookingsState.Success).response

                                    LazyColumn(
                                        contentPadding = PaddingValues(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(20.dp)
                                    ) {
                                        val tickets =
                                            result.filter { tickets ->
                                                isTimestampAfterCurrent(tickets.showTimestamp!!).and(
                                                    !tickets.isRefundRequested!!
                                                )
                                            }
                                        isNoBooking.value = tickets.isEmpty()
                                        items(tickets) { ticket ->
                                            BookingCard(ticket, {
                                                viewModel.cancelBooking(ticket.bookingId!!)
                                            }, true)
                                        }
                                    }
                                }

                                is UserVM.UserBookingsState.Error -> {
                                    Column(
                                        Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = (bookingList as UserVM.UserBookingsState.Error).message)
                                    }
                                }

                                is UserVM.UserBookingsState.Loading -> {
                                    LoadingComponent()
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}