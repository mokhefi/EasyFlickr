package com.themasterspirit.easyflickr.ui

import android.app.Application
import android.content.Context
import com.themasterspirit.easyflickr.di.apiModule
import com.themasterspirit.easyflickr.di.flickrDataModule
import com.themasterspirit.easyflickr.di.utilModule
import com.themasterspirit.easyflickr.di.viewModelModule
import com.themasterspirit.easyflickr.utils.FlickrLogger
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class FlickrApplication : Application(), KodeinAware {

    override val kodeinContext: KodeinContext<*> = kcontext(this)

    override val kodein: Kodein = Kodein.lazy {
        val application: FlickrApplication = this@FlickrApplication

        bind<Context>() with instance(application as Context)
        bind<FlickrLogger>() with instance(FlickrLogger())

        import(androidXModule(application))
        import(utilModule)
        import(apiModule)
        import(flickrDataModule)


        import(viewModelModule) // todo move to activity/fragment scope
    }

    private val logger: FlickrLogger by instance()

    override fun onCreate() {
        super.onCreate()
        logger.log("app", "onCreate()")
    }
}