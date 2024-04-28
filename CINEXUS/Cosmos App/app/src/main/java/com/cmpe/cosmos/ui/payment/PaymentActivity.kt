package com.cmpe.cosmos.ui.payment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmpe.cosmos.data.local.UserStore
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.util.convertDateFormat
import com.cmpe.cosmos.viewmodel.BookingVM
import com.cmpe.cosmos.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class PaymentActivity : ComponentActivity() {
    private val viewModel: BookingVM by viewModels()
    private val userViewModel: UserVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }

                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState)
                        }
                    ) { paddingValues ->
                        val store = UserStore(this)
                        val user = runBlocking { store.getUser().first() }
                        LaunchedEffect(key1 = Unit) {
                            userViewModel.getRewardPoints(user.userId)
                        }

                        val rewardPoints by userViewModel.rewardPoints.collectAsState()

                        var myPoints = user.rewards.toString()

                        val paymentType = remember { mutableStateOf("RP") }

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

                        val count = this.intent.getIntExtra("count", 1)
                        val seats = this.intent.getStringExtra("seat")
                        val title = this.intent.getStringExtra("title")
                        val showtime = this.intent.getStringExtra("showtime")
                        val date = this.intent.getStringExtra("date")
                        val theater = this.intent.getStringExtra("theater")
                        val poster = this.intent.getStringExtra("poster")
                        val scheduleId = this.intent.getStringExtra("scheduleId")
                        val discount = this.intent.getStringExtra("discount")
                        val discountInt = discount?.toInt() ?: 0
                        val costBeforeDiscount = 15 * count
                        val cost = 15 * count * (100 - discountInt) / 100
                        val serviceFee = 1.5 * count
                        val total = if (user.membership == "Premium") cost else cost + serviceFee

                        val result by viewModel.booking.collectAsState()

                        when (result) {
                            is BookingVM.CreateBookingState.Success -> {
                                startActivity(
                                    Intent(
                                        this@PaymentActivity,
                                        PaymentSuccessfulActivity::class.java
                                    ).apply {
                                        putExtra("seat", seats)
                                        putExtra("total", total.toString())
                                        putExtra("title", title)
                                        putExtra("showtime", showtime)
                                        putExtra("date", date)
                                        putExtra("theater", theater)
                                        putExtra("poster", poster)
                                    }
                                )
                            }

                            is BookingVM.CreateBookingState.Error -> {
                                Text(text = (result as BookingVM.CreateBookingState.Error).message)
                            }

                            is BookingVM.CreateBookingState.Loading -> {
                                LoadingComponent()
                            }

                            is BookingVM.CreateBookingState.Empty -> {
                            }

                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(24.dp)
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Payment",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.size(56.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Ticket cost: ")
                                Text(
                                    text = "\$$costBeforeDiscount",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Service Fee: ")
                                Text(
                                    text = if (user.membership == "Premium") "\$0" else "\$$serviceFee",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Discount: ")
                                Text(
                                    text = "%${discountInt}",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            HorizontalDivider()
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Total: ")
                                Text(text = "\$$total", fontWeight = FontWeight.SemiBold)
                            }

                            Spacer(modifier = Modifier.size(32.dp))

                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                var name by rememberSaveable { mutableStateOf("") }
                                var cardNumber by rememberSaveable { mutableStateOf("") }
                                var expiry by rememberSaveable { mutableStateOf("") }
                                var cvv by rememberSaveable { mutableStateOf("") }
                                var postal by rememberSaveable { mutableStateOf("") }
                                val (selectedOption, onOptionSelected) = remember { mutableStateOf("RP") }
                                var visible by remember { mutableStateOf(false) }

                                Text(text = "Pay Using: ")
                                ElevatedCard(
                                    shape = RoundedCornerShape(4.dp),
                                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = Color(0xFFFFFFFF)
                                    )
                                ) {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .selectable(
                                                selected = ("RP" == selectedOption),
                                                onClick = {
                                                    paymentType.value = "RP"
                                                    onOptionSelected("RP")
                                                    visible = false
                                                },
                                                role = Role.RadioButton
                                            )
                                            .padding(24.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            RadioButton(
                                                selected = ("RP" == selectedOption),
                                                onClick = null
                                            )
                                            Text(
                                                text = "Reward points",
                                                style = MaterialTheme.typography.bodyLarge,
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                        }
                                        Text(
                                            text = myPoints,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                ElevatedCard(
                                    shape = RoundedCornerShape(4.dp),
                                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
                                    colors = CardDefaults.elevatedCardColors(
                                        containerColor = Color(0xFFFFFFFF)
                                    )
                                ) {

                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .selectable(
                                                selected = ("CD" == selectedOption),
                                                onClick = {
                                                    paymentType.value = "CD"
                                                    onOptionSelected("CD")
                                                    visible = true
                                                },
                                                role = Role.RadioButton
                                            )
                                            .padding(24.dp),
                                        horizontalAlignment = Alignment.Start,
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            RadioButton(
                                                selected = ("CD" == selectedOption),
                                                onClick = null
                                            )
                                            Text(
                                                text = "Card",
                                                style = MaterialTheme.typography.bodyLarge,
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                        }

                                        val density = LocalDensity.current
                                        AnimatedVisibility(
                                            visible = visible,
                                            enter = slideInVertically {
                                                // Slide in from 40 dp from the top.
                                                with(density) { -40.dp.roundToPx() }
                                            } + expandVertically(
                                                // Expand from the top.
                                                expandFrom = Alignment.Top
                                            ) + fadeIn(
                                                // Fade in with the initial alpha of 0.3f.
                                                initialAlpha = 0.3f
                                            ),
                                            exit = slideOutVertically() + shrinkVertically() + fadeOut()
                                        ) {
                                            Column {
                                                OutlinedTextField(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    value = name,
                                                    onValueChange = {
                                                        name = it
                                                    },
                                                    label = { Text(text = "Name on card") }
                                                )

                                                OutlinedTextField(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    value = cardNumber,
                                                    onValueChange = {
                                                        cardNumber = it
                                                    },
                                                    label = { Text(text = "Card Number") }
                                                )

                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                                ) {
                                                    OutlinedTextField(
                                                        modifier = Modifier.weight(1f),
                                                        value = expiry,
                                                        onValueChange = {
                                                            expiry = it
                                                        },
                                                        label = { Text(text = "MM/YY") }
                                                    )

                                                    OutlinedTextField(
                                                        modifier = Modifier.weight(1f),
                                                        value = cvv,
                                                        visualTransformation = PasswordVisualTransformation(),
                                                        keyboardOptions = KeyboardOptions(
                                                            keyboardType = KeyboardType.Password
                                                        ),
                                                        onValueChange = {
                                                            cvv = it
                                                        },
                                                        label = { Text(text = "Security Code") }
                                                    )
                                                }

                                                OutlinedTextField(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    value = postal,
                                                    onValueChange = {
                                                        postal = it
                                                    },
                                                    label = { Text(text = "ZIP/Postal Code") }
                                                )

                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.size(24.dp))
                                Button(
                                    onClick = {
                                        Log.d("test", paymentType.value)
                                        if (paymentType.value == "RP") {
                                            if (myPoints.toDouble() >= total.toDouble() * 10) {
                                                viewModel.createBooking(
                                                    user.username,
                                                    scheduleId!!.toInt(),
                                                    total.toDouble(),
                                                    seats!!,
                                                    convertDateFormat(date!!),
                                                    "reward_points"
                                                )
                                            } else {
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    snackbarHostState.showSnackbar("Insufficient Reward Points")
                                                }
                                            }
                                        } else if (paymentType.value == "CD") {
                                            viewModel.createBooking(
                                                user.username,
                                                scheduleId!!.toInt(),
                                                total.toDouble(),
                                                seats!!,
                                                convertDateFormat(date!!),
                                                "card"
                                            )
                                        }

                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 56.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(text = "Pay Now")
                                }
                            }
                            Spacer(modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }
    }
}