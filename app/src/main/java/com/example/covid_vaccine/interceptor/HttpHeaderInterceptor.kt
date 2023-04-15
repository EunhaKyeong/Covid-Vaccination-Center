package com.example.covid_vaccine.interceptor

import com.example.covid_vaccine.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

object HttpHeaderInterceptor: Interceptor {
    private const val AUTHORIZATION = "Authorization"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(AUTHORIZATION, "Infuser ${BuildConfig.VACCINATION_CENTER_API_KEY}")
            .build()

        return chain.proceed(request)
    }
}