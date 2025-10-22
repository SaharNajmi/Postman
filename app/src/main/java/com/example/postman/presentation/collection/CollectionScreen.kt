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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R
import com.example.postman.common.extensions.parseHttpMethodFromString
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
    val filteredItems = searchCollections(
        collections, searchQuery
    )
    val focusManager = LocalFocusManager.current
    val callbacks = CollectionCallbacks(
        onCollectionItemClick = onCollectionItemClick,
        onRenameRequestClick = { id, newName -> viewModel.changeRequestName(id, newName) },
        onRenameCollectionClick = { collection -> viewModel.updateCollection(collection) },
        onCreateEmptyRequestClick = { collectionId -> viewModel.createAnEmptyRequest(collectionId) },
        onCreateNewCollectionClick = { viewModel.createNewCollection() },
        onHeaderClick = { collectionId -> viewModel.toggleExpanded(collectionId) },
        onDeleteCollectionClick = { collectionId -> viewModel.deleteCollection(collectionId) },
        onDeleteRequestClick = { requestId -> viewModel.deleteRequestItem(requestId) }
    )
    Column(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxSize()
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
            collections.isEmpty() -> CreateNewCollection(callbacks)

            filteredItems.isEmpty() -> NotFoundMessage(searchQuery)
            else -> ExpandedCollectionItems(
                filteredItems,
                callbacks
            )
        }
    }
}


@Composable
private fun CreateNewCollection(callbacks: CollectionCallbacks) {
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
            onClick = { callbacks.onCreateNewCollectionClick() }) {
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
    callbacks: CollectionCallbacks,
) {
    LazyColumn {
        collections.forEachIndexed { index, collection ->
            val allRequests = collection.requests
            item {
                CollectionHeader(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    collection.collectionName,
                    collection.isExpanded,
                    collection,
                    callbacks
                )
            }

            if (allRequests.isNullOrEmpty()) {
                item {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = collection.isExpanded == true
                    ) {
                        AddARequestButton(
                            Modifier.padding(12.dp),
                            collection.collectionId,
                            callbacks
                        )
                    }
                }
            } else {
                items(allRequests.size) { index ->
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = collection.isExpanded == true
                    ) {
                        CollectionItem(
                            Modifier
                                .padding(top = 2.dp, bottom = 2.dp, start = 12.dp),
                            allRequests[index],
                            collection.collectionId,
                            callbacks
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
    collection: Collection,
    callbacks: CollectionCallbacks,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var text by remember {
        mutableStateOf(TextFieldValue(header, TextRange(header.length)))
    }

    LaunchedEffect(header) {
        text = TextFieldValue(header, TextRange(header.length))
    }

    var isEditable by remember { mutableStateOf(false) }
    val icon =
        if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight

    Row(
        modifier = modifier
            .background(if (isExpanded) LightGray else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { callbacks.onHeaderClick(collection.collectionId) }) {
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
                        val newName = text.text
                        if (collection.collectionName != newName) {
                            callbacks.onRenameCollectionClick(collection.copy(collectionName = newName))
                        }
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
                    callbacks.onCreateEmptyRequestClick(collection.collectionId)
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
                    callbacks.onDeleteCollectionClick(collection.collectionId)
                },
            tint = Blue
        )
    }
}

@Composable
fun AddARequestButton(modifier: Modifier, collectionId: String, callbacks: CollectionCallbacks) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text("This collection is empty")
        TextButton(onClick = {
            callbacks.onCreateEmptyRequestClick(collectionId)
        }) {
            Text("Add a request")
        }
    }
}

@Composable
private fun CollectionItem(
    modifier: Modifier,
    request: Request,
    collectionId: String,
    callbacks: CollectionCallbacks,
) {
    val requestName = request.requestName.substringAfter(" ")

    var text by remember {
        val displayText = if (requestName.length > 35) requestName.take(35) + "..." else requestName
        mutableStateOf(TextFieldValue(displayText))
    }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isEditable by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .clickable {
                callbacks.onCollectionItemClick(request.id, collectionId)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val httpMethod = request.requestName.parseHttpMethodFromString()
        Text(
            text = httpMethod.name,
            color = httpMethod.color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

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
                        callbacks.onRenameRequestClick(
                            request.id,
                            "${httpMethod.name} ${text.text}"
                        )
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
            painter = painterResource(R.drawable.ic_delete), contentDescription = "delete",
            Modifier
                .padding(horizontal = 4.dp)
                .size(20.dp)
                .clickable {
                    callbacks.onDeleteRequestClick(request.id)
                }
        )
    }
}