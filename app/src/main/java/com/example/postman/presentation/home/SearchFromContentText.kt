package com.example.postman.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.postman.R
import com.example.postman.common.extensions.formatJson
import com.example.postman.ui.theme.LightGray
import com.example.postman.ui.theme.LightYellow

@Composable
fun SearchFromContentText(contentText: String) {
    val formattedJson = remember(contentText) { contentText.formatJson() }
    val lines = remember(formattedJson) { formattedJson.lines() }
    val listState = rememberLazyListState()
    var searchQuery by remember { mutableStateOf("") }
    var targetMatchIndex by remember { mutableStateOf(0) }

    val highlightedTextLines = remember(lines, searchQuery) {
        buildHighlightedTextLines(lines, searchQuery)
    }

    val allMatches = remember(highlightedTextLines) {
        highlightedTextLines.flatMapIndexed { index, line ->
            line.matchPositions.map { index }
        }
    }

    val totalMatches = allMatches.size
    val foundIndex = allMatches.getOrNull(targetMatchIndex) ?: -1

    LaunchedEffect(foundIndex) {
        if (foundIndex >= 0) {
            listState.scrollToItem(foundIndex)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        SearchBar(
            searchQuery = searchQuery,
            totalMatches = totalMatches,
            targetMatchIndex = targetMatchIndex,
            onQueryChange = {
                targetMatchIndex = 0
                searchQuery = it
            },
            onPrev = {
                if (totalMatches > 0) {
                    targetMatchIndex =
                        if (targetMatchIndex > 0) targetMatchIndex - 1 else totalMatches - 1
                }
            },
            onNext = {
                if (totalMatches > 0) {
                    targetMatchIndex = (targetMatchIndex + 1) % totalMatches
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        HighlightedTextList(
            lines = highlightedTextLines,
            foundIndex = foundIndex,
            listState = listState
        )
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    totalMatches: Int,
    targetMatchIndex: Int,
    onQueryChange: (String) -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(LightGray)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            label = { Text("Find") },
            modifier = Modifier
                .weight(3f)
                .padding(bottom = 4.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = if (totalMatches == 0) "No results" else "${targetMatchIndex + 1} of $totalMatches",
            fontSize = 12.sp,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(R.drawable.arrow_upward),
            contentDescription = "Previous match",
            modifier = Modifier.clickable { onPrev() }
        )

        Icon(
            painter = painterResource(R.drawable.arrow_downward),
            contentDescription = "Next match",
            modifier = Modifier.clickable { onNext() }
        )
    }
}

@Composable
fun HighlightedTextList(
    lines: List<HighlightedTextLine>,
    foundIndex: Int,
    listState: LazyListState
) {
    LazyColumn(state = listState) {
        itemsIndexed(lines) { index, item ->
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

fun buildHighlightedTextLines(
    lines: List<String>,
    searchQuery: String
): List<HighlightedTextLine> {
    return lines.map { line ->
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