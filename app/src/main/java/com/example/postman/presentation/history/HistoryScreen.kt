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
import androidx.compose.runtime.State
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
import com.example.postman.presentation.base.PickItemDialog
import com.example.postman.presentation.base.searchEntries
import com.example.postman.ui.theme.Blue
import com.example.postman.ui.theme.LightGray

@Composable
fun HistoryScreen(
    navController: NavController,
    viewModel: HistoryViewModel,
    onHistoryItemClick: (Int) -> Unit,
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getHistories()
        viewModel.getCollections()
    }
    val expandedState: State<Map<String, Boolean>> = viewModel.expandedStates.collectAsState()
    val historyRequest by viewModel.httpRequestRequestsModel.collectAsState()
    val collectionNames by viewModel.collectionNames.collectAsState()
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
                onHistoryItemClick, collectionNames,
                expandedState,
                onAddHistoryToCollection = { history, collectionId ->
                    viewModel.addRequestToCollection(history, collectionId)
                    Toast.makeText(
                        context,
                        "successfully added to collections",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onAddHistoriesToCollection = { histories, collectionId ->
                    viewModel.addRequestsToCollection(histories, collectionId)
                    Toast.makeText(
                        context,
                        "successfully added to collections",
                        Toast.LENGTH_SHORT
                    ).show()
                }, onToggleExpandedClick = { date ->
                    viewModel.toggleExpanded(date)
                }, onDeleteHistoriesClick = { historyIds ->
                    viewModel.deleteHistoriesRequest(historyIds)
                },
                onDeleteHistoryClick = { historyId ->
                    viewModel.deleteHistoryRequest(historyId)
                }
            )
        }
    }
}

@Composable
private fun ExpandedHistoryItem(
    historyRequest: Map<String, List<History>>,
    onHistoryItemClick: (Int) -> Unit,
    collectionNames: Map<String, String>,
    expandedState: State<Map<String, Boolean>>,
    onAddHistoryToCollection: (History, String) -> Unit,
    onAddHistoriesToCollection: (List<History>, String) -> Unit,
    onToggleExpandedClick: (String) -> Unit,
    onDeleteHistoriesClick: (historyIds: List<Int>) -> Unit,
    onDeleteHistoryClick: (Int) -> Unit,
) {
    LazyColumn {
        historyRequest.forEach { header, items ->
            item {
                HistoryHeader(
                    header,
                    collectionNames,
                    expandedState.value[header] ?: false,
                    {
                        onToggleExpandedClick(header)
                    },
                    {
                        onDeleteHistoriesClick(items.map { it.id })
                    },
                    { collectionId ->
                        onAddHistoriesToCollection(items, collectionId)
                    }
                )
            }
            items(items.size) { index ->
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(),
                    visible = expandedState.value[header] == true
                ) {
                    HistoryItem(
                        items,
                        collectionNames,
                        index,
                        onHistoryItemClick,
                        onAddHistoryToCollection,
                        onDeleteHistoryClick
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryHeader(
    header: String,
    collectionNames: Map<String, String>,
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit,
    onDeleteHistoriesClicked: () -> Unit,
    onAddHistoriesToCollection: (String) -> Unit,
) {
    val expandedIcon =
        if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight
    var showDropdown by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .clickable { onHeaderClicked() }
            .background(if (isExpanded) LightGray else Color.Transparent)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Icon(
            imageVector = expandedIcon,
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
                    showDropdown = true
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
        if (showDropdown) {
            PickItemDialog(
                collectionNames,
                { showDropdown = false }) { collectionId ->
                onAddHistoriesToCollection(collectionId)
            }
        }
    }
}

@Composable
private fun HistoryItem(
    items: List<History>,
    collectionNames: Map<String, String>,
    index: Int,
    onHistoryItemClick: (Int) -> Unit,
    onAddHistoryToCollection: (History, String) -> Unit,
    onDeleteHistoryClick: (Int) -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
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
                    showDropdown = true
                }
        )
        Icon(
            painter = painterResource(R.drawable.ic_delete), contentDescription = "delete",
            Modifier
                .padding(horizontal = 4.dp)
                .size(20.dp)
                .clickable {
                    onDeleteHistoryClick(items[index].id)
                }
        )
        if (showDropdown) {
            PickItemDialog(
                collectionNames,
                { showDropdown = false }) { collectionId ->
                onAddHistoryToCollection(items[index], collectionId)
            }
        }
    }
}



