package com.example.postman

import com.example.postman.common.utils.HttpMethod
import com.example.postman.domain.model.ApiRequest
import com.example.postman.domain.model.History
import com.example.postman.domain.model.Request
import com.example.postman.domain.repository.ApiService
import com.example.postman.domain.repository.CollectionRepository
import com.example.postman.domain.repository.HistoryRepository
import com.example.postman.presentation.base.Loadable
import com.example.postman.presentation.home.HomeUiState
import com.example.postman.presentation.home.HomeViewModel
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    lateinit var viewModel: HomeViewModel
    lateinit var historyRepo: HistoryRepository
    lateinit var apiService: ApiService
    lateinit var collectionRepo: CollectionRepository

    @Before
    fun setup() {
        apiService = mockk<ApiService>()
        historyRepo = mockk<HistoryRepository>(relaxed = true)
        collectionRepo = mockk<CollectionRepository>(relaxed = true)

        viewModel = HomeViewModel(apiService, historyRepo, collectionRepo)
    }

    @Test
    fun `sendRequest should save response to history`() = runTest {
        viewModel.updateHttpMethod(HttpMethod.GET)
        viewModel.updateRequestUrl("http://example.com")
        viewModel.sendRequest()
        // advanceUntilIdle()
        coVerify(exactly = 1) { historyRepo.insertHistoryRequest(any()) }
    }

    @Test
    fun `sendRequest should updates collection if collectionId is provided`() = runTest {
        viewModel.updateHttpMethod(HttpMethod.GET)
        viewModel.updateRequestUrl("http://example.com")
        viewModel.sendRequest(collectionId = "1232")
        // advanceUntilIdle()
        coVerify(exactly = 1) {
            collectionRepo.updateCollectionRequest(
                "1232",
                any<Request>()
            )
        }
    }

    @Test
    fun `sendRequest should not update collection if collectionId is null`() {
        viewModel.updateHttpMethod(HttpMethod.GET)
        viewModel.updateRequestUrl("http://example.com")
        viewModel.sendRequest(collectionId = null)

        coVerify(exactly = 0) {
            collectionRepo.updateCollectionRequest(
                any<String>(),
                any<Request>()
            )
        }

    }

    @Test
    fun `sendRequest should not call repository when requestUrl is empty`() {
        viewModel.sendRequest()

        coVerify(exactly = 0) {
            apiService.sendRequest(any<String>(), any<String>())
            historyRepo.insertHistoryRequest(any<History>())
            collectionRepo.updateCollectionRequest(
                any<String>(),
                any<Request>()
            )
        }
    }

    @Test
    fun `sendRequest should call repository when requestUrl is not empty`() = runTest {
        viewModel.updateHttpMethod(HttpMethod.GET)
        viewModel.updateRequestUrl("http://example.com")
        viewModel.sendRequest()
        // advanceUntilIdle()
        coVerify(exactly = 1) {
            apiService.sendRequest(
                any<String>(),
                any<String>(),
                any<List<Pair<String, String>>>(),
                any<List<Pair<String, String>>>(),
                any<Any>(),
            )
        }
    }

    @Test
    fun `clearData should reset uiState`() {
        viewModel.clearData()
        val expected = HomeUiState(ApiRequest(), Loadable.Empty)
        viewModel.uiState.value.equals(expected)
    }

    @Test
    fun `updateRequestUrl should update requestUrl in uiState`() {
        viewModel.updateRequestUrl("newUrl")
        viewModel.uiState.value.data.requestUrl.shouldBe("newUrl")
    }

    @Test
    fun `updateHttpMethod should update httpMethod in uiState`() {
        viewModel.updateHttpMethod(HttpMethod.PUT)
        viewModel.uiState.value.data.httpMethod.shouldBe(HttpMethod.PUT)
    }
}