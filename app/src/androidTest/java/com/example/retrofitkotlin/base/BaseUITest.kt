package com.example.retrofitkotlin.base

import androidx.test.platform.app.InstrumentationRegistry
import com.example.retrofitkotlin.network.MovieApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.BufferedReader
import java.io.Reader

abstract class BaseUITest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    open fun setUp() {
        starMockServer()
    }

    fun mockNetworkResponseWithFileContent(fileName: String, responseCode: Int) =
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(getJson(fileName))
        )

    private fun getJson(path: String): String {

        var content = ""
        val testContext = InstrumentationRegistry.getInstrumentation().context
        val inputStream = testContext.assets.open(path)
        val reader = BufferedReader(inputStream.reader() as Reader)

        reader.use {
            content = it.readText()
        }
        return content
    }

    private fun starMockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    private fun getMockWebServerUrl() = mockWebServer.url("/").toString()

    fun createApi(): MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(getMockWebServerUrl())
            .build()
            .create(MovieApi::class.java)
    }

    private fun stopMockServer() {
        mockWebServer.shutdown()
    }

    @After
    open fun tearDown() {
        stopMockServer()
    }
}