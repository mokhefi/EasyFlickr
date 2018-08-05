package com.themasterspirit.easyflickr.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlickrAndroidViewModelFactory<in VM : ViewModel>(
        application: Application,
        private val provider: () -> VM
) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
        @Suppress("UNCHECKED_CAST")
        return provider() as VM
    }
}