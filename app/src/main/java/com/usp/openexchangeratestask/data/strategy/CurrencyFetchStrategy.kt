package com.usp.openexchangeratestask.data.strategy

import com.usp.openexchangeratestask.data.ISharedPrefUtils
import com.usp.openexchangeratestask.data.datasource.LocalDataSource
import com.usp.openexchangeratestask.data.datasource.RemoteDataSource
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyLocalDataSource
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyRemoteDataSource
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.model.ratelist.remote.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyFetchStrategy @Inject constructor(
    private val localDataSource: CurrencyLocalDataSource,
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val sharedPrefUtils: ISharedPrefUtils
) : DataFetchStrategy<Result<List<CurrencyEntity>>> {

    override suspend fun isDataFresh(): Boolean {
        return withContext(Dispatchers.Default) {
            val currentTime = System.currentTimeMillis()
            val lastFetchTime = sharedPrefUtils.getSavedTimestamp()
            currentTime - lastFetchTime > REFRESH_INTERVAL
        }
    }

    override suspend fun fetchData(): Flow<Result<List<CurrencyEntity>>> {
        return flow {
            when {
                !isDataFresh() -> {
                    val localData = localDataSource.fetchData()
                    if (localData.getOrNull() != null && localData.getOrNull()!!.isNotEmpty()) {
                        emit(Result.success(localData.getOrNull()!!))
                    } else {
                        getRemoteData()
                    }
                }

                else -> {
                    getRemoteData()
                }
            }
        }
    }

    private suspend fun FlowCollector<Result<List<CurrencyEntity>>>.getRemoteData() {
        val remoteData = remoteDataSource.fetchData()
        if (remoteData.isSuccess) {
            if (remoteData.getOrNull() != null) {
                localDataSource.saveData(remoteData.getOrNull()!!)
                sharedPrefUtils.saveCurrentTimestamp()
                val localData = localDataSource.fetchData()
                emit(Result.success(localData.getOrNull() ?: emptyList()))
            } else {
                emit(Result.failure(Throwable("Remote data is null")))
            }
        } else {
            emit(Result.failure(Throwable("Failed to fetch remote data")))
        }
    }

    companion object {
        private const val REFRESH_INTERVAL = 30 * 60 * 1000
    }
}
