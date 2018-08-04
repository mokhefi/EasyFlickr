package com.themasterspirit.easyflickr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

//    val parentKodein by closestKodein()

//    override val kodein: Kodein by retainedKodein {
//        extend(parentKodein, copy = Copy.All)
//        /* activity specific bindings */
//    }

//    protected val logger: FlickrLogger by instance()

//    protected val inflater: LayoutInflater by instance {
//        LayoutInflater.from(this@BaseActivity)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        logger.log(tag = this::class.java.simpleName, message = "onCreate()")
    }
}