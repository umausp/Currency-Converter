package com.usp.openexchangeratestask.data.datasource.api

import com.usp.openexchangeratestask.data.model.ratelist.remote.RateListResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RateListServiceTest {

    @Test
    fun `getRateListList() returns a RateListResponse object`() = runBlocking {
        val service = object : RateListService {
            override suspend fun getRateListList(
                appId: String,
                prettyPrint: Boolean,
                showAlternative: Boolean,
                showInactive: Boolean
            ): RateListResponse {
                return RateListResponse(
                    disclaimer = "disclaimer",
                    timestamp = 1678901234,
                    base = "USD",
                    license = "license",
                    rates = mapOf("EUR" to 0.85, "GBP" to 0.75)
                )
            }
        }

        val result = service.getRateListList("my_app_id")

        assertEquals(true, result.rates.isNotEmpty())
        assertEquals(1678901234, result.timestamp)
        assertEquals("USD", result.base)
        assertEquals(2, result.rates.size)
        assertEquals(0.85, result.rates["EUR"])
        assertEquals(0.75, result.rates["GBP"])
    }
}