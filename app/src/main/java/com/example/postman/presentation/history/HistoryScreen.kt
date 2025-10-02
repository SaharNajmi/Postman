package com.example.postman.presentation.history

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R
import com.example.postman.domain.model.History
import com.example.postman.presentation.base.CustomSearchBar
import com.example.postman.presentation.base.CustomToolbar
import com.example.postman.presentation.base.NotFoundMessage
import com.example.postman.presentation.base.searchEntries
import com.example.postman.ui.theme.Blue
import com.example.postman.ui.theme.LightGray

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel,
    onHistoryItemClick: (Int) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getAllHistories()
    }
    val historyRequest by viewModel.httpRequestRequestsModel.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredHistoryRequest = searchEntries(
        historyRequest, searchQuery,
        { item, query ->
            item.requestUrl.contains(query, ignoreCase = true)
        })

    Column(modifier = Modifier.padding(12.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        CustomToolbar("History", navController)
        CustomSearchBar("Search by URl", searchQuery) { searchQuery = it }
        if (filteredHistoryRequest.isEmpty()) {
            NotFoundMessage(searchQuery)
        } else {
            ExpandedHistoryItem(
                filteredHistoryRequest,
                onHistoryItemClick,
                { history ->
                    viewModel.addRequestToCollection(
                        history,
                        "8b8e8a3a-ef6a-4090-8c89-15859d09acd7",
                    )
                    Toast.makeText(
                        context,
                        "successfully added to collections",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                viewModel
            )
        }
    }
}

@Composable
private fun ExpandedHistoryItem(
    historyRequest: Map<String, List<History>>,
    onHistoryItemClick: (Int) -> Unit,
    onAddToCollection: (History) -> Unit,
    viewModel: HistoryViewModel
) {
    val context = LocalContext.current
    val expandedStates = viewModel.expandedStates.collectAsState()

    LazyColumn {
        historyRequest.forEach { header, items ->
            item {
                HistoryHeader(
                    header, expandedStates.value[header] ?: false,
                    {
                        viewModel.toggleExpanded(header)
                    },
                    {
                        viewModel.deleteHistoriesRequest(items.map { it.id })
                    },
                    {
                        viewModel.addRequestsToCollection(items, "test2")
                        Toast.makeText(
                            context,
                            "successfully added to collections",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            items(items.size) { index ->
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(),
                    visible = expandedStates.value[header] == true
                ) {
                    HistoryItem(items, index, onHistoryItemClick, onAddToCollection, viewModel)
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
    onDeleteHistoriesClicked: () -> Unit,
    onAddToCollection: () -> Unit
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
            imageVector = Icons.Default.Add, contentDescription = "add requests to collections",
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    onAddToCollection()
                }, tint = Blue
        )
        Icon(
            painterResource(R.drawable.ic_delete_sweep), contentDescription = "delete list by date",
            Modifier
                .padding(horizontal = 4.dp)
                .clickable {
                    onDeleteHistoriesClicked()
                },
            tint = Blue
        )
    }
}

@Composable
private fun HistoryItem(
    items: List<History>,
    index: Int,
    onHistoryItemClick: (Int) -> Unit,
    onAddToCollection: (History) -> Unit,
    viewModel: HistoryViewModel
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp)
            .clickable {
                onHistoryItemClick(items[index].id)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = items[index].methodOption.name,
            color = items[index].methodOption.color,
            fontSize = 12.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = items[index].requestUrl.toString(),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.Add, contentDescription = "add request to collections",
            modifier = Modifier
                .size(20.dp)
                .clickable {
                    onAddToCollection(items[index])
                }
        )
        Icon(
            painter = painterResource(R.drawable.ic_delete), contentDescription = "delete",
            Modifier
                .padding(horizontal = 4.dp)
                .size(20.dp)
                .clickable {
                    viewModel.deleteHistoryRequest(items[index].id)
                }
        )
    }
}




