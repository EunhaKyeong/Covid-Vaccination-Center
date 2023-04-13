package com.example.covid_vaccine.di

import com.example.covid_vaccine.repository.CenterRepository
import com.example.covid_vaccine.repository.CenterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindCenterRepository(centerRepositoryImpl: CenterRepositoryImpl): CenterRepository
}