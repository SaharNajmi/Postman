package com.example.postman.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.postman.ui.theme.Blue
import com.example.postman.ui.theme.Brown
import com.example.postman.ui.theme.Gray
import com.example.postman.ui.theme.Green
import com.example.postman.ui.theme.LightBlue
import com.example.postman.ui.theme.Magenta
import com.example.postman.ui.theme.PostmanTheme
import com.example.postman.ui.theme.Purple
import com.example.postman.ui.theme.Red

@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
@Composable()
fun PreviewHomeScreen() {
    PostmanTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            HomeScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

enum class HeaderName(val color: Color) {
    GET(Green),
    POST(Brown),
    PUT(Blue),
    PATCH(Purple),
    DELETE(Red),
    HEAD(Green),
    OPTIONS(Magenta)
}

@Composable()
fun HomeScreen(modifier: Modifier) {
    val headerOptions = listOf(
        HeaderName.GET, HeaderName.POST, HeaderName.PUT, HeaderName.PATCH,
        HeaderName.DELETE, HeaderName.HEAD, HeaderName.OPTIONS
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(headerOptions[0]) }
    var urlRequest by remember { mutableStateOf<String>("") }

    Column(modifier = modifier) {

        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(top = 12.dp, start = 12.dp, bottom = 12.dp)
                    .border(
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.5.dp, color = Color.Black)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    selectedOption.name,
                    color = selectedOption.color,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { expanded = true })
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    modifier = modifier
                        .padding(end = 8.dp)
                        .clickable { expanded = true },
                    contentDescription = "drop down icon"
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    headerOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name, color = option.color) },
                            onClick = {
                                selectedOption = option
                                expanded = false
                            })
                    }
                }
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(38.dp)
                        .background(Gray)
                )

                TextField(
                    value = urlRequest,
                    onValueChange = { urlRequest = it },
                    maxLines = 3,
                    modifier = Modifier.padding(0.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }
            Button(
                modifier = modifier
                    .padding(4.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                shape = RoundedCornerShape(4.dp),
                onClick = {}) {
                Text(text = "Send", fontWeight = FontWeight.Bold)
            }
        }
        HorizontalDivider()
        Text(text = "request response")

    }
}

