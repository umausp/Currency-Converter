package com.usp.openexchangeratestask.data.datasource

/**
 * A custom exception class to represent errors encountered during API fetching.
 */
class FetchResponseError(message: String) : Exception(message)
