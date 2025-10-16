package com.example.core.result

import retrofit2.HttpException
import com.google.gson.JsonParseException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import android.database.SQLException

fun Throwable.toAppError(): AppError = when (this) {
    is UnknownHostException -> AppError.Network.NoInternet
    is SocketTimeoutException -> AppError.Network.Timeout
    is HttpException -> AppError.Network.Http(code(), response()?.errorBody()?.string())
    is JsonParseException -> AppError.Data.Parse
    is SQLException -> AppError.Db.Read
    else -> AppError.Unknown(this)
}

inline fun <T> runCatchingApp(block: () -> T): AppResult<T> =
    try { AppResult.Success(block()) }
    catch (t: Throwable) { AppResult.Error(t.toAppError()) }