package com.themasterspirit.flickr.data.db.models

import android.database.Cursor
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

@Entity(tableName = "search_params")
data class SearchParams(
        @PrimaryKey
        @ColumnInfo(name = "query") val query: String,
        @ColumnInfo(name = "date") val date: Date
)

@Dao
interface SearchParamsDao {

    @Query("SELECT * FROM search_params")
    fun get(): Single<List<SearchParams>>

    @Query("SELECT * FROM search_params")
    fun observe(): Flowable<List<SearchParams>>

    @Query("SELECT * FROM search_params WHERE `query` LIKE '%' || :text || '%'")
    fun search(text: String): Single<List<SearchParams>>

    @Query("SELECT * FROM search_params WHERE `query` LIKE '%' || :text || '%'")
    fun searchCursor(text: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg params: SearchParams)

    @Delete
    fun delete(vararg params: SearchParams)
}