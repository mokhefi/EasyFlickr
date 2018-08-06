package com.themasterspirit.flickr.data.api.retrofit

import com.themasterspirit.flickr.data.api.responses.FlickrResentPhotoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("/services/rest/?method=flickr.photos.getRecent&extras=views,date_upload,owner_name,original_format,tags,url_o")
    fun getRecent(
            @Query("per_page") perPage: Long? = 100,
            @Query("user_id") userId: String? = null
    ): Single<FlickrResentPhotoResponse>

}