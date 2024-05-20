package com.usp.openexchangeratestask.utils

import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConversionUtilKtTest {

    @Test
    fun `convertAmount() returns null if list is null`() {
        val rates: List<RatesEntity>? = null
        val result = rates.convertAmount("USD", 100.0)
        assertEquals(null, result)
    }

    @Test
    fun `convertAmount()returns original list if base is USD`() {
        val rates = listOf(
            RatesEntity("EUR", 0.85),
            RatesEntity("GBP", 0.75)
        )
        val result = rates.convertAmount("USD", 100.0)
        assertEquals(rates, result)
    }

    @Test
    fun `convertAmount() handles USD with INR base rate`() {
        val rates = listOf(
            RatesEntity("USD", 1.0),
            RatesEntity("INR", 83.5)
        )
        val result = rates.convertAmount("INR", 100.0)
        assertEquals(
            listOf(
                RatesEntity("USD", 1.2),
                RatesEntity("INR", 100.0)
            ),
            result
        )
    }

    @Test
    fun `convertAmount() converts amounts correctly`() {
        val rates = listOf(
            RatesEntity("EUR", 0.85),
            RatesEntity("GBP", 0.75)
        )
        val result = rates.convertAmount("EUR", 100.0)
        assertEquals(
            listOf(
                RatesEntity("EUR", 100.0),
                RatesEntity("GBP", 88.24)
            ),
            result
        )
    }

    @Test
    fun `convertAmount() handles missing base rate`() {
        val rates = listOf(
            RatesEntity("EUR", 0.85),
            RatesEntity("GBP", 0.75)
        )
        val result = rates.convertAmount("AUD", 100.0)
        assertEquals(
            listOf(
                RatesEntity("EUR", 85.0),
                RatesEntity("GBP", 75.0)
            ),
            result
        )
    }

}