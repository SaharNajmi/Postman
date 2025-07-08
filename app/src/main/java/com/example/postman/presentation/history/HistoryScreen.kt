package com.example.postman.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R

@Composable
fun HistoryScreen(navController: NavController,viewModel: HistoryViewModel) {
    LaunchedEffect(Unit) {
        viewModel.getAllHistories()
    }
    val historyRequest = viewModel.historyRequests.collectAsState()
    Column(modifier = Modifier.padding(12.dp)){
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = "backToHome",
            modifier = Modifier
                .padding(top = 24.dp )
                .clickable(onClick = {
                    navController.popBackStack()
                }),
        )
        LazyColumn() {
            items(historyRequest.value.size) { index ->
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = historyRequest.value[index].methodOption.name,
                        color = historyRequest.value[index].methodOption.color,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = historyRequest.value[index].requestUrl.toString(),
                        fontSize = 12.sp,
                    )
                }

            }
        }
    }
}