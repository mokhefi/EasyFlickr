package com.themasterspirit.flickr.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.themasterspirit.flickr.data.db.models.SearchParams
import com.themasterspirit.flickr.data.db.models.SearchParamsDao

@Database(
        version = 1,
        exportSchema = false,
        entities = [SearchParams::class]
)
@TypeConverters(DatabaseConverters::class)
abstract class FlickrDatabase : RoomDatabase() {

    abstract fun searchParamsDao(): SearchParamsDao
}