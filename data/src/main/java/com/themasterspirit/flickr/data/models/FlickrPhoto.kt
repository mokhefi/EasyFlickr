package com.themasterspirit.flickr.data.models

import com.themasterspirit.flickr.data.api.responses.FlickrPhotoResponse

data class FlickrPhoto(
        val id: Long,
        val owner: String,
        val secret: String,
        val server: Long,
        val farm: Long,
        val title: String,
        val isPublic: Boolean,
        val isFriend: Boolean,
        val isFamily: Boolean
)

fun FlickrPhotoResponse.fromResponse(): FlickrPhoto {
    return FlickrPhoto(
            id = id,
            owner = owner,
            secret = secret,
            server = server,
            farm = farm,
            title = title,
            isPublic = isPublic != 0,
            isFriend = isFriend != 0,
            isFamily = isFamily != 0
    )
}
