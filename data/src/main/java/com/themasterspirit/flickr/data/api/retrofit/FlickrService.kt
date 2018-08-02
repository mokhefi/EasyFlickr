package com.themasterspirit.flickr.data.api.retrofit

import com.themasterspirit.flickr.data.api.responses.FlickrResentPhotoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("/?method=flickr.photos.getRecent")
    fun getRecent(
            @Query("per_page") perPage: Long? = 100,
            @Query("user_id") userId: String? = null
    ): Single<FlickrResentPhotoResponse>

}