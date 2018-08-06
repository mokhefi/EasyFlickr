package com.themasterspirit.easyflickr.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.themasterspirit.easyflickr.utils.*
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.models.FlickrPhoto
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class HomeViewModel(
        application: Application,
        private val repository: FlickrRepository
) : AndroidViewModel(application) {

    private val disposables = mutableListOf<Disposable>()

    private val kodein: Kodein by closestKodein(application)
    private val logger: FlickrLogger by kodein.instance()

    val recentPhotos = MutableLiveData<NetworkStatus<List<FlickrPhoto>>>()

    init {
        logger.log(TAG, "init(); hash = [${hashCode()}]")
    }

    fun search(text: String = "") {
        logger.log(TAG, "search(); text = [$text]")

        recentPhotos.value = Loading(true)

        disposables.add(repository.search(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { recentPhotos.value = Loading(false) }
                .doOnSuccess { recentPhotos.value = Loading(false) }
                .subscribe({ data: List<FlickrPhoto> ->
                    recentPhotos.value = Success(data)
                }) { error ->
                    recentPhotos.value = Failure(throwable = error)
                })
    }

    override fun onCleared() {
        super.onCleared()
        logger.log(TAG, "onCleared(); hash = [${hashCode()}]")
        disposables.forEach { if (!it.isDisposed) it.dispose() }

    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}