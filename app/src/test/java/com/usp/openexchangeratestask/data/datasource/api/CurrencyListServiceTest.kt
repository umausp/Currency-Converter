package com.usp.openexchangeratestask.data.datasource.api

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyListServiceTest {

    @Test
    fun `getCurrencyList() returns a map of currency codes and names`() = runBlocking {
        val service = object : CurrencyListService {
            override suspend fun getCurrencyList(
                appId: String,
                prettyPrint: Boolean,
                showAlternative: Boolean,
                showInactive: Boolean
            ): Map<String, String> {
                return mapOf(
                    "USD" to "United States Dollar",
                    "EUR" to "Euro",
                    "GBP" to "British Pound"
                )
            }
        }

        val result = service.getCurrencyList("my_app_id")

        assertEquals(3, result.size)
        assertEquals("United States Dollar", result["USD"])
        assertEquals("Euro", result["EUR"])
        assertEquals("British Pound", result["GBP"])
    }
}