package com.example.covid_vaccine.datasource

import android.database.sqlite.SQLiteException
import android.util.Log
import retrofit2.HttpException
import java.io.IOException

abstract class BaseDataSource {
    protected suspend fun <T> handleResponse(task: suspend () -> T): Result<T> = try {
        Result.success(task.invoke())
    } catch (e: IOException) {
        Log.e("BaseDataSource", "[IOException] $e")
        Result.failure(IOException(ERROR_NETWORK))
    } catch (e: HttpException) {
        Log.e("BaseDataSource", "[HttpException] code: ${e.response()?.code()}, body: ${e.response()?.errorBody()?.string()}")
        Result.failure(HttpException(e.response()))
    } catch (e: SQLiteException) {
        Log.e("BaseDataSource", "[SQLiteException] $e")
        Result.failure(SQLiteException(ERROR_SQLITE))
    } catch (e: Exception) {
        Log.e("BaseDataSource", "[Exception] $e")
        Result.failure(Exception(ERROR_UNKNOWN))
    }

    companion object {
        private const val ERROR_NETWORK = "네트워크를 확인해주세요."
        private const val ERROR_SQLITE = "내부 데이터 처리 중 문제가 발생했습니다."
        private const val ERROR_UNKNOWN = "알 수 없는 에러가 발생했습니다."
    }
}