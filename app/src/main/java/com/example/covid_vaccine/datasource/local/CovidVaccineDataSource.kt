package com.example.covid_vaccine.datasource.local

import com.example.covid_vaccine.dao.CenterDao
import com.example.covid_vaccine.entity.CenterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CovidVaccineDataSource @Inject constructor(private val centerDao: CenterDao) {
    fun addCenters(centerEntities: List<CenterEntity>): Flow<List<Long>> = flow {
        emit(centerDao.insertCenter(centerEntities))
    }
}