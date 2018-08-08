package com.themasterspirit.flickr.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        fun create(context: Context): FlickrDatabase {
            return Room.databaseBuilder(context, FlickrDatabase::class.java, "flickr.db").build()
        }
    }
}