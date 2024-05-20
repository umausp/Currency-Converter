package com.usp.openexchangeratestask.data.repository

import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.strategy.DataFetchStrategy
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class CurrencyFetchRepositoryTest {
    @Mock
    private lateinit var dataFetchStrategy: DataFetchStrategy<Result<List<CurrencyEntity>>>

    private lateinit var repository: CurrencyFetchRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = CurrencyFetchRepositoryImpl(dataFetchStrategy)
    }

    @Test
    fun `fetchCurrencyList() returns data from the dataFetchStrategy`() = runBlocking {
        val successResult = Result.success(listOf(CurrencyEntity("USD", "United States Dollar")))
        `when`(dataFetchStrategy.fetchData()).thenReturn(flowOf(successResult))
        val result = repository.fetchCurrencyList().single()
        assertEquals(successResult, result)
    }

    @Test
    fun `fetchCurrencyList() returns failure from the dataFetchStrategy`() = runBlocking {
        val failureResult = Result.failure<List<CurrencyEntity>>(Exception("Error fetching data"))
        `when`(dataFetchStrategy.fetchData()).thenReturn(flowOf(failureResult))
        val result = repository.fetchCurrencyList().single()
        assertEquals(failureResult, result)
    }
}