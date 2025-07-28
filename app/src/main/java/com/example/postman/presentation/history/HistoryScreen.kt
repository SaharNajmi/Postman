package com.example.postman.presentation.history

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R
import com.example.postman.common.compose.rememberMutableStateMapOf
import com.example.postman.domain.model.History
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

    val historyRequest by viewModel.httpRequestRequestsModel.collectAsState()

    Column(modifier = Modifier.padding(12.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Toolbar(navController)
        ExpandedHistoryItem(historyRequest, onHistoryItemClick, viewModel)
    }
}

@Composable
private fun Toolbar(navController: NavController) {
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
}

@Composable
private fun ExpandedHistoryItem(
    historyRequest: Map<String, List<History>>,
    onHistoryItemClick: (Int) -> Unit,
    viewModel: HistoryViewModel
) {
    val expandedStates = rememberMutableStateMapOf<String, Boolean>()

    LazyColumn {
        historyRequest.forEach { header, items ->
            item {
                HistoryHeader(
                    header, expandedStates[header] ?: false,
                    { expandedStates[header] = !(expandedStates[header] ?: false) },
                    {
                        viewModel.deleteHistoriesRequest(items.map { it.id })
                    })
            }
            items(items.size) { index ->
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(),
                    visible = expandedStates[header] ?: false
                ) {
                    HistoryItem(items, index, onHistoryItemClick, viewModel)
                }
            }
        }
    }
}

@Composable
fun HistoryHeader(
    header: String,
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit,
    onDeleteHistoriesClicked: () -> Unit
) {
    val icon = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight
    Row(
        modifier = Modifier
            .clickable { onHeaderClicked() }
            .background(if (isExpanded) LightGray else Color.Transparent)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Icon(
            imageVector = icon,
            contentDescription = "isExpandedIcon"
        )

        Text(
            text = header,
            modifier = Modifier.padding(start = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Delete, contentDescription = "delete list by date",
            Modifier
                .padding(horizontal = 4.dp)
                .clickable {
                    onDeleteHistoriesClicked()
                })
    }
}

@Composable
private fun HistoryItem(
    items: List<History>,
    index: Int,
    onHistoryItemClick: (Int) -> Unit,
    viewModel: HistoryViewModel
) {
    Row(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .clickable {
                onHistoryItemClick(items[index].id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = items[index].methodOption.name,
            color = items[index].methodOption.color,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = items[index].requestUrl.toString(),
            fontSize = 12.sp,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )

        Icon(
            imageVector = Icons.Default.Delete, contentDescription = "delete",
            Modifier
                .padding(horizontal = 4.dp)
                .clickable {
                    viewModel.deleteHistoryRequest(items[index].id)
                })
    }
}





