package com.example.covid_vaccine.datasource.local

import com.example.covid_vaccine.dao.CenterDao
import com.example.covid_vaccine.datasource.BaseDataSource
import com.example.covid_vaccine.entity.CenterEntity
import javax.inject.Inject

class CovidVaccineDataSource @Inject constructor(private val centerDao: CenterDao): BaseDataSource() {
    suspend fun saveCenters(centerEntities: List<CenterEntity>): Result<List<Long>> = handleResponse {
        centerDao.insertCenter(centerEntities)
    }

    suspend fun getCenters(): Result<List<CenterEntity>> = handleResponse {
        centerDao.getAll()
    }

    suspend fun deleteAll(): Result<Unit> = handleResponse {
        centerDao.deleteAllUsers()
    }
}