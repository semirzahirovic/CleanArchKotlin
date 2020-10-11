package com.mobile.binariapayments.data.rates

import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("api/latest.json")
    suspend fun getRates(@Query(value = "symbols") symbols: String): RatesResponse
}