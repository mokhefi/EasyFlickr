package com.themasterspirit.easyflickr.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.themasterspirit.easyflickr.BuildConfig
import com.themasterspirit.easyflickr.utils.FlickrLogger
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.api.retrofit.FlickrInterceptor
import com.themasterspirit.flickr.data.api.retrofit.FlickrService
import com.themasterspirit.flickr.data.api.retrofit.RetrofitFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.*

val utilModule = Kodein.Module(name = "utils") {
    bind<FlickrLogger>() with eagerSingleton { FlickrLogger() }

    bind<Gson>() with singleton {
        GsonBuilder().create() // .setDateFormat(DateFormat.LONG)
    }
}

val apiModule = Kodein.Module(name = "api") {
    bind<FlickrService>() with provider {
        RetrofitFactory.createService<FlickrService>(
                gson = instance(),
                context = instance(),
                debug = BuildConfig.DEBUG,
                interceptors = *arrayOf(FlickrInterceptor(context = instance()))
        )
    }
}

val flickrDataModule: Kodein.Module = Kodein.Module(name = "flickr_data") {
    bind<FlickrRepository>() with provider {
        FlickrRepository(service = instance())
    }
}
