package com.usp.openexchangeratestask.data.datasource.ratelist

import com.usp.openexchangeratestask.data.datasource.FetchResponseError
import com.usp.openexchangeratestask.data.datasource.LocalDataSource
import com.usp.openexchangeratestask.data.model.ratelist.local.RateListDao
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.model.ratelist.local.toEntity
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A local data source for storing and retrieving rate lists.
 *
 * @param rateDao The DAO for accessing the rate list
 */
class RateListLocalDataSource @Inject constructor(
    private val rateDao: RateListDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource<List<RateModel>, List<RatesEntity>> {
    override suspend fun saveData(entity: List<RateModel>) {
        withContext(dispatcher) {
            rateDao.insertAll(entity.map { it.toEntity() })
        }
    }

    /**
     * Fetches data from the database.
     *
     * @return A Result object containing either a list of RatesEntity objects or an
     */
    override suspend fun fetchData(): Result<List<RatesEntity>> {
        return runCatching {
            withContext(dispatcher) {
                rateDao.getAll()
            }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(FetchResponseError("Failed to fetch Data")) }
        )
    }
}

