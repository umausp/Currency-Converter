package com.usp.openexchangeratestask.data.datasource.currencylist

import android.content.Context
import com.usp.openexchangeratestask.BuildConfig
import com.usp.openexchangeratestask.data.datasource.FetchResponseError
import com.usp.openexchangeratestask.data.datasource.RemoteDataSource
import com.usp.openexchangeratestask.data.datasource.api.CurrencyListService
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A remote data source that fetches a list of currencies from a remote service.
 *
 * @param service The remote service
 */
class CurrencyRemoteDataSource @Inject constructor(
    private val service: CurrencyListService,
    @ApplicationContext val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    RemoteDataSource<List<Currency>> {

    /**
     * Fetches a list of currencies from the API.
     *
     * @return A Result object containing either a list of currencies or
     */
    override suspend fun fetchData(): Result<List<Currency>> {
        return runCatching {
            withContext(dispatcher) {
                val response = service.getCurrencyList()
                response.map { Currency(it.key, it.value) }
            }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(FetchResponseError("Failed to fetch Data")) }
        )
    }
}
