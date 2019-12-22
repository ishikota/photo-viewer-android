package com.ishikota.photoviewerandroid.infra.okhttp

import com.google.common.truth.Expect
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ApiAccessKeyInterceptorTest {

    @Rule
    @JvmField
    val expect: Expect = Expect.create()

    private lateinit var server: MockWebServer

    @Before
    fun setup() {
        server = MockWebServer()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun intercept() {
        // Given
        val accessKey = "accessKey"
        server = MockWebServer()
        server.enqueue(MockResponse())
        server.start()
        val baseUrl = server.url("v1/test")
        val client = OkHttpClient.Builder().addInterceptor(ApiAccessKeyInterceptor(accessKey)).build()

        // When
        client.newCall(Request.Builder().url(baseUrl).get().build()).execute()
        val request = server.takeRequest()

        // Then
        expect.that(request.getHeader("Authorization")).isEqualTo("Client-ID $accessKey")
    }
}
