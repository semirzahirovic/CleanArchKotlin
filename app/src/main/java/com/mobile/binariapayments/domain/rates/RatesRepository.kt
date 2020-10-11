package com.mobile.binariapayments.domain.rates

import com.mobile.binariapayments.data.rates.RatesResponse
import kotlinx.coroutines.flow.Flow

interface RatesRepository {

    fun getRates(symbol: String): Flow<RatesResponse>
}