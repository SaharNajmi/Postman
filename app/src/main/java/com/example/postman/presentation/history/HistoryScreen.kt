package com.example.postman.presentation.history

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.postman.R
import com.example.postman.presentation.utils.formatDate
import com.example.postman.ui.theme.Gray
import com.example.postman.ui.theme.LightGray

@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel) {
    LaunchedEffect(Unit) {
        viewModel.getAllHistories()
    }
    val historyRequest = viewModel.historyRequestsModel.collectAsState()
    Column(modifier = Modifier.padding(12.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = "backToHome",
                modifier = Modifier
                    .clickable(onClick = {
                        navController.popBackStack()
                    }),
            )
            Text(text = "History", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(thickness = 1.dp)
        LazyColumn() {
            items(historyRequest.value.size) { index ->
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = historyRequest.value[index].methodOption.name,
                        color = historyRequest.value[index].methodOption.color,
                        modifier = Modifier.padding(end = 8.dp)
                    )

                    Text(
                        text = historyRequest.value[index].requestUrl.toString(),
                        fontSize = 12.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = formatDate(historyRequest.value[index].createdAt),
                        Modifier
                            .clip(shape = RoundedCornerShape(4.dp))
                            .background(
                                LightGray
                            )
                            .padding(horizontal = 2.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = "delete",
                        Modifier.padding(horizontal = 4.dp).clickable {

                        })
                }
            }
        }
    }
}