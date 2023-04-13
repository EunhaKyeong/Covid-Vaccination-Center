package com.example.covid_vaccine.service

import com.example.covid_vaccine.dto.CenterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CenterService {
    @GET("15077586/v1/centers")
    suspend fun getCenters(@Query("page") page: Int): CenterResponse
}