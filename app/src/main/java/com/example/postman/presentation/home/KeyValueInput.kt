package com.example.postman.presentation.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.postman.ui.theme.LightGreen

@Composable
fun KeyValueInput(
    item: (String, String) -> Unit
) {
    var key by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = LightGreen,
                    shape = RoundedCornerShape(8.dp)
                ),
            value = key,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            onValueChange = {
                key = it
            },
            label = { Text("Key") }
        )
        Spacer(modifier = Modifier.width(4.dp))
        TextField(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = LightGreen,
                    shape = RoundedCornerShape(8.dp)
                ),
            value = value,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            onValueChange = {
                value = it
            },
            label = { Text("Value") }
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "headers",
            modifier = Modifier.clickable {
                item(key.trim(), value.trim())
                key = ""
                value = ""
            })
    }
}