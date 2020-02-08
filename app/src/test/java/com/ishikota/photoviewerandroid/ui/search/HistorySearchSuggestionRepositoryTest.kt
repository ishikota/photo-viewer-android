package com.ishikota.photoviewerandroid.ui.search

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Expect
import com.ishikota.photoviewerandroid.data.PhotoViewerPreferenceImpl
import com.ishikota.photoviewerandroid.di.PreferenceModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

private typealias Item = SearchSuggestionRecyclerViewAdapter.Item

@RunWith(RobolectricTestRunner::class)
class HistorySearchSuggestionRepositoryTest {

    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    private lateinit var repository: HistorySearchSuggestionRepository
    private lateinit var context: Context
    private lateinit var preference: SharedPreferences

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        preference = context.getSharedPreferences(PreferenceModule.PREF_NAME, Context.MODE_PRIVATE)
        repository = HistorySearchSuggestionRepository(PhotoViewerPreferenceImpl(preference), 2)
    }

    @After
    fun tearDown() {
        preference.edit().clear().apply()
    }

    @Test
    fun testGetWhenEmpty() {
        val result = repository.getSuggestions("word1").blockingGet()

        expect.that(result).isEmpty()
    }

    @Test
    fun testRememberAndGet() {
        // given
        repository.rememberQuery("word1")
        repository.rememberQuery("word2")

        // when
        val result = repository.getSuggestions("word").blockingGet()

        // then
        expect.that(result).isEqualTo(listOf(Item("word1"), Item("word2")))
    }

    @Test
    fun testGetOnlyRelatedWord() {
        // given
        repository.rememberQuery("HOGE")
        repository.rememberQuery("FUGA")

        // when
        val result = repository.getSuggestions("hoge").blockingGet()

        // then
        expect.that(result).isEqualTo(listOf(Item("HOGE")))
    }

    @Test
    fun testDropWhenCapacityExceeded() {
        // given
        repository.rememberQuery("word1")
        repository.rememberQuery("word2")
        repository.rememberQuery("word3")

        // when
        val result = repository.getSuggestions("word").blockingGet()

        // then
        expect.that(result).isEqualTo(listOf(Item("word2"), Item("word3")))
    }

    @Test
    fun ignoreDeprecatedEntry() {
        // given
        repository.rememberQuery("word1")
        repository.rememberQuery("word1")
        repository.rememberQuery("word2")

        // when
        val result = repository.getSuggestions("word").blockingGet()

        // then
        expect.that(result).isEqualTo(listOf(Item("word1"), Item("word2")))
    }
}
