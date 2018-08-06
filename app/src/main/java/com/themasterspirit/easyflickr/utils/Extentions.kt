package com.themasterspirit.easyflickr.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.themasterspirit.easyflickr.ui.FlickrApplication
import com.themasterspirit.flickr.data.models.FlickrPhoto

val Context.application: FlickrApplication
    get() = this.applicationContext as FlickrApplication

fun ViewGroup.inflate(layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

//enum class LoadingMethod {
//    ORIGINAL, REQUIRED, HARD, PROGRESSIVE
//}

fun ImageView.loadFlickrPhoto(
        photo: FlickrPhoto,
        expectedSize: FlickrPhoto.Companion.Size = FlickrPhoto.Companion.Size.DEFAULT,
        placeholder: Drawable? = null,
        callback: ((Bitmap?) -> Unit)? = null
) {
    val logger = context.application.logger
    val link: String = photo.link(expectedSize)
    logger.log("ImageView", "photo url = [$link]")
    Glide.with(this)
            .asBitmap()
            .load(link)
            .into(this)
}

// status bar height
val Context.statusBarHeightPx: Int
    get() {
        val resId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }

// navigation bar height
val Context.navigationBarHeightPx: Int
    get() {
        val resId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resId > 0) {
            resources.getDimensionPixelSize(resId)
        } else 0
    }

// action bar height
//val FragmentActivity.actionBarHeight: Int
//    get() {
//        theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).let {
//
//        }
//    }
//int actionBarHeight = 0;
//final TypedArray styledAttributes = getActivity().getTheme().obtainStyledAttributes(
//new int[] { android.R.attr.actionBarSize }
//);
//actionBarHeight = (int) styledAttributes.getDimension(0, 0);
//styledAttributes.recycle();


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