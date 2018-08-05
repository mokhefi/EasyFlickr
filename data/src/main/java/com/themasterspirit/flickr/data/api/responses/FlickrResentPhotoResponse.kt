package com.themasterspirit.flickr.data.api.responses

import com.google.gson.annotations.SerializedName

data class FlickrResentPhotoResponse(
        @SerializedName("photos") val photos: FlickrPhotoListResponse
)