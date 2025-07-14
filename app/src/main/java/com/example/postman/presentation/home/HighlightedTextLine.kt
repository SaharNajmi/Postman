package com.example.postman.presentation.home

import androidx.compose.ui.text.AnnotatedString

data class HighlightedTextLine(
    val annotatedString: AnnotatedString,
    val matchPositions: List<Int>
)