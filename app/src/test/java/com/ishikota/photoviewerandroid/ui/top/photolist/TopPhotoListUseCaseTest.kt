package com.ishikota.photoviewerandroid.ui.top.photolist

import com.google.common.truth.Expect
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.sampledata.buildSamplePhoto
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TopPhotoListUseCaseTest {

    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    @Mock
    private lateinit var photoRepository: PhotoRepository

    private lateinit var useCase: TopPhotoListUseCase

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        useCase = TopPhotoListUseCase(photoRepository)
    }

    @Test
    fun loadFirstPage() {
        // Given
        val order = PhotoRepository.Order.POPULAR
        whenever(photoRepository.getPhotos(1, order))
            .thenReturn(Single.just(buildSamplePhotoList(10)))

        // When
        val result = useCase.execute(1, order).blockingGet()

        // Then
        expect.that(result.size).isEqualTo(11)
        expect.that(result[0]).isEqualTo(PhotoListAdapter.Item.Header(order))
    }

    @Test
    fun loadSecondPage() {
        // Given
        val order = PhotoRepository.Order.POPULAR
        whenever(photoRepository.getPhotos(2, order))
            .thenReturn(Single.just(buildSamplePhotoList(10)))

        // When
        val result = useCase.execute(2, order).blockingGet()

        // Then
        expect.that(result.size).isEqualTo(10)
        expect.that(result[0]).isNotInstanceOf(PhotoListAdapter.Item.Header::class.java)
    }

    private fun buildSamplePhotoList(count: Int): List<Photo> = (1..count).toList().map { idx ->
        buildSamplePhoto().copy(id = idx.toString())
    }

}
