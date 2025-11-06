package com.example.postman

import com.example.postman.domain.repository.CollectionRepository
import com.example.postman.presentation.collection.CollectionViewModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    lateinit var viewModel: CollectionViewModel
    lateinit var collectionRepository: CollectionRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        collectionRepository = mockk<CollectionRepository>()
        viewModel = CollectionViewModel(collectionRepository, testDispatcher)
    }
    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}