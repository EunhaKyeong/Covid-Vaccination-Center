package com.example.covid_vaccine.repository

import kotlinx.coroutines.flow.Flow

interface CenterRepository {
    fun getCenters(page: Int): Flow<Result<List<Long>>>
}