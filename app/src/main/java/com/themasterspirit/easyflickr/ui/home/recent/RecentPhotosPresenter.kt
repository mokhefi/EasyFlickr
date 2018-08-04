package com.themasterspirit.easyflickr.ui.home.recent

import androidx.lifecycle.MutableLiveData
import com.themasterspirit.easyflickr.ui.BasePresenter
import com.themasterspirit.easyflickr.utils.Failure
import com.themasterspirit.easyflickr.utils.NetworkStatus
import com.themasterspirit.easyflickr.utils.Success
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.models.FlickrPhoto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecentPhotosPresenter(private val repository: FlickrRepository) : BasePresenter() {

    val recentPhotos = MutableLiveData<NetworkStatus<List<FlickrPhoto>>>()

    init {
        refreshPhotos()
    }

    fun refreshPhotos() {
        val disposable = repository.getRecent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data: List<FlickrPhoto> ->
                    recentPhotos.value = Success(data)
                }) { error ->
                    recentPhotos.value = Failure(throwable = error)
                }
    }
}