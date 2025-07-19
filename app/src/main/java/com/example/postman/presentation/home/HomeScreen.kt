package com.example.postman.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.stylusHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.postman.R
import com.example.postman.common.extensions.formatJson
import com.example.postman.common.utils.MethodName
import com.example.postman.domain.model.HeaderItem
import com.example.postman.presentation.base.Loadable
import com.example.postman.ui.theme.Gray
import com.example.postman.ui.theme.Green
import com.example.postman.ui.theme.LightBlue
import com.example.postman.ui.theme.LightGray
import com.example.postman.ui.theme.LightGreen
import com.example.postman.ui.theme.LightYellow
import com.example.postman.ui.theme.RadioButtonSelectedColor

enum class RadioHttpParameterOptions {
    Params, Auth, Header, Body
}


@Composable
fun RequestParametersSection(
    modifier: Modifier,
    uiState: HomeUiState,
    homeViewModel: HomeViewModel,
//    headers: (Map<String, String>) -> Unit
) {
    val radioHttpParameterOptions = RadioHttpParameterOptions.entries.toList()
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioHttpParameterOptions[0]) }

    Column(modifier = modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            radioHttpParameterOptions.forEach { option ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = (option == selectedOption),
                            onClick = {
                                onOptionSelected(option)
                            }, role = Role.RadioButton
                        )
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (option == selectedOption), onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = RadioButtonSelectedColor,
                            unselectedColor = RadioButtonSelectedColor
                        ), modifier = Modifier.padding(end = 2.dp)
                    )
                    Text(text = option.name)
                }
            }
        }

        when (selectedOption) {
            RadioHttpParameterOptions.Auth -> AuthSection(
                modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            RadioHttpParameterOptions.Params -> ParamsSection(
                modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            RadioHttpParameterOptions.Header -> HeaderSection(
                modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp), uiState, homeViewModel
            )

            RadioHttpParameterOptions.Body -> BodySection(
                modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp), uiState, homeViewModel
            )
        }
    }
}

@Composable
fun ParamsSection(modifier: Modifier) {
    Column(modifier) { }
}

@Composable
fun AuthSection(modifier: Modifier) {
    Column(modifier) { }
}

@Composable
fun HeaderSection(
    modifier: Modifier,
    uiState: HomeUiState,
    homeViewModel: HomeViewModel
) {
    var header by remember { mutableStateOf(HeaderItem()) }

    Column(modifier) {
        RemovableTagList(
            items = uiState.data.headers,
            onRemoveItem = { key, value ->
                homeViewModel.removeHeader(key, value)
            }
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = LightGreen,
                        shape = RoundedCornerShape(8.dp)
                    ),
                value = header.key,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                onValueChange = {
                    header = header.copy(key = it)
                },
                label = { Text("Key") }
            )
            Spacer(modifier = Modifier.width(4.dp))
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = LightGreen,
                        shape = RoundedCornerShape(8.dp)
                    ),
                value = header.value,
                maxLines = 1,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                onValueChange = {
                    header = header.copy(value = it)
                },
                label = { Text("Value") }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "headers",
                modifier = Modifier.clickable {
                    homeViewModel.addHeader(header.key, header.value)
                    header = HeaderItem()
                })
        }
    }
}

@Composable
fun RemovableTagList(items: Map<String, String>?, onRemoveItem: (String, String) -> Unit) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .height(46.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(LightGreen)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items?.forEach { (key, value) ->
            Text(
                text = "$key : $value",
                modifier = Modifier
                    .clickable {
                        onRemoveItem(key, value)
                    }
                    .padding(8.dp)
                    .background(LightGray, shape = MaterialTheme.shapes.small)
                    .padding(4.dp),
            )
        }
    }
}

@Composable
fun BodySection(
    modifier: Modifier,
    uiState: HomeUiState,
    homeViewModel: HomeViewModel
) {

    TextField(
        value = uiState.data.body ?: "",
        onValueChange = {
            homeViewModel.updateRequest(uiState.data.copy(body = it))
        },
        maxLines = Int.MAX_VALUE,
        modifier = modifier
            .padding(0.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable()
fun HomeScreen(
    homeViewModel: HomeViewModel,
    historyId: Int,
    onNavigateToHistory: () -> Unit,
) {

    val uiState by homeViewModel.uiState.collectAsState()

    LaunchedEffect(historyId) {
        if (historyId != -1) {
            homeViewModel.loadRequestFromHistory(historyId)
        }
    }

    RequestBuilder(
        uiState,
        onNavigateToHistory,
        homeViewModel
    )
}

@Composable
fun RequestBuilder(
    uiState: HomeUiState,
    onNavigateToHistory: () -> Unit,
    homeViewModel: HomeViewModel
) {
    val request = uiState.data
    val methodOptions = listOf(
        MethodName.GET, MethodName.POST, MethodName.PUT, MethodName.PATCH,
        MethodName.DELETE, MethodName.HEAD, MethodName.OPTIONS
    )
    var expandedMethodOption by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        TextButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 12.dp)
                .stylusHoverIcon(
                    icon = PointerIcon(R.drawable.arrow_upward)
                ), onClick = {
                onNavigateToHistory()
            }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.outline_history),
                    contentDescription = "history"
                )
                Text("History")
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
                    .border(
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.5.dp, color = Color.Black)
                    ),
            ) {
                Text(
                    request.methodOption.name,
                    color = request.methodOption.color,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(12.dp)
                        .clickable { expandedMethodOption = true })
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { expandedMethodOption = true },
                    contentDescription = "drop down icon"
                )

                DropdownMenu(
                    expanded = expandedMethodOption,
                    onDismissRequest = { expandedMethodOption = false }
                ) {
                    methodOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name, color = option.color) },
                            onClick = {
                                expandedMethodOption = false
                                homeViewModel.updateRequest(request.copy(methodOption = option))
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
                    value = request.requestUrl,
                    onValueChange = {
                        homeViewModel.updateRequest(request.copy(requestUrl = it))
                    },
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
                modifier = Modifier
                    .padding(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                shape = RoundedCornerShape(4.dp),
                onClick = { homeViewModel.sendRequest() }
            ) {
                Text(text = "Send", fontWeight = FontWeight.Bold)
            }
        }
        RequestParametersSection(
            modifier = Modifier,
            homeViewModel = homeViewModel,
            uiState = uiState,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "{ } JSON",
                modifier = Modifier
                    .background(LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(6.dp, 2.dp)
            )

            uiState.response?.let {
                if (uiState.response is Loadable.Success) {
                    Text(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                LightGreen
                            )
                            .padding(horizontal = 4.dp),
                        color = Green,
                        text = uiState.response.data.statusCode.toString(),
                    )
                }
            }

            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search",
                modifier = Modifier.padding(end = 4.dp)
            )
            Icon(
                painter = painterResource(R.drawable.outline_content_copy),
                contentDescription = "copy all"
            )
        }
        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(4.dp))
        ResponseBody(uiState)
    }
}

@Composable
fun SearchFromContentText(contentText: String) {
    val formattedJson = remember(contentText) { contentText.formatJson() }
    val lines = remember(formattedJson) { formattedJson.lines() }
    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }
    var targetMatchIndex by remember { mutableStateOf(0) }

    // Build the full list of lines with highlights and match info
    val highlightedTextLines: List<HighlightedTextLine> = remember(lines, searchQuery) {
        lines.map { line ->
            val lowerLine = line.lowercase()
            val lowerTerm = searchQuery.lowercase()
            var currentIndex = 0
            val matchPositions = mutableListOf<Int>()

            val annotated = buildAnnotatedString {
                if (lowerTerm.isEmpty()) {
                    append(line)
                } else {
                    while (currentIndex < line.length) {
                        val matchIndex = lowerLine.indexOf(lowerTerm, currentIndex)
                        if (matchIndex == -1) {
                            append(line.substring(currentIndex))
                            break
                        }

                        append(line.substring(currentIndex, matchIndex))

                        withStyle(
                            style = SpanStyle(
                                background = Color.Yellow,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(line.substring(matchIndex, matchIndex + searchQuery.length))
                        }

                        matchPositions.add(matchIndex)
                        currentIndex = matchIndex + searchQuery.length
                    }
                }
            }

            HighlightedTextLine(annotated, matchPositions)
        }
    }

    val allMatches = remember(highlightedTextLines) {
        highlightedTextLines.flatMapIndexed { lineIndex, line ->
            line.matchPositions.map { lineIndex }
        }
    }

    val totalMatches = allMatches.size
    val foundIndex = allMatches.getOrNull(targetMatchIndex) ?: -1

    // Scroll to match when targetMatchIndex changes
    LaunchedEffect(foundIndex) {
        if (foundIndex >= 0) {
            listState.scrollToItem(foundIndex)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(LightGray)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    targetMatchIndex = 0
                    searchQuery = it
                },
                label = { Text("Find") },
                modifier = Modifier
                    .weight(3f)
                    .padding(bottom = 4.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (totalMatches == 0) "No results"
                else "${targetMatchIndex + 1} of $totalMatches",
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(R.drawable.arrow_upward),
                contentDescription = "Previous match",
                modifier = Modifier.clickable {
                    if (totalMatches > 0) {
                        targetMatchIndex =
                            if (targetMatchIndex > 0) targetMatchIndex - 1 else totalMatches - 1
                    }
                }
            )

            Icon(
                painter = painterResource(R.drawable.arrow_downward),
                contentDescription = "Next match",
                modifier = Modifier.clickable {
                    if (totalMatches > 0) {
                        targetMatchIndex = (targetMatchIndex + 1) % totalMatches
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(state = listState) {
            itemsIndexed(highlightedTextLines) { index, item ->
                Text(
                    text = item.annotatedString,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(if (index == foundIndex) LightYellow else Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun ResponseBody(
    uiState: HomeUiState,
) {
    when (uiState.response) {
        is Loadable.Success -> {
            SearchFromContentText(uiState.response.data.response)

            if (uiState.response.data.imageResponse != null) {
                Image(
                    bitmap = uiState.response.data.imageResponse,
                    contentDescription = "Decoded Image",
                    modifier = Modifier.fillMaxWidth(), alignment = Alignment.Center,
                )
            }
        }

        is Loadable.Error -> Text(
            text = "Error: ${uiState.response.message}",
            modifier = Modifier.padding(16.dp), color = Color.Red
        )

        Loadable.Loading -> CircularProgressIndicator()
        is Loadable.NetworkError -> Text(
            text = "Error: ${uiState.response.message}",
            modifier = Modifier.padding(16.dp), color = Color.Red
        )

        null -> {}
    }
}