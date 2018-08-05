package com.themasterspirit.flickr.data.models

import com.themasterspirit.flickr.data.api.responses.FlickrPhotoResponse

/*
https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
	or
https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[mstzb].jpg
	or
https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{o-secret}_o.(jpg|gif|png)
 */

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
) {
    val link = "https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg"
    val origin = "https://farm$farm.staticflickr.com/$server/${id}_o-${secret}_o.jpg"
}

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
