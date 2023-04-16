package com.example.covid_vaccine.repository

import com.example.covid_vaccine.entity.CenterEntity
import kotlinx.coroutines.flow.Flow

interface CenterRepository {
    fun getCentersFromApi(totalPage: Int): Flow<Result<List<Long>>>
    fun getCentersFromDB(): Flow<Result<List<CenterEntity>>>
}