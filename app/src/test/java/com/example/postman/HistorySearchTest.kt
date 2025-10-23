package com.example.postman

import com.example.postman.domain.model.History
import com.example.postman.domain.model.HistoryEntry
import com.example.postman.presentation.base.searchHistories
import io.kotest.matchers.collections.shouldBeEmpty
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
        val historyRequests: List<HistoryEntry> = listOf(HistoryEntry("12 Aug", history),
            HistoryEntry("14 Aug", history.toMutableList()
                .also { it.add(History(requestUrl = "request55")) }))
        val result = searchHistories(historyRequests, "5")
        result shouldBe listOf(
            HistoryEntry("12 Aug" , listOf(History(requestUrl = "url5"))),
            HistoryEntry("14 Aug" , listOf(History(requestUrl = "url5"), History(requestUrl = "request55")))
        )
    }

    @Test
    fun `return emptyList when url doesn't exist`() {
        val history = listOf(
            History(requestUrl = "url1"),
            History(requestUrl = "url2")
        )
        val historyRequests = listOf(
            HistoryEntry("12 Aug" , history),
            HistoryEntry("14 Aug" , history.toMutableList()
                .also { it.add(History(requestUrl = "url3")) }))
        val result = searchHistories(historyRequests, "4")
        result.shouldBeEmpty()
    }
}