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
import androidx.compose.runtime.collectAsState
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
import com.example.postman.data.ApiClient
import com.example.postman.data.ApiRepositoryImp
import com.example.postman.ui.theme.Gray
import com.example.postman.ui.theme.LightBlue
import com.example.postman.ui.theme.PostmanTheme
val viewModel= HomeViewModel(ApiRepositoryImp(ApiClient.createApiService()))

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
                modifier = Modifier.padding(innerPadding),
                viewModel
            )
        }
    }
}

@Composable()
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel )
{
    val methodOptions = listOf(
        MethodName.GET, MethodName.POST, MethodName.PUT, MethodName.PATCH,
        MethodName.DELETE, MethodName.HEAD, MethodName.OPTIONS
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedMethodOption by remember { mutableStateOf(methodOptions[0]) }
    var urlRequest by remember { mutableStateOf<String>("") }
    val uiState by viewModel.response.collectAsState()

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
                    selectedMethodOption.name,
                    color = selectedMethodOption.color,
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
                    methodOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name, color = option.color) },
                            onClick = {
                                selectedMethodOption = option
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
                onClick = {
                    viewModel.request(selectedMethodOption, urlRequest)
                }) {
                Text(text = "Send", fontWeight = FontWeight.Bold)
            }
        }
        HorizontalDivider()

        Text(text = "request response: $uiState")

    }
}