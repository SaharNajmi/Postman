package com.example.postman.presentation.base

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.postman.R

@Composable
 fun CustomToolbar(title:String,navController: NavController) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.arrow_back),
            contentDescription = "backToHome",
            modifier = Modifier
                .clickable(onClick = {
                    navController.popBackStack()
                }),
        )
        Text(text = title, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
    }
    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider(thickness = 1.dp)
}