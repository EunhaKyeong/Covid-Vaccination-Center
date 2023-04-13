package com.example.covid_vaccine.dto

data class CenterResponse(
    val currentCount: Int,
    val data: List<CenterDTO>,
    val matchCount: Int,
    val page: Int,
    val perPage: Int,
    val totalCount: Int
)