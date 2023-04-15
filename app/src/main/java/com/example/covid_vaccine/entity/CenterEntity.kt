package com.example.covid_vaccine.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "center")
data class CenterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lat: Double,
    val lng: Double,
    val address: String,
    val centerName: String,
    val centerType: String, //중앙/권역, 지역
    val facilityName: String,
    val phoneNumber: String,
    val updatedAt: String
)
