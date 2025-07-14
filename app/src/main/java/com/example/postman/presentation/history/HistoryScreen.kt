package com.example.postman.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R
import com.example.postman.common.utils.formatDate
import com.example.postman.ui.theme.LightGray

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel,
    onHistoryItemClick: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getAllHistories()
    }

    val historyRequest by viewModel.historyRequestsModel.collectAsState()

    Column(modifier = Modifier.padding(12.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "backToHome",
                modifier = Modifier
                    .clickable(onClick = {
                        navController.popBackStack()
                    }),
            )
            Text(text = "History", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(thickness = 1.dp)
        LazyColumn() {
            items(historyRequest.size) { index ->
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .clickable {
                            onHistoryItemClick(historyRequest[index].id)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = historyRequest[index].methodOption.name,
                        color = historyRequest[index].methodOption.color,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = historyRequest[index].requestUrl.toString(),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = formatDate(historyRequest[index].createdAt),
                        Modifier
                            .clip(shape = RoundedCornerShape(4.dp))
                            .background(
                                LightGray
                            )
                            .padding(horizontal = 2.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = "delete",
                        Modifier
                            .padding(horizontal = 4.dp)
                            .clickable {
                                viewModel.deleteHistoryRequest(historyRequest[index])
                            })
                }
            }
        }
    }
}