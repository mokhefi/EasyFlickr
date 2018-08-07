package com.themasterspirit.flickr.data.api.repositories

import android.annotation.SuppressLint
import android.database.Cursor
import com.themasterspirit.flickr.data.api.retrofit.FlickrService
import com.themasterspirit.flickr.data.db.FlickrDatabase
import com.themasterspirit.flickr.data.db.models.SearchParams
import com.themasterspirit.flickr.data.db.models.SearchParamsDao
import com.themasterspirit.flickr.data.models.FlickrPhoto
import com.themasterspirit.flickr.data.models.fromResponse
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class FlickrRepository(
        private val service: FlickrService,
        private val database: FlickrDatabase
) {

    private val searchDao: SearchParamsDao
        get() = database.searchParamsDao()

    fun search(text: String = ""): Single<List<FlickrPhoto>> {
        return service.search(text = text)
                .map { response ->
                    response.photos.photo
                            .map { it.fromResponse() }
//                            .filter { it.originalFormat != null }
                }
    }


    fun observeSearchSuggestions(): Flowable<List<SearchParams>> = searchDao.observe()

//    fun searchSuggestions(text: String): Single<List<SearchParams>> = searchDao.search(text)

    fun searchSuggestions(text: String): Cursor = searchDao.searchCursor(text)

    @SuppressLint("CheckResult")
    fun saveSearchQuerySilent(text: String) {
        if (text.isNotEmpty()) {
            Single.just(SearchParams(query = text, date = Date()))
                    .subscribeOn(Schedulers.computation())
                    .map { params: SearchParams -> searchDao.insert(params) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({}) { error -> error.printStackTrace() }
        }
    }
}