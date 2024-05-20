package com.usp.openexchangeratestask.data.strategy

import com.usp.openexchangeratestask.data.ISharedPrefUtils
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyLocalDataSource
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyRemoteDataSource
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
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
class CurrencyFetchStrategyTest {

    companion object {
        private const val REFRESH_INTERVAL = 30 * 60 * 1000
    }

    @Mock
    private lateinit var localDataSource: CurrencyLocalDataSource

    @Mock
    private lateinit var remoteDataSource: CurrencyRemoteDataSource

    @Mock
    private lateinit var sharedPrefUtils: ISharedPrefUtils

    private lateinit var strategy: DataFetchStrategy<Result<List<CurrencyEntity>>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        strategy = CurrencyFetchStrategy(localDataSource, remoteDataSource, sharedPrefUtils)
    }

    @Test
    fun `isDataFresh() returns true when data is stale`() = runBlocking {
        val currentTime = System.currentTimeMillis()
        val lastFetchTime = currentTime - REFRESH_INTERVAL - 1000
        `when`(sharedPrefUtils.getSavedTimestamp()).thenReturn(lastFetchTime)
        val result = strategy.isDataFresh()
        assertEquals(true, result)
    }

    @Test
    fun `isDataFresh() returns false when data is fresh`() = runBlocking {
        val currentTime = System.currentTimeMillis()
        val lastFetchTime = currentTime - REFRESH_INTERVAL + 1000
        `when`(sharedPrefUtils.getSavedTimestamp()).thenReturn(lastFetchTime)
        val result = strategy.isDataFresh()
        assertEquals(false, result)
    }

    @Test
    fun `fetchData() emits local data when fresh`() = runBlocking {
        val localData = listOf(CurrencyEntity("USD", "United States Dollar"))
        `when`(localDataSource.fetchData()).thenReturn(Result.success(localData))
        `when`(sharedPrefUtils.getSavedTimestamp()).thenReturn(System.currentTimeMillis())
        val result = strategy.fetchData().single()
        assertEquals(Result.success(localData), result)
    }

    @Test
    fun `fetchData() emits remote data when not fresh`() = runBlocking {
        val remoteData = listOf( Currency(
            "USD", "United States Dollar"
        ))

        val localData = listOf(CurrencyEntity("USD", "United States Dollar"))

        `when`(remoteDataSource.fetchData()).thenReturn(Result.success(remoteData))

        `when`(localDataSource.fetchData()).thenReturn(Result.success(localData))

        `when`(sharedPrefUtils.getSavedTimestamp()).thenReturn(System.currentTimeMillis())

        val result = strategy.fetchData().single()
        assertEquals(Result.success(localData), result)
    }
}