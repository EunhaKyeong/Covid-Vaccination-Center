package com.example.covid_vaccine.repository

import com.example.covid_vaccine.domain.Center
import kotlinx.coroutines.flow.Flow

interface CenterRepository {
    fun getCenters(page: Int): Flow<List<Center>>
}