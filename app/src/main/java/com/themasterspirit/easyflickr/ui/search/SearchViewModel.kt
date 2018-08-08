package com.themasterspirit.easyflickr.ui.search

import android.app.Application
import android.database.Cursor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.themasterspirit.easyflickr.utils.*
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.models.FlickrPhoto
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.kodein.di.Kodein
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class SearchViewModel(
        application: Application,
        private val repository: FlickrRepository
) : AndroidViewModel(application) {

    private val disposables = mutableListOf<Disposable>()

    private val kodein: Kodein by closestKodein(application)
    private val logger: FlickrLogger by kodein.instance()

    val recentPhotos = MutableLiveData<NetworkStatus<List<FlickrPhoto>>>()
    val searchSuggestions = MutableLiveData<Cursor>()
    val deleteSuggestionsAction = SingleLiveEvent<Int>()

    init {
        logger.log(TAG, "init(); hash = [${hashCode()}]")
        updateSuggestions()
    }

    fun search(text: String = "") {
        logger.log(TAG, "search(); text = [$text]")

        recentPhotos.value = Loading(true)

        disposables.add(repository.search(text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { recentPhotos.value = Loading(false) }
                .doOnSuccess {
                    recentPhotos.value = Loading(false)
                    repository.saveSearchQuerySilent(text)
                }
                .subscribe({ data: List<FlickrPhoto> ->
                    recentPhotos.value = Success(data)
                }) { error ->
                    recentPhotos.value = Failure(throwable = error)
                })
    }

    fun updateSuggestions(text: String = "") {
        disposables.add(Single.just(text)
                .subscribeOn(Schedulers.computation())
                .map { query: String ->
                    if (query.isEmpty()) {
                        repository.getAll()
                    } else {
                        repository.searchSuggestions(query)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ cursor: Cursor ->
                    searchSuggestions.value = cursor
                    logger.log(TAG, "suggestion success, count = [${cursor.count}]")
                }) { error ->
                    error.printStackTrace()
                    logger.log(TAG, "suggestion error", error)
                })
    }

    fun deleteSuggestion(vararg text: String) {
        disposables.add(repository.deleteSuggestion(*text)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    deleteSuggestionsAction.value = it
                }) { error: Throwable -> error.printStackTrace() }
        )
    }

    override fun onCleared() {
        super.onCleared()
        logger.log(TAG, "onCleared(); hash = [${hashCode()}]")
        disposables.forEach { if (!it.isDisposed) it.dispose() }

    }

    companion object {
        const val TAG = "SearchViewModel"
    }
}