package com.usp.openexchangeratestask.data.model.currencylist.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencies")
    suspend fun getAll(): List<CurrencyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)
}

fun Currency.toEntity(): CurrencyEntity {
    return CurrencyEntity(
        code = code ?: "",
        name = name
    )
}
