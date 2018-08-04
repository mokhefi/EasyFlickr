package com.themasterspirit.easyflickr.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.themasterspirit.easyflickr.utils.FlickrLogger
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.instance

abstract class BaseActivity : AppCompatActivity(), KodeinAware {

    val parentKodein by closestKodein()

    override val kodein: Kodein by retainedKodein {
        extend(parentKodein, copy = Copy.All)
        /* activity specific bindings */
    }

    protected val logger: FlickrLogger by instance()

    protected val inflater: LayoutInflater by instance {
        LayoutInflater.from(this@BaseActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.log(tag = this::class.java.simpleName, message = "onCreate()")
    }
}