package com.cmpe.cosmos.ui.dashboard.theater

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.entities.Theaters


@Composable
fun TheaterCard(theater: Theaters) {
    val context = LocalContext.current
    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFFFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = theater.name, fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = painterResource(id = R.drawable.pin), contentDescription = "")
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = theater.address)
            }
            Spacer(modifier = Modifier.size(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.pin),
                    contentDescription = "",
                    modifier = Modifier.size(16.dp)
                )
                Text(text = "${theater.distance} away", fontSize = 12.sp)
                Spacer(modifier = Modifier.size(4.dp))
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(
                                context,
                                TheaterDetailActivity::class.java
                            ).apply {
                                putExtra("theaterId", theater.theaterId.toString())
                                putExtra("name", theater.name)
                                putExtra("address", theater.address)
                                putExtra("distance", theater.distance)
                                putExtra("coordinates", theater.coordinates)
                            }
                        )
                    },
                    shape = RoundedCornerShape(2.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    Text(text = "View Shows")
                }
                Spacer(modifier = Modifier.size(4.dp))
                Row(modifier = Modifier.clickable {
                    val title = theater.name
                    val geoUri =
                        Uri.parse("http://maps.google.com/maps?q=loc:${theater.coordinates} ($title)")
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(mapIntent)
                }, verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.direction),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(text = "Directions", fontSize = 12.sp)
                }
            }
        }
    }
}