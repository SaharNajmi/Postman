package com.example.postman.presentation.collection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R
import com.example.postman.common.extensions.parseMethodNameFromString
import com.example.postman.domain.model.Collection
import com.example.postman.domain.model.Request
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
    onCollectionItemClick: (Int, String) -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.getCollections()
    }
    var searchQuery by remember { mutableStateOf("") }
    val collections by viewModel.collections.collectAsState()
    val expandedStates = viewModel.expandedStates.collectAsState()
    val filteredItems = searchCollections(
        collections, searchQuery
    )
    val focusManager = LocalFocusManager.current
    val modifier = Modifier
    Column(
        modifier = modifier
            .padding(12.dp)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown().consume()
                    waitForUpOrCancellation()?.let { click ->
                        focusManager.clearFocus()
                    }
                }
            }) {
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
            collections.isEmpty() -> CreateNewCollection {
                viewModel.createNewCollection()
            }

            filteredItems.isEmpty() -> NotFoundMessage(searchQuery)
            else -> ExpandedCollectionItems(
                modifier,
                filteredItems,
                expandedStates,
                viewModel,
                onCollectionItemClick
            )
        }
    }
}


@Composable
private fun CreateNewCollection(onCreateCollectionClick: () -> Unit) {
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
            onClick = { onCreateCollectionClick() }) {
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
    modifier: Modifier,
    collections: List<Collection>,
    expandedStates: State<Map<String, Boolean>>,
    viewModel: CollectionViewModel,
    onCollectionItemClick: (Int, String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxHeight()

    ) {
        collections.forEachIndexed { index, it ->
            val allRequests = it.requests
            item {
                CollectionHeader(
                    modifier,
                    it.collectionName,
                    expandedStates.value[it.collectionId] ?: false,
                    {
                        viewModel.toggleExpanded(it.collectionId)
                    }, {
                        viewModel.deleteCollection(it.collectionId)
                    },
                    {
                        viewModel.createAnEmptyRequest(it.collectionId)
                    }, { newName ->
                        if (it.collectionName != newName) {
                            viewModel.updateCollection(it.copy(collectionName = newName))
                        }
                    })
            }

            if (allRequests.isNullOrEmpty()) {
                item {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = expandedStates.value[it.collectionId] == true
                    ) {
                        AddARequest {
                            viewModel.createAnEmptyRequest(it.collectionId)
                        }
                    }
                }
            } else {
                items(allRequests.size) { index ->
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = expandedStates.value[it.collectionId] == true
                    ) {
                        CollectionItem(
                            allRequests[index],
                            it.collectionId,
                            onCollectionItemClick,
                            viewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CollectionHeader(
    modifier: Modifier,
    header: String,
    isExpanded: Boolean,
    onHeaderClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onAddNewRequestClick: () -> Unit,
    onRenameCollectionClick: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var text by remember {
        mutableStateOf(TextFieldValue(header, TextRange(header.length)))
    }
    var isEditable by remember { mutableStateOf(false) }
    val icon =
        if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isExpanded) LightGray else Color.Transparent)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { onHeaderClicked() }) {
            Icon(
                imageVector = icon,
                contentDescription = "isExpandedIcon",
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            readOnly = !isEditable,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .focusable(isEditable)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && isEditable) {
                        isEditable = false
                        onRenameCollectionClick(text.text)
                    }
                },
            textStyle = TextStyle(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isEditable) Blue else Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    isEditable = false
                    focusManager.clearFocus()
                }
            )
        )

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
            Icons.Default.Edit,
            contentDescription = "rename",
            Modifier
                .padding(horizontal = 4.dp)
                .clickable {
                    if (isEditable) {
                        isEditable = false
                        focusManager.clearFocus()
                    } else {
                        isEditable = true
                        focusRequester.requestFocus()
                    }
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
fun AddARequest(onAddNewRequestClick: () -> Unit) {
    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text("This collection is empty")
        TextButton(onClick = {
            onAddNewRequestClick()
        }) {
            Text("Add a request")
        }
    }
}

@Composable
private fun CollectionItem(
    request: Request,
    collectionId: String,
    onCollectionItemClick: (Int, String) -> Unit,
    viewModel: CollectionViewModel,
) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp)
            .clickable {
                onCollectionItemClick(request.id, collectionId)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val methodOption = request.requestName.parseMethodNameFromString()
        Text(
            text = methodOption.name,
            color = methodOption.color,
            fontSize = 12.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        Text(
            text = request.requestName.substringAfter(" "),
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
                    viewModel.deleteRequestItem(request.id)
                }
        )
    }
}