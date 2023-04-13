package com.example.covid_vaccine.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_vaccine.repository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val centerRepository: CenterRepository): ViewModel() {
    fun getCenters(page: Int) {
        viewModelScope.launch {
            centerRepository.getCenters(page).collect {
                Log.d("SplashViewModel", "getCenters: $it")
            }
        }
    }
}