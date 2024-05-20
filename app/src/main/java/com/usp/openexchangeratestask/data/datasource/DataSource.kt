package com.usp.openexchangeratestask.data.datasource

/**
 * A data source that provides access to data of any DataSource.
 */
interface DataSource<out T> {
    /**
     * Fetches data from the data source.
     *
     * @return a [Result] object containing either the data or an error.
     */
    suspend fun fetchData() : Result<T>
}
