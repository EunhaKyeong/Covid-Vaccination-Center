package com.example.covid_vaccine.di

import com.example.covid_vaccine.datasource.local.CovidVaccineDataSource
import com.example.covid_vaccine.datasource.remote.CenterDataSource
import com.example.covid_vaccine.repository.CenterRepository
import com.example.covid_vaccine.repository.CenterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideCenterRepository(
        centerDataSource: CenterDataSource,
        covidVaccineDataSource: CovidVaccineDataSource
    ): CenterRepository {
        return CenterRepositoryImpl(centerDataSource, covidVaccineDataSource)
    }
}