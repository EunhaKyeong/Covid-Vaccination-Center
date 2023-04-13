package com.example.covid_vaccine.di

import com.example.covid_vaccine.datasource.CenterDataSource
import com.example.covid_vaccine.service.CenterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideCenterDatasource (centerService: CenterService): CenterDataSource = CenterDataSource(centerService)
}