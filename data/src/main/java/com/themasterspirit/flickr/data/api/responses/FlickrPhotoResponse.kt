package com.themasterspirit.flickr.data.api.responses

import com.google.gson.annotations.SerializedName

data class FlickrPhotoResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("owner") val owner: String,
        @SerializedName("secret") val secret: String,
        @SerializedName("server") val server: Long,
        @SerializedName("farm") val farm: Long,
        @SerializedName("title") val title: String,
        @SerializedName("ispublic") val isPublic: Int,
        @SerializedName("isfriend") val isFriend: Int,
        @SerializedName("isfamily") val isFamily: Int
)
