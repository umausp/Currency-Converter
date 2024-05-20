package com.usp.openexchangeratestask.data.repository

import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.strategy.DataFetchStrategy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RateListRepository {
    suspend fun fetchRateList(base: String, amount: Double): Flow<Result<List<RatesEntity>>>
}

class RateListRepositoryImpl @Inject constructor(
    private val rateListFetchStrategy: DataFetchStrategy<Result<List<RatesEntity>>>
) : RateListRepository {
    override suspend fun fetchRateList(
        base: String,
        amount: Double
    ): Flow<Result<List<RatesEntity>>> {
        return rateListFetchStrategy.fetchData()
    }
}
