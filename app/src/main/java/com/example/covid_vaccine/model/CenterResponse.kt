package com.example.covid_vaccine.model

data class CenterResponse(
    val currentCount: Int,
    val data: List<Center>,
    val matchCount: Int,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)