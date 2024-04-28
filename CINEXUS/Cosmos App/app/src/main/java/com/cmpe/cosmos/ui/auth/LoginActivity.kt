package com.cmpe.cosmos.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.local.LocationStore
import com.cmpe.cosmos.data.local.UserStore
import com.cmpe.cosmos.model.UserModel
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.dashboard.DashboardActivity
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.viewmodel.AuthVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel: AuthVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginCompose(viewModel)
                }
            }
        }
    }
}

@Composable
fun LoginCompose(viewModel: AuthVM) {

    val result = viewModel.login.collectAsState()

    val context = LocalContext.current
    val store = LocationStore(context)
    val location = store.getLocation.collectAsState(initial = "")
    val userStore = UserStore(context)
    var email by rememberSaveable { mutableStateOf("") }

    when (result.value) {
        is AuthVM.State.LoginSuccess -> {
            val user = (result.value as AuthVM.State.LoginSuccess).response
            LaunchedEffect(key1 = Unit) {
                userStore.saveUser(
                    user = UserModel(
                        userId = user.userId!!,
                        username = email,
                        membership = user.membership!!,
                        rewards = user.reward!!
                    )
                )
            }
            if (location.value.isEmpty())
                context.startActivity(Intent(context, LocationActivity::class.java))
            else
                context.startActivity(Intent(context, DashboardActivity::class.java))

            (context as Activity).finish()
        }

        is AuthVM.State.Error -> {
            Text(text = (result.value as AuthVM.State.Error).message)
        }

        is AuthVM.State.Loading -> {
            LoadingComponent()
        }

        is AuthVM.State.Empty -> {
        }

        else -> {}
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.size(64.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(160.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.size(24.dp))

        Text(text = "Please enter your \nemail address and password", textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.size(32.dp))


        //todo add email validation
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text(text = "Enter email address") }
        )

        Spacer(modifier = Modifier.size(8.dp))

        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text(text = "Enter password") },
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.visible)
                else painterResource(id = R.drawable.invisible)
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, description, Modifier.size(24.dp))
                }
            }
        )

        Spacer(modifier = Modifier.size(32.dp))

        Button(
            onClick = {
                viewModel.doLogin(email, password)
                //context.startActivity(Intent(context, LocationActivity::class.java))
            },
            enabled = email.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 56.dp)
        ) {
            Text(text = "LOGIN")
        }

        Spacer(modifier = Modifier.size(24.dp))

        Row {
            Text(text = "Don't have an account? ", textAlign = TextAlign.Center)

            Text(
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, SignupActivity::class.java))
                },
                text = "Sign up",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp, horizontal = 56.dp))
        Row {
            Text(text = "Continue as a ", textAlign = TextAlign.Center)

            Text(
                modifier = Modifier.clickable {
                    CoroutineScope(Dispatchers.IO).launch {
                        userStore.saveUser(
                            user = UserModel(
                                userId = 0,
                                username = "guest@gmail.com",
                                membership = "guest",
                                rewards = 0
                            )
                        )
                    }
                    context.startActivity(Intent(context, LocationActivity::class.java))
                },
                text = "Guest",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}