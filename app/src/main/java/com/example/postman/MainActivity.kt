package com.example.postman

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.postman.presentation.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavHost()
        }
    }
}


fun main() {
//    val list = mapOf<Int, Int>(3 to 6, 7 to 14)
//    for (each in list){
//        println(each.key+4)
//    }
//   println( list.map { it.key })
//   val newList= list.flatMap { listOf(3,5) }
//    println(newList)

    val initArray= Array<Int>(10){0}
    println(initArray.joinToString())
}