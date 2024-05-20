package com.usp.openexchangeratestask.data.datasource.currencylist

import com.usp.openexchangeratestask.data.datasource.FetchResponseError
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyDao
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.currencylist.local.toEntity
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class CurrencyLocalDataSourceTest {


    @Mock
    private lateinit var dao: CurrencyDao

    private lateinit var dataSource: CurrencyLocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dataSource = CurrencyLocalDataSource(dao, Dispatchers.Unconfined)
    }

    @Test
    fun `saveData() inserts data into the DAO`() = runBlocking {
        val data = listOf(Currency("USD", "United States Dollar"))
        dataSource.saveData(data)
        verify(dao).insertAll(data.map { it.toEntity() })
    }

    @Test
    fun `fetchData() returns success with data from the DAO`() = runBlocking {
        val entities = listOf(CurrencyEntity("USD", "United States Dollar"))
        `when`(dao.getAll()).thenReturn(entities)
        val result = dataSource.fetchData()
        assertEquals(Result.success(entities), result)
    }

    @Test
    fun `fetchData() returns failure when an exception occurs`() = runBlocking {
        `when`(dao.getAll()).thenThrow(RuntimeException("Failed to fetch Data"))
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
