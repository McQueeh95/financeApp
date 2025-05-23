package com.example.lab5.network

import com.example.lab5.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiService {
    @GET("latest.json")
    suspend fun getRates(
        @Query("app_id") apiKey: String,
        @Query("base") base: String = "USD"
    ): CurrencyResponse
}