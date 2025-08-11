package com.example.postman

import com.example.postman.common.extensions.buildUrlWithParams
import com.example.postman.common.extensions.mapKeyValuePairsToQueryParameter
import com.example.postman.common.extensions.mapStringToKeyValuePairs
import io.kotest.assertions.withClue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Test

class QueryParameterParserTest {
    @Test
    fun stringToKeyValue() {
        val input = "https://api.escuelajs.co/api/v1/products?offset=1&limit=2"
        val output = input.mapStringToKeyValuePairs()
        val expected = listOf("offset" to "1", "limit" to "2")

        expected.forEachIndexed { i, pair ->
            withClue("expected $pair but got ${output[i]}") {
                output[i] shouldBe pair
            }
        }
    }

    @Test
    fun shouldReturnEmptyListGivenEmptyString() {
        val input = ""
        val output = input.mapStringToKeyValuePairs()
        assert(output.isEmpty())
        output.shouldBeEmpty()
    }

    @Test
    fun shouldReturnEmptyListGivenInvalidUrl() {
        val input = "s"
        val output = input.mapStringToKeyValuePairs()
        output.shouldBeEmpty()
    }

    @Test
    fun shouldReturnPairsAfterGivenQuestionMark() {
        val input = "url?n"
        val outPut = input.mapStringToKeyValuePairs()
        val expected = listOf("n" to "")
        outPut shouldBe expected
    }

    @Test
    fun returnQueryParametersByGivenPairs() {
        val input = listOf("offset" to "3", "limit" to "4")
        // val input = listOf("offset" to "3", "limit" to "4")
        val outPut = input.mapKeyValuePairsToQueryParameter()
        val expected = "offset=3&limit=4"
        outPut shouldBe expected
    }

    @Test
    fun returnUrlIfThereIsNoQueryParameters() {
        val url = "url?"
        val params = ""
        val output = buildUrlWithParams(url, params)
        output shouldBe url
    }
}