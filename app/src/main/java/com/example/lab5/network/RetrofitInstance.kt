package com.example.lab5.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openexchangerates.org/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: CurrencyApiService by lazy {
        retrofit.create(CurrencyApiService::class.java)
    }

}