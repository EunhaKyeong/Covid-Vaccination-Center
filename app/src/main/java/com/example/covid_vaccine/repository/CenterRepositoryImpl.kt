package com.example.covid_vaccine.repository

import com.example.covid_vaccine.datasource.local.CovidVaccineDataSource
import com.example.covid_vaccine.datasource.remote.CenterDataSource
import com.example.covid_vaccine.dto.CenterDTO
import com.example.covid_vaccine.entity.CenterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CenterRepositoryImpl @Inject constructor(private val centerDataSource: CenterDataSource, private val covidVaccineDataSource: CovidVaccineDataSource): CenterRepository {
    override fun getCenters(page: Int): Flow<Result<List<Long>>> = flow {
        centerDataSource.getCenters(page).collect {
            it.onSuccess {response ->
                val centers: List<CenterEntity> = mapperToCenterEntities(response.data)
                covidVaccineDataSource.addCenters(centers).collect { results ->
                    emit(Result.success(results))
                }
            }

            it.onFailure {e ->
                emit(Result.failure(e))
            }
        }
    }

    private fun mapperToCenterEntities(centers: List<CenterDTO>): List<CenterEntity> {
        return centers.toList().map {
            CenterEntity(
                address = it.address,
                centerName = it.centerName,
                facilityName = it.facilityName,
                phoneNumber = it.phoneNumber,
                updatedAt = it.updatedAt
            )
        }
    }
}