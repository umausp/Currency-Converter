package com.usp.openexchangeratestask.data.datasource.currencylist

import android.content.Context
import com.usp.openexchangeratestask.data.datasource.FetchResponseError
import com.usp.openexchangeratestask.data.datasource.api.CurrencyListService
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CurrencyRemoteDataSourceTest {

    @Mock
    private lateinit var service: CurrencyListService

    @Mock
    private lateinit var context: Context

    private lateinit var dataSource: CurrencyRemoteDataSource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dataSource = CurrencyRemoteDataSource(service, context, Dispatchers.Unconfined)
    }

    @Test
    fun `fetchData() returns success with data from the API`() = runBlocking {
        val response = mapOf("USD" to "United States Dollar", "EUR" to "Euro")
        `when`(service.getCurrencyList("8a487b9cf1a64d6a8dbd412c4e424d38")).thenReturn(response)
        val result = dataSource.fetchData()
        assertEquals(
            Result.success(
                listOf(
                    Currency("USD", "United States Dollar"),
                    Currency("EUR", "Euro")
                )
            ), result
        )
    }

    @Test
    fun `fetchData() returns failure when an exception occurs`() = runBlocking {
        `when`(service.getCurrencyList("8a487b9cf1a64d6a8dbd412c4e424d38")).thenThrow(
            RuntimeException("Failed to fetch Data")
        )
        val result = dataSource.fetchData()
        result.fold(
            onSuccess = { assert(false) },
            onFailure = {
                assert(it is FetchResponseError)
                assertEquals("Failed to fetch Data", it.message)
            }
        )
    }
}