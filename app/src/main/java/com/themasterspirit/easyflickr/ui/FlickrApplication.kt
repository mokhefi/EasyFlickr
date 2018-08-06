package com.themasterspirit.easyflickr.ui

import android.app.Application
import android.content.Context
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.themasterspirit.easyflickr.di.apiModule
import com.themasterspirit.easyflickr.di.flickrDataModule
import com.themasterspirit.easyflickr.di.utilModule
import com.themasterspirit.easyflickr.utils.FlickrLogger
import com.themasterspirit.easyflickr.utils.application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import org.kodein.di.generic.provider

class FlickrApplication : Application(), KodeinAware {

    override val kodeinContext: KodeinContext<*> = kcontext(this)

    override val kodein: Kodein = Kodein.lazy {
        val application: FlickrApplication = this@FlickrApplication

        bind<Context>() with provider { application as Context }

        import(androidXModule(application))
        import(utilModule)
        import(apiModule)
        import(flickrDataModule)
    }

    val logger: FlickrLogger by instance()

    override fun onCreate() {
        super.onCreate()
        logger.log("app", "onCreate()")
        BigImageViewer.initialize(GlideImageLoader.with(application))
    }
}