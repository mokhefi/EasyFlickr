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
import com.themasterspirit.flickr.data.db.FlickrDatabase
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.*

class FlickrApplication : Application(), KodeinAware {

    override val kodeinContext: KodeinContext<*> = kcontext(this)

    override val kodein: Kodein = Kodein.lazy {
        val application: FlickrApplication = this@FlickrApplication

        bind<Context>() with provider { application as Context }

        bind<FlickrDatabase>() with singleton {
//            Room.databaseBuilder(application, FlickrDatabase::class.java, "flickr.db").build()
            FlickrDatabase.create(application)
        }

        import(androidXModule(application))
        import(utilModule)
        import(apiModule)
        import(flickrDataModule)
    }

    val logger: FlickrLogger by instance()

    override fun onCreate() {
        super.onCreate()
        logger.log(TAG, "onCreate()")
        BigImageViewer.initialize(GlideImageLoader.with(application))
    }

    companion object {
        const val TAG = "FlickrApplication"
    }
}