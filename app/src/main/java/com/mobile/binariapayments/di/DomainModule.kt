package com.mobile.binariapayments.di

import com.mobile.binariapayments.domain.countries.CountriesInteractor
import com.mobile.binariapayments.domain.rates.RatesInteractor
import org.koin.dsl.module

val domainModule = module {
    single { RatesInteractor(get()) }
    single { CountriesInteractor(get()) }
}