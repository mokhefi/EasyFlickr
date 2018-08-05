package com.themasterspirit.easyflickr.utils

import android.util.Log

class FlickrLogger {

    fun log(tag: String, message: String?, throwable: Throwable? = null) {
        throwable?.let {
            Log.w("MY_LOG_$tag", "$message", it)
        } ?: run {
            Log.d("MY_LOG_$tag", "$message")
        }
    }
}