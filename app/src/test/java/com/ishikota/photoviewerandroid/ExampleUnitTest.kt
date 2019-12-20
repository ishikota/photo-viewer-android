package com.ishikota.photoviewerandroid

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Expect
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.whenever
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

private interface SomeTestSubject {
    fun fetchItem(context: Context): String
}

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    @Mock
    private lateinit var subject: SomeTestSubject

    private lateinit var context: Context

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        context = ApplicationProvider.getApplicationContext()
    }

    @After
    fun tearDown() {
        // clean up if needed
    }

    @Test
    fun addition_isCorrect() {
        // given
        whenever(subject.fetchItem(any())).thenReturn("mocked")

        // when
        val item = subject.fetchItem(context)

        // then
        expect.that(item).isEqualTo("mocked")
    }
}
