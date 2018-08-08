package com.themasterspirit.flickr.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.themasterspirit.flickr.data.db.FlickrDatabase
import com.themasterspirit.flickr.data.db.models.SearchParams
import com.themasterspirit.flickr.data.db.models.SearchParamsDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class SearchParamsDaoTest {

    private val queryFirst = "query"
    private val querySecond = "sample text"

    private val initialData: List<SearchParams> = listOf(
            SearchParams("$queryFirst 0", Date()),
            SearchParams("$queryFirst 1", Date()),
            SearchParams("$querySecond 1", Date()),
            SearchParams("$querySecond 0", Date())
    ).sortedByDescending { it.date.time }

    private lateinit var database: FlickrDatabase

    private val dao: SearchParamsDao
        get() = database.searchParamsDao()

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, FlickrDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun testWrite() {
        dao.insert(*initialData.toTypedArray())
        dao.get().test()
                .assertValue(initialData)
                .assertNoErrors()
    }

    @Test
    fun testUniqueQuery() {
        val query = "query_text"
        val items = listOf(SearchParams(query, Date()), SearchParams(query, Date()))
        dao.insert(*items.toTypedArray())
        dao.get().test()
                .assertValue(listOf(items.first()))
                .assertNoErrors()
    }

    @Test
    fun testDeleting() {
        val itemsToDelete = listOf(initialData[0], initialData[2])
        val itemsLeft = listOf(initialData[1], initialData[3])

        dao.insert(*initialData.toTypedArray())
        dao.delete(*itemsToDelete.toTypedArray())

        dao.get().test().assertValue(itemsLeft)

        val queryToDelete = itemsLeft.first().query
        val expectedResult = listOf(itemsLeft.last())

        val deletedItemCount = dao.deleteByQuery(queryToDelete)

        dao.get().test().assertValue(expectedResult)

        assertEquals(deletedItemCount, 1)
    }

    @Test
    fun testSearch() {
        val items = listOf(
                SearchParams("text1", Date()),          // 0
                SearchParams("text2", Date()),          // 1
                SearchParams("sample text 1", Date()),  // 2
                SearchParams("sample text 2", Date()),  // 3
                SearchParams("query 1", Date()),        // 4
                SearchParams("query 2", Date()),        // 5
                SearchParams("Hello World!", Date())    // 6
        )
        dao.insert(*items.toTypedArray())

        dao.search("text").test().assertValue(items.subList(0, 2).sortedByDescending { it.date.time })
        dao.search("t").test().assertValue(items.subList(0, 2).sortedByDescending { it.date.time })
        dao.search("sample").test().assertValue(items.subList(2, 4).sortedByDescending { it.date.time })
        dao.search("Hello World!").test().assertValue(listOf(items.last()))
    }
}