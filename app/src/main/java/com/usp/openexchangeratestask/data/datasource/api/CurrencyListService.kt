package com.usp.openexchangeratestask.data.datasource.api

import com.usp.openexchangeratestask.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface for a service that provides a list of currencies.
 */
interface CurrencyListService {

    companion object {
        private const val PRETTY_PRINT = "prettyprint"
        private const val SHOW_ALTERNATIVE = "show_alternative"
        private const val SHOW_INACTIVE = "show_inactive"
        private const val APP_ID = "app_id"
    }

    @GET("api/currencies.json")
    suspend fun getCurrencyList(
        @Query(APP_ID) appId: String = BuildConfig.API_KEY,
        @Query(PRETTY_PRINT) prettyPrint: Boolean = false,
        @Query(SHOW_ALTERNATIVE) showAlternative: Boolean = false,
        @Query(SHOW_INACTIVE) showInactive: Boolean = false
    ): Map<String, String>
}
