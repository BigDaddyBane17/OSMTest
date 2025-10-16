package com.example.core.result

sealed interface AppResult<out T> {
    data class Success<T>(val value: T): AppResult<T>
    data class Error(val error: AppError): AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.map(f: (T) -> R): AppResult<R> =
    when (this) {
        is AppResult.Success -> AppResult.Success(f(value))
        is AppResult.Error -> this
    }

inline fun <T, R> AppResult<T>.flatMap(f: (T) -> AppResult<R>): AppResult<R> =
    when (this) {
        is AppResult.Success -> f(value)
        is AppResult.Error -> this
    }

inline fun <T> AppResult<T>.fold(
    onSuccess: (T) -> Unit,
    onError: (AppError) -> Unit
) = when (this) {
    is AppResult.Success -> onSuccess(value)
    is AppResult.Error -> onError(error)
}