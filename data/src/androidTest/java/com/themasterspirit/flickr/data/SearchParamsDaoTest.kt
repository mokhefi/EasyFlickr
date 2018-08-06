package com.themasterspirit.flickr.data

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchParamsDaoTest {

    @BeforeClass
    fun setup() {
        throw RuntimeException("Sorry dude, you won't find any test!")
    }


    @Test
    fun sampleTest() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()

        assertEquals("com.themasterspirit.flickr.data.test", appContext.packageName)
    }
}