package com.usp.openexchangeratestask.data.datasource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyDao
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.ratelist.local.RateListDao
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity

@Database(entities = [CurrencyEntity::class, RatesEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun ratesDao(): RateListDao
}
