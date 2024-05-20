package com.usp.openexchangeratestask.data.datasource
/**
 * Local data source interface for saving and retrieving data.
 */
interface LocalDataSource<in Entity, out DataModel> : DataSource<DataModel> {
    /**
     * Saves the given entity to local storage.
     *
     * @param entity The entity to save.
     */
    suspend fun saveData(entity: Entity)
}
