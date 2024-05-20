package com.usp.openexchangeratestask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usp.openexchangeratestask.data.model.currencylist.local.CurrencyEntity
import com.usp.openexchangeratestask.data.model.ratelist.local.RatesEntity
import com.usp.openexchangeratestask.data.repository.CurrencyFetchRepository
import com.usp.openexchangeratestask.data.repository.RateListRepository
import com.usp.openexchangeratestask.utils.convertAmount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val currencyRepository: CurrencyFetchRepository,
    private val rateFetchRepository: RateListRepository
) : ViewModel() {

    private var _currentStateFlow = MutableStateFlow<List<CurrencyEntity>>(emptyList())
    val currentStateFlow: StateFlow<List<CurrencyEntity>> = _currentStateFlow

    private var _currentRateListStateFlow = MutableStateFlow<List<RatesEntity>>(emptyList())
    val rateListStateFlow: StateFlow<List<RatesEntity>> = _currentRateListStateFlow

    private var _loaderStateFlow = MutableStateFlow(false)
    val loaderStateFlow: StateFlow<Boolean> = _loaderStateFlow

    fun fetchCurrencies() {
        viewModelScope.launch {
            currencyRepository.fetchCurrencyList()
                .collect {
                    if (it.isSuccess) {
                        _currentStateFlow.value = it.getOrNull() ?: emptyList()
                    } else {
                        _currentStateFlow.value = it.getOrNull() ?: emptyList()
                    }
                }
        }
    }

    fun fetchRates(base: String, amount: Double) {
        viewModelScope.launch {
            rateFetchRepository.fetchRateList(base, amount)
                .onStart {
                    _loaderStateFlow.value = true
                }.onCompletion {
                    _loaderStateFlow.value = false
                }
                .collect {
                    if (it.isSuccess) {
                        _currentRateListStateFlow.value =
                            it.getOrNull().convertAmount(base, amount) ?: emptyList()
                    } else {
                        _currentRateListStateFlow.value = it.getOrNull() ?: emptyList()
                    }
                }
        }
    }
}
