package com.example.covid_vaccine.viewmodel

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_vaccine.entity.CenterEntity
import com.example.covid_vaccine.repository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val centerRepository: CenterRepository): ViewModel() {
    private val _centers: MutableLiveData<List<CenterEntity>> = MutableLiveData()
    val centers: LiveData<List<CenterEntity>> get() = _centers

    fun getCenters() {
        viewModelScope.launch {
            centerRepository.getCentersFromDB().collect {result ->
                result.onSuccess {
                    _centers.postValue(it)
                }

                result.onFailure {e ->
                    when (e) {
                        is IOException -> Log.e("SplashViewModel", "[IOException] ${e.message}")
                        is SQLiteException -> Log.e("SplashViewModel", "[SQLiteException] ${e.message}}")
                        is Exception -> Log.e("SplashViewModel", "[Exception] ${e.message}")
                    }
                }
            }
        }
    }
}