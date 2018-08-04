package com.themasterspirit.easyflickr.ui

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.themasterspirit.easyflickr.BuildConfig
import com.themasterspirit.easyflickr.ui.home.recent.RecentPhotosPresenter
import com.themasterspirit.easyflickr.utils.FlickrLogger
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.api.retrofit.FlickrInterceptor
import com.themasterspirit.flickr.data.api.retrofit.FlickrService
import com.themasterspirit.flickr.data.api.retrofit.RetrofitFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class FlickrApplication : Application(), KodeinAware {

    override val kodeinContext: KodeinContext<*> = kcontext(this)

    override val kodein: Kodein = Kodein.lazy {
        val application: FlickrApplication = this@FlickrApplication

        bind<FlickrLogger>() with eagerSingleton { FlickrLogger() }

        bind<Context>() with provider { application as Context }

        bind<Gson>() with provider {
            GsonBuilder() // .setDateFormat()
                    .create()
        }

        bind<FlickrService>() with provider {
            RetrofitFactory.createService<FlickrService>(
                    gson = instance(),
                    context = instance(),
                    debug = BuildConfig.DEBUG,
                    interceptors = *arrayOf(FlickrInterceptor(application))
            )
        }

        bind<FlickrRepository>() with provider {
            FlickrRepository(service = instance())
        }

//        todo move to activity/fragment scope
        bind<RecentPhotosPresenter>() with provider {
            RecentPhotosPresenter(repository = instance())
        }

        import(androidXModule(application))

//        import(utilModule)
//        import(apiModule)
//        import(flickrDataModule)
    }

    private val logger: FlickrLogger by instance()

    override fun onCreate() {
        super.onCreate()
        logger.log("app", "onCreate()")
    }
}