package com.example.covid_vaccine.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.covid_vaccine.entity.CenterEntity

@Dao
interface CenterDao {
    @Insert
    suspend fun insertCenter(centers: List<CenterEntity>): List<Long>

    @Query("SELECT * FROM center")
    suspend fun getAll(): List<CenterEntity>

    @Query("DELETE FROM center")
    suspend fun deleteAllUsers()
}