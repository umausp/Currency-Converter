package com.usp.openexchangeratestask.data.strategy

import com.usp.openexchangeratestask.data.ISharedPrefUtils
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListLocalDataSource
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListRemoteDataSource
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.model.ratelist.remote.toModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RateListFetchStrategy @Inject constructor(
    private val localDataSource: RateListLocalDataSource,
    private val remoteDataSource: RateListRemoteDataSource,
    private val sharedPrefUtils: ISharedPrefUtils
) : DataFetchStrategy<Result<List<RatesEntity>>> {

    override suspend fun fetchData(): Flow<Result<List<RatesEntity>>> {
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

    private suspend fun FlowCollector<Result<List<RatesEntity>>>.getRemoteData() {
        val remoteData = remoteDataSource.fetchData()
        if (remoteData.isSuccess) {
            if (remoteData.getOrNull() != null) {
                localDataSource.saveData(remoteData.getOrNull()!!.toModel())
                sharedPrefUtils.saveCurrentTimestamp(remoteData.getOrNull()!!.timestamp)
                val localData = localDataSource.fetchData()
                emit(Result.success(localData.getOrNull() ?: emptyList()))
            } else {
                emit(Result.failure(Throwable("Remote data is null")))
            }
        } else {
            emit(Result.failure(Throwable("Failed to fetch remote data")))
        }
    }

    override suspend fun isDataFresh(): Boolean {
        return withContext(Dispatchers.Default) {
            val currentTime = System.currentTimeMillis()
            val lastFetchTime = sharedPrefUtils.getSavedTimestamp()
            currentTime - lastFetchTime > REFRESH_INTERVAL
        }
    }

    companion object {
        private const val REFRESH_INTERVAL = 30 * 60 * 1000
    }
}