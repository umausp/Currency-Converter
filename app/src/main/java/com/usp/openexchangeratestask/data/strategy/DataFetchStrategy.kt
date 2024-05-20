package com.usp.openexchangeratestask.data.strategy

import kotlinx.coroutines.flow.Flow

interface DataFetchStrategy<out T> {
    suspend fun fetchData(): Flow<T>
    suspend fun isDataFresh(): Boolean
}

