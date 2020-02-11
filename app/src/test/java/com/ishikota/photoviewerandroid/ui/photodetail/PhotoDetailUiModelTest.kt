package com.ishikota.photoviewerandroid.ui.photodetail

import com.google.common.truth.Expect
import com.ishikota.photoviewerandroid.sampledata.buildSamplePhoto
import org.junit.Rule
import org.junit.Test

class PhotoDetailUiModelTest {
    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    @Test
    fun execute() {
        val uiModel = PhotoDetailUiModel(buildSamplePhoto())

        val result = uiModel.toRecyclerViewData()

        expect.that(result.size).isEqualTo(4)
        expect.that(result[0]).isInstanceOf(PhotoDetailAdapter.Item.PhotoItem::class.java)
        expect.that(result[1]).isInstanceOf(PhotoDetailAdapter.Item.DescriptionItem::class.java)
        expect.that(result[2]).isInstanceOf(PhotoDetailAdapter.Item.TagsItem::class.java)
        expect.that(result[3]).isInstanceOf(PhotoDetailAdapter.Item.CollectionItem::class.java)
    }
}
