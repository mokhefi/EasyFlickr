package com.themasterspirit.easyflickr.ui

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.themasterspirit.easyflickr.R
import com.themasterspirit.easyflickr.utils.FlickrLogger
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

abstract class BaseFragment : Fragment(), KodeinAware {

//    override val kodeinContext = kcontext(activity)

    override val kodein by closestKodein()

    protected val logger: FlickrLogger by instance()

    // todo: refactor
    private val progressDialog: ProgressDialog by lazy {
        ProgressDialog(context).apply {
            isIndeterminate = true
            setMessage(getString(R.string.message_loading))
        }
    }

    protected fun progress(visibility: Boolean) {
        if (visibility) {
            if (!progressDialog.isShowing) {
                progressDialog.show()
            }
        } else {
            progressDialog.dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.log(tag = this::class.java.simpleName, message = "onCreate()")
    }
}