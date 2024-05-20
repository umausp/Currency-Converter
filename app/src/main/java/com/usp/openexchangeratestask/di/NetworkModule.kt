package com.usp.openexchangeratestask.di

import com.google.gson.Gson
import com.usp.openexchangeratestask.BuildConfig
import com.usp.openexchangeratestask.data.datasource.api.CurrencyListService
import com.usp.openexchangeratestask.data.datasource.api.RateListService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideCurrencyListService(
        gson: Gson
    ): CurrencyListService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(MapToCurrencyConverterFactory(gson))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyListService::class.java)
    }

    @Provides
    @Singleton
    fun provideRateListService(
        gson: Gson
    ): RateListService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(MapToCurrencyConverterFactory(gson))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RateListService::class.java)
    }
}
