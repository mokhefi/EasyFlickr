package com.themasterspirit.easyflickr.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.themasterspirit.easyflickr.ui.home.recent.RecentPhotosViewModel
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.api.retrofit.FlickrService
import com.themasterspirit.flickr.data.api.retrofit.RetrofitFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.singleton

val utilModule = Kodein.Module(name = "utils") {
    bind<Gson>() with singleton {
        GsonBuilder() // .setDateFormat()
                .create()
    }
}

val apiModule = Kodein.Module(name = "api") {
    bind<FlickrService>() with factory { gson: Gson, context: Context ->
        RetrofitFactory.createService<FlickrService>(
                gson = gson,
                context = context
        )
    }
}

val flickrDataModule: Kodein.Module = Kodein.Module(name = "flickr_data") {
    bind<FlickrRepository>() with factory { service: FlickrService ->
        FlickrRepository(service)
    }
}

val viewModelModule: Kodein.Module = Kodein.Module(name = "view_model") {
    bind<RecentPhotosViewModel>() with factory { application: Application, repository: FlickrRepository ->
        RecentPhotosViewModel(application, repository)
    }
}