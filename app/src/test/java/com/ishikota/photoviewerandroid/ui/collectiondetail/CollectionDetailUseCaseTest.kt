package com.ishikota.photoviewerandroid.ui.collectiondetail

import com.google.common.truth.Expect
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.sampledata.buildSampleCollection
import com.ishikota.photoviewerandroid.sampledata.buildSamplePhoto
import com.ishikota.photoviewerandroid.ui.collectiondeatil.CollectionDetailAdapter
import com.ishikota.photoviewerandroid.ui.collectiondeatil.CollectionDetailUseCase
import com.ishikota.photoviewerandroid.ui.collectiondeatil.CollectionDetailUseCaseImpl
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CollectionDetailUseCaseTest {
    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    @Mock
    private lateinit var collectionRepository: CollectionRepository

    private lateinit var useCase: CollectionDetailUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCase = CollectionDetailUseCaseImpl(collectionRepository)
    }

    @Test
    fun executeForFirstPage() {
        whenever(collectionRepository.getCollection("id"))
            .thenReturn(Single.just(buildSampleCollection()))
        whenever(collectionRepository.getCollectionPhotos("id", 1))
            .thenReturn(Single.just(buildSamplePhotoList(2)))

        val result = useCase.execute("id", 1).blockingGet()

        expect.that(result.size).isEqualTo(3)
        expect.that(result[0]).isInstanceOf(CollectionDetailAdapter.Item.CollectionItem::class.java)
        expect.that(result[1]).isInstanceOf(CollectionDetailAdapter.Item.PhotoItem::class.java)
        expect.that(result[2]).isInstanceOf(CollectionDetailAdapter.Item.PhotoItem::class.java)
    }

    @Test
    fun executeForSecondPage() {
        whenever(collectionRepository.getCollectionPhotos("id", 2))
            .thenReturn(Single.just(buildSamplePhotoList(3)))

        val result = useCase.execute("id", 2).blockingGet()

        expect.that(result.size).isEqualTo(3)
        expect.that(result[0]).isInstanceOf(CollectionDetailAdapter.Item.PhotoItem::class.java)
        expect.that(result[1]).isInstanceOf(CollectionDetailAdapter.Item.PhotoItem::class.java)
        expect.that(result[2]).isInstanceOf(CollectionDetailAdapter.Item.PhotoItem::class.java)
    }

    private fun buildSamplePhotoList(count: Int): List<Photo> = (1..count).toList().map { idx ->
        buildSamplePhoto().copy(id = idx.toString())
    }
}
