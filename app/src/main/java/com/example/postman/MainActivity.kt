package com.example.postman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.postman.data.ApiClient
import com.example.postman.data.ApiRepositoryImp
import com.example.postman.presentation.HomeScreen
import com.example.postman.presentation.HomeViewModel
import com.example.postman.ui.theme.PostmanTheme

class MainActivity : ComponentActivity() {
    val viewModel = HomeViewModel(ApiRepositoryImp(ApiClient.createApiService()))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostmanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding), viewModel
                    )
                }
            }
        }
    }
}