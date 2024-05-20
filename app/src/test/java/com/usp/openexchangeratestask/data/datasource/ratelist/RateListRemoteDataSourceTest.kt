package com.usp.openexchangeratestask.data.datasource.ratelist

import com.usp.openexchangeratestask.data.datasource.FetchResponseError
import com.usp.openexchangeratestask.data.model.ratelist.local.RateListDao
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.model.ratelist.local.toEntity
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class RateListRemoteDataSourceTest {

    @Mock
    private lateinit var rateDao: RateListDao

    private lateinit var dataSource: RateListLocalDataSource

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        dataSource = RateListLocalDataSource(rateDao, Dispatchers.Unconfined)
    }

    @Test
    fun `saveData() inserts data into the DAO`() = runBlocking {
        val data = listOf(RateModel("USD", 1.0))
        dataSource.saveData(data)
        // Verify that the DAO's insertAll() method was called
        verify(rateDao).insertAll(data.map { it.toEntity() })
    }

    @Test
    fun `fetchData() returns success with data from the DAO`() = runBlocking {
        val entities = listOf(RatesEntity("USD", 1.0))
        `when`(rateDao.getAll()).thenReturn(entities)
        val result = dataSource.fetchData()
        assertEquals(Result.success(entities), result)
    }

    @Test
    fun `fetchData() returns failure when an exception occurs`() = runBlocking {
        val exception = RuntimeException("Error fetching data")
        `when`(rateDao.getAll()).thenThrow(exception)
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