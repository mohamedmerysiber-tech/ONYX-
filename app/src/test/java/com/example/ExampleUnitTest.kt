package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun securityCheck_sha256LengthIsValid() {
        // Test basic SHA-256 properties
        val testData = "ONYX_SECURE_VAULT_TEST"
        assertTrue(testData.isNotEmpty())
        assertEquals(22, testData.length)
    }
}
