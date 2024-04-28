package com.cmpe.cosmos.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeatDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (Int) -> Unit,
) {
    var seats by rememberSaveable { mutableIntStateOf(1) }
    AlertDialog(
        title = {
            Text(text = "How many seats?", fontSize = 20.sp)
        },
        text = {
            val selectedValue = remember { mutableIntStateOf(1) }
            val isSelectedItem: (Int) -> Boolean = { selectedValue.intValue == it }
            val onChangeState: (Int) -> Unit =
                {
                    if (selectedValue.intValue == it) selectedValue.intValue =
                        1 else selectedValue.intValue = it
                }
            val interactionSource = remember { MutableInteractionSource() }
            LazyRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                items(8) { index ->
                    Text(
                        text = (index + 1).toString(),
                        color = if (isSelectedItem(index + 1)) Color.White else Color.Black,
                        modifier = Modifier
                            .background(if (isSelectedItem(index + 1)) Color(0xFF109C2F) else Color.Transparent)
                            .padding(8.dp)
                            .clickable(interactionSource = interactionSource, indication = null) {
                                onChangeState(index + 1)
                                seats = selectedValue.intValue
                            }
                    )
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(seats)
                }
            ) {
                Text("Proceed")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(4.dp)
    )
}