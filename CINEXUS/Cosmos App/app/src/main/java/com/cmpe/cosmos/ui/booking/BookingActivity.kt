package com.cmpe.cosmos.ui.booking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmpe.cosmos.R
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.common.SeatDialog
import com.cmpe.cosmos.ui.payment.PaymentActivity
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.util.convertDateFormat
import com.cmpe.cosmos.util.convertSeatNamesToIndexArray
import com.cmpe.cosmos.util.convertTo24HourFormat
import com.cmpe.cosmos.util.getSeatName
import com.cmpe.cosmos.viewmodel.BookingVM
import dagger.hilt.android.AndroidEntryPoint

val seats = StringBuilder()

@AndroidEntryPoint
class BookingActivity : ComponentActivity() {
    private val viewModel: BookingVM by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                val openAlertDialog = remember { mutableStateOf(true) }
                var size by rememberSaveable {
                    mutableIntStateOf(1)
                }
                var scheduleId = 1
                val title = this.intent.getStringExtra("title")
                val showtime = this.intent.getStringExtra("showtime")
                val date = this.intent.getStringExtra("date")
                val theater = this.intent.getStringExtra("theater")
                val poster = this.intent.getStringExtra("poster")
                val theaterId = this.intent.getStringExtra("theaterId")
                val movieId = this.intent.getStringExtra("movieId")
                val discount = this.intent.getStringExtra("discount")

                LaunchedEffect(key1 = Unit) {
                    viewModel.getSeatmap(
                        movieId!!.toInt(),
                        theaterId!!.toInt(),
                        convertTo24HourFormat(showtime!!),
                        convertDateFormat(date!!)
                    )
                }
                val seatmap by viewModel.seatmap.collectAsState()

                if (openAlertDialog.value) {
                    SeatDialog(
                        onDismissRequest = { openAlertDialog.value = false },
                        onConfirmation = {
                            size = it
                            openAlertDialog.value = false
                        }
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = {
                                Text(
                                    text = title!!,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                                navigationIcon = {
                                    Icon(
                                        modifier = Modifier.clickable {
                                            finish()
                                        },
                                        painter = painterResource(id = R.drawable.back),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                })
                        }
                    ) {
                        Column(modifier = Modifier.padding(it)) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Text(text = "$date | $showtime ", color = Color(0xFF747474))
                                Spacer(modifier = Modifier.size(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.seat),
                                            contentDescription = "",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(
                                            text = "Unavailable",
                                            fontSize = 13.sp,
                                            color = Color(0xFF747474)
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.seat),
                                            contentDescription = "",
                                            tint = Color(0xFFD8D8D8)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(
                                            text = "Available",
                                            fontSize = 13.sp,
                                            color = Color(0xFF747474)
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.seat),
                                            contentDescription = "",
                                            tint = Color(0xFF29CF00)
                                        )
                                        Spacer(modifier = Modifier.size(8.dp))
                                        Text(
                                            text = "Selected",
                                            fontSize = 13.sp,
                                            color = Color(0xFF747474)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.size(48.dp))
                                Image(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    painter = painterResource(id = R.drawable.screen),
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "All eyes this way!",
                                        color = Color(0xFFAAAAAA),
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.size(24.dp))

                                Column(
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxHeight()
                                ) {
                                    when (seatmap) {
                                        is BookingVM.SeatmapState.Success -> {
                                            scheduleId =
                                                (seatmap as BookingVM.SeatmapState.Success).response.scheduleId!!
                                            Seats(
                                                size,
                                                (seatmap as BookingVM.SeatmapState.Success).response.currentSeatmap!!
                                            )
                                        }

                                        is BookingVM.SeatmapState.Error -> {
                                            Column(
                                                Modifier.fillMaxSize(),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(text = (seatmap as BookingVM.SeatmapState.Error).message)
                                            }
                                        }

                                        is BookingVM.SeatmapState.Loading -> {
                                            LoadingComponent()
                                        }
                                    }


                                    Button(
                                        onClick = {
                                            val seatsString = seats.substring(0, seats.length - 1)
                                            startActivity(
                                                Intent(
                                                    this@BookingActivity,
                                                    PaymentActivity::class.java
                                                ).apply {
                                                    putExtra("count", size)
                                                    putExtra("seat", seatsString)
                                                    putExtra("title", title)
                                                    putExtra("showtime", showtime)
                                                    putExtra("date", date)
                                                    putExtra("theater", theater)
                                                    putExtra("poster", poster)
                                                    putExtra("scheduleId", scheduleId.toString())
                                                    putExtra("discount", discount)
                                                }
                                            )
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 56.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(text = "Proceed To Pay")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Seats(size: Int, seatmap: String) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val selected = mutableListOf<Int>()
        val available = convertSeatNamesToIndexArray(seatmap)
        Log.d("test", available.toString())
        items(100) { index ->
            var isSelected by remember { mutableStateOf(false) }
            val interactionSource = remember { MutableInteractionSource() }
            Icon(
                modifier = Modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = available!![index] == 1
                    ) {
                        if (available[index] == 1) {
                            if (!isSelected && selected.size < size) {
                                isSelected = true
                                selected.add(index + 1)
                            } else if (isSelected) {
                                isSelected = false
                                selected.remove(index + 1)
                            }
                            seats.setLength(0)
                            selected.forEach { seat ->
                                seats.append(getSeatName(seat))
                                seats.append(",")
                            }
                        }
                    },
                painter = painterResource(id = R.drawable.seat),
                contentDescription = "",
                tint = if (available[index] == 1 && isSelected) {
                    Color(0xFF29CF00)
                } else if (available[index] == 1) Color(0xFFD8D8D8)
                else MaterialTheme.colorScheme.primary,
            )
        }
    }
}
