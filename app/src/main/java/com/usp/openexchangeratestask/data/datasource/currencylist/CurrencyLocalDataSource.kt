package com.usp.openexchangeratestask.data.datasource.currencylist

import com.usp.openexchangeratestask.data.datasource.FetchResponseError
import com.usp.openexchangeratestask.data.datasource.LocalDataSource
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyDao
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.currencylist.local.toEntity
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Local data source for currencies.
 *
 * @param dao The DAO for accessing the currency database.
 * @param dispatcher The CoroutineDispatcher
 */
class CurrencyLocalDataSource @Inject constructor(
    private val dao: CurrencyDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource<List<Currency>, List<CurrencyEntity>> {
    override suspend fun saveData(data: List<Currency>) {
        withContext(dispatcher) {
            dao.insertAll(data.map { it.toEntity() })
        }
    }

    override suspend fun fetchData(): Result<List<CurrencyEntity>> {
        return runCatching {
            withContext(dispatcher) {
                dao.getAll()
            }
        }.fold(
            onSuccess = { Result.success(it) },

            onFailure = {
                Result.failure(FetchResponseError("Failed to fetch Data"))
            }
        )
    }
}
