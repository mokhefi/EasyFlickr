package com.themasterspirit.flickr.data.api.retrofit

import android.content.Context
import com.themasterspirit.flickr.data.R
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class FlickrInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().let { request: Request ->
            return@let request.newBuilder().url(request.url().newBuilder()
                    .addQueryParameter("api_key", context.getString(R.string.api_key))
                    .addQueryParameter("format", context.getString(R.string.api_output_format))
                    .addQueryParameter("nojsoncallback", "1") // pretty json output
                    .build()
            ).build()
        }
        return chain.proceed(request)
    }


    companion object {
        const val FLICKR_API_KEY_NAME = "api_key"
        const val FLICKR_OUTPUT_FORMAT_NAME = "format"
        const val FLICKR_OUTPUT_FORMAT_JSON_NAME = "json"
        const val FLICKR_NO_JSON_CALLBACK_NAME = "nojsoncallback"
    }
}