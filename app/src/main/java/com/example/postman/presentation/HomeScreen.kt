package com.example.postman.presentation

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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.postman.R
import com.example.postman.data.ApiClient
import com.example.postman.data.ApiRepositoryImp
import com.example.postman.data.ApiUiState
import com.example.postman.data.HighlightedLine
import com.example.postman.ui.theme.Gray
import com.example.postman.ui.theme.LightBlue
import com.example.postman.ui.theme.LightGray
import com.example.postman.ui.theme.LightYellow
import com.example.postman.ui.theme.PostmanTheme
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

val viewModel = HomeViewModel(ApiRepositoryImp(ApiClient.createApiService()))

@Preview(name = "Light Mode")
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true,
//    name = "Dark Mode"
//)
@Composable()
fun PreviewHomeScreen() {

    PostmanTheme {

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            HomeScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel
            )
        }
    }
}

@Composable()
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel
) {
    val methodOptions = listOf(
        MethodName.GET, MethodName.POST, MethodName.PUT, MethodName.PATCH,
        MethodName.DELETE, MethodName.HEAD, MethodName.OPTIONS
    )
    var urlRequest by remember { mutableStateOf<String>("") }
    var expandedMethodOption by remember { mutableStateOf(false) }
    var selectedMethodOption by remember { mutableStateOf(methodOptions[0]) }
    val uiState by viewModel.response.collectAsState()
    Column(modifier = modifier) {

        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically

        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
                    .padding(top = 12.dp, start = 12.dp, bottom = 12.dp)
                    .border(
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(0.5.dp, color = Color.Black)
                    ),
                verticalAlignment = Alignment.CenterVertically
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
                    modifier = modifier
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
                modifier = modifier
                    .padding(4.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    viewModel.request(selectedMethodOption, urlRequest)
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
    val formattedJson = remember(contentText) { formatJson(contentText) }
    val lines = remember(formattedJson) { formattedJson.lines() }
    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }
    var targetMatchIndex by remember { mutableStateOf(0) }

    // Build the full list of lines with highlights and match info
    val highlightedLines: List<HighlightedLine> = remember(lines, searchQuery) {
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

            HighlightedLine(annotated, matchPositions)
        }
    }

    val allMatches = remember(highlightedLines) {
        highlightedLines.flatMapIndexed { lineIndex, line ->
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
            itemsIndexed(highlightedLines) { index, item ->
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
fun ShowApiResponse(uiState: ApiUiState<String>) {
    when (uiState) {
        is ApiUiState.Success -> {
            SearchFromContentText(uiState.data)
        }
        is ApiUiState.Error -> Text(
            text = "Error: ${uiState.message}",
            modifier = Modifier.padding(16.dp, top = 0.dp)
        )

        ApiUiState.Loading -> Text(text = "Loading...")
        ApiUiState.Idle -> {}
    }
}


fun formatJson(json: String): String {
    return try {
        val jsonElement = JsonParser.parseString(json)
        GsonBuilder().setPrettyPrinting().create().toJson(jsonElement)
    } catch (e: Exception) {
        json
    }
}

