package com.themasterspirit.flickr.data.api.repositories

import com.themasterspirit.flickr.data.api.retrofit.FlickrService
import com.themasterspirit.flickr.data.models.FlickrPhoto
import com.themasterspirit.flickr.data.models.fromResponse
import io.reactivex.Single

class FlickrRepository(private val service: FlickrService) {

    fun search(text: String = ""): Single<List<FlickrPhoto>> {
        return service.search(text = text)
                .map { response ->
                    response.photos.photo
                            .map { it.fromResponse() }
                            .filter { it.originalFormat != null }
                }
    }
}