package com.cmpe.cosmos.ui.dashboard.membership

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmpe.cosmos.data.local.UserStore
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.viewmodel.UserVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MembershipPaymentActivity : ComponentActivity() {

    private val userViewModel: UserVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                val context = LocalContext.current
                val store = UserStore(context)

                val user = runBlocking { store.getUser().first() }
                val type = this.intent.getStringExtra("type")

                val membership by userViewModel.membership.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var name by rememberSaveable { mutableStateOf("") }
                    var cardNumber by rememberSaveable { mutableStateOf("") }
                    var expiry by rememberSaveable { mutableStateOf("") }
                    var cvv by rememberSaveable { mutableStateOf("") }
                    var postal by rememberSaveable { mutableStateOf("") }

                    when (membership) {
                        is UserVM.MembershipState.Success -> {
                            if ((membership as UserVM.MembershipState.Success).response.message == "Membership updated successfully") {
                                LaunchedEffect(key1 = Unit) {
                                    store.saveMembershipType("Premium")
                                }
                                startActivity(
                                    Intent(
                                        this@MembershipPaymentActivity,
                                        MembershipPaymentSuccessfulActivity::class.java
                                    )
                                )
                            }
                        }

                        is UserVM.MembershipState.Error -> {
                            Text(text = (membership as UserVM.MembershipState.Error).message)
                        }

                        is UserVM.MembershipState.Loading -> {
                            LoadingComponent()
                        }

                        is UserVM.MembershipState.Empty -> {

                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Payment",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.size(24.dp))
                        Text(
                            text = "$15",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.size(56.dp))
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

                        Spacer(modifier = Modifier.size(24.dp))
                        Button(
                            onClick = {
                                userViewModel.postMembership(user.userId, type!!)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 56.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Pay Now")
                        }
                    }
                }
            }
        }
    }
}