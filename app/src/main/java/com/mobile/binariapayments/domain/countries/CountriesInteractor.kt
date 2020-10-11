package com.mobile.binariapayments.domain.countries

class CountriesInteractor(private val countriesRepository: CountriesRepository) {

    fun getCountries() = countriesRepository.getCountries()
}