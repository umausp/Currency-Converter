package com.usp.openexchangeratestask.di

import android.content.Context
import com.usp.openexchangeratestask.data.ISharedPrefUtils
import com.usp.openexchangeratestask.data.SharedPrefUtils
import com.usp.openexchangeratestask.data.datasource.LocalDataSource
import com.usp.openexchangeratestask.data.datasource.RemoteDataSource
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyLocalDataSource
import com.usp.openexchangeratestask.data.datasource.currencylist.CurrencyRemoteDataSource
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListLocalDataSource
import com.usp.openexchangeratestask.data.datasource.ratelist.RateListRemoteDataSource
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.currencylist.remote.Currency
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.repository.CurrencyFetchRepository
import com.usp.openexchangeratestask.data.repository.CurrencyFetchRepositoryImpl
import com.usp.openexchangeratestask.data.repository.RateListRepository
import com.usp.openexchangeratestask.data.repository.RateListRepositoryImpl
import com.usp.openexchangeratestask.data.strategy.CurrencyFetchStrategy
import com.usp.openexchangeratestask.data.strategy.DataFetchStrategy
import com.usp.openexchangeratestask.data.strategy.RateListFetchStrategy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StrategyModule {

    @Provides
    @Singleton
    fun provideSharedPref(
        @ApplicationContext context: Context
    ): ISharedPrefUtils {
        return SharedPrefUtils(context)
    }

    @Provides
    @Singleton
    fun provideRepository(
        dataFetchStrategy: CurrencyFetchStrategy
    ): CurrencyFetchRepository {
        return CurrencyFetchRepositoryImpl(dataFetchStrategy)
    }

    @Provides
    fun provideStrategy(
        localDataSource: CurrencyLocalDataSource,
        remoteDataSource: CurrencyRemoteDataSource,
        sharedPrefUtils: ISharedPrefUtils
    ): DataFetchStrategy<Result<List<CurrencyEntity>>> {
        return CurrencyFetchStrategy(
            localDataSource,
            remoteDataSource,
            sharedPrefUtils
        )
    }

    @Provides
    @Singleton
    fun provideRateListRepository(
        dataFetchStrategy: RateListFetchStrategy
    ): RateListRepository {
        return RateListRepositoryImpl(dataFetchStrategy)
    }

    @Provides
    fun provideRateListStrategy(
        localDataSource: RateListLocalDataSource,
        remoteDataSource: RateListRemoteDataSource,
        sharedPrefUtils: ISharedPrefUtils
    ): DataFetchStrategy<Result<List<RatesEntity>>> {
        return RateListFetchStrategy(
            localDataSource,
            remoteDataSource,
            sharedPrefUtils
        )
    }
}
