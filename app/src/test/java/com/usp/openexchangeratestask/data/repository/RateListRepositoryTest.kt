package com.usp.openexchangeratestask.data.repository

import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
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

class RateListRepositoryTest {
    @Mock
    private lateinit var rateListFetchStrategy: DataFetchStrategy<Result<List<RatesEntity>>>

    private lateinit var repository: RateListRepositoryImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        repository = RateListRepositoryImpl(rateListFetchStrategy)
    }

    @Test
    fun `fetchRateList() returns data from the rateListFetchStrategy`() = runBlocking {
        val successResult = Result.success(listOf(RatesEntity("USD", 1.0)))
        `when`(rateListFetchStrategy.fetchData()).thenReturn(flowOf(successResult))
        val result = repository.fetchRateList("USD", 100.0).single()
        assertEquals(successResult, result)
    }

    @Test
    fun `fetchRateList() returns failure from the rateListFetchStrategy`() = runBlocking {
        val failureResult = Result.failure<List<RatesEntity>>(Exception("Error fetching data"))
        `when`(rateListFetchStrategy.fetchData()).thenReturn(flowOf(failureResult))
        val result = repository.fetchRateList("USD", 100.0).single()
        assertEquals(failureResult, result)
    }
}