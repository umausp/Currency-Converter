package com.usp.openexchangeratestask.data.strategy

import com.usp.openexchangeratestask.data.ISharedPrefUtils
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListLocalDataSource
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListRemoteDataSource
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateListResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class RateListFetchStrategyTest {

    @Mock
    private lateinit var localDataSource: RateListLocalDataSource

    @Mock
    private lateinit var remoteDataSource: RateListRemoteDataSource

    @Mock
    private lateinit var sharedPrefUtils: ISharedPrefUtils

    private lateinit var strategy: RateListFetchStrategy

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        strategy = RateListFetchStrategy(localDataSource, remoteDataSource, sharedPrefUtils)
    }

    @Test
    fun `fetchData() emits local data when fresh`() = runBlocking {
        val localData = listOf(RatesEntity("USD", 1.0))
        `when`(localDataSource.fetchData()).thenReturn(Result.success(localData))
        `when`(sharedPrefUtils.getSavedTimestamp()).thenReturn(System.currentTimeMillis())
        val result = strategy.fetchData().single()
        assertEquals(Result.success(localData), result)
    }

    @Test
    fun `fetchData() saves remote data and updates timestamp on success and return local data`() = runBlocking {
        val remoteData = RateListResponse(
            base = "USD",
            rates = mapOf("EUR" to 0.85),
            timestamp = 1678901234,
            license = "license",
            disclaimer = "disclaimer"
        )

        val localData = listOf(RatesEntity("EUR", 0.85))

        `when`(remoteDataSource.fetchData()).thenReturn(Result.success(remoteData))

        `when`(sharedPrefUtils.getSavedTimestamp()).thenReturn(0)

        `when`(localDataSource.fetchData()).thenReturn(Result.success(localData))

        val result = strategy.fetchData().single()

        assertEquals(Result.success(listOf(RatesEntity("EUR", 0.85))), result)
    }
}