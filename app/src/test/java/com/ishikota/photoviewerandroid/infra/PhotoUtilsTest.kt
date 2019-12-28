package com.ishikota.photoviewerandroid.infra

import com.google.common.truth.Expect
import com.ishikota.photoviewerandroid.sampledata.buildSamplePhoto
import org.junit.Rule
import org.junit.Test

class PhotoUtilsTest {

    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    @Test
    fun calculateViewHeight() {
        // Given
        val photo = buildSamplePhoto()  // width=8192, height=5462
        val viewWidth = 1200

        // when
        val viewHeight = photo.calculateViewHeight(viewWidth)

        // Then
        val expected = 800  // ( 5462 / 8192 ) * 1200 = 800.09...
        expect.that(viewHeight).isEqualTo(expected)
    }

}
