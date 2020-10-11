package com.mobile.binariapayments.data.countries

import com.mobile.binariapayments.domain.countries.CountriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CountriesRepositoryImpl : CountriesRepository {
    override fun getCountries(): Flow<Array<Country>> = flowOf(Country.values())
}