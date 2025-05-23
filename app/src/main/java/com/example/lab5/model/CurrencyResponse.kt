package com.example.lab5.model

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)
