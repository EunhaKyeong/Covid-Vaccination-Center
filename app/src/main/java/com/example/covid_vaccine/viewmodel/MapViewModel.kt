package com.example.covid_vaccine.viewmodel

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_vaccine.MyApplication
import com.example.covid_vaccine.entity.CenterEntity
import com.example.covid_vaccine.repository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val centerRepository: CenterRepository): ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

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
                        is SQLiteException -> _error.postValue(MyApplication.ERROR_SQLITE)
                        is Exception -> _error.postValue(MyApplication.ERROR_UNKNOWN)
                    }
                }
            }
        }
    }
}