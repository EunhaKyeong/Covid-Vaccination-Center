package com.example.covid_vaccine.datasource.remote

import com.example.covid_vaccine.datasource.BaseDataSource
import com.example.covid_vaccine.dto.CenterResponse
import com.example.covid_vaccine.service.CenterService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CenterDataSource @Inject constructor(private val service: CenterService): BaseDataSource() {
    fun getCenters(page: Int): Flow<Result<CenterResponse>> = handleResponse { service.getCenters(page) }
}