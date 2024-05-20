package com.usp.openexchangeratestask.di

import android.content.Context
import com.usp.openexchangeratestask.data.datasource.LocalDataSource
import com.usp.openexchangeratestask.data.datasource.RemoteDataSource
import com.usp.openexchangeratestask.data.datasource.api.CurrencyListService
import com.usp.openexchangeratestask.data.datasource.api.RateListService
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyLocalDataSource
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyRemoteDataSource
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListLocalDataSource
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListRemoteDataSource
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyDao
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
import com.usp.openexchangeratestask.data.model.ratelist.local.RateListDao
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateListResponse
import com.usp.openexchangeratestask.data.model.ratelist.remote.RateModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    fun bindDispatchers(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    fun bindCurrencyLocalDataSource(
        dao: CurrencyDao
    ): LocalDataSource<List<Currency>, List<CurrencyEntity>> {
        return CurrencyLocalDataSource(dao)
    }

    @Provides
    fun bindCurrencyRemoteDataSource(
        service: CurrencyListService,
        @ApplicationContext context: Context
    ): RemoteDataSource<List<Currency>> {
        return CurrencyRemoteDataSource(service, context)
    }

    @Provides
    fun bindRatesLocalDataSource(
        dao: RateListDao
    ): LocalDataSource<List<RateModel>, List<RatesEntity>> {
        return RateListLocalDataSource(dao)
    }

    @Provides
    fun bindRateRemoteDataSource(
        service: RateListService,
    ): RemoteDataSource<RateListResponse> {
        return RateListRemoteDataSource(service)
    }
}
