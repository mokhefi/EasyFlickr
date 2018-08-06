package com.themasterspirit.flickr.data.models

import android.os.Parcelable
import com.themasterspirit.flickr.data.api.responses.FlickrPhotoResponse
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class FlickrPhoto(
        val id: Long,
        val owner: String,
        val secret: String,
        val server: Long,
        val farm: Long,
        val title: String,
        val isPublic: Boolean,
        val isFriend: Boolean,
        val isFamily: Boolean,
        val dateUpload: Date,
        val ownerName: String,
        val views: String,
        val tags: String,
        val originalSecret: String?,
        val originalFormat: String?
) : Parcelable {

    fun link(size: Size = Size.DEFAULT): String {
        val suffix = size.suffix()?.let { suffix ->
            if (originalSecret == null || originalFormat == null) "_z" else "_$suffix"
        } ?: ""

        return if (size == Size.ORIGIN && originalSecret != null && originalFormat != null) {
            "https://farm$farm.staticflickr.com/$server/${id}_${originalSecret}_o.$originalFormat"
        } else {
            "https://farm$farm.staticflickr.com/$server/${id}_$secret$suffix.$defaultFormat"
        }
    }

    companion object {

        const val TAG = "FlickrPhoto"

        const val defaultFormat: String = "jpg"

        /**
         * Size Suffixes
         *
         * The letter suffixes are as follows:
         *
         * s	small square 75x75
         *
         * q	large square 150x150
         *
         * t	thumbnail, 100 on longest side
         *
         * m	small, 240 on longest side
         *
         * n	small, 320 on longest side
         *
         * (-)  medium, 500 on longest side
         *
         * z	medium 640, 640 on longest side
         *
         * c	medium 800, 800 on longest side†
         *
         * b	large, 1024 on longest side*
         *
         * h	large 1600, 1600 on longest side†
         *
         * k	large 2048, 2048 on longest side†
         *
         * o	original image, either a jpg, gif or png, depending on source format
         *
         * .....
         *
         * (*) Before May 25th 2010 large photos only exist for very large original images.
         *
         * (†) Medium 800, large 1600, and large 2048 photos only exist after March 1st 2012.
         */
        enum class Size(private val suffix: String?) {
            /**
             * small square 75x75
             */
            THUMBNAIL_S("s"),
            /**
             * large square 150x150
             */
            THUMBNAIL_Q("q"),
            /**
             * thumbnail, 100 on longest side
             */
            THUMBNAIL("t"),
            /**
             * medium, 500 on longest side
             */
            DEFAULT(null),
            /**
             * medium 640, 640 on longest side
             */
            MEDIUM("z"),
            /**
             * original image, either a jpg, gif or png, depending on source format
             */
            ORIGIN("o");

            // c, b, h, k

            fun suffix(): String? {
                return suffix
            }

//            fun next(expected: Size): Size? {
//                if (expected == this) return null
//                return when (this) {
//                    THUMBNAIL_S -> THUMBNAIL_Q
//                    THUMBNAIL_Q -> DEFAULT
//                    THUMBNAIL -> DEFAULT
//                    DEFAULT -> MEDIUM
//                    MEDIUM -> ORIGIN
//                    ORIGIN -> null
//                }
//            }
        }
    }
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
            isFamily = isFamily != 0,
            dateUpload = Date(dateUpload),
            ownerName = ownerName,
            views = views,
            tags = tags,
            originalSecret = originalSecret,
            originalFormat = originalFormat
    )
}
