package com.example.covid_vaccine.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_vaccine.repository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val centerRepository: CenterRepository): ViewModel() {
    fun getCenters(page: Int) {
        viewModelScope.launch {
            centerRepository.getCenters(page).collect {
                it.onSuccess {centers ->
                    Log.i("SplashViewModel", "getCenters: $centers")
                }

                it.onFailure {e ->
                    when (e) {
                        is IOException -> Log.e("SplashViewModel", "[IOException] ${e.message}")
                        is HttpException -> Log.e("SplashViewModel", "[HttpException] code: ${e.response()?.code()}, body: ${e.response()?.body()}")
                        is Exception -> Log.e("SplashViewModel", "[Exception] ${e.message}")
                    }
                }

            }
        }
    }
}