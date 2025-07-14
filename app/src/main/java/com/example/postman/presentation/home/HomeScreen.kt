package com.example.postman.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.postman.R
import com.example.postman.common.extensions.formatJson
import com.example.postman.presentation.base.BaseUiState
import com.example.postman.common.utils.MethodName
import com.example.postman.presentation.navigation.Screens
import com.example.postman.ui.theme.Gray
import com.example.postman.ui.theme.Green
import com.example.postman.ui.theme.LightBlue
import com.example.postman.ui.theme.LightGray
import com.example.postman.ui.theme.LightGreen
import com.example.postman.ui.theme.LightYellow
import com.example.postman.ui.theme.PostmanTheme

@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
@Composable()
fun PreviewHomeScreen() {

    val nav = rememberNavController()
    PostmanTheme {
        Surface {
            HomeScreen(
                // modifier = Modifier.padding(innerPadding),
                hiltViewModel(), -1,
                onNavigateToHistory = {
                    nav.navigate(Screens.HistoryScreen)
                }
            )
        }
    }
}

@Composable()
fun HomeScreen(
    homeViewModel: HomeViewModel,
    historyId: Int,
    onNavigateToHistory: () -> Unit,
) {
    val methodOptions = listOf(
        MethodName.GET, MethodName.POST, MethodName.PUT, MethodName.PATCH,
        MethodName.DELETE, MethodName.HEAD, MethodName.OPTIONS
    )

    val uiState by homeViewModel.uiState.collectAsState()
    val historyModel by homeViewModel.historyRequest.collectAsState()
    var urlRequest by remember { mutableStateOf<String>("") }
    var expandedMethodOption by remember { mutableStateOf(false) }
    var selectedMethodOption by remember { mutableStateOf(methodOptions[0]) }

    LaunchedEffect(historyId) {
        if (historyId != -1) {
            homeViewModel.loadRequestFromHistory(historyId)
        }
    }
    LaunchedEffect(historyModel) {
        historyModel?.let {
            urlRequest = it.requestUrl
            selectedMethodOption = it.methodOption
        }
    }
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
                    selectedMethodOption.name,
                    color = selectedMethodOption.color,
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
                                selectedMethodOption = option
                                expandedMethodOption = false
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
                modifier = Modifier
                    .padding(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    if (urlRequest.isNotEmpty())
                        homeViewModel.sendRequest(selectedMethodOption, urlRequest)
                }) {
                Text(text = "Send", fontWeight = FontWeight.Bold)
            }
        }
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

            historyModel?.statusCode?.takeIf { it != -1 }?.let { statusCode ->
                Text(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            LightGreen
                        )
                        .padding(horizontal = 4.dp),
                    color = Green,
                    text = statusCode.toString(),
                )
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
        ShowApiResponse(uiState)
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
fun ShowApiResponse(
    uiState: BaseUiState<String>,
) {
    when (uiState) {
        is BaseUiState.Success -> {
            SearchFromContentText(uiState.data)
        }

        is BaseUiState.Error -> Text(
            text = "Error: ${uiState.message}",
            modifier = Modifier.padding(16.dp, top = 0.dp)
        )

        BaseUiState.Loading -> Text(text = "Loading...")
        BaseUiState.Idle -> {}
    }
}

