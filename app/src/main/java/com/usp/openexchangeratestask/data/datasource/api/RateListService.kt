package com.usp.openexchangeratestask.data.datasource.api

import com.usp.openexchangeratestask.BuildConfig
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateListResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for interacting with the Rate List service.
 */
interface RateListService {
    companion object {
        private const val PRETTY_PRINT = "prettyprint"
        private const val SHOW_ALTERNATIVE = "show_alternative"
        private const val SHOW_INACTIVE = "show_inactive"
        private const val APP_ID = "app_id"
    }

    @GET("api/latest.json")
    suspend fun getRateListList(
        @Query(APP_ID) appId: String = BuildConfig.API_KEY,
        @Query(PRETTY_PRINT) prettyPrint: Boolean = false,
        @Query(SHOW_ALTERNATIVE) showAlternative: Boolean = false,
        @Query(SHOW_INACTIVE) showInactive: Boolean = false
    ): RateListResponse
}
