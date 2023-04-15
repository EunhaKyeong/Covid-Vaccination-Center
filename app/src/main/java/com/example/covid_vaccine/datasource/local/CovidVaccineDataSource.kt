package com.example.covid_vaccine.datasource.local

import com.example.covid_vaccine.dao.CenterDao
import com.example.covid_vaccine.datasource.BaseDataSource
import com.example.covid_vaccine.entity.CenterEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CovidVaccineDataSource @Inject constructor(private val centerDao: CenterDao): BaseDataSource() {
    fun addCenters(centerEntities: List<CenterEntity>): Flow<Result<List<Long>>> = handleResponse {
        centerDao.insertCenter(centerEntities)
    }

    fun getCenters(): Flow<Result<List<CenterEntity>>> = handleResponse {
        centerDao.getAll()
    }

    fun deleteAll(): Flow<Result<Unit>> = handleResponse {
        centerDao.deleteAllUsers()
    }
}