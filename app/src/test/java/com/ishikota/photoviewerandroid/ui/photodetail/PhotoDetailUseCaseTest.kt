package com.ishikota.photoviewerandroid.ui.photodetail

import com.google.common.truth.Expect
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.sampledata.buildSamplePhoto
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PhotoDetailUseCaseTest {
    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    @Mock
    private lateinit var photoRepository: PhotoRepository

    private lateinit var useCase: PhotoDetailUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCase = PhotoDetailUseCaseImpl(photoRepository)
    }

    @Test
    fun execute() {
        whenever(photoRepository.getPhoto("id"))
            .thenReturn(Single.just(buildSamplePhoto()))

        val result = useCase.execute("id").blockingGet()

        expect.that(result.size).isEqualTo(4)
        expect.that(result[0]).isInstanceOf(PhotoDetailAdapter.Item.PhotoItem::class.java)
        expect.that(result[1]).isInstanceOf(PhotoDetailAdapter.Item.DescriptionItem::class.java)
        expect.that(result[2]).isInstanceOf(PhotoDetailAdapter.Item.TagsItem::class.java)
        expect.that(result[3]).isInstanceOf(PhotoDetailAdapter.Item.CollectionItem::class.java)
    }
}
