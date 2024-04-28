package com.cmpe.cosmos.ui.auth

import android.R.attr.value
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.cmpe.cosmos.R
import com.cmpe.cosmos.data.local.LocationStore
import com.cmpe.cosmos.model.Locations
import com.cmpe.cosmos.ui.dashboard.DashboardActivity
import com.cmpe.cosmos.ui.theme.CosmosTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LocationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CosmosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Location()
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Location() {
    val context = LocalContext.current
    val selectedValue = remember { mutableStateOf("") }
    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it }
    val onChangeState: (String) -> Unit =
        { if (selectedValue.value == it) selectedValue.value = "" else selectedValue.value = it }
    val store = LocationStore(context)

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp), horizontalAlignment = Alignment.Start
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(start = 16.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Select your city",
                modifier = Modifier.padding(start = 24.dp),
                color = Color.Black
            )

            Spacer(modifier = Modifier.size(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    maxItemsInEachRow = 3
                ) {
                    Locations.values().forEach { location ->
                        LocationChip(
                            text = location.location,
                            selected = isSelectedItem(location.location),
                            onSelected = { onChangeState(location.location) })
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(24.dp))

        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    store.saveLocation(selectedValue.value)
                }
                val intent = Intent(context, DashboardActivity::class.java)
                context.startActivity(intent)
                (context as Activity).finish()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 56.dp),
            enabled = selectedValue.value.isNotEmpty()
        ) {
            Text(text = "Get Started")
        }
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationChip(
    text: String,
    selected: Boolean,
    onSelected: () -> Unit
) {
    InputChip(
        modifier = Modifier.padding(6.dp),
        onClick = {
            onSelected()
        },
        label = {
            Text(
                text = text,
                modifier = Modifier.padding(top = 10.dp, start = 4.dp, bottom = 10.dp),
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
        },
        selected = selected,
        shape = RoundedCornerShape(20.dp),
        colors = InputChipDefaults.inputChipColors(selectedContainerColor = Color.Transparent),
        border = InputChipDefaults.inputChipBorder(
            selectedBorderColor = MaterialTheme.colorScheme.primary,
            selectedBorderWidth = 1.dp
        ),
        trailingIcon = {
            if (selected) {
                Icon(
                    painter = painterResource(id = R.drawable.location),
                    contentDescription = "Localized description",
                    Modifier
                        .size(InputChipDefaults.IconSize)
                        .padding(top = 3.dp, end = 2.dp, bottom = 3.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
    )
}