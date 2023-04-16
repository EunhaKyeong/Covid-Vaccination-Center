package com.example.covid_vaccine

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {
    companion object {
        const val ERROR_NETWORK = "네트워크를 확인해주세요."
        const val ERROR_SQLITE = "내부 데이터 처리 중 문제가 발생했습니다."
        const val ERROR_UNKNOWN = "알 수 없는 에러가 발생했습니다."
        const val ERROR_500 = "API 서버에 문제가 발생했습니다. 개발사에 문의해 주세요."
    }

    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client = NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_MAP_CLIENT_ID)
    }
}