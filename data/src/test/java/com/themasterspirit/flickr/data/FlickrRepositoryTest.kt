package com.themasterspirit.flickr.data

import com.nhaarman.mockitokotlin2.*
import com.themasterspirit.flickr.data.TestData.photos
import com.themasterspirit.flickr.data.TestData.response
import com.themasterspirit.flickr.data.api.repositories.FlickrRepository
import com.themasterspirit.flickr.data.api.responses.FlickrPhotoListResponse
import com.themasterspirit.flickr.data.api.responses.FlickrPhotoListResponseWrapper
import com.themasterspirit.flickr.data.api.responses.FlickrPhotoResponse
import com.themasterspirit.flickr.data.api.retrofit.FlickrService
import com.themasterspirit.flickr.data.db.FlickrDatabase
import com.themasterspirit.flickr.data.db.models.SearchParams
import com.themasterspirit.flickr.data.db.models.SearchParamsDao
import com.themasterspirit.flickr.data.models.FlickrPhoto
import com.themasterspirit.flickr.data.models.fromResponse
import com.themasterspirit.flickr.data.utils.RxSchedulersOverrideRule
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.util.*

class FlickrRepositoryTest {

    @Rule
    @JvmField
    val testSchedulerRule: TestRule = RxSchedulersOverrideRule()

    private val service: FlickrService = mock()
    private val database: FlickrDatabase = mock()
    private val searchParamsDao: SearchParamsDao = mock()

    private lateinit var repository: FlickrRepository

    @Before
    fun setup() {
        repository = FlickrRepository(service, database)
        whenever(database.searchParamsDao()).doReturn(searchParamsDao)
    }

    @Test
    fun addition_isCorrect() {
        assertNotEquals("👍", "👎")
    }

    @Test
    fun test_search_Success() {
        val query = ""

        whenever(service.search(query)).doReturn(Single.just(response))

        val expectedResult: List<FlickrPhoto> = photos.map { it.fromResponse() }

        repository.search().test().assertValue(expectedResult)
    }

    @Test
    fun test_search_Failure() {
        val query = ""
        val throwable = RuntimeException("error1")

        whenever(service.search(query)).doReturn(Single.error(throwable))

        repository.search().test().assertNotComplete().assertError(throwable)
    }

    @Test
    fun test_saveSearchQuerySilent_Empty() {
        val result: Disposable? = repository.saveSearchQuerySilent("")

        assertNull(result)
        verify(database.searchParamsDao(), never()).insert(any())
    }

    @Test
    fun test_saveSearchQuerySilent_NotEmpty() {
        val query = "not_empty"
        val date = Date()

        val result: Disposable? = repository.saveSearchQuerySilent(query, date)

        assertNotNull(result)
        verify(database.searchParamsDao(), times(1)).insert(SearchParams(query, date))
    }
}

object TestData {
    val response: FlickrPhotoListResponseWrapper by lazy {
        FlickrPhotoListResponseWrapper(FlickrPhotoListResponse(
                page = 1,
                pages = 100,
                perPage = 100,
                total = 1000,
                photo = photos
        ))
    }
    val photos: List<FlickrPhotoResponse> = listOf(
            FlickrPhotoResponse(
                    id = 1,
                    owner = "owner1",
                    secret = "secret1",
                    server = 1,
                    isPublic = 1,
                    isFamily = 0,
                    isFriend = 0,
                    farm = 1,
                    title = "title1",
                    dateUpload = 1,
                    ownerName = "owner name 1",
                    views = "1",
                    tags = "",
                    originalSecret = "originalSecret",
                    originalFormat = "originalFormat"
            ),
            FlickrPhotoResponse(
                    id = 2,
                    owner = "owner2",
                    secret = "secret2",
                    server = 2,
                    isPublic = 1,
                    isFamily = 0,
                    isFriend = 0,
                    farm = 2,
                    title = "title2",
                    dateUpload = 2,
                    ownerName = "owner name 2",
                    views = "2",
                    tags = "",
                    originalSecret = null,
                    originalFormat = null
            )
    )
}
