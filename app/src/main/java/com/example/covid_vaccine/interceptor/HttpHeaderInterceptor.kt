package com.example.covid_vaccine.interceptor

import okhttp3.Interceptor
import okhttp3.Response

object HttpHeaderInterceptor: Interceptor {
    private const val AUTHORIZATION = "Authorization"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(AUTHORIZATION, "")
            .build()

        return chain.proceed(request)
    }
}