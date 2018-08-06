package com.themasterspirit.easyflickr.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.themasterspirit.easyflickr.ui.FlickrApplication

val Context.application: FlickrApplication
    get() = this.applicationContext as FlickrApplication

fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

//fun ImageView.picasso(url: String) {
//
//}

//inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
//        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
//        crossinline provider: () -> VM
//) = lazy(mode) {
//    object : ViewModelProvider.AndroidViewModelFactory(activity!!.application) {
//        @Suppress("UNCHECKED_CAST")
//        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = provider() as VM
//    }.create(VM::class.java)
//}
//
//inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
//        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
//        crossinline provider: () -> VM
//) = lazy(mode) {
//    object : ViewModelProvider.AndroidViewModelFactory(application) {
//        @Suppress("UNCHECKED_CAST")
//        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = provider() as VM
//    }.create(VM::class.java)
//}

//inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
//        mode: LazyThreadSafetyMode = LazyThreadSafetyMode.NONE,
//        crossinline provider: () -> VM) = lazy(mode) {
//    ViewModelProviders.of(this, object : ViewModelProvider.Factory {
//        override fun <T1 : ViewModel> create(aClass: Class<T1>) = provider() as T1
//    }).get(VM::class.java)
//}

//fun <T : ViewModel> FragmentActivity.createViewModel(clazz: Class<T>): T {
//    return ViewModelProvider
//            .AndroidViewModelFactory
//            .getInstance(application)
//            .create(clazz)
//}
//
//fun <T : ViewModel> Fragment.createViewModel(clazz: Class<T>): T {
//    return ViewModelProvider
//            .AndroidViewModelFactory
//            .getInstance(activity!!.application) // todo: maybe crash
//            .create(clazz)
//}