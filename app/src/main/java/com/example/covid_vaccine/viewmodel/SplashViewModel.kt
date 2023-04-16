package com.example.covid_vaccine.viewmodel

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covid_vaccine.MyApplication.Companion.ERROR_500
import com.example.covid_vaccine.MyApplication.Companion.ERROR_NETWORK
import com.example.covid_vaccine.MyApplication.Companion.ERROR_SQLITE
import com.example.covid_vaccine.MyApplication.Companion.ERROR_UNKNOWN
import com.example.covid_vaccine.repository.CenterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val centerRepository: CenterRepository): ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _centerCnt: MutableLiveData<Int> = MutableLiveData(0)
    val centerCnt: LiveData<Int> get() = _centerCnt

    fun saveCenters(totalPage: Int) {
        viewModelScope.launch {
            centerRepository.getCentersFromApi(totalPage).collect {
                it.onSuccess { centers ->
                    _centerCnt.value = _centerCnt.value?.plus(centers.size)
                }

                it.onFailure { e ->
                    when (e) {
                        is IOException -> _error.postValue(ERROR_NETWORK)
                        is HttpException -> {
                            when(e.response()?.code()) {
                                401 -> _error.postValue("잘못된 인증정보 입니다. 개발사에 문의해 주세요.")
                                500 -> _error.postValue(ERROR_500)
                                else -> _error.postValue("코로나19 예방접종센터 제공 서비스에 문제가 발생했습니다. 개발사에 문의해 주세요.")
                            }
                        }
                        is SQLiteException -> _error.postValue(ERROR_SQLITE)
                        is Exception -> _error.postValue(ERROR_UNKNOWN)
                    }
                }
            }
        }
    }
}