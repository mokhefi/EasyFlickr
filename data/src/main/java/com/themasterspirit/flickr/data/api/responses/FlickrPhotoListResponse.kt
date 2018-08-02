package com.themasterspirit.flickr.data.api.responses

import com.google.gson.annotations.SerializedName

data class FlickrPhotoListResponse(
        @SerializedName("page") val page: Long,
        @SerializedName("pages") val pages: Long,
        @SerializedName("perpage") val perPage: Long,
        @SerializedName("total") val total: Long,
        @SerializedName("photo") val photo: List<FlickrPhotoResponse> = listOf()
)