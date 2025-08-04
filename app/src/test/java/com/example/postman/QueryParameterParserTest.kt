package com.example.postman

import com.example.postman.common.extensions.buildUrlWithParams
import com.example.postman.common.extensions.mapKeyValuePairsToQueryParameter
import com.example.postman.common.extensions.mapStringToKeyValuePairs
import org.junit.Test

class QueryParameterParserTest {
    @Test
    fun stringToKeyValue() {
        val input = "https://api.escuelajs.co/api/v1/products?offset=1&limit=2"
        val output = input.mapStringToKeyValuePairs()
        val expected = listOf("offset" to "1", "limit" to "2")

        expected.forEachIndexed { i, pair ->
            assert(output[i] == pair, { "expectd $pair but got ${output[i]}" })
        }
    }

    @Test
    fun shouldReturnEmptyListGivenEmptyString() {
        val input = ""
        val output = input.mapStringToKeyValuePairs()
        assert(output.isEmpty())
    }

    @Test
    fun shouldReturnEmptyListGivenInvalidUrl() {
        val input = "s"
        val output = input.mapStringToKeyValuePairs()
        assert(output.isEmpty())
    }

    @Test
    fun shouldReturnPairsAfterGivenQuestionMark() {
        val input = "url?n"
        val outPut = input.mapStringToKeyValuePairs()
        val expected = listOf("n" to "")
        assert(outPut == expected)
    }

    @Test
    fun returnQueryParametersByGivenPairs() {
        val input = listOf("offset" to "3", "limit" to "4")
       // val input = listOf("offset" to "3", "limit" to "4")
        val outPut = input.mapKeyValuePairsToQueryParameter()
        val expected = "offset=3&limit=4"
        assert(outPut == expected)
    }

    @Test
    fun returnUrlIfThereIsNoQueryParameters() {
        val url = "url?"
        val params = ""
        val output = buildUrlWithParams(url, params)
        assert(output == url)
    }

//    @Test
//    fun returnUrlIfThereIsNoQueryParameters() {
//        val url = "url?1=3"
//        val params = ""
//        val output = buildUrlWithParams(url, params)
//        assert(output == url)
//    }
}