package com.example.covid_vaccine.datasource

import com.example.covid_vaccine.dto.CenterResponse
import com.example.covid_vaccine.service.CenterService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CenterDataSource @Inject constructor(private val service: CenterService) {
    fun getCenters(page: Int): Flow<CenterResponse> = flow {
        emit(service.getCenters(page))
    }
}