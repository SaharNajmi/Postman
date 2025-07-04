package com.example.postman.data

import androidx.compose.ui.text.AnnotatedString

data class HighlightedLine(
    val annotatedString: AnnotatedString,
    val matchPositions: List<Int>
)