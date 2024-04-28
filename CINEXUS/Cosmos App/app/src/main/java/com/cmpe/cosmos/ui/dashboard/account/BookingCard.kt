package com.cmpe.cosmos.ui.dashboard.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.constant.ApiEndpoints
import com.cmpe.cosmos.data.remote.models.Bookings
import com.cmpe.cosmos.util.convertTimestampToDate
import com.cmpe.cosmos.util.convertTimestampToTime

@Composable
fun BookingCard(ticket: Bookings, onCanceled: () -> Unit, isCancel: Boolean) {
    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.Start) {
                AsyncImage(
                    model = ApiEndpoints.BASE_TMDB_URL + ApiEndpoints.POSTER + ticket.movies.first().posterUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .height(150.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    placeholder = painterResource(id = R.drawable.placeholder),
                    contentScale = ContentScale.FillHeight
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = ticket.movies.first().title!!,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "Date: "
                        )
                        Text(
                            text = convertTimestampToDate(ticket.showTimestamp!!)
                        )
                    }

                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "Time: "
                        )
                        Text(
                            text = convertTimestampToTime(ticket.showTimestamp!!)
                        )
                    }

                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "Seats: "
                        )
                        Text(
                            text = ticket.seatList!!
                        )
                    }

                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "Total amount: "
                        )
                        Text(
                            text = ticket.totalAmountPaid.toString()
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            if (isCancel) {
                Button(onClick = {
                    onCanceled()
                }) {
                    Text(text = "Cancel Ticket")
                }
            }
        }

    }
}