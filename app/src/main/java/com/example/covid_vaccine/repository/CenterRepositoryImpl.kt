package com.example.covid_vaccine.repository

import android.util.Log
import com.example.covid_vaccine.datasource.CenterDataSource
import com.example.covid_vaccine.domain.Center
import com.example.covid_vaccine.dto.CenterDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CenterRepositoryImpl @Inject constructor(private val centerDataSource: CenterDataSource): CenterRepository {
    override fun getCenters(page: Int): Flow<List<Center>> = flow{
        centerDataSource.getCenters(page).collect {
            Log.d("CenterRepositoryImpl", "getCenters: $it")
            emit(mapperToCenter(it.data))
        }

    }

    private fun mapperToCenter(centers: List<CenterDTO>): List<Center> {
        return centers.toList().map {
            Center(
                it.address,
                it.centerName,
                it.facilityName,
                it.phoneNumber,
                it.updatedAt
            )
        }
    }
}