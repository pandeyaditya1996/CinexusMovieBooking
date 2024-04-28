package com.cmpe.cosmos.ui.dashboard.membership

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmpe.cosmos.data.local.UserStore
import com.cmpe.cosmos.ui.auth.SignupActivity
import com.cmpe.cosmos.util.getNextYearDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun MembershipScreen(modifier: Modifier) {

    val context = LocalContext.current
    val store = UserStore(context)

    val user = runBlocking { store.getUser().first() }

    if (user.membership == "Premium") {
        Column(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Already a Premium member",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                text = "Expires on: ${getNextYearDate()}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    } else {
        Column(
            modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Column {
                Text(
                    text = "Become a Member Today",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = "Join our Premium membership today and enjoy the perk" +
                            " of having the online service fee waived on every booking," +
                            " saving you \$1.50 per ticket!"
                )
            }

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                val (selectedOption, onOptionSelected) = remember { mutableStateOf("RM") }

                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    if (user.membership != "Regular") {
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
                                        selected = ("RM" == selectedOption),
                                        onClick = { onOptionSelected("RM") },
                                        role = Role.RadioButton
                                    )
                                    .padding(24.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row {
                                    RadioButton(
                                        selected = ("RM" == selectedOption),
                                        onClick = null
                                    )
                                    Text(
                                        text = "Regular Membership",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                                Text(
                                    text = "\$0",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
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
                                    selected = ("PM" == selectedOption),
                                    onClick = { onOptionSelected("PM") },
                                    role = Role.RadioButton
                                )
                                .padding(24.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                RadioButton(
                                    selected = ("PM" == selectedOption),
                                    onClick = null
                                )
                                Text(
                                    text = "Premium Membership",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                            Text(
                                text = "\$15",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.size(16.dp))
                val myContext = LocalContext.current
                Button(
                    onClick = {
                        if (user.membership == "Regular") {
                            myContext.startActivity(
                                Intent(
                                    myContext,
                                    MembershipPaymentActivity::class.java
                                ).apply {
                                    putExtra("type", "Premium")
                                }
                            )
                        } else {
                            myContext.startActivity(
                                Intent(
                                    myContext,
                                    SignupActivity::class.java
                                )
                            )
                            (myContext as Activity).finish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 56.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Enroll")
                }
            }
        }
    }
}