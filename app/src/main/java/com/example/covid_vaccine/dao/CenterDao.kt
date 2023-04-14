package com.example.covid_vaccine.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.covid_vaccine.entity.CenterEntity

@Dao
interface CenterDao {
    @Insert
    suspend fun insertCenter(centers: List<CenterEntity>): List<Long>
}