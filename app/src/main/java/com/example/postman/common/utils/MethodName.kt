package com.example.postman.common.utils

import androidx.compose.ui.graphics.Color
import com.example.postman.ui.theme.Blue
import com.example.postman.ui.theme.Brown
import com.example.postman.ui.theme.Green
import com.example.postman.ui.theme.Magenta
import com.example.postman.ui.theme.Purple
import com.example.postman.ui.theme.Red

enum class MethodName(val color: Color) {
    GET(Green),
    POST(Brown),
    PUT(Blue),
    PATCH(Purple),
    DELETE(Red),
    HEAD(Green),
    OPTIONS(Magenta)
}