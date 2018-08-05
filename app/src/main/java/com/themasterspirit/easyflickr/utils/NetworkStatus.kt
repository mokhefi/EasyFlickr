package com.themasterspirit.easyflickr.utils

sealed class NetworkStatus<out T : Any>

data class Success<out T : Any>(val result: T) : NetworkStatus<T>()
data class Failure(var error: String? = null, var throwable: Throwable? = null) : NetworkStatus<Nothing>()
data class Loading(var loading: Boolean) : NetworkStatus<Nothing>()