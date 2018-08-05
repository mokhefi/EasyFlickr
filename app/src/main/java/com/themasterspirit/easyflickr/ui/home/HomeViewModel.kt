package com.themasterspirit.easyflickr.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.themasterspirit.easyflickr.utils.Failure
import com.themasterspirit.easyflickr.utils.Loading
import com.themasterspirit.easyflickr.utils.NetworkStatus
import com.themasterspirit.easyflickr.utils.Success
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.models.FlickrPhoto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
        application: Application,
        private val repository: FlickrRepository
) : AndroidViewModel(application) {

    val recentPhotos = MutableLiveData<NetworkStatus<List<FlickrPhoto>>>()

    init {
        Log.d(TAG, "init(); hash = [${hashCode()}]")
    }

    fun refreshPhotos() {
        Log.d(TAG, "refreshPhotos() ")

        recentPhotos.value = Loading(true)
        val disposable = repository.getRecent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { recentPhotos.value = Loading(false) }
                .doOnSuccess { recentPhotos.value = Loading(false) }
                .subscribe({ data: List<FlickrPhoto> ->
                    recentPhotos.value = Success(data)
                }) { error ->
                    recentPhotos.value = Failure(throwable = error)
                }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared(); hash = [${hashCode()}]")
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}