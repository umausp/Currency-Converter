package com.usp.openexchangeratestask.data.repository

import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.strategy.DataFetchStrategy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


interface CurrencyFetchRepository {
    suspend fun fetchCurrencyList(): Flow<Result<List<CurrencyEntity>>>
}

class CurrencyFetchRepositoryImpl @Inject constructor(
    private val dataFetchStrategy: DataFetchStrategy<Result<List<CurrencyEntity>>>
) :
    CurrencyFetchRepository {
    override suspend fun fetchCurrencyList(): Flow<Result<List<CurrencyEntity>>> {
        return dataFetchStrategy.fetchData()
    }
}
