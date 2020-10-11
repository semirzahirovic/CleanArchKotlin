package com.mobile.binariapayments.di

import com.mobile.binariapayments.data.countries.CountriesRepositoryImpl
import com.mobile.binariapayments.data.rates.RatesApi
import com.mobile.binariapayments.data.rates.RatesRepositoryImpl
import com.mobile.binariapayments.domain.countries.CountriesRepository
import com.mobile.binariapayments.domain.rates.RatesRepository
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val OPEN_RATES_RETROFIT = "OpenRatesRetrofit"
private const val BASE_URL = "https://openexchangerates.org/"
private const val APP_ID_QUERY_PARAM = "app_id"
private const val APP_ID = "fa9aa72aa16b46c3a8a8fadd5292c0a4"
val dataModule = module {
    single {
        OkHttpClient()
    }
    //inject singleton named OpenRatesRetrofit for usage with exchange rates API
    // we want to add interceptor so that in every request to exchange rates API we don't need to send
    // app_id param
    single(named(OPEN_RATES_RETROFIT)) {
        val client = get<OkHttpClient>().newBuilder().addInterceptor { chain ->
            var request: Request = chain.request()
            val url: HttpUrl =
                request.url().newBuilder().addQueryParameter(APP_ID_QUERY_PARAM, APP_ID).build()
            request = request.newBuilder().url(url).build()
            chain.proceed(request)
        }.build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    single {
        get<Retrofit>(named(OPEN_RATES_RETROFIT)).create(RatesApi::class.java)
    }

    single<RatesRepository> {
        RatesRepositoryImpl(get())
    }

    single<CountriesRepository> {
        CountriesRepositoryImpl()
    }
}