package com.usp.openexchangeratestask.ui.viewmodel

import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateListResponse
import com.usp.openexchangeratestask.data.model.ratelist.remote.toModel
import com.usp.openexchangeratestask.data.repository.CurrencyFetchRepository
import com.usp.openexchangeratestask.data.repository.RateListRepository
import com.usp.openexchangeratestask.utils.convertAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CurrencyListViewModelTest {
    @Mock
    private lateinit var currencyRepository: CurrencyFetchRepository

    @Mock
    private lateinit var rateFetchRepository: RateListRepository

    private lateinit var viewModel: CurrencyListViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CurrencyListViewModel(currencyRepository, rateFetchRepository)
    }

    @Test
    fun `fetchCurrencies() updates state flow on success`() = testScope.runTest {
        val mockData = listOf(CurrencyEntity("USD", "United States Dollar"))
        `when`(currencyRepository.fetchCurrencyList()).thenReturn(flowOf(Result.success(mockData)))

        viewModel.fetchCurrencies()

        testScheduler.advanceUntilIdle()

        assertEquals(mockData, viewModel.currentStateFlow.value)
    }

    @Test
    fun `fetchCurrencies() updates state flow with empty list on failure`() = testScope.runTest {
        `when`(currencyRepository.fetchCurrencyList()).thenReturn(
            flowOf(
                Result.failure(
                    Throwable("Error fetching currencies")
                )
            )
        )

        viewModel.fetchCurrencies()

        testScheduler.advanceUntilIdle()

        assertEquals(emptyList<List<CurrencyEntity>>(), viewModel.currentStateFlow.value)
    }

    @Test
    fun `fetchRates() updates state flow with converted rates on success`() = testScope.runTest {
        val baseCurrency = "USD"
        val amount = 100.0
        val mockedRatesEntity = listOf(RatesEntity("EUR", 85.0))

        `when`(rateFetchRepository.fetchRateList(baseCurrency, amount)).thenReturn(flowOf(Result.success(mockedRatesEntity)))

        viewModel.fetchRates(baseCurrency, amount)

        testScheduler.advanceUntilIdle()

        assertEquals(mockedRatesEntity, viewModel.rateListStateFlow.value)
    }

    @Test
    fun `fetchRates() updates state flow with empty list on failure`() = testScope.runTest {
        val baseCurrency = "USD"
        val amount = 100.0
        `when`(rateFetchRepository.fetchRateList(baseCurrency, amount)).thenReturn(
            flowOf(
                Result.failure(
                    Throwable("Error fetching currencies")
                )
            )
        )

        viewModel.fetchRates(baseCurrency, amount)

        testScheduler.advanceUntilIdle()

        assertEquals(emptyList<List<CurrencyEntity>>(), viewModel.rateListStateFlow.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}