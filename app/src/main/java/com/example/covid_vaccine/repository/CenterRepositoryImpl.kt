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
        val deleteResult = covidVaccineDataSource.deleteAll()

        if (deleteResult.isSuccess) {
            for (page in 1..totalPage) {
                // Step 2: Retrofit을 통해 데이터 가져오기
                val getResult = centerDataSource.getCenters(page)

                if (getResult.isSuccess) {
                    // Step 3: 가져온 데이터를 Room에 저장
                    val centers: List<CenterEntity> = mapperToCenterEntities(getResult.getOrThrow().data)

                    val saveResult = covidVaccineDataSource.saveCenters(centers)
                    if (saveResult.isSuccess) {
                        emit(Result.success(saveResult.getOrThrow()))
                    } else {
                        emit(Result.failure(saveResult.exceptionOrNull()?: Throwable(ERROR_FAILURE_ERROR)))
                    }
                } else {
                    emit(Result.failure(getResult.exceptionOrNull()?: Throwable(ERROR_FAILURE_ERROR)))
                    break
                }
            }
        } else {
            emit(Result.failure(deleteResult.exceptionOrNull()?: Throwable(ERROR_FAILURE_ERROR)))
        }
    }

    override fun getCentersFromDB(): Flow<Result<List<CenterEntity>>> = flow {
        val result = covidVaccineDataSource.getCenters()

        if (result.isSuccess) {
            emit(Result.success(result.getOrThrow()))
        } else {
            emit(Result.failure(result.exceptionOrNull()?: Throwable(ERROR_FAILURE_ERROR)))
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

    companion object {
        private const val ERROR_FAILURE_ERROR = "데이터 처리 중 문제가 발생했습니다."
    }
}