package com.example.postman

import com.example.postman.common.extensions.getHeaderValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ListExtensionTest : StringSpec({

    "give a pairs of strings and returns correct value for existing key" {
        val headers: List<Pair<String, String>> = listOf(
            "Authorization" to "bearer jksdfs",
            "Authorization" to "cgwbkscd",
            "Token" to "gusdh",
            "Content-Type" to "application/json",
        )
        headers.getHeaderValue("Authorization") shouldBe "bearer jksdfs"
        headers.getHeaderValue("Content-Type") shouldBe "application/json"
    }

    "return empty string when the key not founded" {
        val headers = listOf(
            Pair("A", "aaa"),
            Pair("B", "bbb"),
            Pair("C", "ccc")
        )
        headers.getHeaderValue("D") shouldBe ""
    }

    "is case-insensitive" {
        val headers = listOf(
            "Content-Type" to "application/json"
        )

        headers.getHeaderValue("CONTENT-type") shouldBe "application/json"
    }
})