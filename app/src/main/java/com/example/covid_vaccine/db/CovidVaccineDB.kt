package com.example.covid_vaccine.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.covid_vaccine.dao.CenterDao
import com.example.covid_vaccine.entity.CenterEntity

@Database(entities = [CenterEntity::class], version = 1)
abstract class CovidVaccineDB: RoomDatabase() {
    abstract fun centerDao(): CenterDao
}