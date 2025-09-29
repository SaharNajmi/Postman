package com.example.postman

import com.example.postman.domain.model.History
import com.example.postman.presentation.history.searchEntries
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Test

class HistorySearchTest {
    @Test
    fun `return histories by searching url`() {
        val history = listOf(
            History(requestUrl = "url1"),
            History(requestUrl = "url2"),
            History(requestUrl = "url3"),
            History(requestUrl = "url3"),
            History(requestUrl = "url5"),
        )
        val historyRequests: Map<String, List<History>> = mapOf(
            "12 Aug" to history,
            "14 Aug" to history.toMutableList()
                .also { it.add(History(requestUrl = "request55")) }) //toMutableList() for new copy not the same reference
        val result = searchEntries(historyRequests, "5")
        result shouldBe mapOf(
            "12 Aug" to listOf(History(requestUrl = "url5")),
            "14 Aug" to listOf(History(requestUrl = "url5"), History(requestUrl = "request55"))
        )
    }

    @Test
    fun `return emptyList when url doesn't exist`() {
        val history = listOf(
            History(requestUrl = "url1"),
            History(requestUrl = "url2")
        )
        val historyRequests: Map<String, List<History>> = mapOf(
            "12 Aug" to history,
            "14 Aug" to history.toMutableList()
                .also { it.add(History(requestUrl = "url3")) })
        val result = searchEntries(historyRequests, "4")
        result.shouldBeEmpty()
    }
}