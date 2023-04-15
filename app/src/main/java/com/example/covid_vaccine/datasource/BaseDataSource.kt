package com.example.covid_vaccine.datasource

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

abstract class BaseDataSource {
    protected fun <T> handleResponse(task: suspend () -> T): Flow<Result<T>> = flow {
        try {
            emit(Result.success(task.invoke()))
        } catch (e: IOException) {
            emit(Result.failure(IOException(ERROR_NETWORK)))
        } catch (e: HttpException) {
            emit(Result.failure(HttpException(e.response())))
        } catch (e: SQLiteException) {
            emit(Result.failure(SQLiteException(e.message)))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(Exception(ERROR_UNKNOWN)))
        }
    }

    companion object {
        private const val ERROR_NETWORK = "네트워크를 확인해주세요."
        private const val ERROR_UNKNOWN = "알 수 없는 에러가 발생했습니다."
    }
}