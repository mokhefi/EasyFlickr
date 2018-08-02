package com.themasterspirit.flickr.data.api.retrofit

import android.content.Context
import com.google.gson.Gson
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    const val TAG = "RetrofitFactory"

    const val endpoint = "https://api.flickr.com/services/rest/"
    const val RETROFIT_TIMEOUT: Int = 30

    inline fun <reified T> createService(
            gson: Gson,
            debug: Boolean = false,
            context: Context? = null,
            endpoint: String = RetrofitFactory.endpoint,
            vararg interceptors: Interceptor = arrayOf()
    ): T {
        val client: OkHttpClient = OkHttpClient.Builder().let { builder ->
            interceptors.forEach { builder.addInterceptor(it) }

            if (debug) {
                builder.addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                context.let { builder.addInterceptor(ChuckInterceptor(it)) }
            }

            builder.connectTimeout(RETROFIT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            builder.build()
        }

        return Retrofit.Builder()
                .baseUrl(endpoint)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(T::class.java)
    }
}