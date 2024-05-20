package com.usp.openexchangeratestask.data.datasource.ratelist

import com.usp.openexchangeratestask.data.datasource.FetchResponseError
import com.usp.openexchangeratestask.data.datasource.RemoteDataSource
import com.usp.openexchangeratestask.data.datasource.api.RateListService
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateListResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * A remote data source that fetches rate list data from a server.
 *
 * @param rateService The service used to
 */
class RateListRemoteDataSource @Inject constructor(
    private val rateService: RateListService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource<RateListResponse> {

    override suspend fun fetchData(): Result<RateListResponse> {
        return runCatching {
            withContext(dispatcher) {
                rateService.getRateListList()
            }
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(FetchResponseError("Failed to fetch Data")) }
        )
    }
}
