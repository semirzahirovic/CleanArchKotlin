package com.mobile.binariapayments.domain.rates

import com.mobile.binariapayments.domain.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

class RatesInteractor(private val ratesRepository: RatesRepository) {

    fun getRates(symbol: String) =
        ratesRepository.getRates(symbol).mapNotNull {
            Resource.success(
                it.rates[symbol]
            )
        }
            .catch { emit(
                Resource.error(
                    it.message.orEmpty()
                )
            ) }
            .flowOn(Dispatchers.IO)
}