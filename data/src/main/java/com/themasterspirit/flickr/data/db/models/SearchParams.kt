package com.themasterspirit.flickr.data.db.models

import android.database.Cursor
import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Single
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(
        tableName = "search_params",
        primaryKeys = ["_id", "query"],
        indices = [Index("query", unique = true)]
)
data class SearchParams(
        @ColumnInfo(name = "query") val query: String,
        @ColumnInfo(name = "date") val date: Date
) {

    @ColumnInfo(name = "_id")
    @NotNull
    var id: Int = Random().nextInt()
}

@Dao
interface SearchParamsDao {

    @Query("SELECT * FROM search_params ORDER BY date DESC")
    fun get(): Single<List<SearchParams>>

    @Query("SELECT * FROM search_params ORDER BY date DESC")
    fun observe(): Flowable<List<SearchParams>>

    @Query("SELECT * FROM search_params WHERE `query` LIKE :text || '%' ORDER BY date DESC")
    fun search(text: String): Single<List<SearchParams>>

    @Query("SELECT * FROM search_params WHERE `query` LIKE :text || '%' ORDER BY date DESC")
    fun searchCursor(text: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg params: SearchParams)

    @Delete
    fun delete(vararg params: SearchParams)
}