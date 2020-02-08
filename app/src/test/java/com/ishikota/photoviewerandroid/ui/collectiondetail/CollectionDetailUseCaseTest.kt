package com.ishikota.photoviewerandroid.ui.collectiondetail

import com.google.common.truth.Expect
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
    fun insertRelatedCollectionsAtFirstPage() {
        whenever(collectionRepository.getCollectionPhotos("id", 1))
            .thenReturn(Single.just(buildSamplePhotos(10)))
        whenever(collectionRepository.getCollectionsRelatedCollections("id"))
            .thenReturn(Single.just(buildSampleCollections(3)))

        val result = useCase.execute("id", 1).blockingGet()

        expect.that(result.size).isEqualTo(11)
        expect.that(result[3]).isInstanceOf(CollectionDetailAdapter.Item.RelatedCollections::class.java)
    }

    @Test
    fun doNotInsertRelatedCollectionsWhenPaging() {
        whenever(collectionRepository.getCollectionPhotos("id", 2))
            .thenReturn(Single.just(buildSamplePhotos(10)))
        whenever(collectionRepository.getCollectionsRelatedCollections("id"))
            .thenReturn(Single.just(buildSampleCollections(3)))

        val result = useCase.execute("id", 2).blockingGet()

        // Check if result does not contain RelatedCollections item
        expect.that(result.any { it is CollectionDetailAdapter.Item.RelatedCollections }).isFalse()
    }

    @Test
    fun doNotInsertRelatedCollectionsWhenItemsAreShort() {
        whenever(collectionRepository.getCollectionPhotos("id", 1))
            .thenReturn(Single.just(buildSamplePhotos(1)))
        whenever(collectionRepository.getCollectionsRelatedCollections("id"))
            .thenReturn(Single.just(buildSampleCollections(3)))

        val result = useCase.execute("id", 1).blockingGet()

        // Check if result does not contain RelatedCollections item
        expect.that(result.any { it is CollectionDetailAdapter.Item.RelatedCollections }).isFalse()
    }

    @Test
    fun doNotInsertRelatedCollectionsWhenRelatedCollectionsAreShort() {
        whenever(collectionRepository.getCollectionPhotos("id", 1))
            .thenReturn(Single.just(buildSamplePhotos(10)))
        whenever(collectionRepository.getCollectionsRelatedCollections("id"))
            .thenReturn(Single.just(buildSampleCollections(2)))

        val result = useCase.execute("id", 1).blockingGet()

        // Check if result does not contain RelatedCollections item
        expect.that(result.any { it is CollectionDetailAdapter.Item.RelatedCollections }).isFalse()
    }

    private fun buildSamplePhotos(count: Int) = (1..count).toList().map { idx ->
        buildSamplePhoto()
    }

    private fun  buildSampleCollections(count: Int) = (1..count).toList().map { idx ->
        buildSampleCollection()
    }
}
