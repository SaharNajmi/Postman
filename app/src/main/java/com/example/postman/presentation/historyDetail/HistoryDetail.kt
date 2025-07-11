package com.example.postman.presentation.historyDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.postman.R

@Composable
fun HistoryDetail(navController: NavController, historyId: Int) {
    Column {
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = "back to histories",
            modifier = Modifier.padding(start = 12.dp, top = 36.dp).clickable {
                navController.popBackStack()
            })
        Text(
            text = "history id: $historyId ",
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
        )
    }
}