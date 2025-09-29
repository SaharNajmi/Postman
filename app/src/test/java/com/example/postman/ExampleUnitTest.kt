package com.example.postman

import io.kotest.matchers.shouldBe
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `reduce for numbers`() {
        val list = listOf(2, 3, 4)
        val result = list.reduceCustom { a, b -> a + b }
        result shouldBe 9
    }

    @Test
    fun `reduce for strings`() {
        val list = listOf("a", "b", "c")
        val result = list.reduceCustom { a, b -> a + b }
        result shouldBe "abc"
    }

    @Test
    fun `reduce indexed`() {
        val list = listOf(2, 3, 4,1)

        val result = list.reduceIndexedCustom { i, a, b -> i + a + b }
        result shouldBe 16
    }

    @Test
    fun `associateWith test`(){
        val nums = listOf(2, 4)
        val result = nums.associateWithCustom { it * it }
        val expected=mapOf(2 to 4, 4 to 16)
        result shouldBe expected
    }
}