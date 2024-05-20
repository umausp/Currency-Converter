package com.usp.openexchangeratestask.di

import android.content.Context
import androidx.room.Room
import com.usp.openexchangeratestask.data.datasource.db.AppDatabase
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyDao
import com.usp.openexchangeratestask.data.model.ratelist.local.RateListDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDbModule {

    @Singleton
    @Provides
    fun provideCurrencyDao(
        appDatabase: AppDatabase
    ): CurrencyDao {
        return appDatabase.currencyDao()
    }

    @Singleton
    @Provides
    fun provideRatesDao(
        appDatabase: AppDatabase
    ): RateListDao {
        return appDatabase.ratesDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(
        context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
