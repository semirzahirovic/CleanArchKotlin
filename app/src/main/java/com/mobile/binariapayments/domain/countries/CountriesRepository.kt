package com.mobile.binariapayments.domain.countries

import com.mobile.binariapayments.data.countries.Country
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {
    fun getCountries(): Flow<Array<Country>>
}