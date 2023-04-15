package com.example.covid_vaccine.viewmodel

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_vaccine.repository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val centerRepository: CenterRepository): ViewModel() {
    private val _centerCnt: MutableLiveData<Int> = MutableLiveData(0)
    val centerCnt: LiveData<Int> get() = _centerCnt

    fun getCenters(page: Int) {
        viewModelScope.launch {
            centerRepository.getCentersFromApi(page).collect {
                it.onSuccess {centers ->
                    _centerCnt.value = _centerCnt.value?.plus(centers.size)
                    Log.d("SplashViewModel", "${_centerCnt.value}")
                }

                it.onFailure {e ->
                    when (e) {
                        is IOException -> Log.e("SplashViewModel", "[IOException] ${e.message}")
                        is HttpException -> Log.e("SplashViewModel", "[HttpException] code: ${e.response()?.code()}, body: ${e.response()?.body()}")
                        is SQLiteException -> Log.e("SplashViewModel", "[SQLiteException] ${e.message}}")
                        is Exception -> Log.e("SplashViewModel", "[Exception] ${e.message}")
                    }
                }

            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            centerRepository.deleteAll().collect() {
                it.onSuccess {
                    for (i in 1..10) {
                        getCenters(i)
                    }
                }

                it.onFailure { e ->
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