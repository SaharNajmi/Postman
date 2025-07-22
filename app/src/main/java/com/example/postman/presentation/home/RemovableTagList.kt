package com.example.postman.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.postman.ui.theme.LightGray
import com.example.postman.ui.theme.LightGreen

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
                text = "$key: $value",
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
