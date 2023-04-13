package com.example.covid_vaccine.di

import com.example.covid_vaccine.interceptor.HttpHeaderInterceptor
import com.example.covid_vaccine.service.CenterService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.odcloud.kr/api/"

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor(HttpHeaderInterceptor).build()

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .serializeSpecialFloatingPointValues()
            .setLenient()
            .create()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideCenterService(retrofit: Retrofit): CenterService = retrofit.create(CenterService::class.java)
}