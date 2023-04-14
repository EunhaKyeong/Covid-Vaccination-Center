package com.example.covid_vaccine.di

import android.content.Context
import androidx.room.Room
import com.example.covid_vaccine.dao.CenterDao
import com.example.covid_vaccine.db.CovidVaccineDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DBModule {
    private const val DB_NAME = "COVID_VACCINE_DB"

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): CovidVaccineDB {
        return Room.databaseBuilder(
            context,
            CovidVaccineDB::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    fun provideCenterDao(database: CovidVaccineDB): CenterDao {
        return database.centerDao()
    }
}