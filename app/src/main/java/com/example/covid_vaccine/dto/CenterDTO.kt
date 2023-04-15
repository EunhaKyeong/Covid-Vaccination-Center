package com.example.covid_vaccine.dto

data class CenterDTO(
    val address: String,
    val centerName: String,
    val centerType: String, //중앙/권역, 지역
    val createdAt: String,
    val facilityName: String,
    val id: Int,
    val lat: String,
    val lng: String,
    val org: String,
    val phoneNumber: String,
    val sido: String,
    val sigungu: String,
    val updatedAt: String,
    val zipCode: String
)