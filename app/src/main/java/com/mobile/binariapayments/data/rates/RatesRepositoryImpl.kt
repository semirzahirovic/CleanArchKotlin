package com.mobile.binariapayments.data.rates

import com.mobile.binariapayments.domain.rates.RatesRepository
import kotlinx.coroutines.flow.flow

class RatesRepositoryImpl(private val ratesApi: RatesApi) :
    RatesRepository {
    override fun getRates(symbol: String) = flow { emit(ratesApi.getRates(symbol)) }
}