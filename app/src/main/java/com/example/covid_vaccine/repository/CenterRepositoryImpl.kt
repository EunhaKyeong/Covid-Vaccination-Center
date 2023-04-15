package com.example.covid_vaccine.repository

import com.example.covid_vaccine.datasource.local.CovidVaccineDataSource
import com.example.covid_vaccine.datasource.remote.CenterDataSource
import com.example.covid_vaccine.dto.CenterDTO
import com.example.covid_vaccine.entity.CenterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CenterRepositoryImpl @Inject constructor(private val centerDataSource: CenterDataSource, private val covidVaccineDataSource: CovidVaccineDataSource): CenterRepository {
    override fun getCentersFromApi(page: Int): Flow<Result<List<Long>>> = flow {
        centerDataSource.getCenters(page).collect {apiResult ->
            apiResult.onSuccess { response ->
                val centers: List<CenterEntity> = mapperToCenterEntities(response.data)
                covidVaccineDataSource.addCenters(centers).collect { dbResults ->
                    dbResults.onSuccess {
                        emit(Result.success(it))
                    }
                    dbResults.onFailure {e ->
                        emit(Result.failure(e))
                    }
                }
            }

            apiResult.onFailure {e ->
                emit(Result.failure(e))
            }
        }
    }

    override fun getCentersFromDB(): Flow<Result<List<CenterEntity>>> = flow {
        covidVaccineDataSource.getCenters().collect { result ->
            result.onSuccess {centerEntities ->
                emit(Result.success(centerEntities))
            }
            result.onFailure {e ->
                emit(Result.failure(e))
            }
        }
    }

    override fun deleteAll(): Flow<Result<Unit>> = flow {
        covidVaccineDataSource.deleteAll().collect() { result ->
            result.onSuccess {
                emit(Result.success(it))
            }
            result.onFailure { e ->
                emit(Result.failure(e))
            }
        }
    }

    private fun mapperToCenterEntities(centers: List<CenterDTO>): List<CenterEntity> {
        return centers.toList().map {
            CenterEntity(
                address = it.address,
                lat = it.lat.toDouble(),
                lng = it.lng.toDouble(),
                centerName = it.centerName,
                centerType = it.centerType,
                facilityName = it.facilityName,
                phoneNumber = it.phoneNumber,
                updatedAt = it.updatedAt
            )
        }
    }
}