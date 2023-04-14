package com.example.covid_vaccine.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CenterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val address: String,
    val centerName: String,
    val facilityName: String,
    val phoneNumber: String,
    val updatedAt: String
)
