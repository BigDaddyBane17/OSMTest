package com.example.core.result

sealed interface AppError {

    sealed interface Network: AppError {
        data object NoInternet: Network
        data object Timeout: Network
        data class Http(val code: Int, val body: String?): Network
    }

    sealed interface Data: AppError {
        data object Parse: Data
    }

    sealed interface Db: AppError {
        data object Read: Db
    }

    data class Unknown(val cause: Throwable?): AppError

}