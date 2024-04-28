package com.cmpe.cosmos.ui.auth

import android.annotation.SuppressLint
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.cmpe.cosmos.ui.common.LoadingComponent
import com.cmpe.cosmos.ui.theme.CosmosTheme
import com.cmpe.cosmos.viewmodel.AuthVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : ComponentActivity() {
    private val viewModel: AuthVM by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
                    ) {
                        SignupCompose(viewModel, snackbarHostState)
                    }
                }
            }
        }
    }
}


@Composable
fun SignupCompose(viewModel: AuthVM, snackbarHostState: SnackbarHostState) {

    val result = viewModel.register.collectAsState()
    val context = LocalContext.current

    when (result.value) {
        is AuthVM.State.RegistrationSuccess -> {
            if ((result.value as AuthVM.State.RegistrationSuccess).response.message == "User registered successfully") {
                LaunchedEffect(key1 = Unit) {
                    snackbarHostState.showSnackbar("User registered successfully")
                }
                context.startActivity(Intent(context, LoginActivity::class.java))
            }
        }

        is AuthVM.State.Error -> {
            LaunchedEffect(key1 = Unit) {
                snackbarHostState.showSnackbar((result.value as AuthVM.State.Error).message)
            }
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

        Text(text = "Create your account", textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.size(32.dp))

        var username by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = { Text(text = "Username") }
        )

        Spacer(modifier = Modifier.size(8.dp))

        var email by rememberSaveable { mutableStateOf("") }

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

        Spacer(modifier = Modifier.size(8.dp))

        var confirmPassword by rememberSaveable { mutableStateOf("") }
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            label = { Text(text = "Confirm password") }
        )

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = { viewModel.register(username, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 56.dp)
        ) {
            Text(text = "SIGN UP")
        }

        Spacer(modifier = Modifier.size(12.dp))

        Row {
            Text(text = "Already have an account? ", textAlign = TextAlign.Center)

            val activity = (LocalContext.current as? Activity)

            Text(
                modifier = Modifier.clickable {
                    activity?.finish()
                },
                text = "Login",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }

    }
}