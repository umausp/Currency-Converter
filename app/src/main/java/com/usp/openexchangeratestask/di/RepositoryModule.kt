package com.usp.openexchangeratestask.di

import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.repository.CurrencyFetchRepository
import com.usp.openexchangeratestask.data.repository.CurrencyFetchRepositoryImpl
import com.usp.openexchangeratestask.data.strategy.CurrencyFetchStrategy
import com.usp.openexchangeratestask.data.strategy.DataFetchStrategy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

//    @Provides
//    @Singleton
//    fun provideRepository(
//        dataFetchStrategy: CurrencyFetchStrategy
//    ): CurrencyFetchRepository {
//        return CurrencyFetchRepositoryImpl(dataFetchStrategy)
//    }
}
