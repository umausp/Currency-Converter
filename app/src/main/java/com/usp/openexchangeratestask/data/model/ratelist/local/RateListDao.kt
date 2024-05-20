package com.usp.openexchangeratestask.data.model.ratelist.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RateListDao {
    @Query("SELECT * FROM rates")
    suspend fun getAll(): List<RatesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(rates: List<RatesEntity>)
}
