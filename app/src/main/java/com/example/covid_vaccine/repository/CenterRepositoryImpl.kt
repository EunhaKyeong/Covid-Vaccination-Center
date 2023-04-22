package com.example.covid_vaccine.repository

import com.example.covid_vaccine.datasource.local.CovidVaccineDataSource
import com.example.covid_vaccine.datasource.remote.CenterDataSource
import com.example.covid_vaccine.dto.CenterDTO
import com.example.covid_vaccine.entity.CenterEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CenterRepositoryImpl @Inject constructor(private val centerDataSource: CenterDataSource, private val covidVaccineDataSource: CovidVaccineDataSource): CenterRepository {
    override fun getCentersFromApi(totalPage: Int): Flow<Result<List<Long>>> = flow {
        // Step 1: Room 데이터 삭제
        val deleteAllResult = covidVaccineDataSource.deleteAll()

        deleteAllResult.onSuccess {
            for (page in 1..totalPage) {
                val getCentersResult = centerDataSource.getCenters(page)

                getCentersResult.onSuccess {
                    // Step 3: 가져온 데이터를 Room에 저장
                    val centers: List<CenterEntity> = mapperToCenterEntities(getCentersResult.getOrThrow().data)
                    val saveCentersResult = covidVaccineDataSource.saveCenters(centers)

                    saveCentersResult.onSuccess {
                        emit(Result.success(it))
                    }

                    saveCentersResult.onFailure {
                        emit(Result.failure(it))
                    }
                }

                getCentersResult.onFailure {
                    emit(Result.failure(it))
                }
            }
        }

        deleteAllResult.onFailure {
            emit(Result.failure(it))
        }
    }

    override fun getCentersFromDB(): Flow<Result<List<CenterEntity>>> = flow {
        val getCentersResult = covidVaccineDataSource.getCenters()

        getCentersResult.onSuccess {
            emit(Result.success(getCentersResult.getOrThrow()))
        }

        getCentersResult.onFailure {
            emit(Result.failure(it))
        }
    }

    private fun mapperToCenterEntities(centers: List<CenterDTO>): List<CenterEntity> {
        return centers.map {
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