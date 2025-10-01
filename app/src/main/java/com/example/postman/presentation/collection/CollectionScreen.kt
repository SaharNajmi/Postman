package com.example.postman.presentation.collection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R
import com.example.postman.domain.model.Collection
import com.example.postman.presentation.base.CustomSearchBar
import com.example.postman.presentation.base.CustomToolbar
import com.example.postman.presentation.base.NotFoundMessage
import com.example.postman.presentation.base.searchCollections
import com.example.postman.ui.theme.Blue
import com.example.postman.ui.theme.LightGray
import com.example.postman.ui.theme.Silver

@Composable
fun CollectionScreen(
    navController: NavController,
    viewModel: CollectionViewModel,
    onCollectionItemClick: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.getAllCollections()
    }
    var searchQuery by remember { mutableStateOf("") }
    val collections by viewModel.collections.collectAsState()
    val expandedStates = viewModel.expandedStates.collectAsState()
    val filteredItems = searchCollections(
        collections, searchQuery
    )

    Column(modifier = Modifier.padding(12.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        CustomToolbar("Collections", navController)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                viewModel.createNewCollection()
            }, Modifier.size(24.dp)) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "create new collection",
                )
            }
            CustomSearchBar("Search collections", searchQuery) { searchQuery = it }
        }
        when {
            collections.isEmpty() -> CreateACollection {
               viewModel.createNewCollection()
            }

            filteredItems.isEmpty() -> NotFoundMessage(searchQuery)
            else -> ExpandedCollectionItems(
                filteredItems,
                expandedStates,
                viewModel,
                onCollectionItemClick
            )
        }
    }
}


@Composable
private fun CreateACollection(onCreateCollection: () -> Unit) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create a collection for your requests",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "A collection lets you group related requests",
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            modifier = Modifier.border(width = 1.dp, color = Silver, RoundedCornerShape(8.dp)),
            onClick = { onCreateCollection }) {
            Text(
                "Create Collection",
                color = Color.Black,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ExpandedCollectionItems(
    collections: List<Collection>,
    expandedStates: State<Map<String, Boolean>>,
    viewModel: CollectionViewModel,
    onCollectionItemClick: (Int) -> Unit
) {
    LazyColumn {
        collections.groupBy { it.collectionId to it.collectionName }.map { (key, requests) ->
            val (collectionId, collectionName) = key
            item {
                CollectionHeader(
                    collectionName,
                    expandedStates.value[collectionId] ?: false,
                    {
                        viewModel.toggleExpanded(collectionId)
                    }, {
                        viewModel.deleteRequests(requests.map { it.id })
                    },
                    {
                        viewModel.createAnEmptyRequest(collectionId)
                    })
            }
            items(requests.size) { index ->
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth(),
                    visible = expandedStates.value[collectionId] == true
                ) { CollectionItem(requests, index, onCollectionItemClick, viewModel) }
            }
        }
    }
}

@Composable
fun CollectionHeader(
    header: String,
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onAddNewRequestClick: () -> Unit
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
            Icons.Default.Add, contentDescription = "add new request",
            Modifier
                .padding(horizontal = 4.dp)
                .clickable {
                    onAddNewRequestClick()
                },
            tint = Blue
        )
        Icon(
            painterResource(R.drawable.ic_delete_sweep), contentDescription = "delete lists",
            Modifier
                .padding(horizontal = 4.dp)
                .clickable {
                    onDeleteClicked()
                },
            tint = Blue
        )
    }
}

@Composable
private fun CollectionItem(
    items: List<Collection>,
    index: Int,
    onCollectionItemClick: (Int) -> Unit,
    viewModel: CollectionViewModel
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp)
            .clickable {
                onCollectionItemClick(items[index].id)
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
            text = items[index].collectionId.toString(),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )
        Icon(
            painter = painterResource(R.drawable.ic_delete), contentDescription = "delete",
            Modifier
                .padding(horizontal = 4.dp)
                .size(20.dp)
                .clickable {
                    viewModel.deleteRequestItem(items[index].id)
                }
        )
    }
}